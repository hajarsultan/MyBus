package net.hajar.mybus;

import android.app.Application;

import com.backendless.Backendless;

/**
 * Created by tarekkma on 15/06/16.
 */
public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.initApp( this,
                BackendlessSeting.APPLICATION_ID, BackendlessSeting.ANDROID_SECRET_KEY, BackendlessSeting.VERSION );


    }
}
