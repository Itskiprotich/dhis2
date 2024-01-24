package com.nacare.capture.ui.base;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nacare.capture.R;

public class ListItemWithSyncHolder extends ListItemHolder {

    public final ImageView syncIcon;
    public final RecyclerView recyclerView;

    public ListItemWithSyncHolder(@NonNull View view) {
        super(view);
        syncIcon = view.findViewById(R.id.syncIcon);
        recyclerView = view.findViewById(R.id.importConflictsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}