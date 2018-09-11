package cn.lhj.linkstatubar.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * File description.
 * 提供给viewpager使用的PagerAdapter
 *
 * @author lihongjun
 * @date 2018/7/17
 */
public class MFFragmentPagerAdapter extends MFFragmentStatePageAdapter {

    private List<FragmentItem> mFragmentItems; // fragment item集合

    public MFFragmentPagerAdapter(FragmentManager fm, List<FragmentItem> fragmentItems) {
        super(fm);
        if (fragmentItems != null) {
            this.mFragmentItems = fragmentItems;
        } else {
            this.mFragmentItems = new ArrayList<>();
        }
    }

    public void setmFragmentItems(List<FragmentItem> mFragmentItems) {
        this.mFragmentItems = mFragmentItems;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        if (mFragmentItems != null && mFragmentItems.size() > position) {
            try {
                Fragment fragment = (Fragment) mFragmentItems.get(position).getFragmentClass().newInstance();
                fragment.setArguments(mFragmentItems.get(position).getBundle());
                return fragment;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mFragmentItems != null ? mFragmentItems.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (mFragmentItems != null && mFragmentItems.size() > position) ? mFragmentItems.get(position).getTitle() : "";
    }
}
