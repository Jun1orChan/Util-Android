package com.nd.util;

import android.content.Context;

import java.io.File;

/**
 * 由于Android 10的原因，暂时都先放在内部存储里面
 *
 * @author cwj
 */
public final class StorageUtil {

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = context.getCacheDir();
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    /**
     * 获取缓存目录
     *
     * @param context
     * @param cacheDir 子目录
     * @return
     */
    public static File getCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = getCacheDirectory(context);
        File subCacheDir = new File(appCacheDir, cacheDir);
        if (!subCacheDir.exists()) {
            subCacheDir.mkdirs();
        }
        return subCacheDir;
    }
}