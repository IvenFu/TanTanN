package hack.com.tantan.utils;

import android.os.Bundle;

import com.netease.net.detector.sdk.dig.DigInfo;
import com.netease.net.detector.sdk.dns.DnsInfo;
import com.netease.net.detector.sdk.ip.IpInfo;
import com.netease.net.detector.sdk.ipv6.IPv6Info;
import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.telnet.TelnetInfo;

import java.util.ArrayList;

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
        return "Ping信息探测成功！" + "\n"
                + "主机：" + info.getHost() + "\n"
                + "丢包率：" + info.getLoss() + "\n"
                + "RTT：" + castRttToString(info.getRtt()) + "\n";
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
        return "Telnet信息探测成功！" + "\n"
                + "主机：" + info.getHost() + "\n"
                + "端口：" + info.getPort() + "\n";
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

        return "Dig信息探测成功！" + "\n"
                + "主机：" + info.getHost() + "\n"
                + "IP列表" + sb.substring(0, sb.length() - separator.length()) + "\n";
    }

    public static Bundle castDnsInfo(DnsInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString("Dns", info.getDns());
        bundle.putString("Gateway", info.getGateway());
        return bundle;
    }

    public static String castDnsInfoToString(DnsInfo info) {
        return "DNS信息探测成功！" + "\n"
                + "DNS服务器地址：" + info.getDns() + "\n"
                + "DNS网关地址：" + info.getGateway() + "\n";
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
        return "IP信息探测成功！" + "\n"
                + "本机IP：" + info.getIp() + "\n"
                + "国家：" + info.getCountry() + "\n"
                + "省份：" + info.getProvince() + "\n"
                + "城市：" + info.getCarrier() + "\n"
                + "运营商：" + info.getCarrier() + "\n"
                + "经度：" + info.getLongitude() + "\n"
                + "纬度：" + info.getLatitude() + "\n"
                + "时区：" + info.getTimezone() + "\n";
    }

    public static Bundle castIpv6Info(IPv6Info info) {
        Bundle bundle = new Bundle();
        bundle.putString("Ip", info.getIp());
        bundle.putBoolean("Support", info.isSupport());
        return bundle;
    }

    public static String castIpv6InfoToString(IPv6Info info) {
        StringBuilder sb = new StringBuilder();
        sb.append("IPv6信息探测成功！\n");
        if (info.isSupport()) {
            sb.append("本机IPv6 IP：").append(info.getIp());
        } else {
            sb.append("本机不支持IPv6");
        }
        sb.append("\n");
        return sb.toString();
    }
}
