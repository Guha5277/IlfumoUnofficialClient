package ru.guhar4k.ilfumoclient.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.guhar4k.ilfumoclient.R;
import ru.guhar4k.ilfumoclient.presenter.Presenter;
import ru.guhar4k.ilfumoclient.presenter.PresenterListener;
import ru.guhar4k.ilfumoclient.product.Product;

public class MainActivity extends AppCompatActivity implements PresenterListener.View {
    private ViewListener listener;
//    private boolean isMainPageLoaded;
    private RecyclerView productListView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton filterButton = findViewById(R.id.ib_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

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

    private void showFilterDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View my_custom_view = getLayoutInflater().inflate(R.layout.filter_dialog, null); //находим разметку
        adb.setView(my_custom_view); //ставим ее в окно
        //TextView ad_tv = my_custom_view.findViewById(R.id.tv_test); //находим TextView
        //ad_tv.setTextColor(Color.BLACK);
        AlertDialog ad = adb.create();
        ad.show();
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