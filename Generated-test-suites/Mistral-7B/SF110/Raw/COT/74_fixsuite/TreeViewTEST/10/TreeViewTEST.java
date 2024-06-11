package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

    private Library library;
    private TreeView treeView;
    private FixInfoTreeNode rootNode;
    private JTree fixTree;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        rootNode = (FixInfoTreeNode) fixTree.getModel().getRoot();
    }

    // Test methods go here...

}