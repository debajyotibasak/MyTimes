package com.example.droiddebo.mytimes.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.droiddebo.mytimes.R;

/*
** Article Activity is loaded when the user presses on one of the card views of the Recycler View
 * which is present in the Main Activity.
**/

public class ArticleActivity extends AppCompatActivity {

    private Menu menu;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        /*
        ** Custom Toolbar ( App Bar )
        **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // For not showing the title of the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        ** Action of the Floating Action Button ( FAB )
        **/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            }
        });

        /*
        ** Customising animations of the AppBar Layout
        **/
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption(R.id.action_url);
                } else if (isShow) {
                    isShow = false;
                    hideOption(R.id.action_url);
                }
            }
        });


        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        Typeface montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");

        /*
        ** We get the response data from the Main Activity as Intents
        **/
        String headLine = getIntent().getStringExtra("key_HeadLine");
        String author = getIntent().getStringExtra("key_author");
        String description = getIntent().getStringExtra("key_description");
        String date = getIntent().getStringExtra("key_date");
        String imgURL = getIntent().getStringExtra("key_imgURL");
        URL = getIntent().getStringExtra("key_URL");

        TextView content_Headline = (TextView) findViewById(R.id.content_Headline);
        content_Headline.setText(headLine.replace("- Times of India", ""));
        content_Headline.setTypeface(montserrat_semiBold);

//        TextView content_Date = (TextView) findViewById(R.id.content_Date);
//        content_Date.setText(getString(R.string.article_activity_date) + " " + date);
//        content_Date.setTypeface(montserrat_regular);

        TextView content_Description = (TextView) findViewById(R.id.content_Description);
        content_Description.setText(description);
        content_Description.setTypeface(montserrat_regular);

        TextView content_Author = (TextView) findViewById(R.id.content_Author);
        content_Author.setText(getString(R.string.article_activity_author) + " " + author);
        content_Author.setTypeface(montserrat_semiBold);

//        TextView content_Source = (TextView) findViewById(R.id.content_source);
//        content_Source.setText(R.string.article_activity_source);
//        content_Source.setTypeface(montserrat_semiBold);

        ImageView collapsingImage = (ImageView) findViewById(R.id.collapsingImage);
        Glide.with(this)
                .load(imgURL)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(collapsingImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_article, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*
            * Load the URL to the news when pressing the URL button
            * */
            case R.id.action_url:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);

            /*
            * Override the Up/Home Button
            * */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
