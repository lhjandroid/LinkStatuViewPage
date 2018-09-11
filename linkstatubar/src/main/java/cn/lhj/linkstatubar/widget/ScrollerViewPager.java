package cn.lhj.linkstatubar.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.lhj.linkstatubar.adapter.FragmentItem;
import cn.lhj.linkstatubar.adapter.MFFragmentPagerAdapter;
import cn.lhj.linkstatubar.adapter.MFFragmentStatePageAdapter;
import cn.lhj.linkstatubar.bean.StatuBean;
import cn.lhj.linkstatubar.listener.OnPageChangeDirectListener;
import cn.lhj.linkstatubar.support.ViewPagerScroller;
import cn.lhj.linkstatubar.transformer.AlphaTranformer;
import cn.lhj.linkstatubar.view.StatusBarFragment;


/**
 * File description.
 * 为viewpager 设置切换during
 * @author lihongjun
 * @date 2018/7/17
 */
public class ScrollerViewPager extends ViewPager {

    private int mScrollDuration = 0; // viewpager切换时间

    public ScrollerViewPager(@NonNull Context context) {
        this(context,null);
    }

    public ScrollerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try {
            //设置滚动切换的动画时间
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(getContext(),
                    new AccelerateInterpolator());
            field.set(this, scroller);
            scroller.setScrollDuration(mScrollDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPageTransformer(true,new AlphaTranformer());

    }

    /**
     * 联动依附的viewpage
     * @param viewPager
     */
    public void setLinkWith(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new OnPageChangeDirectListener() {
                @Override
                public int getCurrentItem() {
                    return viewPager.getCurrentItem();
                }

                @Override
                public void setWillGoItem(int position) {
                    if (getAdapter() != null && getAdapter() instanceof MFFragmentStatePageAdapter && getNextPositionByCheck(position) != -1) {
                        setCurrentItem(position);
                    }
                }

                @Override
                public void onProgressChange(float progress) {
                    super.onProgressChange(progress);
                    if (viewPager != null && viewPager.getAdapter() != null && getAdapter() != null && getAdapter() instanceof MFFragmentPagerAdapter
                            && getNextPositionByCheck(getNextPosition()) != -1) { // 若果没有数据则不做任何处理 如果pos位置不合法页也不做任何处理
                        View currentView = null;
                        if (((MFFragmentPagerAdapter)getAdapter()).getCurrentPrimaryItem() != null) {
                            currentView = ((MFFragmentPagerAdapter)getAdapter()).getCurrentPrimaryItem().getView();
                        }
                        View nextView = ((MFFragmentPagerAdapter)getAdapter()).getItemView(getNextPosition());
                        if (currentView != null) {
                            ViewCompat.setAlpha(currentView,1 - progress);
                        }
                        if (nextView != null) {
                            ViewCompat.setAlpha(nextView,progress);
                        }
                    }

                }
            });
        }
    }

    /**
     * 设置viewpager切换时间
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration) {
        mScrollDuration = scrollDuration;
    }

    /**
     * 获取下一页 并判断下一页合法性
     * @param position
     * @return
     */
    private int getNextPositionByCheck(int position) {
        if (getAdapter() == null) {
            return -1;
        }
        return (position < 0 || position >= getAdapter().getCount())?-1:position;
    }

    /**
     * 设置沉沁栏数据
     * @param statuBeans
     */
    public void setSatuBarDatas(FragmentManager manager,List<StatuBean> statuBeans) {
        if (statuBeans == null || statuBeans.size() == 0) {
            setAdapter(null);
            return;
        }
        List<FragmentItem> fragmentItems = new ArrayList<>();
        FragmentItem fragmentItem;
        Bundle bundle;
        for(StatuBean statuBean: statuBeans) {
            bundle = new Bundle();
            bundle.putString(StatusBarFragment.ARG_BG_URL,statuBean.getBackgroundUrl());
            bundle.putInt(StatusBarFragment.ARG_IS_HOT,statuBean.getHot());
            fragmentItem = new FragmentItem("",StatusBarFragment.class,bundle);
            fragmentItems.add(fragmentItem);
        }
        MFFragmentPagerAdapter adapter = new MFFragmentPagerAdapter(manager,fragmentItems);
        setAdapter(adapter);
    }
}
