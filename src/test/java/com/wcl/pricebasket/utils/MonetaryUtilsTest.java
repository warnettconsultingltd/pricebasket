package com.wcl.pricebasket.utils;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


import static org.junit.jupiter.api.Assertions.*;

public final class MonetaryUtilsTest {

    @Test
    @DisplayName("Return the correct GBP amount of money")
    public void checkCorrectGBPAmountOfMoneyCreated() {
        assertTrue("GBP 2.56".equals(MonetaryUtils.gbpAmount(2.56).toString()));
    }
}
