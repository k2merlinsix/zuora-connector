/**
 * Mule Zuora Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.zuora.zobject;

import com.zuora.api.object.Account;
import com.zuora.api.object.AccountingRun;
import com.zuora.api.object.AccountingRunAdjustmentSummary;
import com.zuora.api.object.AccountingRunInvoiceSummary;
import com.zuora.api.object.AccountingRunPaymentSummary;
import com.zuora.api.object.AdjustmentAccountingDetail;
import com.zuora.api.object.Amendment;
import com.zuora.api.object.Contact;
import com.zuora.api.object.CreditBalanceAdjustment;
import com.zuora.api.object.Export;
import com.zuora.api.object.Invoice;
import com.zuora.api.object.InvoiceAccountingDetail;
import com.zuora.api.object.InvoiceItem;
import com.zuora.api.object.InvoiceItemAdjustment;
import com.zuora.api.object.InvoicePayment;
import com.zuora.api.object.Payment;
import com.zuora.api.object.PaymentAccountingDetail;
import com.zuora.api.object.PaymentMethod;
import com.zuora.api.object.Product;
import com.zuora.api.object.ProductRatePlan;
import com.zuora.api.object.ProductRatePlanCharge;
import com.zuora.api.object.ProductRatePlanChargeTier;
import com.zuora.api.object.RatePlan;
import com.zuora.api.object.RatePlanCharge;
import com.zuora.api.object.RatePlanChargeTier;
import com.zuora.api.object.Refund;
import com.zuora.api.object.RefundInvoicePayment;
import com.zuora.api.object.Subscription;
import com.zuora.api.object.TaxationItem;
import com.zuora.api.object.Usage;
import com.zuora.api.object.ZObject;

/**
 * Enumeration of available ZObject classes. 
 * @author flbulgarelli
 */
public enum ZObjectType
{
    Account(Account.class), //
    Invoice(Invoice.class), InvoiceItem(InvoiceItem.class), //
    InvoiceItemAdjustment(InvoiceItemAdjustment.class), //
    Payment(Payment.class), //
    Product(Product.class), //
    ProductRatePlan(ProductRatePlan.class), //
    ProductRatePlanCharge(ProductRatePlanCharge.class), //
    Refund(Refund.class), // 
    Subscription(Subscription.class), //
    Usage(Usage.class), //
    AccountingRun(AccountingRun.class), // 
    AccountingRunAdjustmentSummary(AccountingRunAdjustmentSummary.class), //
    AccountingRunInvoiceSummary(AccountingRunInvoiceSummary.class), //
    AccountingRunPaymentSummary(AccountingRunPaymentSummary.class), //
    AdjustmentAccountingDetail(AdjustmentAccountingDetail.class), //
    Amendment(Amendment.class), //
    Contact(Contact.class), //
    CreditBalanceAdjustment(CreditBalanceAdjustment.class), //
    Export(Export.class), // 
    InvoiceAccountingDetail(InvoiceAccountingDetail.class), //
    InvoicePayment(InvoicePayment.class), //
    PaymentAccountingDetail(PaymentAccountingDetail.class), //
    PaymentMethod(PaymentMethod.class), //
    ProductRatePlanChargeTier(ProductRatePlanChargeTier.class), //
    RatePlan(RatePlan.class), //
    RatePlanChargeTier(RatePlanChargeTier.class), // 
    RefundInvoicePayment(RefundInvoicePayment.class), //
    RatePlanCharge(RatePlanCharge.class), //
    TaxationItem(TaxationItem.class);

    
    private Class<? extends ZObject> clazz;

    private ZObjectType(Class<? extends ZObject> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Currently this attribute has no usage. It is here just to ensure that 
     * each object has a corresponding class
     * @return the associated class
     */
    public Class<? extends ZObject> getZObjectClass()
    {
        return clazz;
    }

    public String getTypeName()
    {
        return name();
    }

    public ZObject newInstance()
    {
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }
    }

}
