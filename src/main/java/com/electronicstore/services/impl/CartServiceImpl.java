package com.electronicstore.services.impl;

import com.electronicstore.dtos.CartDto;
import com.electronicstore.dtos.CartItemDto;
import com.electronicstore.entities.Cart;
import com.electronicstore.entities.CartItem;
import com.electronicstore.entities.Product;
import com.electronicstore.entities.User;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.helper.CartItemDetails;
import com.electronicstore.helper.CartItemRequest;
import com.electronicstore.repositories.CartItemRepository;
import com.electronicstore.repositories.CartRepository;
import com.electronicstore.repositories.ProductRepository;
import com.electronicstore.repositories.UserRepository;
import com.electronicstore.services.CartService;
import com.electronicstore.services.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemsToCart(String userId, String productId, int quantity) {
        AtomicReference<Product> product = new AtomicReference<>(this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "product id", productId)));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        AtomicBoolean updated = new AtomicBoolean(false);
        //check if the required quantity exists for the product the user is requesting for
        if (product.get().getQuantity() < quantity) {
            throw new BadApiRequest("out of stock");
        }
        product.set(this.modelMapper.map(this.productService.updateProductQuantityByProductId(productId, quantity, ApplicationConstants.ADD_ITEM_TO_CART), Product.class));
        Cart cart = user.getCart();
        if (cart == null) {
            logger.info("cart does not exist");
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }
        List<CartItem> cartItems = cart.getCartItems();
        //check if the product already exist
        cartItems.forEach(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPriceAsPerQuantity(item.getQuantity() * product.get().getPrice());
                updated.set(true);
            }
        });

        if (!updated.get()) {
            logger.info("add new items");
            CartItem cartItem = CartItem.builder()
                    .product(product.get())
                    .cart(cart)
                    .quantity(quantity)
                    .totalPriceAsPerQuantity(quantity * product.get().getPrice())
                    .build();
            cart.getCartItems().add(cartItem);
        }
        //calculate total amount for all the items and number of items present in the cart
        CartItemDetails cartDetails = getCartDetails(cart);
        cart.setUser(user);
        cart.setTotalCartPrice(cartDetails.getTotalPrice());
        cart.setTotalNumberOfItemsInCart(cartDetails.getTotalQuantity());
        Cart updatedCart = cartRepository.save(this.modelMapper.map(cart, Cart.class));
        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public CartDto removeItemFromCart(String userId, CartItemRequest cartItemRequest) {
        //get the product id and product quantity from the request
        String productId = cartItemRequest.getProductId();
        int requestedQuantity = cartItemRequest.getQuantity();
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "product id", productId));
        //if request quantity is less than zero or negative or equal to zero throw exception
        if (requestedQuantity <= 0) {
            throw new BadApiRequest("Quantity must be greater than zero");
        }
        /*find user from user find the cart from cart find the cartItemId if*/
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        //now get the cart from the user
        Cart cart = user.getCart();
        //check if the user already have a cart -- if not then throw exception
        if (cart == null) {
            throw new ResourceNotFoundException("cart not found", "user id", userId);
        }
        //at this step it means that the user have a cart so from the cart get the items
        List<CartItem> existingItemsInUsersCart = cart.getCartItems();
        //now check if that product which you want to remove exists in the user's cart or not
        CartItem singleItemInTheCart = existingItemsInUsersCart.stream().filter(item -> item.getProduct().getProductId().equals(productId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("product ", "product id", productId));
        //now we got the product which we want to remove from the list of products
        //so, we need to check what is the quantity if more than one we need to reduce with the request quantity
        if (requestedQuantity > singleItemInTheCart.getQuantity()) {
            throw new BadApiRequest("Request quantity to be removed is more than the quantity present in the cart");
        }
        this.modelMapper.map(this.productService.updateProductQuantityByProductId(productId, requestedQuantity, ApplicationConstants.REMOVE_ITEM_FROM_CART), Product.class);
        if (singleItemInTheCart.getQuantity() <= requestedQuantity) {
            //it means that the user wants to remove the entire product
            //ex suppose I have iphone13 with quantity as 4 so now the requested quantity is 5 so, it will remove entirely
            existingItemsInUsersCart.remove(singleItemInTheCart);
            singleItemInTheCart.setTotalPriceAsPerQuantity(0);
            //now delete it from the database even
            cartItemRepository.deleteById(singleItemInTheCart.getCartItemId());
        } else {
            singleItemInTheCart.setQuantity(singleItemInTheCart.getQuantity() - requestedQuantity);
            singleItemInTheCart.setTotalPriceAsPerQuantity(singleItemInTheCart.getQuantity() * singleItemInTheCart.getProduct().getPrice());
        }
        //now calculate the updated quantity and price

        cart.setCartItems(existingItemsInUsersCart);
        CartItemDetails cartDetails = getCartDetails(cart);
        cart.setTotalNumberOfItemsInCart(cartDetails.getTotalQuantity());
        cart.setTotalCartPrice(cartDetails.getTotalPrice());
        Cart updatedCart = this.cartRepository.save(cart);
        return this.modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public CartDto removeAllItemsFromCart(String userId) {
        /*try to find the user whose cart you want to clear
         * from that user get the list of cart item
         * check if the cart have some item it means check the size of cart item it should be greater than zero
         * traverse through the cart item and based on product id reduce the items quantity and add it to respective product
         * */
        //find the user from user id and if user doesn't exist throw an exception
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        //now get the cart
        Cart cart = user.getCart();
        //check if cart is empty
        if (cart == null) {
            throw new BadApiRequest("cart is not present for this user");
        }
        if (cart.getCartItems().isEmpty()) {
            throw new BadApiRequest("cart is already empty");
        }
        //create a list where we need to store items id which we need to remove from the cart
        List<Integer> cartItemIdsToDelete = new ArrayList<>();
        //create a list of product whose quantity need to be updated
        List<Product> productToBeUpdated = new ArrayList<>();
        //get the list of items from the cart
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem productPresentInsideTheCart : cartItems) {
            //get the cart item id's which needs to be removed
            int cartItemId = productPresentInsideTheCart.getCartItemId();
            //get the product which needs to be updated
            Product product = productPresentInsideTheCart.getProduct();
            //create a variable to store the updated the quantity
            int updatedQuantity = product.getQuantity() + productPresentInsideTheCart.getQuantity();
            product.setQuantity(updatedQuantity);
            cartItemIdsToDelete.add(cartItemId);
            productToBeUpdated.add(product);
        }
        cartItemRepository.deleteAllById(cartItemIdsToDelete);
        productRepository.saveAll(productToBeUpdated);
        //clear the cart
        cart.getCartItems().clear();
        cart.setTotalCartPrice(0);
        cart.setTotalNumberOfItemsInCart(0);
        Cart newCart = cartRepository.save(cart);
        return modelMapper.map(newCart, CartDto.class);
    }

    @Override
    public CartDto getCartByUserId(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        //get the cart from user
        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("cart ", "user id", userId);
        }
        return this.modelMapper.map(cart, CartDto.class);
    }

    public CartItemDetails getCartDetails(Cart cart) {
        //iterate and calculate the total price
        double totalPrice = cart.getCartItems().stream().mapToDouble(CartItem::getTotalPriceAsPerQuantity).sum();
        //get total count
        System.out.println("total cart price " + totalPrice);
        int sum = (int) cart.getCartItems().stream().filter(cartItem -> (cartItem.getCart().getCartId()).equals(cart.getCartId())).mapToDouble(CartItem::getQuantity).sum();
        return CartItemDetails.builder().totalPrice(totalPrice).totalQuantity(sum).build();
    }
}
