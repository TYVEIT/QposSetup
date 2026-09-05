package com.android.setup.cardui.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 卡片数据提供者接口
 * 每个业务模块实现此接口来提供自己的卡片
 * 对应 AOSP 中各种 CardController
 */
public interface CardDataProvider {

    /** 获取提供者唯一ID */
    @NonNull
    String getProviderId();

    /**
     * 获取该提供者的卡片列表
     * @param category 卡片分类筛选，null表示返回所有分类
     * @return 卡片列表
     */
    @NonNull
    List<ContextualCard> getCards(@Nullable ContextualCard.Category category);

    /** 该提供者是否可用 */
    default boolean isAvailable() {
        return true;
    }

    /**
     * 卡片被点击时的回调
     * @return 是否消费了点击事件
     */
    default boolean onCardClicked(@NonNull ContextualCard card) {
        return false;
    }

    /**
     * 卡片被删除时的回调
     * @return 是否允许删除
     */
    default boolean onCardDismissed(@NonNull ContextualCard card) {
        return true;
    }
}
