package ar.com.perren.esteban.digaloconemocion;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Instrucciones extends Dialog{


    public Instrucciones(Context context) {
        super(context);
    }

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
       setTitle("Bienvenido a Diagalo con emocion");

       Button ajugar =  (Button) findViewById(R.id.bAJugar);
       final CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

       ajugar.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               if(checkbox.isChecked()){
                   Log.e("onCreate: ", "chekeado");
                   SharedPreferences settings = getContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = settings.edit();
                   editor.putString("skipInstrucciones", "True");
                   editor.commit();
               }
           dismiss();
           }
       });

           }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

        if(checkbox.isChecked()){
            Log.e("onCreate: ", "chekeado");
            SharedPreferences settings = getContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("skipInstrucciones", "True");
            editor.commit();
        }
    }
}
