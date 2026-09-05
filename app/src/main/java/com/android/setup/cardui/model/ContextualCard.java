package com.android.setup.cardui.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 卡片数据模型
 * 参考 AOSP Settings 的 ContextualCardProto 定义
 */
public class ContextualCard {

    /** 卡片分类枚举 */
    public enum Category {
        DEFAULT(0),
        SUGGESTION(1),
        POSSIBLE(2),
        IMPORTANT(3),
        DEFERRED_SETUP(5),
        STICKY(6);

        private final int value;

        Category(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @NonNull
    private final String cardName;          // 卡片唯一标识
    @NonNull
    private final String title;             // 标题
    @Nullable
    private final String summary;           // 摘要
    private final int iconRes;              // 图标资源ID，0表示无
    @NonNull
    private final Category category;        // 分类
    private final double cardScore;         // 评分，用于排序
    @Nullable
    private final String deepLinkUri;       // 深链接
    private final boolean dismissible;      // 是否可删除
    @NonNull
    private final Map<String, Object> extras; // 扩展数据

    private ContextualCard(Builder builder) {
        this.cardName = builder.cardName;
        this.title = builder.title;
        this.summary = builder.summary;
        this.iconRes = builder.iconRes;
        this.category = builder.category;
        this.cardScore = builder.cardScore;
        this.deepLinkUri = builder.deepLinkUri;
        this.dismissible = builder.dismissible;
        this.extras = builder.extras;
    }

    @NonNull
    public String getCardName() { return cardName; }

    @NonNull
    public String getTitle() { return title; }

    @Nullable
    public String getSummary() { return summary; }

    public int getIconRes() { return iconRes; }

    @NonNull
    public Category getCategory() { return category; }

    public double getCardScore() { return cardScore; }

    @Nullable
    public String getDeepLinkUri() { return deepLinkUri; }

    public boolean isDismissible() { return dismissible; }

    @NonNull
    public Map<String, Object> getExtras() { return extras; }

    /** Builder 模式，方便构建卡片 */
    public static class Builder {
        private String cardName;
        private String title;
        private String summary;
        private int iconRes = 0;
        private Category category = Category.DEFAULT;
        private double cardScore = 0.0;
        private String deepLinkUri;
        private boolean dismissible = true;
        private Map<String, Object> extras = new HashMap<>();

        public Builder(@NonNull String cardName, @NonNull String title) {
            this.cardName = cardName;
            this.title = title;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setCardScore(double cardScore) {
            this.cardScore = cardScore;
            return this;
        }

        public Builder setDeepLinkUri(String deepLinkUri) {
            this.deepLinkUri = deepLinkUri;
            return this;
        }

        public Builder setDismissible(boolean dismissible) {
            this.dismissible = dismissible;
            return this;
        }

        public Builder putExtra(String key, Object value) {
            this.extras.put(key, value);
            return this;
        }

        public ContextualCard build() {
            if (cardName == null || cardName.isEmpty()) {
                throw new IllegalArgumentException("cardName must not be empty");
            }
            if (title == null) {
                throw new IllegalArgumentException("title must not be null");
            }
            return new ContextualCard(this);
        }
    }
}
