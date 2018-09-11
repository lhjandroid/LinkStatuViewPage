package cn.lhj.linkstatubar.adapter;

import android.os.Bundle;

/**
 * File description.
 *  fragment item 提供给 viewpager使用
 * @author lihongjun
 * @date 2018/7/17
 */
public class FragmentItem {

    private CharSequence mTitle; // fragment标题
    private Class mFragmentClass; //fragment class类
    private Bundle mBundle; //fragment 构建时所带的参数

    public FragmentItem(Class fragmentClass) {
        this("",fragmentClass,null);
    }

    public FragmentItem(CharSequence title, Class fragmentClass) {
        this(title,fragmentClass,null);
    }

    public FragmentItem(CharSequence title, Class fragmentClass, Bundle bundle) {
        mTitle = title;
        mFragmentClass = fragmentClass;
        mBundle = bundle;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public Class getFragmentClass() {
        return mFragmentClass;
    }

}
