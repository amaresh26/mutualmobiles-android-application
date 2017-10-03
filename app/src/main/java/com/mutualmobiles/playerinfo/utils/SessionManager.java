package com.mutualmobiles.playerinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amareshjana on 24/09/17.
 */

public class SessionManager {

    private final String APP_SHARED_PREF_NAME = "com.mutualmobiles.playerinfo";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;

    //shared preference variables
    private final String PLAYER_NAME = "player_name";
    private final String PLAYER_IMG = "player_img";
    private final String BALANCE = "balance";
    private final String LAST_LOGIN = "last_login";
    private final String CURRENCY = "currency";
    private final String GAME_LIST = "game_list";
    private final String IS_CACHED = "is_cached";

    public SessionManager(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(APP_SHARED_PREF_NAME,
                Activity.MODE_PRIVATE);
        this.mSharedPreferenceEditor = mSharedPreferences.edit();
    }

    /*
    * Player Name
    * */
    public void setName(String playerName) {
        mSharedPreferenceEditor.putString(PLAYER_NAME, playerName);
        mSharedPreferenceEditor.commit();
    }

    public String getName() {
        return mSharedPreferences.getString(PLAYER_NAME, "");
    }

    /*
    * Player Balance
    * */
    public void setBalance(int balance) {
        mSharedPreferenceEditor.putInt(BALANCE, balance);
        mSharedPreferenceEditor.commit();
    }

    public Integer getBalance() {
        return mSharedPreferences.getInt(BALANCE, 0);
    }

    /*
    * Player Last Login Date
    * */
    public void setLastLoginDate(String mLastLoginDate) {
        mSharedPreferenceEditor.putString(LAST_LOGIN, mLastLoginDate);
        mSharedPreferenceEditor.commit();
    }

    public String getLastLoginDate() {
        return mSharedPreferences.getString(LAST_LOGIN, "");
    }

    /*
    * Player Image
    * */
    public void setPlayerImg(String mPlayerImage) {
        mSharedPreferenceEditor.putString(PLAYER_IMG, mPlayerImage);
        mSharedPreferenceEditor.commit();
    }

    public String getPlayerImg() {
        return mSharedPreferences.getString(PLAYER_IMG, "");
    }

    /*
    * Currency
    * */
    public void setCurrency(String mCurrency) {
        mSharedPreferenceEditor.putString(CURRENCY, mCurrency);
        mSharedPreferenceEditor.commit();
    }

    public String getCurrency() {
        return mSharedPreferences.getString(CURRENCY, "");
    }

    /*
    * Currency
    * */
    public void setGameList(String mGameList) {
        mSharedPreferenceEditor.putString(GAME_LIST, mGameList);
        mSharedPreferenceEditor.commit();
    }

    public String getGameList() {
        return mSharedPreferences.getString(GAME_LIST, "");
    }

    /*
   * is cached
   * */
    public void setIsCached(boolean mGameList) {
        mSharedPreferenceEditor.putBoolean(IS_CACHED, mGameList);
        mSharedPreferenceEditor.commit();
    }

    public Boolean getIsCached() {
        return mSharedPreferences.getBoolean(IS_CACHED, false);
    }
}
