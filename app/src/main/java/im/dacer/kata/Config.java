/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata;

import com.baoyz.treasure.Default;
import com.baoyz.treasure.Preferences;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
@Preferences
public interface Config {

    @Default("true")
    boolean isListenClipboard();
    void setListenClipboard(boolean listenClipboard);

    @Default("false")
    boolean isDatabaseImported();
    void setDatabaseImported(boolean imported);

    @Default(SegmentEngine.TYPE_THIRD)
    String getSegmentEngine();
    void setSegmentEngine(String segmentEngine);

}
