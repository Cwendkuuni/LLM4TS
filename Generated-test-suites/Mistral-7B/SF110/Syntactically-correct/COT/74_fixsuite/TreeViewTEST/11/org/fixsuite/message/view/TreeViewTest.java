package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;

public class TreeViewTest {

    private Library library;
    private TreeView treeView;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
        assertNotNull(treeView.getToolBar());
    }

    @Test
    public void testLoadFIXTree() {
        assertNotNull(treeView.loadFIXTree());
    }

    @Test
    public void testLoadMessages() {
        final FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertNotNull(rootNode);
        assertTrue(rootNode.getChildCount() > 0);
    }

    @Test
    public void testLoadItems() {
        final FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
        treeView.loadItems(versionNode, new MessageInfo("Message1", "MessageType1"), 0);
        assertNotNull(versionNode);
        assertTrue(versionNode.getChildCount() > 0);
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        final TreeCellRenderer renderer = treeView.getFixTree().getCellRenderer();
        final int nodeCount = treeView.getFixTree().getModel().getNodeCount(treeView.getFixTree().getModel().getRoot());

        for (int i = 0; i < nodeCount; ++i) {
            final TreePath nodePath = new TreePath(treeView.getFixTree().getModel().getPathToRoot(treeView.getFixTree().getModel().getChild(treeView.getFixTree().getModel().getRoot(), i)));
            final TreeCellNode node = treeView.getFixTree().getModel().getNodeForPath(nodePath);
            final Component rendererComponent = renderer.getTreeCellRendererComponent(treeView.getFixTree(), node.getUserObject(), false, false, false, node.getModelIndex(), false);
            assertTrue(rendererComponent.isOpaque());
            assertTrue(rendererComponent.isVisible());
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.expandAll();
        treeView.collapseAll();

        final TreeCellRenderer renderer = treeView.getFixTree().getCellRenderer();
        final int nodeCount = treeView.getFixTree().getModel().getNodeCount(treeView.getFixTree().getModel().getRoot());

        for (int i = 0; i < nodeCount; ++i) {
            final TreePath nodePath = new TreePath(treeView.getFixTree().getModel().getPathToRoot(treeView.getFixTree().getModel().getChild(treeView.getFixTree().getModel().getRoot(), i)));
            final TreeCellNode node = treeView.getFixTree().getModel().getNodeForPath(nodePath);
            final Component rendererComponent = renderer.getTreeCellRendererComponent(treeView.getFixTree(), node.getUserObject(), false, false, false, node.getModelIndex(), false);
            assertFalse(rendererComponent.isOpaque());
            assertFalse(rendererComponent.isVisible());
        }
    }

    @Test
    public void testSearch() {
        final String searchString = "Test";
        treeView.expandAll();
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), searchString);
        final int nodeCount = treeView.getFixTree().getModel().getLeafCount();
        int matchedNodeCount = 0;

        for (int i = 0; i < nodeCount; ++i) {
            final TreePath nodePath = new TreePath(treeView.getFixTree().getModel().getPathToLeaf(treeView.getFixTree().getModel().getChild(treeView.getFixTree().getModel().getRoot(), i)));
            final TreeCellNode node = treeView.getFixTree().getModel().getNodeForPath(nodePath);
            if (((FixInfoTreeNode)node.getParent()).getUserObject() instanceof FixInfo && ((FixInfo)((FixInfoTreeNode)node.getParent()).getUserObject()).isHighlighted()) {
                matchedNodeCount++;
            }
        }

        assertEquals(matchedNodeCount, 10); // Assuming there are 10 nodes matching the search string
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullNode() {
        treeView.search(null, "Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullSearchString() {
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), null);
    }

    @Test
    public void testClear() {
        treeView.expandAll();
        treeView.clear((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot());

        int nodeCount = treeView.getFixTree().getModel().getLeafCount();
        assertEquals(nodeCount, 0);
    }

    @Test
    public void testClearWithNullNode() {
        treeView.clear(null);
    }

    
}