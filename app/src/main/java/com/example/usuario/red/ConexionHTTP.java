package com.example.usuario.red;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by usuario on 11/7/17.
 */

public class ConexionHTTP extends AppCompatActivity implements View.OnClickListener {

    EditText edt_url;
    RadioButton rbtn_java, rbtn_apache;
    Button btn_conectar;
    WebView webview;
    TextView txv_tiempo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_http);
        iniciar();
    }

    private void iniciar() {
        edt_url = (EditText) findViewById(R.id.edt_url);
        rbtn_java = (RadioButton) findViewById(R.id.rbtn_java);
        rbtn_apache = (RadioButton) findViewById(R.id.rbtn_apache);
        btn_conectar = (Button) findViewById(R.id.btn_conectar);
        btn_conectar.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        txv_tiempo = (TextView) findViewById(R.id.txv_tiempo);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void onClick(View v) {
        String texto = edt_url.getText().toString();
        long inicio, fin;
        Resultado resultado;
        if (v == btn_conectar) {
            inicio = System.currentTimeMillis();
            if (rbtn_java.isChecked()) {
                resultado = Conexion.conectarJava(texto);
            }
            else {
                resultado = Conexion.conectarApache(texto);
            }
            fin = System.currentTimeMillis();
            if (resultado.getCodigo()){
                webview.loadDataWithBaseURL(null, resultado.getContenido(),"text/html", "UTF-8", null);
            }
            else {
                webview.loadDataWithBaseURL(null, resultado.getMensaje(), "text/html", "UTF-8", null);
            }
            txv_tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }
    }
}
