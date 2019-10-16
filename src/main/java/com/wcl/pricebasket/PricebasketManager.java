/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket;

import com.wcl.pricebasket.entities.Product;
import com.wcl.pricebasket.parser.PricebasketParser;
import com.wcl.pricebasket.receipt.Receipt;
import com.wcl.pricebasket.receipt.ReceiptGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class performs the heavy lifting of the application.
 *
 * It takes the user input and uses that to generate a receipt for the basket provided.
 */
final class PricebasketManager {
    private final PricebasketParser parser;
    private final ReceiptGenerator receiptGenerator;

    PricebasketManager(final PricebasketParser parser,
                       final ReceiptGenerator receiptGenerator) {
        this.parser = parser;
        this.receiptGenerator = receiptGenerator;
    }

    /**
     * Retrieves the list of Products contained within the user provided text.  The basket retrieved is then processed
     * to return a receipt for the shopping.
     *
     * If an error occurs within the processing of the user provided text, then an {@link IllegalArgumentException}
     * is thrown with an appropriate error message.
     *
     * @param input  the user provided input
     * @return generated shopping receipt; IllegalArgumentException thrown on error.
     */
    Receipt generateShoppingReceipt(final String input) {
        final List<Product> enteredItemNames = parser.retrieveShoppingList(input);

        final Map<Product, Long> basket = enteredItemNames.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return receiptGenerator.generateReceipt(basket);
    }


}

