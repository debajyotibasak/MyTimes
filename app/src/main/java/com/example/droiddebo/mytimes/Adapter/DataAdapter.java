package com.example.droiddebo.mytimes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.droiddebo.mytimes.Model.Article;
import com.example.droiddebo.mytimes.R;
import com.example.droiddebo.mytimes.View.ArticleActivity;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    private List<Article> articles;
    private Context mContext;

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
        Article article = articles.get(position);

        holder.tv_card_main_title.setText(article.getTitle());
        Glide.with(mContext)
                .load(article.getUrlToImage())
                .thumbnail(Glide.with(mContext).load(R.raw.ic_loading))
                .centerCrop()
                .error(R.drawable.ic_image)
                .crossFade()
                .into(holder.img_card_main);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchArticleActivity();
            }
        });
    }

    private void launchArticleActivity() {
        Intent intent = new Intent(mContext, ArticleActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_main_title;
        private ImageView img_card_main;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);

            tv_card_main_title = (TextView) view.findViewById(R.id.tv_card_main_title);
            img_card_main = (ImageView) view.findViewById(R.id.img_card_main);
            cardView = (CardView) view.findViewById(R.id.card_row);
        }
    }
}
