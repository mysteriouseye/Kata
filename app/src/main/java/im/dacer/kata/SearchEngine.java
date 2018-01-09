/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata;

import android.content.Context;

import im.dacer.kata.core.action.BingSearchAction;
import im.dacer.kata.core.action.GoogleSearchAction;
import im.dacer.kata.core.action.SearchAction;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class SearchEngine {

    public static final String BING = "Bing";
    public static final String GOOGLE = "Google";

    public static SearchAction getSearchAction(Context context) {
        Config config = Treasure.get(context, Config.class);
        switch (config.getSearchEngine()) {
            case BING:
                return BingSearchAction.create();
            case GOOGLE:
                return GoogleSearchAction.create();
        }
        return null;
    }

    public static String[] getSupportSearchEngineList() {
        return new String[]{BING, GOOGLE};
    }
}
