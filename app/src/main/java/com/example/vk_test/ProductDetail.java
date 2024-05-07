package com.example.vk_test;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetail extends AppCompatActivity {

    private TextView productTitle, productDesc, productPrice;
    private ImageView productThumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String thumbnail = getIntent().getStringExtra("thumbnail");
        int price = getIntent().getIntExtra("price", 0);

        productTitle = findViewById(R.id.productTitle);
        productThumbnail = findViewById(R.id.productThumbnail);
        productDesc = findViewById(R.id.productDesc);
        productPrice = findViewById(R.id.productPrice);

        productTitle.setText(title);
        productDesc.setText(description);
        productPrice.setText("$" + price);
        Glide.with(this)
                .load(thumbnail)
                .into(productThumbnail);


    }
}