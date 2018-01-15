package im.dacer.kata;

import android.content.Context;

import im.dacer.kata.core.action.BingSearchAction;
import im.dacer.kata.core.action.DuckDuckGoSearchAction;
import im.dacer.kata.core.action.GoogleSearchAction;
import im.dacer.kata.core.action.SearchAction;
import im.dacer.kata.core.data.AppPreference;

public class SearchEngine {

    public static final String GOOGLE = "Google";
    public static final String BING = "Bing";
    public static final String DUCKDUCKGO = "DuckDuckGo";

    public static SearchAction getSearchAction(Context context) {
        AppPreference appPreference = new AppPreference(context);
        switch (appPreference.getSearchEngine()) {
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
