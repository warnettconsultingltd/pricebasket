/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.receipt;

import com.wcl.pricebasket.entities.Product;
import com.wcl.pricebasket.offers.DiscountOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class designed to generated a receipt for a basket of products supplied.
 */
public final class ReceiptGenerator {
    /** The list of discount offers available. */
    private final List<DiscountOffer> discountOffers;

    public ReceiptGenerator(final List<DiscountOffer> discountOffers) {
        this.discountOffers = new ArrayList<>();
        this.discountOffers.addAll(discountOffers);
    }

    /**
     * Generates a receipt for the basket of products supplied.
     *
     * The receipt comprises the following elements :-
     * <ul>
     *     <li>Subtotal - this is the initial cost of all the products within the basket.</li>
     *     <li>Offers applied - lists each offer applicable to the basket along with the applied discount.</li>
     *     <li>Total - this is the actual cost to the customer, after discounts have been applied.</li>
     * </ul>
     *
     * @param basket - the basket of products to generate a receipt for.
     * @return a generated receipt for the basket of products
     * @see Receipt
     */
    public Receipt generateReceipt(final Map<Product, Long> basket) {
        double subtotal = generateSubtotal(basket);
        final List<AppliedOffer> appliedOffers = generateListOfOffersToApply(basket);
        double finalTotal = generateDiscountedTotal(subtotal, appliedOffers);

        return Receipt.builder().subtotal(subtotal)
                                .appliedOffers(appliedOffers)
                                .finalTotal(finalTotal)
                                .build();
    }

    /*
     * Generates the initial subtotal by iterating over the basket of products, multiplying each product by its unit
     * cost, returning the sum of those as the initial subtotal.
     */
    private double generateSubtotal(final Map<Product, Long> basket) {
        return basket.entrySet().stream().mapToDouble(e -> e.getKey().getCostPerUnit() * e.getValue() )
                                                               .sum();
    }

    /*
     * Method takes in a Map of Product in basket along with the quantities.  These are then filtered to find the
     * only applicable offers over the basket as a whole.
     * The applicable offers are then applied to the basket of products, generating a list of the offers applied to the
     * basket containing the offer description along with the disount applied per offer.
     */
    private List<AppliedOffer> generateListOfOffersToApply(final Map<Product, Long> boughtItems) {
        return discountOffers.stream().filter(e -> e.isOfferApplicable((boughtItems)))
                       .map( e -> AppliedOffer.builder().description(e.getDescription())
                                                         .discountAmount(e.calculateDiscountToApply(boughtItems))
                                                         .build())
                       .collect(Collectors.toList());
    }

    /*
     * Generates the final discounter total by calculating the sum of all the discounts applied by the offers and
     * then subtracting it from the initial subtotal.
     */
    private double generateDiscountedTotal(final double subtotal, final List<AppliedOffer> offers) {
        double offerTotal = offers.stream().mapToDouble(AppliedOffer::getDiscountAmount)
                                          .sum();
        return subtotal - offerTotal;

    }

 }



