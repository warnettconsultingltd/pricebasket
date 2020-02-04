/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.receipt;

import com.wcl.pricebasket.entities.Product;
import com.wcl.pricebasket.offers.DiscountOffer;
import com.wcl.pricebasket.testutils.MoneyTestUtils;
import com.wcl.pricebasket.utils.MonetaryUtils;
import org.apache.commons.collections4.CollectionUtils;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiptGeneratorTest {


    @Test
    @DisplayName("Check receipt generated correctly when no available offers")
    public void checkReceiptGeneratedCorrectlyWhenNoAvailableOffers() {
        final ReceiptGenerator receiptGenerator = new ReceiptGenerator(new ArrayList<>());
        final Map<Product, Long> basket = new HashMap<>();
        basket.put(Product.MILK, 5L);
        basket.put(Product.APPLES, 3L);

        final Receipt receipt = receiptGenerator.generateReceipt(basket);
        assertEquals(MonetaryUtils.gbpAmount(9.5), receipt.getSubtotal());
        assertTrue(CollectionUtils.isEmpty(receipt.getAppliedOffers()));
        assertEquals(MonetaryUtils.gbpAmount(9.5), receipt.getFinalTotal());
    }

    @Test
    @DisplayName("Check receipt generated correctly when no matching offer")
    public void checkReceiptGeneratedCorrectlyWhenNoMatchingOffer() {
        final List<DiscountOffer> offers = new ArrayList<>();

        offers.add(new DiscountOffer("Apples half price Offer",
                p -> false,
                p -> MonetaryUtils.gbpAmount(p.get(Product.APPLES) * 0.5)));


        final ReceiptGenerator receiptGenerator = new ReceiptGenerator(offers);
        final Map<Product, Long> basket = new HashMap<>();
        basket.put(Product.MILK, 1L);

        final Receipt receipt = receiptGenerator.generateReceipt(basket);
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(1.3),  receipt.getSubtotal());

        assertEquals(0, receipt.getAppliedOffers().size());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(1.3),  receipt.getFinalTotal());

    }

    @Test
    @DisplayName("Check receipt generated correctly when single available offer")
    public void checkReceiptGeneratedCorrectlyWhenSingleAvailableOffer() {
        final List<DiscountOffer> offers = new ArrayList<>();

        offers.add(new DiscountOffer("Apples half price Offer",
                                      p -> true,
                                      p -> MonetaryUtils.gbpAmount(p.get(Product.APPLES) * 0.5)));


        final ReceiptGenerator receiptGenerator = new ReceiptGenerator(offers);
        final Map<Product, Long> basket = new HashMap<>();
        basket.put(Product.APPLES, 6L);

        final Receipt receipt = receiptGenerator.generateReceipt(basket);
        assertEquals(MonetaryUtils.gbpAmount(6.0), receipt.getSubtotal());
        assertEquals(1, receipt.getAppliedOffers().size());
        assertEquals("Apples half price Offer", receipt.getAppliedOffers().get(0).getDescription());
        assertEquals(MonetaryUtils.gbpAmount(3.0), receipt.getFinalTotal());
    }


    @Test
    @DisplayName("Check receipt generated correctly when multiple available offers")
    public void checkReceiptGeneratedCorrectlyWhenMultipleAvailableOffers() {
        final List<DiscountOffer> offers = new ArrayList<>();

        offers.add(new DiscountOffer("Apples 25% off",
                p -> true,
                p -> MonetaryUtils.gbpAmount(p.get(Product.APPLES) * 0.25)));

        offers.add(new DiscountOffer("Milk 40% off",
                p -> true,
                p -> MonetaryUtils.gbpAmount(p.get(Product.MILK) * 0.40)));

        final ReceiptGenerator receiptGenerator = new ReceiptGenerator(offers);
        final Map<Product, Long> basket = new HashMap<>();
        basket.put(Product.APPLES, 4L);
        basket.put(Product.MILK, 10L);

        final Receipt receipt = receiptGenerator.generateReceipt(basket);
        assertEquals(MonetaryUtils.gbpAmount(17.0), receipt.getSubtotal());
        assertEquals(2, receipt.getAppliedOffers().size());
        assertEquals("Apples 25% off", receipt.getAppliedOffers().get(0).getDescription());
        assertEquals("Milk 40% off", receipt.getAppliedOffers().get(1).getDescription());
        assertEquals(MonetaryUtils.gbpAmount(12.0), receipt.getFinalTotal());
    }

}
