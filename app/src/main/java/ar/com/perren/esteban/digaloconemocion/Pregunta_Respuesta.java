package ar.com.perren.esteban.digaloconemocion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Esteban on 06/06/2016.
 */
public class Pregunta_Respuesta {
    private String idPregunta_Respuesta;
    private String pregunta = "";
    private String respuesta="";
    private String Pista1="";
    private String Pista2="";
    private String Pista3="";

    public Pregunta_Respuesta(String idPregunta_Respuesta, String pregunta, String respuesta, String pista1, String pista2, String pista3) {
        this.idPregunta_Respuesta = idPregunta_Respuesta;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        Pista1 = pista1;
        Pista2 = pista2;
        Pista3 = pista3;
    }
    public Pregunta_Respuesta(JSONObject json) throws JSONException {
        this.idPregunta_Respuesta = json.getString("idPregunta_Respuesta");
        this.pregunta = json.getString("Pregunta");
        this.respuesta = json.getString("Respuesta");
        Pista1 = json.getString("Pista1");
        Pista2 = json.getString("Pista2");
        Pista3 = json.getString("Pista3");
    }


    public String getIdPregunta_Respuesta() {
        return idPregunta_Respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }


    public String getPista1() {
        return Pista1;
    }

    public String getPista2() {
        return Pista2;
    }

    public String getPista3() {
        return Pista3;
    }

    public void setIdPregunta_Respuesta(String idPregunta_Respuesta) {
        this.idPregunta_Respuesta = idPregunta_Respuesta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }



    public void setPista1(String pista1) {
        Pista1 = pista1;
    }

    public void setPista2(String pista2) {
        Pista2 = pista2;
    }

    public void setPista3(String pista3) {
        Pista3 = pista3;
    }
}
