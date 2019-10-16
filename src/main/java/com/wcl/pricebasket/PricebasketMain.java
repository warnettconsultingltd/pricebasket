/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket;

import com.wcl.pricebasket.entities.Product;
import com.wcl.pricebasket.offers.DiscountOffer;
import com.wcl.pricebasket.parser.PricebasketParser;
import com.wcl.pricebasket.receipt.ReceiptGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main entry point for the application.
 *
 * The application loops awaiting user inout, ending when the user types 'EXIT'.
 *
 * Any errors in input are reported back to the terminal.
 */
class PricebasketMain {
    private static boolean continueProcessing = true;

    /*
     * Loops around awaiting user input.
     */
    private void performWork() {
        final PricebasketManager priceBasketManager = new PricebasketManager(new PricebasketParser(),
                                                                             new ReceiptGenerator(constructCurrentOffers()));
        final Scanner scanner = new Scanner(System.in);
        String input;

        while (continueProcessing) {
            System.out.println("Please type Pricebasket followed by the items to purchase or type EXIT to quit");
            input = scanner.nextLine();

            if ("EXIT".equalsIgnoreCase(input)) {
                continueProcessing = false;
                break;
            }

            try {
                System.out.println(priceBasketManager.generateShoppingReceipt(input));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

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

    public static void main(String... args) {
        final PricebasketMain mainApp = new PricebasketMain();
        mainApp.performWork();
    }
}
