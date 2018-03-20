package com.csecu.amrit.ctgrestaurants.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amrit on 18/03/2018.
 */

public class Booking implements Parcelable {
    int id;
    String name, phone, occassion, person, date, time, item, req, restaurant, token;

    public Booking(String name, String phone, String occassion, String person, String date,
                   String time, String item, String req, String restaurant, String token) {
        this.name = name;
        this.phone = phone;
        this.occassion = occassion;
        this.person = person;
        this.date = date;
        this.time = time;
        this.item = item;
        this.req = req;
        this.restaurant = restaurant;
        this.token = token;
    }

    public Booking() {
    }

    public Booking(int id, String name, String phone, String occassion, String person, String date,
                   String time, String item, String req, String restaurant, String token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.occassion = occassion;
        this.person = person;
        this.date = date;
        this.time = time;
        this.item = item;
        this.req = req;
        this.restaurant = restaurant;
        this.token = token;
    }

    protected Booking(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
        occassion = in.readString();
        person = in.readString();
        date = in.readString();
        time = in.readString();
        item = in.readString();
        req = in.readString();
        restaurant = in.readString();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(occassion);
        dest.writeString(person);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(item);
        dest.writeString(req);
        dest.writeString(restaurant);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOccassion() {
        return occassion;
    }

    public void setOccassion(String occassion) {
        this.occassion = occassion;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
