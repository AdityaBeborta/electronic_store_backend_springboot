package com.electronicstore.services.impl;

import com.electronicstore.dtos.TransactionDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDetails {


    public List<TransactionDTO> getTransactionDetailsFromAccountNumber(String accountNumber) {
        TransactionDTO transactionDetails1 = TransactionDTO.builder().accountNumber("12345").transactionAmount(12).transactionDate(new Date()).build();
        TransactionDTO transactionDetails2 = TransactionDTO.builder().accountNumber("12345").transactionAmount(24).transactionDate(new Date(21 - 11 - 2024)).build();
        TransactionDTO transactionDetails3 = TransactionDTO.builder().accountNumber("100").transactionAmount(36).transactionDate(new Date(20 - 11 - 2024)).build();
        List<TransactionDTO> allTransactionDetails = List.of(transactionDetails1, transactionDetails2, transactionDetails3);
        List<TransactionDTO> collect = allTransactionDetails.stream().filter(transactionDTO -> transactionDTO.getAccountNumber().equals(accountNumber)).collect(Collectors.toList());
        return collect;
    }
}
