/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avimarkmodmedcond;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author jbaudens
 */
public class QuestionJPanel extends JPanel {
    JTextField questionJTextField;
    JTextField textToDisplayJTextField;
    JComboBox categoriesJComboBox;
    
    QuestionJPanel(Question question,ArrayList<String> listOfCategories){
        this.setLayout(new GridLayout(1,3));
        questionJTextField = new JTextField("",50);
        textToDisplayJTextField = new JTextField("",50);
        categoriesJComboBox = new JComboBox(listOfCategories.toArray());
        
        questionJTextField.setText(question.getQuestion());
        textToDisplayJTextField.setText(question.getTextToDisplay());
        categoriesJComboBox.setSelectedItem(question.getCategory());
        
        this.add(questionJTextField);
        this.add(textToDisplayJTextField);
        this.add(categoriesJComboBox);
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
    
    
}
