package com.news.droiddebo.mytimes.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.news.droiddebo.mytimes.R;
import com.news.droiddebo.mytimes.model.Constants;

import java.util.Objects;

/**
 * Article Activity is loaded when the user presses on one of the card views of the Recycler View
 * which is present in the Main Activity.
 */
public class ArticleActivity extends AppCompatActivity {

    private String url;
    private Typeface montserratRegular;
    private Typeface montserratSemibold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        createToolbar();
        floatingButton();
        assetManager();
        receiveFromDataAdapter(montserratRegular, montserratSemibold);
        buttonLinktoFullArticle(montserratRegular);
    }

    private void assetManager() {
        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        montserratSemibold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");
    }

    private void buttonLinktoFullArticle(Typeface montserratRegular) {
        Button linkToFullArticle = findViewById(R.id.article_button);
        linkToFullArticle.setTypeface(montserratRegular);
        linkToFullArticle.setOnClickListener(v -> openWebViewActivity());
    }

    private void openWebViewActivity() {
        Intent browserIntent = new Intent(ArticleActivity.this, WebViewActivity.class);
        browserIntent.putExtra(Constants.INTENT_URL, url);
        startActivity(browserIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void receiveFromDataAdapter(Typeface montserratRegular, Typeface montserratSemiBold) {
        String headLine = getIntent().getStringExtra(Constants.INTENT_HEADLINE);
        String description = getIntent().getStringExtra(Constants.INTENT_DESCRIPTION);
        String imgURL = getIntent().getStringExtra(Constants.INTENT_IMG_URL);
        url = getIntent().getStringExtra(Constants.INTENT_ARTICLE_URL);

        TextView contentHeadline = findViewById(R.id.content_Headline);
        contentHeadline.setText(headLine);
        contentHeadline.setTypeface(montserratSemiBold);

        TextView contentDescription = findViewById(R.id.content_Description);
        contentDescription.setText(description);
        contentDescription.setTypeface(montserratRegular);

        ImageView collapsingImage = findViewById(R.id.collapsingImage);

        Glide.with(this)
                .load(imgURL)
                .apply(new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(collapsingImage);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this news! Send from MyTimes App\n" + Uri.parse(url));
            startActivity(Intent.createChooser(shareIntent, "Share with"));
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Override the Up/Home Button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
