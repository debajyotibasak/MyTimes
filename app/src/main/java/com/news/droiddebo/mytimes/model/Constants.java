package com.news.droiddebo.mytimes.model;

import com.news.droiddebo.mytimes.BuildConfig;

public class Constants {

    //Old api version
    public static final String BASE_URL_OLD = "https://newsapi.org/v1/";

    //New api version
    public static final String BASE_URL = "https://newsapi.org/v2/";

    //API Key
    public static final String API_KEY = BuildConfig.NEWS_API_KEY;

    //Used to save the instance of the recyclerView
    public static final String RECYCLER_STATE_KEY = "recycler_list_state";

    //Used to save the instance of the title of Toolbar.
    public static final String TITLE_STATE_KEY = "title_state";

    //Used to save the instance of the selected source.
    public static final String SOURCE = "source";

    //Used to save the instance of the title of Toolbar.
    public static final String TITLE_WEBVIEW_KEY = "save_text_webView";

    //Used by intents
    public static final String INTENT_URL = "url";

    public static final String INTENT_HEADLINE = "key_HeadLine";
    public static final String INTENT_DESCRIPTION = "key_description";
    public static final String INTENT_DATE = "key_date";
    public static final String INTENT_IMG_URL = "key_imgURL";
    public static final String INTENT_ARTICLE_URL = "key_URL";

    public static final String TRANSITION = "transition";
}
