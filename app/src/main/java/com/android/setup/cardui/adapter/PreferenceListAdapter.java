package com.android.setup.cardui.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.setup.R;
import com.android.setup.cardui.model.PreferenceItem;

/**
 * 设置项列表适配器
 * 支持分类标题和普通设置项两种类型
 * 设置项以分组卡片形式展示，组内自动粘连
 */
public class PreferenceListAdapter extends ListAdapter<PreferenceItem, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    public PreferenceListAdapter() {
        super(new PreferenceDiffCallback());
    }

    @Override
    public int getItemViewType(int position) {
        PreferenceItem item = getItem(position);
        return item.getType() == PreferenceItem.Type.CATEGORY ? VIEW_TYPE_CATEGORY : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CATEGORY) {
            View view = inflater.inflate(R.layout.item_preference_category, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_preference, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PreferenceItem item = getItem(position);
        if (holder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) holder).bind(item);
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind(item);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.categoryTitle);
        }

        void bind(PreferenceItem item) {
            titleText.setText(item.getTitle());
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView summaryText;
        private final View divider;
        private final View itemContainer;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.prefTitle);
            summaryText = itemView.findViewById(R.id.prefSummary);
            divider = itemView.findViewById(R.id.prefDivider);
            itemContainer = itemView.findViewById(R.id.prefItemContainer);
        }

        void bind(final PreferenceItem item) {
            titleText.setText(item.getTitle());

            if (item.getSummary() != null && !item.getSummary().isEmpty()) {
                summaryText.setVisibility(View.VISIBLE);
                summaryText.setText(item.getSummary());
            } else {
                summaryText.setVisibility(View.GONE);
            }

            // 根据分组位置设置背景和分割线
            PreferenceItem.GroupPosition position = item.getGroupPosition();
            Drawable background;
            boolean showDivider = false;

            switch (position) {
                case TOP:
                    background = ContextCompat.getDrawable(itemView.getContext(),
                            R.drawable.bg_group_card_top);
                    showDivider = true; // 顶部项显示底部分割线
                    break;
                case MIDDLE:
                    background = ContextCompat.getDrawable(itemView.getContext(),
                            R.drawable.bg_group_card_middle);
                    showDivider = true; // 中间项显示底部分割线
                    break;
                case BOTTOM:
                    background = ContextCompat.getDrawable(itemView.getContext(),
                            R.drawable.bg_group_card_bottom);
                    showDivider = false; // 底部项不显示分割线
                    break;
                case SINGLE:
                    background = ContextCompat.getDrawable(itemView.getContext(),
                            R.drawable.bg_group_card_single);
                    showDivider = false;
                    break;
                case NONE:
                default:
                    background = ContextCompat.getDrawable(itemView.getContext(),
                            R.drawable.bg_group_card_single);
                    showDivider = false;
                    break;
            }

            if (itemContainer != null) {
                itemContainer.setBackground(background);
            } else {
                itemView.setBackground(background);
            }

            if (divider != null) {
                divider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (item.getOnClickAction() != null) {
                    item.getOnClickAction().run();
                }
            });
        }
    }

    static class PreferenceDiffCallback extends DiffUtil.ItemCallback<PreferenceItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PreferenceItem oldItem, @NonNull PreferenceItem newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PreferenceItem oldItem, @NonNull PreferenceItem newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && (oldItem.getSummary() == null ? newItem.getSummary() == null : oldItem.getSummary().equals(newItem.getSummary()))
                    && oldItem.getType() == newItem.getType()
                    && oldItem.getGroupPosition() == newItem.getGroupPosition();
        }
    }
}
