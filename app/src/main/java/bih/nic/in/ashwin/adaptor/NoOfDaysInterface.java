package bih.nic.in.ashwin.adaptor;

public interface NoOfDaysInterface {

    public void onNoOfDaysChanged(int position, int days);

    public void onAdditionInCentre(int position, int value);
    public void onDeductionInCentre(int position, int value);
    public void onAdditionRemarks(int position, String value,Boolean forstate);
    public void onDeductionRemarks(int position, String value,Boolean forstate);

    public void onAdditionInState(int position, int value);
    public void onDeductionInStatere(int position, int value);
}
