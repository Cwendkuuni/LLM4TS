package org.fixsuite.message.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {
    private static final String SEARCH_STRING = "test";
    private Library libraryMock;
    private TreeView treeView;
    private JTree fixTreeMock;
    private DefaultMutableTreeNode rootNodeMock;
    private DefaultMutableTreeNode dictionaryNodeMock;
    private DefaultMutableTreeNode messageNodeMock;
    private DefaultMutableTreeNode fieldNodeMock;
    private DefaultTreeModel treeModelMock;
    
    @Before
    public void setUp() {
        libraryMock = Mockito.mock(Library.class);
        treeView = new TreeView(libraryMock);
        
        fixTreeMock = Mockito.mock(JTree.class);
        treeModelMock = Mockito.mock(DefaultTreeModel.class);
        rootNodeMock = Mockito.mock(DefaultMutableTreeNode.class);
        dictionaryNodeMock = Mockito.mock(DefaultMutableTreeNode.class);
        messageNodeMock = Mockito.mock(DefaultMutableTreeNode.class);
        fieldNodeMock = Mockito.mock(DefaultMutableTreeNode.class);
        
        Mockito.when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(new DictionaryInfo("Test", new ArrayList<>())));
        
        Mockito.when(rootNodeMock.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(rootNodeMock.getChildCount()).thenReturn(1);
        Mockito.when(rootNodeMock.getChild(0)).thenReturn(dictionaryNodeMock);
        
        Mockito.when(dictionaryNodeMock.getUserObject()).thenReturn("Test (1)");
        Mockito.when(dictionaryNodeMock.getChildCount()).thenReturn(2);
        Mockito.when(dictionaryNodeMock.getChild(0)).thenReturn(messageNodeMock);
        Mockito.when(dictionaryNodeMock.getChild(1)).thenReturn(fieldNodeMock);
        
        Mockito.when(messageNodeMock.getUserObject()).thenReturn(new MessageInfo("TestMessage", "TestMessageType"));
        Mockito.when(messageNodeMock.getChildCount()).thenReturn(0);
        
        Mockito.when(fieldNodeMock.getUserObject()).thenReturn(new FieldInfo("TestField", "TestTagNumber"));
        Mockito.when(fieldNodeMock.getChildCount()).thenReturn(0);
        
        Mockito.when(treeModelMock.getRoot()).thenReturn(rootNodeMock);
        Mockito.when(fixTreeMock.getModel()).thenReturn(treeModelMock);
        
        treeView.initGUI();
        treeView.add(fixTreeMock);
    }
    
    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }
    
    @Test
    public void testLoadFIXTree() {
        treeView.loadFIXTree();
        verify(treeModelMock, times(1)).addNode(rootNodeMock, 0);
        verify(rootNodeMock, times(1)).add(dictionaryNodeMock);
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int i = 0; i < fixTreeMock.getRowCount(); ++i) {
            treeView.fixTree.expandRow(i);
        }
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int i = fixTreeMock.getRowCount() - 1; i >= 0; --i) {
            treeView.fixTree.collapseRow(i);
        }
    }
    
    @Test
    public void testSearch() {
        treeView.searchTextField.setText(SEARCH_STRING);
        treeView.searchButton.doClick();
        verify(dictionaryNodeMock, times(1)).getFirstChild();
        verify(messageNodeMock, times(1)).getFirstChild();
        verify(fieldNodeMock, times(1)).getFirstChild();
        
        treeView.searchTextField.setText("");
        treeView.searchButton.doClick();
        Mockito.verifyNoMoreInteractions(dictionaryNodeMock, messageNodeMock, fieldNodeMock);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullLibrary() {
        treeView = new TreeView(null);
    }
    
    @Test
    public void testClear() {
        treeView.searchTextField.setText(SEARCH_STRING);
        treeView.searchButton.doClick();
        treeView.clearButton.doClick();
        Mockito.verifyNoMoreInteractions(dictionaryNodeMock, messageNodeMock, fieldNodeMock);
    }
    
    @Test
    public void testKeyPressedEvent() {
        treeView.addKeyListener(Mockito.mock(KeyListener.class));
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);
        treeView.keyPressed(keyEvent);
        verify(treeView, times(1)).searchButton.doClick();
    }
    
    @Test
    public void testActionPerformedEvent() {
        treeView.addActionListener(Mockito.mock(ActionListener.class));
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        treeView.actionPerformed(actionEvent);
        if (actionEvent.getSource().equals(treeView.expandButton)) {
            treeView.expandAll();
        }
        else if (actionEvent.getSource().equals(treeView.collapseButton)) {
            treeView.collapseAll();
        }
        else if (actionEvent.getSource().equals(treeView.searchButton)) {
            treeView.search((FixInfoTreeNode) treeView.fixTree.getModel().getRoot(), SEARCH_STRING);
        }
        else if (actionEvent.getSource().equals(treeView.clearButton)) {
            treeView.clear((FixInfoTreeNode) treeView.fixTree.getModel().getRoot());
        }
    }
    
    @Test
    public void testTreeSelectionEvent() {
        treeView.addTreeSelectionListener(Mockito.mock(TreeSelectionListener.class));
        TreeSelectionEvent treeSelectionEvent = Mockito.mock(TreeSelectionEvent.class);
        treeView.valueChanged(treeSelectionEvent);
        if (treeSelectionEvent.getPath().getLastPathComponent() instanceof FieldInfo) {
            LibrarySuite.getInstance().getDetailsPanel().displayDetails(Mockito.any(FixInfo.class), Mockito.any(ComponentInfo.class));
        }
        else if (treeSelectionEvent.getPath().getLastPathComponent() instanceof MessageInfo) {
            LibrarySuite.getInstance().getDetailsPanel().displayDetails(Mockito.any(FixInfo.class), null);
        }
        else if (treeSelectionEvent.getPath().getLastPathComponent() instanceof ComponentInfo) {
            LibrarySuite.getInstance().getDetailsPanel().displayDetails(Mockito.any(FixInfo.class), null);
        }
        else {
            LibrarySuite.getInstance().getDetailsPanel().clearDisplay();
        }
    }
}