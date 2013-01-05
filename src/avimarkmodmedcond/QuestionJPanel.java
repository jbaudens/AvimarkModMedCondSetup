/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avimarkmodmedcond;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author jbaudens
 */
public class QuestionJPanel extends JPanel implements ActionListener{
    private JTextField questionJTextField;
    private JTextField textToDisplayJTextField;
    private JComboBox categoriesJComboBox;
    private JButton upJButton;
    private JButton downJButton;
    private ClinicalComplaintSetupPanel parent;
    
    QuestionJPanel(Question question,ArrayList<String> listOfCategories,ClinicalComplaintSetupPanel parent){
        this.parent = parent;
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double x = screenSize.width*0.05;
        double y = screenSize.height*0.05;
        double width = screenSize.width*0.9;
        double height = screenSize.height*0.9;
        setBounds((int)Math.round(x),(int)Math.round(y),(int)Math.round(width),(int)Math.round(height));
        
        
        questionJTextField = new JTextField("",30);
        textToDisplayJTextField = new JTextField("",20);
        
       
        categoriesJComboBox = new JComboBox(listOfCategories.toArray());
        categoriesJComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");
        
        
        questionJTextField.setText(question.getQuestion());
        textToDisplayJTextField.setText(question.getTextToDisplay());
        categoriesJComboBox.setSelectedItem(question.getCategory());

        
        upJButton = new JButton("Up");
        upJButton.addActionListener(this);
        downJButton = new JButton("Down");
        downJButton.addActionListener(this);
        
        this.add(questionJTextField);
        this.add(textToDisplayJTextField);
        this.add(categoriesJComboBox);
        this.add(upJButton);
        this.add(downJButton);
    }
    
    Question toQuestion(){
        Question q = null;
        if (!questionJTextField.getText().isEmpty()){        
            q = new Question();
            q.setCategory((String)categoriesJComboBox.getSelectedItem());
            q.setQuestion(questionJTextField.getText());
            q.setTextToDisplay(textToDisplayJTextField.getText());
        }
        return q;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == upJButton){
            this.parent.upQuestionJPanel(this);
        }
        if (e.getSource() == downJButton){
            this.parent.downQuestionJPanel(this);
        }    
    }
    
    
    
}
