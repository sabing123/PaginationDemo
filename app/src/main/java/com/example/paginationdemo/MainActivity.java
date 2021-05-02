package com.example.paginationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<MainData> dataArrayList = new ArrayList<>();
    MainAdapter adapter;


    int page = 1,limit=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress_bar);


        //Initialize apdater


        adapter = new MainAdapter(MainActivity.this, dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        //create get data method;

        getData(page,limit);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //check codition
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    //when reach last item position

                    //increased page size
                    page++;

                    progressBar.setVisibility(View.VISIBLE);
//                    call Method
                    getData(page,limit);
                }
            }
        });



    }

    private void getData(int page, int limit) {
        //initialize retrofit;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


//        create main interface

        MainInterface mainInterface = retrofit.create(MainInterface.class);

        Call<String> call = mainInterface.String_Call(page, limit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()&& response.body() != null){
//                    when response is success
                    progressBar.setVisibility(View.GONE);
                    try {

                        JSONArray jsonArray = new JSONArray(response.body());
                        parseResult(jsonArray);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void parseResult(JSONArray jsonArray) {
        // use for loop
        for (int i=0; i<jsonArray.length(); i++)
        {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                //initialize
                MainData data = new MainData();
//                set Image
                data.setImage(object.getString("download_url"));
//                set name
                data.setName(object.getString("author"));
                //add data in array list
                dataArrayList.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new MainAdapter(MainActivity.this, dataArrayList);
//            set adapter

            recyclerView.setAdapter(adapter);



        }
    }
}