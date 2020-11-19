package com.pedroc.appmadhacks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oracle.bmc.Region;

public class ConfigActivity extends AppCompatActivity {

    EditText edtApiGateway;
    Button btnConfigurar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        edtApiGateway = (EditText) findViewById(R.id.edtApiGatewayURL);
        btnConfigurar = (Button) findViewById(R.id.btnConfigurar);

        btnConfigurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigFile.API_GATEWAY_URL = edtApiGateway.getText().toString();
                Toast.makeText(getApplicationContext(), "Configurado !!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }
}