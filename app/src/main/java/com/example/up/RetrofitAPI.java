package com.example.up;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("animals")
    Call<JSONObject > createPost(@Body JSONObject dataModal);

   /* @PUT("animals/")
    Call<Animal> updateData(@Query("Id") int id, @Body Animal dataModal);

    @DELETE("animals/")
    Call<Animal> deleteData(@Query("Id") int id);
*/
}
