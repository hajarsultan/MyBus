package net.hajar.mybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        Backendless.initApp(this, BackendlessSeting.APPLICATION_ID, BackendlessSeting.ANDROID_SECRET_KEY, BackendlessSeting.VERSION);

        Button register = (Button) findViewById( R.id.regist );

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText Name = (EditText) findViewById( R.id.name );
                EditText E_mail = (EditText) findViewById( R.id.mail);
                EditText Passward = (EditText) findViewById( R.id.Passward);
                EditText Confirm = (EditText) findViewById( R.id.confirmPassward );
                EditText Phone= (EditText) findViewById(R.id.phone);


                CharSequence name = Name.getText();
                CharSequence email = E_mail.getText();
                CharSequence password = Passward.getText();
                CharSequence confirm = Confirm.getText();
                CharSequence phone=Phone.getText();

                if( isRegistrationValuesValid( name, email, password,confirm,phone ) )
                {
                    LoadingCallback<BackendlessUser> registrationCallback = createRegistrationCallback();
                    registrationCallback.showLoading();
                    registerUser(name.toString(), email.toString(), password.toString(), phone.toString(), registrationCallback);

                }


                   // Intent intent=new Intent(v.getContext(),LoginActivity.class);
                    // startActivityForResult(intent,0);
                }


});}

    public boolean isRegistrationValuesValid( CharSequence name, CharSequence email, CharSequence password,
                                              CharSequence confirm,CharSequence phone )
    {
        return  Validator.isNameValid(this, name)
                && Validator.isEmailValid(this, email)
                && Validator.isPasswordValid(this, password)
                && Validator.isphoneValid(this, phone)
                && isPasswordsMatch( password, confirm );

    }


    public boolean isPasswordsMatch( CharSequence password, CharSequence passwordConfirm )
    {
        if( !TextUtils.equals(password, passwordConfirm) )
        {
            Toast.makeText(this, "Passward don't Match", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    public void registerUser( String name, String email, String password,String phone,
                              AsyncCallback<BackendlessUser> registrationCallback )
    {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email );
        user.setPassword(password);
        user.setProperty("name", name);
        user.setProperty("Phone",phone);

        //Backendless handles password hashing by itself, so we don't need to send hash instead of plain text
        Backendless.UserService.register( user, registrationCallback );
    }

    /**
     * Creates a callback, containing actions to be executed on registration request result.
     * Shows a Toast with BackendlessUser's objectId on success,
     * show a dialog with an error message on failure.
     *
     * @return a callback, containing actions to be executed on registration request result
     */
    public LoadingCallback<BackendlessUser> createRegistrationCallback()
    {
        return new LoadingCallback<BackendlessUser>( this, "Sending Registration Requst" )
        {
            @Override
            public void handleResponse( BackendlessUser registeredUser )
            {
                super.handleResponse( registeredUser );
                Toast.makeText(RegistrationActivity.this, String.format("Regstration objectId: ", registeredUser.getObjectId()), Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Creates a listener, which proceeds with registration on button click.
     *
     * @return a listener, handling registration button click
     */

    }

