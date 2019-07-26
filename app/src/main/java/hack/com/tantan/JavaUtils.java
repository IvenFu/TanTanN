package hack.com.tantan;


public class JavaUtils {
    static {
        System.loadLibrary("jnitest");
    }

    public native static String getString();
}
