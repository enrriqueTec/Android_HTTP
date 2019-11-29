package com.controlador;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class AnalizadorJSON {
    InputStream is = null;
    JSONObject jsonObject = null;
    String json = null;
    OutputStream os = null;

    //Metodo para Altas, Bajas, Cambios

    public JSONObject peticionHTTP(String url, String metodo, Map datos){
        HttpURLConnection conexion = null;
        URL mUrl  = null;

        String cadenaJSON = null;
        try {
            cadenaJSON = "{\"nc\":\"" + URLEncoder.encode(String.valueOf(datos.get("nc")),"UTF-8") +
                    "\", \"n\":\"" + URLEncoder.encode(String.valueOf(datos.get("n")),"UTF-8")+
                    "\", \"pa\":\"" + URLEncoder.encode(String.valueOf(datos.get("pa")),"UTF-8")+
                    "\", \"sa\":\"" + URLEncoder.encode(String.valueOf(datos.get("sa")),"UTF-8")
                    + "\", \"e\":\"" + URLEncoder.encode(String.valueOf(datos.get("e")),"UTF-8")+
                    "\", \"s\":\"" + URLEncoder.encode(String.valueOf(datos.get("s")),"UTF-8")+
                    "\", \"c\":\"" + URLEncoder.encode(String.valueOf(datos.get("c")),"UTF-8")+"\"}";
            Log.d("MSJ", cadenaJSON);

            mUrl = new URL(url);

            conexion = (HttpURLConnection) mUrl.openConnection();
            conexion.setDoOutput(true);
            conexion.setDoInput(true);
            conexion.setRequestMethod(metodo);

            conexion.setFixedLengthStreamingMode(cadenaJSON.getBytes().length);

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = new BufferedOutputStream(conexion.getOutputStream());

            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("MSJ ===", "Error de CODIFICACIÓN");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("MSJ ===", "Error en la URL");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MSJ ===", "Error en la CONEXION");
        }

        //Si no se genera una excepcion continua este codigo

        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();

            String linea;

            while((linea = br.readLine()) != null){
                cadena.append(linea + "\n");
            }

            is.close();

            json = cadena.toString();

            Log.i("Mensaje 1 >>>>>>", "RESPUESTA JSON: " + json);

            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MSJ===", "Error en la respuesta del servidor");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MSJ===", "Error en la creacion de objeto JSON");
        }
        return jsonObject;
    }


    public JSONObject consultaHTTP(String url){
        HttpURLConnection conexion=null;
        URL miurl =null;
        //OutputStream os;
        String cadenaJSON= null;


        try {
            miurl= new URL(url);
            conexion= (HttpURLConnection) miurl.openConnection();
            //activamos el envio de datos atraves de POST
            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");

            // conexion.setFixedLengthStreamingMode(cadenaJSON.getBytes().length);

            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            os=new BufferedOutputStream(conexion.getOutputStream());
            os.flush();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is=new BufferedInputStream(conexion.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder cadena= new StringBuilder();
            String linea;

            while ((linea=br.readLine())!=null){
                cadena.append(linea+"\n");
            }


            is.close();

            json=cadena.toString();
            Log.i("Mensaje uno >>> ","RESPUESTA JSON: "+json);

            jsonObject= new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }//metodo para peticion HTTP

    public JSONObject buscarHTTP(String url, String campo, String valor) {
        HttpURLConnection conexion = null;
        URL mUrl = null;

        //magia
        String cadenaJSON = null;
        try {
            cadenaJSON = "{\""+URLEncoder.encode("campo", "UTF-8")+"\":\""+URLEncoder.encode(campo, "UTF-8")+"\"," +
                    "\""+URLEncoder.encode("valor", "UTF-8")+"\":\""+URLEncoder.encode(valor, "UTF-8")+"\"}";
            Log.i("Cadena jeson busqueda: ",cadenaJSON);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            mUrl = new URL(url);
            conexion = (HttpURLConnection) mUrl.openConnection();

            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");

            conexion.setFixedLengthStreamingMode(cadenaJSON.length());

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = new BufferedOutputStream(conexion.getOutputStream());

            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();

            String linea;

            while ((linea = br.readLine()) != null){
                cadena.append(linea+"\n");
            }
            is.close();

            json = cadena.toString();



            Log.i("Mensaje 1 >>>>>", "Respuesta JSON: " + json);

            jsonObject = new JSONObject(json);
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    public JSONObject loginHTTP(String url, String u, String c) {
        HttpURLConnection conexion = null;
        URL mUrl = null;

        //magia
        String cadenaJSON = null;
        try {
            cadenaJSON = "{\""+URLEncoder.encode("usuario", "UTF-8")+"\":\""+URLEncoder.encode(u, "UTF-8")+"\"," +
                    "\""+URLEncoder.encode("clave", "UTF-8")+"\":\""+URLEncoder.encode(c, "UTF-8")+"\"}";
            Log.i("Cadena jeson busqueda: ",cadenaJSON);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            mUrl = new URL(url);
            conexion = (HttpURLConnection) mUrl.openConnection();

            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");

            conexion.setFixedLengthStreamingMode(cadenaJSON.length());

            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            os = new BufferedOutputStream(conexion.getOutputStream());

            os.write(cadenaJSON.getBytes());
            os.flush();
            os.close();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            is = new BufferedInputStream(conexion.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder cadena = new StringBuilder();

            String linea;

            while ((linea = br.readLine()) != null){
                cadena.append(linea+"\n");
            }
            is.close();

            json = cadena.toString();

            jsonObject = new JSONObject(json);
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

}

