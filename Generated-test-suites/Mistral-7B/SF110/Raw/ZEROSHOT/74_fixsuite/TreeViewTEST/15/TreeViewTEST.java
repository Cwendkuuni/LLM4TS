package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
    }

    @Test
    public void testInitGUI() {
        // Check if the components have been initialized
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree() {
        // Test if the tree is loaded with the correct data
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        int numberOfDictionaries = dictionaries.size();

        assertEquals(numberOfDictionaries, root.getChildCount());

        for (int i = 0; i < numberOfDictionaries; i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) root.getChildAt(i);
            assertEquals("Version " + (i + 1), dictionaryNode.getUserObject().toString());

            int numberOfMessages = ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().size();

            assertEquals(numberOfMessages, dictionaryNode.getChildCount());

            for (int j = 0; j < numberOfMessages; j++) {
                DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals("Message " + (j + 1), messageNode.getUserObject().toString());
            }
        }
    }

    @Test
    public void testLoadItems() {
        // Test if the items are loaded correctly under the messages
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) fixTree.getModel().getRoot().getChildAt(0);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);

        int numberOfItems = ((MessageInfo) messageNode.getUserObject()).getItems().size();

        assertEquals(numberOfItems, messageNode.getChildCount());

        for (int i = 0; i < numberOfItems; i++) {
            DefaultMutableTreeNode itemNode = (DefaultMutableTreeNode) messageNode.getChildAt(i);
            assertEquals("Item " + (i + 1), itemNode.getUserObject().toString());
        }
    }

    @Test
    public void testExpandAll() {
        // Test if the tree is expanded correctly when expandAll is called
        treeView.expandAll();

        for (int i = 0; i < fixTree.getRowCount(); i++) {
            assertTrue(fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRoot(), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0) })));
        }
    }

    @Test
    public void testCollapseAll() {
        // Test if the tree is collapsed correctly when collapseAll is called
        treeView.collapseAll();

        for (int i = 0; i < fixTree.getRowCount(); i++) {
            assertFalse(fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRoot(), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0) })));
        }
    }

    @Test
    public void testSearch() {
        // Test if the search function works correctly
        String searchString = "M";

        treeView.search(fixTree.getModel().getRoot(), searchString);

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) fixTree.getModel().getRoot();

        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(j);
                    if (childNode.isHighlighted()) {
                        if (childNode.getUserObject() instanceof MessageInfo) {
                            MessageInfo messageInfo = (MessageInfo) childNode.getUserObject();
                            assertTrue(messageInfo.getName().matches(searchString) || messageInfo.getMessageType().matches(searchString));
                        } else if (childNode.getUserObject() instanceof FieldInfo) {
                            FieldInfo fieldInfo = (FieldInfo) childNode.getUserObject();
                            assertTrue(fieldInfo.getName().matches(searchString) || "" + fieldInfo.getTagNumber().matches(searchString));
                        } else if (childNode.getUserObject() instanceof ComponentInfo) {
                            ComponentInfo componentInfo = (ComponentInfo) childNode.getUserObject();
                            assertTrue(componentInfo.getName().matches(searchString));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testClear() {
        // Test if the clear function works correctly
        treeView.search(fixTree.getModel().getRoot(), "D");

        int numberOfHighlightedNodesBeforeClear = 0;

        for (int i = 0; i < fixTree.getRowCount(); i++) {
            TreePath treePath = fixTree.getPathForLocation(0, i);
            if (fixTree.isSelectedPath(treePath)) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fixTree.getModel().getPathForRow(i);
                if (node.isHighlighted()) {
                    numberOfHighlightedNodesBeforeClear++;
                }
            }
        }

        treeView.clear((DefaultMutableTreeNode) fixTree.getModel().getRoot());

        int numberOfHighlightedNodesAfterClear = 0;

        for (int i = 0; i < fixTree.getRowCount(); i++) {
            TreePath treePath = fixTree.getPathForLocation(0, i);
            if (fixTree.isSelectedPath(treePath)) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fixTree.getModel().getPathForRow(i);
                if (node.isHighlighted()) {
                    numberOfHighlightedNodesAfterClear++;
                }
            }
        }

        assertEquals(0, numberOfHighlightedNodesAfterClear);
    }

    @Test
    public void testKeyPressedEvent() {
        // Test if the search button is clicked when enter key is pressed
        MockTree treeMock = new MockTree();
        TreeCellRenderer treeCellRendererMock = new MockTreeCellRenderer();
        treeView.setFixTree(treeMock);
        treeView.setFixTreeCellRenderer(treeCellRendererMock);

        treeView.searchTextField.setText("Test");
        treeView.searchTextField.sendKeyEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));

        verify(treeMock).setCellRenderer(eq(treeCellRendererMock));
        verify(treeMock).expandPath(any());
        verify(treeMock).setModel(eq(treeView.loadFIXTree()));
        verify(treeMock).setShowsRootHandles(isFalse());
        verify(treeMock).setRootVisible(isFalse());
        verify(treeMock).setCellRenderer(eq(treeCellRendererMock));
        verify(treeMock).setModel(eq(treeView.loadFIXTree()));

        String searchString = "Test";
        treeView.search(treeMock.getModel().getRoot(), searchString);

        verify(treeMock).setCellRenderer(eq(treeCellRendererMock));
    }
}

class MockTree extends JTree {
    private TreeModel treeModel;

    public TreeModel getModel() {
        return treeModel;
    }

    public void setModel(TreeModel model) {
        this.treeModel = model;
    }
}

class MockTreeCellRenderer extends DefaultTreeCellRenderer {
}