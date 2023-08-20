package librarypass.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.table.DefaultTableModel;

public class ModelProfilo extends DefaultTableModel implements KeyListener {

    public ModelProfilo(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
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