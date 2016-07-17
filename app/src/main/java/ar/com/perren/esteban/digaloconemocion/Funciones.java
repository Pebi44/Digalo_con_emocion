package ar.com.perren.esteban.digaloconemocion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Esteban on 06/06/2016.
 */
public class Funciones {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public static void nueva_jugada(final Activity activity, final Context context, final String email) {
        DBHelper db = new DBHelper(context);
        Pregunta_Respuesta pregunta_respuesta = db.GetPregunta(email);
if (pregunta_respuesta==null){
    AlertDialog.Builder alerta = new AlertDialog.Builder(context);

    alerta.setMessage("No hay mas jugadas para realizar en este momento, por favor intente nuevamente mas tarde");
    alerta.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    activity.finish();
                    Intent i = new Intent(context, MenuPrincipal.class);
                    i.putExtra("email", email);
                    context.startActivity(i);
                }  });
db.close();
    alerta.show();
}
        else{
        ArrayList<Integer> emoId = new ArrayList<Integer>();
        String pregunta = pregunta_respuesta.getPregunta();
        String[] emoticones = pregunta.split(" ");

        Resources res = context.getResources();

        for (int j = 0; j < emoticones.length; j++) {
            int resID = res.getIdentifier(emoticones[j], "drawable", context.getPackageName());
            emoId.add(resID);
            Log.e("onPostExecute=", "" + resID);

        }

        Intent i = new Intent(context, Jugada.class);

        i.putExtra("email", email);
        i.putExtra("pregunta", emoId);
        i.putExtra("respuesta", pregunta_respuesta.getRespuesta());
        i.putExtra("idpregunta", pregunta_respuesta.getIdPregunta_Respuesta());
    i.putExtra("pista1", pregunta_respuesta.getPista1());
    i.putExtra("pista2", pregunta_respuesta.getPista2());
    i.putExtra("pista3", pregunta_respuesta.getPista3());
    context.startActivity(i);
db.close();
}
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
if(email.equals("")){return false;}
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void init(TableLayout tablelayout, Context context, String posicion, String apodo, String puntos) {
        TableLayout stk =tablelayout;
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        tv0.setText(posicion);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(apodo+" ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setText("   "+puntos+"  ");
        tv2.setTextColor(Color.WHITE);

        tbrow0.addView(tv2);
        stk.addView(tbrow0);


    }

    }

