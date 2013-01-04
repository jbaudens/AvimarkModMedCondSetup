package avimarkmodmedcond;

import com.google.common.base.Joiner;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
public class BodySystemSetupPanel extends JPanel implements ActionListener{
    
    private JTextField nameJTextField;
    private JLabel symmetryJLabel;
    private JComboBox isSymmetricalJComboBox;
    private JTextField listOfPredefinedCommentsJTextField;
    private JButton deleteJButton;
    private BodySystemSetupListPanel parent;
   
    /**
     *
     * @param bs
     * @param parent
     */
    public BodySystemSetupPanel(BodySystem bs,BodySystemSetupListPanel parent){
        this.parent = parent;
        
        nameJTextField = new JTextField(bs.getName(),20);
        
        symmetryJLabel = new JLabel("Symmetry: ");
        
        isSymmetricalJComboBox = new JComboBox();
        isSymmetricalJComboBox.addItem("Yes");
        isSymmetricalJComboBox.addItem("No");
        if(false == bs.getIsSymmetrical()){
            isSymmetricalJComboBox.setSelectedItem("No");
        }
        else{
            isSymmetricalJComboBox.setSelectedItem("Yes");
        }
        
        listOfPredefinedCommentsJTextField = new JTextField(Joiner.on(',').skipNulls().join(bs.getListOfPredefinedComments()),50);
        
        deleteJButton = new JButton("Delete");
        deleteJButton.addActionListener(this);
        
        
  
        this.setSize(800,25);
           
        this.setLayout(null);
        this.addComponent(this.nameJTextField,10,0,250,25);
        this.addComponent(this.symmetryJLabel,260,0,70,25);
        this.addComponent(this.isSymmetricalJComboBox,330,0,70,25);
        this.addComponent(this.listOfPredefinedCommentsJTextField,400,0,320,25);
        this.addComponent(this.deleteJButton,720,0,70,25);
    }
    
    private void addComponent(Component c,int x,int y,int width,int height)
    {
        c.setBounds(x,y,width,height);
        this.add(c);
    }
    
    /**
     *
     * @return
     */
    public BodySystem toBodySystem(){
        List<String> listOfPredefinedComments = Arrays.asList(this.listOfPredefinedCommentsJTextField.getText().split(","));
        BodySystem bs= new BodySystem();
        bs.setName(this.nameJTextField.getText());
        bs.setIsSymmetrical(this.isSymmetricalJComboBox.getSelectedItem() == "Yes" ? true : false);
        bs.setListOfPredefinedComments(new ArrayList<>(listOfPredefinedComments));
        return bs;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.parent.refresh();
    }
}
