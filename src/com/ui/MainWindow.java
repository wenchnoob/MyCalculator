package com.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static MainWindow self = new MainWindow();

    public static final int WIDTH = 500, HEIGHT = 500;

    private MainWindow() {
        setSize(new Dimension(WIDTH, HEIGHT));


        add(OptionsPane.self, BorderLayout.NORTH);
        add(ContentPane.self, BorderLayout.CENTER);
        add(InputPane.self, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}
