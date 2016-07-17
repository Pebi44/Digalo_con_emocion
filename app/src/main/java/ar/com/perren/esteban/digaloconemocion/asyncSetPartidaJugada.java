package ar.com.perren.esteban.digaloconemocion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;


/**
 * Created by Esteban on 25/04/2016.
 */
public class asyncSetPartidaJugada extends AsyncTask<Void, Void, Void> {
    private String email, idpartida, puntaje;
    private Httppostaux post;
    private String IP_Server = "digaloconemocion.esy.es";//IP DE NUESTRO PC
    private String URL_connect = "http://" + IP_Server + "/partidasjugadas.php";//ruta en donde estan nuestros archivos
    private InternetConnectionService online;
    private Context context;
    private boolean bandera;
    private DBHelper db;
    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("email", email));
        postparameters2send.add(new BasicNameValuePair("idpartida", idpartida));
            postparameters2send.add(new BasicNameValuePair("puntaje", puntaje));
        Log.e("CargandoPartida: ", email + idpartida + puntaje);
        if(Funciones.isOnline(context)) {
                post = new Httppostaux();
                post.getserverdata(postparameters2send, URL_connect);
            }

        return null;
    }
    protected void onPreExecute() {

    }


    protected void onPostExecute(Void v) {
    bandera=true;
    }

    public boolean getBandera(){
    return bandera;
}

        public asyncSetPartidaJugada(Context context, String email, String idpartida, String puntaje) {
        this.bandera= false;
        this.email = email;
        this.idpartida = idpartida;
        this.puntaje = puntaje;
        this.context = context;
        }
}
