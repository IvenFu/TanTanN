package hack.com.tantan.test;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import hack.com.tantan.JavaUtils;

public class NetworkStatistic {


   private final String  TAG = "NetworkStatistic";

    CallbackBase mCallback;
    int mHeartBeat;
    int mHeartBeatCount;

    private Runnable mStatsRunnable;
    HandlerThread monitor;

    private Handler mMonitorHandler;

    public  NetworkStatistic(int heartBeat, int heartBeatCount, CallbackBase callbackBase){

        mHeartBeat = heartBeat;
        mCallback = callbackBase;
        mHeartBeatCount = heartBeatCount;

        monitor = new HandlerThread("network_monitor");
        monitor.start();
        mMonitorHandler = new Handler(monitor.getLooper());
    }

    public void uploadBw(final JavaUtils javaUtils , String probeIP){

        javaUtils.probingUpBw(probeIP);

        ///probe may take some time, so we sleep a while.
        try {
            Thread.sleep(mHeartBeat);
        } catch (InterruptedException e) {
        }

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {
                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    int upBw = javaUtils.getUpBw();
                    mCallback.call(upBw);
                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };

        mMonitorHandler.post(mStatsRunnable);
        Log.i(TAG, "network return upBw end ");

    }

    public void downloadBw(final  JavaUtils javaUtils , String probeIP){

        javaUtils.probingDownBw(probeIP);

        ///probe may take some time, so we sleep a while.
        try {
            Thread.sleep(mHeartBeat);
        } catch (InterruptedException e) {
        }

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {
                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    int downBw = javaUtils.getDownBw();
                    mCallback.call(downBw);

                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };

        mMonitorHandler.post(mStatsRunnable);
        Log.i(TAG, "network return downBw end ");
    }

    public void lostrateAndRTT(final  JavaUtils javaUtils , String probeIP){

        javaUtils.probingLostrateAndRTT(probeIP);
        ///probe may take some time, so we sleep a while.
        try {
            Thread.sleep(mHeartBeat);
        } catch (InterruptedException e) {
        }

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {
                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    int rtt = javaUtils.getRTT();
                    float upLossrate = javaUtils.getLossrate();
                    mCallback.call(rtt,upLossrate);
                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };

        mMonitorHandler.post(mStatsRunnable);
        Log.i(TAG, "network return lossrate and  RTT end ");
    }
}
