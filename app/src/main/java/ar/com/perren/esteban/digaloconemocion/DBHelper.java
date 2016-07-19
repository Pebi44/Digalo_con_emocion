package ar.com.perren.esteban.digaloconemocion;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Esteban on 11/05/2016.
 */
class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "juego.db";
    String DB_PATH = null;
    private static final int VERSION = 1;
    public static final String Email = "Email";
    public static final String idPregunta_Respuesta = "idPregunta_Respuesta";
    public static final String Puntaje = "Puntaje";
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context=context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDbSql;
        createDbSql = "CREATE TABLE IF NOT EXISTS partidasjugadas (" + Email
                + " text, " + idPregunta_Respuesta + " text," + Puntaje + " text, `Cargada` int(1), PRIMARY KEY (`Email`,`idPregunta_Respuesta`))";
        db.execSQL(createDbSql);
        createDbSql = "CREATE TABLE IF NOT EXISTS `pregunta_respuesta` " +
                "(`idPregunta_Respuesta` int(11), " +
                "`Pregunta` varchar(200) DEFAULT NULL, " +
                "`Respuesta` varchar(300)DEFAULT NULL, " +
                "`Pista1` text, " +
                "`Pista2` text, " +
                "`Pista3` text, " +
                "PRIMARY KEY (`idPregunta_Respuesta`))";
        db.execSQL(createDbSql);
        createDbSql = "CREATE TABLE IF NOT EXISTS usuarios ( `Email` text, `Contraseña` text, `Apodo` text, PRIMARY KEY (`Email`))";
        db.execSQL(createDbSql);
        createDbSql = "CREATE TABLE IF NOT EXISTS posiciones ( `Posicion` int(1), `Apodo` text, `Puntaje_Total` int(11), `Cant_Part_jugadas` int(11), PRIMARY KEY (`Posicion`))";
        db.execSQL(createDbSql);

    }


    /////////// FUNCIONES PARA COPIAR BASE DE DATOS PRE-CARGADA ////////////////////////////
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }




    ////////////////////////// FIN DE LAS FUNCIONES DE LA BD PRE-CARGADA //////////////////////////////////
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public int getLastId(){
        int idlast =0;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * from pregunta_respuesta";
        Cursor c = db.rawQuery(sql,null);
        if (c!=null && c.moveToLast()) {
            idlast = c.getInt(0);

        }
        Log.e("getLastId: ", Integer.toString(idlast));
        return idlast;
    }
    public boolean login(String email, String contraseña){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM usuarios WHERE Email='"+email+"' AND Contraseña='"+contraseña+"'" ;
        Cursor c = db.rawQuery(sql,null);
        if (c!=null && c.moveToFirst()) {
            Log.e("login: ", "true");
           return true;
        }
        Log.e("login: ", "false");
        return false;
    }
    public void insertUser(String email, String contraseña, String apodo){
        ContentValues valores = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        valores.put("Email", email);
        valores.put("Contraseña", contraseña);
        valores.put("Apodo",apodo);
        db.insertWithOnConflict("usuarios", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("insert: ", "se inserto usuario");
        db.close();
    }
    public void insert(String email, String idpregunta_respuesta, String puntaje){
    ContentValues valores = new ContentValues();
    SQLiteDatabase db = getWritableDatabase();
    valores.put("Email", email);
    valores.put("idPregunta_Respuesta", idpregunta_respuesta);
    valores.put("Puntaje", puntaje);
    valores.put("Cargada",0);
    //db.insertWithOnConflict("pendientes", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
    db.insertWithOnConflict("partidasjugadas", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
    db.close();
}
    public void insert(int posicion, String apodo, int puntajeTotal, int cantidaJugadas){
        ContentValues valores = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        valores.put("Posicion", posicion);
        valores.put("Apodo", apodo);
        valores.put("Puntaje_Total", puntajeTotal);
        valores.put("Cant_Part_Jugadas", cantidaJugadas);
        db.insertWithOnConflict("posiciones", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public JSONArray getTabla(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from posiciones", null);
        JSONArray jsonArray = new JSONArray();
        if (c!=null) {
            c.moveToFirst();
            while(!c.isAfterLast()){
                JSONObject json= new JSONObject();
                try {
                    json.put("posicion", c.getString(0));
                    json.put("apodo", c.getString(1));
                    json.put("puntaje", c.getString(2));
                    json.put("partidas_jugadas", c.getString(3));
                    jsonArray.put(json);
                    c.moveToNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return jsonArray;
    }
    public void insert(Pregunta_Respuesta pregunta_respuesta){
        ContentValues valores = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        valores.put("idPregunta_Respuesta", pregunta_respuesta.getIdPregunta_Respuesta());
        valores.put("Pregunta", pregunta_respuesta.getPregunta());
        valores.put("Respuesta", pregunta_respuesta.getRespuesta());
        valores.put("Pista1", pregunta_respuesta.getPista1());
        valores.put("Pista2", pregunta_respuesta.getPista2());
        valores.put("Pista3", pregunta_respuesta.getPista3());
        db.insertWithOnConflict("pregunta_respuesta", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }
    public void delete(String email, String idPregunta_Respuesta){
        SQLiteDatabase db = getWritableDatabase();
        String condicion="Email='"+email+"' and idPregunta_Respuesta='"+idPregunta_Respuesta+"'";
        db.delete("pendientes", condicion,null);
        db.close();
    }
    public void cargada(String email, String idPregunta_Respuesta){
        SQLiteDatabase db = getWritableDatabase();
        String condicion="Email='"+email+"' and idPregunta_Respuesta='"+idPregunta_Respuesta+"'";
        ContentValues valores = new ContentValues();
        valores.put("Cargada",1);
        db.update("partidasjugadas", valores,condicion,null);
        db.close();
    }
    public JSONArray getAll() throws JSONException {

        SQLiteDatabase db = getReadableDatabase();
     //   Cursor c = db.rawQuery("select * from pendientes", null);
        Cursor c = db.rawQuery("select * from partidasjugadas where Cargada=0", null);
        JSONArray jsonArray = new JSONArray();
        if (c!=null) {
                c.moveToFirst();
            while(!c.isAfterLast()){
            JSONObject json= new JSONObject();
                Log.e("registro: ", c.getString(0));
            json.put("email", c.getString(0));
            json.put("idpartida", c.getInt(1));
            json.put("puntaje", c.getInt(2));
            jsonArray.put(json);
            c.moveToNext();
        }
        }
        return jsonArray;
    }
    public Boolean isNull(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from pendientes", null);

        if (c==null) {
            return true;
        }
return false;
        }


public Pregunta_Respuesta GetPregunta(String email){
    Pregunta_Respuesta pregunta_respuesta;
    SQLiteDatabase db = getReadableDatabase();
    String sql="SELECT * FROM pregunta_respuesta WHERE idPregunta_Respuesta NOT IN (SELECT idPregunta_Respuesta FROM partidasjugadas WHERE email = '"+email+"') ORDER BY RANDOM() LIMIT 1";
    Cursor c = db.rawQuery(sql, null);
    c.moveToNext();
    if (c!=null&& c.getCount()>0) {
        c.moveToFirst();
       pregunta_respuesta = new Pregunta_Respuesta(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
        return pregunta_respuesta;
    }
return null;
}
}
