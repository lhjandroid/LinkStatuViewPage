# LinkStatuViewPage
联动沉沁状态栏 透明度切换图片
忙了一阵终于有点时间了,记录下最近UI上实现的功能
效果如下
![这里写图片描述](https://img-blog.csdn.net/20180911115537503)

沉沁状态栏渐变切换图片
一开始想的是用两张imageview去实现这个效果 但是由于app有别的效果还有用两张Imageview的方式判断太复杂.所以就想了另一种方法去实现
![这里写图片描述](https://img-blog.csdn.net/20180911144320647)

通过采用两个viewpager联动的方式来达到切换图片渐变的效果
沉沁区的viewpage 暂时命名为 statuViewPage
内容区的viewpage 暂时命名为 contentViewPage;
项目１
:   首先实现沉沁栏viewpager 堆叠在一起 并且滑动的时候透明度渐变 还好有PageTransformer,所以很轻松的就实现了这个点
```
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
```
    对于transformPage的position参数的解释不知道可以网上搜索下
    把上面的AlphaTranformer设置给statuViewPage就能实现堆叠模式的viewpager了,且切换时带透明度变换

项目2
:   滑动内容区域的contentViewPage让沉淀statuViewPage联动起来,同过给内容区的contentViewPage设置切换pager监听来实现 
1.  当内容区域往左滑动  那么contentViewPage当前页的fragment 透明度从1变到0
    contentViewPage的前一个页面从0变到1
2. 内容区域往右滑动  那么contentViewPage当前页的fragment 透明度从1变到0 contentViewPage的后一个页面从0变到1

首先需要知道获取viewpage的切换方向
代码如下
```
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
```
通过判断 onPageScrolled 中的position变化可以得知 viewpage的切换方向
当positio等于viewpage.getCurrentItem()时表示向右话
否则的话向左滑动

根据滑动方向来动态设置当前页的透明度和即将要切换到的页面的透明度
```
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
```
通过这样设置就能让两个viewpage联动切换了

**关于沉沁栏透明采用的GitHub上的 https://github.com/laobie 来兼容实现的**
