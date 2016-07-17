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
 * Created by Esteban on 17/04/2016.
 */
public class asyncGetPregunta extends AsyncTask<Void, Void, Void> {

    private ProgressDialog pDialog;

    Context context;
    Httppostaux post;
    String IP_Server = "digaloconemocion.esy.es";//IP DE NUESTRO PC
    String URL_connect = "http://" + IP_Server + "/getallPreguntas.php";//ruta en donde estan nuestros archivos


    @Override
    protected Void doInBackground(Void... params) {
        DBHelper db = new DBHelper(context);
        post = new Httppostaux();
        int lastid = db.getLastId();
        ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("id", Integer.toString(lastid)));
//RECUPERAMOS LOS DATOS DEVUELTOS POR EL SERVIDOR - ES UN JSONARRAY -
        JSONArray jdata =  post.getserverdata(postparameters2send, URL_connect);
        JSONObject json_data = null; //creamos un objeto JSON
//VERIFICAMOS QUE EL JSONARRAY NO ESTE VACIO O NULO
        if (jdata != null) {
            for (int i = 0; i < jdata.length(); i++) {
                try {
                    json_data = jdata.getJSONObject(i);
                    Pregunta_Respuesta pregunta_respuesta = new Pregunta_Respuesta(json_data);
                    Log.e("cargandopregunta: ", json_data.toString() );
                    db.insert(pregunta_respuesta);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Cargando datos...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void onPostExecute(Void v) {
        pDialog.dismiss();
    }
    public asyncGetPregunta(Context context) {
        this.context = context;
     }


}