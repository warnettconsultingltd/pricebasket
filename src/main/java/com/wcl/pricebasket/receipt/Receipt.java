/**
 * Copyright (c) 2019 Warnett Consulting Ltd
 * All rights reserved
 **/
package com.wcl.pricebasket.receipt;

import com.wcl.pricebasket.utils.MonetaryUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.util.List;

@Data
@Builder
/**
 * Models a receipt.
 * The format is as follows.
 * <ul>
 *     <li>Initial subtotal</li>
 *     <li>Applied offers or message indicating no applicable offers</li>
 *     <li>Final total</li>
 * </ul>
 *
 * Examples are below.
 * <code>
 *     Subtotal: £3.10
 *     Apples 10% off: -10p
 *     Total: £3.00
 * </code>
 *
 * <code>
 *     Subtotal: £0.65
 *     (no offers available)
 *     Total: £0.65
 * </code>
 */
public final class Receipt {
    private Money subtotal;
    private List<AppliedOffer> appliedOffers;
    private Money finalTotal;

    /**
     * Renders the receipt in String format.
     *
     * @return the receipt as a String
     */
    @Override
    public String toString() {
        final StringBuilder receiptText = new StringBuilder();
        receiptText.append(String.format("Subtotal: £%.2f", subtotal.getNumber().doubleValue()));
        receiptText.append(System.lineSeparator());
        renderAppliedOffers(receiptText, appliedOffers);
        receiptText.append(String.format("Total: £%.2f", finalTotal.getNumber().doubleValue()));
        return receiptText.toString().trim();

    }

    /* Renders the applied offers, with a generic message if no offers were applicable. */
    private void renderAppliedOffers(final StringBuilder receiptText, final List<AppliedOffer> appliedOffers) {
        if (CollectionUtils.isEmpty(appliedOffers)) {
            receiptText.append("(no offers available)");
            receiptText.append(System.lineSeparator());
        } else {
            appliedOffers.forEach(o -> renderAppliedOffer(receiptText, o));
        }
    }

    /* Renders the applied discount offer description and discount amount. */
    private void renderAppliedOffer(final StringBuilder receiptText, final AppliedOffer appliedOffer) {
        receiptText.append(
                appliedOffer.getDiscountAmount().isLessThan(
                        MonetaryUtils.gbpAmount(1.00)
                ) ? displayInPenceFormat(appliedOffer)
                                                       : renderInPoundFormat(appliedOffer));
        receiptText.append(System.lineSeparator());
    }

    /* Renders the offer text with the amount rendered on 0.XXp format. */
    private String displayInPenceFormat(final AppliedOffer appliedOffer) {
        return String.format("%s: -%dp", appliedOffer.getDescription(), (int)(appliedOffer.getDiscountAmount().getNumber().doubleValue() * 100));
    }

    /* Renders the offer text with the amount rendered on £0.XX format. */
    private String renderInPoundFormat(final AppliedOffer appliedOffer) {
        return String.format("%s: -£%.2f", appliedOffer.getDescription(), appliedOffer.getDiscountAmount().getNumber().doubleValue());
    }
}
