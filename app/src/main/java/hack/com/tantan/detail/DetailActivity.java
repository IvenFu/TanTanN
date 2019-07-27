package hack.com.tantan.detail;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hack.com.tantan.R;
import hack.com.tantan.detail.contract.DetailContractView;
import hack.com.tantan.detail.presenter.DetailPresenter;

public class DetailActivity extends AppCompatActivity implements DetailContractView, View.OnClickListener {
    private ImageButton mBackBtn = null;
    private DetailPresenter mPresenter = null;
    private LinearLayout mDetectResultLL = null;
    private TextView mDetectingTV = null;

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

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.executeNetDetector();
            }
        }, 500);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View mDecorView = getWindow().getDecorView();
//        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    private void initViews() {
        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mDetectResultLL = findViewById(R.id.ll_result);
        mDetectingTV = findViewById(R.id.tv_detecting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                DetailActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void printDetecting(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mDetectingTV.getLayoutParams();
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mDetectingTV.setLayoutParams(layoutParams);
                mDetectingTV.setText(text);
            }
        });
    }

    @Override
    public void addDetectResult(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetectResultLL.addView(newInfoTv(text),
                        mDetectResultLL.getChildCount() - 1);
            }
        });
    }

    @Override
    public void onDetectFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetectingTV.setText("探测完成");
            }
        });
    }

    @Override
    public void onError(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                addDetectResult(errorMessage);
            }
        });
    }

    private TextView newInfoTv(String text) {
        TextView textView = new TextView(this.getApplicationContext());
        Resources resources = getResources();
        textView.setTextColor(resources.getColor(R.color.white));
        textView.setTextSize(resources.getDimension(R.dimen.activity_detail_info_value));
        textView.setText(text);
        return textView;
    }
}
