package com.example.droiddebo.mytimes.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.droiddebo.mytimes.MyTimesApplication;
import com.example.droiddebo.mytimes.R;
import com.example.droiddebo.mytimes.adapter.DataAdapter;
import com.example.droiddebo.mytimes.model.Article;
import com.example.droiddebo.mytimes.model.ArticleResponse;
import com.example.droiddebo.mytimes.network.ApiClient;
import com.example.droiddebo.mytimes.network.ApiInterface;
import com.example.droiddebo.mytimes.network.interceptors.OfflineResponseCacheInterceptor;
import com.example.droiddebo.mytimes.network.interceptors.ResponseCacheInterceptor;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     ** These 3 strings are very important as they are required for querying the json before parsing.
     **/
    private String[] SOURCE_ARRAY = {"the-times-of-india","mtv-news","espn-cric-info","the-hindu"};
    private String SOURCE;
    private String[] SORT_BY_VALUES = {"top","latest"};
    private String SORT_BY;
    private final static String API_KEY = "7ab0b19b6b2142bd8dd2e0ab78258be9";

    private List<Article> articles = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        /*
        ** SwipeRefreshLayout is used for reloading the JSON by pulling the refresh button from top
        ** and refreshing the Layout with new JSON responses.
        **/
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        /*
        ** Adapter is the place where the articles are loaded into.
        **/
        adapter = new DataAdapter(this, articles);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        /*
        ** show loader and fetch messages.
        **/
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        SOURCE = SOURCE_ARRAY[0];
                        loadJSON();
                    }
                }
        );

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("The Times of India");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("MTV News");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("ESPN Cric Info");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("The Hindu");

        AccountHeader accountheader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background_acc_header)
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(accountheader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item1,item2,item3,item4)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem.getIdentifier()==1){
                            SOURCE = SOURCE_ARRAY[0];
                            swipeRefreshLayout.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadJSON();
                                        }
                                    }
                            );
                        }
                        else if(drawerItem.getIdentifier()==2){
                            SOURCE = SOURCE_ARRAY[1];
                            swipeRefreshLayout.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadJSON();
                                        }
                                    }
                            );
                        }
                        else if(drawerItem.getIdentifier()==3){
                            SOURCE = SOURCE_ARRAY[2];
                            swipeRefreshLayout.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadJSON();
                                        }
                                    }
                            );
                        }
                        else if(drawerItem.getIdentifier()==4){
                            SOURCE = SOURCE_ARRAY[3];
                            swipeRefreshLayout.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadJSON();
                                        }
                                    }
                            );
                        }
                    return false;
                    }
                })
                .build();


        Spinner spinner = (Spinner) findViewById(R.id.spinner_toolbar);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_list_item,
                getResources().getStringArray(R.array.spinner));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                if(position == 0){
                    SORT_BY = SORT_BY_VALUES[0];
                    swipeRefreshLayout.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    loadJSON();
                                }
                            }
                    );
                }
                else
                {
                    SORT_BY = SORT_BY_VALUES[1];
                    swipeRefreshLayout.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    loadJSON();
                                }
                            }
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
                SORT_BY = SORT_BY_VALUES[0];
                swipeRefreshLayout.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                loadJSON();
                            }
                        }
                );
            }
        });
    }

    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        /*
        ** Used to show Log files of the HTTP GET REQUESTS and what is fetched
        ** and the status codes and the body of the requests etc.
        **/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        /*
        ** OkHttp is added as a default in Retrofit so it is added here for adding the
        ** different interceptors which handles the offline caching etc.
        **/
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add your other interceptors …
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
        httpClient.cache(new Cache(new File(MyTimesApplication.getMyTimesApplicationInstance()
                .getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        /*
        ** Calls the Retrofit client (ApiClient) and passes the OkHTTP client
        ** (httpCLient declared above) and creates the call with the help of ApiInterface.
        **/
        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);


        Call<ArticleResponse> call = request.getCall(SOURCE, SORT_BY, API_KEY);
        call.enqueue(new Callback<ArticleResponse>() {
            /*
            ** The response is build with the Call and the Response while using the Article Response
             * POJO class to construct the responses .
            **/
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {

                if (response.isSuccessful()) {
                    articles.clear();
                    articles.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /*
    ** Loads the JSON when Refreshing the swipeRefreshLayout
    **/
    @Override
    public void onRefresh() {
        loadJSON();
    }
    /*
    ** TODO: APP INDEXING(App is not indexable by Google Search; consider adding at least one Activity with an ACTION-VIEW) .
    ** TODO: ADDING ATTRIBUTE android:fullBackupContent
    **/
}
