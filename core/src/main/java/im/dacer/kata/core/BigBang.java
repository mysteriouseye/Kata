/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata.core;

import android.content.Context;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import im.dacer.kata.core.action.Action;
import im.dacer.kata.segment.SimpleParser;
import im.dacer.kata.segment.parser.KuromojiParser;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class BigBang {

    public static final String ACTION_SEARCH = "search";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_COPY = "copy";
    public static final String ACTION_BACK = "back";
    private static SimpleParser sParser;
    private static int sItemSpace;
    private static int sLineSpace;
    private static int sItemTextSize;

    public static void setStyle(int itemSpace, int lineSpace, int itemTextSize) {
        sItemSpace = itemSpace;
        sLineSpace = lineSpace;
        sItemTextSize = itemTextSize;
    }

    @StringDef({ACTION_SEARCH, ACTION_SHARE, ACTION_COPY, ACTION_BACK})
    @Retention(SOURCE)
    public @interface ActionType {

    }

    private static Map<String, Action> mActionMap = new HashMap<>();

    private BigBang() {
    }

    public static boolean initialized() {
        return sParser != null;
    }

    public static void registerAction(@ActionType String type, Action action) {
        mActionMap.put(type, action);
    }

    public static void unregisterAction(@ActionType String type) {
        mActionMap.remove(type);
    }

    public static Action getAction(@ActionType String type) {
        return mActionMap.get(type);
    }

    public static void startAction(Context context, @ActionType String type, String text) {
        Action action = BigBang.getAction(type);
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

    public static int getItemSpace() {
        return sItemSpace;
    }

    public static int getLineSpace() {
        return sLineSpace;
    }

    public static int getItemTextSize() {
        return sItemTextSize;
    }
}
