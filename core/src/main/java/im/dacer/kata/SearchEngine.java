package im.dacer.kata;

import android.content.Context;

import im.dacer.kata.core.action.BingSearchAction;
import im.dacer.kata.core.action.DuckDuckGoSearchAction;
import im.dacer.kata.core.action.GoogleSearchAction;
import im.dacer.kata.core.action.JishoSearchAction;
import im.dacer.kata.core.action.SearchAction;
import im.dacer.kata.core.data.MultiprocessPref;

public class SearchEngine {

    public static final String GOOGLE = "Google";
    public static final String BING = "Bing";
    public static final String DUCKDUCKGO = "DuckDuckGo";
    public static final String JISHO = "Jisho";

    public static SearchAction getSearchAction(Context context) {
        MultiprocessPref multiprocessPref = new MultiprocessPref(context);
        switch (multiprocessPref.getSearchEngine()) {
            case GOOGLE:
                return GoogleSearchAction.create();
            case BING:
                return BingSearchAction.create();
            case DUCKDUCKGO:
                return DuckDuckGoSearchAction.create();
            case JISHO:
                return JishoSearchAction.create();
        }
        return null;
    }

    public static String[] getSupportSearchEngineList() {
        return new String[]{GOOGLE, BING, DUCKDUCKGO, JISHO};
    }
}
