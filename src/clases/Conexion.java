/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Ceperro
 */
public class Conexion {

    public static FTPClient ConectarFTP() {
        FTPClient ftpClient = new FTPClient();

        if (!ftpClient.isConnected()) {
            try {
                ftpClient.connect(Global.Desencriptar(Global.direccionFtp));
                ftpClient.login(Global.Desencriptar(Global.usuarioFtp), Global.Desencriptar(Global.passwordFtp));
                ftpClient.enterLocalPassiveMode();
            } catch (UnknownHostException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            int reply = ftpClient.getReplyCode();

            System.out.println("Respuesta recibida de conexión FTP:" + reply);

            if (FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Conectado Satisfactoriamente");
            } else {
                System.out.println("Imposible conectarse al servidor");
            }
        }

        return ftpClient;
    }

    public static Connection getConexionMySQL() {
        Connection con = null;
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://" + Global.Desencriptar(Global.direccionMySQL) + "/" + Global.Desencriptar(Global.baseDeDatos), Global.Desencriptar(Global.usuarioMySQL), Global.Desencriptar(Global.passwordMySQL));
        } catch (SQLException ex) {
            Global.MostrarMensaje("Error al conectar al servidor MySQL", "Error Conexion MySQL");
        } catch (Exception ex) {
            Global.MostrarMensaje("Error al conectar al servidor MySQL", "Error Conexion MySQL");
        }

        return con;
    }

}
