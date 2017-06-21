package com.example.droiddebo.mytimes.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.TextView;
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
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

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
    private static final String LIST_STATE_KEY = "recycler_list_state";
    private String SAVE_TEXT = "save_text";
    /*
     ** These 3 strings are very important as they are required for querying the json before parsing.
     **/
    private String[] SOURCE_ARRAY = {"bbc-news", "the-hindu", "the-times-of-india", "mtv-news", "bbc-sport",
            "espn-cric-info", "talksport", "the-verge", "techcrunch", "techradar"};
    private String SOURCE;
    private String SORT_BY = "top";
    private final static String API_KEY = "7ab0b19b6b2142bd8dd2e0ab78258be9";

    private List<Article> articles = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private Typeface montserrat_regular;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        createToolbar();
        createRecyclerView();

        /*
        ** show loader and fetch messages.
        **/
        SOURCE = SOURCE_ARRAY[0];
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar, montserrat_regular);

    }

    private void createToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setTypeface(montserrat_regular);
    }

    private void createRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

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

    }

    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar, Typeface montserrat_regular) {
        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
                .withTypeface(montserrat_regular).withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("BBC News")
                .withIcon(R.drawable.ic_bbcnews).withTypeface(montserrat_regular);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu).withTypeface(montserrat_regular);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserrat_regular);
        SectionDrawerItem item4 = new SectionDrawerItem().withIdentifier(4).withName("ENTERTAINMENT")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews).withTypeface(montserrat_regular);
        SectionDrawerItem item6 = new SectionDrawerItem().withIdentifier(6).withName("SPORTS")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports).withTypeface(montserrat_regular);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("ESPN Cric Info")
                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserrat_regular);
        PrimaryDrawerItem item9= new PrimaryDrawerItem().withIdentifier(9).withName("TalkSport")
                .withIcon(R.drawable.ic_talksport).withTypeface(montserrat_regular);
        SectionDrawerItem item10 = new SectionDrawerItem().withIdentifier(10).withName("TECHNOLOGY")
                .withTypeface(montserrat_regular);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("The Verge")
                .withIcon(R.drawable.ic_theverge).withTypeface(montserrat_regular);
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(12).withName("TechCrunch")
                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserrat_regular);
        PrimaryDrawerItem item13= new PrimaryDrawerItem().withIdentifier(13).withName("TechRadar")
                .withIcon(R.drawable.ic_techradar).withTypeface(montserrat_regular);
        SectionDrawerItem item14 = new SectionDrawerItem().withIdentifier(14).withName("MORE INFO")
                .withTypeface(montserrat_regular);
        SecondaryDrawerItem item15 = new SecondaryDrawerItem().withIdentifier(15).withName("About the app")
                .withIcon(R.drawable.ic_info).withTypeface(montserrat_regular);
        SecondaryDrawerItem item16 = new SecondaryDrawerItem().withIdentifier(16).withName("Open Source")
                .withIcon(R.drawable.ic_code).withTypeface(montserrat_regular);
        SecondaryDrawerItem item17 = new SecondaryDrawerItem().withIdentifier(17).withName("Powered by newsapi.org")
                .withIcon(R.drawable.ic_power).withTypeface(montserrat_regular);
        SecondaryDrawerItem item18 = new SecondaryDrawerItem().withIdentifier(18).withName("Contact us")
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
                .addDrawerItems(item0,item1,item2,item3,item4,item5,item6,item7,item8,item9,
                        item10,item11,item12,item13,item14,item15,item16,item17,item18)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case 1: SOURCE = SOURCE_ARRAY[0]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 2: SOURCE = SOURCE_ARRAY[1]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 3: SOURCE = SOURCE_ARRAY[2]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 5: SOURCE = SOURCE_ARRAY[3]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 7: SOURCE = SOURCE_ARRAY[4]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 8: SOURCE = SOURCE_ARRAY[5]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 9: SOURCE = SOURCE_ARRAY[6]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 11: SOURCE = SOURCE_ARRAY[7]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 12: SOURCE = SOURCE_ARRAY[8]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 13: SOURCE = SOURCE_ARRAY[9]; onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 15: openAboutActivity(); break;
                            case 16: Intent browserSource = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/debo1994/MyTimes"));
                                    startActivity(browserSource);
                                    break;
                            case 17: Intent browserAPI = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://newsapi.org/"));
                                    startActivity(browserAPI);
                                    break;
                            case 18: sendEmail(); break;
                            default: break;
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
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(LIST_STATE_KEY, listState);
        bundle.putString("SOURCE", SOURCE);
//        bundle.putString("TOOLBAR", toolbar.getTitle().toString());
        bundle.putString(SAVE_TEXT, mTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            SOURCE = savedInstanceState.getString("SOURCE");
            createToolbar();
//            toolbar.setTitle(savedInstanceState.getString("TOOLBAR"));
            mTitle.setText(savedInstanceState.getString(SAVE_TEXT));
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            createDrawer(savedInstanceState, toolbar, montserrat_regular);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listState!=null){
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
