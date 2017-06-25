package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BookmarkObject that is used through out our app and in inserting and retrieving from dataBase
 * Created by Ark on 6/24/2017.
 */

public class BookMarksObject extends BaseModel implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.geoAddress);
        dest.writeLong(this.id);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeLong(this.createdTime);
        dest.writeParcelable(this.weatherObj, flags);
        dest.writeByte(this.updateFailed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.updating ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
    }

    public BookMarksObject() {
    }

    protected BookMarksObject(Parcel in) {
        this.title = in.readString();
        this.geoAddress = in.readString();
        this.id = in.readLong();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.createdTime = in.readLong();
        this.weatherObj = in.readParcelable(WeatherObj.class.getClassLoader());
        this.updateFailed = in.readByte() != 0;
        this.updating = in.readByte() != 0;
        this.isDefault = in.readByte() != 0;
    }

    public static final Creator<BookMarksObject> CREATOR = new Creator<BookMarksObject>() {
        @Override
        public BookMarksObject createFromParcel(Parcel source) {
            return new BookMarksObject(source);
        }

        @Override
        public BookMarksObject[] newArray(int size) {
            return new BookMarksObject[size];
        }
    };
}
