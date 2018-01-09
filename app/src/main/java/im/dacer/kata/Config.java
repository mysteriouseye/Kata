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

    @Default(SearchEngine.GOOGLE)
    String getSearchEngine();
    void setSearchEngine(String searchEngine);

    @Default("false")
    boolean isAutoCopy();
    void setAutoCopy(boolean autoCopy);

    @Default("true")
    boolean isListenClipboard();
    void setListenClipboard(boolean listenClipboard);

    @Default(SegmentEngine.TYPE_THIRD)
    String getSegmentEngine();
    void setSegmentEngine(String segmentEngine);

    @Default("10")
    int getLineSpace();
    void setLineSpace(int space);

    @Default("0")
    int getItemSpace();
    void setItemSpace(int space);

    @Default("15")
    int getItemTextSize();
    void setItemTextSize(int size);
}
