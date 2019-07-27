package hack.com.tantan.detail;

import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.net.detector.sdk.ping.PingInfo;
import com.netease.net.detector.sdk.report.ReportStats;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hack.com.tantan.R;
import hack.com.tantan.detail.contract.DetailContractView;
import hack.com.tantan.detail.presenter.DetailPresenter;
import hack.com.tantan.utils.ServiceDataHandler;

public class DetailActivity extends AppCompatActivity implements DetailContractView, View.OnClickListener {
    private ImageButton mBackBtn = null;
    private Button mTestBtn = null;
    private DetailPresenter mPresenter = null;
    private LinearLayout mPingLL = null;

    public static final int DETAIL_TYPE_PING = 0;
    public static final int DETAIL_TYPE_TELNET = 1;
    public static final int DETAIL_TYPE_DIG = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DETAIL_TYPE_PING, DETAIL_TYPE_TELNET, DETAIL_TYPE_DIG})
    @interface DetailType {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailPresenter(this);
        //沉浸式状态栏
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary2));

        //设置layout文件
        setContentView(R.layout.activity_detail);
        //初始化Views
        initViews();
    }

    private void initViews() {
        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mTestBtn = findViewById(R.id.btn_test);
        mTestBtn.setOnClickListener(this);
        mPingLL = findViewById(R.id.ll_ping);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                DetailActivity.this.finish();
                break;
            case R.id.btn_test:
                mPresenter.executeNetDetector();
                break;
            default:
                break;
        }
    }


    @Override
    public void displayReportStats(final ReportStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayPing(stats.getPingInfoList());
            }
        });
    }

    private void displayPing(List<PingInfo> pingInfoList) {
        for (PingInfo info : pingInfoList) {
            String host = info.getHost();
            //丢包率
            double loss = info.getLoss();
            String rttStr = ServiceDataHandler.castRttToString(info.getRtt());
            ArrayList<String> textList = new ArrayList<>(Arrays.asList(host, loss + "", rttStr));
            addNewInfo(textList, mPingLL);
        }
    }

    private TextView newInfoTv(String text) {
        TextView textView = new TextView(this.getApplicationContext());
        Resources resources = getResources();
        textView.setTextColor(resources.getColor(R.color.white));
        textView.setTextSize(resources.getDimension(R.dimen.activity_detail_info_value));
        textView.setText(text);
        textView.setMaxEms(25);
        return textView;
    }

    //新建一条分割线
    private void setLineStyle(View line, LinearLayout parent) {
        ViewGroup.LayoutParams parentParams = parent.getLayoutParams();
        ViewGroup.LayoutParams lineParams = line.getLayoutParams();
        lineParams.width = parentParams.width;
        lineParams.height = 1;
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void addNewInfo(List<String> textList, LinearLayout parent) {
        int parentWidth = parent.getLayoutParams().width;
        LinearLayout newLineParent = new LinearLayout(getApplicationContext());
        parent.addView(newLineParent);
        newLineParent.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams newLineParentParams = newLineParent.getLayoutParams();
        newLineParentParams.width = parentWidth;
        newLineParentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        newLineParent.setLayoutParams(newLineParentParams);

        for (String text : textList) {
//        for (int i=0; i< 3; ++i) {
            TextView textView = newInfoTv(text);
            newLineParent.addView(textView);
//            textView.setText("ahskedfhosa");
//            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
//            layoutParams.width = parentWidth / textList.size();
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            textView.setLayoutParams(layoutParams);
        }

        View line = new View(getApplicationContext());
        parent.addView(line);
        setLineStyle(line, parent);
    }
}
