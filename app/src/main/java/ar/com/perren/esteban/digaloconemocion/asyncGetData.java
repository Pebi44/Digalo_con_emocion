package ar.com.perren.esteban.digaloconemocion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;

/**
 * Created by Esteban on 27/06/2016.
 */
public class asyncGetData extends AsyncTask<Void, Void, JSONArray> {
    Context context;
    MenuPrincipal menuPrincipal;
    private ProgressDialog pDialog;
    Httppostaux post;
    String IP_Server = "digaloconemocion.esy.es";//IP DE NUESTRO PC
    String URL_connect1 = "http://" + IP_Server + "/getPosition.php";//ruta en donde estan nuestros archivos
    String URL_connect2 = "http://" + IP_Server + "/getTablaPosition.php";//ruta en donde estan nuestros archivos
    String email, accion="";
    Boolean cargando=false;

    protected void onPreExecute() {
        cargando=true;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Cargando datos...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    @Override
    protected JSONArray doInBackground(Void... params) {
        DBHelper db = new DBHelper(context);
        JSONArray jdata = new JSONArray();
        post = new Httppostaux();
        JSONObject json_data = null; //creamos un objeto JSON
        //VERIFICAMOS SI HAY INTERNET

        if (Funciones.isOnline(context)) {

        if(accion.equals("tabla")) {

//CONECTAMOS AL SERVIDOR Y DESCARGAMOS LA TABLA DE POSICIONES(LOS PRIMEROS 10 LUGARES) Y LA GUARDAMOS
                 jdata = post.getserverdata(URL_connect2);
//VERIFICAMOS QUE EL JSONARRAY NO ESTE VACIO O NULO
                if (jdata != null) {
                    for (int i = 0; i < jdata.length(); i++) {
                        try {
                            json_data = jdata.getJSONObject(i);
                            db.insert(json_data.getInt("Posicion"), json_data.getString("Apodo"), json_data.getInt("Puntaje_Total"), json_data.getInt("Cant_Part_Jugadas"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (accion.equals("posicion")) {

                //SE CONECTA CON EL SERVIDOR Y SE DESCARGA LA POSICION DEL USUARIO
                ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
                postparameters2send.add(new BasicNameValuePair("email", email));
                Log.e("getdata: ", email);
                jdata = post.getserverdata(postparameters2send, URL_connect1);

            }
        }





        return jdata;
    }
    protected void onPostExecute(JSONArray jdata2) {
        cargando = false;
        pDialog.dismiss();

        Log.e("onPostExecute getdata: ","entre" );
        if (accion.equals("posicion")) {

            JSONObject json_data2 = null; //creamos un objeto JSON
//VERIFICAMOS QUE EL JSONARRAY NO ESTE VACIO O NULO
            if (jdata2 != null && !jdata2.isNull(0) && jdata2.length() != 0) {
                try {
                    json_data2 = jdata2.getJSONObject(0);
                    menuPrincipal.cargarDatos(json_data2.getString("Posicion"), json_data2.getString("Puntaje_Total"), json_data2.getString("Cant_Part_Jugadas"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        Log.e("onPostExecute getdata: ","estoy saliendo" );
        Log.e("onPostExecute getdata: ","sali" );


    }
public Boolean getBandera(){
    return cargando;
}
    public asyncGetData(Context context, MenuPrincipal menuPrincipal, String email, String accion) {
        this.context = context;
        this.menuPrincipal = menuPrincipal;
        this.email = email;
        this.accion = accion;
    }
}
