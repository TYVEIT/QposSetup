package com.android.setup.cardui.data;

import android.content.Context;
import android.content.Intent;

import com.android.setup.StandardActivity;
import com.android.setup.cardui.model.PreferenceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置项数据提供者
 * 替代 PreferenceFragment，提供设置列表数据
 * 自动计算分组内位置，实现卡片粘连效果
 */
public class PreferenceDataProvider {

    private final Context context;

    public PreferenceDataProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    public List<PreferenceItem> getPreferences() {
        List<PreferenceItem> items = new ArrayList<>();

        // ========== 分类：基本设置 ==========
        items.add(new PreferenceItem.Builder("cat_basic", "基本设置")
                .setType(PreferenceItem.Type.CATEGORY)
                .build());

        items.add(new PreferenceItem.Builder("pref_network", "网络和互联网")
                .setSummary("WLAN、移动网络、流量使用")
                .setGroupPosition(PreferenceItem.GroupPosition.TOP)
                .setOnClickAction(() -> {
                    // 跳转到网络设置
                })
                .build());

        items.add(new PreferenceItem.Builder("pref_connected", "已连接的设备")
                .setSummary("蓝牙、投屏、NFC")
                .setGroupPosition(PreferenceItem.GroupPosition.MIDDLE)
                .setOnClickAction(() -> {
                    // 跳转到连接设备
                })
                .build());

        items.add(new PreferenceItem.Builder("pref_apps", "应用")
                .setSummary("默认应用、权限、通知")
                .setGroupPosition(PreferenceItem.GroupPosition.MIDDLE)
                .setOnClickAction(() -> {
                    // 跳转到应用设置
                })
                .build());

        items.add(new PreferenceItem.Builder("pref_display", "显示")
                .setSummary("亮度、壁纸、字体大小")
                .setGroupPosition(PreferenceItem.GroupPosition.MIDDLE)
                .setOnClickAction(() -> {
                    // 跳转到显示设置
                })
                .build());

        items.add(new PreferenceItem.Builder("pref_sound", "声音和振动")
                .setSummary("音量、铃声、勿扰模式")
                .setGroupPosition(PreferenceItem.GroupPosition.BOTTOM)
                .setOnClickAction(() -> {
                    // 跳转到声音设置
                })
                .build());

        // ========== 分类：演示 ==========
        items.add(new PreferenceItem.Builder("cat_demo", "演示")
                .setType(PreferenceItem.Type.CATEGORY)
                .build());

        items.add(new PreferenceItem.Builder("open_standard", "标准折叠标题栏 Demo")
                .setSummary("演示随滚动自动折叠的大标题")
                .setGroupPosition(PreferenceItem.GroupPosition.TOP)
                .setOnClickAction(() -> {
                    Intent intent = new Intent(context, StandardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .build());

        items.add(new PreferenceItem.Builder("pref_about", "关于手机")
                .setSummary("设备信息、法律信息")
                .setGroupPosition(PreferenceItem.GroupPosition.BOTTOM)
                .setOnClickAction(() -> {
                    // 跳转到关于手机
                })
                .build());

        return items;
    }
}
