package net.hajar.mybus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

public class LoginActivity extends AppCompatActivity {
   final int  REGISTER_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Backendless.initApp(this, BackendlessSeting.APPLICATION_ID, BackendlessSeting.ANDROID_SECRET_KEY, BackendlessSeting.VERSION);

        Button login = (Button) findViewById(R.id.Log);
        final EditText Email = (EditText) findViewById(R.id.e_mailmaile);
        final EditText passward = (EditText) findViewById(R.id.passwarD);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence email = Email.getText();
                CharSequence Passward = passward.getText();

                if (isLoginValuesValid(email, Passward)) {
                    LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

                    loginCallback.showLoading();
                    loginUser(email.toString(), Passward.toString(), loginCallback);


                }
                Intent loginIntent = new Intent(v.getContext(), CompanyActivity.class);
                startActivityForResult(loginIntent,0);

            }
        });




    }

    public void loginUser(String email, String password, AsyncCallback<BackendlessUser> loginCallback) {
        Backendless.UserService.login(email, password, loginCallback);
    }

    public boolean isLoginValuesValid(CharSequence email, CharSequence password) {
        return Validator.isEmailValid(this, email) && Validator.isPasswordValid(this, password);

    }


    public LoadingCallback<BackendlessUser> createLoginCallback() {
        return new LoadingCallback<BackendlessUser>(this, getString(R.string.login)) {
            @Override
            public void handleResponse(BackendlessUser loggedInUser) {
                super.handleResponse(loggedInUser);
                Toast.makeText(LoginActivity.this, String.format("login id:", loggedInUser.getObjectId()), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK )
        {
            switch( requestCode )
            {
                case REGISTER_REQUEST_CODE:
                    String email = data.getStringExtra( BackendlessUser.EMAIL_KEY );
                    EditText emailField = (EditText) findViewById( R.id.e_mailmaile );
                    emailField.setText( email );

                    EditText passwordField = (EditText) findViewById( R.id.passwarD );
                    passwordField.requestFocus();

                    Toast.makeText( this, "user", Toast.LENGTH_SHORT ).show();
            }
        }
    }
}
