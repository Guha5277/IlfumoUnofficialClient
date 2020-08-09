package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.guhar4k.ilfumoclient.common.Library;
import ru.guhar4k.ilfumoclient.common.ProductRequest;
import ru.guhar4k.ilfumoclient.network.SocketThread;
import ru.guhar4k.ilfumoclient.network.SocketThreadListener;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.DailyOffer;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Remains;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public class Model implements PresenterListener.Model, SocketThreadListener, MessageHandlerListener {
    private ModelListener listener;
    private MessageHandlerImpl messageHandler;
    private String ip = "109.111.178.130";
    private int port = 5277;
    private SocketThread socketThread;
    private ExecutorService threadPool;
    private final String LOG_TAG = "Model";
    private int sortType;

    public Model(ModelListener listener) {
        this.listener = listener;
        threadPool = Executors.newFixedThreadPool(2, Thread::new);
        messageHandler = new MessageHandler(this);
    }

    //Presenter events
    @Override
    public void onUIReady() {
        if (socketThread != null && socketThread.isAlive()) return;
        threadPool.execute(this::connect);
    }

    @Override
    public void onAppClosed() {
        if (socketThread != null) socketThread.close();
        threadPool.shutdown();
    }

    @Override
    public void onAppPaused() {

    }

    @Override
    public void getMoreProducts() {
        threadPool.execute(this::sendProductRequest);
    }

    @Override
    public void newProductRequest(int city, int store, int volumeStart, int volumeEnd, int strengthStart, int strengthEnd, int priceStart, int priceEnd) {
        threadPool.execute(() -> {
            sendMessage(Library.productRequestToJson(new ProductRequest(true, city, store, strengthStart, strengthEnd, volumeStart, volumeEnd, priceStart, priceEnd, sortType)));
        });
    }

    @Override
    public void getRemainsForProduct(int productID) {
        threadPool.execute(() -> getProductRemains(productID));
    }

    @Override
    public void onSortRequest(int sortType) {
        this.sortType = sortType;
        threadPool.execute(() -> sendMessage(msgOf(header(Library.PRODUCT_REQUEST, Library.SORT), String.valueOf(sortType))));
    }

    @Override
    public void isHomePageReady() {
                String msg = "{\n" +
                "  \"header\": [\n" +
                "    30,\n" +
                "    45\n" +
                "  ],\n" +
                "  \"dataLength\": 176,\n" +
                "  \"data\": \"{\\n  \\\"stock\\\": true,\\n  \\\"regionID\\\": -1,\\n  \\\"storeID\\\": -1,\\n  \\\"strengthStart\\\": -1,\\n  \\\"strengthEnd\\\": -1,\\n  \\\"volumeStart\\\": -1,\\n  \\\"volumeEnd\\\": -1,\\n  \\\"priceStart\\\": -1,\\n  \\\"priceEnd\\\": -1\\n}\"\n" +
                "} ";
        socketThread.sendMessage(msg);
    }

    //socket events
    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        getServerInfo();
        sendNewProductRequest();
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {

    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMsg(msg);
    }

    private void handleMsg(String msg) {
        messageHandler.handleMessage(msg);
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception e) {

    }

    //Getting an imageView from server or HW
    void getImage(int id) {
        Log.i(LOG_TAG, "Getting an image for product with id: " + id);
        socketThread.sendMessage(msgOf(header(Library.IMAGE), String.valueOf(id)));
    }

    private String msgOf(byte[] header, String... data) {
        return Library.makeJsonString(header, data);
    }

    private byte[] header(byte... header) {
        return header;
    }

    private void sendProductRequest() {
        socketThread.sendMessage(msgOf(header(Library.PRODUCT_REQUEST, Library.NEXT)));
    }

    private void connect() {
        try {
            Socket socket = new Socket(ip, port);
            socketThread = new SocketThread(this, "client", socket);
            Log.i(LOG_TAG, "Successful connected");
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(e.getCause()));
        }
    }

    private void getServerInfo(){
        socketThread.sendMessage(msgOf(header(Library.SERVER_INFO)));
    }

    private void sendNewProductRequest() {
        sendMessage(msgOf(header(Library.PRODUCT_REQUEST, Library.DAILY_OFFER)));
    }

    void sendMessage(String msg) {
        socketThread.sendMessage(msg);
    }

    void getProductRemains(int productID){
        String request = msgOf(header(Library.REMAINS), String.valueOf(productID));
        sendMessage(request);
    }

    private List<Remains> parseRemains(String data){
        String[] arrayData = data.split(Library.DELIMITER);
        int productID = Integer.parseInt(arrayData[arrayData.length - 1]);

        ArrayList<Remains> remains = new ArrayList<>();

        for (int i = 0; i < arrayData.length - 1; i++){
            String[] arr = arrayData[i].split(":");
            Warehouse warehouse = listener.getWarehouseByID(Integer.parseInt(arr[0]));
            remains.add(new Remains(productID, warehouse, Integer.parseInt(arr[1])));
        }
        return remains;
    }

    //Message Handler
    @Override
    public void noResultForProductRequest() {
        //TODO
    }

    @Override
    public void onProductReceived(Product product) {
        listener.onProductReceived(product);
        getImage(product.getId());
    }

    @Override
    public void productListPageChanged(boolean hasNextPage) {
        listener.availableProducts(hasNextPage);
    }

    @Override
    public void onRemainsReceived(String remains) {
        List<Remains> remainsList = parseRemains(remains);
        listener.onRemainsReceived(remainsList);
    }

    @Override
    public void onWarehousesReceived(Warehouse warehouse) {
        listener.warehouseReceived(warehouse);
    }

    @Override
    public void onWarehousesListEnd() {
        listener.warehouseListEnd();
    }

    @Override
    public void noImageForProduct(int productID) {
        listener.noImageForProduct(productID);
    }

    @Override
    public void onImageDownload(int productID, Bitmap image) {
        listener.onImageDownload(productID, image);
    }

    @Override
    public void onDailyOfferReceived(DailyOffer dailyOffer) {
        listener.onDailyOfferReceived(dailyOffer);
        getImage(dailyOffer.getProductsList().get(0).getId());
    }

    @Override
    public void onDailyOfferCategoryReceived(String offerName) {
        listener.onDailyOfferCategoryReceived(offerName);
    }
}
