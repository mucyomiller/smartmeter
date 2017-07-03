package com.vincent.smartenergymeter;

public class Voucher {

    private String voucherCode;
    private Double voucherValue;

    public Voucher() {
    }

    public Voucher(String voucherCode, Double voucherValue) {
        this.voucherCode = voucherCode;
        this.voucherValue = voucherValue;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Double getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(Double voucherValue) {
        this.voucherValue = voucherValue;
    }
}
