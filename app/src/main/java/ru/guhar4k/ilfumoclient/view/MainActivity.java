package ru.guhar4k.ilfumoclient.view;

import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.Product;

public class MainActivity extends AppCompatActivity implements PresenterListener.View, SearchFragment.OnClickListener {
    private static final String LOGTAG = "MainActivity";
    private ViewListener listener;
    private RecyclerView productListView;
    private ProductAdapter productAdapter;
    private ImageButton filterButton;
    private AlertDialog filterDialog;

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchFragment = SearchFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, searchFragment).commit();

//        filterButton = findViewById(R.id.ib_filter);
//        filterButton.setEnabled(false);
//        filterButton.setOnClickListener(this);

//        initRecycler();
//
//        productListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1)) {
//                    handleListScrolled();
//                }
//            }
//        });

        listener = new Presenter(this);
    }

    //Listener for search fragment listener
    @Override
    public void onClick(ProductItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProductFragment productFragment = ProductFragment.newInstance(item);
        ft.hide(searchFragment);
        ft.add(R.id.fl_content, productFragment);
//        ft.replace(R.id.fl_content, productFragment);
        ft.addToBackStack(null);
//        ft.hide(searchFragment);
        ft.commit();
    }

    //    private void initFilterDialog() {
//        AlertDialog.Builder adb = new AlertDialog.Builder(this);
//        View filterView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
//        adb.setView(filterView);
//        Spinner spinnerCity = filterView.findViewById(R.id.spinner_city);
//        Spinner spinnerStore = filterView.findViewById(R.id.spinner_store);
//
//        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listener.getCitiesList());
//        WarehousesAdapter storesAdapter = new WarehousesAdapter(this, android.R.layout.simple_spinner_item, (WarehousesProvider) listener, spinnerStore);
//
//        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        storesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinnerCity.setAdapter(adapterCities);
//        spinnerStore.setAdapter(storesAdapter);
//
//        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                storesAdapter.onParentAdapterChanged(parent.getItemAtPosition(position).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        Button button = filterView.findViewById(R.id.btn_filter_ok);
//        EditText etVolumeStart = filterView.findViewById(R.id.et_volume_start);
//        EditText etVolumeEnd = filterView.findViewById(R.id.et_volume_end);
//        EditText etStrengthStart = filterView.findViewById(R.id.et_strength_start);
//        EditText etStrengthEnd = filterView.findViewById(R.id.et_strength_end);
//        EditText etPriceStart = filterView.findViewById(R.id.et_price_start);
//        EditText etPriceEnd = filterView.findViewById(R.id.et_price_end);
//
//        button.setOnClickListener(view -> {
//            String city = spinnerCity.getSelectedItem().toString();
//            Object storeSelected = spinnerStore.getSelectedItem();
//            String store = storeSelected == null ? null : spinnerStore.getSelectedItem().toString();
//            String txtStrengthStart = etStrengthStart.getText().toString();
//            String txtStrengthEnd = etStrengthEnd.getText().toString();
//            String txtVolumeStart = etVolumeStart.getText().toString();
//            String txtVolumeEnd = etVolumeEnd.getText().toString();
//            String txtPriceStart = etPriceStart.getText().toString();
//            String txtPriceEnd = etPriceEnd.getText().toString();
//
//            listener.onApplyProductFilter(city, store, txtStrengthStart, txtStrengthEnd, txtVolumeStart, txtVolumeEnd, txtPriceStart, txtPriceEnd);
//            productAdapter.clearItems();
//            filterDialog.cancel();
//        });
//
//        filterDialog = adb.create();
//        filterButton.setEnabled(true);
//    }

    //ProductAdapter on click listener
//    @Override
//    public void onClick(ProductItem item) {
//        Intent intent = new Intent(this, ProductPageActivity.class);
//        intent.putExtra(ProductItem.class.getSimpleName(), item);
//        startActivity(intent);
//    }

//    private void showFilterDialog() {
//        filterDialog.show();
//    }

//    private void handleListScrolled() {
//        listener.getMoreProducts();
//    }

//    private void initRecycler() {
//        productListView = findViewById(R.id.rv_products);
//        productListView.setLayoutManager(new LinearLayoutManager(this));
//        productAdapter = new ProductAdapter(this);
//        productListView.setAdapter(productAdapter);
//    }

    //OnClickHandler
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ib_filter:
//                showFilterDialog();
//        }
//    }

    //Presenter events
    @Override
    public void onProductFound(Product product) {
        runOnUiThread(() -> searchFragment.onProductFound(product));
    }

    @Override
    public void onProductImageDownload(int productID, Bitmap image) {
        runOnUiThread(() -> searchFragment.onImageFound(productID, image));
    }

    @Override
    public void noImageForProduct(int productID) {
        runOnUiThread(() -> searchFragment.noImageForProduct(productID));
    }

    @Override
    public void onWarehousesInfoReceived() {
        runOnUiThread(() -> {
            searchFragment.setListener(listener);
            searchFragment.initFilterDialog(this);
        });

//        runOnUiThread(this::initFilterDialog);
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