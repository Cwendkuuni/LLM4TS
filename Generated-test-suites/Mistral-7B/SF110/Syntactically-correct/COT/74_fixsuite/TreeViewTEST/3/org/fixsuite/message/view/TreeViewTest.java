package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private TreeCellRenderer cellRenderer;

    @Before
    public void setUp() {
        Library library = new Library();
        Dictionary<String, DictionaryInfo> dictionaries = new Hashtable<>();
        DictionaryInfo dictionary = new DictionaryInfo("TestDictionary");
        dictionaries.put("TestDictionary", dictionary);
        library.setDictionaries(dictionaries);

        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testTreeViewInitialization() {
        assertNotNull(treeView);
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree() {
        assertNotNull(treeView.loadFIXTree());
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertNotNull(rootNode);
        assertEquals("Dictionaries", rootNode.getUserObject().toString());

        int childCount = rootNode.getChildCount();
        assertEquals(1, childCount);

        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals("TestDictionary", dictionaryNode.getUserObject().toString());

        childCount = dictionaryNode.getChildCount();
        assertTrue(childCount > 0);

        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertNotNull(messageNode);
        assertEquals("TestMessage", messageNode.getUserObject().toString());

        childCount = messageNode.getChildCount();
        assertEquals(0, childCount);
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);

        int childCount = messageNode.getChildCount();
        assertEquals(3, childCount);

        FixInfoTreeNode fieldNode1 = (FixInfoTreeNode) messageNode.getChildAt(0);
        assertEquals("TestField1", fieldNode1.getUserObject().toString());

        FixInfoTreeNode fieldNode2 = (FixInfoTreeNode) messageNode.getChildAt(1);
        assertEquals("TestField2", fieldNode2.getUserObject().toString());

        FixInfoTreeNode componentNode = (FixInfoTreeNode) messageNode.getChildAt(2);
        assertEquals("TestComponent", componentNode.getUserObject().toString());

        childCount = componentNode.getChildCount();
        assertEquals(2, childCount);

        DefaultMutableTreeNode subComponentNode1 = (DefaultMutableTreeNode) componentNode.getChildAt(0);
        assertEquals("SubComponent1", subComponentNode1.getUserObject().toString());

        DefaultMutableTreeNode subComponentNode2 = (DefaultMutableTreeNode) componentNode.getChildAt(1);
        assertEquals("SubComponent2", subComponentNode2.getUserObject().toString());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        int rowCount = fixTree.getRowCount();
        assertTrue(rowCount > 0);
        for (int i = 0; i < rowCount; i++) {
            assertTrue(fixTree.isExpanded(new TreePath(fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        int rowCount = fixTree.getRowCount();
        assertTrue(rowCount > 0);
        for (int i = 0; i < rowCount; i++) {
            assertFalse(fixTree.isExpanded(new TreePath(fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testSearch() {
        treeView.expandAll();

        treeView.search(treeView.getFixTree().getModel().getRoot(), "TestField1");

        int childCount = ((DefaultMutableTreeNode) treeView.getFixTree().getModel().getRoot()).getChildCount();
        assertTrue(childCount > 0);

        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getChild(0, i);
            if (node.getUserObject() instanceof MessageInfo) {
                MessageInfo message = (MessageInfo) node.getUserObject();
                assertTrue(message.getName().matches("TestMessage") || message.getMessageType().matches("TestMessage"));
            }
            else if (node.getUserObject() instanceof FieldInfo) {
                FieldInfo field = (FieldInfo) node.getUserObject();
                assertTrue(field.getName().matches("TestField1") || field.getTagNumber().matches("TestField1"));
            }
            else if (node.getUserObject() instanceof ComponentInfo) {
                ComponentInfo component = (ComponentInfo) node.getUserObject();
                // Add a test for the component name here
            }
        }

        treeView.clear(treeView.getFixTree().getModel().getRoot());
        treeView.collapseAll();
    }

    @Test
    public void testClear() {
        treeView.search(treeView.getFixTree().getModel().getRoot(), "TestField1");
        treeView.clear(treeView.getFixTree().getModel().getRoot());

        int childCount = ((DefaultMutableTreeNode) treeView.getFixTree().getModel().getRoot()).getChildCount();
        assertEquals(0, childCount);
    }

    @Test
    public void testSearchTextFieldKeyEvent() {
        treeView.initGUI();

        treeView.searchTextField.setText("TestField1");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 0));

        int rowCount = fixTree.getRowCount();
        assertTrue(rowCount > 0);

        for (int i = 0; i < rowCount; i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getChild(0, i);
            if (node.getUserObject() instanceof FieldInfo) {
                FieldInfo field = (FieldInfo) node.getUserObject();
                assertTrue(field.getName().matches("TestField1") || field.getTagNumber().matches("TestField1"));
            }
            else if (node.getUserObject() instanceof MessageInfo) {
                MessageInfo message = (MessageInfo) node.getUserObject();
                // Add a test for the message name here
            }
            else if (node.getUserObject() instanceof ComponentInfo) {
                ComponentInfo component = (ComponentInfo) node.getUserObject();
                // Add a test for the component name here
            }
        }

        treeView.clear(treeView.getFixTree().getModel().getRoot());
        treeView.collapseAll();
    }
}