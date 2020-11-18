package bih.nic.in.ashwin.adaptor;

public interface SalaryOfAshaByBhm {

    public void onAdditionInDava(int position, int value);
    public void onDeductionInDava(int position, int value);

    public void onDeductionRemarks(int position, String value);
    public void onMarkSalary(int position, boolean isChecked);
}
