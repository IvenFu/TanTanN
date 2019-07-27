package hack.com.tantan.test;

import android.util.Log;

public interface CallbackBase {

    public static final String InterfaceName="CallBack";

    public void onRTTandUploadLossCallback(int rtt,float uploadLoss);
    public void onUploadBwCallback(int upBw);
    public void onDownloadBwCallback(int downBw);
    public void onDownloadLossCallback(float downloadLoss);
}


