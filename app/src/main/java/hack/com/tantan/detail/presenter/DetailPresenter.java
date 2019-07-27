package hack.com.tantan.detail.presenter;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Pair;

import com.netease.net.detector.sdk.Task;
import com.netease.net.detector.sdk.config.NetDetectorConfig;
import com.netease.net.detector.sdk.exception.NetDetectorException;
import com.netease.net.detector.sdk.report.ReportStats;

import javax.security.auth.callback.Callback;

import hack.com.tantan.detail.contract.DetailContractView;

public class DetailPresenter {
    private DetailContractView mContract;
    private static final String EXECUTE_NAME = "DetailPresenter.execute";
    public DetailPresenter(DetailContractView contractView){
        mContract = contractView;
    }

    public void execute() {
        HandlerThread thread = new HandlerThread(EXECUTE_NAME);
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Task task = new Task();
                //做一些自带的检测
                try {
                    task.execute();
                    //可以拿到下发的配置
                    NetDetectorConfig config = task.getConfig();
                    ReportStats reportStats = task.getCurrentReportStats();
                    String reportId = task.closeAndReport();
                    Log.e("@CJL/reportId", reportId);
                } catch (NetDetectorException e) {
                    e.printStackTrace();
                    Log.e("@CJL/Exception", e.getMessage());
                }
            }
        });
    }
}
