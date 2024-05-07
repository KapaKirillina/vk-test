package com.example.vk_test;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<String> categoryList;
    private Spinner spinnerCategories;
    private ImageButton btnPreviousPage, btnNextPage, btnSearch, btnClearSearch;
    private EditText editTextSearch;
    private int skip = 0;
    private static final int LIMIT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        btnSearch = findViewById(R.id.btnSearch);
        editTextSearch = findViewById(R.id.editTextSearch);
        btnPreviousPage = findViewById(R.id.btnPreviousPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        categoryList = new ArrayList<>();
        loadCategories();

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryList.get(position);
                if (selectedCategory.equals("Все категории")) {
                    skip = 0;
                    loadAllProducts(skip, LIMIT);
                } else {
                    skip = 0;
                    loadProductsByCategory(selectedCategory, skip, LIMIT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ничего не делаем
            }
        });

        if (skip == 0) {
            btnPreviousPage.setVisibility(View.GONE);
        } else {
            btnPreviousPage.setVisibility(View.VISIBLE);
        }
        btnPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skip > 0) {
                    skip -= LIMIT;
                    loadProducts(skip);
                }
                if (skip == 0) {
                    btnPreviousPage.setVisibility(View.GONE);
                }
            }
        });

        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip += LIMIT;
                loadProducts(skip);
                if (skip > 0) {
                    btnPreviousPage.setVisibility(View.VISIBLE);
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchProducts(query);
                }
            }
        });


        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                String selectedCategory = spinnerCategories.getSelectedItem().toString();
                if (selectedCategory.equals("Все категории")) {
                    skip = 0;
                    loadAllProducts(skip, LIMIT);
                } else {
                    skip = 0;
                    loadProductsByCategory(selectedCategory, skip, LIMIT);
                }
            }
        });

    }
    private void loadProducts(int skip) {
        String selectedCategory = spinnerCategories.getSelectedItem().toString();
        if (selectedCategory.equals("Все категории")) {
            loadAllProducts(skip, LIMIT);
        } else {
            loadProductsByCategory(selectedCategory, skip, LIMIT);
        }
    }

    private void loadCategories() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://dummyjson.com/products/categories")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseCategories(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void loadAllProducts(int skip, int limit) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dummyjson.com/products").newBuilder();
        urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        urlBuilder.addQueryParameter("limit", String.valueOf(limit));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseAndDisplay(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void parseCategories(String responseData) throws JSONException {
        JSONArray jsonArray = new JSONArray(responseData);
        categoryList.add("Все категории");
        for (int i = 0; i < jsonArray.length(); i++) {
            String category = jsonArray.getString(i);
            categoryList.add(category);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
    }

    private void loadProductsByCategory(String category, int skip, int limit) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dummyjson.com/products/category/" + category).newBuilder();
        urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        urlBuilder.addQueryParameter("limit", String.valueOf(limit));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseAndDisplay(responseData);
                            if (productList.size() <= LIMIT) {
                                btnPreviousPage.setVisibility(View.GONE);
                            } else {
                                btnPreviousPage.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void parseAndDisplay(String response) throws JSONException {
        productList.clear();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("products");

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject productObject = jsonArray.getJSONObject(i);
            String title = productObject.getString("title");
            String description = productObject.getString("description");
            String thumbnail = productObject.getString("thumbnail");
            int price = productObject.getInt("price");

            Product product = new Product(title, description, thumbnail, price);
            productList.add(product);
        }
        productAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
        if (jsonArray.length() < LIMIT) {
            btnNextPage.setVisibility(View.GONE);
        } else {
            btnNextPage.setVisibility(View.VISIBLE);
        }
    }

    private void searchProducts(String query) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://dummyjson.com/products/search").newBuilder();
        urlBuilder.addQueryParameter("q", query);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseAndDisplay(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
