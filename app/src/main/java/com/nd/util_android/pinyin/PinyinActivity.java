package com.nd.util_android.pinyin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nd.util.PinyinUtil;
import com.nd.util_android.R;


public class PinyinActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyin);
        mTvResult = (TextView) findViewById(R.id.tvResult);
        findViewById(R.id.btnCss2Pinyin).setOnClickListener(this);
        findViewById(R.id.btnCss2PinyinWithSplit).setOnClickListener(this);
        findViewById(R.id.btnPinyinFirstLetter).setOnClickListener(this);
        findViewById(R.id.btnPinyinFirstLetters).setOnClickListener(this);
        findViewById(R.id.btnSurnamePinyin).setOnClickListener(this);
        findViewById(R.id.btnSurnameFirstLetter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCss2Pinyin:
                mTvResult.setText(PinyinUtil.ccs2Pinyin(((Button) v).getText().toString()));
                break;
            case R.id.btnCss2PinyinWithSplit:
                mTvResult.setText(PinyinUtil.ccs2Pinyin(((Button) v).getText().toString(), "-"));
                break;
            case R.id.btnPinyinFirstLetter:
                mTvResult.setText(PinyinUtil.getPinyinFirstLetter(((Button) v).getText().toString()));
                break;
            case R.id.btnPinyinFirstLetters:
                mTvResult.setText(PinyinUtil.getPinyinFirstLetters(((Button) v).getText().toString()));
                break;
            case R.id.btnSurnamePinyin:
                mTvResult.setText(PinyinUtil.getSurnamePinyin(((Button) v).getText().toString()));
                break;
            case R.id.btnSurnameFirstLetter:
                mTvResult.setText(PinyinUtil.getSurnameFirstLetter(((Button) v).getText().toString()));
                break;
        }
    }
}
