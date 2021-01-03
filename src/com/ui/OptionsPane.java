package com.ui;

import com.calculator.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OptionsPane extends JPanel {

    public static OptionsPane self = new OptionsPane();
    private static boolean init = false;

    JButton help = new JButton("Help");
    JButton addVariable = new JButton("Add Variable");
    JButton viewVariable = new JButton("View Variables");

    private OptionsPane() {

        help.addActionListener(action -> helpPopup.show(ContentPane.self, ContentPane.WIDTH/5, ContentPane.HEIGHT/6));
        addVariable.addActionListener(action -> addVariablePopup.show(ContentPane.self, ContentPane.WIDTH/5, ContentPane.HEIGHT/6));
        viewVariable.addActionListener(action -> viewVariablePopup.show(ContentPane.self, ContentPane.WIDTH/8, ContentPane.HEIGHT/6));


        add(help);
        add(addVariable);
        add(viewVariable);
    }

    private JPopupMenu helpPopup = new JPopupMenu("Help") {
        {
            setBackground(Color.GRAY);
            add(new JLabel("This is how you work this application."));
        }
    };

    private JPopupMenu addVariablePopup = new JPopupMenu("Add Variable") {
        {
            JTextField name = new JTextField();
            JTextField value = new JTextField();
            name.setPreferredSize(new Dimension(150, 15));
            value.setPreferredSize(new Dimension(150, 15));
            setLayout(new GridLayout(3, 2));
            add(new JLabel("Variable name: "));
            add(name);
            add(new JLabel("Variable value: "));
            add(value);

            add(new JLabel("                "));
            JButton submit = new JButton("Submit");

            ActionListener get = action -> {
                setVisible(false);
                String var = name.getText().strip();
                String val = value.getText().strip();
                if (Calculator.addVariable(var,val, false)) variablesContainer.add(
                        new Block.VariableBlock(new Block.Calculation(var, val)));
                variablesContainer.setPreferredSize(new Dimension(Block.WIDTH, Block.HEIGHT*(Calculator.variables().size()+1)));
                variablesContainer.revalidate();
                name.setText("");
                value.setText("");
            };
            submit.addActionListener(get);

            value.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyChar() != KeyEvent.VK_ENTER) return;
                    get.actionPerformed(new ActionEvent(this, 0, "Submit Variables"));
                }
            });

            add(submit);
        }
    };

    private JPanel variablesContainer = new JPanel() {
        {
            setPreferredSize(new Dimension(Block.WIDTH-5, Block.HEIGHT));
            setLayout(new GridLayout(5, 1));
        }
    };
    private JScrollPane containerScrollPane = new JScrollPane(variablesContainer);
    private JPopupMenu viewVariablePopup = new JPopupMenu("View Variable") {
        {
            containerScrollPane.setPreferredSize(new Dimension(ContentPane.WIDTH, ContentPane.HEIGHT));
            setSize(new Dimension(ContentPane.WIDTH, ContentPane.HEIGHT));
            add(containerScrollPane);
        }
    };

    public JPanel getVariablesContainer() {
        return variablesContainer;
    }
}
