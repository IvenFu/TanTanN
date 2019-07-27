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
import com.netease.net.detector.sdk.qos.QosInfo;
import com.netease.net.detector.sdk.report.ReportStats;
import com.netease.net.detector.sdk.telnet.TelnetInfo;
import com.netease.net.detector.sdk.traceroute.TraceRouteInfo;

import java.util.ArrayList;
import java.util.List;

import hack.com.tantan.detail.contract.DetailContractView;
import hack.com.tantan.utils.ServiceDataHandler;

public class DetailPresenter {
    private DetailContractView mContract;
    private static final String EXECUTE_NAME = "DetailPresenter.executeNetDetector";

    public DetailPresenter(DetailContractView contractView) {
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
                    mContract.onDetectFinished();
                    //可以拿到下发的配置
                    NetDetectorConfig config = task.getConfig();
                    ReportStats reportStats = task.getCurrentReportStats();
                    List<QosInfo> qosInfoList = new ArrayList<>();
                    QosInfo qosInfo = new QosInfo();
                    qosInfo.setHost("");
                    qosInfo.setRtt(1);
                    qosInfo.setDownloadBandwidth(1);
                    qosInfo.setDownloadLossRate(0.0);
                    qosInfo.setUploadBandwidth(1);
                    qosInfo.setUploadLossRate(0.0);
                    reportStats.setQosInfoList(qosInfoList);
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
            String text = "获取配置中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void configSyncCallback(NetDetectorConfig netDetectorConfig) {
            mContract.printDetecting("");
            mContract.addDetectResult("获取配置成功\n");
        }

        @Override
        public void startGetIpInfo() {
            String text = "获取IP信息中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void ipInfoCallback(IpInfo ipInfo) {
            mContract.addDetectResult(ServiceDataHandler.castIpInfoToString(ipInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startGetDnsInfo() {
            String text = "获取DNS信息中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void dnsInfoCallback(DnsInfo dnsInfo) {
            mContract.addDetectResult(ServiceDataHandler.castDnsInfoToString(dnsInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startCheckIPv6Info() {

            String text = "检测IPV6中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void ipv6InfoCallback(IPv6Info iPv6Info) {
            mContract.addDetectResult(ServiceDataHandler.castIpv6InfoToString(iPv6Info));
            mContract.printDetecting("");
        }

        @Override
        public void startPing(String s) {
            String text = "PING任务进行中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void pingInfoCallback(PingInfo pingInfo) {
            mContract.addDetectResult(ServiceDataHandler.castPingInfoToString(pingInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startDig(String s) {
            String text = "Dig任务进行中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void digInfoCallback(DigInfo digInfo) {
            mContract.addDetectResult(ServiceDataHandler.castDigInfoToString(digInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startTelnet(String s, int i) {
            String text = "Telnet任务进行中...";
            mContract.printDetecting(text);
            mContract.addDetectResult(text);
        }

        @Override
        public void telnetInfoCallback(TelnetInfo telnetInfo) {
            mContract.addDetectResult(ServiceDataHandler.castTelnetInfoToString(telnetInfo));
            mContract.printDetecting("");
        }

        @Override
        public void startTraceRoute(String host) {
            mContract.printDetecting("路由探测中...");
            mContract.addDetectResult("TraceRoute开始探测，域名：" + host);
        }

        @Override
        public void traceRouteTargetIp(String host, String ip) {
            mContract.addDetectResult("目标IP：" + ip);
        }

        @Override
        public void traceRouteCallback(String host, TraceRouteInfo.NodeInfo nodeInfo) {
            StringBuilder sb = new StringBuilder();
            sb.append("第").append(nodeInfo.getIndex()).append("跳, ");
            sb.append("ip=").append(nodeInfo.getIp()).append(", ");

            PingInfo pingInfo = nodeInfo.getPingInfo();
            if (pingInfo == null || pingInfo.getRtt() == null) {
                sb.append("loss=*, rtt=*.*.*, ");
            } else {
                PingInfo.Rtt rtt = pingInfo.getRtt();
                sb.append(String.format("loss=%s, rtt=%s/%s/%s, ", pingInfo.getLoss(), rtt.getMin(), rtt.getAvg(), rtt.getMax()));
            }
            IpInfo ipInfo = nodeInfo.getIpInfo();
            if (ipInfo == null) {
                sb.append("*/*/*/*");
            } else {
                sb.append(String.format("%s/%s/%s/%s", ipInfo.getCountry(), ipInfo.getProvince(), ipInfo.getCity(), ipInfo.getCarrier()));
            }
            mContract.addDetectResult(sb.toString());
        }

        @Override
        public void traceRouteDown(String host) {
            mContract.addDetectResult("TraceRoute探测完成，域名: " + host + "\n");
        }

        @Override
        public void onError(Exception e) {
            mContract.onError("获取失败" + e.getMessage());
        }
    }
}
