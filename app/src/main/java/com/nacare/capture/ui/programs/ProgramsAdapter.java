package com.nacare.capture.ui.programs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.nacare.capture.R;
import com.nacare.capture.data.service.StyleBinderHelper;
import com.nacare.capture.ui.base.DiffByIdItemCallback;
import com.nacare.capture.ui.base.ListItemWithStyleHolder;

import org.hisp.dhis.android.core.program.Program;
import org.hisp.dhis.android.core.program.ProgramType;

public class ProgramsAdapter extends PagedListAdapter<Program, ListItemWithStyleHolder> {

    private final OnProgramSelectionListener programSelectionListener;

    ProgramsAdapter(OnProgramSelectionListener programSelectionListener) {
        super(new DiffByIdItemCallback<>());
        this.programSelectionListener = programSelectionListener;
    }

    @NonNull
    @Override
    public ListItemWithStyleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_with_style, parent, false);
        return new ListItemWithStyleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemWithStyleHolder holder, int position) {
        Program program = getItem(position);
        holder.title.setText(program.displayName());
        holder.subtitle1.setText(program.programType() == ProgramType.WITH_REGISTRATION ?
                "Program with registration" : "Program without registration");
        StyleBinderHelper.bindStyle(holder, program.style());

        holder.card.setOnClickListener(view -> programSelectionListener
                .onProgramSelected(program.uid(), program.programType()));
    }
}
