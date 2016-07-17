package ar.com.perren.esteban.digaloconemocion;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;

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
       ajugar.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
           dismiss();
           }
       });

           }
}
