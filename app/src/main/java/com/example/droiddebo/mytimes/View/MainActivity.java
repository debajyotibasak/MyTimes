package com.example.droiddebo.mytimes.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.droiddebo.mytimes.Adapter.DataAdapter;
import com.example.droiddebo.mytimes.Model.Article;
import com.example.droiddebo.mytimes.Network.ApiClient;
import com.example.droiddebo.mytimes.Network.ApiInterface;
import com.example.droiddebo.mytimes.Network.JSONResponse;
import com.example.droiddebo.mytimes.R;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String SOURCE = "the-times-of-india";
    private final static String SORT_BY = "top";
    private final static String API_KEY = "7ab0b19b6b2142bd8dd2e0ab78258be9";

    private RecyclerView recyclerView;
    private ArrayList<Article> articles;
    private DataAdapter adapter;
//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadJSON();
    }


    private void loadJSON() {

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from newsapi.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

        Call<JSONResponse> call = request.getJSON(SOURCE, SORT_BY, API_KEY);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(@NonNull Call<JSONResponse> call, @NonNull Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                articles = new ArrayList<>(Arrays.asList(jsonResponse.getArticle()));
                adapter = new DataAdapter(getApplicationContext(), articles);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<JSONResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
