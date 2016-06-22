package net.hajar.mybus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MyBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bus);

        Button regist= (Button) findViewById(R.id.regist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        Button login= (Button) findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);

            }
        });


        ((Button)findViewById(R.id.testadd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buses bus = new Buses();
                bus.setBusline("Test");
                bus.setBusnumber("");
                bus.setEnd("END");
                bus.setStart("START");


                bus.saveAsync(new AsyncCallback<Buses>() {
                    @Override
                    public void handleResponse(Buses buses) {
                        Log.d("ADD BUS TEST", "handleResponse: OK");
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.e("ADD BUS TEST", "handleFault: " + backendlessFault.toString());
                    }
                });
            }
        });
    }


}
