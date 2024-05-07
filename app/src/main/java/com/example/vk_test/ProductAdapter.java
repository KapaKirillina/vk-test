package com.example.vk_test;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.bind(product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetail.class);
                intent.putExtra("title", product.getTitle());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("thumbnail", product.getThumbnail());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewDescription, textViewPrice;
        ImageView imageViewThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }

        public void bind(Product product){
            textViewTitle.setText(product.getTitle());
            textViewDescription.setText(product.getDescription());
            textViewPrice.setText("$" + String.valueOf(product.getPrice()));

            Glide.with(itemView.getContext())
                    .load(product.getThumbnail())
                    .into(imageViewThumbnail);
        }
    }
}
