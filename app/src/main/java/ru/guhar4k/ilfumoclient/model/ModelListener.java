package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import ru.guhar4k.ilfumoclient.product.Product;

public interface ModelListener {
    void onProductFound(Product product);

    void onImageDownload(int productID, Bitmap image);

    void noImageForProduct(int productID);
    //void noImageForProduct(int parseInt);
}
