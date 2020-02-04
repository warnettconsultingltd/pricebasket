/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.offers;

import com.wcl.pricebasket.entities.Product;
import org.javamoney.moneta.Money;


import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines a discount offer to be applied to a shopping basket.
 *
 * It comprised a description of the discount offer along with a condition predicate and a discount calculator
 * function - each of the above are injected into the constructor.
 *
 * This generalisation is intended to reduce a proliferation of interface / concrete class implementations, for example
 * separate classes for Appls offer, Soup/Bread offer etc.   Each offer varies in the three identified elements, so
 * a common class can be used that delegates to the variants.
 */
public final class DiscountOffer {
    private final String description;
    private final Predicate<Map<Product, Long>> offerCondition;
    private final Function<Map<Product, Long>, Money> discountCalculator;

    public DiscountOffer(final String description,
                         final Predicate<Map<Product, Long>> offerCondition,
                         final Function<Map<Product, Long>, Money> discountCalculator) {
        this.description = description;
        this.offerCondition = offerCondition;
        this.discountCalculator = discountCalculator;
    }

    /**
     * Returns the description of the discount offer.
     *
     * @return the discount offer description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a boolean indicating if the offer can be applied to the contents of the basket supplied.
     * Delegates to the predicate supplied to the constructor.
     *
     * @param basket  the basket of products to buy
     * @return true if this discount offer can be applied to the basket, false otherwise.
     */
    public boolean isOfferApplicable(final Map<Product, Long> basket) {
        return offerCondition.test(basket);
    }

    /**
     * Returns the amount of discount to be applied to the basket.
     * Delegates to the calculator function supplied to the constructor.
     *
     * @param basket  the basket of products to buy
     * @return the amount of discount to apply based upon the contents of the basket.
     */
    public Money calculateDiscountToApply(final Map<Product, Long> basket) {
        return discountCalculator.apply(basket);
    }
}
