package com.example.android.androidskeletonapp.ui.splash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.androidskeletonapp.R;
import com.example.android.androidskeletonapp.data.Sdk;
import com.example.android.androidskeletonapp.data.service.ActivityStarter;
import com.example.android.androidskeletonapp.ui.login.LoginActivity;
import com.example.android.androidskeletonapp.ui.main.MainActivity;
import com.facebook.stetho.Stetho;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.D2Configuration;
import org.hisp.dhis.android.core.D2Manager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private final static boolean DEBUG = true;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        D2Configuration configuration = Sdk.getD2Configuration(this);

        if (configuration == null) return;

        disposable = D2Manager.instantiateD2(configuration)
                .flatMap(d2 -> d2.userModule().isLogged())
                .doOnSuccess(isLogged -> {
                    if (isLogged) {
                        ActivityStarter.startActivity(this, MainActivity.getMainActivityIntent(this),true);
                    } else {
                        ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
                    }
                }).doOnError(throwable -> {
                    throwable.printStackTrace();
                    ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void initializeBlocking() {
        try {
            D2Configuration configuration = Sdk.getD2Configuration(this);

            D2 d2 = D2Manager.blockingInstantiateD2(configuration);

            if(d2.userModule().blockingIsLogged()) {
                ActivityStarter.startActivity(this, MainActivity.getMainActivityIntent(this),true);
            } else {
                ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            ActivityStarter.startActivity(this, LoginActivity.getLoginActivityIntent(this),true);
        }

    }
}