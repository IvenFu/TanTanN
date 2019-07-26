package hack.com.tantan.test;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import hack.com.tantan.JavaUtils;

public class NetworkStatistic {


   private final String  TAG = "NetworkStatistic";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"handle message" + msg.what);
        }
    };

    public void uploadBw(JavaUtils javaUtils , String probeIP){

        javaUtils.probingUpBw(probeIP);

        int upBw = 0;

        for(int i =0 ;i<10 ;i++) {
            upBw = javaUtils.getUpBw();

            Log.i(TAG, "network return upBw: " + upBw);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void downloadBw(JavaUtils javaUtils , String probeIP){

        javaUtils.probingUpBw(probeIP);

        int downBw = 0;

        for(int i =0 ;i<10 ;i++) {
            downBw = javaUtils.getDownBw();

            Log.i(TAG, "network return downBw: " + downBw);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }


    public void lostrateAndRTT(JavaUtils javaUtils , String probeIP){

        javaUtils.probingLostrateAndRTT(probeIP);

        int rtt = 0;
        float lossRate = 0;

        for(int i =0 ; i<10;i++) {
            rtt = javaUtils.getRTT();
            lossRate = javaUtils.getLossrate();
            Log.i(TAG, "network return lostrateAndRTT lossRate :" + lossRate+ " RTT " + rtt );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
