package com.android.codeaire.meatchain.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.codeaire.meatchain.HelperClasses.Constants;
import com.android.codeaire.meatchain.HelperClasses.MyProductsAdapter;
import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.Models.UserInfo;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.BaseClasses.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewProductsActivity extends BaseActivity {

    //XML Views
    private RecyclerView recyclerView;

    // Class Variables
    private static final String TAG = ViewProductsActivity.class.getSimpleName();
    private List<Product> productList;
    private MyProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        recyclerView = findViewById(R.id.recyclerView);

        productList = new ArrayList<>();

        setUpRecyclerView();

        loadData();
    }

    private void loadData() {
        showFullScreenLoader();
        FirebaseDatabase.getInstance().getReference(Constants.PRODUCTS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideFullScreenLoader();


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Product mProduct = dataSnapshot1.getValue(Product.class);

                    if(mProduct == null || mProduct.getProductId() == null ||mProduct.getProductId().isEmpty()){
                        continue;
                    }

                    String userId = Constants.myUser.getUserId();
                    if(userId.equals(mProduct.getInfo0Id()) ||userId.equals(mProduct.getInfo1Id()) ||userId.equals(mProduct.getInfo2Id()) ||userId.equals(mProduct.getInfo3Id())){
                        productList.add(mProduct);
                    }
                }

                adapter.notifyDataSetChanged();

                if(productList.size() == 0){
                    showToast("No Products Found");
                    onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showApiOnFailureError();
                hideFullScreenLoader();
                onBackPressed();
            }
        });

    }

    private void setUpRecyclerView() {
        //initializing recycler view and its adapter.
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewProductsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyProductsAdapter(productList, ViewProductsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    public void GoBack(View view) {
        onBackPressed();
    }
}
