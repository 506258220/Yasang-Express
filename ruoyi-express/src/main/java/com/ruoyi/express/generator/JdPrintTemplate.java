package com.ruoyi.express.generator;

import java.util.HashMap;
import java.util.Map;

/**
*模板类，字段与上面的变量名一一对应
*/
public class JdPrintTemplate {

    /**
     * 面单最上面的子条形码
     * */
    private String subQrCode;
    /**
     * 面单下面的父条形码
     * */
    private String pQrCode;

    /**
     * 打印次数
     * */
    private String printTime;
    /**
     * 打印时间
     * */
    private String printDate;

    /**
     * 始发分拣中心
     * */
    private String sourceSortCenterName;

    /**
     *始发道口号
     * */
    private String originalCrossCode;

    private String sourceCrossCode;

    /**
     *始发笼车号
     * */
    private String originalTabletrolleyCode;

    /**
     * 目的分拣中心
     * */
    private String targetSortCenterName;

    private String targetCrossCode;

    /**
     * 目的道口号
     * */
    private String destinationCrossCode;

    /**
     * 目的笼车号
     * */
    private String destinationTabletrolleyCode;

    /**
     * 路区
     * */
    private String road;

    /**
     * 重量
     * */
    private String weight;

    /**
     * 目的站点
     * */
    private String siteName;

    /**
     * 客户名字
     * */
    private String consignee;
    /**
     * 客户电话
     * */
    private String consigneeTel;
    /**
     * 目的地址
     * */
    private String destination;
    /**
     * 发件人名字
     * */
    private String sender;
    /**
     * 发件地址
     * */
    private String senderAddr;

    /**
     * 发件人电话
     * */
    private String senderTel;

    /**
     * 描述
     * */
    private String desc;
    /**
     * 分发码
     * */
    private String distributeCode;
    /**
     * 定单id
     * */
    private String orderId;

    /**
     * 备注
     * */
    private String comment;

    /**
     * 代收金额
     * */
    private String collectMoney;

    /**
     * 应收金额
     * */
    private String totalMoney;

    /**
     * 第几个快递
     * */
    private String serial;

    /**
     * 获取占位字段
     * */
    public Map<String,String> getColumns(){
        Map<String,String> map = new HashMap<>();
        map.put("printTime",this.printTime);
        map.put("printDate",this.printDate);
        map.put("serial",this.serial);
        map.put("sourceSortCenterName",this.sourceSortCenterName);
        map.put("originalCrossCode",this.originalCrossCode);
        map.put("collectMoney",this.collectMoney);
        map.put("totalMoney",this.totalMoney);
        map.put("originalTabletrolleyCode",this.originalTabletrolleyCode);
        map.put("targetSortCenterName",this.targetSortCenterName);
        map.put("destinationCrossCode",this.destinationCrossCode);
        map.put("destinationTabletrolleyCode",this.destinationTabletrolleyCode);
        map.put("sourceCrossCode", this.sourceCrossCode);
        map.put("targetCrossCode", this.targetCrossCode);
        map.put("road",this.road);
        map.put("weight",this.weight);
        map.put("siteName",this.siteName);
        map.put("consignee",this.consignee);
        map.put("consigneeTel",this.consigneeTel);
        map.put("destination",this.destination);
        map.put("sender",this.sender);
        map.put("senderAddr",this.senderAddr);
        map.put("senderTel",this.senderTel);
        map.put("desc",this.desc);
        map.put("distributeCode",this.distributeCode);
        map.put("orderId",this.orderId);
        map.put("comment",this.comment);

        return map;
    }

    /**
     * 获取条形码字段
     * */
    public Map<String,String> getQrCodes(){
        Map<String,String> map = new HashMap();
        map.put("subQrCode",this.subQrCode);
        map.put("pQrCode",this.pQrCode);
        return map;
    }

    public String getSubQrCode() {
        return subQrCode;
    }

    public void setSubQrCode(String subQrCode) {
        this.subQrCode = subQrCode;
    }

    public String getpQrCode() {
        return pQrCode;
    }

    public void setpQrCode(String pQrCode) {
        this.pQrCode = pQrCode;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getSourceSortCenterName() {
        return sourceSortCenterName;
    }

    public void setSourceSortCenterName(String sourceSortCenterName) {
        this.sourceSortCenterName = sourceSortCenterName;
    }

    public String getOriginalCrossCode() {
        return originalCrossCode;
    }

    public void setOriginalCrossCode(String originalCrossCode) {
        this.originalCrossCode = originalCrossCode;
    }

    public String getSourceCrossCode() {
        return sourceCrossCode;
    }

    public void setSourceCrossCode(String sourceCrossCode) {
        this.sourceCrossCode = sourceCrossCode;
    }

    public String getOriginalTabletrolleyCode() {
        return originalTabletrolleyCode;
    }

    public void setOriginalTabletrolleyCode(String originalTabletrolleyCode) {
        this.originalTabletrolleyCode = originalTabletrolleyCode;
    }

    public String getTargetSortCenterName() {
        return targetSortCenterName;
    }

    public void setTargetSortCenterName(String targetSortCenterName) {
        this.targetSortCenterName = targetSortCenterName;
    }

    public String getTargetCrossCode() {
        return targetCrossCode;
    }

    public void setTargetCrossCode(String targetCrossCode) {
        this.targetCrossCode = targetCrossCode;
    }

    public String getDestinationCrossCode() {
        return destinationCrossCode;
    }

    public void setDestinationCrossCode(String destinationCrossCode) {
        this.destinationCrossCode = destinationCrossCode;
    }

    public String getDestinationTabletrolleyCode() {
        return destinationTabletrolleyCode;
    }

    public void setDestinationTabletrolleyCode(String destinationTabletrolleyCode) {
        this.destinationTabletrolleyCode = destinationTabletrolleyCode;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderAddr() {
        return senderAddr;
    }

    public void setSenderAddr(String senderAddr) {
        this.senderAddr = senderAddr;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDistributeCode() {
        return distributeCode;
    }

    public void setDistributeCode(String distributeCode) {
        this.distributeCode = distributeCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCollectMoney() {
        return collectMoney;
    }

    public void setCollectMoney(String collectMoney) {
        this.collectMoney = collectMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
