package com.nd.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * 虚拟键盘工具类
 * Created by Junior on 2018/1/25.
 *
 * @author cwj
 */

public class KeyboardUtil {

    /**
     * ctivity的根视图
     */
    private View mRootView;
    /**
     * 纪录根视图的显示高度
     */
    private int mRootViewVisibleHeight;
    private OnSoftKeyBoardChangeListener mOnSoftKeyBoardChangeListener;

    public KeyboardUtil(Activity activity) {
        //获取activity的根视图
        mRootView = activity.getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                if (mRootViewVisibleHeight == 0) {
                    mRootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (mRootViewVisibleHeight == visibleHeight) {
                    return;
                }
                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (mRootViewVisibleHeight - visibleHeight > 200) {
                    if (mOnSoftKeyBoardChangeListener != null) {
                        mOnSoftKeyBoardChangeListener.keyBoardShow(mRootViewVisibleHeight - visibleHeight);
                    }
                    mRootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - mRootViewVisibleHeight > 200) {
                    if (mOnSoftKeyBoardChangeListener != null) {
                        mOnSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - mRootViewVisibleHeight);
                    }
                    mRootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }


    /**
     * 动态显示软键盘
     * <p>
     * 避免输入法面板遮挡
     * <p>在 manifest.xml 中 activity 中设置</p>
     * <p>android:windowSoftInputMode="adjustPan"</p>
     *
     * @param activity activity
     */
    public static void showSoftInput(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态显示软键盘
     *
     * @param view 视图
     */
    public static void showSoftInput(View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static boolean hideSoftInput(View view) {
        if (view == null) {
            return false;
        }
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return false;
        }
        return imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 切换软键盘显示与否状态
     *
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 判断软键盘是否可见
     * <p>默认软键盘最小高度为 200</p>
     *
     * @param activity activity
     * @return {@code true}: 可见<br>{@code false}: 不可见
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        return getContentViewInvisibleHeight(activity) >= 200;
    }

    private static int getContentViewInvisibleHeight(final Activity activity) {
        final View contentView = activity.findViewById(android.R.id.content);
        Rect r = new Rect();
        contentView.getWindowVisibleDisplayFrame(r);
        return contentView.getBottom() - r.bottom;
    }


    public interface OnSoftKeyBoardChangeListener {
        /**
         * 软键盘显示
         *
         * @param height 高度
         */
        void keyBoardShow(int height);

        /**
         * 软键盘隐藏
         *
         * @param height 高度
         */
        void keyBoardHide(int height);
    }

    /**
     * 设置监听器
     *
     * @param activity
     * @param onSoftKeyBoardChangeListener
     */
    public static void setListener(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        KeyboardUtil softKeyBoardListener = new KeyboardUtil(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

    private void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.mOnSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }

}
