package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    private TreeView treeView;
    private Library library;

    @Before
    public void setUp() {
        library = new Library(); // Assuming Library has necessary methods and data for testing
        treeView = new TreeView(library);
    }

    @Test
    public void testTreeViewInitialization() {
        // Basic test to check if TreeView is initialized correctly
        assertNotNull(treeView);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
    }

    @Test
    public void testLoadFIXTree() {
        // Testing the loadFIXTree() method
        assertNotNull(treeView.loadFIXTree());
    }

    @Test
    public void testLoadMessages() {
        // Testing the loadMessages() method
        final FixInfoTreeNode root = treeView.loadFIXTree();
        assertTrue(root.getChildCount() > 0);
    }

    @Test
    public void testLoadItems() {
        // Testing the loadItems() method
        final FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
        treeView.loadItems(versionNode, new DictionaryInfo("Test Dictionary"), 0);
        assertTrue(versionNode.getChildCount() > 0);
    }

    @Test
    public void testExpandAll() {
        // Testing the expandAll() method
        treeView.expandAll();
        assertTrue(treeView.fixTree.getRowCount() > 0);
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        // Testing the collapseAll() method
        treeView.expandAll();
        treeView.collapseAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        // Testing the search() method
        final String searchString = "Test";
        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRow(row);
            if (node.getUserObject() instanceof FixInfo) {
                if (((FixInfo)node.getUserObject()).getName().matches(searchString) || ((FixInfo)node.getUserObject()).getMessageType().matches(searchString) || ((FieldInfo)node.getUserObject()).getName().matches(searchString) || ((FieldInfo)node.getUserObject()).getTagNumber().matches(searchString)) {
                    assertTrue(node.isHighlighted());
                } else {
                    assertFalse(node.isHighlighted());
                }
            }
        }
    }
}