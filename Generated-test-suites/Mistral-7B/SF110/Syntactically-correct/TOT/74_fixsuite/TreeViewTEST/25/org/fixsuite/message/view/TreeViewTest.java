package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TreeViewTest {

 private TreeView treeView;
 @Mock
 private Library library;
 @Mock
 private FixInfoTreeNode rootNode;
 @Mock
 private FixInfoTreeNode messageNode;
 @Mock
 private FixInfoTreeNode componentNode;
 @Mock
 private FixInfoTreeNode fieldNode;
 @Mock
 private TreeCellRenderer renderer;
 @Mock
 private JTree tree;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.treeView = new TreeView(this.library);
 when(this.library.getDictionaries()).thenReturn(Arrays.asList(new DictionaryInfo("Version 1"), new DictionaryInfo("Version 2")));
 when(this.library.getDictionaries().get(0).getMessages()).thenReturn(Collections.singletonList(new MessageInfo("MessageType1", "Name1")));
 when(this.library.getDictionaries().get(1).getMessages()).thenReturn(Collections.singletonList(new MessageInfo("MessageType2", "Name2")));
 when(this.library.getDictionaries().get(0).getDictionaries()).thenReturn(Collections.emptyList());
 when(this.library.getDictionaries().get(1).getDictionaries()).thenReturn(Collections.singletonList(new DictionaryInfo("Version3")));
 when(this.rootNode.getUserObject()).thenReturn("Dictionaries");
 when(this.rootNode.getChildCount()).thenReturn(2);
 when(this.rootNode.getChildAt(0)).thenReturn(this.messageNode);
 when(this.rootNode.getChildAt(1)).thenReturn(this.componentNode);
 when(this.messageNode.getUserObject()).thenReturn("MessageType1");
 when(this.messageNode.getChildCount()).thenReturn(1);
 when(this.messageNode.getChildAt(0)).thenReturn(this.fieldNode);
 when(this.fieldNode.getUserObject()).thenReturn(new FieldInfo(1, "Name1", "TagNumber1"));
 when(this.componentNode.getUserObject()).thenReturn("Component1");
 when(this.componentNode.getChildCount()).thenReturn(0);
 when(this.tree.setModel(any(TreeModel.class))).thenReturn(true);
 when(this.renderer.getTreeCellRendererComponent(any(JTree.class), anyObject(), false, false, false, -1, false)).thenReturn(new JLabel());
 this.treeView.initGUI();
 this.treeView.setTreeCellRenderer(this.renderer);
 this.treeView.fixTree = this.tree;
 }

 @Test
 public void testLoadFIXTree() {
 assertEquals(2, this.treeView.fixTree.getModel().getChildCount());
 verify(this.treeView.fixTree, times(1)).setModel(this.treeView.loadFIXTree());
 }

 @Test
 public void testLoadMessages() {
 assertEquals(1, this.messageNode.getChildCount());
 verify(this.treeView, times(1)).loadMessages(this.messageNode, this.library.getDictionaries().get(0));
 }

 @Test
 public void testLoadItems() {
 assertEquals(1, this.fieldNode.getChildCount());
 verify(this.treeView, times(1)).loadItems(this.fieldNode, (CompositeFixInfo)this.fieldNode.getUserObject(), 0);
 }

 @Test
 public void testExpandAll() {
 verify(this.tree, times(1)).expandAll();
 }

 @Test
 public void testCollapseAll() {
 verify(this.tree, times(1)).collapseAll();
 }

 @Test
 public void testSearch() {
 this.treeView.searchTextField.setText("Name1");
 this.treeView.searchButton.doClick();
 verify(this.fieldNode, times(1)).setHighlighted(true);
 }

 @Test
 public void testSearchWithEmptyString() {
 this.treeView.searchTextField.setText("");
 this.treeView.searchButton.doClick();
 verifyNoMoreInteractions(this.fieldNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNullString() {
 this.treeView.searchTextField.setText(null);
 this.treeView.searchButton.doClick();
 }

 @Test
 public void testClear() {
 this.treeView.searchTextField.setText("Name1");
 this.treeView.searchButton.doClick();
 this.treeView.clearButton.doClick();
 verifyNoMoreInteractions(this.fieldNode);
 }

 @Test(expected = IllegalStateException.class)
 public void testClearWithEmptyTree() {
 this.treeView.searchTextField.setText("Name1");
 this.treeView.clearButton.doClick();
 verifyNoMoreInteractions(this.treeView.fixTree);
 this.treeView.fixTree = null;
 this.treeView.clearButton.doClick();
 }
}