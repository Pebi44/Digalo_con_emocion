package ar.com.perren.esteban.digaloconemocion;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        final EditText nombre = (EditText) findViewById(R.id.eTnombre);
        final EditText apellido = (EditText) findViewById(R.id.eTapellido);
        final EditText apodo = (EditText) findViewById(R.id.eTapodo);
        final EditText email = (EditText) findViewById(R.id.eTemail);
        final EditText contraseña = (EditText) findViewById(R.id.eTcontraseña);
        Button aceptar = (Button) findViewById(R.id.bAceptar);
        Button cancelar = (Button) findViewById(R.id.bCancelar);








        cancelar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                finish();
            }});
        aceptar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Boolean bandera = true;
                if( nombre.getText().toString().length() == 0 ){
                    nombre.setError("El nombre es requerido");
                    bandera=false;
                }
                if (apellido.getText().toString().length() == 0) {
                    apellido.setError( "El apellido es requerido" );
                bandera=false;
                }
                if (apodo.getText().toString().length() == 0) {
                    apodo.setError( "El apodo es requerido" );
                    bandera=false;
                }
                if( email.getText().toString().length() == 0 ){
                    email.setError( "El email es requerido" );
                    bandera=false;

                }
                else {
                    if (!Funciones.isEmailValid(email.getText().toString())) {
                        email.setError("El formato del email no es valido");
                        bandera = false;
                    }
                }
                if( contraseña.getText().toString().length() == 0 ){
                    contraseña.setError( "La contraseña es requerida" );
                    bandera=false;
                }
            if(bandera){
                if(Funciones.isOnline(Registro.this)){
            asyncAdduser adduser = new asyncAdduser(Registro.this, Registro.this, nombre.getText().toString(), apellido.getText().toString(),apodo.getText().toString(), email.getText().toString(), contraseña.getText().toString());
            adduser.execute();}
            else
                {
                   Toast toast1 = Toast.makeText(Registro.this, "No hay conexion a internet, intentelo nuevamente mas tarde", Toast.LENGTH_LONG);
                    toast1.show();
                }
            }

            }});
nombre.addTextChangedListener(
        new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            nombre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }

);
        apellido.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        apellido.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }

        );
        email.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        email.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }

        );
        contraseña.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        contraseña.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }

        );
    }
}
