package com.nacare.capture.ui.main.custom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nacare.capture.R;
import com.nacare.capture.data.Sdk;
import com.nacare.capture.data.model.FormatterClass;
import com.nacare.capture.data.service.ActivityStarter;
import com.nacare.capture.ui.programs.ProgramsActivity;

import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.program.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrganizationActivity extends AppCompatActivity {
    private Disposable disposable;
    private MaterialButton nextButton;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> stringList;

    private Map<String, String> stringMap;
    private ArrayAdapter adapter;

    public static Intent getOrganizationActivityIntent(Context context) {
        return new Intent(context, OrganizationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cancer Notification Tool");
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        nextButton = findViewById(R.id.btn_proceed);
        nextButton.setOnClickListener(v -> {
                    String data = autoCompleteTextView.getText().toString();
                    if (data.isEmpty()) {
                        Toast.makeText(this, "Select Organization Unit to Proceed", Toast.LENGTH_SHORT).show();
                        autoCompleteTextView.setError("Select Organization Unit to Proceed");
                        autoCompleteTextView.requestFocus();
                        return;
                    }
                    String orgCode = getCodeFromHash(data);
                    if (TextUtils.isEmpty(orgCode)) {
                        autoCompleteTextView.setError("Select Organization Unit to Proceed");
                        autoCompleteTextView.requestFocus();
                        return;
                    }
                    new FormatterClass().saveSharedPref("orgCode", orgCode, this);
                    new FormatterClass().saveSharedPref("orgName", data, this);

                    ActivityStarter.startActivity(OrganizationActivity.this,
                            ProgramsActivity.getProgramActivityIntent(OrganizationActivity.this), false);
                }
        );

        stringMap = new HashMap<>();
        stringList = new ArrayList<>();

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced with new text.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // This method is called to notify you that somewhere within `charSequence`, text has been added or removed.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called to notify you that the characters within `editable` have changed.
                String newText = editable.toString();
                // Do something with the new text.
                if (newText.length() >= 4) {
                    loadOrganizationsByName(newText);

                }
            }
        });


    }

    private String getCodeFromHash(String data) {
        return stringMap.get(data);
    }

    private void loadOrganizationsByName(String newText) {
        Log.e("TAG", "Organization Units ***** " + newText);
        disposable = Sdk.d2().organisationUnitModule().organisationUnits()
//                .byRootOrganisationUnit(true)
                .byDisplayName().like(newText)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(
                        organisationUnits -> {
                            stringList.clear();
                            // Handle the list of organizations here
                            for (OrganisationUnit organisationUnit : organisationUnits) {
                                // Access information about each organisationUnit
                                String name = organisationUnit.displayName();
                                String uid = organisationUnit.uid();
                                String level = String.valueOf(organisationUnit.level());
                                // ... other properties
                                Log.e("TAG", "Organization Units ***** " + name);
                                Log.e("TAG", "Organization Units ***** " + uid);
                                Log.e("TAG", "Organization Units ***** " + level);
                                stringList.add(name);
                                stringMap.put(name, uid);
                            }
                            adapter = new ArrayAdapter(this,
                                    android.R.layout.simple_list_item_1, stringList);
                            autoCompleteTextView.setAdapter(adapter);
                        },
                        throwable -> {
                            // Handle errors here
                            throwable.printStackTrace();
                        }
                );
    }

    private void loadOrganizations() {
        disposable = Sdk.d2().organisationUnitModule().organisationUnits()
                .byRootOrganisationUnit(true)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(
                        organisationUnits -> {
                            // Handle the list of organizations here
                            for (OrganisationUnit organisationUnit : organisationUnits) {
                                // Access information about each organisationUnit
                                String name = organisationUnit.displayName();
                                String uid = organisationUnit.uid();
                                String level = String.valueOf(organisationUnit.level());
                            }
                        },
                        throwable -> {
                            // Handle errors here
                            throwable.printStackTrace();
                        }
                );
    }

    private void fetchOrganisationUnits(String parentUid) {
        disposable = Sdk.d2().organisationUnitModule().organisationUnits()
                .byParentUid().like(parentUid)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        organisationUnits -> {
                            // Handle the list of organizations here
                            for (OrganisationUnit organisationUnit : organisationUnits) {
                                // Access information about each organisationUnit
                                String name = organisationUnit.displayName();
                                String uid = organisationUnit.uid();
                                String level = String.valueOf(organisationUnit.level());
                                // ... other properties
                                Log.e("TAG", "Organization Unit ***** " + name);
                                Log.e("TAG", "Organization Unit ***** " + uid);
                                Log.e("TAG", "Organization Unit ***** " + level);

                                // Check if the level is less than 5 before fetching children
//                                if (organisationUnit.level() < 5) {
//                                    // Recursive call to fetch children for the current organization unit
//                                    fetchOrganisationUnits(uid);
//                                }
                            }
                        },
                        throwable -> {
                            // Handle errors here
                            throwable.printStackTrace();
                        }
                );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}