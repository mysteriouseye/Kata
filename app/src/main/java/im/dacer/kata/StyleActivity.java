package im.dacer.kata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.baoyz.treasure.Treasure;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import im.dacer.kata.core.BigBang;
import im.dacer.kata.core.BigBangLayout;

public class StyleActivity extends AppCompatActivity {

    private BigBangLayout mBigBang;
    private Config mConfig;
    private DiscreteSeekBar mTextSize;
    private DiscreteSeekBar mLineSpace;
    private DiscreteSeekBar mItemSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        mBigBang = (BigBangLayout) findViewById(R.id.bigbang);
        mTextSize = (DiscreteSeekBar) findViewById(R.id.textSize);
        mLineSpace = (DiscreteSeekBar) findViewById(R.id.lineSpace);
        mItemSpace = (DiscreteSeekBar) findViewById(R.id.itemSpace);

        mConfig = Treasure.get(this, Config.class);

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

        mTextSize.setProgress(mConfig.getItemTextSize());
        mLineSpace.setProgress(mConfig.getLineSpace());
        mItemSpace.setProgress(mConfig.getItemSpace());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConfig.setItemTextSize(mTextSize.getProgress());
        mConfig.setLineSpace(mLineSpace.getProgress());
        mConfig.setItemSpace(mItemSpace.getProgress());
        BigBang.setStyle(mConfig.getItemSpace(), mConfig.getLineSpace(), mConfig.getItemTextSize());
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
