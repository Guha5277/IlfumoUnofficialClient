package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;

import java.io.Serializable;

import ru.guhar4k.ilfumoclient.product.Product;

class ProductItem implements Serializable {
    static final int NOT_LOAD = 0;
    static final int HAS_IMAGE = 1;
    static final int NO_IMAGE = 2;

    final Product product;
    int position;
    int hasImage;
    Bitmap image;

    ProductItem(Product product){
        this.product = product;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Product getProduct() {
        return product;
    }

    public int getImageStatus() {
        return hasImage;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setImageStatus(int status) {
        hasImage = status;
    }
}
