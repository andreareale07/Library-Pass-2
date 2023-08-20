/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypass.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import librarypass.DataBase;
import librarypass.entity.Voce;
import librarypass.gui.setting.Fonts;
import librarypass.model.Model;
import librarypass.utils.Constants;

/**
 *
 * @author Andrea Reale
 */
public class VisualizzaPasswordView extends JFrameOwner implements ActionListener, MouseListener, WindowListener{
    	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel, southPanel,northPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu file, modifica, about;
    private JMenuItem apriProfili, salva, chiudi, mostra, elimina, nascondi, impostazioni, info;
    private JLabel lDescrizione,lUser, lPassword, lSearch;
    private JTextField tDescrizione,tUser, tPassword;
    private JButton bAggiungi;
    private JTextField searchBar;
    private Model model;
    private TableRowSorter<Model> sorter;
    private RowFilter<Object, Object> rf;
    
    private JPopupMenu menuPopup;
	private JMenuItem itemElimina;
    
    private DataBase database;

    public VisualizzaPasswordView(DataBase database){
    	this.database = database;
    	onInit();
    	updateModel();
    }
    
    
    private void onInit() {
	
		mainPanel = new JPanel(new BorderLayout());
		 
		String colonne[] = {"Descrizione","Utente", "Password"};
		Object data[][] = {};
		model = new Model(data, colonne);
		sorter = new TableRowSorter<Model>(model);
		table = new JTable(model);
		table.setRowSorter(sorter);
		 
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		 
		table.setRowSelectionAllowed(true);
		table.setAutoCreateRowSorter(true);
		table.setShowGrid(true); // Mostra le linee della griglia
	    table.setShowHorizontalLines(true); // Mostra le linee delle righe
		table.addMouseListener(this);
		
		// Menu Popup 
        menuPopup = new JPopupMenu();
        itemElimina = new JMenuItem("Elimina");
        itemElimina.addActionListener(this);
		
		// menu bar
		menuBar = new JMenuBar();
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		 
		KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		apriProfili = new JMenuItem("Apri Profili");
		apriProfili.setAccelerator(ctrlA);
		apriProfili.addActionListener(this);
		
		salva = new JMenuItem("Salva");
		salva.addActionListener(this);
		
		chiudi = new JMenuItem("Esci");
		chiudi.addActionListener(this);
		 
		modifica = new JMenu("Modifica");
		modifica.setMnemonic(KeyEvent.VK_M);
		elimina = new JMenuItem("Elimina");
		elimina.addActionListener(this);
		mostra = new JMenuItem("Mostra Password");
		mostra.addActionListener(this);
		
		nascondi = new JMenuItem("Nascondi Password");
		
		nascondi.addActionListener(this);
		
		// ABOUT 
		about = new JMenu("Aiuto");
		
		KeyStroke ctrlI = KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask());
        impostazioni = new JMenuItem("Impostazioni");
        impostazioni.setAccelerator(ctrlI);
        impostazioni.addActionListener(this);
        
		about.setMnemonic(KeyEvent.VK_I);
		info = new JMenuItem("Info");
		info.addActionListener(this);
		
		lDescrizione = new JLabel("Descrizione");
		tDescrizione = new JTextField(20);
		
		lUser = new JLabel("User");
		tUser = new JTextField(20);
		
		lPassword = new JLabel("Password");
		tPassword = new JTextField(20);
		
		bAggiungi = new JButton("Aggiungi");
		bAggiungi.addActionListener(this);

		this.searchBar = new JTextField(32);
		searchBar.setForeground(Color.GRAY);
        searchBar.setText("Ricerca");
		searchBar.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (searchBar.getText().equals("Ricerca")) {
		            searchBar.setText("");
			        searchBar.setForeground(new JTextField().getForeground());
			    }
			}
			@Override
			public void focusLost(FocusEvent e) {
			    if (searchBar.getText().isEmpty()) {
			        searchBar.setForeground(Color.GRAY);
			        searchBar.setText("Ricerca");
			            }
			        }
		});
	    searchBar.addKeyListener(new searchKeyListener());
	    
	    lSearch = new JLabel("Ricerca:");
	    
	    southPanel = new JPanel(new GridBagLayout());
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // Margine tra i componenti
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
		gbc.gridx = 0; // Prima colonna
	    gbc.gridy = 0; // Prima riga	
	    southPanel.add(lDescrizione, gbc);
	    
	    gbc.gridy = 1;
	    southPanel.add(tDescrizione, gbc);
	    
	    gbc.gridx = 1; 
	    gbc.gridy = 0; 	
	    southPanel.add(lUser, gbc);
	    
	    gbc.gridy = 1; 
	    southPanel.add(tUser, gbc);
	    
	    gbc.gridx = 2; 
	    gbc.gridy = 0; 
	    southPanel.add(lPassword, gbc);
	    
	    gbc.gridy = 1; 
	    southPanel.add(tPassword, gbc);
	    
	    gbc.gridx = 3; 
	    gbc.gridy = 1; 
	    southPanel.add(bAggiungi, gbc);

	    northPanel = new JPanel(new GridBagLayout());
	    //searchPanel = new JPanel(new GridBagLayout());
	    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
	    gbc.gridx = 0; // Prima colonna
	    gbc.gridy = 0; // Prima riga
	    northPanel.add(lSearch, gbc);
	    
	    gbc.gridx = 1; // Prima colonna
	    northPanel.add(searchBar, gbc);
	    
	    
	    file.add(apriProfili);
	    file.add(salva);
	    file.add(chiudi);
	   // modifica.add(mostra);
	   // modifica.add(nascondi);
	    modifica.add(elimina);
	    about.add(impostazioni);
	    about.add(info);
	    
	    // menu bar
	    menuBar.add(file);
	    menuBar.add(modifica);
	    menuBar.add(about);
	    
	    // popup menu
        menuPopup.add(itemElimina);
	    
	    mainPanel.add(northPanel, BorderLayout.NORTH);
	    mainPanel.add(scrollPane, BorderLayout.CENTER);
	    mainPanel.add(southPanel, BorderLayout.SOUTH);
	    this.add(menuBar, BorderLayout.NORTH);
	    
	    this.add(mainPanel);
	   
	    this.setSize(new Dimension(Constants.x, Constants.y));
	    this.setLocationRelativeTo(null);
	    this.setVisible(true);
	    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(this);
	    updateTitle(database.getProfilo().getNome());
	    tDescrizione.requestFocus();
    }

    private void updateTitle(String value) {
    	String path = "Nuovo Portachiavi";
    	String title = Constants.version;
    	if(value != null) {
    		path = value;
    	}
    	
    	this.setTitle(title+" - "+path);
    }
    private void refresh(){
        this.repaint();
        
    }

   
    private void updateModel(){
        String colonne[] = {"Descrizione","User", "Password"};
        List<Voce> dataset = database.getAll();
        int size = dataset.size();
        Object data[][] = new Object[size][3];
        for(int i = 0; i<size; i++){
            data[i][0] = dataset.get(i).getDescrizione();
            data[i][1] = dataset.get(i).getUser();
            data[i][2] = dataset.get(i).getPassword();
        }
        this.model = new Model(data, colonne);
        sorter = new TableRowSorter<Model>(model);
        sorter.setRowFilter(rf);
        this.table.setModel(model);
        this.table.setRowSorter(sorter);
        this.refresh();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if("Apri Profili".equals(e.getActionCommand())) {
        	ProfiliView profiliView = new ProfiliView();
        	this.dispose();
        }
        else if("Salva".equals(e.getActionCommand())){
        	boolean canSave = false;
        
        	if(checkDifference()) { // Ci sono modifiche non salvate.
        		if(database.isInListDescUsr(getListFromModel())) {// Se la lista ha duplicati chiedo se li vogliono scartare.
                	if(showNotificaDuplicati() == JOptionPane.YES_OPTION) {
                		updateModel();
                		canSave = false;
                	}
                	else {
                		canSave = false; // ci sono dati sporchi, richiesto intervento utente.
                		JOptionPane.showMessageDialog(null, 
          		                "Impossibile procedere al salvataggio, ci sono elementi duplicati(descrizione, user)", // Testo del messaggio
          		                "Attenzione",JOptionPane.WARNING_MESSAGE);
                	}
                }
        		else {// Non ci sono elementio duplicati
                	canSave = true;
                }
        	}
 
        	if(canSave) {
        		salvaModifiche();
        	}
        }
        else if("Esci".equals(e.getActionCommand())){
        	proceduraDiChiusura();
        }
        else if("Elimina".equals(e.getActionCommand())){
        	int selectedRow = table.getSelectedRow();
			 
            if (selectedRow != -1 ) {
            	eliminaVoce(selectedRow);
            }
            else {
           	 JOptionPane.showMessageDialog(null, 
			                "Non è stato selezionato alcun elemento da eliminare", // Testo del messaggio,
			                "Attenzione",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if("Aggiungi".equals(e.getActionCommand())){
            Voce v = new Voce(tDescrizione.getText(),tUser.getText(), tPassword.getText());
            
            boolean canSave = false;
            if(checkDifference()) { // Ci sono modifiche non salvate.
            	 if(database.isInListDescUsr(getListFromModel())) {// Se la lista ha duplicati chiedo se li vogliono scartare.
                 	if(showNotificaDuplicati() == JOptionPane.YES_OPTION) {
                 		updateModel();
                 		canSave = true;
                 	}
                 	else {
                 		canSave = false; // ci sono dati sporchi, richiesto intervento utente.
                 	}
                 }
            	 else { // La lista non ha duplicati, chiedo se vogliono salvare le modifiche.
            		 salvaModificheBeforeAggiungi();
            		 canSave = true;
            	 }
            }
            else {// Non ci sono modifiche da salvare.
            	canSave = true;
            }
            if(canSave) {
            	  Boolean check = !database.isInListDescUsr(getListFromModel(), v);
                  
                  if(check) check = database.save(v);
                  
                  if(check == null) {
                  	JOptionPane.showMessageDialog(null, 
      		                "Si è verificato un errore durante il salvataggio, riprovare!", // Testo del messaggio
      		                "Attenzione",JOptionPane.ERROR_MESSAGE);
                  }
                  else if(check) {
      	        	Object nuovo[] = {v.getDescrizione(),v.getUser(), v.getPassword()}; 
      	            ((Model)table.getModel()).addRow(nuovo);
      	            this.tDescrizione.setText("");
      	            this.tUser.setText("");
      	            this.tPassword.setText("");
                  	JOptionPane.showMessageDialog(null, 
      		                "Elemento salvato correttamente!", // Testo del messaggio
      		                "Informazione",JOptionPane.INFORMATION_MESSAGE);
                  }
                  else if(!check) {
                  	JOptionPane.showMessageDialog(null, 
      		                "Impossibile salvare, la coppia descrizione e user è già presente in elenco!", // Testo del messaggio
      		                "Attenzione",JOptionPane.WARNING_MESSAGE);
                  }
                  
            }
            refresh();

        }
        else if("Impostazioni".equals(e.getActionCommand())) {
			ConfigurazioneView configView = new ConfigurazioneView(this); 
		}
        else if("Info".equals(e.getActionCommand())){
            JOptionPane.showMessageDialog(null, "Software sviluppato da Andrea Reale !"
                    + "\nOgni diritto sul software è da ritenersi proprietà dello sviluppatore."
                    + "\nContatto : andreareale07@gmail.com");
        }
    }
    

    public class searchKeyListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent ke) {}

        @Override
        public void keyPressed(KeyEvent ke) {}

        @Override
        public void keyReleased(KeyEvent ke) {
        	if(searchBar.isCursorSet()) {
        		newFilter();
        	} 
        }
    }
    
    private void newFilter() {
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)"+searchBar.getText(),0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        updateModel();
    }
    
    private  List<Voce> getListFromModel(){
    	List<Voce> modelList = new ArrayList<Voce>();

    	for(Vector voce : model.getDataVector()) {
    		
    		Voce v  = new Voce();
    		v.setDescrizione((String)voce.get(0));
    		v.setUser((String)voce.get(1));
    		v.setPassword((String)voce.get(2));
    		v.setStatusCrypt(false);
    		
    		modelList.add(v);
    	}
    	return modelList;
    }
    
   

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		
		proceduraDiChiusura();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	private void proceduraDiChiusura() {
		//se ci sono modifiche ai dati
		if(checkDifference()) { 
			
			// Controllo che tra le modifiche non ci siano dei duplicati
			if(database.isInListDescUsr(getListFromModel())) {
				notificaDuplicati();
			}
			else {
				chiusuraFinestra();
			}
			
		}
		else {
			this.dispose();
		}
		
	}
	//se ci sono modifiche torna true
    private Boolean checkDifference() {
    	List<Voce> modelList = getListFromModel();
    	return database.checkDifference(modelList);
    }
	
    private int notificaDuplicati() {
    	int choice = showNotificaDuplicati();
		if (choice == JOptionPane.YES_OPTION) {
		    this.dispose();
		} else if (choice == JOptionPane.NO_OPTION) {
		   
		}
		return choice;
    }
    
    private int showNotificaDuplicati() {
    	String[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(
            null,
            "Sono presenti record duplicati (Descrizione, Username), procedendo le modifiche verranno scartate, proseguire ?",
            "Attenzione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }
    
	private void chiusuraFinestra() {
		int choice = showChiusuraFinestra();
		if (choice == JOptionPane.YES_OPTION) {
		    database.saveAll(getListFromModel());
		    // Chiudi la finestra manualmente
		    this.dispose();
		} else if (choice == JOptionPane.CANCEL_OPTION) {
		    // Chiudi la finestra manualmente
		    this.dispose();
		}
		// Non fare nulla se scegli "Annulla"
	}
	
	private int showChiusuraFinestra() {
        String[] options = {"Salva", "Annulla", "Esci senza salvare"};
        return JOptionPane.showOptionDialog(
            null,
            "Vuoi salvare i dati prima di uscire?",
            "Salvataggio",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }
	
	private void salvaModificheBeforeAggiungi() {
		int choice = showSalvaModificheBeforeAggiungi();
		if (choice == JOptionPane.YES_OPTION) {
		    database.saveAll(getListFromModel());
		} else if (choice == JOptionPane.NO_OPTION) {
		   updateModel();
		}
	}
	private int showSalvaModificheBeforeAggiungi() {
        String[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(
            null,
            "Ci sono modifiche non salvate, vuoi salvare le modifiche prima di proseguire ? \nSe non salvi le modifiche andranno perse.",
            "Salvataggio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }

	private void salvaModifiche() {
		int choice = showSalvaModifiche();
		if (choice == JOptionPane.YES_OPTION) {
		    if(database.saveAll(getListFromModel())) {
		    	JOptionPane.showMessageDialog(null, 
  		                "Salvataggio completato!", // Testo del messaggio
  		                "Informazione",JOptionPane.INFORMATION_MESSAGE);
		    }
		    else {
		    	JOptionPane.showMessageDialog(null, 
  		                "Si è verificato un errore durante il salvataggio, riprovare!", // Testo del messaggio
  		                "Attenzione",JOptionPane.ERROR_MESSAGE);
		    }
		} else if (choice == JOptionPane.NO_OPTION) {

		}
	}
	private int showSalvaModifiche() {
        String[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(
            null,
            "Salvare le modifiche in sospeso?",
            "Salvataggio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }
	
	private int eliminaVoce(int selectedRow) {
    	int choice = showEliminaVoce();
		if (choice == JOptionPane.YES_OPTION) {
			int modelRow = table.convertRowIndexToModel(selectedRow);
            String descr = model.getValueAt(modelRow, 0).toString();
            String user = model.getValueAt(modelRow, 1).toString();
            database.delete(descr, user);
            model.removeRow(modelRow);
		} else if (choice == JOptionPane.NO_OPTION) {
		   
		}
		return choice;
    }
    
    private int showEliminaVoce() {
    	String[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(
            null,
            "Eliminando l'elemento selezionato non sarà più possibile recuperarlo, continuare?",
            "Attenzione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
            showPopupMenu(e);
        }
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
            showPopupMenu(e);
        }
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void showPopupMenu(MouseEvent e) {
		menuPopup.removeAll();
        int row = table.rowAtPoint(e.getPoint());
        if (row >= 0 && row < table.getRowCount()) {
        	
        	menuPopup.add(itemElimina);
        	
            table.setRowSelectionInterval(row, row);
        } else {
            table.clearSelection();
        }
        menuPopup.show(e.getComponent(), e.getX(), e.getY());
    }

	
	
}
