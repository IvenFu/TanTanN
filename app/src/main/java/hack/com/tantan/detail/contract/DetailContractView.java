package hack.com.tantan.detail.contract;

import com.netease.net.detector.sdk.report.ReportStats;

public interface DetailContractView {
    //将ReportStats呈现倒界面
    void displayReportStats(final ReportStats stats);
}
