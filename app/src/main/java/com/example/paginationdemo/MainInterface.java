package com.example.paginationdemo;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {

    @GET("v2/list")
    Call<String> String_Call(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
