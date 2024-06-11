package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.util.*;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

    @Mock
    private Library library;

    @Mock
    private FixInfoTreeNode root;

    @Mock
    private FixInfoTreeNode node;

    @Mock
    private MessageInfo message;

    @Mock
    private ComponentInfo component;

    @Mock
    private FieldInfo field;

    @Mock
    private CompositeFixInfo composite;

    @Mock
    private TreePath treePath;

    @Mock
    private FixInfo info;

    @Mock
    private TreeModel treeModel;

    @Mock
    private DetailsPanel detailsPanel;

    private TreeView treeView;

    @Test
    public void testTreeViewInitialization() {
        treeView = new TreeView(library);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertEquals(library, treeView.library);
    }

    @Test
    public void testLoadFIXTree() {
        when(library.getDictionaries()).thenReturn(Arrays.asList(messageDictionary, componentDictionary));

        when(messageDictionary.getMessages()).thenReturn(Arrays.asList(message1, message2));
        when(message1.getItems()).thenReturn(Arrays.asList(field1, field2));

        when(componentDictionary.getMessages()).thenReturn(Collections.singletonList(componentMessage));

        when(root.getChildCount()).thenReturn(1);
        when(root.getChildAt(0)).thenReturn(node);

        treeView = new TreeView(library);

        verify(treeView).fixTree.setModel(treeModel);
        verify(treeModel).getRoot();
        assertEquals(root, treeView.fixTree.getModel().getRoot());
    }

    @Test
    public void testExpandAll() {
        treeView = new TreeView(library);
        treeView.expandAll();

        verify(fixTree).expandRow(Mockito.anyInt());
    }

    @Test
    public void testCollapseAll() {
        treeView = new TreeView(library);
        treeView.collapseAll();

        verify(fixTree, times(fixTree.getRowCount())).collapseRow(Mockito.anyInt());
    }

    @Test
    public void testSearch() {
        when(library.getDictionaries()).thenReturn(Arrays.asList(messageDictionary, componentDictionary));

        when(messageDictionary.getMessages()).thenReturn(Arrays.asList(message1, message2));
        when(message1.getItems()).thenReturn(Arrays.asList(field1, field2));

        when(componentDictionary.getMessages()).thenReturn(Collections.singletonList(componentMessage));

        when(root.getChildCount()).thenReturn(1);
        when(root.getChildAt(0)).thenReturn(node);

        when(node.getChildCount()).thenReturn(1);
        when(node.getChildAt(0)).thenReturn(leafNode);

        when(leafNode.getUserObject()).thenReturn(field);

        when(field.getName()).thenReturn("Test");
        when(field.getTagNumber()).thenReturn("123");

        treeView = new TreeView(library);

        treeView.searchTextField.setText("Test");
        treeView.searchButton.doClick();

        verify(treeView).search(Mockito.eq(root), Mockito.eq("Test"));
    }

    @Test
    public void testClear() {
        treeView = new TreeView(library);

        treeView.searchTextField.setText("Test");
        treeView.searchButton.doClick();

        treeView.clearButton.doClick();

        verify(treeView).clear(Mockito.eq(root));
    }

    @Test
    public void testValueChanged() {
        treeView = new TreeView(library);

        when(node.getUserObject()).thenReturn(info);
        when(info instanceof MessageInfo).thenReturn(true);

        treeView.valueChanged(new TreeSelectionEvent(treeView, new TreePath(node.getPath())));

        verify(detailsPanel).displayDetails(Mockito.eq(info), Mockito.eq(component));
    }

    @Test
    public void testValueChangedComponent() {
        treeView = new TreeView(library);

        when(node.getUserObject()).thenReturn(info);
        when(info instanceof ComponentInfo).thenReturn(true);

        treeView.valueChanged(new TreeSelectionEvent(treeView, new TreePath(node.getPath())));

        verify(detailsPanel).displayDetails(Mockito.eq(info), Mockito.eq(null));
    }

    @Test
    public void testValueChangedField() {
        treeView = new TreeView(library);

        when(node.getUserObject()).thenReturn(info);
        when(info instanceof FieldInfo).thenReturn(true);

        TreePath parentPath = new TreePath(new Object[]{"Dictionaries", "Component"});
        when(node.getPath()).thenReturn(new TreePath(new Object[]{parentPath.getLastPathComponent(), node.getUserObject()}));

        treeView.valueChanged(new TreeSelectionEvent(treeView, new TreePath(node.getPath())));

        verify(detailsPanel).displayDetails(Mockito.eq(info), Mockito.eq(component));
    }

    @Test
    public void testActionPerformedCollapseAll() {
        treeView = new TreeView(library);

        treeView.actionPerformed(new ActionEvent(searchButton, ActionEvent.ACTION_PERFORMED, ""));

        verify(treeView).collapseAll();
    }

    @Test
    public void testActionPerformedExpandAll() {
        treeView = new TreeView(library);

        treeView.actionPerformed(new ActionEvent(expandButton, ActionEvent.ACTION_PERFORMED, ""));

        verify(treeView).expandAll();
    }

    @Test
    public void testActionPerformedSearch() {
        treeView = new TreeView(library);

        treeView.searchTextField.setText("Test");
        treeView.actionPerformed(new ActionEvent(searchButton, ActionEvent.ACTION_PERFORMED, ""));

        verify(treeView).search(Mockito.eq(root), Mockito.eq("Test"));
    }

    @Test
    public void testActionPerformedClear() {
        treeView = new TreeView(library);

        treeView.searchTextField.setText("Test");
        treeView.actionPerformed(new ActionEvent(clearButton, ActionEvent.ACTION_PERFORMED, ""));

        verify(treeView).clear(Mockito.eq(root));
    }

    @Test
    public void testKeyPressedSearch() {
        treeView = new TreeView(library);

        treeView.searchTextField.setText("Test");
        treeView.searchTextField.requestFocus();
        treeView.doLayout();
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView, 10, true, true, 0, KeyEvent.VK_ENTER, 0));

        verify(treeView).search(Mockito.eq(root), Mockito.eq("Test"));
    }

    private FixInfoTreeNode createNode(Object userObject) {
        return new FixInfoTreeNode(userObject);
    }

    private FixInfo createFixInfo() {
        return Mockito.mock(FixInfo.class);
    }

    private DictionaryInfo createDictionaryInfo() {
        return Mockito.mock(DictionaryInfo.class);
    }

    private MessageInfo createMessageInfo() {
        return Mockito.mock(MessageInfo.class);
    }

    private ComponentInfo createComponentInfo() {
        return Mockito.mock(ComponentInfo.class);
    }

    private FieldInfo createFieldInfo() {
        return Mockito.mock(FieldInfo.class);
    }

    private CompositeFixInfo createCompositeFixInfo() {
        return Mockito.mock(CompositeFixInfo.class);
    }
}