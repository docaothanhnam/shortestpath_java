/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Nam Do
 */
public class myFrame extends JFrame{
    
    
    
    public myFrame(String title){
        setTitle(title);
        inIt();
    }
    public void inIt(){
        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
    
    
    }
    
    private JPanel createServerGui(){
        JPanel panel = new JPanel(new BorderLayout());
        return panel;
    }
}
