package ar.com.perren.esteban.digaloconemocion;
        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Vibrator;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


        import java.io.IOException;
        import java.util.ArrayList;

        import ar.com.perren.esteban.digaloconemocion.library.Httppostaux;
import android.support.v7.app.AppCompatActivity;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LoginButton loginButton;
    EditText user;
    EditText pass;
    Button blogin;
    TextView registrar, recpass;
    Httppostaux post;
    String IP_Server="digaloconemocion.esy.es";//IP DE NUESTRO PC
    String URL_connect="http://"+IP_Server+"/acces.php";//ruta en donde estan nuestros archivos
    Intent i, registro;
    private ProgressDialog pDialog;

    private CallbackManager callbackManager;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        DBHelper db = new DBHelper(MainActivity.this);
          try {
              db.createDataBase();
          } catch (IOException e) {
              e.printStackTrace();
              this.finish();
          }
          checklogin();

        i=new Intent(MainActivity.this, MenuPrincipal.class);
/////////////// LOGIN COMUN //////////////////////////
        post=new Httppostaux();

        user= (EditText) findViewById(R.id.edusuario);
        pass= (EditText) findViewById(R.id.edpassword);
        blogin= (Button) findViewById(R.id.Blogin);
        registrar=(TextView) findViewById(R.id.link_to_register);
        recpass=(TextView) findViewById(R.id.Trecpass);



          user.addTextChangedListener(                new TextWatcher() {

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  if (!Funciones.isEmailValid(user.getText().toString())) {
                      user.setError("El formato del email no es valido");
                  } else
                      user.setError(null);
              }

              @Override
              public void afterTextChanged(Editable s) {

              }


          });
        //Login button action

        blogin.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Boolean bandera = true;

                //Extreamos datos de los EditText
                String usuario=user.getText().toString();
                if (!Funciones.isEmailValid(usuario)) {
                    user.setError("El formato del email no es valido");
                    bandera = false;
                }


                String passw=pass.getText().toString();

                //verificamos si estan en blanco
                if( checklogindata( usuario , passw )==true && bandera){

                    //si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
                    if(Funciones.isOnline(MainActivity.this)) {
                        new asynclogin().execute(usuario, passw);
                    }else
                    {
                        if(loginoffline(usuario, passw)){
                        i.putExtra("email",usuario);
                        startActivity(i);
                        }
                        else{
                            Toast toast1 = Toast.makeText(MainActivity.this, "NO TINE CONEXION A INTERNET! Nombre de usuario o contraseña incorrecto", Toast.LENGTH_LONG);
                            toast1.setGravity(Gravity.CENTER, 0, 0);
                            toast1.show();

                        }

                    }

                }else{
                    //si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    err_login();
                }

            }
        });
          recpass.setOnClickListener(new View.OnClickListener() {

              public void onClick(View view) {
                  final Dialog dialog = new Dialog(MainActivity.this);
                  dialog.setContentView(R.layout.recpass);
                  dialog.setTitle("Recuperar contraseña");
                  dialog.show();
                  final EditText correo = (EditText) dialog.findViewById(R.id.Correo);
                  correo.addTextChangedListener(                new TextWatcher() {

                      @Override
                      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                      }

                      @Override
                      public void onTextChanged(CharSequence s, int start, int before, int count) {
                          if (!Funciones.isEmailValid(correo.getText().toString())) {
                              correo.setError("El formato del email no es valido");
                          } else
                              correo.setError(null);
                      }

                      @Override
                      public void afterTextChanged(Editable s) {

                      }


                  });

                Button aceptar = (Button) dialog.findViewById(R.id.Baceptarrecpass);
                aceptar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Context context2 = dialog.getContext();
                        String mail=correo.getText().toString();
                        if(Funciones.isOnline(context2)){
                        if(Funciones.isEmailValid(mail)){
                            asyncGetPass asyncgetpass = new asyncGetPass(context2, mail);
                            asyncgetpass.execute();
                        }}
                        else
                        {
                            Toast toast1 = Toast.makeText(context2, "No hay conexion a internet, intentelo nuevamente mas tarde", Toast.LENGTH_LONG);
                            toast1.setGravity(Gravity.CENTER, 0, 0);
                            toast1.show();

                        }



                        dialog.dismiss();
                    }
                });
              }});


        registrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

              /*  //Abre el navegador al formulario adduser.html
                String url = "http://" + IP_Server + "/adduser.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
                registro = new Intent(MainActivity.this, Registro.class );
                startActivity(registro);
            }
        });

        ////////////////FIN LOGIN COMUN //////////////////////
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        ////////////////////////
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        try {
                            if (json != null) {
                                String user = json.getString("email");
                                Log.d("Email","Email: " + user);
                                i.putExtra("email",user);

                                startActivity(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

        @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {


            }
        });


   }
    ////////////////// MENU ////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Minstrucciones) {
            Instrucciones ins = new Instrucciones(MainActivity.this);
            ins.show();
            //Intent i = new Intent(getApplicationContext(), Instrucciones.class);
           // startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ////////////////////// fin menu ///////////////////////////////






    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
   protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    public void checklogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Iniciando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    JSONObject json = response.getJSONObject();
                    try {
                        if (json != null) {
                            String user = json.getString("email");
                            Log.d("Email", "Email: " + user);
                            i.putExtra("email", user);
                            pDialog.cancel();
                            pDialog.dismiss();
                            startActivity(i);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {

           }

    public void err_login(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
        toast1.show();
    }

public boolean loginoffline(String user, String pass){
    DBHelper bd = new DBHelper(MainActivity.this);
    return bd.login(user,pass);
}
    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/

    public String loginstatus(String username ,String password ) {
        int logstatus = -1;
        String apodo = "";
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("usuario", username));
        postparameters2send.add(new BasicNameValuePair("password", password));

        //realizamos una peticion y como respuesta obtenes un array JSON
        JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);


        //si lo que obtuvimos no es null
        if (jdata != null && jdata.length() > 0) {

            JSONObject json_data; //creamos un objeto JSON
            try {
                if (jdata.length() > 1) {
                    json_data = jdata.getJSONObject(1); //leemos el segundo segmento en nuestro caso el unico
                    logstatus = json_data.getInt("logstatus");//accedemos al valor
                    Log.e("loginstatus", "logstatus= " + logstatus);//muestro por log que obtuvimos
                    json_data = jdata.getJSONObject(0); //leemos el segundo segmento en nuestro caso el unico
                    apodo = json_data.getString("apodo");

                } else {
                    json_data = jdata.getJSONObject(0); //leemos el segundo segmento en nuestro caso el unico
                    logstatus = json_data.getInt("logstatus");//accedemos al valor
                    Log.e("loginstatus", "logstatus= " + logstatus);//muestro por log que obtuvimos
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //validamos el valor obtenido
           /* if (logstatus==0){// [{"logstatus":"0"}]
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "valido");
                return true;
            }

        }else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR");
            return false;
        }
*/
        }
        return apodo;

    }

    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){

        if 	(username.equals("") || password.equals("")){
            Log.e("Login ui", "checklogindata user or pass error");
            return false;

        }else{

            return true;
        }

    }

/*		CLASE ASYNCTASK
 *
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir
 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.
 */

    class asynclogin extends AsyncTask< String, String, String > {

        String user,pass;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y pass
            user=params[0];
            pass=params[1];

            //enviamos y recibimos y analizamos los datos en segundo plano.
            return loginstatus(user,pass);


        }


        protected void onPostExecute(String result) {
            DBHelper db = new DBHelper(MainActivity.this);
            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);

            if (!result.equals("")){

                //Intent i=new Intent(MainActivity.this, HiScreen.class);

                i.putExtra("email",user);
                i.putExtra("apodo", result);
                db.insertUser(user, pass, result);
                startActivity(i);

            }else{
                err_login();
            }

        }

    }

}




