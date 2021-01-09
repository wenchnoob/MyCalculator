package com.ui;

import com.calculator.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Key;

public class Block extends JPanel {
    private Calculation calculation;

    protected JLabel outputLabel = new JLabel();
    protected JButton delete = new JButton("Delete");

    public static final int WIDTH = ContentPane.WIDTH, HEIGHT = ContentPane.HEIGHT/5;


    public Block(Calculation calculation, boolean defaultDelete) {
        this.calculation = calculation;
        outputLabel.setText(calculation.input + " = " + calculation.output);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new FlowLayout());

        if (defaultDelete) delete.addActionListener(action -> {
            ContentPane cont = ContentPane.self;
            ContentPane.self.removeBlock(this);
            cont.updateUI();
        });

        add(outputLabel);
        add(delete);
    }

    public static record Calculation(String input, String output) { }


    public static class VariableBlock extends Block {
        private JButton reassign = new JButton("Reassign");
        private JPopupMenu reassignPopup = new JPopupMenu();

        public VariableBlock(Calculation calculation) {
            super(calculation, false);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            delete.addActionListener(action -> {
                Calculator.removeVariable(calculation.input);
                OptionsPane.self.getVariablesContainer().remove(this);
                OptionsPane.self.getVariablesContainer().setPreferredSize(new Dimension(Block.WIDTH/2, Block.HEIGHT*Calculator.variables().size()));
                OptionsPane.self.getVariablesContainer().revalidate();
                OptionsPane.self.getVariablesContainer().updateUI();
            });

            reassign.addActionListener(action -> reassignPopup.show(ContentPane.self, 0, OptionsPane.self.getVariablesContainer().getHeight()/2));
            reassignPopup.setLayout(new FlowLayout());
            reassignPopup.setPreferredSize(new Dimension(ContentPane.WIDTH, HEIGHT));
            reassignPopup.add(new JLabel("New value:"));
            JTextField input = new JTextField();
            input.setPreferredSize(new Dimension(ContentPane.WIDTH/3, HEIGHT/2));
            input.setRequestFocusEnabled(true);
            reassignPopup.add(input);

            ActionListener submitAction = action -> {
                String out = input.getText().strip();
                outputLabel.setText(calculation.input + " = " + out);
                Calculator.addVariable(calculation.input, out, true);
                reassignPopup.setVisible(false);
            };

            input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyChar() != KeyEvent.VK_ENTER) return;
                    submitAction.actionPerformed(new ActionEvent(this, 0, "send"));
                }
            });
            reassignPopup.add(new JButton("Submit") {
                {
                    addActionListener(submitAction);
                }
            });

            add(reassign);
        }
    }
}
