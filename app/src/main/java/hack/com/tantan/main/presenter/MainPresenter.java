package hack.com.tantan.main.presenter;

import hack.com.tantan.GetSpeedTestHostsHandler;
import hack.com.tantan.main.contract.MainContractView;

public class MainPresenter {
    private MainContractView mContractView;
    private GetSpeedTestHostsHandler mGetSpeedTestHostsHandler;
    public MainPresenter(MainContractView contractView, GetSpeedTestHostsHandler getSpeedTestHostsHandler){
        mContractView = contractView;
        mGetSpeedTestHostsHandler = getSpeedTestHostsHandler;
    }
}
