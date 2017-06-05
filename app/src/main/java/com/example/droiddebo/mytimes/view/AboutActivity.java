package com.example.droiddebo.mytimes.view;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droiddebo.mytimes.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        /*
        ** Custom Toolbar ( App Bar )
        **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // For not showing the title of the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        ** Action of the Floating Action Button ( FAB )
        **/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_about);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        /*
        ** Customising animations of the AppBar Layout
        **/
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_about);
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
                } else if (isShow) {
                    isShow = false;
                }
            }
        });

        AssetManager assetManager = this.getApplicationContext().getAssets();
        Typeface montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        Typeface montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");

        TextView aboutHeaderAppName = (TextView) findViewById(R.id.about_header_app_name);
        aboutHeaderAppName.setTypeface(montserrat_semiBold);

        TextView aboutHeaderAppDescription = (TextView) findViewById(R.id.about_header_app_description);
        aboutHeaderAppDescription.setTypeface(montserrat_regular);

        TextView cardInfo = (TextView) findViewById(R.id.tv_card_info);
        cardInfo.setTypeface(montserrat_regular);

        TextView madeWithLove = (TextView) findViewById(R.id.tv_made_with_love);
        madeWithLove.setTypeface(montserrat_regular);

        TextView librariesUsed = (TextView) findViewById(R.id.tv_libraries_used);
        librariesUsed.setTypeface(montserrat_regular);

        TextView info1 = (TextView) findViewById(R.id.tv_info1);
        info1.setTypeface(montserrat_semiBold);

        TextView author1 = (TextView) findViewById(R.id.tv_author1);
        author1.setTypeface(montserrat_regular);

        TextView info2 = (TextView) findViewById(R.id.tv_info2);
        info2.setTypeface(montserrat_semiBold);

        TextView author2 = (TextView) findViewById(R.id.tv_author2);
        author2.setTypeface(montserrat_regular);

        TextView info3 = (TextView) findViewById(R.id.tv_info3);
        info3.setTypeface(montserrat_semiBold);

        TextView author3 = (TextView) findViewById(R.id.tv_author3);
        author3.setTypeface(montserrat_regular);

        TextView info4 = (TextView) findViewById(R.id.tv_info4);
        info4.setTypeface(montserrat_semiBold);

        TextView author4 = (TextView) findViewById(R.id.tv_author4);
        author4.setTypeface(montserrat_regular);

        CardView cardViewInfo = (CardView) findViewById(R.id.card_info);
        cardViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/debo1994"));
                startActivity(browserIntent);
            }
        });

        CardView cardViewLibrary1 = (CardView) findViewById(R.id.cardView1);
        cardViewLibrary1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mikepenz/MaterialDrawer"));
                startActivity(browserIntent);
            }
        });

        CardView cardViewLibrary2 = (CardView) findViewById(R.id.cardView2);
        cardViewLibrary2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/square/retrofit"));
                startActivity(browserIntent);
            }
        });

        CardView cardViewLibrary3 = (CardView) findViewById(R.id.cardView3);
        cardViewLibrary3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/square/okhttp"));
                startActivity(browserIntent);
            }
        });

        CardView cardViewLibrary4 = (CardView) findViewById(R.id.cardView4);
        cardViewLibrary4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bumptech/glide"));
                startActivity(browserIntent);
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
            Toast.makeText(AboutActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
