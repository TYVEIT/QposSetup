package com.android.setup.cardui.data;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.setup.cardui.model.CardDataProvider;
import com.android.setup.cardui.model.ContextualCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 卡片仓库 - 统一管理所有卡片数据
 * 对应 AOSP Settings 中 ContextualCardsController + CardContentProvider
 *
 * 使用 LiveData 提供响应式数据流
 */
public class CardRepository {

    private static volatile CardRepository instance;

    private final List<CardDataProvider> providers = new CopyOnWriteArrayList<>();
    private final MutableLiveData<List<ContextualCard>> cardsLiveData = new MutableLiveData<>(Collections.emptyList());
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private CardRepository() {
        // 单例
    }

    public static CardRepository getInstance() {
        if (instance == null) {
            synchronized (CardRepository.class) {
                if (instance == null) {
                    instance = new CardRepository();
                }
            }
        }
        return instance;
    }

    /** 注册卡片提供者 */
    public void registerProvider(@NonNull CardDataProvider provider) {
        if (provider.isAvailable()) {
            boolean exists = false;
            for (CardDataProvider p : providers) {
                if (p.getProviderId().equals(provider.getProviderId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                providers.add(provider);
            }
        }
    }

    /** 注销卡片提供者 */
    public void unregisterProvider(@NonNull String providerId) {
        CardDataProvider toRemove = null;
        for (CardDataProvider p : providers) {
            if (p.getProviderId().equals(providerId)) {
                toRemove = p;
                break;
            }
        }
        if (toRemove != null) {
            providers.remove(toRemove);
        }
    }

    /** 获取卡片 LiveData（观察用） */
    @NonNull
    public LiveData<List<ContextualCard>> getCards() {
        return cardsLiveData;
    }

    /**
     * 刷新所有卡片（异步）
     * 从所有提供者获取卡片，合并后按规则排序
     */
    public void refreshCards() {
        new Thread(() -> {
            final List<ContextualCard> allCards = new ArrayList<>();

            for (CardDataProvider provider : providers) {
                if (provider.isAvailable()) {
                    try {
                        List<ContextualCard> cards = provider.getCards(null);
                        if (cards != null) {
                            allCards.addAll(cards);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 按分类优先级 + 评分排序
            Collections.sort(allCards, new Comparator<ContextualCard>() {
                @Override
                public int compare(ContextualCard c1, ContextualCard c2) {
                    // 先按分类优先级（降序）
                    int priorityCompare = Integer.compare(
                            getCategoryPriority(c2.getCategory()),
                            getCategoryPriority(c1.getCategory())
                    );
                    if (priorityCompare != 0) {
                        return priorityCompare;
                    }
                    // 再按评分（降序）
                    return Double.compare(c2.getCardScore(), c1.getCardScore());
                }
            });

            // 切回主线程更新 LiveData
            mainHandler.post(() -> cardsLiveData.setValue(allCards));
        }).start();
    }

    /** 获取指定分类的卡片 */
    @NonNull
    public List<ContextualCard> getCardsByCategory(@NonNull ContextualCard.Category category) {
        List<ContextualCard> result = new ArrayList<>();
        List<ContextualCard> current = cardsLiveData.getValue();
        if (current != null) {
            for (ContextualCard card : current) {
                if (card.getCategory() == category) {
                    result.add(card);
                }
            }
        }
        return result;
    }

    /** 移除指定卡片 */
    public void dismissCard(@NonNull String cardName) {
        ContextualCard cardToDismiss = null;
        List<ContextualCard> current = cardsLiveData.getValue();
        if (current != null) {
            for (ContextualCard card : current) {
                if (card.getCardName().equals(cardName)) {
                    cardToDismiss = card;
                    break;
                }
            }
        }

        if (cardToDismiss == null) return;

        // 询问提供者是否允许删除
        CardDataProvider provider = findProviderForCard(cardToDismiss);
        boolean allowDismiss = true;
        if (provider != null) {
            allowDismiss = provider.onCardDismissed(cardToDismiss);
        }

        if (allowDismiss) {
            List<ContextualCard> newList = new ArrayList<>();
            if (current != null) {
                for (ContextualCard card : current) {
                    if (!card.getCardName().equals(cardName)) {
                        newList.add(card);
                    }
                }
            }
            cardsLiveData.setValue(newList);
        }
    }

    /** 处理卡片点击 */
    public boolean handleCardClick(@NonNull ContextualCard card) {
        CardDataProvider provider = findProviderForCard(card);
        if (provider != null) {
            return provider.onCardClicked(card);
        }
        return false;
    }

    /** 查找卡片所属的提供者 */
    @Nullable
    private CardDataProvider findProviderForCard(@NonNull ContextualCard card) {
        // 通过卡片名称前缀匹配提供者（约定 cardName 以 providerId_ 开头）
        for (CardDataProvider provider : providers) {
            if (card.getCardName().startsWith(provider.getProviderId() + "_")) {
                return provider;
            }
        }
        return null;
    }

    /** 分类优先级，数值越高越靠前 */
    private int getCategoryPriority(ContextualCard.Category category) {
        switch (category) {
            case IMPORTANT: return 100;
            case STICKY: return 90;
            case SUGGESTION: return 80;
            case DEFERRED_SETUP: return 70;
            case POSSIBLE: return 60;
            case DEFAULT:
            default: return 50;
        }
    }
}
