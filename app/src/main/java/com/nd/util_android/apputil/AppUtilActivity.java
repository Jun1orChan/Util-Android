package com.nd.util_android.apputil;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nd.util.AppUtil;
import com.nd.util_android.R;

public class AppUtilActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apputil);
        findViewById(R.id.btnSignatureMD5).setOnClickListener(this);
        findViewById(R.id.btnSignatureSHA1).setOnClickListener(this);
        findViewById(R.id.btnSignatureSHA256).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignatureMD5:
                ((TextView) v).setText(AppUtil.getAppSignatureMD5(this));
                break;
            case R.id.btnSignatureSHA1:
                ((TextView) v).setText(AppUtil.getAppSignatureSHA1(this));
                break;
            case R.id.btnSignatureSHA256:
                ((TextView) v).setText(AppUtil.getAppSignatureSHA256(this));
                AppUtil.goAppDetailsSettings(getApplication());
                break;
        }
    }
}
