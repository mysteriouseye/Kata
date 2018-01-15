package im.dacer.kata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import im.dacer.kata.core.BigBangLayout;
import im.dacer.kata.core.data.AppPreference;
import im.dacer.kata.core.model.BigBangStyle;

public class StyleActivity extends AppCompatActivity {

    private BigBangLayout mBigBang;
    private DiscreteSeekBar mTextSize;
    private DiscreteSeekBar mLineSpace;
    private DiscreteSeekBar mItemSpace;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        mBigBang = (BigBangLayout) findViewById(R.id.bigbang);
        mTextSize = (DiscreteSeekBar) findViewById(R.id.textSize);
        mLineSpace = (DiscreteSeekBar) findViewById(R.id.lineSpace);
        mItemSpace = (DiscreteSeekBar) findViewById(R.id.itemSpace);
        appPreference = new AppPreference(this);

        String[] testStrings = new String[]{
                "日本国",
                "または",
                "日本",
                "は",
                "、",
                "東アジア",
                "に",
                "位置する",
                "日本列島",
                "及び",
                "、",
                "南西諸島",
                "・",
                "伊豆諸島",
                "・",
                "小笠原諸島",
                "など",
                "から",
                "成る",
                "島国",
                "である"};
        for (String testString : testStrings) {
            mBigBang.addTextItem(testString);
        }

        mTextSize.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setItemTextSize(value);
            }
        });

        mLineSpace.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setLineSpace(value);
            }
        });

        mItemSpace.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setItemSpace(value);
            }
        });


        mTextSize.setProgress(appPreference.getItemTextSize());
        mLineSpace.setProgress(appPreference.getLineSpace());
        mItemSpace.setProgress(appPreference.getItemSpace());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        appPreference.setBigBangStyle(new BigBangStyle(
                mItemSpace.getProgress(),
                mLineSpace.getProgress(),
                mTextSize.getProgress()
        ));
        super.onDestroy();
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

    static abstract class SimpleListener implements DiscreteSeekBar.OnProgressChangeListener {

        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

        }
    }
}
