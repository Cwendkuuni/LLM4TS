package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private static final String SEARCH_STRING = "Message";
    private TreeView treeView;
    private Library library;
    private JTree tree;

    @Before
    public void setUp() {
        library = new Library();
        library.addDictionary(new DictionaryInfo("1.0", Arrays.asList(new MessageInfo("1.0.1", "Heartbeat"), new MessageInfo("1.0.2", "Login")), Collections.emptyList()));
        treeView = new TreeView(library);
        tree = treeView.getFixTree();
    }

    @After
    public void tearDown() {
        treeView = null;
        library = null;
    }

    @Test
    public void testTreeViewConstruction() {
        assertNotNull(treeView);
        assertNotNull(tree);
    }

    @Test
    public void testInitGUI() {
        assertEquals(new Dimension(300, 400), treeView.getPreferredSize());
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
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        assertEquals(1, rootNode.getChildCount());
        assertEquals("Dictionaries", rootNode.getUserObject());
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals(2, dictionaryNode.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) tree.getModel().getPathForRow(0)[1];
        assertEquals(2, versionNode.getChildCount());
        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) versionNode.getChildAt(0);
        assertEquals(1, messageNode1.getChildCount());
        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) versionNode.getChildAt(1);
        assertEquals(1, messageNode2.getChildCount());

        assertInstanceOf(MessageInfo.class, messageNode1.getUserObject());
        assertInstanceOf(MessageInfo.class, messageNode2.getUserObject());
    }

    @Test
    public void testLoadItems() {
        DefaultMutableTreeNode branchNode = (DefaultMutableTreeNode) tree.getModel().getPathForRow(1)[1];
        assertEquals(2, branchNode.getChildCount());

        DefaultMutableTreeNode leafNode1 = (DefaultMutableTreeNode) branchNode.getChildAt(0);
        assertEquals(1, leafNode1.getChildCount());
        assertInstanceOf(FieldInfo.class, leafNode1.getUserObject());

        DefaultMutableTreeNode leafNode2 = (DefaultMutableTreeNode) branchNode.getChildAt(1);
        assertEquals(1, leafNode2.getChildCount());
        assertInstanceOf(FieldInfo.class, leafNode2.getUserObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadItemsWithDeepNestedStructure() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode) componentNode.getChildAt(0);
        leafNode.loadItems(null, null, 5);
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tree.expandRow(i);
        }
        assertTrue(treeView.isExpanded(tree.getModel().getRoot()));
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        int rowCount = tree.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tree.collapseRow(i);
        }
        assertTrue(treeView.isCollapsed(tree.getModel().getRoot()));
    }

    @Test
    public void testSearch() {
        treeView.search(tree.getModel().getRoot(), SEARCH_STRING);
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object userObject = tree.getModel().getNodeForPath(new TreePath(tree.getModel().getPathForRow(i))).getUserObject();
            if (userObject instanceof FieldInfo || userObject instanceof MessageInfo || userObject instanceof ComponentInfo) {
                String name = "";
                if (userObject instanceof FieldInfo) {
                    name = ((FieldInfo) userObject).getName();
                } else if (userObject instanceof MessageInfo) {
                    name = ((MessageInfo) userObject).getName();
                } else if (userObject instanceof ComponentInfo) {
                    name = ((ComponentInfo) userObject).getName();
                }
                assertTrue(name.matches(SEARCH_STRING) || ((FieldInfo) userObject).getTagNumber().matches(SEARCH_STRING) || name.equals(SEARCH_STRING));
            }
        }
    }

    @Test
    public void testClear() {
        treeView.search(tree.getModel().getRoot(), SEARCH_STRING);
        int rowCount = tree.getRowCount();
        treeView.clear(tree.getModel().getRoot());
        int clearedRowCount = tree.getRowCount();
        assertEquals(clearedRowCount, rowCount - rowCount);
        assertFalse(treeView.isHighlighted((FixInfoTreeNode) tree.getModel().getPathForRow(0)[1]));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSearchString() {
        treeView.search(tree.getModel().getRoot(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSearchDepth() {
        treeView.search(tree.getModel().getRoot(), SEARCH_STRING, -1);
    }

    @Test
    public void testKeyPressed() {
        TreeCellRenderer cellRenderer = treeView.getFixTree().getCellRenderer();
        treeView.getSearchTextField().setText(SEARCH_STRING);
        treeView.getSearchTextField().dispatchEvent(new KeyEvent(treeView.getSearchTextField(), KeyEvent.KEY_PRESSED, false, false, KeyEvent.VK_ENTER, 0));
        treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), 0, ""));
    }

}