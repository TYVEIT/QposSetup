package com.android.setup.cardui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.setup.R;
import com.android.setup.cardui.model.ContextualCard;

import java.util.List;

/**
 * 卡片列表适配器
 * 支持多种卡片类型，根据卡片分类使用不同的布局
 */
public class ContextualCardAdapter extends ListAdapter<ContextualCard, ContextualCardAdapter.CardViewHolder> {

    public static final int VIEW_TYPE_DEFAULT = 0;
    public static final int VIEW_TYPE_SUGGESTION = 1;
    public static final int VIEW_TYPE_IMPORTANT = 2;
    public static final int VIEW_TYPE_STICKY = 3;

    public interface OnCardClickListener {
        void onCardClick(ContextualCard card);
    }

    public interface OnCardDismissListener {
        void onCardDismiss(ContextualCard card);
    }

    private OnCardClickListener clickListener;
    private OnCardDismissListener dismissListener;

    public ContextualCardAdapter() {
        super(new CardDiffCallback());
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnCardDismissListener(OnCardDismissListener listener) {
        this.dismissListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        ContextualCard card = getItem(position);
        switch (card.getCategory()) {
            case SUGGESTION: return VIEW_TYPE_SUGGESTION;
            case IMPORTANT: return VIEW_TYPE_IMPORTANT;
            case STICKY: return VIEW_TYPE_STICKY;
            default: return VIEW_TYPE_DEFAULT;
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case VIEW_TYPE_SUGGESTION:
                layoutRes = R.layout.item_card_suggestion;
                break;
            case VIEW_TYPE_IMPORTANT:
                layoutRes = R.layout.item_card_important;
                break;
            case VIEW_TYPE_STICKY:
                layoutRes = R.layout.item_card_sticky;
                break;
            default:
                layoutRes = R.layout.item_card_default;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView summaryText;
        private final ImageView iconImage;
        private final View dismissButton;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.cardTitle);
            summaryText = itemView.findViewById(R.id.cardSummary);
            iconImage = itemView.findViewById(R.id.cardIcon);
            dismissButton = itemView.findViewById(R.id.cardDismiss);
        }

        void bind(final ContextualCard card) {
            titleText.setText(card.getTitle());

            if (card.getSummary() != null && !card.getSummary().isEmpty()) {
                summaryText.setVisibility(View.VISIBLE);
                summaryText.setText(card.getSummary());
            } else {
                summaryText.setVisibility(View.GONE);
            }

            if (card.getIconRes() != 0) {
                iconImage.setVisibility(View.VISIBLE);
                iconImage.setImageResource(card.getIconRes());
            } else {
                iconImage.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onCardClick(card);
                }
            });

            if (card.isDismissible()) {
                dismissButton.setVisibility(View.VISIBLE);
                dismissButton.setOnClickListener(v -> {
                    if (dismissListener != null) {
                        dismissListener.onCardDismiss(card);
                    }
                });
            } else {
                dismissButton.setVisibility(View.GONE);
            }
        }
    }

    static class CardDiffCallback extends DiffUtil.ItemCallback<ContextualCard> {
        @Override
        public boolean areItemsTheSame(@NonNull ContextualCard oldItem, @NonNull ContextualCard newItem) {
            return oldItem.getCardName().equals(newItem.getCardName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContextualCard oldItem, @NonNull ContextualCard newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && (oldItem.getSummary() == null ? newItem.getSummary() == null : oldItem.getSummary().equals(newItem.getSummary()))
                    && oldItem.getCategory() == newItem.getCategory()
                    && oldItem.getCardScore() == newItem.getCardScore()
                    && oldItem.isDismissible() == newItem.isDismissible();
        }
    }
}
