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
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;

import java.util.ArrayList;
import java.util.List;

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
        //用于取值的SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //按 XML 中分组内的实际顺序，动态给首/中/尾分配卡片位置
        applyCardPositions();
        initView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 延迟到视图创建后再关闭分隔线，避免 mRecyclerView 尚未初始化导致 NPE
        setDividerHeight(0);
    }

    /**
     * 遍历 PreferenceScreen 下所有分组，把同一分组内的卡片项按顺序分配
     * TOP / MIDDLE / BOTTOM / SINGLE，从而拼成一张大卡片。
     */
    private void applyCardPositions() {
        PreferenceScreen screen = getPreferenceScreen();
        if (screen == null) {
            return;
        }
        applyCardPositions(screen);
    }

    private void applyCardPositions(PreferenceGroup group) {
        List<Preference> cardItems = new ArrayList<>();
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference preference = group.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                applyCardPositions((PreferenceGroup) preference);
            } else if (preference instanceof CardPreference
                    || preference instanceof CardSwitchPreference) {
                cardItems.add(preference);
            }
        }

        int size = cardItems.size();
        for (int i = 0; i < size; i++) {
            Preference preference = cardItems.get(i);
            int position;
            if (size == 1) {
                position = CardPreference.POSITION_SINGLE;
            } else if (i == 0) {
                position = CardPreference.POSITION_TOP;
            } else if (i == size - 1) {
                position = CardPreference.POSITION_BOTTOM;
            } else {
                position = CardPreference.POSITION_MIDDLE;
            }
            applyPosition(preference, position);
        }
    }

    private void applyPosition(Preference preference, int position) {
        if (preference instanceof CardSwitchPreference) {
            ((CardSwitchPreference) preference).setPosition(position);
        } else if (preference instanceof CardPreference) {
            ((CardPreference) preference).setPosition(position);
        }
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
        if ("icon0".equals(preference.getKey())) {
            new Thread(() -> {
                boolean success = OverlayHelper.disableOverlay(IconNot1);
                boolean success1 = OverlayHelper.disableOverlay(IconNot2);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        } else if ("icon1".equals(preference.getKey())) {
            new Thread(() -> {
                boolean success = OverlayHelper.enableOverlay(IconQqq);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        } else if ("icon2".equals(preference.getKey())) {
            new Thread(() -> {
                boolean success = OverlayHelper.enableOverlay(IconQut);
            }).start();
            Intent intent = new Intent(getActivity(),OverActivity.class);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preference);
    }
}
