package hack.com.tantan.detail.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.netease.net.detector.sdk.Task;
import com.netease.net.detector.sdk.config.NetDetectorConfig;
import com.netease.net.detector.sdk.exception.NetDetectorException;
import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.report.ReportStats;

import java.util.List;

import hack.com.tantan.detail.contract.DetailContractView;
import kotlin.Triple;

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
                    task.execute();
                    //可以拿到下发的配置
                    NetDetectorConfig config = task.getConfig();
                    ReportStats reportStats = task.getCurrentReportStats();
                    mContract.displayReportStats(reportStats);
                    String reportId = task.report();
                    Log.e("@CJL/reportId", reportId);
                } catch (NetDetectorException e) {
                    e.printStackTrace();
                    Log.e("@CJL/Exception", e.getMessage());
                }
            }
        });
    }
}
