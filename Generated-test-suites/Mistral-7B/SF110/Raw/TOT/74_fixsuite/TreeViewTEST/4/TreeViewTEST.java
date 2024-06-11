package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TreeViewTest {

 private Library library;
 private TreeView treeView;
 private FixInfoTreeNode rootNode;
 private FixInfoTreeNode messageNode;
 private FixInfoTreeNode componentNode;
 private FixInfoTreeNode fieldNode;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 library = mock(Library.class);
 treeView = new TreeView(library);
 rootNode = new FixInfoTreeNode("Dictionaries");
 messageNode = new FixInfoTreeNode(new MessageInfo("TestMessage", "TestMessageType"));
 componentNode = new FixInfoTreeNode(new ComponentInfo("TestComponent", "TestComponentName"));
 fieldNode = new FixInfoTreeNode(new FieldInfo("TestField", 1, "TestFieldName"));

 rootNode.add(messageNode);
 messageNode.add(componentNode);
 componentNode.add(fieldNode);

 DefaultTreeModel model = new DefaultTreeModel(rootNode);
 treeView.fixTree.setModel(model);
 }

 @Test
 public void testLoadFIXTree() {
 assertNotNull(treeView.fixTree);
 verify(library, times(1)).getDictionaries();
 }

 @Test
 public void testLoadMessages() {
 List<MessageInfo> messages = new ArrayList<>();
 messages.add(new MessageInfo("TestMessage1", "TestMessageType1"));
 messages.add(new MessageInfo("TestMessage2", "TestMessageType2"));

 when(library.getDictionaries().get(0).getMessages()).thenReturn(messages);

 assertEquals(2, treeView.loadFIXTree().getChildCount());
 }

 @Test
 public void testLoadItems() {
 List<FixInfo> items = new ArrayList<>();
 items.add(new MessageInfo("TestMessage", "TestMessageType"));
 items.add(new ComponentInfo("TestComponent", "TestComponentName"));
 items.add(new FieldInfo("TestField1", 1, "TestFieldName1"));
 items.add(new FieldInfo("TestField2", 2, "TestFieldName2"));

 when(library.getDictionaries().get(0).getMessages().get(0).getItems()).thenReturn(items);

 assertEquals(4, loadItems(messageNode, null, 0).getChildCount());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertTrue(treeView.fixTree.isExpanded(i));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertFalse(treeView.fixTree.isExpanded(i));
 }
 }

 @Test
 public void testSearch() {
 when(library.getDictionaries().get(0).getMessages().get(0).getName()).thenReturn("TestMessage");
 when(library.getDictionaries().get(0).getMessages().get(0).getMessageType()).thenReturn("TestMessageType");
 when(library.getDictionaries().get(0).getMessages().get(0).getItems().get(0).getName()).thenReturn("TestFieldName");
 when(library.getDictionaries().get(0).getMessages().get(0).getItems().get(0).getTagNumber()).thenReturn(1);

 treeView.searchTextField.setText("TestMessageType|TestMessage|TestFieldName|1");
 treeView.actionPerformed(mock(ActionEvent.class));

 assertTrue(messageNode.isHighlighted());
 assertTrue(componentNode.isHighlighted());
 assertTrue(fieldNode.isHighlighted());
 }

 @Test
 public void testSearchEmptyString() {
 treeView.searchTextField.setText("");
 treeView.actionPerformed(mock(ActionEvent.class));

 assertFalse(messageNode.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 assertFalse(fieldNode.isHighlighted());
 }

 @Test
 public void testSearchInvalidRegex() {
 treeView.searchTextField.setText("TestMessageTypeTestMessage|TestFieldName|1");
 treeView.actionPerformed(mock(ActionEvent.class));

 assertFalse(messageNode.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 assertFalse(fieldNode.isHighlighted());
 }

 @Test
 public void testClear() {
 treeView.searchTextField.setText("TestMessageType|TestMessage|TestFieldName|1");
 treeView.actionPerformed(mock(ActionEvent.class));
 treeView.clearButton.doClick();

 assertFalse(messageNode.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 assertFalse(fieldNode.isHighlighted());
 }

 private FixInfoTreeNode loadItems(FixInfoTreeNode node, CompositeFixInfo parent, int level) {
 FixInfoTreeNode newNode;
 if (parent != null) {
 newNode = new FixInfoTreeNode(parent);
 }
 else {
 newNode = node;
 }
 for (FixInfo info : parent.getItems()) {
 if (info instanceof CompositeFixInfo && level < 5) {
 newNode.add(loadItems(new FixInfoTreeNode(info), (CompositeFixInfo)info, level + 1));
 }
 else {
 newNode.add(new FixInfoTreeNode(info));
 }
 }
 return newNode;
 }
}