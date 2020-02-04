/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.entities;

import com.wcl.pricebasket.testutils.MoneyTestUtils;
import com.wcl.pricebasket.utils.MonetaryUtils;
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
        assertEquals(MonetaryUtils.gbpAmount(1.0), MonetaryUtils.gbpAmount(Product.APPLES.getCostPerUnit()));
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.8), MonetaryUtils.gbpAmount(Product.BREAD.getCostPerUnit()));
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(1.3), MonetaryUtils.gbpAmount(Product.MILK.getCostPerUnit()));
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.65), MonetaryUtils.gbpAmount(Product.SOUP.getCostPerUnit()));
    }
}
