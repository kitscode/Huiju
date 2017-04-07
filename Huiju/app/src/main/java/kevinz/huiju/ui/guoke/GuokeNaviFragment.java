package kevinz.huiju.ui.guoke;

import android.app.Fragment;
import android.os.Bundle;

import kevinz.huiju.ui.base.BaseNaviFragment;
import kevinz.huiju.ui.base.PagerAdapter;
import kevinz.huiju.utils.Utils;


public class GuokeNaviFragment extends BaseNaviFragment {
    public static String[] channel_tag = {"hot", "frontier", "review", "interview", "visual", "brief", "fact", "techb"};
    public static String[] channel_title = {"热点", "前沿", "评论", "专访", "视觉", "速读", "谣言粉碎机", "商业科技"};
    private PagerAdapter pagerAdapter;
    int lang = Utils.getCurrentLanguage();
    String[] tabs;
    @Override
    protected PagerAdapter initPagerAdapter() {

        if(lang==1){
            tabs = channel_title;
        }else {
            tabs = channel_tag;
        }
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), tabs) {
            @Override
            public Fragment getItem(int position) {
                GuokeFragment fragment = new GuokeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("num",position);
                bundle.putSerializable("channel",channel_tag[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }


}
