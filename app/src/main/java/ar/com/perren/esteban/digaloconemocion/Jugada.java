package ar.com.perren.esteban.digaloconemocion;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Jugada extends AppCompatActivity {
DBHelper db;
    String idpartida;
    String puntaje;
    AlertDialog.Builder alerta = null;
    Button boton_ok, bnuevapista;
    String pelicula = "";
    SpannableStringBuilder builder = new SpannableStringBuilder();
    TextView Tpelicula, TVPistas;
    ArrayList<Integer> emoId;
    String email="";
    int puntos = 100;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugada);
        final String[] pistas = new String[3];
        email = getIntent().getExtras().getString("email");
        alerta =  new AlertDialog.Builder(this);
        alerta.setCancelable(false);
        boton_ok = (Button) findViewById(R.id.button_ok);
        bnuevapista = (Button) findViewById(R.id.bnuevapista);
        final EditText Erespuesta = (EditText) findViewById(R.id.respuesta);
        Tpelicula = (TextView) findViewById(R.id.nombre_pelicula);
        TVPistas = (TextView) findViewById(R.id.TVPistas);
        idpartida = getIntent().getExtras().getString("idpregunta");
        final Context context = this;
        db = new DBHelper(context);
if(idpartida.equals("")){
    alerta.setMessage("No hay mas jugadas para realizar en este momento, por favor intente nuevamente mas tarde");
    alerta.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogo1, int id) {
            finish();
        }
    });
    alerta.show();
}
        emoId = getIntent().getExtras().getIntegerArrayList("pregunta");
        pelicula = getIntent().getExtras().getString("respuesta");
        pistas[0] = getIntent().getExtras().getString("pista1");
        pistas[1] = getIntent().getExtras().getString("pista2");
        pistas[2] = getIntent().getExtras().getString("pista3");
        try {
            pelicula = new String(pelicula.getBytes("UTF-8"), "UTF-8");
            Log.e("Email=", "" + email);
            Log.e("Emoticones=", "" + emoId.toString());
            Log.e("Pelicula=", "" + pelicula);
            Log.e("idpartida=", "" + idpartida);
            Log.e("Pistas: ", pistas[0] + pistas[1] + pistas[2]);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (int i=0; i<emoId.size(); i++) {
            builder.append("").append(" ");
            builder.setSpan(new ImageSpan(this, emoId.get(i)),
                    builder.length() - 1, builder.length(), 0);
        }
      Tpelicula.setText(builder);
        bnuevapista.setOnClickListener(new View.OnClickListener() {
             int cantidad_pistas=0;

            public void onClick(View view) {
                if(cantidad_pistas<3){
String contenido = TVPistas.getText().toString();
                    contenido=contenido+"\n * "+ pistas[cantidad_pistas];
TVPistas.setText(contenido);
                cantidad_pistas++;
                    puntos=puntos-25;
                }
            }});
        boton_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String respuesta = Erespuesta.getText().toString();
                respuesta = normalizar(respuesta);
                String pelicula2 = pelicula;
                pelicula = normalizar(pelicula);
                if (respuesta.equals(pelicula)) {
                    alerta.setTitle("Correcto!");
                    puntaje =""+ Integer.toString(puntos);
                    alerta.setMessage("Puntos obetenidos:  " + puntaje);
                    db.insert(email, idpartida, puntaje);
                } else {
                    alerta.setTitle("Ups! Fallo!");
                    alerta.setMessage("La respuesta era: "+ pelicula2);
                    puntaje =""+ Integer.toString(0);
                    db.insert(email, idpartida, puntaje);
                }
                alerta.setPositiveButton("Nueva jugada", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Funciones.nueva_jugada(Jugada.this, Jugada.this, email);
                        finish();

                    }
                });
                alerta.setNegativeButton("Volver al menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                       finish();
                    }
                });
                alerta.show();

            }

        });

    }

 /*  protected void onDestroy(){
        //you may call the cancel() method but if it is not handled in doInBackground() method
       if (getPregunta != null && getPregunta.getStatus() != AsyncTask.Status.FINISHED)
        getPregunta.cancel(true);
        super.onDestroy();
    }*/
public void onStop(){
    super.onStop();
}
public String normalizar(String pababra){
    String aux = pababra;
    aux = aux.toLowerCase();
    aux =aux.replace("á", "a");
    aux =aux.replace("é","e");
    aux =aux.replace("í","i");
    aux =aux.replace("ó","o");
    aux =aux.replace("ú","u");
    aux =aux.replace(" ","");

    return aux;
}


  public void onBackPressed() {
      alerta.setTitle("");
        alerta.setMessage("Si sale de la jugada la misma se contabilizara con puntaje 0 y no podra volver a jugarla. \n" +
                "Seguro desea salir?");
      alerta.setPositiveButton("Si, salir", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogo1, int id) {
              puntaje =""+ Integer.toString(0);
              db.insert(email, idpartida, puntaje);
              finish();

          }
      });
      alerta.setNegativeButton("No, seguir jugando", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogo1, int id) {

          }
      });

      alerta.show();
    }
    ////////////////// MENU ////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Minstrucciones) {
            Instrucciones ins = new Instrucciones(Jugada.this);
            ins.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ////////////////////// fin menu ///////////////////////////////

}



