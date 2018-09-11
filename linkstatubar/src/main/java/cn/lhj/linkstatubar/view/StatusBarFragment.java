package cn.lhj.linkstatubar.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import cn.lhj.linkstatubar.R;
import cn.lhj.linkstatubar.adapter.FragmentItem;
import cn.lhj.linkstatubar.util.StatusBarUtil;
import cn.lhj.linkstatubar.util.UiUtil;


/**
 * File description.
 * 首页statusbar 沉沁fragment
 *
 * @author lihongjun
 * @date 2018/7/17
 */
public class StatusBarFragment extends Fragment {

    public static final String ARG_BG_URL = "ARG_BG_URL"; // 背景图片
    public static final String ARG_IS_HOT = "ARG_IS_HOT"; // 是否需要拼接

    private ImageView mIvContent; // 沉沁图片
    private View mVWhite; // 白色背景

    private boolean isWhite = false;
    private boolean mHaseImage; // 是否是有图片

    private boolean mIsVisiblityToUser; // 当前界面是否可见
    private int mHot = 0; // 是否需要和内容区的viewpage链接 1 是 0 不是
    private int contentStartY = 0; // 内容区域距离顶部的位置

    private boolean mIsSeemToImageMode = false; // 似乎是图片模式 有背景图片地址 但不一定成功

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_statusbar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    /**
     * 初始化布局
     *
     * @param view
     */
    private void initView(View view) {
        mIvContent = view.findViewById(R.id.homepage_iv_content);
        mVWhite = view.findViewById(R.id.homepage_v_white);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        contentStartY = StatusBarUtil.getStatusBarHeight(getActivity()) // statubar
                + UiUtil.dp2px(getContext(),44) // titlebar
                + UiUtil.dp2px(getContext(),40); // tabbar
        Bundle bundle = getArguments();
        String bgUrl = "";
        if (bundle != null) {
            bgUrl = bundle.getString(ARG_BG_URL);
            mIsSeemToImageMode = !UiUtil.isStringEmpt(bgUrl);
            mHot = bundle.getInt(ARG_IS_HOT);
        }
        if (bgUrl != null && !bgUrl.equals("")) {
            Glide.with(this).load(bgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (resource != null) {
                        mHaseImage = true;
                        UiUtil.resetBannerLayoutParam(mIvContent, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        mIvContent.setImageDrawable(resource);
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable) resource).start();
                        }
                        mIvContent.post(new Runnable() {
                            @Override
                            public void run() {
                                setTopMargin(mIvContent.getHeight());
                            }
                        });
                    }
                    changeTitleBarStyle(false);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    mHaseImage = false;
                    mIsSeemToImageMode = false;
                    changeTitleBarStyle(false);
                }
            });
        }
    }

    /**
     * 设置气氛图是否需要偏移
     *
     * @param height
     */
    private void setTopMargin(int height) {
        if (mHot == 1 && height > contentStartY) { // 如果有热区图 且布局的高度大于货架内容开始的位置 使布局向上偏移
            mIvContent.setY(contentStartY - height);
        }
    }

    /**
     * statubar显示图片还是白色背景
     *
     * @param isShow
     */
    public void setStatuBarImageVisible(boolean isShow) {
        isWhite = !isShow;
        if (isShow) {
            mVWhite.setVisibility(View.GONE);
            mIvContent.setVisibility(View.VISIBLE);
        } else {
            mVWhite.setVisibility(View.VISIBLE);
            mIvContent.setVisibility(View.GONE);
        }
    }

    public boolean isWhite() {
        return isWhite;
    }

    /**
     * 是否是图片模式
     *
     * @return
     */
    public boolean isImageMode() {
        return mHaseImage;
    }

    /**
     * 是否将要切换到图片模式
     *
     * @return
     */
    public boolean isSeemToImageMode() {
        return mIsSeemToImageMode;
    }

    public void setWhite(boolean white) {
        if (isWhite != white) {
            isWhite = white;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisiblityToUser = isVisibleToUser;
    }

    /**
     * 根据可见行更改theme
     * @param isFromPageSelected 是否是由onPageSelected调用的
     */
    public void changeTitleBarStyle(boolean isFromPageSelected) {
        if ((mIsVisiblityToUser || isFromPageSelected) && mIvContent != null && getParentFragment() != null) { // 当前可见且 已经被初始化
            if (mHaseImage) {
                if (isWhite) {
                    //changeToLightTheme();
                } else {
                    //changeToDarkTheme();
                }
            } else {
                if (mIsSeemToImageMode) {
                    //changeToDarkTheme();
                } else {
                    //changeToLightTheme();
                }
            }
        }
    }

    @Override
    public void setInitialSavedState(@Nullable SavedState state) {

    }
}
