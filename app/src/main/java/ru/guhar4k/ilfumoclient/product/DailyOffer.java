package ru.guhar4k.ilfumoclient.product;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Optional;

public class DailyOffer {
    private final String name;
    private ArrayList<Product> products;
    private boolean isAllImageReady;
    private boolean isAllProductReceived;

    public DailyOffer(String name, Product product) {
        this.name = name;
        this.products = new ArrayList<>();
        this.products.add(product);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProductsList() {
        return products;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public boolean isAllImageReady(){
        return isAllImageReady;
    }

    public boolean containsProduct(int productID){
        return products.stream().anyMatch(p -> p.getId() == productID);
    }

    public boolean addImage(int productID, Bitmap image){
        Optional<Product> product = products.stream().filter(p -> p.getId() == productID).findFirst();
        product.ifPresent(p -> {
            p.setImage(image);
            checkForAllImagesDownload();
        });

        return product.isPresent();
    }

    public boolean addNoImageMarker(int productID){
        Optional<Product> product = products.stream().filter(p -> p.getId() == productID).findFirst();
        product.ifPresent(p -> {
            p.setNoImage();
            checkForAllImagesDownload();
        });

        return product.isPresent();
    }

    public void checkForAllImagesDownload(){
        isAllImageReady = products.stream().allMatch(Product::isImageSet);
    }



    public boolean isAllProductReceived() {
        return isAllProductReceived;
    }

    public void setAllProductReceived(boolean allProductReceived) {
        isAllProductReceived = allProductReceived;
        if (!isAllImageReady) checkForAllImagesDownload();
    }

    public boolean isReady() {
        return isAllProductReceived && isAllImageReady;
    }
}

