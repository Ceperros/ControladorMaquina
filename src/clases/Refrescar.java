/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import javax.swing.SwingWorker;
import ventanas.Carga;
import ventanas.Principal;

public class Refrescar extends SwingWorker<Void, Void> {

    @Override
    public Void doInBackground() {
        Carga c = new Carga();
        c.setVisible(true);
        Principal p = new Principal();
        while (c.pbCarga.getValue() < 100) {
            c.pbCarga.setValue(Math.round(Global.carga));
            c.pbCarga.setString(Math.round(Global.carga) + "%");
        }
        p.setVisible(true);
        c.dispose();
        return null;
    }
}
