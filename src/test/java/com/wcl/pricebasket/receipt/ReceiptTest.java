/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
 package com.wcl.pricebasket.receipt;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {
    @Test
    @DisplayName("Check that a receipt with no applicable offers generates correctly")
    public void checkThatAReceiptWithNoOffersGeneratesCorrectly() {
        final Receipt receipt = Receipt.builder().subtotal(5.65)
                .appliedOffers(new ArrayList<>())
                .finalTotal(5.65)
                .build();

        String expectedReceiptText = "Subtotal: £5.65" +
                System.lineSeparator() +
                "(no offers available)" +
                System.lineSeparator() +
                "Total: £5.65";
        assertEquals(expectedReceiptText, receipt.toString());
    }


    @Test
    @DisplayName("Check that a receipt with a single applied offer generates correctly")
    public void checkThatAReceiptWithASingleAppliedOfferGeneratesCorrectly() {
        final List<AppliedOffer> offers = new ArrayList<>();
        offers.add(AppliedOffer.builder().description("Wibbles 70% off")
                                         .discountAmount(0.7)
                                         .build());
        final Receipt receipt = Receipt.builder().subtotal(1.00)
                .appliedOffers(offers)
                .finalTotal(0.30)
                .build();

        String expectedReceiptText = "Subtotal: £1.00" +
                System.lineSeparator() +
                "Wibbles 70% off: -70p" +
                System.lineSeparator() +
                "Total: £0.30";
        assertEquals(expectedReceiptText, receipt.toString());
    }

    @Test
    @DisplayName("Check that a receipt with multiple applied offers generates correctly")
    public void checkThatAReceiptWitMultipleAppliedOffersGeneratesCorrectly() {
        final List<AppliedOffer> offers = new ArrayList<>();
        offers.add(AppliedOffer.builder().description("Wobbles 75% off")
                .discountAmount(0.75)
                .build());
        offers.add(AppliedOffer.builder().description("Weebles 50% off")
                .discountAmount(3.00)
                .build());
        final Receipt receipt = Receipt.builder().subtotal(10.00)
                .appliedOffers(offers)
                .finalTotal(6.25)
                .build();

        String expectedReceiptText = "Subtotal: £10.00" +
                System.lineSeparator() +
                "Wobbles 75% off: -75p" +
                System.lineSeparator() +
                "Weebles 50% off: -£3.00" +
                System.lineSeparator() +
                "Total: £6.25";
        assertEquals(expectedReceiptText, receipt.toString());
    }
}
