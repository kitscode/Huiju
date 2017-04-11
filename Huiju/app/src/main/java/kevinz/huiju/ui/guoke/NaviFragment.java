
package kevinz.huiju.ui.guoke;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kevinz.huiju.R;
import kevinz.huiju.db.DBHelper;
import kevinz.huiju.ui.base.PagerAdapter;
import kevinz.huiju.utils.Utils;


public  class NaviFragment extends Fragment {
    protected View parentView;
    protected ViewPager viewPager;
    private TabLayout TabLayout;
    private Button Rearrange;

    private PagerAdapter pagerAdapter;
    int lang = Utils.getCurrentLanguage();
    String[] tabs=new String[8];
    String[] channels=new String[8];
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = View.inflate(getContext(), R.layout.layout_top_navigation,null);
        viewPager = (ViewPager) parentView.findViewById(R.id.inner_viewpager);
        TabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        Rearrange =(Button) getActivity().findViewById(R.id.rearrange_tabs);
        TabLayout.setVisibility(View.VISIBLE);
        Rearrange.setVisibility(View.VISIBLE);
        pagerAdapter = initPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        TabLayout.setupWithViewPager(viewPager);
        return parentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        TabLayout.setVisibility(View.GONE);
        Rearrange.setVisibility(View.GONE);
    }

    protected PagerAdapter initPagerAdapter() {
        db=new DBHelper(getContext(),"huiju",null,1).getWritableDatabase();
        Cursor c=db.rawQuery("select * from tabs",null);
        int i=0;

        if(lang==1){
            while(c.moveToNext()){
                tabs[i]=c.getString(2);
                channels[i]=c.getString(1);
                i++;
            }
        }else {
            while(c.moveToNext()){
                tabs[i]=c.getString(1);
                channels[i]=c.getString(1);
                i++;
            }
        }

        pagerAdapter = new PagerAdapter(getChildFragmentManager(), tabs) {
            @Override
            public Fragment getItem(int position) {
                GuokeFragment fragment = new GuokeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("num",position);
                bundle.putSerializable("channel",channels[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }

}
