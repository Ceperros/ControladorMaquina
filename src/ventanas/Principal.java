/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import clases.*;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.awt.*;
import javax.swing.UIManager;

/**
 *
 * @author david
 */
public class Principal extends javax.swing.JFrame {

    ArrayList<String> nombresMaquinas = new ArrayList<>();

    /**
     * Creates new form Principal
     */
    public Principal() {
        this.setUndecorated(true);
        initComponents();
        this.getContentPane().setBackground(Color.WHITE);
        Global.carga = 0;
        String home = System.getProperty("user.home");
        Global.directorioVideos = Global.Encriptar(home + "/Downloads/Videos/");
        this.setOpacity(0.97f);
      

        new Start().execute();

        pnlAsignar.setVisible(true);
        pnlEliminar.setVisible(false);
        pnlVisualizar.setVisible(false);
        setLocationRelativeTo(null);
    }
    
    private class Start extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            CargarCmb ccMac = new CargarCmb(cmbMac, "mac");
            CargarCmb ccTipo = new CargarCmb(cmbTipo, "tipo");
            CargarCmb ccMarca = new CargarCmb(cmbMarca, "propietario");
            CargarDirectorios();
            CargarCmbLugar cclMac = new CargarCmbLugar(cmbLugarMac);
            ccMac.execute();
            while (!ccMac.isDone() && !ccMac.isCancelled()) {
            }
            ccTipo.execute();
            while (!ccTipo.isDone() && !ccTipo.isCancelled()) {
            }
            ccMarca.execute();
            while (!ccMarca.isDone() && !ccMarca.isCancelled()) {
            }
            cclMac.execute();
            while (!cclMac.isDone() && !cclMac.isCancelled()) {
            }
            return null;
        }
    }

    private void CargarDirectorios() {
        CargarCmbDirectorio ccdMac = new CargarCmbDirectorio(cmbDirectorioMac, cmbArchivoMac);
        CargarCmbDirectorio ccdMarca = new CargarCmbDirectorio(cmbDirectorioMarca, cmbArchivoMarca);
        CargarCmbDirectorio ccdTipo = new CargarCmbDirectorio(cmbDirectorioTipo, cmbArchivoTipo);
        CargarCmbDirectorio ccdTodo = new CargarCmbDirectorio(cmbDirectorioTodo, cmbArchivoTodo);
        CargarCmbDirectorio ccdEliminarVideo = new CargarCmbDirectorio(cmbDirectorioEliminarVideo, cmbArchivoEliminarVideo);
        CargarCmbDirectorio ccdEliminarDirectorio = new CargarCmbDirectorio(cmbEliminarDirectorio);
        CargarCmbDirectorio ccdVisualizar = new CargarCmbDirectorio(cmbDirectorioVisualizar, lstArchivo);
        ccdMac.execute();
        while (!ccdMac.isDone() && !ccdMac.isCancelled()) {
        }
        ccdMarca.execute();
        while (!ccdMarca.isDone() && !ccdMarca.isCancelled()) {
        }
        ccdTipo.execute();
        while (!ccdTipo.isDone() && !ccdTipo.isCancelled()) {
        }
        ccdTodo.execute();
        while (!ccdTodo.isDone() && !ccdTodo.isCancelled()) {
        }
        ccdEliminarVideo.execute();
        while (!ccdEliminarVideo.isDone() && !ccdEliminarVideo.isCancelled()) {
        }
        ccdEliminarDirectorio.execute();
        while (!ccdEliminarDirectorio.isDone() && !ccdEliminarDirectorio.isCancelled()) {
        }
        ccdVisualizar.execute();
        while (!ccdVisualizar.isDone() && !ccdVisualizar.isCancelled()) {
        }
    }

    private class CargarCmb extends SwingWorker<Void, Void> {

        private final JComboBox cmb;
        private final String tipo;

        public CargarCmb(JComboBox cmb, String tipo) {
            this.cmb = cmb;
            this.tipo = tipo;
        }

        @Override
        public Void doInBackground() {
            ArrayList<String> tipos = new ArrayList<>();
            Connection c = null;
            Statement s = null;
            ResultSet rs = null;
            try {
                c = Conexion.getConexionMySQL();
                s = c.createStatement();
                if (tipo.equals("mac")) {
                    rs = s.executeQuery("select " + tipo + ",nombre from maquinas");
                } else {
                    rs = s.executeQuery("select " + tipo + " from maquinas");
                }

                while (rs.next()) {
                    tipos.add(Global.Desencriptar(rs.getString(1)));
                    if (tipo.equals("mac")) {
                        nombresMaquinas.add(Global.Desencriptar(rs.getString(2)));
                    }
                }
                Modelo m = new Modelo();
                m.setModelo(tipos);
                if (m.getSize() > 0) {
                    m.setSelectedItem(m.getElementAt(0));
                    txtNombreMaquina.setText(nombresMaquinas.get(0));
                }
                cmb.setModel(m);
                rs.close();
                s.close();
                c.close();
            } catch (SQLException ex) {
                Global.MostrarMensaje("No se pudo cargar " + tipo + ", por favor refresque el programa", "Error " + tipo);
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo cargar " + tipo + ", por favor refresque el programa", "Error " + tipo);
            } finally {
                Global.carga += 6.25;
            }
            return null;
        }
    }

    private class CargarCmbLugar extends SwingWorker<Void, Void> {

        private final JComboBox cmb;

        public CargarCmbLugar(JComboBox cmb) {
            this.cmb = cmb;
        }

        @Override
        public Void doInBackground() {
            ArrayList<String> videos = new ArrayList<>();
            Connection c = null;
            Statement s = null;
            ResultSet rs = null;
            try {
                c = Conexion.getConexionMySQL();
                s = c.createStatement();
                rs = s.executeQuery("SELECT video1, video2, video3, video4, video5, video6, video7, video8, video9, video10, video11, video12, video13, video14, video15, video16, video17, video18, video19, video20  FROM maquinas WHERE mac='" + Global.Encriptar(cmbMac.getSelectedItem().toString()) + "'");
                int indice =1;
                while (rs.next()) {
                    
                    videos.add(indice+"=" + Global.Desencriptar(rs.getString(indice)));
                    indice ++;
                }

                Modelo m = new Modelo();
                m.setModelo(videos);
                if (m.getSize() > 0) {
                    m.setSelectedItem(m.getElementAt(0));
                }
                cmb.setModel(m);
                rs.close();
                s.close();
                c.close();
            } catch (SQLException ex) {
                Global.MostrarMensaje("No se pudo cargar los lugares, por favor refresque el programa", "Error CargarCmbLugar");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo cargar los lugares, por favor refresque el programa", "Error CargarCmbLugar");
            } finally {
                Global.carga += 6.25;
            }
            return null;
        }

    }

    private class CargarCmbDirectorio extends SwingWorker<Void, Void> {

        private final JComboBox cmbDirectorio;
        private final JComboBox cmbArchivoC;
        private final JList cmbArchivoL;

        public CargarCmbDirectorio(JComboBox cmbDirectorio, JComboBox cmbArchivo) {
            this.cmbDirectorio = cmbDirectorio;
            this.cmbArchivoC = cmbArchivo;
            this.cmbArchivoL = null;
        }

        public CargarCmbDirectorio(JComboBox cmbDirectorio, JList lstArchivo) {
            this.cmbDirectorio = cmbDirectorio;
            this.cmbArchivoL = lstArchivo;
            this.cmbArchivoC = null;
        }

        public CargarCmbDirectorio(JComboBox cmbDirectorio) {
            this.cmbDirectorio = cmbDirectorio;
            this.cmbArchivoL = null;
            this.cmbArchivoC = null;
        }

        @Override
        public Void doInBackground() {
            FTPClient ftpClient = null;
            try {
                ArrayList<String> carpetas = new ArrayList<>();
                ftpClient = Conexion.ConectarFTP();
                ftpClient.changeWorkingDirectory("/SMARTPOINT TWO");
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
                if (cmbArchivoC != null) {
                    new CargarCmbArchivo(cmbArchivoC, cmbDirectorio.getSelectedItem().toString()).execute();
                }
                if (cmbArchivoL != null) {
                    new CargarCmbArchivo(cmbArchivoL, cmbDirectorio.getSelectedItem().toString()).execute();
                }
            } catch (IOException ex) {
                Global.MostrarMensaje("No se pudo cargar los directorios, por favor refresque el programa", "Error CargarCmbDirectorio");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo cargar los directorios, por favor refresque el programa", "Error CargarCmbDirectorio");
            } finally {
                Global.carga += 6.25;
            }
            return null;
        }
    }

    private class CargarCmbArchivo extends SwingWorker<Void, Void> {

        private final JComboBox cmb;
        private final JList lst;
        private final String directorio;

        public CargarCmbArchivo(JComboBox cmb, String directorio) {
            this.cmb = cmb;
            this.lst = null;
            this.directorio = directorio;
        }

        public CargarCmbArchivo(JList lst, String directorio) {
            this.cmb = null;
            this.lst = lst;
            this.directorio = directorio;
        }

        @Override
        public Void doInBackground() {
            ArrayList<String> archivos = new ArrayList<>();
            FTPClient ftpClient = null;
            try {
                ftpClient = Conexion.ConectarFTP();
                ftpClient.changeWorkingDirectory("/SMARTPOINT TWO/" + directorio);
                ftpClient.enterLocalPassiveMode();
                for (FTPFile file : ftpClient.listFiles()) {
                    if (!file.getName().equals(".") && !file.getName().equals("..")) {
                        archivos.add(file.getName());
                    }
                }
                Modelo m = new Modelo();
                m.setModelo(archivos);
                if (m.getSize() > 0) {
                    m.setSelectedItem(m.getElementAt(0));
                }
                if (cmb != null) {
                    cmb.setModel(m);
                }
                if (lst != null) {
                    lst.setModel(m);
                }
                
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                Global.MostrarMensaje("No se pudo cargar los archivos de carga por mac, por favor refresque el programa", "Error CargarCmbArchivoMac");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo cargar los archivos de carga por mac, por favor refresque el programa", "Error CargarCmbArchivoMac");
            } finally {
                Global.carga += 6.25;
            }
            return null;
        }
    }

    private class CargarDetallesArchivoVisualizar extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                Connection con = Conexion.getConexionMySQL();
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery("select * from videos where nombre='"+Global.Encriptar(lstArchivo.getSelectedValue())+"' and carpeta='"+Global.Encriptar(cmbDirectorioVisualizar.getSelectedItem().toString())+"'");
                while (rs.next()) {
                    try {
                        txtAltoFotograma.setText(Global.Desencriptar(rs.getString(6)));
                        txtAnchoFotograma.setText(Global.Desencriptar(rs.getString(5)));
                        txtCanales.setText(Global.Desencriptar(rs.getString(11)));
                        txtDuracion.setText(Global.Desencriptar(rs.getString(4)));
                        txtVelocidadBits.setText(Global.Desencriptar(rs.getString(8)));
                        txtVelocidadBitsSonido.setText(Global.Desencriptar(rs.getString(10)));
                        txtVelocidadDatos.setText(Global.Desencriptar(rs.getString(7)));
                        txtVelocidadFotograma.setText(Global.Desencriptar(rs.getString(9)));
                        txtVelocidadSonido.setText(Global.Desencriptar(rs.getString(12)));
                    } catch (IndexOutOfBoundsException ex) {
                        Global.MostrarMensaje("Imposible cargar la información del video.\n\nError: "+ex.getMessage(), "Error CargarDetallesArchivoVisualizar");
                    }
                }
                rs.close();
                s.close();
                con.close();
            } catch (SQLException ex) {
                Global.MostrarMensaje("Imposible cargar la información del video.\n\nError: "+ex.getMessage(), "Error CargarDetallesArchivoVisualizar");
            }
            return null;
        }
    }

    private class BorrarDirectorio extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                FTPClient client = Conexion.ConectarFTP();
                client.changeWorkingDirectory("/SMARTPOINT TWO");
                client.deleteFile(cmbEliminarDirectorio.getSelectedItem().toString());
                client.logout();
                client.disconnect();
            } catch (IOException ex) {
                Global.MostrarMensaje("No se pudo eliminar el directorio: " + cmbEliminarDirectorio.getSelectedItem().toString(), "Error Eliminar Directorio");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo eliminar el directorio: " + cmbEliminarDirectorio.getSelectedItem().toString(), "Error Eliminar Directorio");
            }
            return null;
        }
    }

    private class AgregarDirectorio extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                FTPClient client = Conexion.ConectarFTP();
                client.changeWorkingDirectory("/SMARTPOINT TWO");
                client.makeDirectory(txtNombreDirectorio.getText());
                client.logout();
                client.disconnect();
            } catch (IOException ex) {
                Global.MostrarMensaje("No se pudo crear el directorio: " + txtNombreDirectorio.getText(), "Error Eliminar Directorio");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo crear el directorio: " + txtNombreDirectorio.getText(), "Error Eliminar Directorio");
            }
            return null;
        }
    }

    private class Asignar extends SwingWorker<Void, Void> {

        private final JComboBox cmbDirectorio;
        private final JComboBox cmbArchivo;
        private final JComboBox cmbLugar;
        private final String a;
        private final String valor;

        public Asignar(JComboBox cmbDirectorio, JComboBox cmbArchivo, JComboBox cmbLugar, String valor, String a) {
            this.cmbDirectorio = cmbDirectorio;
            this.cmbArchivo = cmbArchivo;
            this.cmbLugar = cmbLugar;
            this.valor = valor;
            this.a = a;
        }

        public Asignar(JComboBox cmbDirectorio, JComboBox cmbArchivo, JComboBox cmbLugar, String a) {
            this.cmbDirectorio = cmbDirectorio;
            this.cmbArchivo = cmbArchivo;
            this.cmbLugar = cmbLugar;
            this.valor = "";
            this.a = a;
        }

        @Override
        public Void doInBackground() throws IOException {
            if (cmbLugar.getSelectedItem().toString().length() > 0 && cmbDirectorio.getSelectedItem().toString().length() > 0 && cmbArchivo.getSelectedItem().toString().length() > 0) {
                if (cmbLugar.getModel().getSize() > 0 && cmbDirectorio.getModel().getSize() > 0 && cmbArchivo.getModel().getSize() > 0) {
                    try {
                        Connection con = Conexion.getConexionMySQL();
                        Statement s = con.createStatement();
                        switch (a) {
                            case "mac":
                                s.executeUpdate("update maquinas set " + cmbLugar.getSelectedItem().toString() + "= '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString() + "/" + cmbArchivo.getSelectedItem().toString()) + "' where mac='" + Global.Encriptar(valor) + "'");
                                break;
                            case "tipo":
                                s.executeUpdate("update maquinas set " + cmbLugar.getSelectedItem().toString() + "= '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString() + "/" + cmbArchivo.getSelectedItem().toString()) + "' where tipo='" + Global.Encriptar(valor) + "'");
                                break;
                            case "marca":
                                s.executeUpdate("update maquinas set " + cmbLugar.getSelectedItem().toString() + "= '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString() + "/" + cmbArchivo.getSelectedItem().toString()) + "' where propietario='" + Global.Encriptar(valor) + "'");
                                break;
                            case "todo":
                                s.executeUpdate("update maquinas set " + cmbLugar.getSelectedItem().toString() + "= '" + Global.Encriptar(cmbDirectorio.getSelectedItem().toString() + "/" + cmbArchivo.getSelectedItem().toString()) + "'");
                                break;
                            default:
                                Global.MostrarMensaje("Imposible agregar el video", "Error");
                                break;
                        }
                        s.close();
                        con.close();

                    } catch (SQLException ex) {
                        Global.MostrarMensaje("Imposible agregar el video", "Error");
                    }
                } else {
                    Global.MostrarMensaje("Imposible agregar el video", "Error");
                }
            } else {
                Global.MostrarMensaje("Imposible agregar el video", "Error");
            }
            return null;
        }
    }

    private class BorrarVideoFTP extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                FTPClient client = Conexion.ConectarFTP();
                client.changeWorkingDirectory("/SMARTPOINT TWO/" + cmbDirectorioEliminarVideo.getSelectedItem().toString());
                if (client.deleteFile(cmbArchivoEliminarVideo.getSelectedItem().toString())) {
                    Global.MostrarMensaje("Archivo Eliminado Con éxito", "¡Éxito!");
                }
                client.logout();
                client.disconnect();
                BorrarVideoMySQL bvm = new BorrarVideoMySQL();
                bvm.execute();
                while(!bvm.isDone() && !bvm.isCancelled()){}
            } catch (IOException e) {
                Global.MostrarMensaje("No se pudo eliminar el video: " + cmbArchivoEliminarVideo.getSelectedItem().toString() + " de la carpeta: " + cmbDirectorioEliminarVideo.getSelectedItem().toString(), "Error Eliminar Video");
            } catch (HeadlessException ex) {
                Global.MostrarMensaje("No se pudo eliminar el video: " + cmbArchivoEliminarVideo.getSelectedItem().toString() + " de la carpeta: " + cmbDirectorioEliminarVideo.getSelectedItem().toString(), "Error Eliminar Video");
            } catch (Exception ex) {
                Global.MostrarMensaje("No se pudo eliminar el video: " + cmbArchivoEliminarVideo.getSelectedItem().toString() + " de la carpeta: " + cmbDirectorioEliminarVideo.getSelectedItem().toString(), "Error Eliminar Video");
            }
            return null;
        }
    }
    
    private class BorrarVideoMySQL extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            try {
                Connection con = Conexion.getConexionMySQL();
                Statement s = con.createStatement();
                s.executeUpdate("delete from videos where nombre='"+Global.Encriptar(cmbArchivoEliminarVideo.getSelectedItem().toString())+"' and carpeta='"+Global.Encriptar(cmbDirectorioEliminarVideo.getSelectedItem().toString())+"'");
                s.close();
                con.close();
            } catch (SQLException ex) {
                Global.MostrarMensaje("No se pudo eliminar el video: " + cmbArchivoEliminarVideo.getSelectedItem().toString() + " de la carpeta: " + cmbDirectorioEliminarVideo.getSelectedItem().toString(), "Error Eliminar Video");
            }
            return null;
        }
    }

    /**
     *
     * @param cmb
     * @param tipo
     */
    private void Filtro(JComboBox cmb, JTextField txt, String tipo) {
        if (txt.getText().equals("")) {
            CargarCmb cc = new CargarCmb(cmb, tipo);
            cc.execute();
        }
        Modelo modelo = (Modelo) cmb.getModel();
        ArrayList<String> antiguo = modelo.getModelo();
        ArrayList<String> nuevo = new ArrayList<>();
        for (String tmp : antiguo) {
            if (tmp.contains(txt.getText())) {
                nuevo.add(tmp);
            }
        }
        if (nuevo.size() <= 0) {
            nuevo.add("");
        }
        modelo.setModelo(nuevo);
        cmb.setModel(modelo);

        if (nuevo.size() > 0) {
            cmb.setSelectedIndex(0);
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

        jLabel6 = new javax.swing.JLabel();
        pnlBotonera = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAsignar = new javax.swing.JButton();
        pnlVisualizar = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstArchivo = new javax.swing.JList<>();
        cmbDirectorioVisualizar = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        txtVelocidadFotograma = new javax.swing.JTextField();
        txtAnchoFotograma = new javax.swing.JTextField();
        txtAltoFotograma = new javax.swing.JTextField();
        txtVelocidadDatos = new javax.swing.JTextField();
        txtVelocidadBits = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtVelocidadBitsSonido = new javax.swing.JTextField();
        txtVelocidadSonido = new javax.swing.JTextField();
        txtCanales = new javax.swing.JTextField();
        btnReproducir = new javax.swing.JButton();
        pnlAsignar = new javax.swing.JPanel();
        pnlCargaMac = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtNombreMaquina = new javax.swing.JTextField();
        cmbMac = new javax.swing.JComboBox<>();
        cmbDirectorioMac = new javax.swing.JComboBox<>();
        cmbArchivoMac = new javax.swing.JComboBox<>();
        cmbLugarMac = new javax.swing.JComboBox<>();
        btnCargarMac = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtFiltroMac = new javax.swing.JTextField();
        pnlCargaMarca = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbMarca = new javax.swing.JComboBox<>();
        cmbDirectorioMarca = new javax.swing.JComboBox<>();
        cmbArchivoMarca = new javax.swing.JComboBox<>();
        cmbLugarMarca = new javax.swing.JComboBox<>();
        btnCargarMarca = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtFiltroMarca = new javax.swing.JTextField();
        pnlCargaTipo = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        cmbDirectorioTipo = new javax.swing.JComboBox<>();
        cmbArchivoTipo = new javax.swing.JComboBox<>();
        cmbLugarTipo = new javax.swing.JComboBox<>();
        btnCargarTipo = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtFiltroTipo = new javax.swing.JTextField();
        pnlCargaTodo = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        cmbDirectorioTodo = new javax.swing.JComboBox<>();
        cmbArchivoTodo = new javax.swing.JComboBox<>();
        cmbLugarTodo = new javax.swing.JComboBox<>();
        btnCargarTodo = new javax.swing.JButton();
        pnlEliminar = new javax.swing.JPanel();
        pnlBorrarVideo = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cmbArchivoEliminarVideo = new javax.swing.JComboBox<>();
        btnBorrarVideo = new javax.swing.JButton();
        cmbDirectorioEliminarVideo = new javax.swing.JComboBox<>();
        pnlCrearEliminarDir = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        cmbEliminarDirectorio = new javax.swing.JComboBox<>();
        btnAgregarDirectorio = new javax.swing.JButton();
        btnBorrarDirectorio = new javax.swing.JButton();
        txtNombreDirectorio = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        pnlBarra = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnRefrescar = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(0, 0));
        setMinimumSize(new java.awt.Dimension(1280, 713));
        setSize(new java.awt.Dimension(1280, 720));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Faster Intelligent ®");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, -1, -1));

        pnlBotonera.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotonera.setPreferredSize(new java.awt.Dimension(690, 60));

        btnCargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487017544_icon-81-document-add.png"))); // NOI18N
        btnCargar.setText("Cargar");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        btnVisualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ic_see-20 (1).png"))); // NOI18N
        btnVisualizar.setText("Visualizar");
        btnVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1489445585_vendingmachine.png"))); // NOI18N
        jButton2.setText("Máquinas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1489513442_Cancel.png"))); // NOI18N
        jButton3.setText("Salir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487291347_trash_bin.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnAsignar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1488844214_ic_add_to_photos_48px.png"))); // NOI18N
        btnAsignar.setText("Asignar");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotoneraLayout = new javax.swing.GroupLayout(pnlBotonera);
        pnlBotonera.setLayout(pnlBotoneraLayout);
        pnlBotoneraLayout.setHorizontalGroup(
            pnlBotoneraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotoneraLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );
        pnlBotoneraLayout.setVerticalGroup(
            pnlBotoneraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotoneraLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(pnlBotoneraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAsignar)
                    .addComponent(btnCargar)
                    .addComponent(btnEliminar)
                    .addComponent(btnVisualizar)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        getContentPane().add(pnlBotonera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, -1));

        pnlVisualizar.setBackground(new java.awt.Color(255, 255, 255));
        pnlVisualizar.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualizar"));

        lstArchivo.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstArchivo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstArchivoValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstArchivo);

        cmbDirectorioVisualizar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDirectorioVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioVisualizarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Informacion Video"));

        jLabel1.setText("Duracion :");

        jLabel2.setText("Alto Fotograma :");

        jLabel3.setText("Ancho Fotograma :");

        jLabel8.setText("Velocidad Datos :");

        jLabel10.setText("Velocidad Bits :");

        jLabel16.setText("Velocidad Fotograma :");

        txtDuracion.setEditable(false);

        txtVelocidadFotograma.setEditable(false);

        txtAnchoFotograma.setEditable(false);

        txtAltoFotograma.setEditable(false);

        txtVelocidadDatos.setEditable(false);

        txtVelocidadBits.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDuracion)
                    .addComponent(txtAnchoFotograma, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVelocidadDatos, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVelocidadBits, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVelocidadFotograma, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtAltoFotograma, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAnchoFotograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAltoFotograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtVelocidadDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtVelocidadBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtVelocidadFotograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Informacion Audio"));

        jLabel17.setText("Velocidad Bits :");

        jLabel18.setText("Canales :");

        jLabel19.setText("Velocidad Sonido :");

        txtVelocidadBitsSonido.setEditable(false);

        txtVelocidadSonido.setEditable(false);

        txtCanales.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCanales, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtVelocidadBitsSonido)
                    .addComponent(txtVelocidadSonido))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVelocidadBitsSonido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtCanales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtVelocidadSonido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        btnReproducir.setText("Reproducir");
        btnReproducir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReproducirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVisualizarLayout = new javax.swing.GroupLayout(pnlVisualizar);
        pnlVisualizar.setLayout(pnlVisualizarLayout);
        pnlVisualizarLayout.setHorizontalGroup(
            pnlVisualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVisualizarLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(pnlVisualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReproducir, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(cmbDirectorioVisualizar, 0, 225, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        pnlVisualizarLayout.setVerticalGroup(
            pnlVisualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVisualizarLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(cmbDirectorioVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(pnlVisualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlVisualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnReproducir)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        getContentPane().add(pnlVisualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1280, 580));

        pnlAsignar.setBackground(new java.awt.Color(255, 255, 255));
        pnlAsignar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Asignar"));
        pnlAsignar.setPreferredSize(new java.awt.Dimension(1274, 582));

        pnlCargaMac.setBackground(new java.awt.Color(255, 255, 255));
        pnlCargaMac.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Mac", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCargaMac.setPreferredSize(new java.awt.Dimension(630, 330));

        jLabel40.setText("Lugar :");

        jLabel39.setText("Archivo :");

        jLabel38.setText("Direcctorios :");

        jLabel37.setText("Mac :");

        jLabel21.setText("Nombre Maquina :");

        txtNombreMaquina.setEditable(false);

        cmbMac.setToolTipText("");
        cmbMac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMacActionPerformed(evt);
            }
        });
        cmbMac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cmbMacKeyTyped(evt);
            }
        });

        cmbDirectorioMac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cmbDirectorioMac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioMacActionPerformed(evt);
            }
        });

        cmbLugarMac.setMaximumRowCount(20);
        cmbLugarMac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "video1", "video2", "video3", "video4", "video5", "video6", "video7", "video8", "video9", "video10", "video11", "video12", "video13", "video14", "video15", "video16", "video17", "video18", "video19", "video20" }));

        btnCargarMac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487282891_icon-130-cloud-upload.png"))); // NOI18N
        btnCargarMac.setText("Cargar");
        btnCargarMac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarMacActionPerformed(evt);
            }
        });

        jLabel7.setText("Filtro:");

        txtFiltroMac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroMacKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pnlCargaMacLayout = new javax.swing.GroupLayout(pnlCargaMac);
        pnlCargaMac.setLayout(pnlCargaMacLayout);
        pnlCargaMacLayout.setHorizontalGroup(
            pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaMacLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCargaMacLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(txtNombreMaquina))
                    .addGroup(pnlCargaMacLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37)
                            .addComponent(jLabel39)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCargaMacLayout.createSequentialGroup()
                                .addComponent(cmbArchivoMac, 0, 224, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel40)
                                .addGap(18, 18, 18)
                                .addComponent(cmbLugarMac, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbDirectorioMac, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbMac, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtFiltroMac))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCargaMacLayout.createSequentialGroup()
                .addContainerGap(252, Short.MAX_VALUE)
                .addComponent(btnCargarMac)
                .addGap(249, 249, 249))
        );
        pnlCargaMacLayout.setVerticalGroup(
            pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaMacLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtNombreMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFiltroMac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(12, 12, 12)
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(cmbMac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(cmbDirectorioMac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaMacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLugarMac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArchivoMac)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCargarMac)
                .addContainerGap())
        );

        pnlCargaMarca.setBackground(new java.awt.Color(255, 255, 255));
        pnlCargaMarca.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Marca", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCargaMarca.setPreferredSize(new java.awt.Dimension(630, 318));

        jLabel12.setText("Marca :");

        jLabel13.setText("Direcctorios :");

        jLabel14.setText("Archivo :");

        jLabel15.setText("Lugar :");

        cmbMarca.setPreferredSize(new java.awt.Dimension(443, 27));

        cmbDirectorioMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioMarcaActionPerformed(evt);
            }
        });

        cmbArchivoMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbArchivoMarcaActionPerformed(evt);
            }
        });

        cmbLugarMarca.setMaximumRowCount(20);
        cmbLugarMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "video1", "video2", "video3", "video4", "video5", "video6", "video7", "video8", "video9", "video10", "video11", "video12", "video13", "video14", "video15", "video16", "video17", "video18", "video19", "video20" }));

        btnCargarMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487282891_icon-130-cloud-upload.png"))); // NOI18N
        btnCargarMarca.setText("Cargar");
        btnCargarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarMarcaActionPerformed(evt);
            }
        });

        jLabel20.setText("Filtro:");

        txtFiltroMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroMarcaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pnlCargaMarcaLayout = new javax.swing.GroupLayout(pnlCargaMarca);
        pnlCargaMarca.setLayout(pnlCargaMarcaLayout);
        pnlCargaMarcaLayout.setHorizontalGroup(
            pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaMarcaLayout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(btnCargarMarca)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlCargaMarcaLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(58, 58, 58)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFiltroMarca)
                    .addComponent(cmbDirectorioMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbArchivoMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbLugarMarca, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbMarca, javax.swing.GroupLayout.Alignment.TRAILING, 0, 1, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCargaMarcaLayout.setVerticalGroup(
            pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaMarcaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFiltroMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(cmbDirectorioMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cmbArchivoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCargaMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbLugarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addComponent(btnCargarMarca)
                .addGap(18, 18, 18))
        );

        pnlCargaTipo.setBackground(new java.awt.Color(255, 255, 255));
        pnlCargaTipo.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Tipo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCargaTipo.setPreferredSize(new java.awt.Dimension(100, 330));

        jLabel27.setText("Tipo :");

        jLabel28.setText("Direcctorios :");

        jLabel29.setText("Archivo :");

        jLabel30.setText("Lugar :");

        cmbTipo.setPreferredSize(new java.awt.Dimension(126, 27));

        cmbDirectorioTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioTipoActionPerformed(evt);
            }
        });

        cmbLugarTipo.setMaximumRowCount(20);
        cmbLugarTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "video1", "video2", "video3", "video4", "video5", "video6", "video7", "video8", "video9", "video10", "video11", "video12", "video13", "video14", "video15", "video16", "video17", "video18", "video19", "video20" }));

        btnCargarTipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487282891_icon-130-cloud-upload.png"))); // NOI18N
        btnCargarTipo.setText("Cargar");
        btnCargarTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarTipoActionPerformed(evt);
            }
        });

        jLabel9.setText("Filtro:");

        txtFiltroTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroTipoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout pnlCargaTipoLayout = new javax.swing.GroupLayout(pnlCargaTipo);
        pnlCargaTipo.setLayout(pnlCargaTipoLayout);
        pnlCargaTipoLayout.setHorizontalGroup(
            pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaTipoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCargaTipoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTipo, 0, 443, Short.MAX_VALUE)
                            .addComponent(cmbDirectorioTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbArchivoTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbLugarTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlCargaTipoLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(txtFiltroTipo)))
                .addContainerGap())
            .addGroup(pnlCargaTipoLayout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(btnCargarTipo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCargaTipoLayout.setVerticalGroup(
            pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaTipoLayout.createSequentialGroup()
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(cmbDirectorioTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(cmbArchivoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCargaTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(cmbLugarTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCargarTipo)
                .addContainerGap())
        );

        pnlCargaTodo.setBackground(new java.awt.Color(255, 255, 255));
        pnlCargaTodo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Todo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pnlCargaTodo.setPreferredSize(new java.awt.Dimension(630, 319));

        jLabel41.setText("Directorio :");

        jLabel44.setText("Archivo :");

        jLabel45.setText("Lugar :");

        cmbDirectorioTodo.setPreferredSize(new java.awt.Dimension(443, 27));
        cmbDirectorioTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioTodoActionPerformed(evt);
            }
        });

        cmbLugarTodo.setMaximumRowCount(20);
        cmbLugarTodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "video1", "video2", "video3", "video4", "video5", "video6", "video7", "video8", "video9", "video10", "video11", "video12", "video13", "video14", "video15", "video16", "video17", "video18", "video19", "video20" }));

        btnCargarTodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487282891_icon-130-cloud-upload.png"))); // NOI18N
        btnCargarTodo.setText("Cargar");
        btnCargarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCargaTodoLayout = new javax.swing.GroupLayout(pnlCargaTodo);
        pnlCargaTodo.setLayout(pnlCargaTodoLayout);
        pnlCargaTodoLayout.setHorizontalGroup(
            pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCargaTodoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCargarTodo)
                .addGap(235, 235, 235))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCargaTodoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbDirectorioTodo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbArchivoTodo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbLugarTodo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCargaTodoLayout.setVerticalGroup(
            pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCargaTodoLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel41)
                    .addComponent(cmbDirectorioTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbArchivoTodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(43, 43, 43)
                .addGroup(pnlCargaTodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLugarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addGap(18, 18, 18)
                .addComponent(btnCargarTodo)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout pnlAsignarLayout = new javax.swing.GroupLayout(pnlAsignar);
        pnlAsignar.setLayout(pnlAsignarLayout);
        pnlAsignarLayout.setHorizontalGroup(
            pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAsignarLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCargaMac, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                    .addComponent(pnlCargaTipo, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCargaTodo, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addComponent(pnlCargaMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlAsignarLayout.setVerticalGroup(
            pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAsignarLayout.createSequentialGroup()
                .addGroup(pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCargaMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .addComponent(pnlCargaMac, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAsignarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCargaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlCargaTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getContentPane().add(pnlAsignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 74, 1280, 590));

        pnlEliminar.setBackground(new java.awt.Color(255, 255, 255));
        pnlEliminar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Eliminar"));
        pnlEliminar.setPreferredSize(new java.awt.Dimension(1274, 582));

        pnlBorrarVideo.setBackground(new java.awt.Color(255, 255, 255));
        pnlBorrarVideo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true)), "Borrar Video"));

        jLabel23.setText("Directorio :");

        jLabel24.setText("Video :");

        cmbArchivoEliminarVideo.setEditable(true);

        btnBorrarVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487291347_trash_bin.png"))); // NOI18N
        btnBorrarVideo.setText("Borrar");
        btnBorrarVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarVideoActionPerformed(evt);
            }
        });

        cmbDirectorioEliminarVideo.setEditable(true);
        cmbDirectorioEliminarVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDirectorioEliminarVideoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBorrarVideoLayout = new javax.swing.GroupLayout(pnlBorrarVideo);
        pnlBorrarVideo.setLayout(pnlBorrarVideoLayout);
        pnlBorrarVideoLayout.setHorizontalGroup(
            pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBorrarVideoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbArchivoEliminarVideo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDirectorioEliminarVideo, 0, 336, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBorrarVideoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBorrarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );
        pnlBorrarVideoLayout.setVerticalGroup(
            pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBorrarVideoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cmbDirectorioEliminarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlBorrarVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbArchivoEliminarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(31, 31, 31)
                .addComponent(btnBorrarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        pnlCrearEliminarDir.setBackground(new java.awt.Color(255, 255, 255));
        pnlCrearEliminarDir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true), "Crear o Eliminar directorio"));

        jLabel25.setText("Directorio :");

        cmbEliminarDirectorio.setEditable(true);
        cmbEliminarDirectorio.setActionCommand("");
        cmbEliminarDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEliminarDirectorioActionPerformed(evt);
            }
        });

        btnAgregarDirectorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487878919_free-10.png"))); // NOI18N
        btnAgregarDirectorio.setText("Agregar");
        btnAgregarDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarDirectorioActionPerformed(evt);
            }
        });

        btnBorrarDirectorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1487291347_trash_bin.png"))); // NOI18N
        btnBorrarDirectorio.setText("Borrar");
        btnBorrarDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarDirectorioActionPerformed(evt);
            }
        });

        jLabel32.setText("Nombre :");

        javax.swing.GroupLayout pnlCrearEliminarDirLayout = new javax.swing.GroupLayout(pnlCrearEliminarDir);
        pnlCrearEliminarDir.setLayout(pnlCrearEliminarDirLayout);
        pnlCrearEliminarDirLayout.setHorizontalGroup(
            pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel32)))
                .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombreDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(cmbEliminarDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addComponent(btnBorrarDirectorio, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                        .addGap(21, 21, 21))
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addComponent(btnAgregarDirectorio)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlCrearEliminarDirLayout.setVerticalGroup(
            pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(cmbEliminarDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarDirectorio))
                .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(btnAgregarDirectorio)
                        .addGap(84, 84, 84))
                    .addGroup(pnlCrearEliminarDirLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(pnlCrearEliminarDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtNombreDirectorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout pnlEliminarLayout = new javax.swing.GroupLayout(pnlEliminar);
        pnlEliminar.setLayout(pnlEliminarLayout);
        pnlEliminarLayout.setHorizontalGroup(
            pnlEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEliminarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(pnlBorrarVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(pnlCrearEliminarDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );
        pnlEliminarLayout.setVerticalGroup(
            pnlEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEliminarLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(pnlEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlBorrarVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCrearEliminarDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(203, Short.MAX_VALUE))
        );

        getContentPane().add(pnlEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 74, 1280, 590));

        pnlBarra.setBackground(new java.awt.Color(255, 255, 255));
        pnlBarra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlBarraLayout = new javax.swing.GroupLayout(pnlBarra);
        pnlBarra.setLayout(pnlBarraLayout);
        pnlBarraLayout.setHorizontalGroup(
            pnlBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1278, Short.MAX_VALUE)
        );
        pnlBarraLayout.setVerticalGroup(
            pnlBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(pnlBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 66, 1280, -1));

        lblImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1_Primary_logo_on_transparent_432x63.png"))); // NOI18N
        getContentPane().add(lblImagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 0, 410, -1));

        jLabel4.setText("By Faster");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 40, -1, -1));

        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("Faster Intelligent ®");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jMenu1.setText("Archivo");

        mnRefrescar.setText("Refrescar");
        mnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRefrescarActionPerformed(evt);
            }
        });
        jMenu1.add(mnRefrescar);

        jMenuItem1.setText("Logout");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ayuda");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMacActionPerformed
        txtNombreMaquina.setText(nombresMaquinas.get(cmbMac.getSelectedIndex()));
        CargarCmbLugar ccv = new CargarCmbLugar(cmbLugarMac);
        ccv.execute();
        while (!ccv.isDone() && !ccv.isCancelled()) {
        }
    }//GEN-LAST:event_cmbMacActionPerformed

    private void cmbDirectorioMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioMacActionPerformed
        new CargarCmbArchivo(cmbArchivoMac, cmbDirectorioMac.getSelectedItem().toString()).execute();
    }//GEN-LAST:event_cmbDirectorioMacActionPerformed

    private void cmbDirectorioTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioTipoActionPerformed
        new CargarCmbArchivo(cmbArchivoTipo, cmbDirectorioTipo.getSelectedItem().toString()).execute();
    }//GEN-LAST:event_cmbDirectorioTipoActionPerformed

    private void cmbDirectorioMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioMarcaActionPerformed
        new CargarCmbArchivo(cmbArchivoMarca, cmbDirectorioMarca.getSelectedItem().toString()).execute();
    }//GEN-LAST:event_cmbDirectorioMarcaActionPerformed

    private void cmbDirectorioTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioTodoActionPerformed
        new CargarCmbArchivo(cmbArchivoTodo, cmbDirectorioTodo.getSelectedItem().toString()).execute();
    }//GEN-LAST:event_cmbDirectorioTodoActionPerformed

    private void btnCargarMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarMacActionPerformed
        // AGREGAR POR MAC
        Asignar a = new Asignar(cmbDirectorioMac, cmbArchivoMac, cmbLugarMac, cmbMac.getSelectedItem().toString(), "mac");
        a.execute();
        while (!a.isCancelled() && !a.isDone()) {
        }
    }//GEN-LAST:event_btnCargarMacActionPerformed

    private void btnCargarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarTipoActionPerformed
        //AGREGAR POR TIPO
        Asignar a = new Asignar(cmbDirectorioTipo, cmbArchivoTipo, cmbLugarTipo, cmbTipo.getSelectedItem().toString(), "tipo");
        a.execute();
        while (!a.isCancelled() && !a.isDone()) {
        }
    }//GEN-LAST:event_btnCargarTipoActionPerformed

    private void btnCargarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarMarcaActionPerformed
        //AGREGAR POR MARCA
        Asignar a = new Asignar(cmbDirectorioMarca, cmbArchivoMarca, cmbLugarMarca, cmbMarca.getSelectedItem().toString(), "marca");
        a.execute();
        while (!a.isCancelled() && !a.isDone()) {
        }
    }//GEN-LAST:event_btnCargarMarcaActionPerformed

    private void btnCargarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarTodoActionPerformed
        //AGREGAR A TODO
        Asignar a = new Asignar(cmbDirectorioTodo, cmbArchivoTodo, cmbLugarTodo, "todo");
        a.execute();
        while (!a.isCancelled() && !a.isDone()) {
        }
    }//GEN-LAST:event_btnCargarTodoActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        //ABRE LA VENTANA PARA CARGAR VIDEOS AL FTP
        new Cargar().setVisible(true);
    }//GEN-LAST:event_btnCargarActionPerformed

    private void btnBorrarVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarVideoActionPerformed
        //ELIMINA EL VIDEO SELECCIONADO DE LA CARPETA SELECCIONADA EN EL PANEL DE ELIMINAR
        BorrarVideoFTP bv = new BorrarVideoFTP();
        bv.execute();
        while (!bv.isDone() && !bv.isCancelled()) {
        }
        CargarCmbArchivo cca = new CargarCmbArchivo(cmbArchivoEliminarVideo, cmbDirectorioEliminarVideo.getSelectedItem().toString());
        cca.execute();
        while (!cca.isDone() && !cca.isCancelled()) {
        }
    }//GEN-LAST:event_btnBorrarVideoActionPerformed

    private void cmbDirectorioEliminarVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioEliminarVideoActionPerformed
        //CARGA LOS ARCHIVOS DEPENDIENDO DE EL DIRECTORIO SELECCIONADO EN EL PANEL ELIMINAR
        CargarCmbArchivo cca = new CargarCmbArchivo(cmbArchivoEliminarVideo, cmbDirectorioEliminarVideo.getSelectedItem().toString());
        cca.execute();
        while (!cca.isDone() && !cca.isCancelled()) {
        }
    }//GEN-LAST:event_cmbDirectorioEliminarVideoActionPerformed

    private void cmbEliminarDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEliminarDirectorioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEliminarDirectorioActionPerformed

    private void btnAgregarDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarDirectorioActionPerformed
        //CREA UN DIRECTORIO CON EL NOMBRE ESPECIFICADO EN EL PANEL DE ELIMINAR
        AgregarDirectorio ad = new AgregarDirectorio();
        ad.execute();
        while (!ad.isDone() && !ad.isCancelled()) {
        }
        CargarDirectorios();
    }//GEN-LAST:event_btnAgregarDirectorioActionPerformed

    private void btnBorrarDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarDirectorioActionPerformed
        //BORRA EL DIRECTORIO SELECCIONADO EN EL PANEL ELIMINAR
        BorrarDirectorio bd = new BorrarDirectorio();
        bd.execute();
        while (!bd.isDone() && !bd.isCancelled()) {
        }
        CargarDirectorios();
    }//GEN-LAST:event_btnBorrarDirectorioActionPerformed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        //ESTE METODO HACE VISIBLE EL PANEL ASIGNAR E INVISIBLE LOS PANELES DE ELIMINAR Y VISUALIZAR
        pnlAsignar.setVisible(true);
        pnlEliminar.setVisible(false);
        pnlVisualizar.setVisible(false);
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        //ESTE METODO HACE VISIBLE EL PANEL ELIMINAR E INVISIBLE LOS PANELES DE VISUALIZAR Y ASIGNAR
        pnlEliminar.setVisible(true);
        pnlAsignar.setVisible(false);
        pnlVisualizar.setVisible(false);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void lstArchivoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstArchivoValueChanged
        //CARGA LOS DETALLES DEL VIDEO SELECCIONADO EN LA LISTA DE VISUALIZAR
        CargarDetallesArchivoVisualizar cdav = new CargarDetallesArchivoVisualizar();
        cdav.execute();
        while (!cdav.isDone() && !cdav.isCancelled()) {
        }
    }//GEN-LAST:event_lstArchivoValueChanged

    private void cmbDirectorioVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDirectorioVisualizarActionPerformed
        //CARGA LOS ARCHIVO CADA VEZ QUE SELECCIONA UN DIRECTORIO EN DETALLES
        CargarCmbArchivo cca = new CargarCmbArchivo(lstArchivo, cmbDirectorioVisualizar.getSelectedItem().toString());
        cca.execute();
        while (!cca.isDone() && !cca.isCancelled()) {
        }
    }//GEN-LAST:event_cmbDirectorioVisualizarActionPerformed

    private void btnVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarActionPerformed
        //ESTE METODO HACE VISIBLE EL PANEL VISUALIZAR E INVISIBLE LOS PANELES DE ELIMINAR Y ASIGNAR
        pnlVisualizar.setVisible(true);
        pnlEliminar.setVisible(false);
        pnlAsignar.setVisible(false);
    }//GEN-LAST:event_btnVisualizarActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        //ESTE MÉTODO CIERRA EL PROGRAMA
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void cmbMacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMacKeyTyped

    }//GEN-LAST:event_cmbMacKeyTyped

    private void cmbArchivoMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArchivoMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbArchivoMarcaActionPerformed

    private void txtFiltroMacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroMacKeyTyped
        Filtro(cmbMac, txtFiltroMac, "mac");
    }//GEN-LAST:event_txtFiltroMacKeyTyped

    private void txtFiltroTipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroTipoKeyTyped
        Filtro(cmbTipo, txtFiltroTipo, "tipo");
    }//GEN-LAST:event_txtFiltroTipoKeyTyped

    private void txtFiltroMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroMarcaKeyTyped
        Filtro(cmbMarca, txtFiltroMarca, "marca");
    }//GEN-LAST:event_txtFiltroMarcaKeyTyped

    private void mnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRefrescarActionPerformed
        new Refrescar().execute();
        this.dispose();
    }//GEN-LAST:event_mnRefrescarActionPerformed

    private void btnReproducirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReproducirActionPerformed
        try {
            String video = Global.Encriptar("http://www.smartpoint.cl/SMARTPOINT TWO/"+cmbDirectorioVisualizar.getSelectedItem().toString()+"/"+lstArchivo.getSelectedValue()).replace("=", "-");
            String URL = "http://www.smartpoint.cl/reproducir.php?link="+video;
            java.awt.Desktop.getDesktop().browse(new URI(URL.replace(" ", "%20")));
        } catch (URISyntaxException ex) {
            Global.MostrarMensaje("Error al reproducir el video\n\nError: "+ex.getMessage(), "Error");
        } catch (IOException ex) {
            Global.MostrarMensaje("Error al reproducir el video\n\nError: "+ex.getMessage(), "Error");
        }
    }//GEN-LAST:event_btnReproducirActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Maquinas().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
     
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDirectorio;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnBorrarDirectorio;
    private javax.swing.JButton btnBorrarVideo;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCargarMac;
    private javax.swing.JButton btnCargarMarca;
    private javax.swing.JButton btnCargarTipo;
    private javax.swing.JButton btnCargarTodo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnReproducir;
    private javax.swing.JButton btnVisualizar;
    private javax.swing.JComboBox<String> cmbArchivoEliminarVideo;
    private javax.swing.JComboBox<String> cmbArchivoMac;
    private javax.swing.JComboBox<String> cmbArchivoMarca;
    private javax.swing.JComboBox<String> cmbArchivoTipo;
    private javax.swing.JComboBox<String> cmbArchivoTodo;
    private javax.swing.JComboBox<String> cmbDirectorioEliminarVideo;
    private javax.swing.JComboBox<String> cmbDirectorioMac;
    private javax.swing.JComboBox<String> cmbDirectorioMarca;
    private javax.swing.JComboBox<String> cmbDirectorioTipo;
    private javax.swing.JComboBox<String> cmbDirectorioTodo;
    private javax.swing.JComboBox<String> cmbDirectorioVisualizar;
    private javax.swing.JComboBox<String> cmbEliminarDirectorio;
    private javax.swing.JComboBox<String> cmbLugarMac;
    private javax.swing.JComboBox<String> cmbLugarMarca;
    private javax.swing.JComboBox<String> cmbLugarTipo;
    private javax.swing.JComboBox<String> cmbLugarTodo;
    private javax.swing.JComboBox<String> cmbMac;
    private javax.swing.JComboBox<String> cmbMarca;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImagen;
    public javax.swing.JList<String> lstArchivo;
    private javax.swing.JMenuItem mnRefrescar;
    private javax.swing.JPanel pnlAsignar;
    private javax.swing.JPanel pnlBarra;
    private javax.swing.JPanel pnlBorrarVideo;
    private javax.swing.JPanel pnlBotonera;
    private javax.swing.JPanel pnlCargaMac;
    private javax.swing.JPanel pnlCargaMarca;
    private javax.swing.JPanel pnlCargaTipo;
    private javax.swing.JPanel pnlCargaTodo;
    private javax.swing.JPanel pnlCrearEliminarDir;
    private javax.swing.JPanel pnlEliminar;
    private javax.swing.JPanel pnlVisualizar;
    public javax.swing.JTextField txtAltoFotograma;
    public javax.swing.JTextField txtAnchoFotograma;
    public javax.swing.JTextField txtCanales;
    public javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtFiltroMac;
    private javax.swing.JTextField txtFiltroMarca;
    private javax.swing.JTextField txtFiltroTipo;
    private javax.swing.JTextField txtNombreDirectorio;
    private javax.swing.JTextField txtNombreMaquina;
    public javax.swing.JTextField txtVelocidadBits;
    public javax.swing.JTextField txtVelocidadBitsSonido;
    public javax.swing.JTextField txtVelocidadDatos;
    public javax.swing.JTextField txtVelocidadFotograma;
    public javax.swing.JTextField txtVelocidadSonido;
    // End of variables declaration//GEN-END:variables
}
