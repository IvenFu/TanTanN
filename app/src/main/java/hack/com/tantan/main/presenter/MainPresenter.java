package hack.com.tantan.main.presenter;

import hack.com.tantan.main.contract.MainContractView;

public class MainPresenter {
    private MainContractView mContractView;

    public MainPresenter(MainContractView contractView) {
        mContractView = contractView;
    }

    public int getPositionByValue(double rate) {
        if (rate > 0 && rate <= 1) {
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
}
