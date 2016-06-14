package net.hajar.mybus;

import android.app.ProgressDialog;
import android.content.Context;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by ENG MUHAMED on 19/04/2016.
 */

public class LoadingCallback<T> implements AsyncCallback<T> {
    private  Context context;
    private ProgressDialog progres;

    public LoadingCallback( Context context )
    {
        this(context,"Loading...");
    }

    public LoadingCallback(Context context, String loadingMessage) {
        this.context = context;
        progres = new ProgressDialog( context );
        progres.setMessage(loadingMessage);
    }


    @Override
    public void handleResponse(T response) {progres.dismiss();

    }

    @Override
    public void handleFault(BackendlessFault Fault) {
        progres.dismiss();
        DialogHelper.createErrorDialog( context, "BackendlessFault", Fault.getMessage() ).show();

    }

    public void showLoading()
    {
        progres.show();
    }

    public void hideLoading()
    {
        progres.dismiss();
    }
}
