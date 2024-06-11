package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTreeMock;
    private JScrollPane mainScrollPaneMock;
    private Library libraryMock;
    private JLabel searchLabelMock;
    private JTextField searchTextFieldMock;
    private JButton searchButtonMock;
    private JButton clearButtonMock;
    private JButton expandButtonMock;
    private JButton collapseButtonMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.libraryMock = Mockito.mock(Library.class);
        this.fixTreeMock = Mockito.mock(JTree.class);
        this.mainScrollPaneMock = Mockito.mock(JScrollPane.class);
        this.searchLabelMock = Mockito.mock(JLabel.class);
        this.searchTextFieldMock = Mockito.mock(JTextField.class);
        this.searchButtonMock = Mockito.mock(JButton.class);
        this.clearButtonMock = Mockito.mock(JButton.class);
        this.expandButtonMock = Mockito.mock(JButton.class);
        this.collapseButtonMock = Mockito.mock(JButton.class);

        Mockito.when(this.libraryMock.getDictionaries()).thenReturn(new DictionaryInfo[0]);

        this.treeView = new TreeView(this.libraryMock);

        Mockito.when(this.libraryMock.getDetailsPanel()).thenReturn(Mockito.spy(LibrarySuite.getInstance().getDetailsPanel()));

        Mockito.when(this.mainScrollPaneMock.getViewport()).thenReturn(this.mainScrollPaneMock);
        Mockito.when(this.mainScrollPaneMock.getPreferredSize()).thenReturn(new Dimension(300, 400));

        Mockito.when(this.fixTreeMock.getCellRenderer()).thenReturn(this.treeView.getFixInfoCellRenderer());

        Mockito.when(this.treeView.loadFIXTree()).thenReturn(new FixInfoTreeNode("Dictionaries"));

        Mockito.when(this.treeView.getFixTree()).thenReturn(this.fixTreeMock);
        Mockito.when(this.treeView.getMainScrollPane()).thenReturn(this.mainScrollPaneMock);
        Mockito.when(this.treeView.getSearchLabel()).thenReturn(this.searchLabelMock);
        Mockito.when(this.treeView.getSearchTextField()).thenReturn(this.searchTextFieldMock);
        Mockito.when(this.treeView.getSearchButton()).thenReturn(this.searchButtonMock);
        Mockito.when(this.treeView.getClearButton()).thenReturn(this.clearButtonMock);
        Mockito.when(this.treeView.getExpandButton()).thenReturn(this.expandButtonMock);
        Mockito.when(this.treeView.getCollapseButton()).thenReturn(this.collapseButtonMock);

        Mockito.doAnswer(invocation -> {
            final ActionEvent event = (ActionEvent)invocation.getArguments()[0];
            if (event.getSource().equals(this.searchButtonMock)) {
                this.treeView.search((FixInfoTreeNode)this.fixTreeMock.getModel().getRoot(), this.searchTextFieldMock.getText().trim());
            }
            return null;
        }).when(this.treeView).actionPerformed(ArgumentMatchers.any(ActionEvent.class));

        Mockito.doAnswer(invocation -> {
            final TreeSelectionEvent event = (TreeSelectionEvent)invocation.getArguments()[0];
            this.treeView.valueChanged(event);
            return null;
        }).when(this.treeView).valueChanged(ArgumentMatchers.any(TreeSelectionEvent.class));

        Mockito.doAnswer(invocation -> {
            final KeyEvent event = (KeyEvent)invocation.getArguments()[0];
            this.treeView.keyPressed(event);
            return null;
        }).when(this.treeView).keyPressed(ArgumentMatchers.any(KeyEvent.class));

        this.treeView.initGUI();
    }

    @Test
    public void testTreeViewInitialization() {
        assertTrue(this.treeView.getLibrary() == this.libraryMock);
        assertTrue(this.treeView.getFixTree() == this.fixTreeMock);
        assertTrue(this.treeView.getMainScrollPane() == this.mainScrollPaneMock);
        assertTrue(this.treeView.getSearchLabel() == this.searchLabelMock);
        assertTrue(this.treeView.getSearchTextField() == this.searchTextFieldMock);
        assertTrue(this.treeView.getSearchButton() == this.searchButtonMock);
        assertTrue(this.treeView.getClearButton() == this.clearButtonMock);
        assertTrue(this.treeView.getExpandButton() == this.expandButtonMock);
        assertTrue(this.treeView.getCollapseButton() == this.collapseButtonMock);
    }

    @Test
    public void testLoadFIXTree() {
        assertEquals(1, this.treeView.loadFIXTree().getChildCount());
    }

    @Test
    public void testLoadMessages() {
        final DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        final FixInfoTreeNode versionNodeMock = Mockito.mock(FixInfoTreeNode.class);

        Mockito.when(this.libraryMock.getDictionaries()).thenReturn(new DictionaryInfo[]{dictionary});

        this.treeView.loadMessages(versionNodeMock, dictionary);

        assertEquals(1 + DictionaryInfo.NUM_MESSAGES_PER_DICTIONARY, versionNodeMock.getChildCount());
    }

    @Test
    public void testLoadItems() {
        final FixInfoTreeNode branchNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final CompositeFixInfo compositeFixInfoMock = Mockito.mock(CompositeFixInfo.class);
        final FixInfoTreeNode leafNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final FixInfo infoMock = Mockito.mock(FixInfo.class);

        Mockito.when(compositeFixInfoMock.getItems()).thenReturn(new FixInfo[0]);

        this.treeView.loadItems(branchNodeMock, compositeFixInfoMock, 0);

        assertEquals(0, branchNodeMock.getChildCount());

        Mockito.when(infoMock.getItems()).thenReturn(new FixInfo[1]);

        this.treeView.loadItems(branchNodeMock, infoMock, 0);

        assertEquals(1, branchNodeMock.getChildCount());
    }

    @Test
    public void testExpandAll() {
        final FixInfoTreeNode rootNodeMock = Mockito.mock(FixInfoTreeNode.class);

        Mockito.when(this.fixTreeMock.getModel()).thenReturn(new DefaultMutableTreeModel(rootNodeMock));

        this.treeView.expandAll();

        Mockito.verify(this.fixTreeMock, Mockito.times(this.fixTreeMock.getRowCount())).expandRow(ArgumentMatchers.anyInt());
    }

    @Test
    public void testCollapseAll() {
        final FixInfoTreeNode rootNodeMock = Mockito.mock(FixInfoTreeNode.class);

        Mockito.when(this.fixTreeMock.getModel()).thenReturn(new DefaultMutableTreeModel(rootNodeMock));

        this.treeView.collapseAll();

        Mockito.verify(this.fixTreeMock, Mockito.times(this.fixTreeMock.getRowCount())).collapseRow(ArgumentMatchers.anyInt());
    }

    @Test
    public void testSearch() {
        final FixInfoTreeNode rootNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final FixInfoTreeNode messageNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final MessageInfo messageMock = Mockito.mock(MessageInfo.class);
        final FixInfoTreeNode fieldNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final FieldInfo fieldMock = Mockito.mock(FieldInfo.class);
        final FixInfoTreeNode componentNodeMock = Mockito.mock(FixInfoTreeNode.class);
        final ComponentInfo componentMock = Mockito.mock(ComponentInfo.class);

        Mockito.when(rootNodeMock.getChildCount()).thenReturn(3);
        Mockito.when(rootNodeMock.getChildAt(0)).thenReturn(messageNodeMock);
        Mockito.when(rootNodeMock.getChildAt(1)).thenReturn(fieldNodeMock);
        Mockito.when(rootNodeMock.getChildAt(2)).thenReturn(componentNodeMock);

        Mockito.when(messageNodeMock.getChildCount()).thenReturn(1);
        Mockito.when(messageNodeMock.getChildAt(0)).thenReturn(fieldNodeMock);

        Mockito.when(fieldNodeMock.getChildCount()).thenReturn(0);

        Mockito.when(componentNodeMock.getChildCount()).thenReturn(1);
        Mockito.when(componentNodeMock.getChildAt(0)).thenReturn(fieldNodeMock);

        Mockito.when(messageMock.getName()).thenReturn("TestMessage");
        Mockito.when(messageMock.getMessageType()).thenReturn("TEST");
        Mockito.when(fieldMock.getName()).thenReturn("TestField");
        Mockito.when(fieldMock.getTagNumber()).thenReturn("123");
        Mockito.when(componentMock.getName()).thenReturn("TestComponent");

        Mockito.when(messageNodeMock.getUserObject()).thenReturn(messageMock);
        Mockito.when(fieldNodeMock.getUserObject()).thenReturn(fieldMock);
        Mockito.when(componentNodeMock.getUserObject()).thenReturn(componentMock);

        this.treeView.search(rootNodeMock, "Test.*");

        Mockito.verify(fieldNodeMock, Mockito.times(1)).setHighlighted(true);
        Mockito.verify(messageNodeMock, Mockito.times(1)).expandPath(ArgumentMatchers.any(TreePath.class));
        Mockito.verify(fieldNodeMock, Mockito.times(1)).expandPath(ArgumentMatchers.any(TreePath.class));

        Mockito.verify(componentNodeMock, Mockito.never()).setHighlighted(true);
    }

    @Test
    public void testClear() {
        final FixInfoTreeNode rootNodeMock = Mockito.mock(FixInfoTreeNode.class);

        Mockito.when(rootNodeMock.getChildCount()).thenReturn(1);
        Mockito.when(rootNodeMock.getChildAt(0)).thenReturn(Mockito.mock(FixInfoTreeNode.class));

        this.treeView.clear(rootNodeMock);

        Mockito.verify(rootNodeMock, Mockito.times(1)).setHighlighted(false);
        Mockito.verify(rootNodeMock.getChildAt(0), Mockito.times(1)).setHighlighted(false);
    }
}