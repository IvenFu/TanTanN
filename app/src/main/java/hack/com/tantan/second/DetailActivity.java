package hack.com.tantan.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import hack.com.tantan.R;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBackBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBackBtn = findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                DetailActivity.this.finish();
            default:
                break;
        }
    }
}
