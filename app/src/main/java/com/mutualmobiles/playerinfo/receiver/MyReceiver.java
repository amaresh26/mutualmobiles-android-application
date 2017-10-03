package com.mutualmobiles.playerinfo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mutualmobiles.playerinfo.utils.SessionManager;

/**
 * Created by amareshjana on 24/09/17.
 */

public class MyReceiver extends BroadcastReceiver {
    private static SessionManager mSessionManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mSessionManager = new SessionManager(context);
        clearCache(context);
    }

    public static void clearCache(Context mContext) {
        mSessionManager.setIsCached(false);
        mSessionManager.setGameList("");
    }
}