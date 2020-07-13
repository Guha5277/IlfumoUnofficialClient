package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import ru.guhar4k.ilfumoclient.product.Product;

class ProductItem implements Parcelable {
    static final int NOT_LOAD = 0;
    static final int HAS_IMAGE = 1;
    static final int NO_IMAGE = 2;

    private final Product product;
    private int position;
    private int hasImage;
    private Bitmap image;

    ProductItem(Product product){
        this.product = product;
    }

    ProductItem(Parcel parcel){
        product = parcel.readParcelable(Product.class.getClassLoader());
        position = parcel.readInt();
        hasImage = parcel.readInt();
        image = parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public Product getProduct() {
        return product;
    }

    int getImageStatus() {
        return hasImage;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    void setImageStatus(int status) {
        hasImage = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeParcelable(product, flag);
        parcel.writeInt(position);
        parcel.writeInt(hasImage);
        parcel.writeParcelable(image, flag);
    }

    public static final Parcelable.Creator<ProductItem> CREATOR = new Parcelable.Creator<ProductItem>(){
        @Override
        public ProductItem createFromParcel(Parcel parcel) {
            return new ProductItem(parcel);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };
}
