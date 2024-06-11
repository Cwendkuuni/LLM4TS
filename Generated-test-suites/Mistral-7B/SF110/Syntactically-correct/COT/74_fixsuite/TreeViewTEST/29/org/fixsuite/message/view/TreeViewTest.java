package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

    private Library library;
    private TreeView treeView;
    private JTree fixTree;
    private TreeCellRenderer cellRenderer;

    @Before
    public void setUp() {
        library = Mockito.mock(Library.class);
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }

    @Test
    public void testInitGUI() {
        treeView.initGUI();
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
        DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field = Mockito.mock(FieldInfo.class);

        when(library.getDictionaries()).thenReturn(Arrays.asList(dictionary));
        when(dictionary.getMessages()).thenReturn(Collections.singletonList(message));
        when(message.getItems()).thenReturn(Arrays.asList(component, field));
        when(component.getItems()).thenReturn(Collections.emptyList());

        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        assertEquals(1, rootNode.getChildCount());
        assertEquals(2, ((DefaultMutableTreeNode)rootNode.getChildAt(0)).getChildCount());
    }

    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field = Mockito.mock(FieldInfo.class);

        when(library.getDictionaries()).thenReturn(Collections.singletonList(dictionary));
        when(dictionary.getMessages()).thenReturn(Collections.singletonList(message));
        when(message.getItems()).thenReturn(Arrays.asList(component, field));
        when(component.getItems()).thenReturn(Collections.emptyList());

        FixInfoTreeNode versionNode = Mockito.mock(FixInfoTreeNode.class);
        treeView.loadMessages(versionNode, dictionary);

        assertEquals(2, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field = Mockito.mock(FieldInfo.class);

        when(component.getItems()).thenReturn(Collections.singletonList(field));

        FixInfoTreeNode branchNode = Mockito.mock(FixInfoTreeNode.class);
        treeView.loadItems(branchNode, field, 0);

        assertEquals(1, branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        TreeModel treeModel = fixTree.getModel();
        int initialRowCount = fixTree.getRowCount();

        treeView.expandAll();

        int expandedRowCount = fixTree.getRowCount();
        assertEquals(initialRowCount, expandedRowCount);
    }

    @Test(expected = IllegalStateException.class)
    public void testCollapseAllWithNullTree() {
        treeView.collapseAll();
        assertNull(fixTree);
        fail("IllegalStateException expected but not thrown");
    }

    @Test
    public void testSearch() {
        DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field1 = Mockito.mock(FieldInfo.class);
        FieldInfo field2 = Mockito.mock(FieldInfo.class);

        when(library.getDictionaries()).thenReturn(Arrays.asList(dictionary));
        when(dictionary.getMessages()).thenReturn(Collections.singletonList(message));
        when(message.getItems()).thenReturn(Arrays.asList(component));
        when(component.getItems()).thenReturn(Arrays.asList(field1, field2));

        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        String searchString = "Field";
        treeView.search(rootNode, searchString);

        FixInfoTreeNode field1Node = (FixInfoTreeNode)fixTree.getModel().getRoot().getPathForRow(0).getLastPathComponent();
        FixInfoTreeNode field2Node = (FixInfoTreeNode)fixTree.getModel().getRoot().getPathForRow(1).getLastPathComponent();

        assertTrue(field1Node.isHighlighted());
        assertTrue(field2Node.isHighlighted());
    }

    @Test
    public void testClear() {
        DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field = Mockito.mock(FieldInfo.class);

        when(library.getDictionaries()).thenReturn(Collections.singletonList(dictionary));
        when(dictionary.getMessages()).thenReturn(Collections.singletonList(message));
        when(message.getItems()).thenReturn(Arrays.asList(component, field));
        when(component.getItems()).thenReturn(Collections.emptyList());

        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.clear(rootNode);

        assertFalse(rootNode.isHighlighted());
    }

    @Test
    public void testValueChanged() {
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);

        when(message.getMessageType()).thenReturn("TEST");
        when(component.getName()).thenReturn("TEST_COMPONENT");

        TreeSelectionEvent event = Mockito.mock(TreeSelectionEvent.class);
        TreePath treePath = new TreePath(new Object[]{"root", "message", "component"});

        treeView.valueChanged(event);

        verify(LibrarySuite.getInstance().getDetailsPanel()).displayDetails(Mockito.eq(message), Mockito.eq(component));
    }

    @Test
    public void testActionPerformed() {
        treeView.collapseAll();
        treeView.expandAll();
        treeView.searchTextField.setText("Test");
        treeView.actionPerformed(Mockito.mock(ActionEvent.class));

        assertEquals(5, fixTree.getRowCount());
    }

    @Test
    public void testKeyPressed() {
        treeView.actionPerformed(Mockito.mock(ActionEvent.class));
        treeView.searchTextField.setText("Test");
        treeView.keyPressed(Mockito.mock(KeyEvent.class));

        treeView.actionPerformed(Mockito.mock(ActionEvent.class));
    }
}