package hack.com.tantan.test;

import android.util.Log;

public interface CallbackBase {

    public static final String InterfaceName="CallBack";

    public void call(int data);
    public void call(float data);
    public void call(int data1, float data2);

}


