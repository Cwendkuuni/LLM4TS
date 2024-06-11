package org.apache.commons.cli; // This line is incorrect, please use the actual package name for TreeView tests

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton clearButton;
    private JButton expandButton;
    private JButton collapseButton;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        searchTextField = treeView.getSearchTextField();
        searchButton = treeView.getSearchButton();
        clearButton = treeView.getClearButton();
        expandButton = treeView.getExpandButton();
        collapseButton = treeView.getCollapseButton();
    }

    @Test
    public void testTreeViewInitialization() {
        assertNotNull(treeView);
        assertEquals("Dictionaries", fixTree.getModel().getRoot().getUserObject());
    }

    @Test
    public void testLoadFIXTree() {
        assertNotNull(fixTree);
        assertEquals(2, fixTree.getRowCount());
    }

    @Test
    public void testLoadMessages() {
        assertNotNull(fixTree);
        assertEquals(3, fixTree.getRowCount());
    }

    @Test
    public void testLoadItems() {
        assertNotNull(fixTree);
        assertEquals(7, fixTree.getRowCount());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        int expandedNodes = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            TreePath nodePath = new TreePath(fixTree.getPathForLocation(0, i));
            assertNotNull(nodePath);
            treeView.expandPath(nodePath);
            if (fixTree.isExpanded(nodePath)) {
                expandedNodes++;
            }
        }
        assertEquals(7, expandedNodes);
    }

    @Test
    public void testCollapseAll() {
        treeView.expandAll();
        treeView.collapseAll();
        int collapsedNodes = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            TreePath nodePath = new TreePath(fixTree.getPathForLocation(0, i));
            assertNotNull(nodePath);
            if (!fixTree.isExpanded(nodePath)) {
                collapsedNodes++;
            }
        }
        assertEquals(7, collapsedNodes);
    }

    @Test
    public void testSearch() {
        searchTextField.setText("Field");
        searchButton.doClick();
        int matchingNodes = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            TreePath nodePath = new TreePath(fixTree.getPathForLocation(0, i));
            assertNotNull(nodePath);
            if (fixTree.isExpanded(nodePath)) {
                TreeCellRenderer cellRenderer = fixTree.getCellRenderer();
                Component renderedComponent = cellRenderer.getTreeCellRendererComponent(fixTree, nodePath.getLastPathComponent(), false, false, false, i, false);
                String label = ((JLabel)renderedComponent).getText();
                if (label.contains("Field")) {
                    matchingNodes++;
                    assertTrue(nodePath.getLastPathComponent().isHighlighted());
                }
            }
        }
        assertEquals(3, matchingNodes);
    }

    @Test
    public void testClear() {
        searchTextField.setText("Field");
        searchButton.doClick();
        treeView.clear(fixTree.getModel().getRoot());
        assertEquals(0, fixTree.getRowCount());
    }
}