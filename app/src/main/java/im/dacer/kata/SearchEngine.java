/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata;

import android.content.Context;

import com.baoyz.treasure.Treasure;

import im.dacer.kata.core.action.BingSearchAction;
import im.dacer.kata.core.action.DuckDuckGoSearchAction;
import im.dacer.kata.core.action.GoogleSearchAction;
import im.dacer.kata.core.action.SearchAction;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class SearchEngine {

    public static final String GOOGLE = "Google";
    public static final String BING = "Bing";
    public static final String DUCKDUCKGO = "DuckDuckGo";

    public static SearchAction getSearchAction(Context context) {
        Config config = Treasure.get(context, Config.class);
        switch (config.getSearchEngine()) {
            case GOOGLE:
                return GoogleSearchAction.create();
            case BING:
                return BingSearchAction.create();
            case DUCKDUCKGO:
                return DuckDuckGoSearchAction.create();
        }
        return null;
    }

    public static String[] getSupportSearchEngineList() {
        return new String[]{GOOGLE, BING, DUCKDUCKGO};
    }
}
