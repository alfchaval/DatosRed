package com.example.usuario.red;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.internal.http2.Header;

/**
 * Created by usuario on 11/7/17.
 */

public class Descarga extends Activity {

    EditText edtUrl;
    Button btnImagen, btnFichero;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_imagenes);
        edtUrl = (EditText) findViewById(R.id.edtUrl);
        btnImagen = (Button) findViewById(R.id.btnimagen);
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = edtUrl.getText().toString();
                descargaImagen(url);

            }
        });
        btnFichero = (Button) findViewById(R.id.btnFichero);
        btnFichero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = edtUrl.getText().toString();
                descargaFichero(url);
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void descargaImagen(String url) {
        /*
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imageView);
        */

        OkHttpClient client = new OkHttpClient();

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imageView);
    }

    private void descargaFichero(String url) {
        File archivo = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(archivo) {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                Toast.makeText(Descarga.this, "Archivo descargado en: " + file.getAbsolutePath(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                Toast.makeText(Descarga.this, "Error descargando fichero",Toast.LENGTH_LONG).show();
            }
        });
    }
}
