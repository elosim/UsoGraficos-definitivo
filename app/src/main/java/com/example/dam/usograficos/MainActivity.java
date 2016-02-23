package com.example.dam.usograficos;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_GET = 1;
    private Vista v;
    private RelativeLayout l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(null);
        setContentView(R.layout.activity_main);

        v = new Vista(this);
        l = (RelativeLayout) findViewById(R.id.lay);
        l.addView(v);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==REQUEST_IMAGE_GET){
            Uri uri = data.getData();
            if(uri!=null){
                File f = new File(getImagePath(uri));
                v.importarN(f);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.Circulo) {
            v.circulo();
            return true;
        }
        if (id == R.id.linea) {
            v.linea();
            return true;
        }
        if (id == R.id.rectangulo) {
            v.rectangulo();
            return true;
        }
        if(id == R.id.linearecta){
            v.lineaRecta();
            return true;
        }
        if (id == R.id.solobordes){
            v.soloBordes();
        }
        if (id == R.id.nueva){
            v.nueva();
        }
        if (id == R.id.importar){
            buscarFoto();
        }
        if (id == R.id.colorpicker){
            colorPicker();
        }
        if (id == R.id.exportar){
            v.exportar();
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscarFoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    public void colorPicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setFastChooser(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListner(int position, int color) {

                colorPicker.dismissDialog();
                v.changeColor(color);
            }
        }).setColumns(5).show();
    }

    public String getImagePath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }



}
