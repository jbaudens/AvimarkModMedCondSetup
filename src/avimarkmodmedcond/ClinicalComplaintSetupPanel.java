package avimarkmodmedcond;

import com.google.common.base.Joiner;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jbaudens
 */
public class ClinicalComplaintSetupPanel extends JPanel implements ActionListener{
    
    private ClinicalComplaintSetupListPanel parent;
    private JTextField nameJTextField;
    private JButton updateQuestionsJButton;
    private JButton deleteJButton;
    private JPanel questionsJPanel;
    private final int maxNumberOfQuestions = 15;
    
    
    void updateCategories(){
        
        for(Component c1 : questionsJPanel.getComponents()){
            if (c1 instanceof JPanel){
                for(Component c2 : ((JPanel)c1).getComponents()){
                    if (c2 instanceof JComboBox){
                        JComboBox comboBox = (JComboBox)c2;
                        String selected = (String)comboBox.getSelectedItem();
                        comboBox.removeAllItems();
                        for (String s:  parent.getListOfCategories()){
                            comboBox.addItem(s);
                        }
                        comboBox.setSelectedItem(selected);
                    }
                }
            }
        }

    }   
    
    
    /**
     *
     * @param cc
     * @param parent
     */
    public ClinicalComplaintSetupPanel(ClinicalComplaint cc,ClinicalComplaintSetupListPanel parent){
        this.parent = parent;
        
        nameJTextField = new JTextField(cc.getName(),20);
        
        updateQuestionsJButton = new JButton("Update Questions");
        updateQuestionsJButton.addActionListener(this);    
              
        
        questionsJPanel = new JPanel();
        questionsJPanel.setLayout(new GridLayout(maxNumberOfQuestions+1,1));
        
        JPanel header = new JPanel();
        header.setLayout(new GridLayout(1,3));
        JLabel questionJLabel = new JLabel("Question");
        questionJLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel textToDisplayJLabel = new JLabel("Alternative Text");
        textToDisplayJLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel categoriesJLabel = new JLabel("Categories");
        categoriesJLabel.setHorizontalAlignment(JLabel.CENTER);
        header.add(questionJLabel);
        header.add(textToDisplayJLabel);
        header.add(categoriesJLabel);
        questionsJPanel.add(header);
        for (int i=0;i<maxNumberOfQuestions;i++){
            
            
            JPanel questionJPanel = new QuestionJPanel(new Question(),parent.getListOfCategories());
            if(i<cc.getListOfQuestions().size()){
                questionJPanel = new QuestionJPanel(cc.getListOfQuestions().get(i),parent.getListOfCategories());
            }
            questionsJPanel.add(questionJPanel);
        }
        
        
        
        deleteJButton = new JButton("Delete");
        deleteJButton.addActionListener(this);
                
        this.setSize(800,25);
        this.setLayout(new GridLayout(1,3));
        this.add(nameJTextField);
        this.add(updateQuestionsJButton);
        this.add(deleteJButton);
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
    public ClinicalComplaint toClinicalComplaint(){
        ClinicalComplaint cc= new ClinicalComplaint();
        cc.setName(nameJTextField.getText());
        ArrayList<Question> listOfQuestions = new ArrayList<>();
        for(Component c1 : questionsJPanel.getComponents()){
            if (c1 instanceof QuestionJPanel){
                if(null != ((QuestionJPanel) c1).toQuestion()){
                    listOfQuestions.add(((QuestionJPanel) c1).toQuestion());
                }
            }
        }
        cc.setListOfQuestions(listOfQuestions);
        return cc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteJButton){
            this.setEnabled(false);
            this.parent.refresh();
        }
        if (e.getSource() == updateQuestionsJButton){
            JOptionPane.showMessageDialog(parent,
                    questionsJPanel, "Enter the questions", JOptionPane.PLAIN_MESSAGE); 
        }
    }
}
