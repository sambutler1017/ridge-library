package com.ridge.test.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CurrentSystemSettings {
    private int Id;

    private int SystemId;

    private int PlantName;

    private LocalDate GrowStartDate;

    private int NumPlants;

    private float MinPH;

    private float MaxPH;

    private float MinTDS;

    private float MaxTDS;

    private float MinWaterTemp;

    private float MaxWaterTemp;

    private float MinHumidity;

    private float MaxHumidity;

    private float PlantTemp;

    private LocalDate LightOnTime;

    private LocalDate LightOffTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSystemId() {
        return SystemId;
    }

    public void setSystemId(int systemId) {
        SystemId = systemId;
    }

    public int getPlantName() {
        return PlantName;
    }

    public void setPlantName(int plantName) {
        PlantName = plantName;
    }

    public LocalDate getGrowStartDate() {
        return GrowStartDate;
    }

    public void setGrowStartDate(LocalDate growStartDate) {
        GrowStartDate = growStartDate;
    }

    public int getNumPlants() {
        return NumPlants;
    }

    public void setNumPlants(int numPlants) {
        NumPlants = numPlants;
    }

    public float getMinPH() {
        return MinPH;
    }

    public void setMinPH(float minPH) {
        MinPH = minPH;
    }

    public float getMaxPH() {
        return MaxPH;
    }

    public void setMaxPH(float maxPH) {
        MaxPH = maxPH;
    }

    public float getMinTDS() {
        return MinTDS;
    }

    public void setMinTDS(float minTDS) {
        MinTDS = minTDS;
    }

    public float getMaxTDS() {
        return MaxTDS;
    }

    public void setMaxTDS(float maxTDS) {
        MaxTDS = maxTDS;
    }

    public float getMinWaterTemp() {
        return MinWaterTemp;
    }

    public void setMinWaterTemp(float minWaterTemp) {
        MinWaterTemp = minWaterTemp;
    }

    public float getMaxWaterTemp() {
        return MaxWaterTemp;
    }

    public void setMaxWaterTemp(float maxWaterTemp) {
        MaxWaterTemp = maxWaterTemp;
    }

    public float getMinHumidity() {
        return MinHumidity;
    }

    public void setMinHumidity(float minHumidity) {
        MinHumidity = minHumidity;
    }

    public float getMaxHumidity() {
        return MaxHumidity;
    }

    public void setMaxHumidity(float maxHumidity) {
        MaxHumidity = maxHumidity;
    }

    public void setPlantTemp(float plantTemp) {
        PlantTemp = plantTemp;
    }

    public float getPlantTemp() {
        return PlantTemp;
    }

    public LocalDate getLightOnTime() {
        return LightOnTime;
    }

    public void setLightOnTime(LocalDate lightOnTime) {
        LightOnTime = lightOnTime;
    }

    public LocalDate getLightOffTime() {
        return LightOffTime;
    }

    public void setLightOffTime(LocalDate lightOffTime) {
        LightOffTime = lightOffTime;
    }

    @Override
    public String toString() {
        return this.getSystemId() + ","
                + this.getMinPH() + ","
                + this.getMaxPH() + ","
                + this.getMinTDS() + ","
                + this.getMaxTDS() + ","
                + this.getMinWaterTemp() + ","
                + this.getMaxWaterTemp() + ","
                + this.getPlantTemp() + ","
                + this.getLightOnTime() + ","
                + this.getLightOffTime() + ","
                + LocalDateTime.now().getDayOfMonth() + ","
                + LocalDateTime.now().getYear() + ","
                + LocalDateTime.now().getHour() + ","
                + LocalDateTime.now().getMinute() + ","
                + LocalDateTime.now().getSecond();
    }

}
