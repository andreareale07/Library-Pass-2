package librarypass.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import librarypass.Configurazione;
import librarypass.gui.panel.ImpostazioniGrafichePanel;
import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;

public class ConfigurazioneView extends JFrameOwner implements ActionListener{

	private static final long serialVersionUID = -4744658651432669190L;
	
	private ImpostazioniGrafichePanel impostazioniGuiPanel;
	
	private JPanel mainPanel, southPanel;
	
	private JScrollPane menuPanel, bodyPanel;
	
	private JSplitPane splitPane; 
   
	private JButton bSalva, bAnnulla;
	
	private JFrameOwner owner;
	
	public ConfigurazioneView(JFrameOwner owner) {
		
		Theme.getInstance().addObserver(this);
		Zoom.getInstance().addObserver(this);
		Fonts.getInstance().addObserver(this);
		this.owner = owner;
		
		onInit();
	}
	
	private void onInit() {
		JTree menuTree = createMenuTree();
		menuPanel = new JScrollPane(menuTree);
		menuPanel.setMinimumSize(new Dimension(200, 400));
		
        impostazioniGuiPanel = new ImpostazioniGrafichePanel();
       
        
        bodyPanel = new JScrollPane(impostazioniGuiPanel);
        bodyPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bodyPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, bodyPanel);
        splitPane.setDividerLocation(200); // Imposta la posizione del divisore
        
        mainPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        bSalva = new JButton("Salva");
		bSalva.addActionListener(this);
		
		bAnnulla = new JButton("Annulla");
		bAnnulla.addActionListener(this);
        
		southPanel.add(bAnnulla);
		southPanel.add(bSalva);
		
		mainPanel.add(splitPane, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Imposta il padding
        this.add(mainPanel);
        
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(owner.getWidth(), 300);
        this.setLocation(owner.getLocation().x, owner.getLocation().y+owner.getHeight()-400);
        this.setVisible(true);
        
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if("Salva".equals(e.getActionCommand())){
			salvaConfigurazione();
			this.dispose();
		}
		else if("Annulla".equals(e.getActionCommand())){
			annullaModifiche();
			this.dispose();
		}
		
	}
	
	private void salvaConfigurazione() {
		Configurazione config = Configurazione.getInstance();
		Fonts.getInstance().salvaConfigurazione(config);
		Theme.getInstance().salvaConfigurazione(config);
		Zoom.getInstance().salvaConfigurazione(config);	
	}
	
	private void annullaModifiche() {
		 Configurazione.getInstance().discardChanges();
	}
	
	 private JTree createMenuTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Menu");
        
        root.breadthFirstEnumeration();
        
        DefaultMutableTreeNode ui = new DefaultMutableTreeNode("Impostazioni Grafiche");
        DefaultMutableTreeNode item2 = new DefaultMutableTreeNode("Sicurezza");
        DefaultMutableTreeNode item3 = new DefaultMutableTreeNode("Voce 3");

        //DefaultMutableTreeNode subItem1 = new DefaultMutableTreeNode("Sottomenu 1");
        //DefaultMutableTreeNode subItem2 = new DefaultMutableTreeNode("Sottomenu 2");

        //item1.add(subItem1);
        //item1.add(subItem2);

        root.add(ui);
        //root.add(item2);
        //root.add(item3);

        JTree menuTree = new JTree(new DefaultTreeModel(root));
        menuTree.setRootVisible(false); // Nasconde la radice
        
        TreePath path = new TreePath(ui.getPath());
        menuTree.setSelectionPath(path);
        
        //menuTree.setUI(new CustomTreeUI());
        return menuTree;
    }

}
