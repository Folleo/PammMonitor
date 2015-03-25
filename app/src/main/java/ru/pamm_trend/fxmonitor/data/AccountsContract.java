package ru.pamm_trend.fxmonitor.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Represents single Pamm Account.
 */
public class AccountsContract {

    public static final String CONTENT_AUTHORITY = "ru.pamm_trend.android.fxmonitor.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ACCOUNT = "account";

    public static final class AccountEntry implements BaseColumns{

        public static final String COLUMN_INVESTOR_ID = "investor_id";
        public static final String COLUMN_INVESTOR_TYPE = "investor_type";
        public static final String COLUMN_PARTNER_ID = "partner_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_OFFER_ID = "offer_id";
        public static final String COLUMN_PAMM_MT_LOGIN = "pamm_mt_login";
        public static final String COLUMN_INV_MT_LOGIN = "inv_mt_login";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_ACTIVATED_AT = "activated_at";
        public static final String COLUMN_CLOSED_AT = "closed_at";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_CURRENT_SUM = "current_sum";
        public static final String COLUMN_INSURED_SUM = "insured_sum";
        public static final String COLUMN_FOR_INDEX = "for_index";
        public static final String COLUMN_FOR_IC = "for_ic";
        public static final String COLUMN_AUTO_WITHDRAWAL_PROFIT = "auto_withdrawal_profit";
        public static final String COLUMN_SHOW_MODE = "show_mode";
        public static final String COLUMN_OFFER_MIN_BALANCE = "offer_min_balance";
        public static final String COLUMN_OFFER_COMMISSION = "offer_commission";
        public static final String COLUMN_TRADE_SESSION_START_UNIXTIME = "trade_session_start_unixtime";
        public static final String COLUMN_INVESTORS_DEPOSIT = "investors_deposit";
        public static final String COLUMN_INVESTORS_WITHDRAW = "investors_withdraw";
        public static final String COLUMN_TRADE_SESSION_PAYMENTS = "trade_session_payments";
        public static final String COLUMN_PERIOD_PROFIT = "period_profit";
        public static final String COLUMN_AVAILSUM = "availsum";
        public static final String COLUMN_PROFIT_PERCENT = "profit_percent";
        public static final String COLUMN_PRETERMSUM = "pretermsum";
        public static final String COLUMN_CURRENT_CAPITAL = "current_capital";
        public static final String COLUMN_MAX_DEFINED_SUM = "max_defined_sum";
        public static final String COLUMN_ACTUAL_DATETIME = "actual_datetime";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNT;

        // Table name
        public static final String TABLE_NAME = "account";

    }

}
