package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.*;

public class TreeViewTest {

    private static TreeView treeView;
    private static JTree fixTree;
    private static TreeCellRenderer renderer;
    private static DefaultMutableTreeNode rootNode;
    private static Library library;

    @BeforeClass
    public static void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        renderer = treeView.getFixTree().getCellRenderer();
        rootNode = (DefaultMutableTreeNode)fixTree.getModel().getRoot();
    }

    @AfterClass
    public static void tearDown() {
        if (treeView != null) {
            treeView.dispose();
        }
    }

    @Test
    public void testTreeViewConstruction() {
        assertNotNull(treeView);
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertNotNull(treeView.getToolBar());
    }

    @Test
    public void testLoadFIXTree() {
        TreeModel model = fixTree.getModel();
        assertEquals(model.getChildCount(rootNode), library.getDictionaries().size());
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = (FixInfoTreeNode)rootNode.getFirstChild();
        assertEquals(versionNode.getChildCount(), 1);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode)versionNode.getFirstChild();
        assertEquals(messageNode.getChildCount(), 1);
        assertInstanceOf(MessageInfo.class, messageNode.getUserObject());
    }

    @Test
    public void testLoadItems() {
        DefaultMutableTreeNode branchNode = (DefaultMutableTreeNode)messageNode.getFirstChild();
        assertEquals(branchNode.getChildCount(), 1);
        DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode)branchNode.getFirstChild();
        assertInstanceOf(FixInfo.class, leafNode.getUserObject());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        int expandedCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowExpanded(i)) {
                expandedCount++;
            }
        }
        assertEquals(expandedCount, fixTree.getRowCount());
    }

    @Test
    public void testCollapseAll() {
        treeView.expandAll();
        treeView.collapseAll();
        int expandedCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowExpanded(i)) {
                expandedCount++;
            }
        }
        assertEquals(expandedCount, 0);
    }

    @Test
    public void testSearch() {
        library.addDictionary(new DictionaryInfo("FIX/4.4"));
        library.getDictionaries().get(0).addMessage(new MessageInfo("1", "Heartbeat", ""));
        library.getDictionaries().get(0).getMessages().get(0).addField(new FieldInfo("35", "BodyLength", ""));

        treeView.expandAll();

        String searchString = "35";
        treeView.search(rootNode, searchString);

        int highlightedNodeCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowSelected(i) && ((FixInfoTreeNode)fixTree.getModel().getPathForRow(i)).isHighlighted()) {
                highlightedNodeCount++;
            }
        }
        assertEquals(highlightedNodeCount, 1);
    }

    @Test
    public void testClear() {
        library.addDictionary(new DictionaryInfo("FIX/4.4"));
        library.getDictionaries().get(0).addMessage(new MessageInfo("1", "Heartbeat", ""));
        library.getDictionaries().get(0).getMessages().get(0).addField(new FieldInfo("35", "BodyLength", ""));

        treeView.expandAll();
        treeView.search(rootNode, "35");

        int highlightedNodeCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowSelected(i) && ((FixInfoTreeNode)fixTree.getModel().getPathForRow(i)).isHighlighted()) {
                highlightedNodeCount++;
            }
        }
        assertEquals(highlightedNodeCount, 1);

        treeView.clear(rootNode);
        treeView.collapseAll();

        highlightedNodeCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowSelected(i) && ((FixInfoTreeNode)fixTree.getModel().getPathForRow(i)).isHighlighted()) {
                highlightedNodeCount++;
            }
        }
        assertEquals(highlightedNodeCount, 0);
    }

    @Test
    public void testKeyEvent() {
        library.addDictionary(new DictionaryInfo("FIX/4.4"));
        library.getDictionaries().get(0).addMessage(new MessageInfo("1", "Heartbeat", ""));
        library.getDictionaries().get(0).getMessages().get(0).addField(new FieldInfo("35", "BodyLength", ""));

        treeView.expandAll();

        treeView.searchTextField.setText("35");
        TreePath path = treeView.fixTree.getPathForRow(0);
        treeView.fixTree.setSelectionPath(path);

        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));

        int highlightedNodeCount = 0;
        for (int i = 0; i < fixTree.getRowCount(); i++) {
            if (fixTree.isRowSelected(i) && ((FixInfoTreeNode)fixTree.getModel().getPathForRow(i)).isHighlighted()) {
                highlightedNodeCount++;
            }
        }
        assertEquals(highlightedNodeCount, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLibrary() {
        TreeView treeView = new TreeView(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLevel() {
        FixInfoTreeNode rootNode = new FixInfoTreeNode("root");
        rootNode.add(new FixInfoTreeNode("level-1", null));
        rootNode.getFirstChild().add(new FixInfoTreeNode("level-2", null));
        rootNode.getFirstChild().getFirstChild().add(new FixInfoTreeNode("level-3", null));

        treeView.loadItems(rootNode, null, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUserObject() {
        FixInfoTreeNode rootNode = new FixInfoTreeNode("root");
        FixInfoTreeNode messageNode = new FixInfoTreeNode(null);
        rootNode.add(messageNode);

        treeView.loadMessages(rootNode, null);
    }
}