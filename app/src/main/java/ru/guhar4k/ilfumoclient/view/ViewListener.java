package ru.guhar4k.ilfumoclient.view;

import androidx.fragment.app.Fragment;

import java.util.List;

public interface ViewListener {
    void onAppReady();
    void onAppClosed();
    void onAppPaused();
    void getMoreProducts();
    List<String> getCitiesList();
    void onApplyProductFilter(String city, String store, String volumeStart, String volumeEnd, String strengthStart, String strengthEnd, String priceStart, String priceEnd);
    void getRemainsForProduct(int id);
    void onnSortRequest(int sortType);
    void onHomeClicked(Fragment fragment);
    void onSearchClicked(Fragment fragment);
    void onFavoriteClicked(Fragment fragment);
}
