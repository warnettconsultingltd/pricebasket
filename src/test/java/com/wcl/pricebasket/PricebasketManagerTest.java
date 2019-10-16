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
        assertEquals(3.10, receipt.getSubtotal(), 0.01);
        assertEquals(1, receipt.getAppliedOffers().size());
        final AppliedOffer appliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Apples 10% off", appliedOffer.getDescription());
        assertEquals(0.10, appliedOffer.getDiscountAmount());
        assertEquals(3.00, receipt.getFinalTotal(), 0.01);
    }

    @Test
    @DisplayName("Checks a single Bread offer correctly applied")
    public void checkASingleBreadOfferCorrectlyApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Bread Soup Soup Milk");
        assertEquals(3.40, receipt.getSubtotal(), 0.01);
        assertEquals(1, receipt.getAppliedOffers().size());
        final AppliedOffer appliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Bread half price if 2 tins of soup bought", appliedOffer.getDescription());
        assertEquals(0.40, appliedOffer.getDiscountAmount(), 0.01);
        assertEquals(3.00, receipt.getFinalTotal(), 0.01);
    }

    @Test
    @DisplayName("Checks  Bread and Apples offers correctly applied")
    public void checkBreadAndApplesOffersCorrectlyApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Bread Soup Soup Milk Apples");
        assertEquals(4.40, receipt.getSubtotal(), 0.01);
        assertEquals(2, receipt.getAppliedOffers().size());

        final AppliedOffer applesAppliedOffer = receipt.getAppliedOffers().get(0);
        assertEquals("Apples 10% off", applesAppliedOffer.getDescription());
        assertEquals(0.10, applesAppliedOffer.getDiscountAmount(), 0.01);

        final AppliedOffer breadAppliedOffer = receipt.getAppliedOffers().get(1);
        assertEquals("Bread half price if 2 tins of soup bought", breadAppliedOffer.getDescription());
        assertEquals(0.40, breadAppliedOffer.getDiscountAmount(), 0.01);

        assertEquals(3.90, receipt.getFinalTotal(), 0.01);
    }

    @Test
    @DisplayName("Checks scenario when no offer applied")
    public void checkNoOfferApplied() {
        final Receipt receipt = testSubject.generateShoppingReceipt("Pricebasket Milk");
        assertEquals(1.30, receipt.getSubtotal(), 0.01);
        assertEquals(0, receipt.getAppliedOffers().size());
        assertEquals(1.30, receipt.getFinalTotal(), 0.01);
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
                p -> 0.1 * p.get(Product.APPLES)));

        currentOffers.add(new DiscountOffer("Bread half price if 2 tins of soup bought",
                p -> p.getOrDefault(Product.SOUP, 0L) >= 2
                        && p.containsKey(Product.BREAD),
                p -> {
                    long countOfSoups = p.getOrDefault(Product.SOUP, 0L);

                    int maximumNumberOfBreadsEligibleForOffer = (int)(countOfSoups / 2);
                    long numberOfBreadsEligibleForOffer = Math.min(p.get(Product.BREAD),
                            maximumNumberOfBreadsEligibleForOffer);
                    return 0.5 * Product.BREAD.getCostPerUnit() * numberOfBreadsEligibleForOffer;
                }));

        return currentOffers;
    }
}
