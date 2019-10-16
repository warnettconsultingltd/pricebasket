/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.parser;

import com.wcl.pricebasket.entities.Product;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PricebasketParserTest {

    @Test
    @DisplayName("Checks an appropriate error message displayed for no input")
    public void checkErrorMessageDisplayedWhenNoTextEntered() {
        final PricebasketParser testParser = new PricebasketParser();
        checkErrorMessageDisplayedWhenNothingTyped(testParser, null);
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for blank input")
    public void checkErrorMessageDisplayedWhenBlankTextEntered() {
        final PricebasketParser testParser = new PricebasketParser();
        checkErrorMessageDisplayedWhenNothingTyped(testParser, "");
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for only spaces")
    public void checkErrorMessageDisplayedWhenOnlySpaceEntered() {
        final PricebasketParser testParser = new PricebasketParser();
        checkErrorMessageDisplayedWhenNothingTyped(testParser, " ");
    }

    private void checkErrorMessageDisplayedWhenNothingTyped(final PricebasketParser testParser, final String text) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testParser.retrieveShoppingList(text));
        assertEquals("Nothing was entered.",
                     exception.getMessage());
    }

    @Test
    @DisplayName("Checks an appropriate error message displayed for when input does not start with Pricebasket")
    public void checkErrorMessageDisplayedWhenPricebasketIsMissing() {
        final PricebasketParser testParser = new PricebasketParser();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testParser.retrieveShoppingList("Apples Bread Milk"));
        assertEquals("Pricebasket was not found at the start.",
                exception.getMessage());
    }

    @Test
    public void checkErrorMessageDisplayedWhenNoItemsProvided() {
        final PricebasketParser testParser = new PricebasketParser();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testParser.retrieveShoppingList("Pricebasket"));
        assertEquals("No products to purchase were entered.",
                exception.getMessage());
    }

    @Test
    public void checkSingleItemProvidedReturnedCorrectly() {
        final PricebasketParser testParser = new PricebasketParser();
        List<Product> list = testParser.retrieveShoppingList("Pricebasket Apples");
        assertEquals(1, list.size());
        assertEquals(Product.APPLES, list.get(0));
    }

    @Test
    public void checkMultipleItemsProvidedReturnedCorrectly() {
        final PricebasketParser testParser = new PricebasketParser();
        List<Product> list = testParser.retrieveShoppingList("Pricebasket Apples Milk Milk");
        assertEquals(3, list.size());
        assertEquals(Product.APPLES, list.get(0));
        assertEquals(Product.MILK, list.get(1));
        assertEquals(Product.MILK, list.get(2));
    }

}
