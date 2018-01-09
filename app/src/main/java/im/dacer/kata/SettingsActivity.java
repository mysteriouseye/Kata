/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package im.dacer.kata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import im.dacer.kata.R;
import im.dacer.kata.core.BigBang;
import im.dacer.kata.core.action.CopyAction;
import im.dacer.kata.service.ListenClipboardService;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView mSearchEngineText;
    private SwitchCompat mAutoCopySwitch;
    private SwitchCompat mListenClipboardSwitch;
    private Config mConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        findViewById(R.id.search_engine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this).setItems(SearchEngine.getSupportSearchEngineList(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConfig.setSearchEngine(SearchEngine.getSupportSearchEngineList()[which]);
                        BigBang.registerAction(BigBang.ACTION_SEARCH, SearchEngine.getSearchAction(getApplicationContext()));
                        updateUI();
                    }
                }).show();
            }
        });

        mSearchEngineText = (TextView) findViewById(R.id.search_engine_text);

        mAutoCopySwitch = (SwitchCompat) findViewById(R.id.auto_copy_switch);
        mAutoCopySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfig.setAutoCopy(isChecked);
                BigBang.registerAction(BigBang.ACTION_BACK, mConfig.isAutoCopy() ? CopyAction.create() : null);
                updateUI();
            }
        });

        mListenClipboardSwitch = (SwitchCompat) findViewById(R.id.listen_clipboard_switch);
        mListenClipboardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfig.setListenClipboard(isChecked);
                if (mConfig.isListenClipboard()) {
                    ListenClipboardService.start(getApplicationContext());
                } else {
                    ListenClipboardService.stop(getApplicationContext());
                }
                updateUI();
            }
        });

        findViewById(R.id.bigbang_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, StyleActivity.class));
            }
        });
        mConfig = Treasure.get(this, Config.class);
        updateUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateUI() {
        mSearchEngineText.setText(mConfig.getSearchEngine());
        mAutoCopySwitch.setChecked(mConfig.isAutoCopy());
        mListenClipboardSwitch.setChecked(mConfig.isListenClipboard());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
