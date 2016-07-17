package ar.com.perren.esteban.digaloconemocion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class InternetConnectionService extends Service {
    Context context;
    static cargarPendientes pendientes;
   DBHelper dbHelper;
   JSONObject json;
    private static boolean banderaCargado;
    public InternetConnectionService(){
    }
    public void onCreate(){
        banderaCargado=false;
        super.onCreate();
      context=this;
        dbHelper= new DBHelper(context);
        //VERIFICO SI HAY REGISTROS PENDIENTES
            pendientes = new cargarPendientes(context);
pendientes.start();

        Log.w("TAG", "ScreenListenerService---OnCreate ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
private class cargarPendientes extends Thread {
    private static final long DELAY = 300000;
    private Context context;
    private JSONArray jsonArray = new JSONArray();
    String email, idpartida, puntaje;
    public void run() {
asyncSetPartidaJugada setpartida;
while(true) {
    try {
        //RECUPERO LOS REGISTROS DE LA BASE DE DATOS
        jsonArray = dbHelper.getAll();
//SI HAY REGISTROS POR CARGAR Y HAY CONEXION A INTERNET
    if (jsonArray.length()!=0 & Funciones.isOnline(context)){
        //CAMBIO EL VALOR DE LA BANDERA "CARGANDO" A TRUE
        banderaCargado=true;
        for(int i = 0 ; i<jsonArray.length(); i++){
            try {
                json=jsonArray.getJSONObject(i);
                email=json.getString("email");
                idpartida=json.getString("idpartida");
                puntaje=json.getString("puntaje");
                //LLAMO A LA CLASE QUE SE ENCARGA DE CARGAR LOS REGISTROS
                setpartida = new asyncSetPartidaJugada(context,email,idpartida,puntaje);
                setpartida.execute();
               //WHILE HASTA QUE SE TERMINE DE CARGAR LOS REGISTROS
                while(!setpartida.getBandera()){
                  //  Log.e("While: ", "esperando a que termine setpartida");
                }
              //  //CUANDO TERMINA DE CARGAR ELIMINO DE LA BASE DE DATOS
                //dbHelper.delete(email, idpartida);

                //CUANDO TERMINA SE ACTUALIZA EL VALOR A CARGADA
                dbHelper.cargada(email,idpartida);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    else {
        //SI NO HAY MAS REGISTROS O NO HAY INTERNET SE CAMBIA LA BANDERA DE CARGANDO A FALSE
        banderaCargado=false;
    try {
        //SE DUERME EL HILO
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
    }



        cargarPendientes(Context context ){
    this.context = context;
}
}

    public boolean estaCargando(){
        return banderaCargado;
    }




}



