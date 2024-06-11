package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.*;

public class TreeViewTest {

    @Test
    public void testLoadFIXTree() {
        TreeView treeView = new TreeView(new Library());
        Assert.assertNotNull(treeView.fixTree);
    }

    @Test
    public void testLoadMessages() {
        TreeView treeView = new TreeView(new Library());
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertNotNull(rootNode);
        assertEquals(treeView.library.getDictionaries().size(), rootNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        TreeView treeView = new TreeView(new Library());
        FixInfoTreeNode versionNode = new FixInfoTreeNode("version");
        treeView.loadItems(versionNode, new DictionaryInfo("1.0"), 0);
        assertEquals(treeView.library.getMessages().size(), versionNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        TreeView treeView = new TreeView(new Library());
        treeView.expandAll();
        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            assertTrue(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getModel().getPathForRow(row))));
            count++;
        }
        assertEquals(count, treeView.fixTree.getRowCount());
    }

    @Test
    public void testCollapseAll() {
        TreeView treeView = new TreeView(new Library());
        treeView.collapseAll();
        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getModel().getPathForRow(row))));
            count++;
        }
        assertEquals(count, treeView.fixTree.getRowCount());
    }

    @Test
    public void testSearch() {
        TreeView treeView = new TreeView(new Library());
        treeView.searchTextField.setText("tagNumber");
        treeView.searchButton.doClick();
        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            Object userObject = treeView.fixTree.getModel().getUserObjectAt(row);
            if (userObject instanceof FieldInfo && userObject.getClass().getDeclaredField("tagNumber").get(userObject).toString().matches("\\d+")) {
                assertTrue(((FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(row)).isHighlighted());
                count++;
            }
        }
        assertEquals(count, treeView.library.getFields().size());
    }

    @Test
    public void testClear() {
        TreeView treeView = new TreeView(new Library());
        treeView.searchTextField.setText("tagNumber");
        treeView.searchButton.doClick();
        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            if (((FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(row)).isHighlighted()) {
                treeView.clear((FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(row));
                count++;
            }
        }
        int actualUnhighlightedNodesCount = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            if (!((FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(row)).isHighlighted()) {
                actualUnhighlightedNodesCount++;
            }
        }
        assertEquals(treeView.fixTree.getRowCount(), actualUnhighlightedNodesCount);
    }
}