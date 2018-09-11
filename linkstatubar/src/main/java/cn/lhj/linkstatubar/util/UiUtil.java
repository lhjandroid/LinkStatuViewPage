package cn.lhj.linkstatubar.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Filedescription.
 *
 * @author lihongjun
 * @date 2018/9/7
 */
public class UiUtil {

    public static boolean resetBannerLayoutParam(View banner, int ratioW, int ratioH) {
        if (banner != null) {
            if (ratioH > 0 && ratioW > 0) {
                ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
                // 此处直接使用屏幕宽度作为banner的宽度
                int realWidth = getScreenWidth(banner.getContext()) - banner.getPaddingLeft() - banner.getPaddingRight();
                int realHeight = (int) (realWidth * 1.0f * ratioH / ratioW);

                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(realWidth, realHeight);
                } else {
                    layoutParams.width = realWidth;
                    layoutParams.height = realHeight;
                }
                banner.setLayoutParams(layoutParams);
                banner.setVisibility(View.VISIBLE);
                return true;
            } else {
                banner.setVisibility(View.GONE);
            }
        }
        return false;
    }

    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static boolean isStringEmpt(String str) {
        return str == null || "".equals(str);
    }
}
