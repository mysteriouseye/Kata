package im.dacer.kata.core;

import android.content.Context;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import im.dacer.kata.SearchEngine;
import im.dacer.kata.core.action.Action;
import im.dacer.kata.core.action.CopyAction;
import im.dacer.kata.core.action.ShareAction;
import im.dacer.kata.segment.SimpleParser;
import im.dacer.kata.segment.parser.KuromojiParser;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BigBang {

    public static final String ACTION_SEARCH = "search";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_COPY = "copy";
    public static final String ACTION_BACK = "back";
    private static SimpleParser sParser;

    @StringDef({ACTION_SEARCH, ACTION_SHARE, ACTION_COPY, ACTION_BACK})
    @Retention(SOURCE)
    public @interface ActionType {

    }

    private Map<String, Action> mActionMap = new HashMap<>();

    public BigBang(Context context) {
        registerAction(ACTION_SEARCH, SearchEngine.getSearchAction(context));
        registerAction(ACTION_COPY, CopyAction.create());
        registerAction(ACTION_SHARE, ShareAction.create());
    }

    public static boolean initialized() {
        return sParser != null;
    }

    public void registerAction(@ActionType String type, Action action) {
        mActionMap.put(type, action);
    }

    public void unregisterAction(@ActionType String type) {
        mActionMap.remove(type);
    }

    public Action getAction(@ActionType String type) {
        return mActionMap.get(type);
    }

    public void startAction(Context context, @ActionType String type, String text) {
        Action action = getAction(type);
        if (action != null) {
            action.start(context, text);
        }
    }

    public static Observable<SimpleParser> getSegmentParserAsync() {

        return Observable.fromCallable(new Callable<SimpleParser>() {
            @Override
            public SimpleParser call() throws Exception {
                if (sParser == null) {
                    sParser = new KuromojiParser();
                }
                return sParser;
            }
        }).subscribeOn(Schedulers.io());
    }

    public static void setSegmentParser(SimpleParser parser) {
        sParser = parser;
    }

}
