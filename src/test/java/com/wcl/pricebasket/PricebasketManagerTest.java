/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket;

import com.wcl.pricebasket.entities.Product;
import com.wcl.pricebasket.offers.DiscountOffer;
import com.wcl.pricebasket.parser.PricebasketParser;
import com.wcl.pricebasket.receipt.AppliedOffer;
import com.wcl.pricebasket.receipt.Receipt;
import com.wcl.pricebasket.receipt.ReceiptGenerator;
import com.wcl.pricebasket.testutils.MoneyTestUtils;
import com.wcl.pricebasket.utils.MonetaryUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PricebasketManagerTest {
    private PricebasketManager testSubject= new PricebasketManager(new PricebasketParser(),
                                                                   new ReceiptGenerator(constructCurrentOffers()));

    @Test
    @DisplayName("Checks an appropriate error message displayed for no input")
    public void checkErrorMessageDisplayedWhenNoTextEntered() {
        checkErrorMessageDisplayedWhenNothingTyped(null);
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for blank input")
    public void checkErrorMessageDisplayedWhenBlankTextEntered() {
        checkErrorMessageDisplayedWhenNothingTyped("");
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for only spaces")
    public void checkErrorMessageDisplayedWhenOnlySpaceEntered() {
        checkErrorMessageDisplayedWhenNothingTyped(" ");
    }

    private void checkErrorMessageDisplayedWhenNothingTyped(final String text) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testSubject.generateShoppingReceipt(text));
        assertEquals("Nothing was entered.",
                exception.getMessage());
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for when input does not start with Pricebasket")
    public void checkErrorMessageDisplayedWhenPricebasketIsMissing() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testSubject.generateShoppingReceipt("Apples Bread Milk"));
        assertEquals("Pricebasket was not found at the start.",
                exception.getMessage());
    }

    @Test
    public void checkErrorMessageDisplayedWhenNoItemsProvided() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testSubject.generateShoppingReceipt("Pricebasket"));
        assertEquals("No products to purchase were entered.",
                exception.getMessage());
    }

    @Test
    @DisplayName("Checks a single Apples offer correctly applied")
    public void checkASingleApplesOfferCorrectlyApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Apples Milk Bread");

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(3.1), receipt.getSubtotal());

        assertEquals(1, receipt.getAppliedOffers().size());
        final AppliedOffer appliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Apples 10% off", appliedOffer.getDescription());

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.1), appliedOffer.getDiscountAmount());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(3), receipt.getFinalTotal());
    }

    @Test
    @DisplayName("Checks a single Bread offer correctly applied")
    public void checkASingleBreadOfferCorrectlyApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Bread Soup Soup Milk");
        System.out.println(MonetaryUtils.gbpAmount(3.4));
        System.out.println(receipt.getSubtotal());

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(3.4),  receipt.getSubtotal());

        assertEquals(1, receipt.getAppliedOffers().size());
        final AppliedOffer appliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Bread half price if 2 tins of soup bought", appliedOffer.getDescription());

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.4),  appliedOffer.getDiscountAmount());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(3),  receipt.getFinalTotal());
    }

    @Test
    @DisplayName("Checks  Bread and Apples offers correctly applied")
    public void checkBreadAndApplesOffersCorrectlyApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Bread Soup Soup Milk Apples");

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(4.4), receipt.getSubtotal());

        assertEquals(2, receipt.getAppliedOffers().size());

        final AppliedOffer applesAppliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Apples 10% off", applesAppliedOffer.getDescription());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.1), applesAppliedOffer.getDiscountAmount());

        final AppliedOffer breadAppliedOffer = receipt.getAppliedOffers().get(1);
        assertEquals("Bread half price if 2 tins of soup bought", breadAppliedOffer.getDescription());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(0.4), breadAppliedOffer.getDiscountAmount());

        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(3.9), receipt.getFinalTotal());
    }

    @Test
    @DisplayName("Checks scenario when no offer applied")
    public void checkNoOfferApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Milk");
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(1.3), receipt.getSubtotal());
        assertEquals(0, receipt.getAppliedOffers().size());
        MoneyTestUtils.assertMoneyValuesEquals(MonetaryUtils.gbpAmount(1.3), receipt.getFinalTotal());
    }

    /*
     * Generate current offers; in real world app, would use a different structure, ie possibly reading in and
     * transforming JSON from an external API call.
     *
     * For purposes of this simple code exercise, hard-coded the current offers.
     */
    private List<DiscountOffer> constructCurrentOffers() {
        final List<DiscountOffer> currentOffers = new ArrayList<>();

        currentOffers.add(new DiscountOffer("Apples 10% off",
                p -> p.containsKey(Product.APPLES),
                p -> MonetaryUtils.gbpAmount(0.1 * p.get(Product.APPLES))));

        currentOffers.add(new DiscountOffer("Bread half price if 2 tins of soup bought",
                p -> p.getOrDefault(Product.SOUP, 0L) >= 2
                        && p.containsKey(Product.BREAD),
                p -> {
                    long countOfSoups = p.getOrDefault(Product.SOUP, 0L);

                    int maximumNumberOfBreadsEligibleForOffer = (int)(countOfSoups / 2);
                    long numberOfBreadsEligibleForOffer = Math.min(p.get(Product.BREAD),
                            maximumNumberOfBreadsEligibleForOffer);
                    return MonetaryUtils.gbpAmount(0.5 * Product.BREAD.getCostPerUnit() * numberOfBreadsEligibleForOffer);
                }));

        return currentOffers;
    }
}
