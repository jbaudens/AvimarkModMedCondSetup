package avimarkmodmedcond;

import java.awt.Component;
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
public class ClinicalComplaintSetupPanel extends JPanel implements ActionListener{
    
    private ClinicalComplaintSetupListPanel parent;
    private JTextField nameJTextField;
    private JButton updateQuestionsJButton;
    private JButton deleteJButton;
    private JButton upJButton;
    private JButton downJButton;
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
        
        if(null == cc){
            cc = new ClinicalComplaint();
        }
        
        nameJTextField = new JTextField(cc.getName(),30);
        
        updateQuestionsJButton = new JButton("Update Questions");
        updateQuestionsJButton.addActionListener(this);    
              
        
        questionsJPanel = new JPanel();
        questionsJPanel.setLayout(new GridLayout(maxNumberOfQuestions,1));
        
        for (int i=0;i<maxNumberOfQuestions;i++){
            
            
            JPanel questionJPanel = new QuestionJPanel(new Question(),parent.getListOfCategories(),this);
            if(i<cc.getListOfQuestions().size()){
                questionJPanel = new QuestionJPanel(cc.getListOfQuestions().get(i),parent.getListOfCategories(),this);
            }
            questionsJPanel.add(questionJPanel);
        }
        
        
        upJButton = new JButton("Up");
        upJButton.addActionListener(this);
        downJButton = new JButton("Down");
        downJButton.addActionListener(this);
        deleteJButton = new JButton("Delete");
        deleteJButton.addActionListener(this);
                
        this.setSize(800,25);
        this.add(nameJTextField);
        this.add(updateQuestionsJButton);
        this.add(upJButton);
        this.add(downJButton);
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
        if (e.getSource() == upJButton){
            this.parent.upClinicalComplaintSetupPanel(this);
        }
        if (e.getSource() == downJButton){
            this.parent.downClinicalComplaintSetupPanel(this);
        }     
        if (e.getSource() == deleteJButton){
            this.setEnabled(false);
            this.parent.refresh();
        }
        if (e.getSource() == updateQuestionsJButton){
            JOptionPane.showMessageDialog(parent,
                    questionsJPanel, "Enter the questions", JOptionPane.PLAIN_MESSAGE); 
        }
    }
    
    
    void upQuestionJPanel(QuestionJPanel qjp){
       ArrayList<QuestionJPanel> reorderedListOfQuestionJPanel = new ArrayList<>();
       for(Object o : questionsJPanel.getComponents()){
            if(o instanceof QuestionJPanel){
                QuestionJPanel currentQjp = (QuestionJPanel) o;
                if(qjp == currentQjp){
                    //If it is the first we don't do anything        
                    if (reorderedListOfQuestionJPanel.isEmpty()){
                        return;
                    }
                    //Swap
                    QuestionJPanel temp = reorderedListOfQuestionJPanel.get(reorderedListOfQuestionJPanel.size()-1);
                    reorderedListOfQuestionJPanel.remove(reorderedListOfQuestionJPanel.size()-1);
                    reorderedListOfQuestionJPanel.add(currentQjp);
                    reorderedListOfQuestionJPanel.add(temp);
                 }
                 else{
                    reorderedListOfQuestionJPanel.add(currentQjp);
                 }
                 questionsJPanel.remove(qjp);
            }
       }
       for(QuestionJPanel cssp : reorderedListOfQuestionJPanel){
           questionsJPanel.add(cssp);
       }
       questionsJPanel.updateUI();
    }
    
    void downQuestionJPanel(QuestionJPanel qjp){
       ArrayList<QuestionJPanel> reorderedListOfQuestionJPanel = new ArrayList<>();
       QuestionJPanel found = null;
       for(Object o : questionsJPanel.getComponents()){
            if(o instanceof QuestionJPanel){
                QuestionJPanel currentQjp = (QuestionJPanel) o;
                if(qjp == currentQjp){
                    found = currentQjp;
                 }
                 else{
                    reorderedListOfQuestionJPanel.add(currentQjp);
                    if(null != found){
                        reorderedListOfQuestionJPanel.add(found);
                        found = null;
                    }
                 }
                 questionsJPanel.remove(qjp);
            }
       }
       if(null != found){
           reorderedListOfQuestionJPanel.add(found);
       }
       for(QuestionJPanel cssp : reorderedListOfQuestionJPanel){
           questionsJPanel.add(cssp);
       }
       questionsJPanel.updateUI();        
    }
    
}
