package com.pedroc.appmadhacks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pedroc.appmadhacks.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPass = (EditText) findViewById(R.id.edtPass);

        btnLogar = (Button) findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String user = edtUser.getText().toString();
                    String pass = edtPass.getText().toString();
                    String token = Logar(user,pass);

                    if(token != null){
                        Toast.makeText(getApplicationContext(),"Logado: "+token,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
                    }

            }
        });


    }

    public String Logar(String user, String pass) {
        String access_token = "";
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post("https://idcs-93d02671fcb2467eadd55f2f6687c74a.identity.oraclecloud.com/oauth2/v1/token")
                    .header("Authorization", "Basic MjA4N2I2NzdkOTM4NGFjNjg0YmRmZGE4YTgwZDVkNGQ6ZDBjOTFkMjUtOTFjOS00MGE1LWE5OTEtOTU0MjA1YTZlOWNi")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("grant_type", "password")
                    .field("scope", "danuviusprojectdanuviusapi")
                    .field("username", user) //ex: pedroegcj@hotmail.com
                    .field("password", pass) //ex: OracleCloud123!
                    .asString();
            JSONObject jsonObject = new JSONObject(response.getBody());
            access_token = jsonObject.getString("access_token");

        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return access_token;
    }

}