package hack.com.tantan.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.net.detector.sdk.config.ConfigService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hack.com.tantan.JavaUtils;
import hack.com.tantan.R;
import hack.com.tantan.detail.DetailActivity;
import hack.com.tantan.main.contract.MainContractView;
import hack.com.tantan.main.presenter.MainPresenter;
import hack.com.tantan.test.CallbackBase;
import hack.com.tantan.test.NetworkStatistic;
import hack.com.tantan.utils.XYMultipleSeriesRendererHandler;


public class MainActivity extends AppCompatActivity implements MainContractView, View.OnClickListener {
    private final String TAG = "MainActivity";
    private static final int HEART_BEAT = 500;
    private static final int HEART_BEAT_COUNT = 10;
    private static final String PROBE_IP = "184.170.218.205";

    private static int mPosition = 0;
    private static int mLastPosition = 0;
    private Button mStartButton = null;
    private Button mGoToDetailButton = null;
    private ImageView mBarImageView = null;
    private TextView mRttTV = null;
    private TextView mLossRateTV = null;
    private TextView mDownloadBwTV = null;
    private TextView mUploadBwTV = null;
    private LinearLayout mRttLL = null;
    private LinearLayout mLossRateLL = null;
    private LinearLayout mDownloadBwLL = null;
    private LinearLayout mUploadBwLL = null;
    private MainPresenter mPresenter = null;
    private RotateAnimation mRotate;

    private final List<Double> mDownLossRateList = new ArrayList<>();
    private final List<Double> mUploadBwList = new ArrayList<>();
    private final List<Double> mDownloadBwList = new ArrayList<>();

    public static double mUploadBw;
    public static double mDownloadBw;
    public static double mDownLossRate;
    public static double mUploadLossRate;
    public static double mRtt;

    private final List<Double> mRttList = new ArrayList<>();
    private final List<Double> mUploadRateLossList = new ArrayList<>();
    private int mGetDownloadBwCount = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(this);
        //沉浸式状态栏
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary2));

        setContentView(R.layout.activity_main);
        findView();
        mStartButton.setOnClickListener(this);
        mStartButton.setText("Begin Test");

        mGoToDetailButton.setOnClickListener(this);
    }

    private void findView() {
        mStartButton = findViewById(R.id.btn_start);
        mGoToDetailButton = findViewById(R.id.btn_detail);
        mBarImageView = findViewById(R.id.img_bar);
        mRttTV = findViewById(R.id.tv_rtt);
        mLossRateTV = findViewById(R.id.tv_dw_loss_rate_chart);
        mDownloadBwTV = findViewById(R.id.tv_download);
        mUploadBwTV = findViewById(R.id.tv_upload);
        mRttLL = findViewById(R.id.ll_rtt_chart);
        mLossRateLL = findViewById(R.id.ll_dw_loss_rate_chart);
        mDownloadBwLL = findViewById(R.id.ll_download_chart);
        mUploadBwLL = findViewById(R.id.ll_upload_chart);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                 mStartButton.setEnabled(false);
                 networkUpdate();
                break;
            case R.id.btn_detail:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void networkUpdate() {

        final JavaUtils javaUtils = new JavaUtils();
        javaUtils.createNativeNetwork();

        Callback callback = new Callback();


        final NetworkStatistic networkStatistic = new NetworkStatistic(HEART_BEAT, HEART_BEAT_COUNT, callback);

        try {
            String ip = new ConfigService().get().getQosList().get(0);
            networkStatistic.setProbeIP(ip);
        } catch (Exception e) {
            networkStatistic.setProbeIP(PROBE_IP);
        }

        networkStatistic.lostrateAndRTT(javaUtils);
        networkStatistic.downloadLossrate(javaUtils);
        networkStatistic.uploadBw(javaUtils);
        networkStatistic.downloadBw(javaUtils);
    }

    ///callback data from NetworkStatistic
    public class Callback implements CallbackBase {

        @Override
        public void onRTTandUploadLossCallback(final int rtt, final float uploadLoss) {
            Log.i(TAG, "onRTTandUploadLossCallback uploadLoss " + uploadLoss + " rtt " + rtt);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRttList.add((double) rtt);

                    Double sum = 0.0;
                    double rtt_ = 0.0;
                    for(Double val: mRttList){
                        sum += val;
                    }

                    if(mRttList.size() != 0) {
                        rtt_ = sum / mRttList.size();
                    }

                    mRttTV.setText(String.format(Locale.CHINA, "%.2f Ms", (double) rtt_));
                    mRttLL.removeAllViews();
                    mRttLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(mRttList, getBaseContext()));
                    mRtt = rtt_;

                    mUploadRateLossList.add((double) uploadLoss);
                    sum =0.0;
                    for (double value : mUploadRateLossList){
                        sum += value;
                    }
                    mUploadLossRate = sum / mUploadRateLossList.size();
                }
            });
        }

        @Override
        public void onUploadBwCallback(final int upBw) {
            Log.i(TAG, "onUploadBwCallback  " + upBw);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double upBandWidth = upBw / 1024.0 / 1024;

                    mUploadBwList.add((double) upBandWidth);

                    Double sum = 0.0;
                    double upBw = 0.0;
                    for(Double val: mUploadBwList){
                        sum += val;
                    }

                    if(mUploadBwList.size() != 0) {
                        upBw = sum / mUploadBwList.size();
                    }

                    mUploadBwTV.setText(String.format(Locale.CHINA, "%.2f Mbps", (double) upBw));
                    mUploadBwLL.removeAllViews();
                    mUploadBwLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(mUploadBwList, getBaseContext()));
                    mUploadBw=upBw;

                    mPosition = getPositionByRate(upBandWidth);
                    mRotate = new RotateAnimation(mLastPosition, mPosition, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    mRotate.setFillAfter(true);
                    mRotate.setInterpolator(new LinearInterpolator());
                    mRotate.setDuration(100);
                    mBarImageView.startAnimation(mRotate);
                    mLastPosition = mPosition;
                }
            });
        }

        @Override
        public void onDownloadBwCallback(final int downBw) {
            Log.i(TAG, "onDownloadBwCallback  " + downBw);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    double downBandWidth = downBw / 1024.0 / 1024;

                    mDownloadBwList.add(downBandWidth);

                    Double sum = 0.0;
                    for(Double val: mDownloadBwList){
                        sum += val;
                    }
                    if(mDownloadBwList.size() != 0) {
                        downBandWidth = sum / mDownloadBwList.size();
                    }

                    mDownloadBwTV.setText(String.format(Locale.CHINA, "%.2f Mbps", (double) downBandWidth));
                    mDownloadBwLL.removeAllViews();
                    mDownloadBwLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(mDownloadBwList, getBaseContext()));
                    mDownloadBw = downBandWidth;

                    mPosition = getPositionByRate(downBandWidth);
                    mRotate = new RotateAnimation(mLastPosition, mPosition, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    mRotate.setFillAfter(true);
                    mRotate.setInterpolator(new LinearInterpolator());
                    mRotate.setDuration(100);
                    mBarImageView.startAnimation(mRotate);

                    mLastPosition = mPosition;
                }
            });
        }

        private int getPositionByRate(double rate) {
            if (rate <= 1) {
                return (int) (rate * 30);

            } else if (rate <= 10) {
                return (int) (rate * 6) + 30;

            } else if (rate <= 30) {
                return (int) ((rate - 10) * 3) + 90;

            } else if (rate <= 50) {
                return (int) ((rate - 30) * 1.5) + 150;

            } else if (rate <= 100) {
                return (int) ((rate - 50) * 1.2) + 180;
            }
            return 0;
        }

        @Override
        public void onDownloadLossCallback(final float downloadLoss) {
            Log.i(TAG, "onDownloadLossCallback  " + downloadLoss);

            synchronized (this) {
                mGetDownloadBwCount++;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownLossRateList.add((double) downloadLoss);

                    Double sum = 0.0;
                    double dnLossRate = 0;
                    for(Double val: mDownLossRateList){
                        sum += val;
                    }

                    if(mDownLossRateList.size() != 0) {
                        dnLossRate = sum / mDownLossRateList.size();
                    }

                    mLossRateTV.setText(String.format(Locale.CHINA, "%.2f", (double) dnLossRate));
                    mLossRateLL.removeAllViews();
                    mLossRateLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(mDownLossRateList, getBaseContext()));
                    mDownLossRate = dnLossRate;

                    synchronized (this) {
                        if (mGetDownloadBwCount >= HEART_BEAT_COUNT) {
                            mStartButton.setEnabled(true);
                        }
                    }

                }
            });
        }
    }

}

