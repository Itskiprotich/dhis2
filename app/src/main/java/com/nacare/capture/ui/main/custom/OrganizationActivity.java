package com.nacare.capture.ui.main.custom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.nacare.capture.R;
import com.nacare.capture.data.Sdk;
import com.nacare.capture.data.service.ActivityStarter;
import com.nacare.capture.ui.programs.ProgramsActivity;

import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.program.Program;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrganizationActivity extends AppCompatActivity {
    private Disposable disposable;
    private MaterialButton nextButton;

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
        nextButton=findViewById(R.id.btn_proceed);
        nextButton.setOnClickListener(v -> ActivityStarter.startActivity(OrganizationActivity.this, ProgramsActivity.getProgramActivityIntent(OrganizationActivity.this), false));
        loadOrganizations();


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
                                // ... other properties
                                Log.e("TAG", "Organization Units ***** " + name);
                                Log.e("TAG", "Organization Units ***** " + uid);
                                Log.e("TAG", "Organization Units ***** " + level);
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