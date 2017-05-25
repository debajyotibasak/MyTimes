package com.example.droiddebo.mytimes.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.droiddebo.mytimes.Adapter.DataAdapter;
import com.example.droiddebo.mytimes.Model.Article;
import com.example.droiddebo.mytimes.Model.ArticleResponse;
import com.example.droiddebo.mytimes.Network.ApiClient;
import com.example.droiddebo.mytimes.Network.ApiInterface;
import com.example.droiddebo.mytimes.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String SOURCE = "the-times-of-india";
    private final static String SORT_BY = "top";
    private final static String API_KEY = "7ab0b19b6b2142bd8dd2e0ab78258be9";

    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwitchCompat switchCompat;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

//        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter(this, articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }

    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

//        if (API_KEY.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from newsapi.org first!", Toast.LENGTH_LONG).show();
//            return;
//        }

        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

        Call<ArticleResponse> call = request.getCall(SOURCE, SORT_BY, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {

                articles.clear();
                articles.addAll(response.body().getArticles());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem itemSwitch = menu.findItem(R.id.action_switchMenu);
        itemSwitch.setActionView(R.layout.switch_layout);

        switchCompat = (SwitchCompat) menu.findItem(R.id.action_switchMenu).getActionView().findViewById(R.id.switchLayout);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplication(), "ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_switchMenu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
