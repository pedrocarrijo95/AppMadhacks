package com.pedroc.appmadhacks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Supplier;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.*;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;

import org.apache.commons.io.FileUtils;


public class MainActivity extends AppCompatActivity {
    ImageView imgFoto;
    AuthenticationDetailsProvider provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgFoto = findViewById(R.id.imgFoto);;


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, 100);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET
            }, 100);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }


        FloatingActionButton fabCam = findViewById(R.id.fabCam);
        FloatingActionButton fabGaleria = findViewById(R.id.fabGaleria);

        //Cam
        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,100);
                Snackbar.make(view, "Abrindo a câmera", Snackbar.LENGTH_LONG)
                        .setAction("Executando", null).show();
            }
        });

        //Gallery
        fabGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Selecione a imagem"), 101);
                Snackbar.make(view, "Abrindo a galeria", Snackbar.LENGTH_LONG)
                        .setAction("Executando", null).show();
            }
        });
    }

    public void AuthOCI(){
        try {
            //Get privatekey from assets and create a copy inside User`s device ExternalStorage
            InputStream inputStream = getApplicationContext().getAssets().open(ConfigFile.PRIVATEKEY);
            FileUtils.copyToFile(inputStream, new File(Environment.getExternalStorageDirectory() + File.separator + ConfigFile.PRIVATEKEY));
            String newPath = Environment.getExternalStorageDirectory() + File.separator + ConfigFile.PRIVATEKEY;
            Supplier<InputStream> privateKeySupplier = new SimplePrivateKeySupplier(newPath);

            provider = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(ConfigFile.TENANT_ID)
                    .userId(ConfigFile.USER_ID)
                    .fingerprint(ConfigFile.FINGERPRINT)
                    .privateKeySupplier(privateKeySupplier)
                    .build();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void putObjectStorage(File f){
        try {
            //Authentication OCI
            AuthOCI();
            //Create file and put inside ObjectStorage
            InputStream inputObject = new FileInputStream(f);
            ObjectStorage objectStorageClient = new ObjectStorageClient(provider);
            objectStorageClient.setRegion(ConfigFile.REGION);
            PutObjectResponse response = objectStorageClient.putObject(PutObjectRequest.builder().namespaceName(ConfigFile.NAMESPACE_NAME).bucketName(ConfigFile.BUCKET_NAME).objectName(ConfigFile.OBJECT_NAME).putObjectBody(inputObject).build());
            System.out.println(response.getOpcRequestId());
        }catch(BmcException | IOException e){
            e.printStackTrace();
        }
    }


    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*2, bitmap.getHeight()*2, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            //Camera
            if(requestCode == 100  && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgFoto.setImageBitmap(bitmap);
                File foto = bitmapToFile(MainActivity.this,bitmap,ConfigFile.NOME_FOTO);
                putObjectStorage(foto);
            }
            //Galeria
            if(requestCode == 101 && resultCode == RESULT_OK){
                Uri uri = data.getData();
                imgFoto.setImageURI(uri);
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                File foto = bitmapToFile(MainActivity.this,bitmap,ConfigFile.NOME_FOTO);
                putObjectStorage(foto);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}