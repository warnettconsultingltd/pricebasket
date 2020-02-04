package com.wcl.pricebasket.utils;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public final class MonetaryUtils {
    private MonetaryUtils() {}

    public static Money gbpAmount(final double amount) {
        return createMoney(amount, Monetary.getCurrency("GBP"));
    }

    private static Money createMoney(final double amount, final CurrencyUnit currencyUnit) {
        return Money.of(amount, currencyUnit);
    }
}
