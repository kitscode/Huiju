
package kevinz.huiju.ui.base;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


public abstract class PagerAdapter extends FragmentPagerAdapter {

    private String [] titles;

    public PagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles =titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
