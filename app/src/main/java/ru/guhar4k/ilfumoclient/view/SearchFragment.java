package ru.guhar4k.ilfumoclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.product.Product;

public class SearchFragment extends Fragment implements ProductListAdapter.OnClickListener {
    private static final String LOGTAG = "SearchFragment";
    private RecyclerView productListView;
    private ProductListAdapter productListAdapter;
    private AlertDialog filterDialog;
    private ImageButton filterButton;
    private ViewListener listener;
    private SearchFragment.OnClickListener clickListener;

    interface OnClickListener {
        void onClick(ProductItem item);
    }

    public SearchFragment() {}
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(OnClickListener clickListener) {
        SearchFragment fragment = new SearchFragment();
        fragment.setOnClickListener(clickListener);
        return fragment;
    }

    void setListener(ViewListener listener) {
        this.listener = listener;
    }

    private void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initRecycler(view);
        filterButton = view.findViewById(R.id.ib_filter);
        filterButton.setEnabled(false);
        filterButton.setOnClickListener(v -> showFilterDialog());
        // Inflate the layout for this fragment
        return view;
    }

    private void initRecycler(View view) {
        productListView = view.findViewById(R.id.rv_products);
        productListView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListAdapter = new ProductListAdapter(this);
        productListView.setAdapter(productListAdapter);

        productListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    handleListScrolled();
                }
            }
        });
    }

    void initFilterDialog(Context context) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View filterView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        adb.setView(filterView);
        Spinner spinnerCity = filterView.findViewById(R.id.spinner_city);
        Spinner spinnerStore = filterView.findViewById(R.id.spinner_store);

        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listener.getCitiesList());
        WarehousesAdapter storesAdapter = new WarehousesAdapter(context, android.R.layout.simple_spinner_item, (WarehousesProvider) listener, spinnerStore);

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

        Button button = filterView.findViewById(R.id.btn_filter_ok);
        EditText etVolumeStart = filterView.findViewById(R.id.et_volume_start);
        EditText etVolumeEnd = filterView.findViewById(R.id.et_volume_end);
        EditText etStrengthStart = filterView.findViewById(R.id.et_strength_start);
        EditText etStrengthEnd = filterView.findViewById(R.id.et_strength_end);
        EditText etPriceStart = filterView.findViewById(R.id.et_price_start);
        EditText etPriceEnd = filterView.findViewById(R.id.et_price_end);

        button.setOnClickListener(view -> {
            String city = spinnerCity.getSelectedItem().toString();
            Object storeSelected = spinnerStore.getSelectedItem();
            String store = storeSelected == null ? null : spinnerStore.getSelectedItem().toString();
            String txtStrengthStart = etStrengthStart.getText().toString();
            String txtStrengthEnd = etStrengthEnd.getText().toString();
            String txtVolumeStart = etVolumeStart.getText().toString();
            String txtVolumeEnd = etVolumeEnd.getText().toString();
            String txtPriceStart = etPriceStart.getText().toString();
            String txtPriceEnd = etPriceEnd.getText().toString();

            listener.onApplyProductFilter(city, store, txtVolumeStart, txtVolumeEnd, txtStrengthStart, txtStrengthEnd, txtPriceStart, txtPriceEnd);
            productListAdapter.clearItems();
            filterDialog.cancel();
        });

        filterDialog = adb.create();
        filterButton.setEnabled(true);
    }

    private void showFilterDialog() {
        filterDialog.show();
    }

    private void handleListScrolled() {
        listener.getMoreProducts();
    }

    void onProductFound(Product product){
        productListAdapter.addItem(product);
    }

    public void onImageFound(int productID, Bitmap image) {
        productListAdapter.addImage(productID, image);
    }

    public void noImageForProduct(int productID) {
        productListAdapter.noImageForProduct(productID);
    }

    //products list click event
    @Override
    public void onClick(ProductItem item) {
        clickListener.onClick(item);
    }
}