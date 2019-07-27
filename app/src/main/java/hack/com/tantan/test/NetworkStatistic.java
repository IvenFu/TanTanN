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

    public void uploadBw(final JavaUtils javaUtils ,final String probeIP){

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {

                javaUtils.probingUpBw(probeIP);

                ///probe may take some time, so we sleep a while.
                try {
                    Thread.sleep(mHeartBeat);
                } catch (InterruptedException e) {
                }
                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    int upBw = javaUtils.getUpBw();
                    mCallback.onUploadBwCallback(upBw);
                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "network return upBw end ");
            }
        };

        mMonitorHandler.post(mStatsRunnable);


    }

    public void downloadBw(final  JavaUtils javaUtils , final String probeIP){

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {
                javaUtils.probingDownBw(probeIP);

                ///probe may take some time, so we sleep a while.
                try {
                    Thread.sleep(mHeartBeat);
                } catch (InterruptedException e) {
                }

                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    int downBw = javaUtils.getDownBw();
                    mCallback.onDownloadBwCallback(downBw);

                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "network return downBw end ");
            }
        };

        mMonitorHandler.post(mStatsRunnable);
    }

    public void lostrateAndRTT(final  JavaUtils javaUtils , final  String probeIP){

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {

                javaUtils.probingLostrateAndRTT(probeIP);
                ///probe may take some time, so we sleep a while.
                try {
                    Thread.sleep(mHeartBeat);
                } catch (InterruptedException e) {
                }

                for(int i =0 ;i<mHeartBeatCount ;i++) {

                    int rtt = javaUtils.getRTT();
                    float upLossrate = javaUtils.getLossrate();

                    mCallback.onRTTandUploadLossCallback(rtt,upLossrate);
                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "network return lossrate and  RTT end ");

            }
        };

        mMonitorHandler.post(mStatsRunnable);
    }

    public void downloadLossrate(final  JavaUtils javaUtils , final String probeIP){

        mStatsRunnable =  new Runnable() {
            @Override
            public void run() {

                javaUtils.probingDownLostrate(probeIP);
                ///probe may take some time, so we sleep a while.
                try {
                    Thread.sleep(mHeartBeat);
                } catch (InterruptedException e) {
                }

                for(int i =0 ;i<mHeartBeatCount ;i++) {
                    float downLossrate = javaUtils.getDownLossrate();

                    mCallback.onDownloadLossCallback(downLossrate);
                    try {
                        Thread.sleep(mHeartBeat);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "network return download lossrate  end");
            }

        };

        mMonitorHandler.post(mStatsRunnable);
    }
}
