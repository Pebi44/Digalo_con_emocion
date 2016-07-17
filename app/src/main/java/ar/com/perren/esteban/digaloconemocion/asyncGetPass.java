package ar.com.perren.esteban.digaloconemocion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;

/**
 * Created by Esteban on 05/07/2016.
 */
public class asyncGetPass extends AsyncTask<Void, Void, String> {
    private ProgressDialog pDialog;

    Context context;
    Httppostaux post;
    String IP_Server = "digaloconemocion.esy.es";//IP DE NUESTRO PC
    String URL_connect = "http://" + IP_Server + "/mail.php";//ruta en donde estan nuestros archivos
    String email="";
    @Override
    protected String doInBackground(Void... params) {
        post = new Httppostaux();

        ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("email",email));
        JSONArray jdata =  post.getserverdata(postparameters2send, URL_connect);
        JSONObject json_data = null; //creamos un objeto JSON
String resultado="";
        if (jdata != null && jdata.length()>0) {
            try {
                json_data=jdata.getJSONObject(0);
                resultado=json_data.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String resultado=s;
        Toast toast1 = Toast.makeText(context,resultado, Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER, 0, 0);

        toast1.show();
    }

    public asyncGetPass(Context context, String email) {
        this.context = context;
        this.email = email;
    }
}
