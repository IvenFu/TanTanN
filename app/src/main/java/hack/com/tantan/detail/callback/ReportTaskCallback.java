package hack.com.tantan.detail.callback;

import com.netease.net.detector.sdk.TaskCallback;
import com.netease.net.detector.sdk.config.NetDetectorConfig;
import com.netease.net.detector.sdk.dig.DigInfo;
import com.netease.net.detector.sdk.dns.DnsInfo;
import com.netease.net.detector.sdk.ip.IpInfo;
import com.netease.net.detector.sdk.ipv6.IPv6Info;
import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.telnet.TelnetInfo;

public class ReportTaskCallback implements TaskCallback {
    @Override
    public void startSyncConfig() {

    }

    @Override
    public void configSyncCallback(NetDetectorConfig netDetectorConfig) {

    }

    @Override
    public void startGetIpInfo() {

    }

    @Override
    public void ipInfoCallback(IpInfo ipInfo) {

    }

    @Override
    public void startGetDnsInfo() {

    }

    @Override
    public void dnsInfoCallback(DnsInfo dnsInfo) {

    }

    @Override
    public void startCheckIPv6Info() {

    }

    @Override
    public void ipv6InfoCallback(IPv6Info iPv6Info) {

    }

    @Override
    public void startPing(String s) {

    }

    @Override
    public void pingInfoCallback(PingInfo pingInfo) {

    }

    @Override
    public void startDig(String s) {

    }

    @Override
    public void digInfoCallback(DigInfo digInfo) {

    }

    @Override
    public void startTelnet(String s, int i) {

    }

    @Override
    public void telnetInfoCallback(TelnetInfo telnetInfo) {

    }

    @Override
    public void onError(Exception e) {

    }
}
