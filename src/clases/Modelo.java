/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author david
 */
public class Modelo extends AbstractListModel implements ComboBoxModel {

    ArrayList<String> modelo = new ArrayList<>();

    String selection = null;

    @Override
    public Object getElementAt(int index) {
        return modelo.get(index);
    }

    @Override
    public int getSize() {
        return modelo.size();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (String) anItem; // to select and register an
    } // item from the pull-down list

    // Methods implemented from the interface ComboBoxModel
    @Override
    public Object getSelectedItem() {
        return selection; // to add the selection to the combo box
    }

    public void setModelo(ArrayList modelo) {
        this.modelo = modelo;
    }

    public ArrayList<String> getModelo() {
        return this.modelo;
    }
}
