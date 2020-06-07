package com.news.droiddebo.mytimes.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.news.droiddebo.mytimes.MyTimesApplication;
import com.news.droiddebo.mytimes.R;
import com.news.droiddebo.mytimes.adapter.DataAdapter;
import com.news.droiddebo.mytimes.model.ArticleStructure;
import com.news.droiddebo.mytimes.model.Constants;
import com.news.droiddebo.mytimes.model.NewsResponse;
import com.news.droiddebo.mytimes.network.ApiClient;
import com.news.droiddebo.mytimes.network.ApiInterface;
import com.news.droiddebo.mytimes.network.interceptors.OfflineResponseCacheInterceptor;
import com.news.droiddebo.mytimes.network.interceptors.ResponseCacheInterceptor;
import com.news.droiddebo.mytimes.util.UtilityMethods;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String[] SOURCE_ARRAY = {"google-news-in", "bbc-news", "the-hindu", "the-times-of-india",
            "buzzfeed", "mashable", "mtv-news", "bbc-sport", "espn-cric-info", "talksport", "medical-news-today",
            "national-geographic", "crypto-coins-news", "engadget", "the-next-web", "the-verge", "techcrunch", "techradar", "ign", "polygon"};
    private String source;

    private List<ArticleStructure> articleStructure = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private Typeface montserratRegular;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        createToolbar();
        createRecyclerView();

        source = SOURCE_ARRAY[0];
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar, montserratRegular);
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setTypeface(montserratRegular);
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar, Typeface montserratRegular) {
        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
                .withTypeface(montserratRegular).withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Google News India")
                .withIcon(R.drawable.ic_googlenews).withTypeface(montserratRegular);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
                .withIcon(R.drawable.ic_bbcnews).withTypeface(montserratRegular);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("The Hindu")
                .withIcon(R.drawable.ic_thehindu).withTypeface(montserratRegular);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("The Times of India")
                .withIcon(R.drawable.ic_timesofindia).withTypeface(montserratRegular);
        SectionDrawerItem item5 = new SectionDrawerItem().withIdentifier(5).withName("ENTERTAINMENT")
                .withTypeface(montserratRegular);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Buzzfeed")
                .withIcon(R.drawable.ic_buzzfeednews).withTypeface(montserratRegular);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Mashable")
                .withIcon(R.drawable.ic_mashablenews).withTypeface(montserratRegular);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews).withTypeface(montserratRegular);
        SectionDrawerItem item9 = new SectionDrawerItem().withIdentifier(9).withName("SPORTS")
                .withTypeface(montserratRegular);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports).withTypeface(montserratRegular);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("ESPN Cric Info")
                .withIcon(R.drawable.ic_espncricinfo).withTypeface(montserratRegular);
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(12).withName("TalkSport")
                .withIcon(R.drawable.ic_talksport).withTypeface(montserratRegular);
        SectionDrawerItem item13 = new SectionDrawerItem().withIdentifier(13).withName("SCIENCE")
                .withTypeface(montserratRegular);
        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(14).withName("Medical News Today")
                .withIcon(R.drawable.ic_medicalnewstoday).withTypeface(montserratRegular);
        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(15).withName("National Geographic")
                .withIcon(R.drawable.ic_nationalgeographic).withTypeface(montserratRegular);
        SectionDrawerItem item16 = new SectionDrawerItem().withIdentifier(16).withName("TECHNOLOGY")
                .withTypeface(montserratRegular);
        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(17).withName("Crypto Coins News")
                .withIcon(R.drawable.ic_ccnnews).withTypeface(montserratRegular);
        PrimaryDrawerItem item18 = new PrimaryDrawerItem().withIdentifier(18).withName("Engadget")
                .withIcon(R.drawable.ic_engadget).withTypeface(montserratRegular);
        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(19).withName("The Next Web")
                .withIcon(R.drawable.ic_thenextweb).withTypeface(montserratRegular);
        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(20).withName("The Verge")
                .withIcon(R.drawable.ic_theverge).withTypeface(montserratRegular);
        PrimaryDrawerItem item21 = new PrimaryDrawerItem().withIdentifier(21).withName("TechCrunch")
                .withIcon(R.drawable.ic_techcrunch).withTypeface(montserratRegular);
        PrimaryDrawerItem item22 = new PrimaryDrawerItem().withIdentifier(22).withName("TechRadar")
                .withIcon(R.drawable.ic_techradar).withTypeface(montserratRegular);
        SectionDrawerItem item23 = new SectionDrawerItem().withIdentifier(23).withName("GAMING")
                .withTypeface(montserratRegular);
        PrimaryDrawerItem item24 = new PrimaryDrawerItem().withIdentifier(24).withName("IGN")
                .withIcon(R.drawable.ic_ignnews).withTypeface(montserratRegular);
        PrimaryDrawerItem item25 = new PrimaryDrawerItem().withIdentifier(25).withName("Polygon")
                .withIcon(R.drawable.ic_polygonnews).withTypeface(montserratRegular);
        SectionDrawerItem item26 = new SectionDrawerItem().withIdentifier(26).withName("MORE INFO")
                .withTypeface(montserratRegular);
        SecondaryDrawerItem item27 = new SecondaryDrawerItem().withIdentifier(27).withName("About the app")
                .withIcon(R.drawable.ic_info).withTypeface(montserratRegular);
        SecondaryDrawerItem item28 = new SecondaryDrawerItem().withIdentifier(28).withName("Open Source")
                .withIcon(R.drawable.ic_code).withTypeface(montserratRegular);
        SecondaryDrawerItem item29 = new SecondaryDrawerItem().withIdentifier(29).withName("Powered by newsapi.org")
                .withIcon(R.drawable.ic_power).withTypeface(montserratRegular);
        SecondaryDrawerItem item30 = new SecondaryDrawerItem().withIdentifier(30).withName("Contact us")
                .withIcon(R.drawable.ic_mail).withTypeface(montserratRegular);

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
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10, item11, item12, item13, item14, item15, item16, item17, item18, item19,
                        item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    int selected = (int) drawerItem.getIdentifier();
                    switch (selected) {
                        case 1:
                            source = SOURCE_ARRAY[0];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 2:
                            source = SOURCE_ARRAY[1];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 3:
                            source = SOURCE_ARRAY[2];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 4:
                            source = SOURCE_ARRAY[3];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 6:
                            source = SOURCE_ARRAY[4];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 7:
                            source = SOURCE_ARRAY[5];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 8:
                            source = SOURCE_ARRAY[6];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 10:
                            source = SOURCE_ARRAY[7];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 11:
                            source = SOURCE_ARRAY[8];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 12:
                            source = SOURCE_ARRAY[9];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 14:
                            source = SOURCE_ARRAY[10];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 15:
                            source = SOURCE_ARRAY[11];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 17:
                            source = SOURCE_ARRAY[12];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 18:
                            source = SOURCE_ARRAY[13];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 19:
                            source = SOURCE_ARRAY[14];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 20:
                            source = SOURCE_ARRAY[15];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 21:
                            source = SOURCE_ARRAY[16];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 22:
                            source = SOURCE_ARRAY[17];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 24:
                            source = SOURCE_ARRAY[18];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 25:
                            source = SOURCE_ARRAY[19];
                            onLoadingSwipeRefreshLayout();
                            mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                            break;
                        case 27:
                            openAboutActivity();
                            break;
                        case 28:
                            Intent browserSource = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/debo1994/MyTimes"));
                            startActivity(browserSource);
                            break;
                        case 29:
                            Intent browserAPI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                            startActivity(browserAPI);
                            break;
                        case 30:
                            sendEmail();
                            break;
                        default:
                            break;
                    }
                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }


    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
        httpClient.cache(new Cache(new File(MyTimesApplication.getApplication().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        Call<NewsResponse> call = request.getHeadlines(source, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getArticles() != null) {
                    if (!articleStructure.isEmpty()) {
                        articleStructure.clear();
                    }

                    articleStructure = response.body().getArticles();

                    adapter = new DataAdapter(MainActivity.this, articleStructure);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }

    private void onLoadingSwipeRefreshLayout() {
        if (!UtilityMethods.isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Could not load latest News. Please turn on the Internet.", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.post(this::loadJSON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu:
                openAboutActivity();
                break;
            case R.id.action_search:
                openSearchActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void openSearchActivity() {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: d.basak.db@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }

    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle bundle) {
        //add the values which need to be saved from the drawer to the bundle
        bundle = result.saveInstanceState(bundle);
        //add the values which need to be saved from the accountHeader to the bundle
        bundle = accountHeader.saveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
        listState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState);
        bundle.putString(Constants.SOURCE, source);
        bundle.putString(Constants.TITLE_STATE_KEY, mTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        source = savedInstanceState.getString(Constants.SOURCE);
        createToolbar();
        mTitle.setText(savedInstanceState.getString(Constants.TITLE_STATE_KEY));
        listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY);
        createDrawer(savedInstanceState, toolbar, montserratRegular);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(listState);
        }
    }
}
