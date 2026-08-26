package com.android.setup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;

public class CardSwitchPreference extends SwitchPreferenceCompat {

    private int position = CardPreference.POSITION_MIDDLE;

    public CardSwitchPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.crad_preference);
        setWidgetLayoutResource(R.layout.widget_switch_android16);
    }

    public CardSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.crad_preference);
        setWidgetLayoutResource(R.layout.widget_switch_android16);
    }

    public CardSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.crad_preference);
        setWidgetLayoutResource(R.layout.widget_switch_android16);
    }

    public CardSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.crad_preference);
        setWidgetLayoutResource(R.layout.widget_switch_android16);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View itemView = holder.itemView;
        switch (position) {
            case CardPreference.POSITION_TOP:
                itemView.setBackgroundResource(R.drawable.card_style_top);
                break;
            case CardPreference.POSITION_MIDDLE:
                itemView.setBackgroundResource(R.drawable.card_style_middle);
                break;
            case CardPreference.POSITION_BOTTOM:
                itemView.setBackgroundResource(R.drawable.card_style_bottom);
                break;
            case CardPreference.POSITION_SINGLE:
                itemView.setBackgroundResource(R.drawable.card_style_single);
                break;
        }
    }
}