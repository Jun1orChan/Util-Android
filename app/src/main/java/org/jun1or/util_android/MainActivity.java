package org.jun1or.util_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.jun1or.util_android.apputil.AppUtilActivity;
import org.jun1or.util_android.clipboard.ClipboardActivity;
import org.jun1or.util_android.pinyin.PinyinActivity;
import org.jun1or.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucent(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnAppUtil).setOnClickListener(this);
        findViewById(R.id.btnClipboard).setOnClickListener(this);
        findViewById(R.id.btnPinyin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAppUtil:
                Intent appUtilIntent = new Intent(this, AppUtilActivity.class);
                startActivity(appUtilIntent);
                break;
            case R.id.btnClipboard:
                Intent clipboardIntent = new Intent(this, ClipboardActivity.class);
                startActivity(clipboardIntent);
                break;
            case R.id.btnPinyin:
                Intent pinyinIntent = new Intent(this, PinyinActivity.class);
                startActivity(pinyinIntent);
                break;
        }
    }
}
