/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata;

import android.app.Application;

import im.dacer.kata.core.BigBang;
import im.dacer.kata.core.action.CopyAction;
import im.dacer.kata.core.action.ShareAction;
import im.dacer.kata.service.ListenClipboardService;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BigBang.registerAction(BigBang.ACTION_SEARCH, SearchEngine.getSearchAction(this));
        BigBang.registerAction(BigBang.ACTION_COPY, CopyAction.create());
        BigBang.registerAction(BigBang.ACTION_SHARE, ShareAction.create());
        Config config = Treasure.get(this, Config.class);
        BigBang.registerAction(BigBang.ACTION_BACK, config.isAutoCopy() ? CopyAction.create() : null);

        SegmentEngine.setup(this);

        BigBang.setStyle(config.getItemSpace(), config.getLineSpace(), config.getItemTextSize());

        if (config.isListenClipboard()) {
            ListenClipboardService.start(this);
        }
    }
}
