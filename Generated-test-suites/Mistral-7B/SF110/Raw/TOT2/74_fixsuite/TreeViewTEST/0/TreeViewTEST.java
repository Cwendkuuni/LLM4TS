package org.fixsuite.message.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

public class TreeViewTest {

    private TreeView treeView;
    private DefaultTreeModel treeModel;
    private TreeCellRenderer treeCellRenderer;

    @Before
    public void setUp() {
        treeView = new TreeView(new Library());
        treeModel = (DefaultTreeModel) treeView.fixTree.getModel();
        treeCellRenderer = treeView.fixTree.getCellRenderer();
    }

    @Test
    public void testLoadFIXTree() {
        treeView.initGUI();
        assertTrue(treeModel.getRoot().getChildCount() > 0);
    }

    @Test
    public void testLoadMessages() {
        final FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
        assertTrue(rootNode.getChildCount() > 0);
        final FixInfoTreeNode firstNode = (FixInfoTreeNode) rootNode.getChildAt(0);
        assertTrue(firstNode.getChildCount() > 0);
    }

    @Test
    public void testLoadItems() {
        final FixInfoTreeNode branchNode = (FixInfoTreeNode) treeModel.getRoot().getChildAt(0);
        assertTrue(branchNode.getChildCount() > 0);
        final FixInfoTreeNode leafNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertTrue(leafNode.getChildCount() == 0);
    }

    @Test
    public void testHighlightSearchResult() {
        final String searchString = "test";
        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();
        final FixInfoTreeNode node = (FixInfoTreeNode) treeModel.getRoot().getChildAt(0).getChildAt(0);
        node.setHighlighted(true);
        treeView.fixTree.expandPath(new TreePath(node.getPath()));
        final Component renderedComponent = treeCellRenderer.getTreeCellRendererComponent(treeView.fixTree, node, false, false, false, treeView.fixTree.getRowCount() - 1, false);
        assertTrue(renderedComponent.isDisplayable());
        assertTrue(((JLabel)renderedComponent).getText().contains(searchString));
        assertTrue(((JLabel)renderedComponent).getForeground().equals(Color.BLUE));
    }

    @Test
    public void testClearSearchResult() {
        final String searchString = "test";
        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();
        treeView.clearButton.doClick();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        for (int i = 0; i < rootNode.getChildCount(); ++i) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }
}