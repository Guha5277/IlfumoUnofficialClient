package ru.guhar4k.ilfumoclient.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class ImageDownloader {
    private final String LOGTAG = "ImageDownloader";
    private HashMap<Integer, byte[][]> images = new HashMap<>();

    void storeImageFirstChunk(String[] messageParts) {
        int productID = Integer.parseInt(messageParts[0]);
        int chunkCount = Integer.parseInt(messageParts[1]);
        String chunk = messageParts[2];

        byte[][] imageArray = new byte[chunkCount][];
        imageArray[0] = chunk.getBytes();
        images.put(productID, imageArray);
    }

    void storeImageTransitChunk(String[] messageParts) {
        int productID = Integer.parseInt(messageParts[0]);
        int chunkIndex = Integer.parseInt(messageParts[1]);
        String chunk = messageParts[2];
        byte[][] imageArray = images.get(productID);
        imageArray[chunkIndex] = chunk.getBytes();
    }

    Bitmap storeImageLastChunk(String[] messageParts) {
        int productID = Integer.parseInt(messageParts[0]);
        String chunk = messageParts[1];
        //store last part to map
        byte[][] imageArray = images.get(productID);
        imageArray[imageArray.length - 1] = chunk.getBytes();

        //decode byte arrays from the HashMap to the final result
        byte[] decodedImageBytes = decodeImage(joinByteArrays(imageArray));
        if (decodedImageBytes == null) {
            Log.e(LOGTAG, "Failed to decode an image");
            return null;
        }

        //write to disk
//            FileOutputStream os = new FileOutputStream(IMAGE_PATH + productID + ".jpeg");
//            os.write(decodedImageBytes);

        //Create an image instance
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
        //Image image = new Image(new ByteArrayInputStream(decodedImageBytes));

        //remove image from map
        images.remove(productID);

//            os.close();

//        listener.onImageDownload(productID, bmp);
        return bmp;
    }

    Bitmap storeFullImage(String[] messageParts) {
        int productID = Integer.parseInt(messageParts[0]);
        String chunk = messageParts[1];
        byte[] decodedImageBytes = decodeImage(chunk.getBytes());
        //write to disk
//            FileOutputStream os = new FileOutputStream(IMAGE_PATH + productID + ".jpeg");
//            os.write(result);

        //Create an image instance
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
        //Image image = new Image(new ByteArrayInputStream(result));

        //remove image from map
        images.remove(productID);

//            os.close();
//        listener.onImageDownload(productID, bmp);
        return bmp;
    }

    private byte[] joinByteArrays(byte[][] arrays) {
        byte[] result = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (byte[] b : arrays) {
            try {
                bos.write(b);
                result = bos.toByteArray();
                bos.close();
            } catch (IOException e) {
                Log.e(LOGTAG, "Failed to join arrays");
            }

        }
        return result;
    }

    private byte[] decodeImage(byte[] encodedImage) {
        String encodedString = new String(encodedImage);

        //decode result
//        Base64.Decoder decoder = Base64.getDecoder();
//        return decoder.decode(encodedString);
        return Base64.getDecoder().decode(encodedImage);
    }
}
