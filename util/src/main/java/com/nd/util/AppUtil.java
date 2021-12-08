package com.nd.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.provider.Telephony;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * App相关工具类
 * Created by Junior on 2018/1/25.
 */

public class AppUtil {


    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * MIME类型与文件后缀名的匹配表
     */
    private static final String[][] MIME_TYPE = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"", "*/*"}};

    /**
     * 进入 App 具体设置
     */
    public static void goAppDetailsSettings(Context context) {
        goAppDetailsSettings(context, context.getPackageName());
    }

    /**
     * 获取 App 具体设置
     *
     * @param packageName 包名
     */
    public static void goAppDetailsSettings(Context context, String packageName) {
        if (isSpace(packageName)) {
            return;
        }
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 App 名称
     *
     * @return App 名称
     */
    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }

    /**
     * 获取 App 名称
     *
     * @param packageName 包名
     * @return App 名称
     */
    public static String getAppName(Context context, String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 App 版本号
     *
     * @return App 版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取 App 版本号
     *
     * @param packageName 包名
     * @return App 版本号
     */
    public static String getAppVersionName(Context context, final String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 App 版本码
     *
     * @return App 版本码
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 获取 App 版本码
     *
     * @param packageName 包名
     * @return App 版本码
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (isSpace(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取 AndroidManifest 下 <strong>meta-data</strong> 的值
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }


    /**
     * 是否空格
     *
     * @param s
     * @return
     */
    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否已经打开gps定位
     *
     * @param context
     * @return boolean
     */
    public static boolean isGPSEnable(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    /**
     * 打开GPS设置界面
     *
     * @param context
     */
    public static void openGPSSetting(Context context) {
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        try {
            context.startActivity(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装程序
     *
     * @param context   上下文
     * @param authority 7.0 及以上安装需要传入清单文件中的{@code <provider>}的 authorities 属性
     * @param apkPath   安装包的路径
     */
    public static void installAPK(Context context, String authority, String apkPath) {
        if (!FileUtil.isFileExists(apkPath)) {
            return;
        }
        try {
            //更改文件权限，三星手机
            chmod("777", apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, authority, new File(apkPath));
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            i.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context.getPackageManager().queryIntentActivities(i, 0).size() > 0) {
            context.startActivity(i);
        }
    }

    /**
     * 获取权限
     *
     * @param permission 权限
     * @param path       路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在pckName包
     *
     * @param pckName
     * @return
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    /**
     * Intent是否可以找到界面
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 在手机上打开文件的方法
     */
    public static void openFile(Context c, String filepath, String authority) {
        if (!new File(filepath).exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(c, authority, new File(filepath));
        } else {
            uri = Uri.fromFile(new File(filepath));
        }
        String type = getMIMEType(filepath);
        intent.setDataAndType(uri, type);
        if (isIntentAvailable(c, intent)) {
            c.startActivity(intent);
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param filepath
     * @return String
     */
    public static String getMIMEType(String filepath) {
        String type = "*/*";
        /* 获取文件的后缀名 */
        String end = FileUtil.getFileExt(filepath).toLowerCase();
        for (int i = 0; i < MIME_TYPE.length; i++) {
            if (end.equals(MIME_TYPE[i][0])) {
                type = MIME_TYPE[i][1];
            }
        }
        return type;
    }

    /**
     * 发送短信
     *
     * @param context
     * @param phone   手机号
     * @param body    内容
     */
    public static void sendSms(Context context, String phone, String body) {
        Intent sendIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
            sendIntent.putExtra("sms_body", body);
            if (defaultSmsPackageName != null) {
                sendIntent.setPackage(defaultSmsPackageName);
            }
        } else {
            sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + phone));
            sendIntent.putExtra("sms_body", body);
            sendIntent.setType("vnd.android-dir/mms-sms");
        }
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(context, sendIntent)) {
            context.startActivity(sendIntent);
        }
    }

    /**
     * 拨打电话
     *
     * @param phone 手机号
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + phone));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Skip to dial.
     *
     * @param phoneNumber The phone number.
     */
    public static void dial(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uri转成File
     * <p>
     * 会在context.getCacheDir()下面新建uri_tmp文件夹，图片缓存在这里
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uriToFile(Context context, Uri uri) {
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
                @SuppressLint("Recycle")
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range")
                    String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    try {
                        InputStream inputStream = resolver.openInputStream(uri);
                        File cacheFile = new File(context.getCacheDir(), "uri_tmp");
                        if (!cacheFile.exists()) {
                            cacheFile.mkdirs();
                        }
                        //该文件放入cache缓存文件夹中
                        File cache = new File(cacheFile, fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(cache);
                        if (inputStream != null) {
                            //上面的copy方法在低版本的手机中会报java.lang.NoSuchMethodError错误，使用原始的读写流操作进行复制
                            byte[] len = new byte[Math.min(inputStream.available(), 1024 * 1024)];
                            int read;
                            while ((read = inputStream.read(len)) != -1) {
                                fileOutputStream.write(len, 0, read);
                            }
                            file = cache;
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
     * 获取应用签名的的 MD5 值
     *
     * @param context
     * @return
     */
    public static String getAppSignatureMD5(Context context) {
        return getAppSignatureHash(context, context.getPackageName(), "MD5");
    }

    /**
     * 获取应用签名的的 SHA1 值
     *
     * @param context
     * @return
     */
    public static String getAppSignatureSHA1(Context context) {
        return getAppSignatureHash(context, context.getPackageName(), "SHA1");
    }

    /**
     * 获取应用签名的的 SHA256 值
     *
     * @param context
     * @return
     */
    public static String getAppSignatureSHA256(Context context) {
        return getAppSignatureHash(context, context.getPackageName(), "SHA256");
    }

    private static String getAppSignatureHash(Context context, final String packageName, final String algorithm) {
        if (isSpace(packageName)) {
            return "";
        }
        Signature[] signature = getAppSignature(context, packageName);
        if (signature == null || signature.length <= 0) {
            return "";
        }
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    /**
     * 获取 App 签名
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Signature[] getAppSignature(Context context, String packageName) {
        if (isSpace(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
