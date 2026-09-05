package com.android.setup.cardui.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设置项数据模型
 */
public class PreferenceItem {

    public enum Type {
        CATEGORY,   // 分类标题
        ITEM        // 普通设置项
    }

    /** 在分组卡片中的位置 */
    public enum GroupPosition {
        TOP,        // 组内第一项
        MIDDLE,     // 组内中间项
        BOTTOM,     // 组内最后一项
        SINGLE,     // 组内只有一项
        NONE        // 不属于分组（如分类标题）
    }

    @NonNull
    private final String key;
    @NonNull
    private final Type type;
    @NonNull
    private final String title;
    @Nullable
    private final String summary;
    private final int iconRes;
    @Nullable
    private final Runnable onClickAction;
    @NonNull
    private final GroupPosition groupPosition;

    private PreferenceItem(Builder builder) {
        this.key = builder.key;
        this.type = builder.type;
        this.title = builder.title;
        this.summary = builder.summary;
        this.iconRes = builder.iconRes;
        this.onClickAction = builder.onClickAction;
        this.groupPosition = builder.groupPosition;
    }

    @NonNull
    public String getKey() { return key; }

    @NonNull
    public Type getType() { return type; }

    @NonNull
    public String getTitle() { return title; }

    @Nullable
    public String getSummary() { return summary; }

    public int getIconRes() { return iconRes; }

    @Nullable
    public Runnable getOnClickAction() { return onClickAction; }

    @NonNull
    public GroupPosition getGroupPosition() { return groupPosition; }

    public static class Builder {
        private String key;
        private Type type = Type.ITEM;
        private String title;
        private String summary;
        private int iconRes = 0;
        private Runnable onClickAction;
        private GroupPosition groupPosition = GroupPosition.NONE;

        public Builder(@NonNull String key, @NonNull String title) {
            this.key = key;
            this.title = title;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setOnClickAction(Runnable action) {
            this.onClickAction = action;
            return this;
        }

        public Builder setGroupPosition(GroupPosition position) {
            this.groupPosition = position;
            return this;
        }

        public PreferenceItem build() {
            return new PreferenceItem(this);
        }
    }
}
