package com.example.droiddebo.mytimes.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        /*
        ** Custom Toolbar ( App Bar )
        **/
        createToolbar();

        /*
        ** Action of the Floating Action Button ( FAB )
        **/
        floatingButton();

        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        Typeface montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");

        /*
        ** We get the response data from the Main Activity as Intents
        **/
        receiveFromDataAdapter(montserrat_regular, montserrat_semiBold);

        buttonLinktoFullArticle(montserrat_regular);
    }

    private void buttonLinktoFullArticle(Typeface montserrat_regular) {
        Button linkToFullArticle = (Button) findViewById(R.id.article_button);
        linkToFullArticle.setTypeface(montserrat_regular);
        linkToFullArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            }
        });
    }

    private void receiveFromDataAdapter(Typeface montserrat_regular, Typeface montserrat_semiBold) {
        String headLine = getIntent().getStringExtra("key_HeadLine");
        String author = getIntent().getStringExtra("key_author");
        String description = getIntent().getStringExtra("key_description");
        String date = getIntent().getStringExtra("key_date");
        String imgURL = getIntent().getStringExtra("key_imgURL");
        URL = getIntent().getStringExtra("key_URL");

        TextView content_Headline = (TextView) findViewById(R.id.content_Headline);
        content_Headline.setText(headLine);
        content_Headline.setTypeface(montserrat_semiBold);

        TextView content_Description = (TextView) findViewById(R.id.content_Description);
        content_Description.setText(description);
        content_Description.setTypeface(montserrat_regular);

        ImageView collapsingImage = (ImageView) findViewById(R.id.collapsingImage);
        Glide.with(this)
                .load(imgURL)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(collapsingImage);
    }

    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // For not showing the title of the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void floatingButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this news! Send from MyTimes App\n" +
                        Uri.parse(URL));

                startActivity(Intent.createChooser(shareIntent, "Share with"));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*
            * Load the URL to the news when pressing the URL button
            * */
//            case R.id.action_url:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
//                startActivity(browserIntent);

            /*
            * Override the Up/Home Button
            * */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
