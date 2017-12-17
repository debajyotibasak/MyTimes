package com.news.droiddebo.mytimes.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.news.droiddebo.mytimes.R;

public class AboutActivity extends AppCompatActivity {

    private Typeface montserrat_regular;
    private Typeface montserrat_semiBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*
        ** Custom Toolbar ( App Bar )
        **/
        createToolbar();

        /*
        ** Action of the Floating Action Button ( FAB )
        **/
        floatingButton();

        /*
        ** Asset manager
        **/
        assetManager();

        createInfoTextView();
        createLibraryCardViews();

    }

    private void assetManager() {
        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");
    }

    private void createInfoTextView() {
        TextView aboutHeaderAppName = findViewById(R.id.about_header_app_name);
        TextView aboutHeaderAppDescription = findViewById(R.id.about_header_app_description);
        TextView cardInfo = findViewById(R.id.tv_card_info);
        TextView madeWithLove = findViewById(R.id.tv_made_with_love);
        TextView librariesUsed = findViewById(R.id.tv_libraries_used);
        TextView info1 = findViewById(R.id.tv_info1);
        TextView author1 = findViewById(R.id.tv_author1);
        TextView info2 = findViewById(R.id.tv_info2);
        TextView author2 = findViewById(R.id.tv_author2);
        TextView info3 = findViewById(R.id.tv_info3);
        TextView author3 = findViewById(R.id.tv_author3);
        TextView info4 = findViewById(R.id.tv_info4);
        TextView author4 = findViewById(R.id.tv_author4);

        aboutHeaderAppName.setTypeface(montserrat_semiBold);
        aboutHeaderAppDescription.setTypeface(montserrat_regular);
        cardInfo.setTypeface(montserrat_regular);
        madeWithLove.setTypeface(montserrat_regular);
        librariesUsed.setTypeface(montserrat_regular);
        info1.setTypeface(montserrat_semiBold);
        author1.setTypeface(montserrat_regular);
        info2.setTypeface(montserrat_semiBold);
        author2.setTypeface(montserrat_regular);
        info3.setTypeface(montserrat_semiBold);
        author3.setTypeface(montserrat_regular);
        info4.setTypeface(montserrat_semiBold);
        author4.setTypeface(montserrat_regular);
    }

    private void createLibraryCardViews() {
        CardView cardViewInfo = findViewById(R.id.card_info);
        CardView cardViewLibrary1 = findViewById(R.id.cardView1);
        CardView cardViewLibrary2 = findViewById(R.id.cardView2);
        CardView cardViewLibrary3 = findViewById(R.id.cardView3);
        CardView cardViewLibrary4 = findViewById(R.id.cardView4);


        cardViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/debo1994"));
                startActivity(browserIntent);
            }
        });

        cardViewLibrary1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mikepenz/MaterialDrawer"));
                startActivity(browserIntent);
            }
        });

        cardViewLibrary2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/square/retrofit"));
                startActivity(browserIntent);
            }
        });

        cardViewLibrary3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/square/okhttp"));
                startActivity(browserIntent);
            }
        });

        cardViewLibrary4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bumptech/glide"));
                startActivity(browserIntent);
            }
        });
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab_about);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    private void createToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_layout_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbarTitle = findViewById(R.id.toolbar_title_about);

        /*
        ** Customising animations of the AppBar Layout
        **/
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_about);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                    toolbarTitle.setTypeface(montserrat_regular);
                    toolbarTitle.setText("About");
                    isShow = true;
                } else if (isShow) {
                    toolbarTitle.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*
            * Override the Up/Home Button
            * */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: d.basak.db@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
