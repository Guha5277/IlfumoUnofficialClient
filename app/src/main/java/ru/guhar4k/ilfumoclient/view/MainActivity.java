package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.Product;

public class MainActivity extends AppCompatActivity implements PresenterListener.View, View.OnClickListener {
    private ViewListener listener;
//    private boolean isMainPageLoaded;
    private RecyclerView productListView;
    private ProductAdapter productAdapter;
    private ImageButton filterButton;
    private AlertDialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterButton = findViewById(R.id.ib_filter);
        filterButton.setEnabled(false);
        filterButton.setOnClickListener(this);

        initRecycler();

        //TODO list end detection
        productListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    handleListScrolled();
                }
            }
        });

        listener = new Presenter(this);
    }

    private void initFilter(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View my_custom_view = getLayoutInflater().inflate(R.layout.filter_dialog, null); //находим разметку
        adb.setView(my_custom_view); //ставим ее в окно
        Spinner spinnerCity = my_custom_view.findViewById(R.id.spinner_city);

        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listener.getCitiesList());
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCity.setAdapter(adapterCities);

        //TextView ad_tv = my_custom_view.findViewById(R.id.tv_test); //находим TextView
        //ad_tv.setTextColor(Color.BLACK);
        filterDialog = adb.create();

        filterButton.setEnabled(true);
    }

    private void showFilterDialog(){
        filterDialog.show();
    }

    private void handleListScrolled() {
        listener.getMoreProducts();
    }

    private void initRecycler(){
        productListView = findViewById(R.id.tv_products);
        productListView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter();
        productListView.setAdapter(productAdapter);
    }

    //OnClickHandler
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_filter:
                showFilterDialog();
        }
    }

    //Presenter events
    @Override
    public void onProductFound(Product product) {
        runOnUiThread(() -> productAdapter.addItem(product));
    }

    @Override
    public void onProductImageDownload(int productID, Bitmap image) {
        runOnUiThread(() -> productAdapter.addImage(productID, image));
    }

    @Override
    public void noImageForProduct(int productID) {
        runOnUiThread(() -> productAdapter.noImageForProduct(productID));
    }

    @Override
    public void onWarehousesInfoReceived() {
        runOnUiThread(this::initFilter);
    }

    //Lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        listener.onAppReady();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listener.onAppPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener.onAppClosed();
    }
}