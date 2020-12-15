package bih.nic.in.ashwin.adaptor;

public interface MonthlyActivityListener {
    public void onActivityCheckboxChanged(int position, Boolean isChecked, String type, String noOfBen);
    public void onActivityCheckboxChanged(int position, String noOfBen);
}
