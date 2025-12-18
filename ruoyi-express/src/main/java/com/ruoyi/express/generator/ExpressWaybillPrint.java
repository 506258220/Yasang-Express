package com.ruoyi.express.generator;

import com.ruoyi.express.domain.ExpressWaybill;

public class ExpressWaybillPrint extends ExpressWaybill {
    private static final long serialVersionUID = 1L;

    private String collectMoney;
    private String freight;
    private String total;
    private String seqNo;
    private String area;
    private String areaPhone;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String expressPrintTime;
    private String senderPostcode;
    private String consigneePostcode;

    public String getCollectMoney() {
        return collectMoney;
    }

    public void setCollectMoney(String collectMoney) {
        this.collectMoney = collectMoney;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaPhone() {
        return areaPhone;
    }

    public void setAreaPhone(String areaPhone) {
        this.areaPhone = areaPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getExpressPrintTime() {
        return expressPrintTime;
    }

    public void setExpressPrintTime(String expressPrintTime) {
        this.expressPrintTime = expressPrintTime;
    }

    public String getSenderPostcode() {
        return senderPostcode;
    }

    public void setSenderPostcode(String senderPostcode) {
        this.senderPostcode = senderPostcode;
    }

    public String getConsigneePostcode() {
        return consigneePostcode;
    }

    public void setConsigneePostcode(String consigneePostcode) {
        this.consigneePostcode = consigneePostcode;
    }
}
