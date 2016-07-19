package ar.com.perren.esteban.digaloconemocion;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MenuPrincipal extends AppCompatActivity {
    Button bnueva_jugada;
    Button bsalir_cerrarsesion, btposiciones;
    TextView apodo, posicion, cantidad, puntos;
    Toast toast1;
    String email = "";
    String Apodo ="";
    Context context;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
       context= this;
        db = new DBHelper(this);
        db.getLastId();
        email = getIntent().getExtras().getString("email");
        Intent onlineIntent = new Intent(this, InternetConnectionService.class);
        startService(onlineIntent);
        asyncGetPregunta getPregunta = new asyncGetPregunta(this);
        getPregunta.execute();
        asyncGetData asyncgetdata= new asyncGetData(context,MenuPrincipal.this,email,"posicion");
        asyncgetdata.execute();
        bnueva_jugada = (Button) findViewById(R.id.nueva_jugada);
        bsalir_cerrarsesion = (Button) findViewById(R.id.button_exit);
        apodo = (TextView) findViewById(R.id.Apodo);
        posicion = (TextView) findViewById(R.id.Posicion);
        puntos = (TextView) findViewById(R.id.Puntos);
        cantidad = (TextView) findViewById(R.id.Cantidad);
        btposiciones = (Button) findViewById(R.id.bTPosiciones);
         Apodo = getIntent().getExtras().getString("apodo");
        apodo.setText(Apodo);

        btposiciones.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.tabla_posiciones);
        TableLayout layout = (TableLayout) dialog.findViewById(R.id.tablaposiciones);
        dialog.setTitle("Los 10 primeros");
        Funciones.init(layout, context, " Posicion ", " Apodo ", " Puntos ");
        if(Funciones.isOnline(context)) {
            JSONArray tabla = new JSONArray();
            asyncGetData asyncgetdata = new asyncGetData(context, MenuPrincipal.this, email, "tabla");
            try {
                asyncgetdata.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            tabla= db.getTabla();
            for(int i = 0; i<tabla.length(); i++){
                JSONObject json = new JSONObject();
                try {
                    json = tabla.getJSONObject(i);
                    Funciones.init(layout,context,json.getString("posicion"),json.getString("apodo"),json.getString("puntaje"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            toast1 = Toast.makeText(context, "No hay conexion a internet, intentelo nuevamente mas tarde", Toast.LENGTH_LONG);
            toast1.show();
        }
        Button cerrar = (Button) dialog.findViewById(R.id.buttoncerrar);
        cerrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

      }
});
        bnueva_jugada.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Funciones.nueva_jugada(MenuPrincipal.this, MenuPrincipal.this, email);

            }


        });
        bsalir_cerrarsesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             finish();

            }


        });
    }
    public void cargarDatos(String Posicion, String Puntos, String Cantidad){
        this.posicion.setText(Posicion);
        this.puntos.setText(Puntos);
        this.cantidad.setText(Cantidad);

    }
    ////////////////// MENU ////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Minstrucciones) {
            Instrucciones ins = new Instrucciones(MenuPrincipal.this);
            ins.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ////////////////////// fin menu ///////////////////////////////
}