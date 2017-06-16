/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import ventanas.Mensaje;

/**
 *
 * @author david
 */
public class Global {
    public static String direccionFtp = "ZnRwLnNtYXJ0cG9pbnQuY2w=";
    public static String usuarioFtp = "c21hcnRwb2ludGZ0cEBzbWFydHBvaW50LmNs";
    public static String passwordFtp = "MTIzNDU=";
    public static String direccionMySQL = "NTAuODcuMTQ0LjIyNjozMzA2";
    public static String baseDeDatos = "c21hcnRwb2lfU21hcnRQb2ludFR3bw==";
    public static String usuarioMySQL = "c21hcnRwb2lfc210";
    public static String passwordMySQL = "c210MjAxNw==";
    public static String directorioVideos = "";
    public static float carga = 0;

    public static String Encriptar(String in) {
        if (in != null) {
            if (!in.toLowerCase().equals("null")) {
                return Base64.encode(in.getBytes());
            }
        }

        return "Vacío";
    }

    public static String Desencriptar(String in) {
        if (in != null) {
            if (!in.toLowerCase().equals("null")) {
                return new String(Base64.decode(in));
            }
        }

        return "Vacío";
    }

    public static void MostrarMensaje(String mensaje, String titulo) {
        new Mensaje(mensaje, titulo).setVisible(true);
    }
}
