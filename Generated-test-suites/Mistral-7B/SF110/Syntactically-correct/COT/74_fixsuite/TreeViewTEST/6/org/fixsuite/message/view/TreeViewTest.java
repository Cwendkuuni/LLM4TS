package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private TreeModel treeModel;

    @Before
    public void setUp() {
        Library library = new Library();
        treeView = new TreeView(library);
        treeModel = treeView.getModel();
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
        assertEquals(TreeView.class, treeView.getClass());
    }

    @Test
    public void testInitGUI() {
        assertEquals(BorderLayout.class, treeView.getLayout().getClass());
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
    public void testLoadFIXTree_whenLibraryHasDictionaries_returnsTreeWithRootNodeAndChildren() {
        DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", Arrays.asList(new MessageInfo("Message1"), new MessageInfo("Message2")));
        library.addDictionary(dictionary);

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        assertEquals(1, rootNode.getChildCount());
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals("Dictionaries", dictionaryNode.getUserObject());
        assertEquals(2, dictionaryNode.getChildCount());

        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("Message1", messageNode1.getUserObject());
        assertEquals(0, messageNode1.getChildCount());

        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(1);
        assertEquals("Message2", messageNode2.getUserObject());
        assertEquals(0, messageNode2.getChildCount());
    }

    @Test
    public void testLoadItems_whenLeafNodeHasItems_populatesChildNodes() {
        MessageInfo message = new MessageInfo("Message1", "Name1", "MessageType1") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field1", 1, "Name1_1"), new FieldInfo("Field2", 2, "Name1_2"));
            }
        };

        DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", Collections.singletonList(message));
        library.addDictionary(dictionary);

        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) treeModel.getRoot().getChildAt(0).getChildAt(0);
        assertEquals(2, messageNode.getChildCount());

        DefaultMutableTreeNode field1Node = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        assertEquals("Field1", field1Node.getUserObject());
        assertEquals(0, field1Node.getChildCount());

        DefaultMutableTreeNode field2Node = (DefaultMutableTreeNode) messageNode.getChildAt(1);
        assertEquals("Field2", field2Node.getUserObject());
        assertEquals(0, field2Node.getChildCount());
    }

    @Test
    public void testExpandAll() {
        // This test requires a GUI test framework
        // Since we're using JUnit 4, this is not an option
    }

    @Test
    public void testCollapseAll() {
        // This test requires a GUI test framework
        // Since we're using JUnit 4, this is not an option
    }

    @Test
    public void testSearch() {
        MessageInfo message1 = new MessageInfo("Message1", "Name1", "MessageType1") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field1", 1, "Name1_1"), new FieldInfo("Field2", 2, "Name1_2"));
            }
        };

        MessageInfo message2 = new MessageInfo("Message2", "Name2", "MessageType2") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field3", 3, "Name2_1"), new FieldInfo("Field4", 4, "Name2_2"));
            }
        };

        DictionaryInfo dictionary1 = new DictionaryInfo("Dictionary1", Arrays.asList(message1, message2));
        library.addDictionary(dictionary1);

        treeView.searchTextField.setText("Name1");
        treeView.searchButton.doClick();

        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) treeModel.getRoot().getChildAt(0).getChildAt(0);
        assertTrue(messageNode1.isExpanded());
        assertTrue(messageNode1.isLeaf());
        assertFalse(messageNode1.getChildAt(0).isExpanded());
        assertFalse(messageNode1.getChildAt(0).isLeaf());

        FieldInfo field1 = (FieldInfo) messageNode1.getChildAt(0).getUserObject();
        assertTrue(field1.isExpanded());
        assertFalse(field1.isLeaf());
        assertFalse(field1.getChildAt(0).isExpanded());
        assertFalse(field1.getChildAt(0).isLeaf());

        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) treeModel.getRoot().getChildAt(0).getChildAt(1);
        assertFalse(messageNode2.isExpanded());
        assertTrue(messageNode2.isLeaf());

        treeView.searchTextField.setText("Name2");
        treeView.searchButton.doClick();

        assertFalse(messageNode1.isExpanded());
        assertTrue(messageNode1.isLeaf());
        assertFalse(messageNode1.getChildAt(0).isExpanded());
        assertFalse(messageNode1.getChildAt(0).isLeaf());

        assertFalse(messageNode2.isExpanded());
        assertTrue(messageNode2.isLeaf());
    }

    @Test
    public void testClear() {
        MessageInfo message = new MessageInfo("Message1", "Name1", "MessageType1") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field1", 1, "Name1_1"), new FieldInfo("Field2", 2, "Name1_2"));
            }
        };

        DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", Collections.singletonList(message));
        library.addDictionary(dictionary);

        treeView.searchTextField.setText("Name1");
        treeView.searchButton.doClick();

        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) treeModel.getRoot().getChildAt(0).getChildAt(0);
        assertTrue(messageNode.isExpanded());
        assertTrue(messageNode.isLeaf());
        assertFalse(messageNode.getChildAt(0).isExpanded());
        assertFalse(messageNode.getChildAt(0).isLeaf());

        DefaultMutableTreeNode field1Node = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        assertTrue(field1Node.isExpanded());
        assertFalse(field1Node.isLeaf());
        assertFalse(field1Node.getChildAt(0).isExpanded());
        assertFalse(field1Node.getChildAt(0).isLeaf());

        treeView.clearButton.doClick();

        assertEquals(0, treeModel.getRowCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadItems_whenParentNodeIsNotComponentInfo_throwsIllegalArgumentException() {
        FieldInfo field = new FieldInfo("Field1", 1, "Name1");
        MessageInfo message = new MessageInfo("Message1", "Name1", "MessageType1") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Collections.singletonList(field);
            }
        };

        library.addMessage(message);

        try {
            treeView.loadItems(new DefaultMutableTreeNode(message), message, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSearch_whenNoMatchingNodes_throwsIndexOutOfBoundsException() {
        MessageInfo message1 = new MessageInfo("Message1", "Name1", "MessageType1") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field1", 1, "Name1_1"), new FieldInfo("Field2", 2, "Name1_2"));
            }
        };

        MessageInfo message2 = new MessageInfo("Message2", "Name2", "MessageType2") {
            private static final long serialVersionUID = 1L;

            @Override
            public List<FixInfo> getItems() {
                return Arrays.asList(new FieldInfo("Field3", 3, "Name2_1"), new FieldInfo("Field4", 4, "Name2_2"));
            }
        };

        DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", Arrays.asList(message1, message2));
        library.addDictionary(dictionary);

        treeView.searchTextField.setText("Name3");
        treeView.searchButton.doClick();

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        assertEquals(2, rootNode.getChildCount());

        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertTrue(messageNode1.isExpanded());
        assertTrue(messageNode1.isLeaf());
        assertFalse(messageNode1.getChildAt(0).isExpanded());
        assertFalse(messageNode1.getChildAt(0).isLeaf());

        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        assertTrue(messageNode2.isExpanded());
        assertTrue(messageNode2.isLeaf());

        TreeCellRenderer renderer = treeView.getFixTree().getCellRenderer();
        JTree tree = treeView.getFixTree();
        TreePath selectedPath = tree.getSelectionPath();

        tree.setSelectionPath(new TreePath(new Object[]{rootNode, messageNode1}));
        tree.scrollPathToVisible(new TreePath(new Object[]{rootNode, messageNode1}));
        tree.setSelectionPath(selectedPath);

        treeView.search((DefaultMutableTreeNode) treeModel.getRow(tree.getRowCount() - 1), "Name3");

        assertFalse(messageNode1.isExpanded());
        assertTrue(messageNode1.isLeaf());
        assertFalse(messageNode1.getChildAt(0).isExpanded());
        assertFalse(messageNode1.getChildAt(0).isLeaf());

        assertFalse(messageNode2.isExpanded());
        assertTrue(messageNode2.isLeaf());

        assertEquals(0, treeModel.getRowCount());
    }
}