package ru.guhar4k.ilfumoclient.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ru.guhar4k.ilfumoclient.model.Model;
import ru.guhar4k.ilfumoclient.model.ModelListener;
import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.product.Warehouse;
import ru.guhar4k.ilfumoclient.view.ViewListener;
import ru.guhar4k.ilfumoclient.view.adapters.WarehousesProvider;

public class Presenter implements ModelListener, ViewListener, WarehousesProvider {
    private static final String LOGTAG = "Presenter";
    PresenterListener.View viewListener;
    PresenterListener.Model modelListener;

    private boolean isAllProductsLoaded;
    private boolean readyToGetProduct;
    private boolean isAllDailyOffersReady;
    private List<Warehouse> warehouses = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private HashMap<String, List<String>> citiesAndStores = new HashMap<>();
    private HashMap<String, DailyOffer> dailyOfferContent = new HashMap<>();

    public Presenter(PresenterListener.View listener) {
        viewListener = listener;
        modelListener = new Model(this);
    }

    //UI evetns
    @Override
    public void onAppReady() {
        modelListener.onUIReady();
    }

    @Override
    public void onAppClosed() {
        modelListener.onAppClosed();
    }

    @Override
    public void onAppPaused() {
        modelListener.onAppPaused();
    }

    @Override
    public void getMoreProducts() {
        if (readyToGetProduct && !isAllProductsLoaded){
            readyToGetProduct = false;
            modelListener.getMoreProducts();
        }
    }

    @Override
    public void availableProducts(boolean hasNextPage) {
        isAllProductsLoaded = !hasNextPage;
        readyToGetProduct = true;
    }

    @Override
    public List<String> getCitiesList() {
        return cities;
    }

    @Override
    public void onApplyProductFilter(String city, String store, String volumeStart, String volumeEnd, String strengthStart, String strengthEnd, String priceStart, String priceEnd) {
        int valStrengthStart = strengthStart.equals("") ? -1 : Integer.parseInt(strengthStart);
        int valStrengthEnd = strengthEnd.equals("") ? -1 : Integer.parseInt(strengthEnd);
        int valVolumeStart = volumeStart.equals("") ? -1 : Integer.parseInt(volumeStart);
        int valVolumeEnd = volumeEnd.equals("") ? -1 : Integer.parseInt(volumeEnd);
        int valPriceStart = priceStart.equals("") ? -1 : Integer.parseInt(priceStart);
        int valPriceEnd = priceEnd.equals("") ? -1 : Integer.parseInt(priceEnd);

        int regionID = -1;
        int storeID = -1;

        if (city != null) {
            for (Warehouse w : warehouses) {
                if (w.getCity().equals(city)) {
                    regionID = w.getRegion();
                    break;
                }
            }
        }
        if (store != null) {
            for (Warehouse w : warehouses) {
                if (w.getAddress().equals(store)) {
                    storeID = w.getId();
                    break;
                }
            }
        }
        modelListener.newProductRequest(regionID, storeID, valVolumeStart, valVolumeEnd, valStrengthStart, valStrengthEnd, valPriceStart, valPriceEnd);
    }

    @Override
    public void onnSortRequest(int sortType) {
        modelListener.onSortRequest(sortType);
    }

    @Override
    public void onHomeClicked(Fragment fragment) {
        viewListener.onHomeClicked(fragment);
    }

    @Override
    public void onSearchClicked(Fragment fragment) {
        viewListener.onSearchClicked(fragment);
    }

    @Override
    public void onFavoriteClicked(Fragment fragment) {
        viewListener.onFavoriteClicked(fragment);
    }

    //Model events
    @Override
    public void onProductReceived(Product product) {
        viewListener.onProductFound(product);
    }

    @Override
    public void onImageDownload(int productID, Bitmap image) {
        if (isAllDailyOffersReady){
            viewListener.onProductImageDownload(productID, image);
        } else {
            boolean result = false;
            DailyOffer dailyOffer = null;

            for (HashMap.Entry<String, DailyOffer> mapEntry : dailyOfferContent.entrySet()){
                dailyOffer = mapEntry.getValue();
                if (dailyOffer.containsProduct(productID)){
                    result = dailyOffer.addImage(productID, image);
                    break;
                }
            }

            if (dailyOffer != null && dailyOffer.isReady()) sendDailyOfferToView(dailyOffer);

            if (!result){
                viewListener.onProductImageDownload(productID, image);
            }
        }
    }

    @Override
    public void noImageForProduct(int productID) {
        Log.i(LOGTAG, "No image for product");
        if (isAllDailyOffersReady){
            viewListener.noImageForProduct(productID);
        } else {
            boolean result = false;
            DailyOffer dailyOffer = null;

            for (HashMap.Entry<String, DailyOffer> mapEntry : dailyOfferContent.entrySet()){
                dailyOffer = mapEntry.getValue();
                if (dailyOffer.containsProduct(productID)){
                    result = dailyOffer.addNoImageMarker(productID);
                    break;
                }
            }

            if (dailyOffer != null && dailyOffer.isReady()) sendDailyOfferToView(dailyOffer);

            if (!result){
                viewListener.noImageForProduct(productID);
            }
        }
        //viewListener.noImageForProduct(productID);
    }

    private void sendDailyOfferToView(DailyOffer dailyOffer) {
        viewListener.addDailyOffer(dailyOffer);
        Log.i(LOGTAG, "Daily offer sent to view: " + dailyOffer.getName());
        dailyOfferContent.remove(dailyOffer.getName());
        isAllDailyOffersReady = dailyOfferContent.size() == 0;
        if (isAllDailyOffersReady) modelListener.isHomePageReady();
        Log.i(LOGTAG, "Daily offer content size " + dailyOfferContent.size());
    }

    @Override
    public void warehouseReceived(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    @Override
    public void warehouseListEnd() {
        Log.i(LOGTAG,"Received warehouses list");
        ArrayList<Warehouse> list = new ArrayList<>(warehouses);
        int index = 0;

        cities.add("Все города");

        while (list.size() > 0) {
            int region = list.get(index).getRegion();
            String city = list.get(index).getCity();

            cities.add(city);
            Iterator<Warehouse> iterator = list.iterator();

            while (iterator.hasNext()) {
                Warehouse warehouse = iterator.next();
                if (warehouse.getRegion() == region) {
                    if (!citiesAndStores.containsKey(city)) {
                        List<String> tempList = new ArrayList<>();
                        tempList.add("Все магазины");
                        tempList.add(warehouse.getAddress());
                        citiesAndStores.put(city, tempList);
                    } else {
                        citiesAndStores.get(city).add(warehouse.getAddress());
                    }
                    iterator.remove();
                    index = 0;
                }
            }
        }

        viewListener.onWarehousesInfoReceived();
    }

    @Override
    public void getRemainsForProduct(int productID) {
        modelListener.getRemainsForProduct(productID);
    }

    @Override
    public Warehouse getWarehouseByID(int id){
        for (Warehouse w : warehouses){
            if (w.getId() == id) {
                return w;
            }
        }
        return null;
    }

    @Override
    public void onRemainsReceived(List<Remains> remains) {
        viewListener.onRemainsReceived(remains);
    }

    @Override
    public void onDailyOfferReceived(DailyOffer dailyOffer) {
        //viewListener.onDailyOfferReceived(dailyOffer);
        String name = dailyOffer.getName();
        if (dailyOfferContent.containsKey(name)){
            dailyOfferContent.get(name).addProduct(dailyOffer.getProductsList().get(0));
        } else {
            dailyOfferContent.put(name, dailyOffer);
        }
    }

    @Override
    public void onDailyOfferCategoryReceived(String offerName) {
        //viewListener.onDailyOfferCategoryReceived(offerName);
        if (!dailyOfferContent.containsKey(offerName)){
            //TODO generate some error
            return;
        }

        DailyOffer dailyOffer = dailyOfferContent.get(offerName);

        Log.i(LOGTAG, "Daily offer with name " + offerName + " complete received. Offer size is: " + dailyOffer.getProductsList().size());

        dailyOffer.setAllProductReceived(true);

        if (dailyOffer.isReady()){
            sendDailyOfferToView(dailyOffer);
        }

        //List<Product> productList = dailyOfferContent.get(offerName);
        //listener.addDailyOffer(offerName, productList);
    }

    //WarehousesProvider events
    @Override
    public List<String> getWarehouses(String selectedItem) {
        return citiesAndStores.get(selectedItem);
    }
}
