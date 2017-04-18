package kevinz.huiju.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import kevinz.huiju.HuijuApplication;
import kevinz.huiju.R;
import kevinz.huiju.db.DBHelper;
import kevinz.huiju.utils.Settings;
import kevinz.huiju.utils.Utils;

/**
 * Created by Administrator on 2016/11/5.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener{

    Toolbar toolbar;
    private Preference mCollection;
    private Preference mLanguage;
    private CheckBoxPreference mNightMode;
    private CheckBoxPreference mNoPicMode;
    private CheckBoxPreference mAutoRefresh;
    private Preference mClearCache ;
    private Preference mAboutSoftware;
    private Button rearrange;

    private Settings mSettings = new Settings();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mLanguage =findPreference("language");
        mLanguage.setSummary(this.getResources().getStringArray(R.array.languages)[Utils.getCurrentLanguage()]);
        mLanguage.setOnPreferenceClickListener(this);

        mCollection = findPreference("collection");
        mCollection.setOnPreferenceClickListener(this);

        mNightMode = (CheckBoxPreference) findPreference("night_mode");
        mNightMode.setOnPreferenceChangeListener(this);

        mNoPicMode = (CheckBoxPreference)findPreference("no_pics");
        mNoPicMode.setOnPreferenceChangeListener(this);

        mClearCache = findPreference("clear");
        mClearCache.setOnPreferenceClickListener(this);

        mAboutSoftware = findPreference("about");
        mAboutSoftware.setOnPreferenceClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.toolbar_settings);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rearrange=(Button)getActivity().findViewById(R.id.rearrange_tabs);
        if(rearrange!=null) {
            rearrange.setVisibility(View.INVISIBLE);
        }
    }

    public void nightModeOpen() {
        Intent intent = new Intent(HuijuApplication.AppContext, NightMode.class);
        HuijuApplication.AppContext.startService(intent);
    }
    public void nightModeClose() {
        Intent intent = new Intent(HuijuApplication.AppContext, NightMode.class);
        HuijuApplication.AppContext.stopService(intent);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference == mNightMode){
            if(!mNightMode.isChecked()){
                nightModeOpen();
            }else{
                nightModeClose();
            }
            return true;
        }else if(preference == mNoPicMode){
            Settings.noPicMode = Boolean.valueOf(newValue.toString());
            Settings.needRecreate = true;
            getActivity().recreate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == mLanguage){
            showLangDialog();
        }else if(preference == mCollection){
            startActivity(new Intent(getContext(), CollectionActivity.class));
        }else if(preference == mClearCache){
            clearCache();
            Snackbar.make(getView(),R.string.clear_success,Snackbar.LENGTH_SHORT).show();
        }else if(preference == mAboutSoftware){

            new AlertDialog.Builder(getContext())
                    .setView(R.layout.about_software)
                    .create()
                    .show();
        }

        return false;
    }



    private void showLangDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.language_dialog)
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.languages), Utils.getCurrentLanguage(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which != Utils.getCurrentLanguage()) {
                                    mSettings.putInt(Settings.LANGUAGE, which);
                                    Settings.needRecreate = true;
                                }
                                dialog.dismiss();
                                if (Settings.needRecreate) {
                                    getActivity().recreate();
                                }
                            }
                        }
                ).show();

    }

    private void clearCache(){
        SQLiteDatabase db=new DBHelper(getContext(),"huiju",null,1).getWritableDatabase();
        db.execSQL("delete from guoke");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbar.setVisibility(View.GONE);
        if(rearrange!=null)
        rearrange.setVisibility(View.GONE);
    }
}