package ch.unige.carron8.bachelor.controllers;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import java.util.Date;

import ch.unige.carron8.bachelor.views.MainActivity;

/**
 * Created by Blaise on 06.06.2015.
 */
public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null){
            wakeLock.release();
        }

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        Log.d("WakeLock",wakeLock.toString()+" acquired at "+new Date(System.currentTimeMillis()).toString());
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null){
            Log.d("WakeLock",wakeLock.toString()+" released at "+new Date(System.currentTimeMillis()).toString());
            wakeLock.release();
        }
        wakeLock = null;
    }
}