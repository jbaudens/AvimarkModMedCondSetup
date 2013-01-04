package avimarkmodmedcond;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jbaudens
 */
public class ClinicalComplaintSetupListPanel extends JPanel implements ActionListener{
    /**
     *
     */
    
    private final int maxNumberOfClinicalComplaints = 20;
    private JButton categoriesJButton;
    private JPanel categoriesJPanel;
    private final int maxNumberOfCategories = 15;


    ClinicalComplaintSetupListPanel() {
        this.setSize(800,700);
        this.setLayout(new GridLayout(maxNumberOfClinicalComplaints+1,1,1,1));
        categoriesJButton = new JButton("Update Categories");
        categoriesJButton.addActionListener(this);
        this.add(categoriesJButton);
        categoriesJPanel = new JPanel();
        categoriesJPanel.setLayout(new GridLayout(maxNumberOfCategories,1));
        
        for (int i=0;i<maxNumberOfCategories;i++){
            JTextField tempJTextField = new JTextField("",30);
            tempJTextField.addActionListener(this);
            categoriesJPanel.add(tempJTextField);
        }
        
    }
    
    public void set(ClinicalComplaints clinicalComplaints){
        this.removeAll();
        this.updateUI();
        categoriesJButton = new JButton("Update Categories");
        categoriesJButton.addActionListener(this);
        this.add(categoriesJButton);
        categoriesJPanel = new JPanel();
        categoriesJPanel.setLayout(new GridLayout(maxNumberOfCategories,1));
        
        for (int i=0;i<maxNumberOfCategories;i++){
            JTextField tempJTextField = new JTextField("",30);
            if(i<clinicalComplaints.getCategories().size()){
                tempJTextField.setText(clinicalComplaints.getCategories().get(i));
            }
            categoriesJPanel.add(tempJTextField);
        }

        for (ClinicalComplaint cc : clinicalComplaints.getListOfClinicalComplaints()){
            ClinicalComplaintSetupPanel tmp = new ClinicalComplaintSetupPanel(cc,this);
            this.add(tmp);
        }
        this.updateUI();
    }
    
    /**
     *
     * @return
     */
    public ArrayList<ClinicalComplaint> getListOfClinicalComplaints(){
        ArrayList<ClinicalComplaint> listOfClinicalComplaints = new ArrayList<>();
        
        for(Object o : this.getComponents()){
            if (o instanceof ClinicalComplaintSetupPanel){
                ClinicalComplaintSetupPanel ccsp = (ClinicalComplaintSetupPanel) o;
                listOfClinicalComplaints.add(ccsp.toClinicalComplaint());
            }
        }
        
        return listOfClinicalComplaints;
    }
    
    public ArrayList<String> getListOfCategories(){
        ArrayList<String> listOfCategories = new ArrayList<>();
        
        for(Object o : categoriesJPanel.getComponents()){
            if (o instanceof JTextField){
                if(!((JTextField)o).getText().isEmpty()){
                    listOfCategories.add(((JTextField)o).getText());
                }
            }
        }
        return listOfCategories;
    }    
    
    
    /**
     *
     * @param cc
     */
    public void addClinicalComplaint(ClinicalComplaint cc){
        if (this.getComponents().length == maxNumberOfClinicalComplaints+1){
            JOptionPane.showMessageDialog(this, "Maximum number of clinical complaints reached");
            return;
        }
        
        ClinicalComplaintSetupPanel tmp = new ClinicalComplaintSetupPanel(cc,this);
        this.add(tmp);
        this.updateUI();
        
    }

    void refresh() {
        for(Object o : this.getComponents()){
            if(o instanceof ClinicalComplaintSetupPanel){
                ClinicalComplaintSetupPanel ccsp = (ClinicalComplaintSetupPanel) o;
                if(!ccsp.isEnabled()){
                    this.remove(ccsp);
                }
                ccsp.updateCategories();
            }
        }
        this.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == categoriesJButton){
            JOptionPane.showMessageDialog(this,
                    categoriesJPanel, "Enter the categories", JOptionPane.PLAIN_MESSAGE);
        }
        refresh();
    }
}
