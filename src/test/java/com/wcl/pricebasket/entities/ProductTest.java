/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.entities;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {
    @Test
    @DisplayName("Checks that the Product display names are correctly defined")
    public void checkThatTheProductNamesAreCorrectlyDefined() {
        assertEquals("Apples", Product.APPLES.getProductName());
        assertEquals("Bread", Product.BREAD.getProductName());
        assertEquals("Milk", Product.MILK.getProductName());
        assertEquals("Soup", Product.SOUP.getProductName());
    }

    @Test
    @DisplayName("Checks that the Product cost per units are correctly defined")
    public void checkThatTheProductCostPerUnitsAreCorrectlyDefined() {
        assertEquals(1.0f, Product.APPLES.getCostPerUnit());
        assertEquals(0.8f, Product.BREAD.getCostPerUnit());
        assertEquals(1.3f, Product.MILK.getCostPerUnit());
        assertEquals(0.65f, Product.SOUP.getCostPerUnit());
    }

}
