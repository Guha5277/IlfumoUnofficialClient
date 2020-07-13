package ru.guhar4k.ilfumoclient.product;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable{
    private String name;
    private String URL;
    private int id;
    private int categoryID;
    private Group group;
    private int price;
    private int volume;
    private double strength;
    private List<Warehouse> remains;
    private String imageID;

    public Product(String name, String URL, int price) {
        this.name = name;
        this.URL = URL;
        this.price = price;
    }

    public Product(String name, String URL, int price, Group group, int categoryID) {
        this(name, URL, price);
        this.group = group;
        this.categoryID = categoryID;
    }

    public Product(int id, String name, String URL, int price, Group group, int categoryID, int volume, double strength, String imageID) {
        this(name, URL, price);
        this.id = id;
        this.group = group;
        this.categoryID = categoryID;
        this.volume = volume;
        this.strength = strength;
        this.imageID = imageID;
    }

    public Product(int id, String name, String URL, int price, int categoryID, int volume, double strength) {
        this(name, URL, price);
        this.id = id;
        this.categoryID = categoryID;
        this.volume = volume;
        this.strength = strength;
    }

    public Product(Parcel parcel){
        name = parcel.readString();
        URL = parcel.readString();
        id = parcel.readInt();
        categoryID = parcel.readInt();
        price = parcel.readInt();
        volume = parcel.readInt();
        strength = parcel.readDouble();
        parcel.readList(remains,Warehouse.class.getClassLoader());
        imageID = parcel.readString();
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public int getPrice() {
        return price;
    }

    public Group getGroup() {
        return group;
    }

    public int getVolume() {
        return volume;
    }

    public double getStrength() {
        return strength;
    }

    public int getId() {
        return id;
    }

    public String getImageID() {
        return imageID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRemainsCount(){
        if (remains == null) return 0;
        return remains.size();
    }

    public void addRemain(Warehouse warehouse){
        if (remains == null){
            remains = new ArrayList<>();
        }
        remains.add(warehouse);
    }

    public List<Warehouse> getRemainsList(){
        return remains;
    }

    @Override
    public String toString() {
        return "<<product.Product: " + name
                + "\n\tURL: " + URL
                + "\n\tID: " + id
                + "\n\tCategoryID: " + categoryID
                + "\n\tgroup: " + group
                + "\n\tprice: " + price
                + "\n\tvolume: " + volume
                + "\n\tstrength: " + strength + ">>";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product && ((Product) obj).getId() == id;
    }

    //parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(URL);
        parcel.writeInt(id);
        parcel.writeInt(categoryID);
        parcel.writeInt(price);
        parcel.writeInt(volume);
        parcel.writeDouble(strength);
        parcel.writeList(remains);
        parcel.writeString(imageID);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel parcel) {
            return new Product(parcel);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
