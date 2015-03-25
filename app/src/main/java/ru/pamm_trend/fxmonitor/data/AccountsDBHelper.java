package ru.pamm_trend.fxmonitor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.pamm_trend.fxmonitor.data.AccountsContract.AccountEntry;

/**
 * Manages a local database for accounts data.
 */
public class AccountsDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "accounts.db";

    public AccountsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //throw new UnsupportedOperationException("onCreate method has not been implemented yet");
        final String SQL_CREATE_ACCOUNTS_TABLE =
                "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
                        AccountEntry._ID + " INTEGER PRIMARY KEY," +
                        AccountEntry.COLUMN_INVESTOR_ID + " INTEGER NOT NULL " +
                        AccountEntry.COLUMN_INVESTOR_TYPE + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_PARTNER_ID + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_USER_ID + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_OFFER_ID + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_PAMM_MT_LOGIN + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_INV_MT_LOGIN + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_CREATED_AT + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_ACTIVATED_AT + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_CLOSED_AT + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_STATUS + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_CURRENT_SUM + "REAL NOT NULL" +
                        AccountEntry.COLUMN_INSURED_SUM + "REAL NOT NULL" +
                        AccountEntry.COLUMN_FOR_INDEX + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_TRADE_SESSION_START_UNIXTIME + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_INVESTORS_DEPOSIT + "REAL NOT NULL" +
                        AccountEntry.COLUMN_INVESTORS_WITHDRAW + "REAL NOT NULL" +
                        AccountEntry.COLUMN_TRADE_SESSION_PAYMENTS + "REAL NOT NULL" +
                        AccountEntry.COLUMN_PROFIT_PERCENT + "INTEGER NOT NULL" +
                        AccountEntry.COLUMN_PERIOD_PROFIT + "REAL NOT NULL" +
                        AccountEntry.COLUMN_AVAILSUM + "REAL NOT NULL" +
                        AccountEntry.COLUMN_PRETERMSUM + "REAL NOT NULL" +
                        AccountEntry.COLUMN_MAX_DEFINED_SUM + "REAL NOT NULL" +
                        AccountEntry.COLUMN_ACTUAL_DATETIME + "INTEGER NOT NULL" +
                        " );";
        db.execSQL(SQL_CREATE_ACCOUNTS_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is
     * to simply to discard the data and start over
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Don't forget to implement history support here
        // by saving old data in the special table

        db.execSQL("DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME);
        onCreate(db);
    }
}