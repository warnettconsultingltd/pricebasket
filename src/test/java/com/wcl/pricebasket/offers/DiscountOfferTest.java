/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.offers;

import com.wcl.pricebasket.entities.Product;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountOfferTest {
    /*
     * Test DiscountOffer uses a predicate check that an entry must be in the Basket map; the discount function merely
     * multiplies the number of key-value entries by 5.
     */
    private final DiscountOffer testSubject = new DiscountOffer("Test Offer Description",
                                                                 p -> p.size() > 0,
                                                                 p -> p.size() * 5.00);


    @Test
    @DisplayName("Check that the description is correctly returned")
    public void checkThatTheDescriptionIsCorrectlyReturned() {
        assertEquals("Test Offer Description", testSubject.getDescription());
    }

    @Test
    @DisplayName("Check that the condition check passes correctly")
    public void checkThatTheConditionCheckPassesCorrectly() {
        final Map<Product, Long> testBasket = new HashMap<>();
        testBasket.put(Product.MILK, 8L);
        assertTrue(testSubject.isOfferApplicable(testBasket));
    }

    @Test
    @DisplayName("Check that the condition check fails correctly")
    public void checkThatTheConditionCheckFailsCorrectly() {
        final Map<Product, Long> testBasket = new HashMap<>();
        assertFalse(testSubject.isOfferApplicable(testBasket));
    }

    @Test
    @DisplayName("Check that the discount calculation works correctly")
    public void checkThatTheDiscountCalculationWorksCorrectly() {
        final Map<Product, Long> testBasket = new HashMap<>();
        testBasket.put(Product.MILK, 2L);
        testBasket.put(Product.APPLES, 7L);
        assertEquals(10.00, testSubject.calculateDiscountToApply(testBasket));
    }
}
