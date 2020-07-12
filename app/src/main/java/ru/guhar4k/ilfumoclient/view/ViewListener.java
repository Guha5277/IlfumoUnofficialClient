package ru.guhar4k.ilfumoclient.view;

import java.util.List;

public interface ViewListener {
    void onAppReady();
    void onAppClosed();
    void onAppPaused();
    void getMoreProducts();
    List<String> getCitiesList();

    void onApplyProductFilter(String city, String store, int volumeStart, int volumeEnd, int strengthStart, int strengthEnd, int priceStart, int priceEnd);
//    List<String> getWarehousesList();
}
