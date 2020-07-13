package ru.guhar4k.ilfumoclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;

public class ProductPageActivity extends AppCompatActivity {
    private final String LOGTAG = "ProductPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        ProductItem productItem = (ProductItem) getIntent().getExtras().getSerializable(ProductItem.class.getSimpleName());
        Product product = productItem.getProduct();
        Log.i(LOGTAG, product.getName());
        Log.i(LOGTAG, String.valueOf(product.getGroup()));
        Log.i(LOGTAG, product.getURL());
        Log.i(LOGTAG, String.valueOf(product.getPrice()));
    }
}