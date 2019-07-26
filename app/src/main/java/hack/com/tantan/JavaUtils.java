package hack.com.tantan;


import android.util.Log;

public class JavaUtils {

    private static final String TAG = "VideoNative";
    private long nativeNetwork;

    static {
        System.loadLibrary("jnitest");
    }

    public void  createNativeNetwork(){

        nativeNetwork = createNetwork(nativeNetwork);
        Log.d(TAG, "createNativeNetwork " + nativeNetwork);
    }

    public  void  init(){
        init(nativeNetwork);
    }

    public void uninit(){
        uninit(nativeNetwork);
    }


    public String getString(){

        return getString(nativeNetwork);
    }

    private native  long createNetwork(long nativeNetwork);
    private native  void  init(long nativeNetwork);
    private native  void  uninit(long nativeNetwork);

    private native  String  getString(long nativeNetwork);

    /*//ø™∆Ù∂™∞¸¬ RTTÃΩ≤‚
    public native int ProbingLostrateAndRTT(nativeNetwork);

    //ªÒ»°Ω·π˚
    public native int GetLostrateAndRTT( nativeNetwork,int pRttMs, float pfLossRate);

    //ø™∆Ù…œ––¥¯øÌÃΩ≤‚
    public native int ProbingUpBw(nativeNetwork);

    //ªÒ»°Ω·π˚
    public native int GetUpBw(nativeNetwork,int pBitrate);

    //ø™∆Ùœ¬––¥¯øÌÃΩ≤‚
    public native int ProbingDownBw(nativeNetwork);

    //ªÒ»°Ω·π˚
    public native int GetDownBw(nativeNetwork,int pBitrate);*/
}
