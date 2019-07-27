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
    public static Bundle castPingInfo(PingInfo info){
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putString("OriginalResponse", info.getOriginalResponse());
        bundle.putDouble("Loss", info.getLoss());
        bundle.putDouble("Rtt", info.getRtt().getAvg());
        return bundle;
    }

    public static Bundle castTelnetInfo(TelnetInfo info){
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putInt("Port", info.getPort());
        return bundle;
    }

    public static Bundle castDigInfo(DigInfo info){
        Bundle bundle = new Bundle();
        bundle.putString("Host", info.getHost());
        bundle.putStringArrayList("IpList", new ArrayList<String>(info.getIpList()));
        return bundle;
    }

    public static Bundle castDnsInfo(DnsInfo info){
        Bundle bundle = new Bundle();
        bundle.putString("Dns", info.getDns());
        bundle.putString("Gateway", info.getGateway());
        return bundle;
    }

    public static Bundle castIpInfo(IpInfo info){
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

    public static Bundle castIpvtInfo(IPv6Info info){
        Bundle bundle = new Bundle();
        bundle.putString("Ip", info.getIp());
        bundle.putBoolean("Support", info.isSupport());
        return bundle;
    }

    public static String castRttToString(PingInfo.Rtt rtt){
        return rtt.getAvg()+"/"+rtt.getMin()+"/" + rtt.getMax()+"/"+rtt.getMdev();
    }
}
