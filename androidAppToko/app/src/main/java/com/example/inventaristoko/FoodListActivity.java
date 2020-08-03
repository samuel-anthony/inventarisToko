package com.example.inventaristoko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.inventaristoko.Adapter.RecyclerAdapter;
import com.example.inventaristoko.Model.Food;
import com.example.inventaristoko.Retrofit.ApiClient;
import com.example.inventaristoko.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends AppCompatActivity {
    private List<Food> foodList;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        foodList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), foodList);
        recyclerView.setAdapter(recyclerAdapter);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Food>> call = apiService.getFoods();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                foodList = response.body();
                Log.d("TAG", "Response = " + foodList);
                recyclerAdapter.setFoodList(foodList);
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}