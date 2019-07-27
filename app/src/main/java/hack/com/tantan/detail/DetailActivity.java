package hack.com.tantan.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.netease.net.detector.sdk.exception.NetDetectorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import hack.com.tantan.R;
import hack.com.tantan.detail.contract.DetailContractView;
import hack.com.tantan.detail.presenter.DetailPresenter;

public class DetailActivity extends AppCompatActivity implements DetailContractView, View.OnClickListener {
    private ImageButton mBackBtn = null;
    private Button mTestBtn = null;
    private DetailPresenter mPresenter = null;

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
        //初始化返回按钮

        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);

        mTestBtn = findViewById(R.id.btn_test);
        mTestBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                DetailActivity.this.finish();
                break;
            case R.id.btn_test:
                mPresenter.execute();
                break;
            default:
                break;
        }
    }
}
