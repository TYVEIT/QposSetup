package com.android.setup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class CardPreference extends Preference {

    public static final int POSITION_TOP = 0;
    public static final int POSITION_MIDDLE = 1;
    public static final int POSITION_BOTTOM = 2;
    public static final int POSITION_SINGLE = 3;

    private int position = POSITION_MIDDLE;

    // 三个构造方法必须全写，避免崩溃
    public CardPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.crad_preference);
    }

    public CardPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.crad_preference);
    }

    public CardPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.crad_preference);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setBackgroundResource(R.drawable.ripple_preference);
        View itemView = holder.itemView;
        switch (position) {
            case POSITION_TOP:
                itemView.setBackgroundResource(R.drawable.card_style_top);
                break;
            case POSITION_MIDDLE:
                itemView.setBackgroundResource(R.drawable.card_style_middle);
                break;
            case POSITION_BOTTOM:
                itemView.setBackgroundResource(R.drawable.card_style_bottom);
                break;
            case POSITION_SINGLE:
                itemView.setBackgroundResource(R.drawable.card_style_single);
                break;
        }
    }
}
