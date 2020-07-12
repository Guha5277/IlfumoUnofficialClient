package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.Product;

public class MainActivity extends AppCompatActivity implements PresenterListener.View, View.OnClickListener {
    private static final String LOGTAG = "MainActivity";
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

    private void initFilterDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View filterView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        adb.setView(filterView);
        Spinner spinnerCity = filterView.findViewById(R.id.spinner_city);
        Spinner spinnerStore = filterView.findViewById(R.id.spinner_store);

        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listener.getCitiesList());
        WarehousesAdapter storesAdapter = new WarehousesAdapter(this, android.R.layout.simple_spinner_item, (WarehousesProvider) listener, spinnerStore);

        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCity.setAdapter(adapterCities);
        spinnerStore.setAdapter(storesAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storesAdapter.onParentAdapterChanged(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText volumeStart = filterView.findViewById(R.id.et_volume_start);
        EditText volumeEnd = filterView.findViewById(R.id.et_volume_end);
        EditText strengthStart = filterView.findViewById(R.id.et_strength_start);
        EditText strengthEnd = filterView.findViewById(R.id.et_strength_end);
        EditText priceStart = filterView.findViewById(R.id.et_price_start);
        EditText priceEnd = filterView.findViewById(R.id.et_price_end);
        Button button = filterView.findViewById(R.id.btn_filter_ok);

        button.setOnClickListener(view -> {
            filterDialog.cancel();
           Log.i(LOGTAG, volumeStart.getText().toString());
        });

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
        runOnUiThread(this::initFilterDialog);
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