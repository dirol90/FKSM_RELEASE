package com.tsimbalyukstudio.fksm;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Refound {

    private int sum;
    private int status = 3;
    private String manager = "";
    private String date;
    private String psesialInfo = "";
    private String refoundID = "";
    private String loanID = "";

    public Refound(int sum, String refoundID, String loanID) {
        this.loanID = loanID;
        this.refoundID = refoundID;
        this.sum = sum;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        date = sdf.format(new Date());
    }

    Refound() {

    }

    public String getPsesialInfo() {
        return psesialInfo;
    }

    public void setPsesialInfo(String psesialInfo) {
        this.psesialInfo = psesialInfo;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDate()
    {
        return date;
    }

    public Date getDate(String s)
    {   DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        Date a= null;
        try {
            a = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRefoundID() {
        return refoundID;
    }

    public void setRefoundID(String refoundID) {
        this.refoundID = refoundID;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }
}
