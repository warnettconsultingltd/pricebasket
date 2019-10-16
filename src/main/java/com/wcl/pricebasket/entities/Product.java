/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.entities;

import java.util.Arrays;

/**
 * Models the type of product available within the application.
 *
 * In real world, it's not the best solution - for one, there are brands within the category and secondly, unit cost
 * can change.  Something like accessing an external API for product information would be a better choice, for this
 * simple example then this hardcoded solution suffices.
 */
public enum Product {
    APPLES("Apples", 1.0f),
    BREAD("Bread", 0.8f),
    MILK("Milk",1.3f),
    SOUP("Soup", 0.65f);

    private final String productName;
    private final float costPerUnit;

    Product(final String nameOfProduct, final float unitCost) {
        productName = nameOfProduct;
        costPerUnit = unitCost;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Returns the cost per unit of the product.
     *
     * @return the cost per unit
     */
    public float getCostPerUnit() {
        return costPerUnit;
    }

    /**
     * Helper method to retrieve the correct Peoduct based upon the supplied product name.
     * The match is performed ignoring case.
     *
     * If no match occurs, then a {@link IllegalArgumentException} is thrown.
     *
     * @param productName - the name of the product to find
     * @return found Product
     * @throws IllegalArgumentException thrown if no match on supplied product name
     */
    public static Product getProductByName(final String productName) {
        return Arrays.stream(Product.values())
                .filter(e -> e.productName.equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No such product %s.", productName)));
    }
}
