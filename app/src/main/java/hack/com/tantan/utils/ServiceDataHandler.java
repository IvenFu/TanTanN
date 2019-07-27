package hack.com.tantan.utils;

import android.os.Bundle;

import com.netease.net.detector.sdk.dig.DigInfo;
import com.netease.net.detector.sdk.dns.DnsInfo;
import com.netease.net.detector.sdk.ip.IpInfo;
import com.netease.net.detector.sdk.ipv6.IPv6Info;
import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.telnet.TelnetInfo;

import java.util.ArrayList;
import java.util.List;

public class ServiceDataHandler {

    public static Bundle castPingInfo(PingInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putString("OriginalResponse", info.getOriginalResponse());
        bundle.putDouble("Loss", info.getLoss());
        bundle.putDouble("Rtt", info.getRtt().getAvg());
        return bundle;
    }

    public static String castPingInfoToString(PingInfo info) {
        return "\n主机：" + info.getHost() +
                "\n丢包率：" + info.getLoss() +
                "\nRTT：" + castRttToString(info.getRtt());
    }

    private static String castRttToString(PingInfo.Rtt rtt) {
        return rtt.getAvg() + "/" + rtt.getMin() + "/" + rtt.getMax() + "/" + rtt.getMdev();
    }

    public static Bundle castTelnetInfo(TelnetInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putInt("Port", info.getPort());
        return bundle;
    }

    public static String castTelnetInfoToString(TelnetInfo info) {
        return "\n主机：" + info.getHost() +
                "\n端口：" + info.getPort();
    }

    public static Bundle castDigInfo(DigInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putStringArrayList("IpList", new ArrayList<String>(info.getIpList()));
        return bundle;
    }

    public static String castDigInfoToString(DigInfo info) {
        StringBuilder sb = new StringBuilder();
        String separator = ", ";
        for (String ip : info.getIpList()) {
            sb.append(ip).append(separator);
        }

        return "\n主机：" + info.getHost() +
                "\nIP列表" + sb.substring(0, sb.length() - separator.length());
    }

    public static Bundle castDnsInfo(DnsInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Dns", info.getDns());
        bundle.putString("Gateway", info.getGateway());
        return bundle;
    }

    public static String castDnsInfoToString(DnsInfo info) {
        return "\nDNS服务器地址：" + info.getDns() +
                "\nDNS网关地址：" + info.getGateway();
    }

    public static Bundle castIpInfo(IpInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Ip", info.getIp());
        bundle.putString("Country", info.getCountry());
        bundle.putString("Province", info.getProvince());
        bundle.putString("Carrier", info.getCarrier());
        bundle.putDouble("Longitude", info.getLongitude());
        bundle.putDouble("Latitude", info.getLatitude());
        bundle.putString("Timezone", info.getTimezone());
        return bundle;
    }

    public static String castIpInfoToString(IpInfo info) {
        return "\n本机IP：" + info.getIp() +
                "\n国家：" + info.getCountry() +
                "\n省份：" + info.getProvince() +
                "\n城市：" + info.getCarrier() +
                "\n运营商：" + info.getCarrier() +
                "\n经度：" + info.getLongitude() +
                "\n纬度：" + info.getLatitude() +
                "\n时区：" + info.getTimezone();
    }

    public static Bundle castIpv6Info(IPv6Info info) {
        Bundle bundle = new Bundle();
        bundle.putString("Ip", info.getIp());
        bundle.putBoolean("Support", info.isSupport());
        return bundle;
    }

    public static String castIpv6InfoToString(IPv6Info info) {
        return "\n本机IP：" + info.getIp() +
                "\n是否支持：" + (info.isSupport() ? "支持" : "不支持");
    }
}
