package com.android.setup.cardui.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.setup.R;
import com.android.setup.cardui.model.CardDataProvider;
import com.android.setup.cardui.model.ContextualCard;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例：建议类卡片提供者
 * 模拟一些系统建议和提示卡片
 */
public class SuggestionCardProvider implements CardDataProvider {

    private static final String PROVIDER_ID = "suggestion";

    @NonNull
    @Override
    public String getProviderId() {
        return PROVIDER_ID;
    }

    @NonNull
    @Override
    public List<ContextualCard> getCards(@Nullable ContextualCard.Category category) {
        List<ContextualCard> cards = new ArrayList<>();

        // 建议：开启查找我的设备
        cards.add(new ContextualCard.Builder(PROVIDER_ID + "_find_device", "开启「查找我的设备」")
                .setSummary("帮助你在丢失时定位、锁定或清空设备")
                .setIconRes(R.drawable.ic_card_search)
                .setCategory(ContextualCard.Category.SUGGESTION)
                .setCardScore(85.0)
                .setDismissible(true)
                .build());

        // 建议：系统更新
        cards.add(new ContextualCard.Builder(PROVIDER_ID + "_system_update", "有新的系统更新可用")
                .setSummary("点击查看 Android 安全更新详情")
                .setIconRes(R.drawable.ic_card_tips)
                .setCategory(ContextualCard.Category.SUGGESTION)
                .setCardScore(80.0)
                .setDismissible(true)
                .build());

        // 建议：设置壁纸
        cards.add(new ContextualCard.Builder(PROVIDER_ID + "_wallpaper", "个性化你的壁纸")
                .setSummary("从多种主题和壁纸中选择")
                .setIconRes(R.drawable.ic_card_tips)
                .setCategory(ContextualCard.Category.POSSIBLE)
                .setCardScore(65.0)
                .setDismissible(true)
                .build());

        // 重要：存储空间不足
        cards.add(new ContextualCard.Builder(PROVIDER_ID + "_storage", "存储空间不足")
                .setSummary("已使用 92%，建议清理缓存和无用文件")
                .setIconRes(R.drawable.ic_card_error)
                .setCategory(ContextualCard.Category.IMPORTANT)
                .setCardScore(92.0)
                .setDismissible(true)
                .build());

        // 固定：用户账号
        cards.add(new ContextualCard.Builder(PROVIDER_ID + "_account", "Google 账号")
                .setSummary("user@example.com · 点击管理账号")
                .setIconRes(R.drawable.ic_card_tips)
                .setCategory(ContextualCard.Category.STICKY)
                .setCardScore(70.0)
                .setDismissible(false)
                .build());

        if (category != null) {
            List<ContextualCard> filtered = new ArrayList<>();
            for (ContextualCard card : cards) {
                if (card.getCategory() == category) {
                    filtered.add(card);
                }
            }
            return filtered;
        }
        return cards;
    }
}
