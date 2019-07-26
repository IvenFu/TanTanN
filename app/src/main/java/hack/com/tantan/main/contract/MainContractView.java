package hack.com.tantan.main.contract;

public interface MainContractView {
    /**
     * 开始检测后立即回调
     */
    void onSelectingBaseServer();

    /**
     * 检测开始后超时(1min内无连接 )
     */
    void onNoConnection();

    void onGetHostLocationFailed();

    void onGetHostLocation(String location, double distance);

    void reset();

    void onPingTestFinished();
}
