package librarypass.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;

import librarypass.DataBase;
import librarypass.entity.Profilo;
import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;
import librarypass.model.ModelProfilo;
import librarypass.repository.ProfiloRepository;
import librarypass.service.Cripter;
import librarypass.service.ProfiloService;
import librarypass.serviceImpl.ProfiloServiceImpl;
import librarypass.utils.Constants;


public class ProfiliView extends JFrameOwner implements ActionListener, MouseListener, ComponentListener{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel, southPanel, northPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton bNuovo, bApri;
	private ModelProfilo model;
	private TableRowSorter<ModelProfilo> sorter;
	private RowFilter<Object, Object> rf;
	private JPopupMenu menuPopup;
	private JMenuItem itemElimina, itemApri, itemNuovo;
	
	private JMenuBar menuBar;
    private JMenu file, modifica, about;
    private JMenuItem nuovo, apri, esci, elimina, impostazioni, info;
	
	private ProfiloRepository profiloRepository;
	private ProfiloService profiloService;
	
	public ProfiliView() {
		
		this.profiloRepository = new ProfiloRepository();
		this.profiloService = new ProfiloServiceImpl(null);
		
		onInit();
	}
	
	
	
	private void onInit() {
		mainPanel = new JPanel(new BorderLayout());
		
		String colonne[] = {"Nome","Data creazione", "Data ultima Modifica"};
		Object data[][] = {};
		this.model = new ModelProfilo(data, colonne);
        this.sorter = new TableRowSorter<ModelProfilo>(model);
        this.table = new JTable(model);
        this.table.setRowSorter(sorter);
        
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        
        table.setRowSelectionAllowed(true);
        table.setAutoCreateRowSorter(true);
        table.setShowGrid(true); // Mostra le linee della griglia
        table.setShowHorizontalLines(true); // Mostra le linee delle righe
        table.addMouseListener(this);
                
        // menu bar
        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        modifica = new JMenu("Modifica");
        modifica.setMnemonic(KeyEvent.VK_M);
        
        about = new JMenu("About");
        
        // menu bar item
        nuovo = new JMenuItem("Nuovo");
        nuovo.addActionListener(this);
        
        apri = new JMenuItem("Apri");
        apri.addActionListener(this);
        
        esci = new JMenuItem("Esci");
        esci.addActionListener(this);
        
        elimina = new JMenuItem("Elimina");
        elimina.addActionListener(this);
                
        @SuppressWarnings("deprecation")
		KeyStroke ctrlI = KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask());
        impostazioni = new JMenuItem("Impostazioni");
        impostazioni.setAccelerator(ctrlI);
        impostazioni.addActionListener(this);
        
        info = new JMenuItem("Info");
        info.addActionListener(this);

        northPanel = new JPanel(new BorderLayout());

        // Menu Popup 
        menuPopup = new JPopupMenu();
        itemNuovo = new JMenuItem("Nuovo");
        itemNuovo.addActionListener(this);
        
        itemElimina = new JMenuItem("Elimina");
        itemElimina.addActionListener(this);
        
        itemApri = new JMenuItem("Apri");
        itemApri.addActionListener(this);
        
        southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        bNuovo = new JButton("Nuovo");
        bNuovo.addActionListener(this);
        
        bApri = new JButton("Apri");
        bApri.addActionListener(this);
        
        bNuovo.setPreferredSize(new Dimension(100, 25));  

        bApri.setPreferredSize(new Dimension(100, 25)); 

        southPanel.add(bApri);
        southPanel.add(bNuovo);

        // popup menu
        menuPopup.add(itemNuovo);
        menuPopup.add(itemApri);
        menuPopup.add(itemElimina);
        
        // menu bar 
        file.add(nuovo);
        file.add(apri);
        file.add(esci);
        
        modifica.add(elimina);
        
        about.add(impostazioni);
        about.add(info);
        
        menuBar.add(file);
        menuBar.add(modifica);
        menuBar.add(about);
        northPanel.add(menuBar);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        this.addComponentListener(this);
        this.add(mainPanel);
        this.setSize(new Dimension(Constants.x, Constants.y));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        updateModel();
        updateTitle();
        
     // Ordina la tabella inizialmente per la colonna "Nome"
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
	}
	
	private void updateModel(){
		
        List<Profilo> lstProfilo;
		try {
			String colonne[] = {"Nome","Data creazione", "Data ultima Modifica"};
			lstProfilo = profiloRepository.getAll();
			
			int size = lstProfilo.size();
		        
	        Object data[][] = new Object[size][3];
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        for(int i = 0; i<size; i++){
	            data[i][0] = lstProfilo.get(i).getNome();
	            data[i][1] = lstProfilo.get(i).getDataCreazione().format(formatter).toString();
	            data[i][2] = lstProfilo.get(i).getUltimaModifica().format(formatter).toString();
	        }
	        this.model = new ModelProfilo(data, colonne);
	        List<? extends SortKey> sortKeys =  sorter.getSortKeys();
	        sorter = new TableRowSorter<ModelProfilo>(model);
	        sorter.setRowFilter(rf);
	        sorter.setSortKeys(sortKeys);
	        this.table.setModel(model);
	        this.table.setRowSorter(sorter);
	        this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
       
    }
	
	
	public void updateTitle() {
    	String center = "Elenco Profili";
    	String title = Constants.version;

    	/*Font f = Fonts.getInstance().createFont();
    	FontMetrics fm = this.getFontMetrics(f);
    	int x = fm.stringWidth(center);
    	int y = fm.stringWidth(" ");
    	int z = (this.getWidth()/2 - (x/2)) - fm.stringWidth(title)-30;
    	int w = (z/y);
    	String pad ="";
    	pad = String.format("%"+w+"s", pad);*/
    	this.setTitle(title+" - "+center);
    }
	
	private void refresh(){
		this.repaint();
	        
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if("Nuovo".equals(e.getActionCommand())){
			CreaProfiloView cpw = null;
			cpw = new CreaProfiloView(this);
			
			// Blocca l'esecuzione finché il dialogo non viene chiuso
			cpw.setModal(true);
			
			updateModel();
        }
		else if("Esci".equals(e.getActionCommand())){ 
			this.dispose();
		}
		else if ("Apri".equals(e.getActionCommand())){
			apriProfilo();
		}
		else if("Elimina".equals(e.getActionCommand())) {
			eliminaProfilo();
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

	@Override
	public void mouseClicked(MouseEvent e) {
		 if (e.getClickCount() == 2) { // Controlla il doppio click
             apriProfilo();
             
            // System.out.println("Doppio click sulla riga " + selectedRow);
         }
		
	}
	
	private void apriProfilo() {
		int selectedRow = table.getSelectedRow();
        
        if (selectedRow != -1 ) {
			int modelRow = table.convertRowIndexToModel(selectedRow); 
			String nome = model.getValueAt(modelRow, 0).toString();
			Profilo profilo = profiloRepository.findProfiloByName(nome);
			DataBase db;
			if(( db = checkPassword(profilo)) != null ) {
				
				VisualizzaPasswordView mw = new VisualizzaPasswordView(db);
				this.dispose();
			}
        }
        else {
        	JOptionPane.showMessageDialog(null, 
	                "Non è stato selezionato alcun elemento da aprire!", // Testo del messaggio
	                "Attenzione",JOptionPane.WARNING_MESSAGE);
        }
	}
	
	private void eliminaProfilo() {
		int selectedRow = table.getSelectedRow();
		 
        if (selectedRow != -1 ) {
       	 	int modelRow = table.convertRowIndexToModel(selectedRow);
            String nome = model.getValueAt(modelRow, 0).toString();
            Profilo profilo = profiloRepository.findProfiloByName(nome);
            if( checkPassword(profilo) != null ) { // se la password è corretta 
            	eliminaProfiilo(profilo, modelRow);
            }
        }
        else {
       	 JOptionPane.showMessageDialog(null, 
		                "Non è stato selezionato alcun elemento da eliminare", // Testo del messaggio
		                "Attenzione",JOptionPane.WARNING_MESSAGE);
        }
	}
	
	private DataBase checkPassword(Profilo profilo)  {
		DataBase db = null;
		String key = profilo.getKey();
		String password = null;
		try {
			JLabel p = new JLabel("Password");
	        JPasswordField pf = new JPasswordField();
	        Object ob[] = {p, pf};
	        pf.requestFocus();
	        int okCxl = JOptionPane.showConfirmDialog(null, ob, "Inserisci Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        String pass = new String(pf.getPassword());
	        if(okCxl==0){
	        	Cripter cripter = Cripter.getCripter(profilo.getCryptType());
	            cripter.setKey(pass);
	            if(cripter.encrypt(pass).equals(key)){
	                if(!pass.equals("") && key!=null){
	                    cripter.setKey(pass);
	                    if(cripter.encrypt(pass).equals(key)){
	                    	String dec = cripter.decrypt(key);
	                    	db = new DataBase(profilo, pass);
	                    }
	                }
	             }
	            else {
	                JOptionPane.showMessageDialog(null, "Password errata !", "Errore", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	        else{
	           
	        }
			
		}
		catch(Exception e) {}
		
		
		return db;
	}
	
	private void eliminaProfiilo(Profilo profilo, int modelRow) {
		int choice = showEliminaProfilo();
		if (choice == JOptionPane.YES_OPTION) {
			profiloService.delete(profilo);
            model.removeRow(modelRow);
		} else if (choice == JOptionPane.NO_OPTION) {
		   
		}
	}
	private int showEliminaProfilo() {
        String[] options = {"Si", "No"};
        return JOptionPane.showOptionDialog(
            null,
            "Eliminando il profilo non sarà più possibile recuperarlo, continuare?",
            "Attenzione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
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
		menuPopup.add(itemNuovo);
       
		int row = table.rowAtPoint(e.getPoint());
        
		if (row >= 0 && row < table.getRowCount()) {
            menuPopup.add(itemApri);
            menuPopup.add(itemElimina);
            
            table.setRowSelectionInterval(row, row);
        } else {
            table.clearSelection();
        }
        menuPopup.show(e.getComponent(), e.getX(), e.getY());
    }

	@Override
	public void componentResized(ComponentEvent e) {
		this.updateTitle();
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		this.updateTitle();
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}


}
