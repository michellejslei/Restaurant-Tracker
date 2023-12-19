package ui.pages;

import javax.swing.*;
import java.awt.*;

//A blank page with a set background and dimensions
public abstract class Page extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    // EFFECTS: creates a blank page of WIDTH and HEIGHT dimensions with a plain background colour
    public Page(String msg) {
        super(msg);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#EAE5E4"));
        setLayout(null);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel and adds it to the JFrame
    public void initializeMainPanel(JPanel panel) {
        panel.setBackground(Color.decode("#EAE5E4"));
        panel.setBounds(0,0, WIDTH, HEIGHT);
        panel.setLayout(null);

        initializePanelComponents();

        this.add(panel);
    }

    // EFFECTS: initializes components to be added to the main panel
    public abstract void initializePanelComponents();

}
