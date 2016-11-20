/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package kevinz.huiju.ui.guoke;

import android.os.Bundle;
import android.app.Fragment;

import kevinz.huiju.api.GuokeApi;
import kevinz.huiju.support.Utils;
import kevinz.huiju.ui.base.BaseNaviFragment;
import kevinz.huiju.ui.base.PagerAdapter;

/**
 * Created by mummyding on 15-11-17.
 */
public class GuokeNaviFragment extends BaseNaviFragment {
    private PagerAdapter pagerAdapter;
    int lang = Utils.getCurrentLanguage();
    String[] tab;
    @Override
    protected PagerAdapter initPagerAdapter() {

        if(lang==1){
            tab = GuokeApi.channel_title;
        }else {
            tab = GuokeApi.channel_tag;
        }
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), tab) {
            @Override
            public Fragment getItem(int position) {
                GuokeFragment fragment = new GuokeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("num",position);
                bundle.putSerializable("channel",GuokeApi.channel_tag[position]);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }


}
