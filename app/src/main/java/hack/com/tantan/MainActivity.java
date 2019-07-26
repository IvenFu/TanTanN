package hack.com.tantan;

import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import hack.com.tantan.test.HttpDownloadTest;
import hack.com.tantan.test.HttpUploadTest;
import hack.com.tantan.test.PingTest;


public class MainActivity extends AppCompatActivity {
    private static int mPosition = 0;
    private static int mLastPosition = 0;
    private GetSpeedTestHostsHandler mGetSpeedTestHostsHandler = null;
    private HashSet<String> mTempBlackList;
    private final  String TAG = "MainActivity";
    private Button mStartButton = null;
    private DecimalFormat mDec = null;
    /**测试用进程的名称*/
    private static final String PROB_THREAD_NAME = "ProbThread";

    @Override
    public void onResume() {
        super.onResume();

        mGetSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        mGetSpeedTestHostsHandler.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartButton = (Button) findViewById(R.id.startButton);
        mDec = new DecimalFormat("#.##");

        mStartButton.setText("Begin Test");

        mTempBlackList = new HashSet<>();

        mGetSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        mGetSpeedTestHostsHandler.start();


        final Button jniTest = (Button) findViewById(R.id.jniTest);

        jniTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String jniString = JavaUtils.getString();
                Log.i(TAG,"JNI test sucess " + jniString);
            }
        });



        mStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mStartButton.setEnabled(false);

                //Restart test icin eger baglanti koparsa
                if (mGetSpeedTestHostsHandler == null) {
                    mGetSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                    mGetSpeedTestHostsHandler.start();
                }

                //开启新线程来执行检测操作
                startNewThread(PROB_THREAD_NAME, new ProbRunnable());
            }
        });
    }

    /**
     * 探测的Runnable
     */
    class ProbRunnable implements Runnable{
        private RotateAnimation mRotate;
        private ImageView mBarImageView = (ImageView) findViewById(R.id.barImageView);
        private TextView mPingTextView = (TextView) findViewById(R.id.pingTextView);
        private TextView mDownloadTextView = (TextView) findViewById(R.id.downloadTextView);
        private TextView mUploadTextView = (TextView) findViewById(R.id.uploadTextView);
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStartButton.setText("Selecting best server based on ping...");
                }
            });

            //Get egcodes.speedtest hosts
            int timeCount = 600; //1min
            while (!mGetSpeedTestHostsHandler.isFinished()) {
                timeCount--;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (timeCount <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                            mStartButton.setEnabled(true);
                            mStartButton.setTextSize(16);
                            mStartButton.setText("Restart Test");
                        }
                    });
                    mGetSpeedTestHostsHandler = null;
                    return;
                }
            }

            //Find closest server
            HashMap<Integer, String> mapKey = mGetSpeedTestHostsHandler.getMapKey();
            HashMap<Integer, List<String>> mapValue = mGetSpeedTestHostsHandler.getMapValue();
            double selfLat = mGetSpeedTestHostsHandler.getSelfLat();
            double selfLon = mGetSpeedTestHostsHandler.getSelfLon();
            double tmp = 19349458;
            double dist = 0.0;
            int findServerIndex = 0;
            for (int index : mapKey.keySet()) {
                if (mTempBlackList.contains(mapValue.get(index).get(5))) {
                    continue;
                }

                Location source = new Location("Source");
                source.setLatitude(selfLat);
                source.setLongitude(selfLon);

                List<String> ls = mapValue.get(index);
                Location dest = new Location("Dest");
                dest.setLatitude(Double.parseDouble(ls.get(0)));
                dest.setLongitude(Double.parseDouble(ls.get(1)));

                double distance = source.distanceTo(dest);
                if (tmp > distance) {
                    tmp = distance;
                    dist = distance;
                    findServerIndex = index;
                }
            }
            String uploadAddr = mapKey.get(findServerIndex);
            final List<String> info = mapValue.get(findServerIndex);
            final double distance = dist;

            if (info == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStartButton.setTextSize(12);
                        mStartButton.setText("There was a problem in getting Host Location. Try again later.");
                    }
                });
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStartButton.setTextSize(13);
                    mStartButton.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                }
            });

            //Init Ping graphic
            final LinearLayout chartPing = (LinearLayout) findViewById(R.id.chartPing);
            XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
            XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
            pingFill.setColor(Color.parseColor("#4d5a6a"));
            pingRenderer.addFillOutsideLine(pingFill);
            pingRenderer.setDisplayChartValues(false);
            pingRenderer.setShowLegendItem(false);
            pingRenderer.setColor(Color.parseColor("#4d5a6a"));
            pingRenderer.setLineWidth(5);
            final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
            multiPingRenderer.setXLabels(0);
            multiPingRenderer.setYLabels(0);
            multiPingRenderer.setZoomEnabled(false);
            multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
            multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
            multiPingRenderer.setPanEnabled(true, true);
            multiPingRenderer.setZoomButtonsVisible(false);
            multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            multiPingRenderer.addSeriesRenderer(pingRenderer);

            //Init Download graphic
            final LinearLayout chartDownload = (LinearLayout) findViewById(R.id.chartDownload);
            XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
            XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
            downloadFill.setColor(Color.parseColor("#4d5a6a"));
            downloadRenderer.addFillOutsideLine(downloadFill);
            downloadRenderer.setDisplayChartValues(false);
            downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
            downloadRenderer.setShowLegendItem(false);
            downloadRenderer.setLineWidth(5);
            final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
            multiDownloadRenderer.setXLabels(0);
            multiDownloadRenderer.setYLabels(0);
            multiDownloadRenderer.setZoomEnabled(false);
            multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
            multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
            multiDownloadRenderer.setPanEnabled(false, false);
            multiDownloadRenderer.setZoomButtonsVisible(false);
            multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

            //Init Upload graphic
            final LinearLayout chartUpload = (LinearLayout) findViewById(R.id.chartUpload);
            XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
            XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
            uploadFill.setColor(Color.parseColor("#4d5a6a"));
            uploadRenderer.addFillOutsideLine(uploadFill);
            uploadRenderer.setDisplayChartValues(false);
            uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
            uploadRenderer.setShowLegendItem(false);
            uploadRenderer.setLineWidth(5);
            final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
            multiUploadRenderer.setXLabels(0);
            multiUploadRenderer.setYLabels(0);
            multiUploadRenderer.setZoomEnabled(false);
            multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
            multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
            multiUploadRenderer.setPanEnabled(false, false);
            multiUploadRenderer.setZoomButtonsVisible(false);
            multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            multiUploadRenderer.addSeriesRenderer(uploadRenderer);

            //Reset value, graphics
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPingTextView.setText("0 ms");
                    chartPing.removeAllViews();
                    mDownloadTextView.setText("0 Mbps");
                    chartDownload.removeAllViews();
                    mUploadTextView.setText("0 Mbps");
                    chartUpload.removeAllViews();
                }
            });
            final List<Double> pingRateList = new ArrayList<>();
            final List<Double> downloadRateList = new ArrayList<>();
            final List<Double> uploadRateList = new ArrayList<>();
            Boolean pingTestStarted = false;
            Boolean pingTestFinished = false;
            Boolean downloadTestStarted = false;
            Boolean downloadTestFinished = false;
            Boolean uploadTestStarted = false;
            Boolean uploadTestFinished = false;

            //Init Test
            final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
            final HttpDownloadTest downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
            final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


            //Tests
            while (true) {
                if (!pingTestStarted) {
                    pingTest.start();
                    pingTestStarted = true;
                }
                if (pingTestFinished && !downloadTestStarted) {
                    downloadTest.start();
                    downloadTestStarted = true;
                }
                if (downloadTestFinished && !uploadTestStarted) {
                    uploadTest.start();
                    uploadTestStarted = true;
                }


                //Ping Test
                if (pingTestFinished) {
                    //Failure
                    if (pingTest.getAvgRtt() == 0) {
                        System.out.println("Ping error...");
                    } else {
                        //Success
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPingTextView.setText(mDec.format(pingTest.getAvgRtt()) + " ms");
                            }
                        });
                    }
                } else {
                    pingRateList.add(pingTest.getInstantRtt());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPingTextView.setText(mDec.format(pingTest.getInstantRtt()) + " ms");
                        }
                    });

                    //Update chart
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Creating an  XYSeries for Income
                            XYSeries pingSeries = new XYSeries("");
                            pingSeries.setTitle("");

                            int count = 0;
                            List<Double> tmpLs = new ArrayList<>(pingRateList);
                            for (Double val : tmpLs) {
                                pingSeries.add(count++, val);
                            }

                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                            dataset.addSeries(pingSeries);

                            GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiPingRenderer);
                            chartPing.addView(chartView, 0);

                        }
                    });
                }


                //Download Test
                if (pingTestFinished) {
                    if (downloadTestFinished) {
                        //Failure
                        if (downloadTest.getFinalDownloadRate() == 0) {
                            System.out.println("Download error...");
                        } else {
                            //Success
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDownloadTextView.setText(mDec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                }
                            });
                        }
                    } else {
                        //Calc mPosition
                        double downloadRate = downloadTest.getInstantDownloadRate();
                        downloadRateList.add(downloadRate);
                        mPosition = getPositionByRate(downloadRate);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mRotate = new RotateAnimation(mLastPosition, mPosition, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                mRotate.setInterpolator(new LinearInterpolator());
                                mRotate.setDuration(100);
                                mBarImageView.startAnimation(mRotate);
                                mDownloadTextView.setText(mDec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                            }

                        });
                        mLastPosition = mPosition;

                        //Update chart
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Creating an  XYSeries for Income
                                XYSeries downloadSeries = new XYSeries("");
                                downloadSeries.setTitle("");

                                List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                int count = 0;
                                for (Double val : tmpLs) {
                                    downloadSeries.add(count++, val);
                                }

                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(downloadSeries);

                                GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiDownloadRenderer);
                                chartDownload.addView(chartView, 0);
                            }
                        });

                    }
                }


                //Upload Test
                if (downloadTestFinished) {
                    if (uploadTestFinished) {
                        //Failure
                        if (uploadTest.getFinalUploadRate() == 0) {
                            System.out.println("Upload error...");
                        } else {
                            //Success
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUploadTextView.setText(mDec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                }
                            });
                        }
                    } else {
                        //Calc mPosition
                        double uploadRate = uploadTest.getInstantUploadRate();
                        uploadRateList.add(uploadRate);
                        mPosition = getPositionByRate(uploadRate);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mRotate = new RotateAnimation(mLastPosition, mPosition, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                mRotate.setInterpolator(new LinearInterpolator());
                                mRotate.setDuration(100);
                                mBarImageView.startAnimation(mRotate);
                                mUploadTextView.setText(mDec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                            }

                        });
                        mLastPosition = mPosition;

                        //Update chart
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Creating an  XYSeries for Income
                                XYSeries uploadSeries = new XYSeries("");
                                uploadSeries.setTitle("");

                                int count = 0;
                                List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                for (Double val : tmpLs) {
                                    if (count == 0) {
                                        val = 0.0;
                                    }
                                    uploadSeries.add(count++, val);
                                }

                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(uploadSeries);

                                GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiUploadRenderer);
                                chartUpload.addView(chartView, 0);
                            }
                        });

                    }
                }

                //Test bitti
                if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                    break;
                }

                if (pingTest.isFinished()) {
                    pingTestFinished = true;
                }
                if (downloadTest.isFinished()) {
                    downloadTestFinished = true;
                }
                if (uploadTest.isFinished()) {
                    uploadTestFinished = true;
                }

                if (pingTestStarted && !pingTestFinished) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }

            //Thread bitiminde button yeniden aktif ediliyor
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStartButton.setEnabled(true);
                    mStartButton.setTextSize(16);
                    mStartButton.setText("Restart Test");
                }
            });


        }
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

    private Looper startNewThread(String threadName, Runnable runnable){
        HandlerThread thread = new HandlerThread(threadName);
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(runnable);
        return thread.getLooper();
    }
}

