package org.fixsuite.message.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.fixsuite.message.info.*;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private JTextField searchTextField;

    @Before
    public void setUp() {
        Library library = new Library(); // assume Library initialization here
        treeView = new TreeView(library);
        fixTree = treeView.fixTree;
        searchTextField = treeView.searchTextField;
    }

    @Test
    public void testLoadFIXTree() {
        // Test if loadFIXTree method returns a valid JTree
        assertNotNull(treeView.fixTree);
    }

    @Test
    public void testExpandAll() {
        // Test expandAll method
        treeView.expandAll();
        int count = 0;
        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            treeView.fixTree.expandRow(i);
            if (!treeView.fixTree.isRowExpanded(i)) {
                fail("Row " + i + " is not expanded.");
            }
            ++count;
        }
        assertEquals("All rows are expanded.", count, fixTree.getRowCount());
    }

    @Test
    public void testCollapseAll() {
        // Test collapseAll method
        treeView.expandAll();
        treeView.collapseAll();
        int count = 0;
        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            if (treeView.fixTree.isRowExpanded(i)) {
                fail("Row " + i + " is expanded.");
            }
            ++count;
        }
        assertEquals("All rows are collapsed.", count, fixTree.getRowCount());
    }

    @Test
    public void testSearch() {
        // Test search method
        DictionaryInfo dictionary = new DictionaryInfo("TestDictionary", "1.0");
        MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
        FieldInfo field = new FieldInfo(1, "TestField");
        ComponentInfo component = new ComponentInfo("TestComponent", "TestComponentName");

        Dictionary dictionaryNode = new FixInfoTreeNode("1.0");
        dictionaryNode.add(new FixInfoTreeNode(message));
        dictionaryNode.add(new FixInfoTreeNode(component));

        fixTree.setModel(new DefaultTreeModel(dictionaryNode));

        treeView.searchTextField.setText("TestField");
        treeView.actionPerformed(new ActionEvent(searchTextField, ActionEvent.ACTION_PERFORMED, null));

        TreePath selectedPath = fixTree.getLastSelectedPathComponent();
        assertEquals("Selected node should be the FieldInfo node.", field, selectedPath.getLastPathComponent());

        treeView.searchTextField.setText("TestMessageType");
        treeView.actionPerformed(new ActionEvent(searchTextField, ActionEvent.ACTION_PERFORMED, null));

        selectedPath = fixTree.getLastSelectedPathComponent();
        assertEquals("Selected node should be the MessageInfo node.", message, selectedPath.getLastPathComponent());
    }

    @Test
    public void testClear() {
        // Test clear method
        DictionaryInfo dictionary = new DictionaryInfo("TestDictionary", "1.0");
        MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
        FieldInfo field = new FieldInfo(1, "TestField");
        ComponentInfo component = new ComponentInfo("TestComponent", "TestComponentName");

        Dictionary dictionaryNode = new FixInfoTreeNode("1.0");
        dictionaryNode.add(new FixInfoTreeNode(message));
        dictionaryNode.add(new FixInfoTreeNode(component));

        fixTree.setModel(new DefaultTreeModel(dictionaryNode));

        treeView.searchTextField.setText("TestField");
        treeView.actionPerformed(new ActionEvent(searchTextField, ActionEvent.ACTION_PERFORMED, null));

        TreePath selectedPath = fixTree.getLastSelectedPathComponent();
        assertEquals("Selected node should be the FieldInfo node.", field, selectedPath.getLastPathComponent());

        treeView.clearButton.doClick();
        selectedPath = fixTree.getLastSelectedPathComponent();
        assertNull("No node should be selected after clearing the search results.");
    }
}