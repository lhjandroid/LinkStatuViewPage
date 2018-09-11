package cn.lhj.linkstatubar.listener;

import android.support.v4.view.ViewPager;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

/**
 * Filedescription.
 * viewpage 切换方向监听
 * @author lihongjun
 * @date 2018/8/27
 */
public abstract class OnPageChangeDirectListener implements ViewPager.OnPageChangeListener{

    private int mViewPagerIndex; // 当前viewpage的position
    private int mStartPosition = -1; // 触摸到viewPager第一次滑动时的位置 根据它来判断时左滑还是右滑
    private int mDirect = 0; // 0 向右 1 向左

    private int mNextPosition = -1; // 即滑到页的position
    private float mNextAlpha; // 下一页的alpha

    private boolean mChangeFromScroll = false;// 是否时滑动引起的分页

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 如果onpagescroll 是由于点击tab切换引起的则不执行下面的操作
        if (!mChangeFromScroll) {
            return;
        }
        if (mStartPosition == -1) { // 确定方向 得到即将要切换的position
            mStartPosition = position;
            if (mStartPosition == mViewPagerIndex) {
                mDirect = 1;
                mNextPosition = mViewPagerIndex + 1;
                onChangeToNext();
            } else {
                mDirect = 0;
                mNextPosition = mViewPagerIndex - 1;
                onChangeToPre();
            }
        }

        if (mStartPosition == position) { // 只在滑动和未达到切换条件并且返回时的范围内进行alpha变化
            if(positionOffset != 0) { // 不在边界值时改变当前和即将滑到布局的alpha值
                if (mDirect == 0) { // 根据不同的方向来取对应alpha的值 左右滑动对应alpah的值是相反的
                    mNextAlpha = 1.0f - positionOffset;
                } else {
                    mNextAlpha = positionOffset;
                }
                onProgressChange(mNextAlpha);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if(!mChangeFromScroll) {
            onPageScrollStateChanged(SCROLL_STATE_IDLE);
        }
        setWillGoItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) { // 当滚动停止时已经确定当前页是什么
            mViewPagerIndex = getCurrentItem(); // 沉沁栏切换
            mStartPosition = -1; // page 改变时置会初始状态
            mNextPosition = -1;
            mChangeFromScroll = false;
        } else if (state == SCROLL_STATE_DRAGGING) {
            if (!mChangeFromScroll) {
                mChangeFromScroll = true;
            }
        }
    }

    /**
     * 获取下一页的position
     * @return
     */
    public int getNextPosition() {
        return mNextPosition;
    }

    /**
     * 切换到下一页
     */
    public void onChangeToNext() {

    }

    /**
     * 切换到上一页
     */
    public void onChangeToPre() {

    }

    /**
     * 切换进度 next的进度 当前的则为 1 - progess
     * @param progress
     */
    public void onProgressChange(float progress) {

    }

    /**
     * 获取当前页面的 position
     *  @return 当前position
     */
    public abstract int getCurrentItem();

    /**
     * 设置即将跳转过去的item
     * @param position
     */
    public void setWillGoItem(int position) {

    }
}
