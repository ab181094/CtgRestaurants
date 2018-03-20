package com.csecu.amrit.ctgrestaurants.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amrit on 10/03/2018.
 */

public class Food implements Parcelable {
    int id;
    String name, price, description, type, image;
    int restaurant;
    double rating;

    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        description = in.readString();
        type = in.readString();
        image = in.readString();
        restaurant = in.readInt();
        rating = in.readDouble();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(int restaurant) {
        this.restaurant = restaurant;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Food(String name, String price, String description, String type, int restaurant, double rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
        this.restaurant = restaurant;
        this.rating = rating;
    }

    public Food() {
    }

    public Food(int id, String name, String price, String description, String type, String image, int restaurant, double rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
        this.image = image;
        this.restaurant = restaurant;
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(type);
        parcel.writeString(image);
        parcel.writeInt(restaurant);
        parcel.writeDouble(rating);
    }
}
