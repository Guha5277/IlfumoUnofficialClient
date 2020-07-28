package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;

import com.google.gson.JsonSyntaxException;

import ru.guhar4k.ilfumoclient.common.DataProtocol;
import ru.guhar4k.ilfumoclient.common.Library;
import ru.guhar4k.ilfumoclient.product.Product;
import ru.guhar4k.ilfumoclient.product.Warehouse;

public class MessageHandler implements MessageHandlerImpl {
    private MessageHandlerListener listener;
    private ImageDownloader imageDownloader;
    private final int PRODUCT_REQUEST_HEADER_LENGTH = 2;
    private final int IMAGE_HEADER_LENGTH = 2;
    private final int PRODUCT_ID = 0;

    public MessageHandler(MessageHandlerListener listener) {
        this.listener = listener;
        imageDownloader = new ImageDownloader();
    }

    @Override
    public void handleMessage(String msg) {
        DataProtocol receivedData;
        try {
            receivedData = Library.jsonToObject(msg);
        } catch (com.google.gson.JsonSyntaxException e) {
            //TODO handle exception
            return;
        }

        byte[] header = receivedData.getHeader();
        String message = receivedData.getData();

        switch (header[0]) {
            case Library.PRODUCT_REQUEST:
                handleProductRequestResponse(header);
                break;
            case Library.PRODUCT_LIST:
                handleProductList(message);
                break;
            case Library.PRODUCT_LIST_END:
                handleProductListEnd(message);
                break;
            case Library.REMAINS:
                handleRemains(message);
                break;
            case Library.WAREHOUSE_LIST:
                handleWarehousesList(message);
                break;
            case Library.WAREHOUSE_LIST_END:
                handleWarehousesListEnd();
                break;
            case Library.IMAGE:
                handleImage(header, message);
                break;
        }
    }

    private void handleProductRequestResponse(byte[] header) {
        if (header.length != PRODUCT_REQUEST_HEADER_LENGTH) {
            //TODO handle wrong server response
            return;
        }
        if (header[1] == Library.EMPTY) listener.noResultForProductRequest();
    }

    private void handleProductList(String message) {
        Product product;
        try {
            product = Library.productFromJson(message);
        } catch (JsonSyntaxException e) {
            //TODO handle exception
            return;
        }
        listener.onProductReceived(product);
    }

    private void handleProductListEnd(String message) {
        boolean hasNextPage = Boolean.parseBoolean(message);
        listener.productListPageChanged(hasNextPage);
    }

    private void handleRemains(String message) {
        if (message == null) {
            //TODO handle wrong server response
            return;
        }
        listener.onRemainsReceived(message);
    }

    private void handleWarehousesList(String message) {
        if (message == null) {
            //TODO handle wrong server response
            return;
        }
        Warehouse warehouse;
        try {
            warehouse = Library.warehouseFromJson(message);
        } catch (JsonSyntaxException e) {
            //TODO handle exception
            return;
        }
        listener.onWarehousesReceived(warehouse);
    }

    private void handleWarehousesListEnd() {
        listener.onWarehousesListEnd();
    }

    private void handleImage(byte[] header, String message) {
        if (header.length != IMAGE_HEADER_LENGTH) {
            //TODO handle wrong server response
            return;
        }
        switch (header[1]) {
            case Library.EXCEPTION:
                //TODO handle exception from server
                break;
            case Library.NO_IMAGE:
                listener.noImageForProduct(Integer.parseInt(message));
                break;
            case Library.FIRST_CHUNK:
                imageDownloader.storeImageFirstChunk(message.split(Library.DELIMITER));
                break;
            case Library.TRANSIT_CHUNK:
                imageDownloader.storeImageTransitChunk(message.split(Library.DELIMITER));
                break;
            case Library.LAST_CHUNK:
                handleImageLastChunk(message);
                break;
            case Library.FULL:
                handleFullImage(message);
                break;
        }
    }

    private void handleImageLastChunk(String message) {
        String[] messageParts = message.split(Library.DELIMITER);
        Bitmap image = imageDownloader.storeImageLastChunk(messageParts);
        int productID = Integer.parseInt(messageParts[PRODUCT_ID]);
        listener.onImageDownload(productID, image);
    }

    private void handleFullImage(String message) {
        String[] messagePart = message.split(Library.DELIMITER);
        Bitmap image = imageDownloader.storeFullImage(messagePart);
        int productID = Integer.parseInt(messagePart[PRODUCT_ID]);
        listener.onImageDownload(productID, image);
    }
}
