package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by usuario on 11/7/17.
 */

public class ConexionVolley extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MyTag";
    EditText edt_url;
    Button btn_conectar;
    WebView webview;
    long inicio, fin;
    TextView txv_tiempo;

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_volley);
        iniciar();
    }

    private void iniciar() {
        edt_url = (EditText) findViewById(R.id.edt_direccion_archivo);
        btn_conectar = (Button) findViewById(R.id.btn_conectar);
        btn_conectar.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        txv_tiempo = (TextView) findViewById(R.id.txv_tiempo);
        mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void onClick(View view) {
        String url;
        if (view == btn_conectar) {
            url = edt_url.getText().toString();
            makeRequest(url);
        }
    }

    public void makeRequest(String url) {
        //mRequestQueue = Volley.newRequestQueue(this);
        final ProgressDialog progreso = new ProgressDialog(ConexionVolley.this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setCancelable(true);
        progreso.setMessage("Conectando . . .");
        progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mRequestQueue.cancelAll(TAG);
            }
        });
        progreso.show();
        inicio = System.currentTimeMillis();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fin = System.currentTimeMillis();
                        webview.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                        txv_tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                        progreso.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = "Error";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError)
                            mensaje = "Timeout Error: " + error.getMessage();
                        else {
                            NetworkResponse errorResponse = error.networkResponse;
                            if (errorResponse != null && errorResponse.data != null)
                                try {
                                    mensaje = "Error: " + errorResponse.statusCode + " " + "\n" + new
                                            String(errorResponse.data, "UTF-8");
                                    Log.e("Error", mensaje);
                                    webview.loadDataWithBaseURL(null, mensaje, "text/html", "UTF-8", null);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                        }
                        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }
                });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}