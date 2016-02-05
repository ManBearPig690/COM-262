package com.murach.newsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class LowBatteryReciever extends BroadcastReceiver {
    public LowBatteryReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter inf = new IntentFilter();
        inf.addAction("android.intent.action.BATTERY_LOW");
        Log.d("New Reader", "Low Battery");

    }
}
