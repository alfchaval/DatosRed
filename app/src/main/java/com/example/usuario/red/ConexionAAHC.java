package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by usuario on 11/7/17.
 */

public class ConexionAAHC extends AppCompatActivity implements View.OnClickListener {

    EditText edt_url;
    Button btn_conectar;
    WebView webview;
    TextView txv_tiempo;
    long inicio, fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_aahc);
        iniciar();
    }

    private void iniciar() {
        edt_url = (EditText) findViewById(R.id.edt_url);
        btn_conectar = (Button) findViewById(R.id.btn_conectar);
        btn_conectar.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        txv_tiempo = (TextView) findViewById(R.id.txv_tiempo);
    }

    @Override
    public void onClick(View view) {

        if(view == btn_conectar) {
            AAHC();;
        }
    }

    private void AAHC() {
        final String texto = edt_url.getText().toString();
        //final long inicio;
        //final long[] fin = new long[1];
        final ProgressDialog progreso = new ProgressDialog(ConexionAAHC.this);
        inicio = System.currentTimeMillis();
        RestClient.get(texto, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando...");
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
                fin = System.currentTimeMillis();
                webview.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                txv_tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                progreso.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                fin = System.currentTimeMillis();
                webview.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                txv_tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                progreso.dismiss();
            }
        });
    }
}
