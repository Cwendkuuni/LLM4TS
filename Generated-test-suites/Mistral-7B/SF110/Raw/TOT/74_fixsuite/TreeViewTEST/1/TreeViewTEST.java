package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreeCellRenderer;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TreeViewTest {

 private TreeView treeView;
 private Library libraryMock;
 private FixInfoTreeNode rootNode;
 private FixInfoTreeNode messageNode;
 private FixInfoTreeNode componentNode;
 private FixInfoTreeNode fieldNode;

 @Before
 public void setUp() {
 libraryMock = mock(Library.class);
 treeView = new TreeView(libraryMock);
 rootNode = new FixInfoTreeNode("Dictionaries");
 messageNode = new FixInfoTreeNode("1.0");
 componentNode = new FixInfoTreeNode("Component");
 fieldNode = new FixInfoTreeNode(new FieldInfo("TagNumber", "FieldName", 1));

 messageNode.add(fieldNode);
 rootNode.add(messageNode);

 when(libraryMock.getDictionaries()).thenReturn(Arrays.asList(new DictionaryInfo("DictionaryName", Arrays.asList(new MessageInfo("MessageName", "MessageType")))));
 }

 @Test
 public void testInitGUI() {
 assertEquals("Unexpected number of components", 7, treeView.getComponentCount());
 }

 @Test
 public void testLoadFIXTree() {
 assertEquals("Unexpected number of nodes in root", 1, rootNode.getChildCount());
 assertEquals("Unexpected root node user object", "Dictionaries", rootNode.getUserObject());
 }

 @Test
 public void testLoadMessages() {
 assertEquals("Unexpected number of children for message node", 1, messageNode.getChildCount());
 assertEquals("Unexpected message node user object", "1.0", messageNode.getUserObject());
 }

 @Test
 public void testLoadItems() {
 assertEquals("Unexpected number of children for component node", 1, componentNode.getChildCount());
 assertEquals("Unexpected component node user object", "Component", componentNode.getUserObject());
 }

 @Test
 public void testSearch() {
 treeView.search(rootNode, ".*Component.*");
 verify(libraryMock).getDictionaries();
 verifyNoMoreInteractions(libraryMock);

 TreePath expectedSearchPath = new TreePath(new Object[] { rootNode, messageNode, componentNode, fieldNode });
 assertEquals("Search failed to highlight correct node", expectedSearchPath, treeView.fixTree.getSelectionPath());
 }

 @Test
 public void testClear() {
 treeView.search(rootNode, ".*Component.*");
 treeView.clear((FixInfoTreeNode)treeView.fixTree.getModel().getRoot());
 assertEquals("Search results not cleared", Collections.emptyList(), treeView.fixTree.getSelectionPaths());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 verify(treeView.fixTree, times(this.fixTree.getRowCount())).expandRow(anyInt());
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 verify(treeView.fixTree, times(this.fixTree.getRowCount())).collapseRow(anyInt());
 }

 @Test
 public void testActionPerformedSearchButton() {
 treeView.actionPerformed(mock(ActionEvent.class));
 verify(treeView).search((FixInfoTreeNode)treeView.fixTree.getModel().getRoot(), treeView.searchTextField.getText().trim());
 }

 @Test
 public void testActionPerformedClearButton() {
 treeView.actionPerformed(mock(ActionEvent.class));
 treeView.search(rootNode, ".*Component.*");
 treeView.clear((FixInfoTreeNode)treeView.fixTree.getModel().getRoot());
 verify(treeView.searchTextField, times(1)).setText("");
 }

 @Test
 public void testActionPerformedCollapseButton() {
 treeView.actionPerformed(mock(ActionEvent.class));
 treeView.collapseAll();
 verify(treeView.fixTree, times(this.fixTree.getRowCount())).collapseRow(anyInt());
 }

 @Test
 public void testActionPerformedExpandButton() {
 treeView.actionPerformed(mock(ActionEvent.class));
 treeView.expandAll();
 verify(treeView.fixTree, times(this.fixTree.getRowCount())).expandRow(anyInt());
 }

 @Test
 public void testKeyPressedSearchTextField() {
 treeView.keyPressed(mock(KeyEvent.class));
 verify(treeView.searchButton).doClick();
 }

 @Test
 public void testKeyReleasedSearchTextField() {
 treeView.keyReleased(mock(KeyEvent.class));
 }

 @Test
 public void testKeyTypedSearchTextField() {
 treeView.keyTyped(mock(KeyEvent.class));
 }

 @Test
 public void testValueChanged() {
 treeView.valueChanged(mock(TreeSelectionEvent.class));
 LibrarySuite librarySuite = LibrarySuite.getInstance();
 assertEquals("Unexpected details panel display", "Component", librarySuite.getDetailsPanel().getSelectedComponentName());
 }

 @Test
 public void testActionPerformedWhenNoSelection() {
 treeView.actionPerformed(mock(ActionEvent.class));
 verify(librarySuite, never()).getDetailsPanel().displayDetails(any(FixInfo.class), any(ComponentInfo.class));
 }

 @Test
 public void testActionPerformedWithNoDetailsPanel() {
 LibrarySuite librarySuite = mock(LibrarySuite.class);
 treeView = new TreeView(librarySuite);
 treeView.actionPerformed(mock(ActionEvent.class));
 verify(librarySuite, never()).getDetailsPanel().displayDetails(any(FixInfo.class), any(ComponentInfo.class));
 }

 @Test
 public void testValueChangedWithNoSelection() {
 treeView.valueChanged(mock(TreeSelectionEvent.class));
 verify(librarySuite, never()).getDetailsPanel().displayDetails(any(FixInfo.class), any(ComponentInfo.class));
 }
}