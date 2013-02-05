package avimarkmodmedcond;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jbaudens
 */
public class BodySystemSetupListPanel extends JPanel {
    /**
     *
     */
    
    private final int maxNumberOfBodySystems = 20;

    BodySystemSetupListPanel() {
        this.setSize(800,700);
        this.setLayout(new GridLayout(maxNumberOfBodySystems,2,1,1));
    }
    
    public void set(BodySystems bodySystems){
        this.removeAll();
        this.updateUI();
        for (BodySystem bs : bodySystems.getListOfBodySystems()){
            BodySystemSetupPanel tmp = new BodySystemSetupPanel(bs,this);
            this.add(tmp);
        }
        this.updateUI();
    }
    
    /**
     *
     * @return
     */
    public ArrayList<BodySystem> getListOfBodySystems(){
        ArrayList<BodySystem> listOfBodySystems = new ArrayList<>();
        
        for(Object o : this.getComponents()){
            BodySystemSetupPanel bssp = (BodySystemSetupPanel) o;
            listOfBodySystems.add(bssp.toBodySystem());
        }
        
        return listOfBodySystems;
    }
    
    /**
     *
     * @param bs
     */
    public void addBodySystem(BodySystem bs){
        if (this.getComponents().length == maxNumberOfBodySystems){
            JOptionPane.showMessageDialog(this, "Maximum number of body systems reached");
            return;
        }
        
        BodySystemSetupPanel tmp = new BodySystemSetupPanel(bs,this);
        this.add(tmp);
        this.updateUI();
        
    }

    void refresh() {
        for(Object o : this.getComponents()){
            BodySystemSetupPanel bssp = (BodySystemSetupPanel) o;
            if(!bssp.isEnabled()){
                this.remove(bssp);
            }
        }
        this.updateUI();
    }
}
