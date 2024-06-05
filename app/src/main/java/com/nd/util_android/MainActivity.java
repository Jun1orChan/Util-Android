package com.nd.util_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.nd.util.AppUtil;
import com.nd.util.StatusBarUtil;
import com.nd.util_android.apputil.AppUtilActivity;
import com.nd.util_android.clipboard.ClipboardActivity;
import com.nd.util_android.pinyin.PinyinActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucent(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnAppUtil).setOnClickListener(this);
        findViewById(R.id.btnClipboard).setOnClickListener(this);
        findViewById(R.id.btnPinyin).setOnClickListener(this);
        ((LinearLayout.LayoutParams) findViewById(R.id.btnAppUtil).getLayoutParams()).setMargins(0, getStatusBarHeight(this), 0, 0);
        Log.e("TAG", "getStatusBarHeight============" + getStatusBarHeight(this));
    }

    public static int getStatusBarHeight(Context context) {
        return StatusBarUtil.getStatusBarHeight(context);
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
