package com.android.setup;

import static android.app.ProgressDialog.show;

import androidx.fragment.app.Fragment;
import com.android.setup.IconActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

public class IconPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private SharedPreferences sharedPreferences = null;
    private static final String TAG = "SystemCmd";
    private static final String IconQqq = "QQQ";
    private static final String IconQut = "Qut";
    private static final String IconNot1 = "Not1";
    private static final String IconNot2 = "Not2";


    private Preference loginDjiAccount;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.icon_settings, rootKey);
        CardPreference profile = findPreference("profile");
        CardPreference security = findPreference("security");
        CardPreference notifications = findPreference("notifications");

        if (profile != null) profile.setPosition(CardPreference.POSITION_TOP);
        if (security != null) security.setPosition(CardPreference.POSITION_MIDDLE);
        if (notifications != null) notifications.setPosition(CardPreference.POSITION_BOTTOM);
        //用于取值的SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initView();
    }

    private void initView() {
        loginDjiAccount = findPreference("login_dji_account");
        if (loginDjiAccount != null) {
            loginDjiAccount.setOnPreferenceClickListener(this);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case "login_dji_account":
                String rtmpUrlStr = sharedPreferences.getString("rtmp_url_pre", "");
                if ("".equals(rtmpUrlStr)) {
                    Toast.makeText(getActivity(), "请在直播推流地址中随意填写值,再来点我...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "填写了:" + rtmpUrlStr, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return false;
    }
    @Override
    public boolean onPreferenceTreeClick(@NonNull Preference preference) {
        if (preference.getKey().equals("icon0")) {
            new Thread(() -> {
                boolean success = OverlayHelper.disableOverlay(IconNot1);
                boolean success1 = OverlayHelper.disableOverlay(IconNot2);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        } else if (preference.getKey().equals("icon1")) {
            new Thread(() -> {
                boolean success = OverlayHelper.enableOverlay(IconQqq);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        } else if (preference.getKey().equals("icon2")) {
            new Thread(() -> {
                boolean success = OverlayHelper.enableOverlay(IconQut);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preference);
    }
}
