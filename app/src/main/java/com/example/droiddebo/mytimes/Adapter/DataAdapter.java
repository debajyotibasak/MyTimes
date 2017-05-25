package com.example.droiddebo.mytimes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.droiddebo.mytimes.Model.Article;
import com.example.droiddebo.mytimes.R;
import com.example.droiddebo.mytimes.View.ArticleActivity;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    private static String TAG = "DATA";

    private List<Article> articles;
    private Context mContext;
    int lastPosition = -1;


    public DataAdapter(Context mContext, List<Article> articles) {
        this.mContext = mContext;
        this.articles = articles;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        final Article article = articles.get(position);

        holder.tv_card_main_title.setText(article.getTitle().replace("- Times of India", ""));

        Glide.with(mContext)
                .load(article.getUrlToImage())
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .crossFade()
                .into(holder.img_card_main);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headLine = article.getTitle();
                String author = article.getAuthor();
                String description = article.getDescription();
                String date = article.getPublishedAt();
                String imgURL = article.getUrlToImage();
                String URL = article.getUrl();

                Intent intent = new Intent(mContext, ArticleActivity.class);

                intent.putExtra("key_HeadLine", headLine);
                intent.putExtra("key_author", author);
                intent.putExtra("key_description", description);
                intent.putExtra("key_date", date);
                intent.putExtra("key_imgURL", imgURL);
                intent.putExtra("key_URL", URL);

                mContext.startActivity(intent);
            }
        });

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_main_title,tv_card_source;
        private ImageView img_card_main;
        private CardView cardView;

        AssetManager assetManager = mContext.getApplicationContext().getAssets();
        Typeface roboto_slab_regular = Typeface.createFromAsset(assetManager, "fonts/RobotoSlab-Regular.ttf");

        public ViewHolder(View view) {
            super(view);

            tv_card_main_title = (TextView) view.findViewById(R.id.tv_card_main_title);
            tv_card_main_title.setTypeface(roboto_slab_regular);
            img_card_main = (ImageView) view.findViewById(R.id.img_card_main);
            cardView = (CardView) view.findViewById(R.id.card_row);
        }
    }
}
