package com.example.droiddebo.mytimes.View;

import android.content.Context;
import android.content.Intent;
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

public class ArticleActivity extends AppCompatActivity {

    private Menu menu;
    private Context mContext;

    private String headLine;
    private String author;
    private String description;
    private String date;
    private String imgURL;
    private String URL;

    private static final String TAG = ArticleActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            }
        });

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

        headLine = getIntent().getStringExtra("key_HeadLine");
        author = getIntent().getStringExtra("key_author");
        description = getIntent().getStringExtra("key_description");
        date = getIntent().getStringExtra("key_date");
        imgURL = getIntent().getStringExtra("key_imgURL");
        URL = getIntent().getStringExtra("key_URL");

        TextView content_Headline = (TextView) findViewById(R.id.content_Headline);
        content_Headline.setText(headLine.replace("- Times of India", ""));

        TextView content_Date = (TextView) findViewById(R.id.content_Date);
        content_Date.setText("DATE: " + date.substring(0,10));

        TextView content_Description = (TextView) findViewById(R.id.content_Description);
        content_Description.setText(description);

        TextView content_Author = (TextView) findViewById(R.id.content_Author);
        content_Author.setText("AUTHOR: " + author);

        TextView content_Source = (TextView) findViewById(R.id.content_source);
        content_Source.setText("SOURCE: Times of India");

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
        hideOption(R.id.action_url);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_url) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            startActivity(browserIntent);
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
}
