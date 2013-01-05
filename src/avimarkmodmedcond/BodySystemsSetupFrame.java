package avimarkmodmedcond;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.yaml.snakeyaml.Yaml;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jbaudens
 */
public class BodySystemsSetupFrame extends JFrame implements ActionListener,KeyListener{
    private final JPanel contentPane;
    private BodySystemSetupListPanel bodySystemSetupListPanel;
    private ClinicalComplaintSetupListPanel clinicalComplaintSetupListPanel;
    private JMenuItem openMenu;
    private JMenuItem saveMenu;
    private JButton addJButton;
    private JPanel miscPanel;
    private JFileChooser fc;
    private JPanel hotKeyPanel;
    private JLabel toggleVisibilityJLabel;
    private JTextField toggleVisibilityHotKey;
    private MyKeyStroke toggleVisibilityKeyStroke = new MyKeyStroke();
    private MyKeyStroke closeKeyStroke = new MyKeyStroke();
    private JMenuBar menubar;
    private JMenu menu;
    private JTabbedPane tabbedPane;
    private String tabLabelBodySystems = "Body Systems";
    private String tabLabelClinicalComplaints = "Clinical Complaints";
    
    /**
     *
     * @param title
     * @throws HeadlessException
     */
    public BodySystemsSetupFrame(String title) throws HeadlessException {
        super(title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double x = screenSize.width*0.05;
        double y = screenSize.height*0.05;
        double width = screenSize.width*0.9;
        double height = screenSize.height*0.9;
        setBounds((int)Math.round(x),(int)Math.round(y),825,(int)Math.round(height));
        
        //Setup File Chooser
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("YAML Files", "yml");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        
        //Set Menu
        menubar = new JMenuBar();
        menu = new JMenu("File");
        openMenu = new JMenuItem("Open"); 
        openMenu.addActionListener(this);        
        
        saveMenu = new JMenuItem("Save"); 
        saveMenu.addActionListener(this);        
        menu.add(openMenu);
        menu.add(saveMenu);
        menubar.add(menu);
        this.setJMenuBar(menubar);
        
        //bodySystemSetupListPanel
        bodySystemSetupListPanel = new BodySystemSetupListPanel();
        double bodySystemPanelHeight = height*0.87;
        bodySystemSetupListPanel.setBounds(0, 0, 800, (int)Math.round(bodySystemPanelHeight));
        
        //clinicalComplaintSetupListPanel
        clinicalComplaintSetupListPanel = new ClinicalComplaintSetupListPanel();
        double clinicalComplaintPanelHeight = height*0.87;
        clinicalComplaintSetupListPanel.setBounds(0, 0, 800, (int)Math.round(clinicalComplaintPanelHeight));
        
        //Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(tabLabelClinicalComplaints, clinicalComplaintSetupListPanel);
        tabbedPane.addTab(tabLabelBodySystems, bodySystemSetupListPanel);
        
        
        miscPanel = new JPanel();
        miscPanel.setLayout(new GridLayout(1,3,0,0));
        double miscPanelHeight = 30;
        miscPanel.setBounds(5,(int)Math.round(bodySystemPanelHeight),800,(int)Math.round(miscPanelHeight));

        addJButton = new JButton("Add"); 
        addJButton.addActionListener(this);
        
        toggleVisibilityJLabel = new JLabel("Toggle Visibility Hot Key");
        toggleVisibilityHotKey = new JTextField();
        toggleVisibilityHotKey.addKeyListener(this);  
        
        miscPanel.add(toggleVisibilityJLabel);
        miscPanel.add(toggleVisibilityHotKey);
        miscPanel.add(addJButton);

        contentPane = (JPanel)this.getContentPane();
        contentPane.add(tabbedPane,BorderLayout.CENTER);
        contentPane.add(miscPanel,BorderLayout.SOUTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
 
    public void serialize(String absolutePath) throws IOException{
        Yaml yaml = new Yaml();
        try (FileWriter fileWriter = new FileWriter(absolutePath)) {
            ArrayList<Object> list = new ArrayList();
            BodySystems bodySystems = new BodySystems();
            bodySystems.setListOfBodySystems(this.bodySystemSetupListPanel.getListOfBodySystems());
            list.add(bodySystems);
            ClinicalComplaints clinicalComplaints = new ClinicalComplaints();
            clinicalComplaints.setListOfClinicalComplaints(this.clinicalComplaintSetupListPanel.getListOfClinicalComplaints());
            clinicalComplaints.setCategories(this.clinicalComplaintSetupListPanel.getListOfCategories());
            list.add(clinicalComplaints);            
            list.add(this.toggleVisibilityKeyStroke);
            yaml.dump(list, fileWriter);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() instanceof JMenuItem){
            JMenuItem source = (JMenuItem)e.getSource();

            if (source == this.openMenu){
                int returnVal = fc.showOpenDialog(this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    Yaml yaml = new Yaml();
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file.getAbsolutePath());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(BodySystemsSetupFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ArrayList listOfObjects = (ArrayList)yaml.load(fileInputStream);

                    for(Object o : listOfObjects){
                        if (o instanceof BodySystems){
                            bodySystemSetupListPanel.set((BodySystems)o);
                        }
                        else if (o instanceof ClinicalComplaints){
                            clinicalComplaintSetupListPanel.set((ClinicalComplaints)o);
                        }
                        else if (o instanceof MyKeyStroke){
                                this.toggleVisibilityKeyStroke = (MyKeyStroke)o;
                                this.toggleVisibilityHotKey.setText(this.toggleVisibilityKeyStroke.getKeyStroke());
                        }
                    }

                }            
            }
            else if (source == this.saveMenu) {
                int returnVal = fc.showSaveDialog(this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.      
                    if (source == saveMenu){
                        try {
                            serialize(file.getAbsolutePath());
                        } catch (IOException ex) {
                            Logger.getLogger(BodySystemsSetupFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        else{
            if(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals(tabLabelBodySystems)){
                this.bodySystemSetupListPanel.addBodySystem(new BodySystem());
            }
            else if(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals(tabLabelClinicalComplaints)){
                this.clinicalComplaintSetupListPanel.addClinicalComplaint();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == this.toggleVisibilityHotKey){
            if (KeyEvent.VK_BACK_SPACE != e.getKeyCode()){
                String text = Tools.keyStroke2String(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()));
                this.toggleVisibilityHotKey.setText(text);
                this.toggleVisibilityKeyStroke.setKeyStroke(text);
            }
            else{
                this.toggleVisibilityHotKey.setText("");
                this.toggleVisibilityKeyStroke.setKeyStroke("control alt O");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
