package hack.com.tantan.detail.presenter;

import hack.com.tantan.detail.contract.DetailContractView;

public class DetailPresenter {
    private DetailContractView mContract;
    public DetailPresenter(DetailContractView contractView){
        mContract = contractView;
    }
}
