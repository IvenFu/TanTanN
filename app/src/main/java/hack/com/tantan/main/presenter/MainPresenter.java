package hack.com.tantan.main.presenter;

import android.graphics.Color;
import android.location.Location;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
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
import java.util.List;

import hack.com.tantan.GetSpeedTestHostsHandler;
import hack.com.tantan.R;
import hack.com.tantan.main.contract.MainContractView;
import hack.com.tantan.test.HttpDownloadTest;
import hack.com.tantan.test.HttpUploadTest;
import hack.com.tantan.test.PingTest;

public class MainPresenter {
    private MainContractView mContractView;
    private GetSpeedTestHostsHandler mGetSpeedTestHostsHandler;
    public MainPresenter(MainContractView contractView, GetSpeedTestHostsHandler getSpeedTestHostsHandler){
        mContractView = contractView;
        mGetSpeedTestHostsHandler = getSpeedTestHostsHandler;
    }
}
