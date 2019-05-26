package com.android.codeaire.meatchain.Models;

public class Product {

   /* <item>Farmer</item>                       0
    <item>Butcher</item>                        1
    <item>Packaging</item>                      2
    <item>Market/Distribution</item>            3
    <item>Customer</item>                       4
    */

    private String productId = "";
    private int processStatus = 0;

    // Farmer
    private String type = "";
    private String Sex = "";
    private String Age = "";
    private String Weight = "";

    // Butcher
    private String dateOfSlaughter = "";

    // Packaging
    private String meatPart = "";
    private String meatType = "";
    private String packagingDate = "";
    private String expiryDate = "";

    // Distribution
    private String dateOfReceiving = "";

    //Information Provider
    private String info0Id = "";
    private String info0Time = "";

    private String info1Id = "";
    private String info1Time = "";

    private String info2Id = "";
    private String info2Time = "";

    private String info3Id = "";
    private String info3Time = "";


    //****************************************************Getter and Setter********************************************************//
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getDateOfSlaughter() {
        return dateOfSlaughter;
    }

    public void setDateOfSlaughter(String dateOfSlaughter) {
        this.dateOfSlaughter = dateOfSlaughter;
    }

    public String getMeatPart() {
        return meatPart;
    }

    public void setMeatPart(String meatPart) {
        this.meatPart = meatPart;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public String getPackagingDate() {
        return packagingDate;
    }

    public void setPackagingDate(String packagingDate) {
        this.packagingDate = packagingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDateOfReceiving() {
        return dateOfReceiving;
    }

    public void setDateOfReceiving(String dateOfReceiving) {
        this.dateOfReceiving = dateOfReceiving;
    }

    public String getInfo0Id() {
        return info0Id;
    }

    public void setInfo0Id(String info0Id) {
        this.info0Id = info0Id;
    }

    public String getInfo0Time() {
        return info0Time;
    }

    public void setInfo0Time(String info0Time) {
        this.info0Time = info0Time;
    }

    public String getInfo1Id() {
        return info1Id;
    }

    public void setInfo1Id(String info1Id) {
        this.info1Id = info1Id;
    }

    public String getInfo1Time() {
        return info1Time;
    }

    public void setInfo1Time(String info1Time) {
        this.info1Time = info1Time;
    }

    public String getInfo2Id() {
        return info2Id;
    }

    public void setInfo2Id(String info2Id) {
        this.info2Id = info2Id;
    }

    public String getInfo2Time() {
        return info2Time;
    }

    public void setInfo2Time(String info2Time) {
        this.info2Time = info2Time;
    }

    public String getInfo3Id() {
        return info3Id;
    }

    public void setInfo3Id(String info3Id) {
        this.info3Id = info3Id;
    }

    public String getInfo3Time() {
        return info3Time;
    }

    public void setInfo3Time(String info3Time) {
        this.info3Time = info3Time;
    }
}
