/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import im.dacer.kata.service.ListenClipboardService;

/**
 * Created by baoyongzhang on 2016/11/1.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ListenClipboardService.start(context);
    }
}
