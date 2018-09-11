package cn.lhj.statubardemo.bean;

import cn.lhj.linkstatubar.bean.StatuBean;

/**
 * Filedescription.
 *
 * @author lihongjun
 * @date 2018/9/7
 */
public class MyStatuBean implements StatuBean {

    private String bgUrl;

    private int hot;

    public MyStatuBean(String bgUrl, int hot) {
        this.bgUrl = bgUrl;
        this.hot = hot;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    /**
     * 背景地址
     *
     * @return
     */
    @Override
    public String getBackgroundUrl() {
        return bgUrl;
    }

    /**
     * 是否需要内容区和沉沁连接为一体
     *
     * @return
     */
    @Override
    public int getHot() {
        return hot;
    }
}
