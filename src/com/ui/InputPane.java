package com.ui;

import com.calculator.LogicUnit;
import com.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputPane extends JPanel {

    public static InputPane self = new InputPane();

    private JTextField input = new JTextField();
    private JButton submitButton = new JButton("Submit");

    private ActionListener getInput = action -> {
        String in = input.getText();
        input.setText("");
        Block cur = new Block(new Block.Calculation(in, LogicUnit.solve(in.transform(Parser.removeWhiteSpace))), true);
        ContentPane.self.addBlock(cur);
    };

    private KeyAdapter keyIn = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() != KeyEvent.VK_ENTER) return;
            super.keyTyped(e);
            getInput.actionPerformed(new ActionEvent(this, 1, "Get Input"));
        }
    };

    private InputPane() {
        input.setPreferredSize(new Dimension(300, 25));
        submitButton.addActionListener(getInput);
        input.addKeyListener(keyIn);
        add(input);
        add(submitButton);
    }

}
