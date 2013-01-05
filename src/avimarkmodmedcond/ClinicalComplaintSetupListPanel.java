package avimarkmodmedcond;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
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
    private final int maxNumberOfCategories = 30;


    ClinicalComplaintSetupListPanel() {
        this.setSize(800,700);
        this.setLayout(new GridLayout(maxNumberOfClinicalComplaints+1,1,1,1));
        categoriesJButton = new JButton("Update Categories");
        categoriesJButton.addActionListener(this);
        this.add(categoriesJButton);
        categoriesJPanel = new JPanel();
        categoriesJPanel.setLayout(new GridLayout(maxNumberOfCategories/2,1));
        
        for (int i=0;i<maxNumberOfCategories;i+=2){
            
            JTextField tempJTextField = new JTextField("",30);
            JTextField tempJTextField2 = new JTextField("",30);
            JPanel tempJPanel = new JPanel();
            tempJPanel.setLayout(new GridLayout(1,2));
            
            tempJPanel.add(tempJTextField);
            tempJPanel.add(tempJTextField2);
            categoriesJPanel.add(tempJPanel);
        }
        
    }
    
    public void set(ClinicalComplaints clinicalComplaints){
        this.removeAll();
        this.updateUI();
        categoriesJButton = new JButton("Update Categories");
        categoriesJButton.addActionListener(this);
        this.add(categoriesJButton);
        categoriesJPanel = new JPanel();
        categoriesJPanel.setLayout(new GridLayout(maxNumberOfCategories/2,1));
        
        for (int i=0;i<maxNumberOfCategories;i+=2){
            JTextField tempJTextField = new JTextField("",30);
            JTextField tempJTextField2 = new JTextField("",30);
            if(i<clinicalComplaints.getCategories().size()){
                tempJTextField.setText(clinicalComplaints.getCategories().get(i));
            }
            if(i+1<clinicalComplaints.getCategories().size()){
                tempJTextField2.setText(clinicalComplaints.getCategories().get(i+1));
            }
            JPanel tempJPanel = new JPanel();
            tempJPanel.setLayout(new GridLayout(1,2));
            
            tempJPanel.add(tempJTextField);
            tempJPanel.add(tempJTextField2);
            categoriesJPanel.add(tempJPanel);
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
            if (o instanceof JPanel){
                for(Object o2 : ((JPanel) o).getComponents()){
                    if (o2 instanceof JTextField){
                        if(!((JTextField)o2).getText().isEmpty()){
                            listOfCategories.add(((JTextField)o2).getText());
                        }
                    }
                }
            }
        }
        return listOfCategories;
    }    
    
    
    /**
     *
     * @param cc
     */
    public void addClinicalComplaint(){
        if (this.getComponents().length == maxNumberOfClinicalComplaints+1){
            JOptionPane.showMessageDialog(this, "Maximum number of clinical complaints reached");
            return;
        }
        
        ClinicalComplaintSetupPanel tmp = new ClinicalComplaintSetupPanel(null,this);
        this.add(tmp);
        this.updateUI();
        
    }
    
    void upClinicalComplaintSetupPanel(ClinicalComplaintSetupPanel ccsp){
       ArrayList<ClinicalComplaintSetupPanel> reorderedListOfClinicalComplaintSetupPanel = new ArrayList<>();
       for(Object o : this.getComponents()){
            if(o instanceof ClinicalComplaintSetupPanel){
                ClinicalComplaintSetupPanel currentCcsp = (ClinicalComplaintSetupPanel) o;
                if(ccsp == currentCcsp){
                    //If it is the first we don't do anything         
                    if (reorderedListOfClinicalComplaintSetupPanel.isEmpty()){
                        return;
                    }
                    //Swap
                    ClinicalComplaintSetupPanel temp = reorderedListOfClinicalComplaintSetupPanel.get(reorderedListOfClinicalComplaintSetupPanel.size()-1);
                    reorderedListOfClinicalComplaintSetupPanel.remove(reorderedListOfClinicalComplaintSetupPanel.size()-1);
                    reorderedListOfClinicalComplaintSetupPanel.add(currentCcsp);
                    reorderedListOfClinicalComplaintSetupPanel.add(temp);
                 }
                 else{
                    reorderedListOfClinicalComplaintSetupPanel.add(currentCcsp);
                 }
                 this.remove(ccsp);
            }
       }
       for(ClinicalComplaintSetupPanel cssp : reorderedListOfClinicalComplaintSetupPanel){
           this.add(cssp);
       }
       this.updateUI();
    }
    
    void downClinicalComplaintSetupPanel(ClinicalComplaintSetupPanel ccsp){
       
       ArrayList<ClinicalComplaintSetupPanel> reorderedListOfClinicalComplaintSetupPanel = new ArrayList<>();
       ClinicalComplaintSetupPanel found = null;
       for(Object o : this.getComponents()){
            if(o instanceof ClinicalComplaintSetupPanel){
                ClinicalComplaintSetupPanel currentCcsp = (ClinicalComplaintSetupPanel) o;
              
                if(ccsp == currentCcsp){
                    found = currentCcsp;
                 }
                 else{
                    reorderedListOfClinicalComplaintSetupPanel.add(currentCcsp);
                    if(null != found){
                        reorderedListOfClinicalComplaintSetupPanel.add(found);
                        found = null;
                    }
                 }
                 this.remove(ccsp);
            }
       }
       if(null != found){
            reorderedListOfClinicalComplaintSetupPanel.add(found);
       }
       
       for(ClinicalComplaintSetupPanel cssp : reorderedListOfClinicalComplaintSetupPanel){
           this.add(cssp);
       }
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
