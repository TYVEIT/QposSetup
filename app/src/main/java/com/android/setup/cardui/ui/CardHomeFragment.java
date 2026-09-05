package com.android.setup.cardui.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.setup.R;
import com.android.setup.cardui.adapter.ContextualCardAdapter;
import com.android.setup.cardui.adapter.PreferenceListAdapter;
import com.android.setup.cardui.data.CardRepository;
import com.android.setup.cardui.data.PreferenceDataProvider;
import com.android.setup.cardui.model.ContextualCard;
import com.android.setup.cardui.provider.SuggestionCardProvider;

import java.util.List;

/**
 * 首页 Fragment：卡片 + 设置列表
 *
 * 使用单个 RecyclerView + ConcatAdapter 实现，参考 AOSP Settings 首页架构：
 * - 顶部：Contextual Cards 上下文卡片（可动态增删）
 * - 底部：设置项列表（分类标题 + 设置项）
 *
 * 优势：
 * 1. 单列表滚动，流畅自然
 * 2. 卡片和设置项各自独立的 Adapter，解耦
 * 3. 卡片数据通过 LiveData 响应式更新
 */
public class CardHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContextualCardAdapter cardAdapter;
    private PreferenceListAdapter preferenceAdapter;
    private CardRepository cardRepository;
    private PreferenceDataProvider preferenceProvider;

    public CardHomeFragment() {
        // Required empty public constructor
    }

    public static CardHomeFragment newInstance() {
        return new CardHomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initDataProviders();
        setupRecyclerView();
        observeCards();

        // 首次加载卡片
        cardRepository.refreshCards();
    }

    private void initViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.homeRecyclerView);
    }

    private void initDataProviders() {
        // 卡片仓库（单例）
        cardRepository = CardRepository.getInstance();

        // 注册卡片提供者 - 在这里添加你自己的卡片提供者
        cardRepository.registerProvider(new SuggestionCardProvider());

        // 设置项数据提供者
        preferenceProvider = new PreferenceDataProvider(requireContext());
    }

    private void setupRecyclerView() {
        // 卡片 Adapter
        cardAdapter = new ContextualCardAdapter();
        cardAdapter.setOnCardClickListener(card -> {
            boolean handled = cardRepository.handleCardClick(card);
            if (!handled) {
                handleCardClickDefault(card);
            }
        });
        cardAdapter.setOnCardDismissListener(card -> {
            cardRepository.dismissCard(card.getCardName());
        });

        // 设置项 Adapter
        preferenceAdapter = new PreferenceListAdapter();
        preferenceAdapter.submitList(preferenceProvider.getPreferences());

        // 使用 ConcatAdapter 组合两个 Adapter（默认配置已开启 isolateViewTypes）
        ConcatAdapter concatAdapter = new ConcatAdapter(cardAdapter, preferenceAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(concatAdapter);
    }

    private void observeCards() {
        cardRepository.getCards().observe(getViewLifecycleOwner(), new Observer<List<ContextualCard>>() {
            @Override
            public void onChanged(List<ContextualCard> cards) {
                cardAdapter.submitList(cards);
            }
        });
    }

    /**
     * 默认的卡片点击处理
     */
    private void handleCardClickDefault(ContextualCard card) {
        // 根据卡片名称处理不同的点击
        // 在实际项目中，可以在这里处理深链接跳转等
    }
}
