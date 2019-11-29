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

public class ActivityCambios extends Activity {
    EditText cajaNumControl, cajaNombre, cajaPrimerAp, cajaSegundoAp, cajaEdad, cajaSemestre, cajaCarrera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        cajaNumControl = findViewById(R.id.caja_num_control);
        cajaNombre = findViewById(R.id.caja_nombre_alumno);
        cajaPrimerAp = findViewById(R.id.caja_primer_ap);
        cajaSegundoAp = findViewById(R.id.caja_segundo_ap);
        cajaEdad = findViewById(R.id.caja_edad);
        cajaSemestre = findViewById(R.id.caja_semestre);
        cajaCarrera = findViewById(R.id.caja_carrera);
    }

    public void cambiarAlumno(View v){
        String nc = cajaNumControl.getText().toString();
        String n = cajaNombre.getText().toString();
        String pa = cajaPrimerAp.getText().toString();
        String sa = cajaSegundoAp.getText().toString();
        byte e = Byte.parseByte(cajaEdad.getText().toString());
        byte s = Byte.parseByte(cajaSemestre.getText().toString());
        String c = cajaCarrera.getText().toString();

        //Verificar conexion WIFI

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected()){
            //proceso para enviar peticion HTTP con la cadena JSON que contendra los datos del alumno
            new ModificarAlumno().execute(nc,n,pa,sa,String.valueOf(e),String.valueOf(s),c);
        }else{
            Toast.makeText(this, "Error Wi-Fi", Toast.LENGTH_LONG).show();
            Log.i("MSJ =", "Error WiFi");
        }
    }//Metodo modificar alumno

    class ModificarAlumno extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... args) {
            Map<String,String> mapDatos=new HashMap<String, String>();
            mapDatos.put("nc",args[0]);
            mapDatos.put("n",args[1]);
            mapDatos.put("pa",args[2]);
            mapDatos.put("sa",args[3]);
            mapDatos.put("e",args[4]);
            mapDatos.put("s",args[5]);
            mapDatos.put("c",args[6]);

            AnalizadorJSON analizador_json= new AnalizadorJSON();
            //url para forma local
            //si se quiere utilizar  el servidor proxmox se tiene que poner la direccion del servido y el puerto
            //10.0.2.2
            String url ="http://176.48.16.22/API_PHP_Android/API_PHP_Android/cambios_alumnos.php";
            String metodo="POST";

            JSONObject resultado = analizador_json.peticionHTTP(url,metodo,mapDatos);
            int r=0;
            try {
                r =resultado.getInt("exito");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (r==1){
                Log.i("Msj resultado", "REGISTRO MODIFICADO");
                //  Toast.makeText(getApplicationContext(),"ALUMNO AGREGADO ",Toast.LENGTH_LONG).show();

            }else
                Log.i("Msj resultado", "NO MODIFICADO ERROR");


            return null;
        }
    }
}
