package hack.com.tantan;


import android.util.Log;

public class JavaUtils {

    private static final String TAG = "networkNative";
    private long nativeNetwork;

    static {
        System.loadLibrary("jnitest");
    }


    public void  createNativeNetwork(){

        nativeNetwork = createNetwork(nativeNetwork);
        Log.i(TAG, "createNativeNetwork " + nativeNetwork);
    }

    public  void  init(){
        createNativeNetwork();
        init(nativeNetwork);
    }

    public void uninit(){
        uninit(nativeNetwork);
    }

    public int probingLostrateAndRTT(String probeIP){
        return ProbingLostrateAndRTT(nativeNetwork, probeIP);
    }
    public float getLossrate(){
        return GetLossrate(nativeNetwork);
    }

    public int getRTT(){
        return GetRTT(nativeNetwork);
    }

    public int probingUpBw(String probeIP){
        return ProbingUpBw(nativeNetwork,probeIP);
    }

    public int getUpBw(){
        return GetUpBw(nativeNetwork);
    }

    public int probingDownBw(String probeIP){
        return ProbingDownBw(nativeNetwork,probeIP);
    }

    public int getDownBw(){
        return GetDownBw(nativeNetwork);
    }


    public int probingDownLostrate (String probeIP){
        return ProbingDownLossrate(nativeNetwork,probeIP);
    }

    public float getDownLossrate () {
        return GetDownLossrate(nativeNetwork);
    }

    private native  long createNetwork(long nativeNetwork);
    private native  void  init(long nativeNetwork);
    private native  void  uninit(long nativeNetwork);

    public native int ProbingLostrateAndRTT(long nativeNetwork,String probeIP);
    //float pfLossRate
    public native float GetLossrate(long nativeNetwork);
    //int pRttMs
    public native  int  GetRTT(long nativeNetwork);
    //String szServerIp
    public native int ProbingUpBw(long nativeNetwor,String probeIP);

    //int pBitrate
    public native int GetUpBw(long nativeNetwork);

    //String szServerIp
    public native int ProbingDownBw(long nativeNetwork,String probeIP);

    //int pBitrate
    public native int GetDownBw(long nativeNetwork);

    //String szServerIp
    public native int ProbingDownLossrate(long nativeNetwork,String probeIP);
    //float pfLossRate
    public native float  GetDownLossrate(long nativeNetwork);


}
