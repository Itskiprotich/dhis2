package com.nacare.capture.ui.base;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nacare.capture.R;

public class SubListItemHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView rightText;

    public SubListItemHolder(@NonNull View view) {
        super(view);
        title = view.findViewById(R.id.subItemTitle);
        rightText = view.findViewById(R.id.subRightText);
    }
}