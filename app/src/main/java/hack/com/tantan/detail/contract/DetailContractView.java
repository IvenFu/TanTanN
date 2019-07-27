package hack.com.tantan.detail.contract;

public interface DetailContractView {

    void printDetecting(final String text);

    void addDetectResult(final String text);

    void onError(final String errorMessage);

    void onDetectFinished();
}
