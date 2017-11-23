package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by usuario on 11/7/17.
 */

public class SubidaFicheros extends AppCompatActivity {

    public final static String WEB = "https://alumno.mobi/~alumno/superior/chamorro/upload.php";

    EditText edt_direccion_archivo;
    Button btn_subir;
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida);
        iniciar();
    }

    private  void iniciar() {
        edt_direccion_archivo = (EditText) findViewById(R.id.edt_direccion_archivo);
        btn_subir = (Button) findViewById(R.id.btn_subir);
        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subida();
            }
        });
        webview = (WebView) findViewById(R.id.webview);
    }

    private void subida() {
        String fichero = edt_direccion_archivo.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(SubidaFicheros.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            progreso.setMessage("Error en el fichero: " + e.getMessage());
        }
        if (existe) {
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    //progreso.setCancelable(false);
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    // called when response HTTP status is "200 OK"
                    progreso.dismiss();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progreso.dismiss();
                }
            });
        }
    }
}
