package hack.com.tantan.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
import android.widget.Toast;

import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
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
    private static int mPosition = 0;
    private static int mLastPosition = 0;
    private HashSet<String> mTempBlackList;
    private Button mStartButton = null;
    private DecimalFormat mDec = null;
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

    /**
     * 测试用进程的名称
     */
    private static final String PROB_THREAD_NAME = "ProbThread";

    final List<Double> downLossRateList = new ArrayList<>();

    final List<Double> uploadBwList = new ArrayList<>();
    final List<Double> downloadBwList = new ArrayList<>();

    final List<Double> rttList = new ArrayList<>();


    int getDownloadBwCount = 0 ;
    int heartBeat = 500;
    int hearBeatCount = 10;
    String probeIP = "184.170.218.205";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary2));

        setContentView(R.layout.activity_main);
        findView();

        mDec = new DecimalFormat("#.##");
        mTempBlackList = new HashSet<>();
        mStartButton.setOnClickListener(this);
        mStartButton.setText("Begin Test");

        mGoToDetailButton.setOnClickListener(this);

//        mRotate = new RotateAnimation(mLastPosition, mPosition, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mRotate.setInterpolator(new LinearInterpolator());
//        mRotate.setDuration(100);
//        mBarImageView.startAnimation(mRotate);

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

    @Override
    public void onSelectingBaseServer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartButton.setText("Selecting best server based on ping...");
            }
        });
    }

    @Override
    public void onNoConnection() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                mStartButton.setEnabled(true);
                mStartButton.setTextSize(16);
                mStartButton.setText("Restart Test");
            }
        });
    }

    @Override
    public void onGetHostLocationFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartButton.setTextSize(12);
                mStartButton.setText("There was a problem in getting Host Location. Try again later.");
            }
        });
    }

    @Override
    public void onGetHostLocation(final String location, final double distance) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartButton.setTextSize(13);
                mStartButton.setText(String.format("Host Location: %s [Distance: %s km]", location, new DecimalFormat("#.##").format(distance / 1000)));
            }
        });

    }

    @Override
    public void reset() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBarImageView = findViewById(R.id.img_bar);
                mLossRateTV.setText("0 ms");
                mLossRateLL.removeAllViews();
                mDownloadBwTV.setText("0 Mbps");
                mDownloadBwLL.removeAllViews();
                mUploadBwTV.setText("0 Mbps");
                mUploadBwLL.removeAllViews();
            }
        });

    }

    @Override
    public void onPingTestFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    private void initPingGraphic(final XYMultipleSeriesRenderer multiPingRenderer) {

        XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        pingFill.setColor(Color.parseColor("#4d5a6a"));
        pingRenderer.addFillOutsideLine(pingFill);
        pingRenderer.setDisplayChartValues(false);
        pingRenderer.setShowLegendItem(false);
        pingRenderer.setColor(Color.parseColor("#4d5a6a"));
        pingRenderer.setLineWidth(5);
        //final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
        multiPingRenderer.setXLabels(0);
        multiPingRenderer.setYLabels(0);
        multiPingRenderer.setZoomEnabled(false);
        multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiPingRenderer.setPanEnabled(true, true);
        multiPingRenderer.setZoomButtonsVisible(false);
        multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiPingRenderer.addSeriesRenderer(pingRenderer);
    }

    private void initDownloadBwGraphic(final XYMultipleSeriesRenderer multiDownloadRenderer) {
        //Init Download graphic
        XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        downloadFill.setColor(Color.parseColor("#4d5a6a"));
        downloadRenderer.addFillOutsideLine(downloadFill);
        downloadRenderer.setDisplayChartValues(false);
        downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
        downloadRenderer.setShowLegendItem(false);
        downloadRenderer.setLineWidth(5);
        //final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
        multiDownloadRenderer.setXLabels(0);
        multiDownloadRenderer.setYLabels(0);
        multiDownloadRenderer.setZoomEnabled(false);
        multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiDownloadRenderer.setPanEnabled(false, false);
        multiDownloadRenderer.setZoomButtonsVisible(false);
        multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

    }


    private void initUploadBwGraphic(final XYMultipleSeriesRenderer multiUploadRenderer) {
        //Init Upload graphic
        XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        uploadFill.setColor(Color.parseColor("#4d5a6a"));
        uploadRenderer.addFillOutsideLine(uploadFill);
        uploadRenderer.setDisplayChartValues(false);
        uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
        uploadRenderer.setShowLegendItem(false);
        uploadRenderer.setLineWidth(5);
        //f = new XYMultipleSeriesRenderer();
        multiUploadRenderer.setXLabels(0);
        multiUploadRenderer.setYLabels(0);
        multiUploadRenderer.setZoomEnabled(false);
        multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiUploadRenderer.setPanEnabled(false, false);
        multiUploadRenderer.setZoomButtonsVisible(false);
        multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiUploadRenderer.addSeriesRenderer(uploadRenderer);
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

    private Looper startNewThread(String threadName, Runnable runnable) {
        HandlerThread thread = new HandlerThread(threadName);
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(runnable);
        return thread.getLooper();
    }

    private void networkUpdate() {

        final JavaUtils javaUtils = new JavaUtils();
        javaUtils.createNativeNetwork();

        Callback callback = new Callback();


        final NetworkStatistic networkStatistic = new NetworkStatistic(heartBeat, hearBeatCount, callback);

        networkStatistic.setProbeIP(probeIP);

        networkStatistic.lostrateAndRTT(javaUtils);
        networkStatistic.downloadLossrate(javaUtils);
        networkStatistic.uploadBw(javaUtils);
        networkStatistic.downloadBw(javaUtils);
    }

    ///callback data from  NetworkStatistic
    public class Callback implements CallbackBase {

        @Override
        public void onRTTandUploadLossCallback(final int rtt, float uploadLoss) {
            Log.i(TAG, "onRTTandUploadLossCallback uploadLoss " + uploadLoss + " rtt " + rtt);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rttList.add((double) rtt);

                    Double sum = 0.0;
                    double rtt_ = 0.0;
                    for(Double val:rttList){
                        sum += val;
                    }

                    if(rttList.size() != 0) {
                        rtt_ = sum / rttList.size();
                    }

                    mRttTV.setText(String.format(Locale.CHINA, "%.2f ms", (double) rtt_));
                    mRttLL.removeAllViews();
                    mRttLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(rttList, getBaseContext()));
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

                    uploadBwList.add((double) upBandWidth);

                    Double sum = 0.0;
                    double upBw = 0.0;
                    for(Double val:uploadBwList){
                        sum += val;
                    }

                    if(uploadBwList.size() != 0) {
                        upBw = sum / uploadBwList.size();
                    }

                    mUploadBwTV.setText(String.format(Locale.CHINA, "%.2f mbps", (double) upBw));
                    mUploadBwLL.removeAllViews();
                    mUploadBwLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(uploadBwList, getBaseContext()));

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

                    downloadBwList.add(downBandWidth);

                    Double sum = 0.0;
                    for(Double val:downloadBwList){
                        sum += val;
                    }
                    if(downloadBwList.size() != 0) {
                        downBandWidth = sum / downloadBwList.size();
                    }

                    mDownloadBwTV.setText(String.format(Locale.CHINA, "%.2f mbps", (double) downBandWidth));
                    mDownloadBwLL.removeAllViews();
                    mDownloadBwLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(downloadBwList, getBaseContext()));

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
                getDownloadBwCount++;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    downLossRateList.add((double) downloadLoss);

                    Double sum = 0.0;
                    double dnLossRate = 0;
                    for(Double val:downLossRateList){
                        sum += val;
                    }

                    if(downLossRateList.size() != 0) {
                        dnLossRate = sum / downLossRateList.size();
                    }

                    mLossRateTV.setText(String.format(Locale.CHINA, "%.2f", (double) dnLossRate));
                    mLossRateLL.removeAllViews();
                    mLossRateLL.addView(XYMultipleSeriesRendererHandler.initGraphicalView(downLossRateList, getBaseContext()));

                    synchronized (this) {
                        if (getDownloadBwCount >= hearBeatCount) {
                            mStartButton.setEnabled(true);
                        }
                    }

                }
            });
        }
    }

}

