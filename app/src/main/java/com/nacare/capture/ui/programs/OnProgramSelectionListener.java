package com.nacare.capture.ui.programs;

import org.hisp.dhis.android.core.program.ProgramType;

public interface OnProgramSelectionListener {
    void onProgramSelected(String programUid, ProgramType programType, String type);
}
