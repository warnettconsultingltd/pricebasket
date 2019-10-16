/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.receipt;


import lombok.Builder;
import lombok.Data;

/**
 * Data class containing the details for an offer that has been applied to the basket.
 * It is used for storing the applied offers and generating receipt text.
 */
@Data
@Builder
public class AppliedOffer {
    /** The description of the applied offer. */
    String description;
    /** The amount of discount for this applied offer. */
    double discountAmount;
}
