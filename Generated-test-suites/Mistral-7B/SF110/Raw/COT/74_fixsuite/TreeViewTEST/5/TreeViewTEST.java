package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.*;
import org.junit.runner.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

 private TreeView treeView;
 private Library library;
 private JTree fixTree;
 private JTree mockTree;
 private JScrollPane mockScrollPane;
 private TreeSelectionModel selectionModel;
 private TreeModel treeModel;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 library = mock(Library.class);
 treeView = new TreeView(library);
 fixTree = mock(JTree.class);
 treeModel = mock(TreeModel.class);
 selectionModel = mock(TreeSelectionModel.class);
 mockScrollPane = mock(JScrollPane.class);
 when(treeView.getFixTree()).thenReturn(fixTree);
 when(treeView.getLibrary()).thenReturn(library);
 when(fixTree.getModel()).thenReturn(treeModel);
 when(fixTree.getSelectionModel()).thenReturn(selectionModel);
 when(mockScrollPane.getViewport()).thenReturn(fixTree);
 treeView.initGUI();
 }

 @Test
 public void testTreeViewConstructor() {
 assertEquals("Unexpected library passed to TreeView", library, treeView.getLibrary());
 }

 @Test
 public void testInitGUI() {
 assertNotNull("TreeView JPanel should not be null", treeView.getComponentCount() > 0);
 verify(library, times(1)).getDictionaries();
 }

 @Test
 public void testLoadFIXTree() {
 final List<DictionaryInfo> dictionaries = Arrays.asList(new DictionaryInfo("Version 1"), new DictionaryInfo("Version 2"));
 when(library.getDictionaries()).thenReturn(dictionaries.toArray(new DictionaryInfo[0]));
 final FixInfoTreeNode root = treeView.loadFIXTree();
 assertEquals("Unexpected number of children for root node", 2, root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = mock(DictionaryInfo.class);
 when(dictionary.getMessages()).thenReturn(new MessageInfo[0]);
 when(library.getDictionaries()).thenReturn(new DictionaryInfo[]{dictionary});
 final FixInfoTreeNode versionNode = treeView.loadFIXTree().getChildAt(0);
 treeView.loadMessages(versionNode, dictionary);
 assertFalse("Unexpected number of children for version node", versionNode.getChildCount() > 0);
 }

 @Test
 public void testLoadItems() {
 final CompositeFixInfo compositeFixInfo = mock(CompositeFixInfo.class);
 final MessageInfo messageInfo = mock(MessageInfo.class);
 final FieldInfo fieldInfo = mock(FieldInfo.class);
 when(compositeFixInfo.getItems()).thenReturn(new FixInfo[0]);
 when(messageInfo.getItems()).thenReturn(new FixInfo[0]);
 when(fieldInfo.getItems()).thenReturn(new FixInfo[0]);
 when(compositeFixInfo.getItems()).thenReturn(new FixInfo[]{messageInfo, fieldInfo});
 final FixInfoTreeNode branchNode = treeView.loadFIXTree().getChildAt(0).getChildAt(0);
 treeView.loadItems(branchNode, compositeFixInfo, 0);
 assertEquals("Unexpected number of children for branch node", 2, branchNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 verify(fixTree, times(getTreeRowCount())).expandRow(anyInt());
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 verify(fixTree, times(getTreeRowCount())).collapseRow(anyInt());
 }

 @Test
 public void testSearch() {
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, "TEST");
 verify(rootNode, times(1)).setHighlighted(false);
 for (int i = 0; i < getTreeRowCount(); i++) {
 final FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRow(i);
 if (node.getUserObject() instanceof FieldInfo && node.getUserObject() != rootNode.getChildAt(0)) {
 verify(node, times(1)).setHighlighted(false);
 }
 if (node.getUserObject() instanceof MessageInfo && node.getUserObject() != rootNode.getChildAt(1)) {
 final String name = ((MessageInfo)node.getUserObject()).getName();
 final String tagNumber = "" + ((FieldInfo)node.getUserObject()).getTagNumber();
 assertTrue("Unexpected match for node " + node, name.matches("TEST") || tagNumber.matches("TEST"));
 verify(node, times(1)).setHighlighted(true);
 }
 }
 }

 @Test
 public void testClear() {
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.clear(rootNode);
 verify(rootNode, times(1)).setHighlighted(false);
 for (int i = 0; i < getTreeRowCount(); i++) {
 final FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRow(i);
 verify(node, times(1)).setHighlighted(false);
 }
 }

 @Test
 public void testValueChanged() {
 final FixInfoTreeNode node = mock(FixInfoTreeNode.class);
 treeView.valueChanged(new TreeSelectionEvent(mockScrollPane, new TreePath(Arrays.asList(node)), TreeSelectionEvent.LEAF_DESELECTED, null));
 verify(LibrarySuite.getInstance().getDetailsPanel(), times(1)).clearDisplay();
 }

 @Test
 public void testActionPerformedSearchButton() {
 treeView.actionPerformed(new ActionEvent(searchButton, ActionEvent.ACTION_PERFORMED, null));
 verify(fixTree, times(1)).expandAll();
 String searchString = "TEST";
 treeView.search(treeView.loadFIXTree().getChildAt(0), searchString);
 verify(searchTextField, times(1)).setText(searchString);
 }

 @Test
 public void testActionPerformedClearButton() {
 treeView.actionPerformed(new ActionEvent(clearButton, ActionEvent.ACTION_PERFORMED, null));
 verify(fixTree, times(1)).collapseAll();
 }

 @Test
 public void testActionPerformedExpandButton() {
 treeView.actionPerformed(new ActionEvent(expandButton, ActionEvent.ACTION_PERFORMED, null));
 verify(fixTree, times(1)).expandAll();
 }

 @Test
 public void testActionPerformedCollapseButton() {
 treeView.actionPerformed(new ActionEvent(collapseButton, ActionEvent.ACTION_PERFORMED, null));
 verify(fixTree, times(1)).collapseAll();
 }

 @Test
 public void testKeyPressedSearchTextField() {
 treeView.searchTextField.setText("TEST");
 treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
 treeView.actionPerformed(new ActionEvent(searchButton, ActionEvent.ACTION_PERFORMED, null));
 treeView.searchTextField.setText("");
 }

 private int getTreeRowCount() {
 return treeView.fixTree.getRowCount();
 }
}