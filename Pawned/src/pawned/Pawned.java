package pawned;

import java.awt.*;
import javax.swing.*;

public class Pawned {
    public static void main(String[] args) {
        Frame f = new Frame();
        f.setVisible(true);
    }
}

class Frame extends JFrame{
    private Panel    panel;
    private JMenuBar menuBar;
    
    public Frame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chesster");
        setResizable(false);
        
        panel   = new Panel();
        menuBar = new JMenuBar();
        
        for(JMenu jm : panel.getMenu())
            menuBar.add(jm);
        
        Container c = getContentPane();
        
        c.add(menuBar, BorderLayout.NORTH);
        c.add(panel);
        
        pack();
    }
}