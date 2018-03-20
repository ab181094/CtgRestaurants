package com.csecu.amrit.ctgrestaurants.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amrit on 07/03/2018.
 */

public class ResAndOwner implements Parcelable {
    int id;
    String name, owner, phone, password, capacity, description, longitude, latitude, token, image;

    public ResAndOwner(int id, String name, String owner, String phone, String password,
                       String capacity, String description, String longitude, String latitude,
                       String token, String image) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.phone = phone;
        this.password = password;
        this.capacity = capacity;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.token = token;
        this.image = image;
    }

    public ResAndOwner(String name, String owner, String phone, String password, String capacity,
                       String description, String longitude, String latitude, String token) {
        this.name = name;
        this.owner = owner;
        this.phone = phone;
        this.password = password;
        this.capacity = capacity;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.token = token;
    }

    public ResAndOwner() {
    }

    protected ResAndOwner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        owner = in.readString();
        phone = in.readString();
        password = in.readString();
        capacity = in.readString();
        description = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        token = in.readString();
        image = in.readString();
    }

    public static final Creator<ResAndOwner> CREATOR = new Creator<ResAndOwner>() {
        @Override
        public ResAndOwner createFromParcel(Parcel in) {
            return new ResAndOwner(in);
        }

        @Override
        public ResAndOwner[] newArray(int size) {
            return new ResAndOwner[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(owner);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(capacity);
        parcel.writeString(description);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(token);
        parcel.writeString(image);
    }
}
