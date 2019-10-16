/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.parser;

import com.wcl.pricebasket.entities.Product;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Retrieves the contents of the shopping basket from user provided text.
 *
 * Validation of the user input also occurs.
 */
public final class PricebasketParser {

    /**
     * Validates and parses the user entered text to retrieve the contents of the shopping basket.
     *
     * If the text is badly formed, then an {@link IllegalArgumentException} is thrown with relevant error message.
     *
     * @param enteredText  the user entered text
     * @return list of Products in the basket
     * @throws IllegalArgumentException if text malformed
     */
    public List<Product> retrieveShoppingList(final String enteredText) {
        final String[] items = validateEnteredText(enteredText);

        return  Arrays.asList(items).subList(1, items.length).stream().map(Product::getProductByName)
                                                                      .collect(Collectors.toList());
    }

    /*
     * Validates the text provided, throwing exception on any errors.
     */
    private String[] validateEnteredText(final String enteredText) {
        if (StringUtils.isBlank(enteredText)) {
            throw new IllegalArgumentException("Nothing was entered.");
        }

        if (!enteredText.startsWith("Pricebasket")) {
            throw new IllegalArgumentException("Pricebasket was not found at the start.");
        }

        final String[] items = enteredText.split(" ");

        // First word is "Pricebasket".
        if (items.length == 1) {
            throw new IllegalArgumentException("No products to purchase were entered.");
        }
        return items;
    }
}
