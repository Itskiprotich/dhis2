package com.example.android.androidskeletonapp.ui.code_executor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.utils.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.hisp.dhis.android.core.analytics.linelist.LineListResponse;
import org.hisp.dhis.android.core.analytics.linelist.LineListResponseValue;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CodeExecutorActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView executingNotificator;
    private TextView resultNotificator;

    private Disposable disposable;

    public static Intent getIntent(Context context) {
        return new Intent(context, CodeExecutorActivity.class);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_executor);
        Toolbar toolbar = findViewById(R.id.codeExecutorToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        executingNotificator = findViewById(R.id.codeExecutorNotificator);
        resultNotificator = findViewById(R.id.resultNotificator);
        progressBar = findViewById(R.id.codeExecutorProgressBar);
        FloatingActionButton codeExecutorButton = findViewById(R.id.codeExecutorButton);

        codeExecutorButton.setOnClickListener(view -> {
            view.setEnabled(Boolean.FALSE);
            view.setVisibility(View.INVISIBLE);
            Snackbar.make(view, "Executing...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            executingNotificator.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            resultNotificator.setVisibility(View.INVISIBLE);

            disposable = executeCode()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            result -> {
                                executingNotificator.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                resultNotificator.setText(result);
                                resultNotificator.setVisibility(View.VISIBLE);
                                view.setEnabled(Boolean.TRUE);
                                view.setVisibility(View.VISIBLE);
                            },
                            Throwable::printStackTrace);
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Exercise(
            exerciseNumber = "ex12-analytics",
            title = "Analytics",
            tips = "Evaluate event line list analytics including some of the values provided."
    )
    private Single<String> executeCode() {

        // Data elements
        String population = "HDRons6AfbL";
        String households = "VNM6zoPECqd";
        String householdsWithCases = "VOIXEuqBTBI";

        // Program indicators
        String density = "zmEg29pWBeV";
        String percentagePositive = "Z16GByeJtie";

        // Program stage
        String fociInvestigation = "CWaAcQYKVpq";

        //TODO: Complete your exercise here. Remove null and add the SDK query
        List<LineListResponse> response = null;

        String result = mapLineListResponseToString(response);
        return Single.just(result);
    }

    private String mapLineListResponseToString(List<LineListResponse> response) {
        if (response == null || response.isEmpty()) return "No results";

        StringBuilder builder = new StringBuilder();
        for (LineListResponse item : response) {
            builder.append("Date: ").append(item.getDate()).append("\n");
            for (LineListResponseValue value : item.getValues()) {
                builder.append(value.getDisplayName()).append(": ").append(value.getValue()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
