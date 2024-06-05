package com.nd.util_android.clipboard;

import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.nd.util.StorageUtil;
import com.nd.util.ClipboardUtil;
import com.nd.util_android.R;

import java.io.File;

public class ClipboardActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mTvClipboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);
        mTvClipboard = (TextView) findViewById(R.id.tvData);
        findViewById(R.id.btnCopyText).setOnClickListener(this);
        findViewById(R.id.btnCopyRichText).setOnClickListener(this);
        findViewById(R.id.btnCopyIntent).setOnClickListener(this);
        findViewById(R.id.btnCopyUri).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCopyText:
                ClipboardUtil.copyText(this, "文本拷贝内容");
                mTvClipboard.setText(ClipboardUtil.getText(this));
                break;
            case R.id.btnCopyRichText:
                ClipboardUtil.copyHtmlText(this, "111", "222",
                        "<strong style=\"margin: 0px; padding: 0px; border: 0px; color: rgb(64, 64, 64); font-family: STHeiti, 'Microsoft YaHei', Helvetica, Arial, sans-serif; font-size: 17px; font-style: normal; font-variant: normal; letter-spacing: normal; line-height: 25.920001983642578px; orphans: auto; text-align: justify; text-indent: 34.560001373291016px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(235, 23, 23);\">央视</strong>");
                mTvClipboard.setText(Html.fromHtml(ClipboardUtil.getHtmlText(this).toString()));
                break;
            case R.id.btnCopyIntent:
                ClipboardUtil.copyIntent(this, getOpenBrowserIntent());
                mTvClipboard.setText(ClipboardUtil.getIntent(this).toString());
                startActivity(ClipboardUtil.getIntent(this));
                break;
            case R.id.btnCopyUri:
                ClipboardUtil.copyUri(this, FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(StorageUtil.getCacheDirectory(this), "1.png")));
                mTvClipboard.setText(ClipboardUtil.getUri(this).toString());
                break;
        }
    }

    private Intent getOpenBrowserIntent() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String mimeType = ClipboardUtil.getPrimaryClipMimeType(this);
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.equals(ClipDescription.MIMETYPE_TEXT_HTML))
                mTvClipboard.setText(Html.fromHtml(ClipboardUtil.getHtmlText(this).toString()));
            else if (mimeType.equals(ClipDescription.MIMETYPE_TEXT_INTENT)) {
                mTvClipboard.setText(ClipboardUtil.getIntent(this).toString());
            } else if (mimeType.equals(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                mTvClipboard.setText(ClipboardUtil.getText(this));
            }
        }
    }
}
