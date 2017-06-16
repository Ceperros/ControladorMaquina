
import java.io.IOException;
import javax.swing.SwingWorker;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author david
 */
public class NewClass {
    public static void main(String[] args){
        tmp t = new tmp();
        t.execute();
        while(!t.isDone() && !t.isCancelled()){}
    }
    
    private static class tmp extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws IOException {
            new Mensaje("mensaje", "titulo").setVisible(true);
            return null;
        }
    }
}
