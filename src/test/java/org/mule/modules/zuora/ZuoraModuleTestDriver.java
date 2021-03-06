/**
 * Mule Zuora Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */

package org.mule.modules.zuora;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.DefinedMapMetaDataModel;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.datatype.DataType;

import com.zuora.api.DeleteResult;
import com.zuora.api.ProductRatePlanChargeTierData;
import com.zuora.api.RatePlanData;
import com.zuora.api.SaveResult;
import com.zuora.api.SubscribeRequest;
import com.zuora.api.SubscribeResult;
import com.zuora.api.SubscriptionData;
import com.zuora.api.object.Account;
import com.zuora.api.object.Contact;
import com.zuora.api.object.PaymentMethod;
import com.zuora.api.object.ProductRatePlanChargeTier;
import com.zuora.api.object.RatePlan;
import com.zuora.api.object.Subscription;
import com.zuora.api.object.ZuoraBeanMap;
import org.mule.streaming.PagingConfiguration;
import org.mule.streaming.ProviderAwarePagingDelegate;

public class ZuoraModuleTestDriver {
    private ZuoraModule module;
    private final String username = "albin.kjellin@mulesoft.com";
    private final String password = "Mule2012";

    @Before
    public void setup() throws Exception {
        module = new ZuoraModule();
        module.setEndpoint("https://apisandbox.zuora.com");
        module.connect(username, password);
    }

    /**
     * Test for creating dynamic zobjects
     */
    @Test
    public void testMetadata() throws Exception {
        List<MetaDataKey> metadataKeys = module.getMetadataKeys();
        assertTrue(metadataKeys.size() > 0);
        
        MetaData metadata = module.getMetadata(new DefaultMetaDataKey("Account", "Account"));
        
        DefinedMapMetaDataModel payload = (DefinedMapMetaDataModel)metadata.getPayload();
        MetaDataModel nameModel = payload.getValueMetaDataModel("name");
        assertEquals(DataType.STRING, nameModel.getDataType());
    }
    
    /**
     * Test that if DM passes in a date, it works.
     */
    @Test
    public void testDateAssigment() throws Exception {
        Subscription subscription = new Subscription();
        ZuoraBeanMap map = new ZuoraBeanMap(subscription);
        map.put("subscriptionEndDate", new Date());
        
        assertNotNull(subscription.getSubscriptionEndDate());
    }
    
    /**
     * Test for creating dynamic zobjects
     */
    @Test
    public void createAndDelete() throws Exception {
        SaveResult result = module.create("Account", Collections.singletonList(testAccount())).get(0);
        assertTrue(result.getSuccess());


        DeleteResult deleteResult = module.delete("Account", Arrays.asList(result.getId())).get(0);
        assertTrue(deleteResult.getSuccess());
    }

    /**
     * Test for creating zobjects with relationships
     */
    @Test
    @SuppressWarnings("serial")
    public void createAndDeleteRelated() throws Exception {
        SaveResult saveResult = module.create("Account", Collections.singletonList(testAccount())).get(0);
        assertTrue(saveResult.getSuccess());

        final String accountId = saveResult.getId();
        try {
            SaveResult result = module.create("Contact",
                    Collections.<Map<String, Object>>singletonList(new HashMap<String, Object>() {
                        {
                            put("Country", "US");
                            put("FirstName", "John");
                            put("LastName", "Doe");
                            put("AccountId", accountId);
                        }
                    })).get(0);
            assertTrue(result.getSuccess());

            DeleteResult deleteResult = module.delete("Contact", Arrays.asList(result.getId())).get(0);
            assertTrue(deleteResult.getSuccess());
        } finally {
            module.delete("Account", Arrays.asList(accountId)).get(0);
        }
    }

    /**
     * Test for fetching zobjects when there is no object that matches the query
     */
    @Test
    public void findNoResult() throws Exception {

        ProviderAwarePagingDelegate<Map<String,Object>, ZuoraModule> pagingDelegate =
                module.find("SELECT Id FROM Account where id ='not here!'", new PagingConfiguration(0));
        List<Map<String, Object>> page = pagingDelegate.getPage(module);
        assertTrue(page.isEmpty());
    }

    /**
     * Test for fetching zobjects when there is an object that matches the query
     */
    @Test
    @Ignore("will fix later")
    public void findOneResult() throws Exception {
        String id = module.create("Account", Collections.singletonList(testAccount())).get(0).getId();
        try {
            ProviderAwarePagingDelegate<Map<String, Object>, ZuoraModule> pagingDelegate =
                    module.find("SELECT Id, Name, AccountNumber FROM Account WHERE AccountNumber = '7891'", new PagingConfiguration(0));
//            assertTrue(result.hasNext());
//            Map<String,Object> next = result.next();
//            assertNotNull(next.get("id"));
//            assertEquals(testAccount().get("name"), next.get("name"));
//            assertFalse(result.hasNext());
        } finally {
            module.delete("Account", Arrays.asList(id));
        }
    }


    

    @Test
    public void getUserInfo() throws Exception {
        User userInfo = module.getUserInfo();
        assertNotNull(userInfo);
        assertFalse(userInfo.getUserId().isEmpty());
        assertFalse(userInfo.getUserEmail().isEmpty());
        assertEquals(username, userInfo.getUsername());
        assertFalse(userInfo.getTenantId().isEmpty());
        assertFalse(userInfo.getTenantName().isEmpty());
    }

    @Test
    @SuppressWarnings("serial")
    public void createRetrieveAnAccountProfileAndDeleteRelatedAccount() throws Exception {
        SaveResult accountResult = module.create("Account", Collections.singletonList(testAccount())).get(0);
        assertTrue(accountResult.getSuccess());

        final String accountId = accountResult.getId();
        try {
            SaveResult contactResult = module.create("Contact",
                    Collections.<Map<String, Object>>singletonList(new HashMap<String, Object>() {
                        {
                            put("country", "US");
                            put("firstName", "John");
                            put("lastName", "Doe");
                            put("accountId", accountId);
                        }
                    })).get(0);

            assertTrue(contactResult.getSuccess());

            Map<String, Object> accountMap = testAccount();
            accountMap.put("Id", accountId);
            accountMap.put("BillToId", contactResult.getId());

            SaveResult accountUpdateResult = module.update("Account", Collections.singletonList(accountMap)).get(0);

            assertTrue(accountUpdateResult.getSuccess());
        } finally {
            module.delete("Account", Arrays.asList(accountId)).get(0);
        }
    }

    @SuppressWarnings("serial")
    private Map<String, Object> testAccount() {
        return new HashMap<String, Object>() {
            {
                put("name", "foo");
                put("currency", "USD");
                put("billCycleDay", "1");
                put("accountNumber", "7891");
                put("allowInvoiceEdit", "false");
                put("autoPay", "false");
                put("notes", "foobar");
                put("status", "Draft");
            }
        };
    }

    /**
     * Test for fetching zobjects when there is an object that matches the query
     */
    @Test
    public void getInvoice() throws Exception {

        //Setup Product details
        String productId = getTestProduct();
        String productRatePlanId = getTestProductRatePlan(productId);
        String productRateplanChargeId = getTestProductRatePlanCharge(productRatePlanId);

        assertNotNull(productId);
        assertNotNull(productRatePlanId);
        assertNotNull(productRateplanChargeId);

        SubscribeRequest subscribeReq = new SubscribeRequest();

        //subscribeReq.setAccount(testAccount());
        String uniqueString = UUID.randomUUID().toString();

        Contact contact = new Contact();
        contact.setFirstName(uniqueString);
        contact.setLastName(uniqueString);


        Account account = new Account();
        account.setName(uniqueString);
        account.setBillCycleDay(1);
        account.setCurrency("USD");
        account.setAllowInvoiceEdit(false);
        account.setAutoPay(false);
        account.setStatus("Draft");
        account.setPaymentTerm("Due Upon Receipt");
        account.setBatch("Batch1");

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType("CreditCard");
        paymentMethod.setCreditCardNumber("5105105105105100");
        paymentMethod.setCreditCardType("Visa");
        paymentMethod.setCreditCardExpirationYear(2026);
        paymentMethod.setCreditCardExpirationMonth(5);
        paymentMethod.setCreditCardHolderName("Unit Test");

        GregorianCalendar calStart = new GregorianCalendar();
        calStart.add(Calendar.DATE, -1);
        XMLGregorianCalendar effectiveStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);

        Subscription subscription = new Subscription();
        subscription.setContractAcceptanceDate(effectiveStartDate);
        subscription.setContractEffectiveDate(effectiveStartDate);
        subscription.setInitialTerm(12);
        subscription.setRenewalTerm(12);

        RatePlan ratePlan = new RatePlan();
        ratePlan.setProductRatePlanId(productRatePlanId);
        RatePlanData ratePlanData = new RatePlanData();
        ratePlanData.setRatePlan(ratePlan);

        SubscriptionData subscriptionData = new SubscriptionData();
        subscriptionData.setSubscription(subscription);
        subscriptionData.getRatePlanData().add(ratePlanData);

        subscribeReq.setAccount(account);
        subscribeReq.setBillToContact(contact);
        subscribeReq.setSoldToContact(contact);
        subscribeReq.setPaymentMethod(paymentMethod);
        subscribeReq.setSubscriptionData(subscriptionData);
        SubscribeResult subscribeResult = module.subscribe(Collections.singletonList(subscribeReq)).get(0);
        assertTrue(subscribeResult.getSuccess());
        assertEquals(0,subscribeResult.getErrors().size());

        DeleteResult deleteResultAccount = module.delete("Account", Collections.singletonList(subscribeResult.getAccountId())).get(0);
        assertTrue(deleteResultAccount.getSuccess());

        DeleteResult deleteResultProduct = module.delete("Product", Collections.singletonList(productId)).get(0);
        assertTrue(deleteResultProduct.getSuccess());
    }

    @SuppressWarnings("serial")
    private String getTestProduct() {

        Map<String, Object> returnMap = new HashMap<String, Object>();



        SaveResult saveResult = null;

        try {
            saveResult = module.create("Product", Collections.<Map<String, Object>>singletonList(new HashMap<String, Object>() {
                {
                    put("Name", "UnitTestProduct");
                    put("EffectiveStartDate", "2011-01-01T20:00:00");
                    put("EffectiveEndDate", "2013-01-01T20:00:00");
                }
            })).get(0);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return saveResult.getId();

    }

    @SuppressWarnings("serial")
    private String getTestProductRatePlan(final String productId) {
        SaveResult saveResult = null;

        try {
            saveResult = module.create("ProductRatePlan", Collections.<Map<String, Object>>singletonList(new HashMap<String, Object>() {
                {
                    put("productId", productId);
                    put("name", "TestProductRatePlan");
                    put("effectiveStartDate", "2011-01-01T20:00:00");
                    put("effectiveEndDate", "2013-01-01T20:00:00");
                    put("description", "Test product used in unit test.");
                }
            })).get(0);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return saveResult.getId();

    }

    @SuppressWarnings("serial")
    private String getTestProductRatePlanCharge(final String productRatePlanId) throws Exception {
        ProductRatePlanChargeTier tier = new ProductRatePlanChargeTier();
        tier.setCurrency("USD");
        tier.setPrice(new BigDecimal(12.2));
        tier.setTier(1);
//        tier.setActive(true);
        final ProductRatePlanChargeTierData productRatePlanChargeTierData = new ProductRatePlanChargeTierData();
        productRatePlanChargeTierData.getProductRatePlanChargeTier().add(tier);

        SaveResult saveResult = module.create("ProducRatePlanCharge", Collections.<Map<String, Object>>singletonList(new HashMap<String, Object>() {
                {
                    put("BillingPeriod", "Month");
                    put("ChargeModel", "FlatFee");
                    put("ChargeType", "Recurring");
                    put("DefaultQuantity", "1	");
                    put("Model", "PerUnit");
                    put("Name", "TestProductRatePlanCharge");
                    put("ProductRatePlanId", productRatePlanId);
                    put("ProductRatePlanChargeTierData", productRatePlanChargeTierData);
                    put("TriggerEvent", "ContractEffective");
                    put("Type", "Recurring");
                }
            })).get(0);
        return saveResult.getId();

    }
}
