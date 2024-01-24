package com.nacare.capture.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nacare.capture.R;
import com.nacare.capture.data.Sdk;
import com.nacare.capture.data.service.ActivityStarter;
import com.nacare.capture.ui.main.MainActivity;
import com.nacare.capture.ui.programs.ProgramsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private Disposable disposable;

    private TextInputEditText serverUrlEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;


    public static Intent getLoginActivityIntent(Context context) {
        return new Intent(context,LoginActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        serverUrlEditText = findViewById(R.id.serverUrlEdittext);
        usernameEditText = findViewById(R.id.usernameEdittext);
        passwordEditText = findViewById(R.id.passwordEdittext);
        loginButton = findViewById(R.id.loginButton);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getServerUrlError() != null) {
                serverUrlEditText.setError(getString(loginFormState.getServerUrlError()));
            }
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                if (Sdk.d2().programModule().programs().blockingCount() > 0) {
                    ActivityStarter.startActivity(this, ProgramsActivity.getProgramActivityIntent(this),true);
                } else {
                    ActivityStarter.startActivity(this, MainActivity.getMainActivityIntent(this),true);
                }
            }
            setResult(Activity.RESULT_OK);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(
                        serverUrlEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        serverUrlEditText.setText("http://45.79.116.38:8080/");
        serverUrlEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return false;
        });

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        loginButton.setVisibility(View.INVISIBLE);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String serverUrl = serverUrlEditText.getText().toString();

        disposable = loginViewModel
                .login(username, password, serverUrl)
                .doOnTerminate(() -> loginButton.setVisibility(View.VISIBLE))
                .subscribe(u -> {}, t -> {});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
