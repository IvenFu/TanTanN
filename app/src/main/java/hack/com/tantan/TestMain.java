package hack.com.tantan;

import com.netease.net.detector.sdk.Task;
import com.netease.net.detector.sdk.config.NetDetectorConfig;
import com.netease.net.detector.sdk.report.ReportStats;

/**
 *
 * Created by yuanyuanjun on 2019/7/26.
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        Task task = new Task();
        //做一些自带的检测
        task.execute();
        //可以拿到下发的配置
        NetDetectorConfig config = task.getConfig();
        ReportStats reportStats = task.getCurrentReportStats();
        String reportId = task.closeAndReport();
        System.out.println(reportId);
    }
}
