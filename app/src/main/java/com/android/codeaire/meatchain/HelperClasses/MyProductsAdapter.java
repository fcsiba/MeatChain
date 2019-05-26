package com.android.codeaire.meatchain.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.codeaire.meatchain.Models.Product;
import com.android.codeaire.meatchain.R;
import com.android.codeaire.meatchain.activities.MyProductDetailsActivity;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;

    public Context getContext() {
        return context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productIdTxt;


        public MyViewHolder(View view) {
            super(view);
            productIdTxt = view.findViewById(R.id.productIdTxt);
        }
    }


    public MyProductsAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_products, parent, false);
        final MyViewHolder mViewHolder = new MyViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // todo : open details activity
                int position = mViewHolder.getPosition();
                Constants.mySingleProduct = productList.get(position);
                context.startActivity(new Intent(context, MyProductDetailsActivity.class));
            }
        });


        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product rowItem = productList.get(position);

        holder.productIdTxt.setText(rowItem.getProductId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Product getDataItem(int index){
        return productList.get(index);
    }
}