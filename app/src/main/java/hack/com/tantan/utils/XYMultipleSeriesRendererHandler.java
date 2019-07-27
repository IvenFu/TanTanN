package hack.com.tantan.utils;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class XYMultipleSeriesRendererHandler {

    private static XYMultipleSeriesRenderer initRender(){
        XYSeriesRenderer render = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        pingFill.setColor(Color.parseColor("#4d5a6a"));
        render.addFillOutsideLine(pingFill);
        render.setDisplayChartValues(false);
        render.setShowLegendItem(false);
        render.setColor(Color.parseColor("#4d5a6a"));
        render.setLineWidth(5);
        final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
        multiPingRenderer.setXLabels(0);
        multiPingRenderer.setYLabels(0);
        multiPingRenderer.setZoomEnabled(false);
        multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiPingRenderer.setPanEnabled(true, true);
        multiPingRenderer.setZoomButtonsVisible(false);
        multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiPingRenderer.addSeriesRenderer(render);
        return multiPingRenderer;
    }

    public static GraphicalView initGraphicalView(List<Double> dataList, Context context){
        XYSeries pingSeries = new XYSeries("");
        pingSeries.setTitle("");

        int count = 0;
        List<Double> tmpLs = new ArrayList<>(dataList);
        for (Double val : tmpLs) {
            pingSeries.add(count++, val);
        }

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(pingSeries);

        return ChartFactory.getLineChartView(context, dataSet, initRender());
    }
}
