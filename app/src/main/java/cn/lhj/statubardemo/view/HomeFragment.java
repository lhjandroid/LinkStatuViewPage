package cn.lhj.statubardemo.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.lhj.linkstatubar.adapter.FragmentItem;
import cn.lhj.linkstatubar.adapter.MFFragmentPagerAdapter;
import cn.lhj.linkstatubar.bean.StatuBean;
import cn.lhj.linkstatubar.util.StatusBarUtil;
import cn.lhj.linkstatubar.widget.ScrollerViewPager;
import cn.lhj.statubardemo.R;
import cn.lhj.statubardemo.bean.MyStatuBean;

/**
 * Filedescription.
 *
 * @author lihongjun
 * @date 2018/9/7
 */
public class HomeFragment extends Fragment {

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    private ScrollerViewPager mStatuViewPage;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        if (view != null) {
            mAppBarLayout = view.findViewById(R.id.appbar_layout);
            mToolbar = view.findViewById(R.id.tool_bar);
            mToolbar.setTitle("home");

            mTabLayout = view.findViewById(R.id.tab_layout);

            mStatuViewPage = view.findViewById(R.id.statu_fragment);
            mViewPager = view.findViewById(R.id.view_page);

            StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), mAppBarLayout);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        // statu data
        List<StatuBean> statuBeans = new ArrayList<>();
        statuBeans.add(new MyStatuBean("http://img4.imgtn.bdimg.com/it/u=3836539368,3142492826&fm=26&gp=0.jpg",0));
        statuBeans.add(new MyStatuBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3257094336,1836927289&fm=200&gp=0.jpg",0));
        statuBeans.add(new MyStatuBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=494124042,2147566119&fm=26&gp=0.jpg",0));
        statuBeans.add(new MyStatuBean("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1625205896,3555993063&fm=26&gp=0.jpg",0));
        statuBeans.add(new MyStatuBean("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3437869569,1977257809&fm=26&gp=0.jpg",1));
        mStatuViewPage.setSatuBarDatas(getFragmentManager(),statuBeans);

        List<FragmentItem> fragmentItems = new ArrayList<>();
        FragmentItem fragmentItem;
        for (int i=0; i < 5; i ++) {
            fragmentItem = new FragmentItem("页面" + i,TestFragment.class);
            fragmentItems.add(fragmentItem);
        }
        MFFragmentPagerAdapter adapter = new MFFragmentPagerAdapter(getChildFragmentManager(),fragmentItems);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mStatuViewPage.setLinkWith(mViewPager);
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
