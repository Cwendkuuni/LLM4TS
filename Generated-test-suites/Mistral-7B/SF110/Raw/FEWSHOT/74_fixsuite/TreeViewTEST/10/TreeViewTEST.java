package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private TreeCellRenderer cellRenderer;

    @Before
    public void setUp() {
        Library library = new Library(); // Assuming library is instantiated correctly
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testLoadFIXTree() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        assertEquals("Dictionaries", rootNode.getUserObject());
        assertTrue(rootNode.getChildCount() > 0);
    }

    @Test
    public void testLoadMessages() {
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) treeView.getFixTree().getModel().findNode("Dictionaries", true);
        assertTrue(dictionaryNode != null);
        assertTrue(dictionaryNode.getChildCount() > 0);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("Message", messageNode.getUserObject());
        assertTrue(messageNode.getChildCount() > 0);
    }

    @Test
    public void testLoadItems() {
        DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) fixTree.getModel().findNode("Message", true);
        assertTrue(versionNode != null);
        assertTrue(versionNode.getChildCount() > 0);
        DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode) versionNode.getChildAt(0);
        assertEquals("Component", leafNode.getUserObject());
        assertTrue(leafNode.getChildCount() == 0);
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        TreeModel treeModel = fixTree.getModel();
        for (int i = 0; i < treeModel.getRowCount(); ++i) {
            treeView.fixTree.expandRow(i);
        }
        assertTrue(fixTree.isExpanded(new TreePath(fixTree.getModel().getRoot())));
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        TreeModel treeModel = fixTree.getModel();
        for (int i = treeModel.getRowCount() - 1; i >= 0; --i) {
            treeView.fixTree.collapseRow(i);
        }
        assertFalse(fixTree.isExpanded(new TreePath(fixTree.getModel().getRoot())));
    }

    @Test
    public void testSearch() {
        String searchString = "Component";
        treeView.search(fixTree.getModel().getRoot(), searchString);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeView.fixTree.getModel().getRoot();
        int count = 0;
        while (node.getChildCount() > 0) {
            node = (DefaultMutableTreeNode) node.getChildAt(0);
            if (node.isHighlighted()) {
                assertEquals("Component", node.getUserObject());
                count++;
            }
            node = node.getNextSibling();
        }
        assertEquals(1, count);
    }

    @Test
    public void testClear() {
        String searchString = "Component";
        treeView.search(fixTree.getModel().getRoot(), searchString);
        treeView.clear(fixTree.getModel().getRoot());
        int count = 0;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        while (node.getChildCount() > 0) {
            node = (DefaultMutableTreeNode) node.getChildAt(0);
            if (node.isHighlighted()) {
                assertFalse(node.isHighlighted());
                count++;
            }
            node = node.getNextSibling();
        }
        assertEquals(0, count);
    }
}