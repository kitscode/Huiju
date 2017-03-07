
package kevinz.huiju.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kevinz.huiju.R;


public abstract class BaseNaviFragment extends Fragment {
    protected View parentView;
    protected ViewPager viewPager;
    protected PagerAdapter pagerAdapter;
    private TabLayout TabLayout;
    protected abstract  PagerAdapter initPagerAdapter();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = View.inflate(getContext(), R.layout.layout_top_navigation,null);
        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        TabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        TabLayout.setVisibility(View.VISIBLE);
        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        TabLayout.setupWithViewPager(viewPager);
        return parentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        TabLayout.setVisibility(View.GONE);
    }
}
