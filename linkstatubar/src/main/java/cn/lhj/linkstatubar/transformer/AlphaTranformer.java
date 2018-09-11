package cn.lhj.linkstatubar.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * File description.
 * viewpager 堆叠模式 不可见的view alpha 透明度为0
 * @author lihongjun
 * @date 2018/7/10
 */
public class AlphaTranformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        // 对于屏幕外的不做任何操作
        page.setTranslationX(-position * page.getWidth());
        page.setAlpha(position < -1f || position > 1f ? 0f : 1f - (Math.abs(position)));
    }
}
