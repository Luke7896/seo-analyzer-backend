package com.demo.seoanalyzer.seoapplication.Utils;

public class BackendConstants {


    /**
     *  EMAIL
     */
    public static final int MAX_EMAIL_SEND_ATTEMPTS = 3;
    public static final int EMAIL_PENDING = 0;
    public static final int EMAIL_SENT = 1;
    public static final int EMAIL_FAILED = 2;
    public static final int EMAIL_CLOSED = 3;

    /**
     *
     * USER
     *
     */
    public static final int ROLE_LEAD = 0;
    public static final int ROLE_CLIENT = 1;
    public static final int ROLE_ADMIN = 2;
    public static final int ROLE_FORMER_CLIENT = 3;

    public static final String ROLE_LEAD_STRING = "ROLE_LEAD";
    public static final String ROLE_CLIENT_STRING = "ROLE_CLIENT";
    public static final String ROLE_ADMIN_STRING = "ROLE_ADMIN";
    public static final String ROLE_FORMER_CLIENT_STRING = "ROLE_FORMER_CLIENT";

    public static final int USER_INACTIVE = 0;
    public static final int USER_ACTIVE = 1;


    /**
     *
     *  REPORTS
     *
     */
    public static final int REPORT_STATUS_PENDING = 0;
    public static final int REPORT_STATUS_PROCESSING = 1;
    public static final int REPORT_STATUS_COMPLETED = 2;
    public static final int REPORT_STATUS_FAILED = 3;
    public static final int REPORT_STATUS_CLOSED = 4;

    public static final int REPORT_TYPE_FREE_AUDIT = 0;
    public static final int REPORT_TIER_PAID = 1;


    /**
     *
     * SUBSCRIPTIONS
     *
     */
    public static final int SUBSCRIPTION_STATUS_INACTIVE = 0;
    public static final int SUBSCRIPTION_STATUS_ACTIVE = 1;
    public static final int SUBSCRIPTION_STATUS_CANCELLED = 2;
    public static final int SUBSCRIPTION_STATUS_EXPIRED = 3;

    /**
     *
     *  STRIPE
     *
     */
    public static final String STRIPE_EVENT_CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";
    public static final String STRIPE_EVENT_CUSTOMER_SUBSCRIPTION_DELETED = "customer.subscription.deleted";
    public static final String STRIPE_EVENT_CUSTOMER_SUBSCRIPTION_UPDATED = "customer.subscription.updated";

    /**
     *
     *
     *  WEBSITES
     *
     *
     */

    // Types
    public static final int WEBSITE_TYPE_AUDIT = 0;
    public static final int WEBSITE_TYPE_POSITION_TRACKING = 1;
    public static final int WEBSITE_TYPE_AUDIT_AND_POSITION_TRACKING = 2;

    // Status
    public static final int WEBSITE_STATUS_INACTIVE = 0;
    public static final int WEBSITE_STATUS_ACTIVE = 1;

}