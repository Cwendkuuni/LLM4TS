package org.apache.commons.cli; //This is not the correct package for TreeView tests. Use the correct package for your project.

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.view.FixInfo;
import org.fixsuite.message.view.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private FixInfoTreeNode rootNode;

    @Before
    public void setup() {
        this.treeView = new TreeView(new Library() { // Placeholder for Library object
            @Override
            public DictionaryInfo[] getDictionaries() {
                return new DictionaryInfo[0];
            }
        });
        this.rootNode = (FixInfoTreeNode) this.treeView.loadFIXTree().getRoot();
    }

    @Test
    public void testLoadFIXTree() {
        assertTrue(this.rootNode.getUserObject().equals("Dictionaries"));
        assertEquals(0, this.rootNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        this.treeView.expandAll();
        assertEquals(this.rootNode.getChildCount(), this.treeView.fixTree.getRowCount());
    }

    @Test
    public void testCollapseAll() {
        this.treeView.expandAll();
        this.treeView.collapseAll();
        assertEquals(0, this.treeView.fixTree.getRowCount());
    }

    @Test
    public void testSearch() {
        this.treeView.searchTextField.setText("Test");
        this.treeView.searchButton.doClick();

        for (int i = 0; i < this.rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.rootNode.getChildAt(i);
            assertTrue(node.isLeaf() || searchNode(node, "Test"));
        }
    }

    @Test
    public void testClear() {
        this.treeView.searchTextField.setText("Test");
        this.treeView.searchButton.doClick();

        for (int i = 0; i < this.rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
        }

        this.treeView.clearButton.doClick();
        for (int i = 0; i < this.rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testKeyPress() {
        TreeCellRenderer renderer = this.treeView.fixTree.getCellRenderer();
        JTree tree = this.treeView.fixTree;
        JTree mockTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode(new Object[]{"Test"})));
        mockTree.setCellRenderer(renderer);

        renderer.getTreeCellRendererComponent(mockTree, new Object[]{"Test"}, false, false, false, 0, false).doClick();
        this.treeView.searchTextField.setText("Test");
        this.treeView.searchTextField.sendKeyEvent(new KeyEvent(this.treeView.searchTextField, 10, 0, KeyEvent.VK_ENTER));

        for (int i = 0; i < this.rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.rootNode.getChildAt(i);
            assertTrue(node.isLeaf() || searchNode(node, "Test"));
        }
    }

    private boolean searchNode(DefaultMutableTreeNode node, String searchString) {
        if (node.isLeaf()) {
            if (node.getUserObject() instanceof FieldInfo) {
                FieldInfo field = (FieldInfo) node.getUserObject();
                return field.getName().matches(searchString) || String.valueOf(field.getTagNumber()).matches(searchString);
            } else if (node.getUserObject() instanceof MessageInfo) {
                MessageInfo message = (MessageInfo) node.getUserObject();
                return message.getName().matches(searchString) || message.getMessageType().matches(searchString);
            } else if (node.getUserObject() instanceof ComponentInfo) {
                ComponentInfo component = (ComponentInfo) node.getUserObject();
                return component.getName().matches(searchString);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            if (searchNode((DefaultMutableTreeNode) node.getChildAt(i), searchString)) {
                return true;
            }
        }

        return false;
    }
}