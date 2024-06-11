package org.fixsuite.message.view;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TreeViewTest {

 private Library libraryMock;
 private TreeView treeView;

 @Before
 public void setUp() {
 libraryMock = mock(Library.class);
 treeView = new TreeView(libraryMock);
 }

 @Test
 public void testInitGUI() {
 // Check if components are added to the JPanel
 assertEquals(1, treeView.getComponentCount());
 JComponent centerComponent = (JComponent) treeView.getComponent(0);
 assertEquals(BorderLayout.CENTER, centerComponent.getLayout().getClass());
 JScrollPane scrollPane = (JScrollPane) centerComponent;
 JTree fixTree = scrollPane.getViewport().getComponent(0);
 assertNotNull(fixTree);
 JToolBar toolbar = (JToolBar) treeView.getComponent(1);
 assertNotNull(toolbar);
 JButton searchButton = (JButton) toolbar.getComponent(2);
 assertNotNull(searchButton);
 JTextField searchTextField = (JTextField) toolbar.getComponent(3);
 assertNotNull(searchTextField);
 JLabel searchLabel = (JLabel) toolbar.getComponent(0);
 assertNotNull(searchLabel);
 JButton clearButton = (JButton) toolbar.getComponent(4);
 assertNotNull(clearButton);
 JButton expandButton = (JButton) toolbar.getComponent(5);
 assertNotNull(expandButton);
 JButton collapseButton = (JButton) toolbar.getComponent(6);
 assertNotNull(collapseButton);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode root = treeView.loadFIXTree();
 assertNotNull(root);
 assertEquals("Dictionaries", root.getUserObject().toString());
 verify(libraryMock).getDictionaries();
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionaryMock = mock(DictionaryInfo.class);
 FixInfoTreeNode versionNode = mock(FixInfoTreeNode.class);
 when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(dictionaryMock));
 treeView.loadMessages(versionNode, dictionaryMock);
 verify(dictionaryMock).getMessages();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadMessages_NullDictionary() {
 treeView.loadMessages(mock(FixInfoTreeNode.class), null);
 }

 @Test
 public void testLoadItems() {
 final CompositeFixInfo compositeFixInfoMock = mock(CompositeFixInfo.class);
 FixInfoTreeNode branchNode = mock(FixInfoTreeNode.class);
 when(compositeFixInfoMock.getItems()).thenReturn(Collections.singletonList(mock(FixInfo.class)));
 treeView.loadItems(branchNode, compositeFixInfoMock, 0);
 verify(branchNode).add(any(FixInfoTreeNode.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadItems_NullInfo() {
 treeView.loadItems(mock(FixInfoTreeNode.class), null, 0);
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 verify(treeView.fixTree).expandRow(eq(0));
 for (int i = 1; i < treeView.fixTree.getRowCount(); ++i) {
 verify(treeView.fixTree).expandRow(eq(i));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 for (int i = treeView.fixTree.getRowCount() - 1; i >= 0; --i) {
 verify(treeView.fixTree).collapseRow(eq(i));
 }
 }

 @Test
 public void testSearch() {
 final MessageInfo messageInfoMock = mock(MessageInfo.class);
 final FieldInfo fieldInfoMock = mock(FieldInfo.class);
 FixInfoTreeNode root = mock(FixInfoTreeNode.class);
 FixInfoTreeNode messageNode = mock(FixInfoTreeNode.class);
 FixInfoTreeNode fieldNode = mock(FixInfoTreeNode.class);
 when(root.getUserObject()).thenReturn("Dictionaries");
 when(messageNode.getUserObject()).thenReturn(messageInfoMock);
 when(fieldNode.getUserObject()).thenReturn(fieldInfoMock);
 when(messageNode.getParent()).thenReturn(root);
 when(fieldNode.getParent()).thenReturn(messageNode);
 treeView.search(messageNode, "MessageType");
 verify(messageNode).setHighlighted(true);
 treeView.search(fieldNode, "FieldName");
 verify(fieldNode).setHighlighted(true);
 }

 @Test
 public void testSearch_EmptySearchString() {
 final MessageInfo messageInfoMock = mock(MessageInfo.class);
 final FieldInfo fieldInfoMock = mock(FieldInfo.class);
 FixInfoTreeNode root = mock(FixInfoTreeNode.class);
 FixInfoTreeNode messageNode = mock(FixInfoTreeNode.class);
 FixInfoTreeNode fieldNode = mock(FixInfoTreeNode.class);
 when(root.getUserObject()).thenReturn("Dictionaries");
 when(messageNode.getUserObject()).thenReturn(messageInfoMock);
 when(fieldNode.getUserObject()).thenReturn(fieldInfoMock);
 when(messageNode.getParent()).thenReturn(root);
 when(fieldNode.getParent()).thenReturn(messageNode);
 treeView.search(messageNode, "");
 verify(messageNode, never()).setHighlighted(true);
 verify(fieldNode, never()).setHighlighted(true);
 }

 @Test
 public void testClear() {
 final FixInfoTreeNode node = mock(FixInfoTreeNode.class);
 treeView.clear(node);
 verify(node).setHighlighted(false);
 }

 @Test
 public void testClear_NullNode() {
 treeView.clear(null);
 verify(treeView.fixTree, never()).repaint();
 }

}