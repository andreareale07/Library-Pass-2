package librarypass.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import librarypass.entity.Profilo;
import librarypass.repository.ProfiloRepository;
import librarypass.service.Cripter;
import librarypass.service.ProfiloService;
import librarypass.serviceImpl.AESEncryption;
import librarypass.serviceImpl.Cript;
import librarypass.serviceImpl.ProfiloServiceImpl;
import librarypass.utils.Constants;

public class CreaProfiloView extends JDialog implements ActionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;

	private JPanel mainPanel, bottoni;
	
	private JLabel lNome, lPassword, lCrypter;
	
	private JTextField tNome, tPassword;
	
	private JButton bSalva, bAnnulla;
	
	private JComboBox<String> boxCrypter;
	
	private ProfiloRepository profiloRepository;
	private ProfiloService profiloService;
	
	public CreaProfiloView(ProfiliView f) {
		super(f, "Aggiungi profilo", true);
		profiloRepository = new ProfiloRepository();
		this.onInit(f);
	}

	private void onInit(ProfiliView owner) {
		
		mainPanel = new JPanel(new GridBagLayout());
		 
		lNome = new JLabel("Nome profilo:");
		tNome = new JTextField(15);
		tNome.addFocusListener(new CustomFocusListener("Profilo"));
		tNome.addKeyListener(this); 
		
		lPassword = new JLabel("Password:");
		tPassword = new JTextField(15);
		tPassword.setForeground(Color.GRAY);
		tPassword.setText("Password");
		tPassword.addFocusListener(new CustomFocusListener("Password")); 
		tPassword.addKeyListener(this);
		
		bSalva = new JButton("Salva");
		bSalva.addActionListener(this);
		bSalva.addKeyListener(this);
		
		bAnnulla = new JButton("Annulla");
		bAnnulla.addActionListener(this);
		
		// crypter
		lCrypter = new JLabel("Tipo Criptazione:");
		boxCrypter = new JComboBox<String>(Constants.lstCrypter);
		boxCrypter.setName("boxCrypter");
		boxCrypter.addKeyListener(this); 
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.insets = new Insets(5, 5, 5, 5); // Margine tra i componenti
        gbc.gridx = 0; // Prima colonna
        gbc.gridy = 0; // Prima riga	
		mainPanel.add(lNome, gbc);
		
		gbc.gridx = 1;
		mainPanel.add(tNome, gbc);
		
		gbc.gridx = 0; 
        gbc.gridy = 1;
		mainPanel.add(lPassword, gbc);
		
		gbc.gridx = 1;
		mainPanel.add(tPassword, gbc);
		
		gbc.gridx = 0; 
        gbc.gridy = 2;
		mainPanel.add(lCrypter, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		mainPanel.add(boxCrypter, gbc);
		
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setHgap(25);
		bottoni = new JPanel(fl);
		
		bottoni.add(bSalva);
		bottoni.add(bAnnulla);
		
		gbc.anchor = GridBagConstraints.BASELINE;
		gbc.gridx = 0; 
        gbc.gridy = 3;
        gbc.gridwidth = 2;
		mainPanel.add(bottoni, gbc);

		mainPanel.setBorder(new EmptyBorder(20, 0, 30, 0)); // Imposta il padding
		
		this.add(mainPanel);
		
		//this.setSize(new Dimension(320, 200));
		this.setSize(owner.getWidth(), 300);
	    this.setLocation(owner.getLocation().x, owner.getLocation().y+owner.getHeight()-400);
        this.setVisible(true);
        this.setTitle("Aggiungi profilo");
        this.setResizable(false);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if("Salva".equals(e.getActionCommand())){
			 if(validatore()) {
				 try {
					 String nome = this.tNome.getText();
					 String password = this.tPassword.getText();
					 String tipoCrypter = this.boxCrypter.getSelectedItem().toString();
					 
					 profiloService = new ProfiloServiceImpl(Cripter.getCripter(tipoCrypter));
					 
					 Profilo profilo = profiloService.createProfilo(nome, password, tipoCrypter);
					 boolean check = profiloRepository.save(profilo);
					 if(check) {
						 this.dispose(); //chiudo il frame
					 }
					 else {
						 JOptionPane.showMessageDialog(null, 
					                "Il nome profilo scelto è già presente in elenco!", // Testo del messaggio
					                "Attenzione",JOptionPane.WARNING_MESSAGE);
					 }
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }
			 else {
				 JOptionPane.showMessageDialog(
			                null, 
			                "Lunghezza password o nome non sufficiente!", // Testo del messaggio
			                "Attenzione", // Titolo della finestra
			                JOptionPane.WARNING_MESSAGE // Icona di avviso
			            );
			 }
		 }
		 else if("Annulla".equals(e.getActionCommand())) {
			 this.dispose();
		 }
		
	}
	
	private boolean validatore() {
		boolean check = true;
		String nome = this.tNome.getText();
		String password = this.tPassword.getText();
		
		if(nome == null) check = false;
		else if(nome.length() < 3 ) check = false;
		
		if(password == null) check = false;
		else if(password.length() < 4) check = false;
		
		return check;
	}
	
	private class CustomFocusListener implements FocusListener{
		private String suggestion;
		
		public CustomFocusListener(String value) {
			this.suggestion = value;
		}
		@Override
		public void focusGained(FocusEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JTextField) {
				JTextField tmp = (JTextField) obj;
				if(tmp.getText().equals(suggestion)) {
					tmp.setText("");
					tmp.setForeground(new JTextField().getForeground());
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JTextField) {
				JTextField tmp = (JTextField) obj;
				if(tmp.getText().isEmpty()) {
					tmp.setText(suggestion);
					tmp.setForeground(Color.GRAY);
				}
			}
			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			if(tNome.isFocusOwner()) {
				tPassword.requestFocus(); // Sposta il focus al secondo campo
			}
			else if(tPassword.isFocusOwner()) {
				boxCrypter.requestFocus();
			}
			else if(boxCrypter.isFocusOwner()) {
				bSalva.doClick();
			}
			
            
        }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
