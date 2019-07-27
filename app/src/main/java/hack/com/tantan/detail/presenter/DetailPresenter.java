package hack.com.tantan.detail.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.netease.net.detector.sdk.Task;
import com.netease.net.detector.sdk.TaskCallback;
import com.netease.net.detector.sdk.config.NetDetectorConfig;
import com.netease.net.detector.sdk.dig.DigInfo;
import com.netease.net.detector.sdk.dns.DnsInfo;
import com.netease.net.detector.sdk.exception.NetDetectorException;
import com.netease.net.detector.sdk.ip.IpInfo;
import com.netease.net.detector.sdk.ipv6.IPv6Info;
import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.report.ReportStats;
import com.netease.net.detector.sdk.telnet.TelnetInfo;

import hack.com.tantan.detail.contract.DetailContractView;
import hack.com.tantan.utils.ServiceDataHandler;

public class DetailPresenter {
    private DetailContractView mContract;
    private static final String EXECUTE_NAME = "DetailPresenter.executeNetDetector";
    public DetailPresenter(DetailContractView contractView){
        mContract = contractView;
    }

    public void executeNetDetector() {
        HandlerThread thread = new HandlerThread(EXECUTE_NAME);
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Task task = new Task();
                //做一些自带的检测
                try {
                    Log.i("@CJL/进度", "准备执行 ");
                    task.execute(new ReportTaskCallback());
                    //可以拿到下发的配置
                    NetDetectorConfig config = task.getConfig();
                    ReportStats reportStats = task.getCurrentReportStats();
                    String reportId = task.report();
                    Log.e("@CJL/reportId", reportId);
                } catch (NetDetectorException e) {
                    e.printStackTrace();
                    Log.e("@CJL/Exception", e.getMessage());
                }
            }
        });
    }


    public class ReportTaskCallback implements TaskCallback {

        @Override
        public void startSyncConfig() {
            mContract.printDetecting("获取配置中...");
        }

        @Override
        public void configSyncCallback(NetDetectorConfig netDetectorConfig) {
            mContract.printDetecting("");
        }

        @Override
        public void startGetIpInfo() {
            mContract.printDetecting("获取IP信息中...");
        }

        @Override
        public void ipInfoCallback(IpInfo ipInfo) {
            mContract.addDetectResult(ServiceDataHandler.castIpInfoToString(ipInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startGetDnsInfo() {
            mContract.printDetecting("获取DNS信息中...");
        }

        @Override
        public void dnsInfoCallback(DnsInfo dnsInfo) {
            mContract.addDetectResult(ServiceDataHandler.castDnsInfoToString(dnsInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startCheckIPv6Info() {

            mContract.printDetecting("检测IPV6中...");
        }

        @Override
        public void ipv6InfoCallback(IPv6Info iPv6Info) {
            mContract.addDetectResult(ServiceDataHandler.castIpv6InfoToString(iPv6Info));
            mContract.printDetecting("");

        }

        @Override
        public void startPing(String s) {
            mContract.printDetecting("PING任务进行中...");

        }

        @Override
        public void pingInfoCallback(PingInfo pingInfo) {
            mContract.addDetectResult(ServiceDataHandler.castPingInfoToString(pingInfo));
            mContract.printDetecting("");

        }

        @Override
        public void startDig(String s) {
            mContract.printDetecting("Dig任务进行中...");

        }

        @Override
        public void digInfoCallback(DigInfo digInfo) {
            mContract.addDetectResult(ServiceDataHandler.castDigInfoToString(digInfo));
            mContract.printDetecting("");

        }

        @Override
        public void startTelnet(String s, int i) {
            mContract.printDetecting("Telnet任务进行中...");

        }

        @Override
        public void telnetInfoCallback(TelnetInfo telnetInfo) {
            mContract.addDetectResult(ServiceDataHandler.castTelnetInfoToString(telnetInfo));
            mContract.printDetecting("");

        }

        @Override
        public void onError(Exception e) {
            mContract.onError(e);
        }
    }
}
