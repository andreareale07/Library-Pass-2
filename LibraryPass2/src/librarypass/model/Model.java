package librarypass.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.table.DefaultTableModel;

public class Model extends DefaultTableModel implements KeyListener {

    public Model(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
       
    }

    @Override
    public void keyPressed(KeyEvent ke) {
     
    }

    @Override
    public void keyReleased(KeyEvent ke) {
       
    }
}