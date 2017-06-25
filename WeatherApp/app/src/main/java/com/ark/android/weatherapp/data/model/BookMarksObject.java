package com.ark.android.weatherapp.data.model;

/**
 * BookmarkObject that is used through out our app and in inserting and retrieving from dataBase
 * Created by Ark on 6/24/2017.
 */

public class BookMarksObject extends BaseModel{
    private String title;
    private String geoAddress;
    private long id;
    private double longitude;
    private double latitude;
    private long createdTime;
    private WeatherObj weatherObj;
    private boolean updateFailed;
    private boolean updating;
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isUpdateFailed() {
        return updateFailed;
    }

    public void setUpdateFailed(boolean updateFailed) {
        this.updateFailed = updateFailed;
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public WeatherObj getWeatherObj() {
        return weatherObj;
    }

    public void setWeatherObj(WeatherObj weatherObj) {
        this.weatherObj = weatherObj;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGeoAddress() {
        return geoAddress;
    }

    public void setGeoAddress(String geoAddress) {
        this.geoAddress = geoAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
