package kevinz.huiju.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import kevinz.huiju.HuijuApplication;
import kevinz.huiju.R;
import kevinz.huiju.support.Settings;
import kevinz.huiju.support.Utils;

/**
 * Created by Administrator on 2016/11/5.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener{

    Toolbar toolbar;
    private Preference mCollections;
    private Preference mLanguage;
    private CheckBoxPreference mNightMode;
    private CheckBoxPreference mNoPicMode;
    private Preference mClearCache ;
    private Preference mAboutSoftware;

    private Settings mSettings = new Settings();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mLanguage =findPreference("language");
        mLanguage.setSummary(this.getResources().getStringArray(R.array.languages)[Utils.getCurrentLanguage()]);
        mLanguage.setOnPreferenceClickListener(this);

        mCollections = findPreference("collections");
        mCollections.setOnPreferenceClickListener(this);

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
        }else if(preference == mCollections){
            startActivity(new Intent(getContext(), CollectionsActivity.class));
        }else if(preference == mClearCache){
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

    @Override
    public void onDetach() {
        super.onDetach();
        toolbar.setVisibility(View.GONE);
    }
}