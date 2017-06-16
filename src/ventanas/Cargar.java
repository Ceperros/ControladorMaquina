/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import clases.Conexion;
import clases.FiltroBuscador;
import clases.Global;
import clases.Modelo;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Ceperro
 */
public class Cargar extends javax.swing.JFrame {

    File file;// Variable para buscar;
    ArrayList<String> valores = new ArrayList<>();
    String nombre = "";

    /**
     * Creates new form Reproductor
     */
    public Cargar() {
        initComponents();
        this.setResizable(false);
        setLocationRelativeTo(null); //Centrado en la pantalla
        this.getContentPane().setBackground(Color.WHITE);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        CargarCmbDirectorio ccd = new CargarCmbDirectorio();
        ccd.execute();
        while (!ccd.isDone() && !ccd.isCancelled()) {
        }
    }
    

    private class CargarDB extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            ProcessBuilder pb;
            if (System.getProperty("os.name").startsWith("Windows")) {
                pb = new ProcessBuilder("C:\\Program Files\\MediaI"
                        + "nfo\\MediaInfo.exe", rutaTxt.getText());
                String[] tmp = rutaTxt.getText().split("/");
                nombre = tmp[tmp.length-1];
            } else {
                pb = new ProcessBuilder("/usr/local/bin/mediainfo", rutaTxt.getText());
                String[] tmp = rutaTxt.getText().split("/");
                nombre = tmp[tmp.length-1];
            }
            
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tmp = line.split("\\:");
                if (tmp.length >= 2) {
                    if (tmp[0].trim().equals("Duration") || tmp[0].trim().equals("Width") || tmp[0].trim().equals("Height") || tmp[0].trim().equals("Bit rate") || tmp[0].trim().equals("Frame rate") || tmp[0].trim().equals("Channel(s)") || tmp[0].trim().equals("Sampling rate")) {
                        valores.add(tmp[1].trim());
                    }
                }
            }
            return null;
        }
    }

    private class SubirMySQLCompleto extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                Connection con  = Conexion.getConexionMySQL();
                Statement s = con.createStatement();
                s.executeUpdate("insert into videos values(null, '" + Global.Encriptar(nombre) + "', '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString()) + "', '" + Global.Encriptar(valores.get(0)) + "', '" + Global.Encriptar(valores.get(3)) + "', '" + Global.Encriptar(valores.get(4)) + "', '" + Global.Encriptar(valores.get(2)) + "', '" + Global.Encriptar(valores.get(2)) + "', '" + Global.Encriptar(valores.get(5)) + "', '" + Global.Encriptar(valores.get(7)) + "', '" + Global.Encriptar(valores.get(8)) + "', '" + Global.Encriptar(valores.get(9)) + "')");
                s.close();
                con.close();
                Start st = new Start();
                st.execute();
                while (!st.isDone() && !st.isCancelled()) {}
            } catch (SQLException ex) {
                Global.MostrarMensaje("Error al agregar el video al servidor\n\nError: " + ex.getMessage(), "Error");
            }
            return null;
        }
    }
    
    private class SubirMySQLIncompleto extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                Connection con  = Conexion.getConexionMySQL();
                Statement s = con.createStatement();
                s.executeUpdate("insert into videos (id, nombre, carpeta) values(null, '" + Global.Encriptar(nombre) + "', '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString()) + "')");
                s.close();
                con.close();
                Start st = new Start();
                st.execute();
                while (!st.isDone() && !st.isCancelled()) {}
            } catch (SQLException ex) {
                Global.MostrarMensaje("Error al agregar el video al servidor.\n\nError: " + ex.getMessage(), "Error");
            }
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rutaTxt = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnSubir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtProgreso = new javax.swing.JLabel();
        lblMensaje = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbDirectorio = new javax.swing.JComboBox<>();

        setBounds(new java.awt.Rectangle(0, 23, 453, 286));
        setMaximumSize(new java.awt.Dimension(453, 286));
        setMinimumSize(new java.awt.Dimension(453, 286));
        setPreferredSize(new java.awt.Dimension(453, 286));
        setResizable(false);
        setSize(new java.awt.Dimension(453, 286));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1488846157_system-search.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnSubir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487282891_icon-130-cloud-upload.png"))); // NOI18N
        btnSubir.setText("Subir");
        btnSubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Faster Intelligent ®");

        lblMensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensaje.setText("Bienvenido");

        jLabel1.setText("Directorio:");

        jLabel2.setText("Ruta:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btnSubir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbDirectorio, 0, 330, Short.MAX_VALUE)
                                    .addComponent(rutaTxt))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblMensaje, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rutaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscar)
                        .addComponent(btnSubir)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        JFileChooser buscador = new JFileChooser(".");
        buscador.setFileFilter(new FiltroBuscador("Videos", new String[] {"mp4","avi","mpeg", "mpg", "mov", "wmv", "rm", "flv"}));
        int returnVal = buscador.showOpenDialog((Component) evt.getSource());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = buscador.getSelectedFile();
            rutaTxt.setText(file.getPath());
        }
        buscador.setVisible(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnSubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirActionPerformed
        if (this.rutaTxt.getText().length() > 0) {
            CargarDB cd = new CargarDB();
            cd.execute();
            while (!cd.isDone() && !cd.isCancelled()) {}
            if(valores.size() >= 10){
                SubirMySQLCompleto smc = new SubirMySQLCompleto();
                smc.execute();
                while (!smc.isDone() && !smc.isCancelled()) {}
            }else{
                Global.MostrarMensaje("Video con error de información, se subirá con la información disponible.", "Error");
                SubirMySQLIncompleto smi = new SubirMySQLIncompleto();
                smi.execute();
                while (!smi.isDone() && !smi.isCancelled()) {}
            }
        } else {
            Global.MostrarMensaje("No seleccinó un archivo para subir.", "Error");
        }
    }//GEN-LAST:event_btnSubirActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cargar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cargar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cargar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cargar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Cargar().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnSubir;
    private javax.swing.JComboBox<String> cmbDirectorio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblMensaje;
    public javax.swing.JTextField rutaTxt;
    private javax.swing.JLabel txtProgreso;
    // End of variables declaration//GEN-END:variables
    private class CargarCmbDirectorio extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            try {
                ArrayList<String> carpetas = new ArrayList<>();
                FTPClient ftpClient = Conexion.ConectarFTP();
                ftpClient.changeWorkingDirectory("/SMARTPOINT TWO/");
                ftpClient.enterLocalPassiveMode();
                for (FTPFile directorio : ftpClient.listDirectories()) {
                    if (!directorio.getName().equals(".") && !directorio.getName().equals("..")) {
                        carpetas.add(directorio.getName());
                    }
                }
                Modelo m = new Modelo();
                m.setModelo(carpetas);
                if (m.getSize() > 0) {
                    m.setSelectedItem(m.getElementAt(0));
                }
                cmbDirectorio.setModel(m);
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                Global.MostrarMensaje("No se pudo cargar los directorios, por favor refrsque el programa", "Error");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo cargar los directorios, por favor refrsque el programa", "Error");
            }
            return null;
        }
    }

    private class Start extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            try {
                BufferedInputStream buffIn;
                FTPClient ftpClient = Conexion.ConectarFTP();
                ftpClient.changeWorkingDirectory("/SMARTPOINT TWO/" + cmbDirectorio.getSelectedItem().toString() + "/");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                try {
                    try (FileInputStream inputStream = new FileInputStream(rutaTxt.getText())) {
                        buffIn = new BufferedInputStream(inputStream);//Ruta del archivo para enviar
                        lblMensaje.setText("Subiendo el video al servidor, por favor espere...");
                        ftpClient.storeFile(file.getName(), buffIn);
                        inputStream.close();
                    } //Ruta del archivo para enviar
                    buffIn.close(); //Cerrar envio de arcivos al FTP
                    ftpClient.logout(); //Cerrar sesión
                    ftpClient.disconnect();//Desconectarse del servidor
                    Global.MostrarMensaje("Archivo subido satisfactoriamente", "¡Éxito!");
                    lblMensaje.setText("Video subido con éxito.");
                } catch (FileNotFoundException ex) {
                    Global.MostrarMensaje("Error al agregar el video al servidor.\n\nError: " + ex.getMessage(), "Error");
                } catch (IOException ex) {
                    Global.MostrarMensaje("Error al agregar el video al servidor.\n\nError: " + ex.getMessage(), "Error");
                }
            } catch (IOException ex) {
                Global.MostrarMensaje("Error al agregar el video al servidor.\n\nError: " + ex.getMessage(), "Error");
            }
            return null;
        }
    }
}
