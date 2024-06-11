package org.fixsuite.message.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

    @Test
    public void testTreeViewInitialization() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        assertEquals("Center", treeView.getLayout().getName());
        assertTrue(treeView.mainScrollPane != null);
        assertTrue(treeView.fixTree != null);
        assertTrue(treeView.searchLabel != null);
        assertTrue(treeView.searchTextField != null);
        assertTrue(treeView.searchButton != null);
        assertTrue(treeView.clearButton != null);
        assertTrue(treeView.expandButton != null);
        assertTrue(treeView.collapseButton != null);
        assertTrue(treeView.add != null);
        assertTrue(treeView.getToolBar() != null);
    }

    @Test
    public void testLoadFIXTree() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<>());
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertTrue(rootNode.getUserObject().equals("Dictionaries"));
        assertTrue(rootNode.getChildCount() == 0);
    }

    @Test
    public void testLoadMessages() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        DictionaryInfo dictionaryMock = Mockito.mock(DictionaryInfo.class);
        Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList() {{
            add(dictionaryMock);
        }});
        Mockito.when(dictionaryMock.getMessages()).thenReturn(new ArrayList<>());
        FixInfoTreeNode versionNode = treeView.loadFIXTree().getChildAt(0);
        Mockito.verify(dictionaryMock).getVersion();
        assertEquals(0, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        DictionaryInfo dictionaryMock = Mockito.mock(DictionaryInfo.class);
        MessageInfo messageMock = Mockito.mock(MessageInfo.class);
        Mockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList() {{
            add(dictionaryMock);
        }});
        Mockito.when(dictionaryMock.getMessages()).thenReturn(new ArrayList() {{
            add(messageMock);
        }});
        FixInfoTreeNode versionNode = treeView.loadFIXTree().getChildAt(0);
        FixInfoTreeNode leafNode = treeView.loadItems(versionNode, messageMock, 0).getChildAt(0);
        assertEquals("MessageInfo", leafNode.getUserObject().getClass().getName());
        Mockito.verify(messageMock).getName();
        Mockito.verify(messageMock).getMessageType();
    }

    @Test
    public void testExpandAll() {
        TreeView treeView = new TreeView(null);
        JTree tree = treeView.fixTree;
        int rowCountBeforeExpandAll = tree.getRowCount();
        treeView.expandAll();
        int rowCountAfterExpandAll = tree.getRowCount();
        assertEquals(rowCountBeforeExpandAll, rowCountAfterExpandAll);
    }

    @Test
    public void testCollapseAll() {
        TreeView treeView = new TreeView(null);
        JTree tree = treeView.fixTree;
        int rowCountBeforeCollapseAll = tree.getRowCount();
        treeView.collapseAll();
        int rowCountAfterCollapseAll = tree.getRowCount();
        assertEquals(rowCountBeforeCollapseAll, rowCountAfterCollapseAll);
    }

    @Test
    public void testSearch() {
        TreeView treeView = new TreeView(null);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        String searchString = "Test";
        treeView.search(rootNode, searchString);
        for (int i = 0; i < rootNode.getChildCount(); ++i) {
            FixInfoTreeNode node = rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testSearchWithMatchingItem() {
        TreeView treeView = new TreeView(null);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        MessageInfo mockMessage = Mockito.mock(MessageInfo.class);
        Mockito.when(mockMessage.getName()).thenReturn("TestMessage");
        FixInfoTreeNode messageNode = treeView.loadItems(rootNode, mockMessage, 0).getChildAt(0);
        treeView.search(messageNode, searchString);
        assertTrue(messageNode.isHighlighted());
    }

    @Test
    public void testClear() {
        TreeView treeView = new TreeView(null);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.clear(rootNode);
        for (int i = 0; i < rootNode.getChildCount(); ++i) {
            FixInfoTreeNode node = rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }
}