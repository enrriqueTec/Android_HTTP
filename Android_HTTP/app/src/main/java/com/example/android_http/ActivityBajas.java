package com.example.android_http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.controlador.AnalizadorJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityBajas extends Activity {
    EditText cajaNumControl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        cajaNumControl = findViewById(R.id.caja_num_control);
    }

    public void eliminarAlumno(View v){
        String nc = cajaNumControl.getText().toString();

        //Verificar conexion WIFI

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()){
            //proceso para enviar peticion HTTP con la cadena JSON que contendra los datos del alumno
            new EliminarAlumno().execute(nc);
        }else{
            Toast.makeText(this, "Error Wi-Fi", Toast.LENGTH_LONG).show();
            Log.i("MSJ =", "Error WiFi");
        }
    }//Metodo eliminar alumno

    class EliminarAlumno extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... args) {
            Map<String,String> mapDatos=new HashMap<String, String>();
            mapDatos.put("nc",args[0]);

            AnalizadorJSON analizador_json= new AnalizadorJSON();
            //url para forma local
            //si se quiere utilizar  el servidor proxmox se tiene que poner la direccion del servido y el puerto
            //10.0.2.2
            String url ="http://176.48.16.22/API_PHP_Android/bajas_alumnos.php";
            String metodo="POST";

            JSONObject resultado = analizador_json.peticionHTTP(url,metodo,mapDatos);
            int r=0;
            try {
                r =resultado.getInt("exito");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (r==1){
                Log.i("Msj resultado", "REGISTRO ELIMINADO");
                //  Toast.makeText(getApplicationContext(),"ALUMNO ELIMINADO ",Toast.LENGTH_LONG).show();

            }else
                Log.i("Msj resultado", "NO ELIMINADO ERROR");


            return null;
        }
    }
}
