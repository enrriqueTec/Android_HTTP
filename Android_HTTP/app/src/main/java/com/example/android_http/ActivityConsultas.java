package com.example.android_http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.controlador.AnalizadorJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ActivityConsultas extends Activity {
    ListView lsAlumnos;
    Spinner carreras;
    RadioButton rbNc;
    RadioButton rbN;
    RadioButton rbPa;
    EditText txtConsulta;
    ArrayList<String> lista = new ArrayList<>();
    AnalizadorJSON json = new AnalizadorJSON();
    ArrayAdapter<String> adaptador;
    String campo = "num_control";
    String valor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        rbNc = findViewById(R.id.rbNc);
        rbN = findViewById(R.id.rbN);
        rbPa = findViewById(R.id.rbPa);
        txtConsulta = findViewById(R.id.txtConsultas);
        lsAlumnos = findViewById(R.id.lsConsulta);
        carreras=findViewById(R.id.spinnerCarreara);

        new MostrarAlumnos().execute();
        adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lista);
        lsAlumnos.setAdapter(adaptador);


        carreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                valor=String.valueOf(carreras.getItemAtPosition(position));
                campo= "carrera";
                try {
                    //String valor = txtConsulta.getText().toString();
                    if ( valor !="" ){
                        lista = new BuscarAlumno().execute(campo,valor).get();
                        if (!lista.isEmpty()){
                            adaptador = new ArrayAdapter<>(ActivityConsultas.this,android.R.layout.simple_list_item_1,lista);
                            lsAlumnos.setAdapter(adaptador);

                        }else{
                            Toast.makeText(ActivityConsultas.this,"No hay registros",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        new MostrarAlumnos().execute();
                        adaptador = new ArrayAdapter<>(ActivityConsultas.this,android.R.layout.simple_list_item_1,lista);
                        lsAlumnos.setAdapter(adaptador);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        rbPa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "primerAp";
                }
            }
        });
        rbN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "Nombre";
                }
            }
        });
        rbNc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    campo = "numControl";
                }
            }
        });

        txtConsulta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    //String valor = txtConsulta.getText().toString();
                    if ( valor !="" ){
                        lista = new BuscarAlumno().execute(campo,valor).get();
                        if (!lista.isEmpty()){
                            adaptador = new ArrayAdapter<>(ActivityConsultas.this,android.R.layout.simple_list_item_1,lista);
                            lsAlumnos.setAdapter(adaptador);

                        }else{
                            Toast.makeText(ActivityConsultas.this,"No hay registros",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        new MostrarAlumnos().execute();
                        adaptador = new ArrayAdapter<>(ActivityConsultas.this,android.R.layout.simple_list_item_1,lista);
                        lsAlumnos.setAdapter(adaptador);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class MostrarAlumnos extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            lista.clear();

            String url = "http://176.48.16.22/API_PHP_Android/consultas_alumnos.php";

            JSONObject jsonObject= json.consultaHTTP(url);

            try {
                JSONArray jesonArray = jsonObject.getJSONArray("alumnos");
                String cadena = "";
                for (int i = 0; i < jesonArray.length(); i++){
                    cadena = jesonArray.getJSONObject(i).getString("nc") + "|" +
                            jesonArray.getJSONObject(i).getString("n") + "|" +
                            jesonArray.getJSONObject(i).getString("pa") + "|" +
                            jesonArray.getJSONObject(i).getString("sa") + "|" +
                            jesonArray.getJSONObject(i).getString("e") + " |" +
                            jesonArray.getJSONObject(i).getString("s") + " |" +
                            jesonArray.getJSONObject(i).getString("c");

                    lista.add(cadena);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class BuscarAlumno extends AsyncTask<String, String, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> lista =  new ArrayList<>(); ;


            String url = "http://176.48.16.22/API_PHP_Android/po.php";
            JSONObject jsonObject = json.buscarHTTP(url,strings[0],strings[1]);


            try {
                if (jsonObject.getInt("exito") == 1){

                    JSONArray jesonArray = jsonObject.getJSONArray("alumnos");

                    String cadena = "";
                    for (int i = 0; i < jesonArray.length(); i++){
                        cadena = jesonArray.getJSONObject(i).getString("nc") + " " +
                                jesonArray.getJSONObject(i).getString("n") + " " +
                                jesonArray.getJSONObject(i).getString("pa") + " " +
                                jesonArray.getJSONObject(i).getString("sa") + " " +
                                jesonArray.getJSONObject(i).getString("e") + " " +
                                jesonArray.getJSONObject(i).getString("s") + " " +
                                jesonArray.getJSONObject(i).getString("c");

                        lista.add(cadena);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return lista;
        }
    }
}