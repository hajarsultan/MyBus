package net.hajar.mybus;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by ENG MUHAMED on 19/04/2016.
 */
public class DialogHelper {

    public static AlertDialog createErrorDialog( Context context, String title, String message )
    {
        return new AlertDialog.Builder( context )
                .setTitle( title )
                .setMessage( message )
                .setIcon( android.R.drawable.ic_dialog_alert )
                .create();
    }
}
