package com.example.droiddebo.mytimes.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.droiddebo.mytimes.MyTimesApplication;
import com.example.droiddebo.mytimes.R;
import com.example.droiddebo.mytimes.adapter.DataAdapter;
import com.example.droiddebo.mytimes.model.Article;
import com.example.droiddebo.mytimes.model.ArticleResponse;
import com.example.droiddebo.mytimes.network.ApiClient;
import com.example.droiddebo.mytimes.network.ApiInterface;
import com.example.droiddebo.mytimes.network.interceptors.OfflineResponseCacheInterceptor;
import com.example.droiddebo.mytimes.network.interceptors.ResponseCacheInterceptor;
import com.example.droiddebo.mytimes.util.UtilityMethods;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
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
    private String[] SOURCE_ARRAY = {"the-times-of-india", "mtv-news", "espn-cric-info", "the-hindu"};
    private String SOURCE;
    private String[] SORT_BY_VALUES = {"top", "latest"};
    private String SORT_BY;
    private final static String API_KEY = "7ab0b19b6b2142bd8dd2e0ab78258be9";

    private List<Article> articles = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;

    private Typeface montserrat_regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {

            SOURCE = savedInstanceState.getString("SOURCE");
            SORT_BY = savedInstanceState.getString("SORT_BY");

            createToolbar();
            createRecyclerView();
            createSpinner();
            createDrawer(savedInstanceState, toolbar, montserrat_regular);
            return;
        }

        createToolbar();

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        createRecyclerView();

        /*
        ** show loader and fetch messages.
        **/
        SOURCE = SOURCE_ARRAY[0];
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar, montserrat_regular);

        createSpinner();

    }

    private void createToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void createRecyclerView() {
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
        recyclerView.getLayoutManager().onSaveInstanceState();
    }

    public void createSpinner() {
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
                if (position == 0) {
                    SORT_BY = SORT_BY_VALUES[0];
                    onLoadingSwipeRefreshLayout();
                } else {
                    SORT_BY = SORT_BY_VALUES[1];
                    onLoadingSwipeRefreshLayout();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
                SORT_BY = SORT_BY_VALUES[0];
                onLoadingSwipeRefreshLayout();
            }
        });

//        if (this.myBundle != null){
//            spinner.setSelection(myBundle.getInt("spinner", 0));
//        }
    }

    private void createDrawer(Bundle savedInstanceState, Toolbar toolbar, Typeface montserrat_regular) {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserrat_regular);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("ESPN Cric Info")
                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserrat_regular);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu).withTypeface(montserrat_regular);
        SectionDrawerItem item5 = new SectionDrawerItem().withIdentifier(5).withName("MORE INFO")
                .withTypeface(montserrat_regular);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName("About the app")
                .withIcon(R.drawable.ic_info).withTypeface(montserrat_regular);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(7).withName("Open Source")
                .withIcon(R.drawable.ic_code).withTypeface(montserrat_regular);
        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withIdentifier(8).withName("Powered by newsapi.org")
                .withIcon(R.drawable.ic_power).withTypeface(montserrat_regular);
        SecondaryDrawerItem item9 = new SecondaryDrawerItem().withIdentifier(9).withName("Contact us")
                .withIcon(R.drawable.ic_email).withTypeface(montserrat_regular);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_back)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item1, item2, item3, item4, item5, item6, item7, item8, item9)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            SOURCE = SOURCE_ARRAY[0];
                            onLoadingSwipeRefreshLayout();
                        } else if (drawerItem.getIdentifier() == 2) {
                            SOURCE = SOURCE_ARRAY[1];
                            onLoadingSwipeRefreshLayout();
                        } else if (drawerItem.getIdentifier() == 3) {
                            SOURCE = SOURCE_ARRAY[2];
                            onLoadingSwipeRefreshLayout();
                        } else if (drawerItem.getIdentifier() == 4) {
                            SOURCE = SOURCE_ARRAY[3];
                            onLoadingSwipeRefreshLayout();
                        } else if (drawerItem.getIdentifier() == 6) {
                            openAboutActivity();
                        } else if (drawerItem.getIdentifier() == 7) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/debo1994/MyTimes"));
                            startActivity(browserIntent);
                        } else if (drawerItem.getIdentifier() == 8) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                            startActivity(browserIntent);
                        } else if (drawerItem.getIdentifier() == 9) {
                            sendEmail();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

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
    private void onLoadingSwipeRefreshLayout() {
        if (!UtilityMethods.isNetworkAvailable()) {
            Toast.makeText(MainActivity.this,
                    "Could not load latest News. Please turn on the Internet.",
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        Menu menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*
            * Load the about menu
            * */
            case R.id.action_menu:
                openAboutActivity();

        }
        return super.onOptionsItemSelected(item);
    }

    public void openAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"d.basak.db@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Do you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        //add the values which need to be saved from the drawer to the bundle
        bundle = result.saveInstanceState(bundle);
        //add the values which need to be saved from the accountHeader to the bundle
        bundle = accountHeader.saveInstanceState(bundle);

        super.onSaveInstanceState(bundle);

        bundle.putString("SOURCE", SOURCE);
        bundle.putString("SORT_BY", SORT_BY);
    }
}
