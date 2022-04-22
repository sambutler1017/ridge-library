package com.ridge.test.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentSystemSettings {

    private int id;

    private int tier;

    private int plantName;

    private Date growStartDate;

    private int numPlants;

    private float minPH;

    private float maxPH;

    private float minTDS;

    private float maxTDS;

    private float minWaterTemp;

    private float maxWaterTemp;

    private float minHumidity;

    private float maxHumidity;

    private Date lightOnTime;

    private Date lightOffTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getPlantName() {
        return plantName;
    }

    public void setPlantName(int plantName) {
        this.plantName = plantName;
    }

    public Date getGrowStartDate() {
        return growStartDate;
    }

    public void setGrowStartDate(Date growStartDate) {
        this.growStartDate = growStartDate;
    }

    public int getNumPlants() {
        return numPlants;
    }

    public void setNumPlants(int numPlants) {
        this.numPlants = numPlants;
    }

    public float getMinPH() {
        return minPH;
    }

    public void setMinPH(float minPH) {
        this.minPH = minPH;
    }

    public float getMaxPH() {
        return maxPH;
    }

    public void setMaxPH(float maxPH) {
        this.maxPH = maxPH;
    }

    public float getMinTDS() {
        return minTDS;
    }

    public void setMinTDS(float minTDS) {
        this.minTDS = minTDS;
    }

    public float getMaxTDS() {
        return maxTDS;
    }

    public void setMaxTDS(float maxTDS) {
        this.maxTDS = maxTDS;
    }

    public float getMinWaterTemp() {
        return minWaterTemp;
    }

    public void setMinWaterTemp(float minWaterTemp) {
        this.minWaterTemp = minWaterTemp;
    }

    public float getMaxWaterTemp() {
        return maxWaterTemp;
    }

    public void setMaxWaterTemp(float maxWaterTemp) {
        this.maxWaterTemp = maxWaterTemp;
    }

    public float getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(float minHumidity) {
        this.minHumidity = minHumidity;
    }

    public float getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(float maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public Date getLightOnTime() {
        return lightOnTime;
    }

    public void setLightOnTime(Date lightOnTime) {
        this.lightOnTime = lightOnTime;
    }

    public Date getLightOffTime() {
        return lightOffTime;
    }

    public void setLightOffTime(Date lightOffTime) {
        this.lightOffTime = lightOffTime;
    }

    @Override
    public String toString() {
        return this.getTier() + ","
                + this.getMinPH() + ","
                + this.getMaxPH() + ","
                + this.getMinTDS() + ","
                + this.getMaxTDS() + ","
                + this.getMinWaterTemp() + ","
                + this.getMaxWaterTemp() + ","
                + formatDate(this.getLightOnTime(), "HH:mm") + ","
                + formatDate(this.getLightOffTime(), "HH:mm") + ","
                + formatDate(null, "MM.dd.yy.HH.mm.ss");
    }

    private String formatDate(Date dt, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateString = simpleDateFormat.format(dt != null ? dt : new Date());
        return dateString;
    }

}