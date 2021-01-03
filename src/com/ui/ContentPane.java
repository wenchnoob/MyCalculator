package com.ui;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ContentPane extends JScrollPane {

    private static JPanel content = new JPanel();
    public static ContentPane self = new ContentPane(content);
    public static final int WIDTH = MainWindow.WIDTH/5 * 4, HEIGHT = MainWindow.HEIGHT/10 * 4;

    private static final List<Block> blocks = new ArrayList<>();

    private ContentPane(Component cont) {
        super(cont);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        setViewportBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.LOWERED));
    }

    public void addBlock(Block block) {
        blocks.add(block);
        content.add(block);
        content.setPreferredSize(new Dimension(WIDTH, HEIGHT * content.getComponentCount()/5));
        content.revalidate();
        getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());
    }

    public void removeBlock(Block e) {
        blocks.remove(e);
        content.remove(e);
        content.setPreferredSize(new Dimension(WIDTH, HEIGHT * content.getComponentCount()/5));
    }
}