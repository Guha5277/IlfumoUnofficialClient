package ru.guhar4k.ilfumoclient.presenter;

import android.graphics.Bitmap;

import ru.guhar4k.ilfumoclient.product.Product;

public interface PresenterListener {
    interface Model {
        void onUIReady();

        void onAppClosed();

        void onAppPaused();

        void getMoreProducts();
    }

    interface View {
        void onProductFound(Product product);

        void onProductImageDownload(int productID, Bitmap image);

        void noImageForProduct(int productID);
    }
}
