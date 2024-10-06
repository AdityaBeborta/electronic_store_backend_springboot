package com.electronicstore.repositories;

import com.electronicstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    @Query(value = "select sum(ci.quantity) from cart_item ci inner join cart c on c.cart_id = ci.cart_id where ci.cart_id = :cartId",nativeQuery = true)
    int countItemsInCart(@Param("cartId") String cartId);

}