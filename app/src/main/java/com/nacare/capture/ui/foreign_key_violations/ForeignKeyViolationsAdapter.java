package com.nacare.capture.ui.foreign_key_violations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.nacare.capture.R;
import com.nacare.capture.data.service.DateFormatHelper;
import com.nacare.capture.ui.base.DiffByIdItemCallback;
import com.nacare.capture.ui.base.ListItemHolder;
import com.nacare.capture.data.service.StyleBinderHelper;

import org.hisp.dhis.android.core.maintenance.ForeignKeyViolation;

public class ForeignKeyViolationsAdapter extends PagedListAdapter<ForeignKeyViolation, ListItemHolder> {

    ForeignKeyViolationsAdapter() {
        super(new DiffByIdItemCallback<>());
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        ForeignKeyViolation fkViolation = getItem(position);
        holder.title.setText(fkViolation.notFoundValue());
        holder.subtitle1.setText(String.format("%s.%s", fkViolation.fromTable(), fkViolation.fromColumn()));
        holder.subtitle2.setText(String.format("%s.%s", fkViolation.toTable(), fkViolation.toColumn()));
        holder.rightText.setText(DateFormatHelper.formatDate(fkViolation.created()));
        holder.icon.setImageResource(R.drawable.ic_foreign_key_black_24dp);
        StyleBinderHelper.setBackgroundColor(R.color.colorAccentDark, holder.icon);
    }
}
