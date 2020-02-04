package com.wcl.pricebasket.testutils;

import org.javamoney.moneta.Money;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTestUtils {
    private MoneyTestUtils() {}

    public static void assertMoneyValuesEquals(final Money expected, final Money actual) {
        assertEquals(expected.getNumber().doubleValue(), actual.getNumber().doubleValue(), 0.01);
    }
}
