package com.example.inventaristoko.Retrofit;

import com.example.inventaristoko.Model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("food-list.json")
    Call<List<Food>> getFoods();
}
