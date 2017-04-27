package com.play.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.play.sdk.Encrypt;
import com.play.sdk.PlaySDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaySDK.getInstance().initSDK(this,"10000",true);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        String[] str = new String[2];
        str[0] = "Tobin";
        str[1] = "Jni";

        String str1 = Encrypt.encrypt(this ,str);
        tv.setText("Tobin Jni encrypt: " + str1);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

}
