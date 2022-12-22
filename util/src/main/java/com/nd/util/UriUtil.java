package com.nd.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Uri 相关
 */
public final class UriUtil {

    private UriUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * Uri转成File
     * <p>
     * 会在context.getCacheDir()下面新建uri_tmp文件夹，缓存在这里
     *
     * @param context
     * @param uri
     * @param path    缓存目录
     * @return
     */
    public static File cacheUriToFile(Context context, Uri uri, String path) {
        if (uri == null) {
            return null;
        }
        File file = null;
        if (uri.getScheme() != null) {
            if (uri.getScheme().equals(ContentResolver.SCHEME_FILE) && uri.getPath() != null) {
                //此uri为文件，并且path不为空(保存在沙盒内的文件可以随意访问，外部文件path则为空)
                file = new File(uri.getPath());
            } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                //此uri为content类型，将该文件复制到沙盒内
                ContentResolver resolver = context.getContentResolver();
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range")
                    String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    try {
                        InputStream inputStream = resolver.openInputStream(uri);
                        File cacheFile = new File(path);
                        if (!cacheFile.exists()) {
                            cacheFile.mkdirs();
                        }
                        //该文件放入cache缓存文件夹中
                        File cache = new File(cacheFile, System.currentTimeMillis() + fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(cache);
                        if (inputStream != null) {
                            byte[] len = new byte[Math.min(inputStream.available() + 1, 1024)];
                            int read;
                            while ((read = inputStream.read(len)) != -1) {
                                fileOutputStream.write(len, 0, read);
                            }
                            file = cache;
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return file;
    }

    /**
     * Resource to uri.
     * <p>res2Uri([res type]/[res name]) -> res2Uri(drawable/icon), res2Uri(raw/icon)</p>
     * <p>res2Uri([resource_id]) -> res2Uri(R.drawable.icon)</p>
     *
     * @param resPath The path of res.
     * @return uri
     */
    public static Uri res2Uri(Context context, String resPath) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resPath);
    }

    /**
     * Uri to file.
     *
     * @param context
     * @param uri     The uri.
     * @return file
     */
    public static File uri2File(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = uri2FileReal(context, uri);
        return file;
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private static File uri2FileReal(Context context, Uri uri) {
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            if (path != null) {
                return new File(path);
            }
            Log.d("UriUtil", uri.toString() + " parse failed. -> 0");
            return null;
        }// end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                    try {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = storageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

                        Object result = getVolumeList.invoke(storageManager);

                        final int length = Array.getLength(result);
                        for (int i = 0; i < length; i++) {
                            Object storageVolumeElement = Array.get(result, i);
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);

                            final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue;

                            //Primary storage is already handled.
                            if ((Boolean) isPrimary.invoke(storageVolumeElement)
                                    && (Boolean) isEmulated.invoke(storageVolumeElement)) {
                                continue;
                            }

                            String uuid = (String) getUuid.invoke(storageVolumeElement);

                            if (uuid != null && uuid.equals(type)) {
                                return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("UriUtil", uri.toString() + " parse failed. " + ex.toString() + " -> 1_0");
                    }
                }
                Log.d("UriUtil", uri.toString() + " parse failed. -> 1_0");
                return null;
            }// end 1_0
            else if ("com.android.providers.downloads.documents".equals(authority)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtil", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return new File(id.substring(4));
                } else if (id.startsWith("msf:")) {
                    id = id.split(":")[1];
                }
                long availableId = 0;
                try {
                    availableId = Long.parseLong(id);
                } catch (Exception e) {
                    return null;
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);
                    try {
                        File file = getFileFromUri(context, contentUri);
                        if (file != null) {
                            return file;
                        }
                    } catch (Exception ignore) {
                    }
                }

                Log.d("UriUtil", uri.toString() + " parse failed. -> 1_1");
                return null;
            }// end 1_1
            else if ("com.android.providers.media.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    Log.d("UriUtil", uri.toString() + " parse failed. -> 1_2");
                    return null;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getFileFromUri(context, contentUri, selection, selectionArgs);
            }// end 1_2
            else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                return getFileFromUri(context, uri);
            }// end 1_3
            else {
                Log.d("UriUtil", uri.toString() + " parse failed. -> 1_4");
                return null;
            }// end 1_4
        }// end 1
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            return getFileFromUri(context, uri);
        }// end 2
        else {
            Log.d("UriUtil", uri.toString() + " parse failed. -> 3");
            return null;
        }// end 3
    }

    private static File getFileFromUri(Context context, Uri uri) {
        return getFileFromUri(context, uri, null, null);
    }

    private static File getFileFromUri(Context context, Uri uri,
                                       String selection,
                                       String[] selectionArgs) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                File fileDir = Environment.getExternalStorageDirectory();
                return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                return new File(path.replace("/root", ""));
            }
        }

        final Cursor cursor = context.getContentResolver().query(
                uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor == null) {
            Log.d("UriUtil", uri.toString() + " parse failed(cursor is null).");
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                } else {
                    Log.d("UriUtil", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong).");
                    return null;
                }
            } else {
                Log.d("UriUtil", uri.toString() + " parse failed(moveToFirst return false).");
                return null;
            }
        } catch (Exception e) {
            Log.d("UriUtil", uri.toString() + " parse failed.");
            return null;
        } finally {
            cursor.close();
        }
    }

}
