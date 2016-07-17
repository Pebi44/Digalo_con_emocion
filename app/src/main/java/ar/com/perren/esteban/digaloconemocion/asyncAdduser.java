package ar.com.perren.esteban.digaloconemocion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;

/**
 * Created by Esteban on 13/06/2016.
 */
public class asyncAdduser extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    Activity activity;
    Context context;
    Httppostaux post;
    String IP_Server = "digaloconemocion.esy.es";//IP DE NUESTRO PC
    String URL_connect = "http://" + IP_Server + "/adduser.php";//ruta en donde estan nuestros archivos
    ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
    JSONArray jsonArray;
    String bandera, resultado;
String nombre, apellido,apodo, email, contraseña;
    protected Void doInBackground(Void... params) {

        post = new Httppostaux();
        postparameters2send.add(new BasicNameValuePair("nombre", nombre));
        postparameters2send.add(new BasicNameValuePair("apellido", apellido));
        postparameters2send.add(new BasicNameValuePair("apodo", apodo));
        postparameters2send.add(new BasicNameValuePair("email", email));
        postparameters2send.add(new BasicNameValuePair("contrasenia", contraseña));
        jsonArray = post.getserverdata(postparameters2send, URL_connect);
        return null;
    }
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Cargando usuario...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    protected void onPostExecute(Void v) {
        pDialog.dismiss();
        if (jsonArray != null) {
            try {
                bandera = jsonArray.getString(0);
                resultado = jsonArray.getString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Toast toast = Toast.makeText(context, resultado, Toast.LENGTH_LONG );
        toast.show();
        if(bandera.equals("true")){
            Intent menu = new Intent(context, MenuPrincipal.class);
            Log.e("Iniciando como: ",email );
            menu.putExtra("email",email);
            activity.finish();
            context.startActivity(menu);
            DBHelper db = new DBHelper(context);
            db.insertUser(email,contraseña,apodo);
        }
    }

    public asyncAdduser(Activity activity, Context context, String nombre, String apellido, String apodo, String email, String contraseña) {
        this.context = context;
        this.activity = activity;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apodo = apodo;
        this.email = email;
        this.contraseña = contraseña;
    }
}
