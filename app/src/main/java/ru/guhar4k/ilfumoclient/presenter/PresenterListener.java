package ru.guhar4k.ilfumoclient.presenter;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

import java.util.List;

import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;

public interface PresenterListener {
    interface Model {
        void onUIReady();
        void onAppClosed();
        void onAppPaused();
        void getMoreProducts();
        void newProductRequest(int city, int store, int volumeStart, int volumeEnd, int strengthStart, int strengthEnd, int priceStart, int priceEnd);
        void getRemainsForProduct(int productID);
        void onSortRequest(int sortType);
        void isHomePageReady();
    }

    interface View {
        void onProductFound(Product product);
        void onProductImageDownload(int productID, Bitmap image);
        void noImageForProduct(int productID);
        void onWarehousesInfoReceived();
        void onRemainsReceived(List<Remains> remains);
        void onHomeClicked(Fragment fragment);
        void onSearchClicked(Fragment fragment);
        void onFavoriteClicked(Fragment fragment);

        void addDailyOffer(DailyOffer dailyOffer);
    }
}
