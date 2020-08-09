package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public interface MessageHandlerListener {
    void noResultForProductRequest();

    void onProductReceived(Product product);

    void productListPageChanged(boolean hasNextPage);

    void onRemainsReceived(String remains);

    void onWarehousesReceived(Warehouse warehouse);

    void onWarehousesListEnd();

    void noImageForProduct(int productID);

    void onImageDownload(int productID, Bitmap image);

    void onDailyOfferReceived(DailyOffer dailyOffer);

    void onDailyOfferCategoryReceived(String message);
}
