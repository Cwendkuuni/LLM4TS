package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.MessageType;
import org.fixsuite.message.view.FixInfoCellRenderer;
import org.fixsuite.message.view.FixInfoTreeNode;
import org.fixsuite.message.view.Library;
import org.fixsuite.message.view.LibrarySuite;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private Library library;
 private TreeView treeView;
 private TreeModel treeModel;
 private TreeCellRenderer cellRenderer;

 @Before
 public void setup() {
 library = mock(Library.class);
 treeView = new TreeView(library);
 treeModel = treeView.getModel();
 cellRenderer = treeView.getCellRenderer();
 }

 @Test
 public void testLoadFIXTree() {
 DictionaryInfo dictionary1 = new DictionaryInfo("dictionary1", Arrays.asList(new MessageInfo("message1", MessageType.HEARTBEAT), new MessageInfo("message2", MessageType.LOGON)));
 DictionaryInfo dictionary2 = new DictionaryInfo("dictionary2", Arrays.asList(new MessageInfo("message3", MessageType.LOGOUT), new MessageInfo("message4", MessageType.LOGOUT_ACK)));
 Library librarySpy = mock(Library.class);
 when(librarySpy.getDictionaries()).thenReturn(Arrays.asList(dictionary1, dictionary2));
 treeView = new TreeView(librarySpy);

 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
 assertEquals("Dictionaries", rootNode.getUserObject());
 assertEquals(2, rootNode.getChildCount());

 assertNotNull(rootNode.getChildAt(0));
 assertEquals("dictionary1", ((FixInfoTreeNode) rootNode.getChildAt(0)).getUserObject().toString());
 assertEquals(2, ((FixInfoTreeNode) rootNode.getChildAt(0)).getChildCount());

 assertNotNull(rootNode.getChildAt(1));
 assertEquals("dictionary2", ((FixInfoTreeNode) rootNode.getChildAt(1)).getUserObject().toString());
 assertEquals(2, ((FixInfoTreeNode) rootNode.getChildAt(1)).getChildCount());
 }

 @Test
 public void testLoadMessages() {
 MessageInfo message1 = new MessageInfo("message1", MessageType.HEARTBEAT, new ArrayList<>());
 MessageInfo message2 = new MessageInfo("message2", MessageType.HEARTBEAT, new ArrayList<>());
 ComponentInfo component1 = new ComponentInfo("component1", "Component1");
 FieldInfo field1 = new FieldInfo(1, "field1", "Field1");
 FieldInfo field2 = new FieldInfo(2, "field2", "Field2");
 List<FieldInfo> fieldList = new ArrayList<>();
 fieldList.add(field1);
 fieldList.add(field2);
 message1.setFields(fieldList);
 message2.setFields(fieldList);

 DictionaryInfo dictionary = new DictionaryInfo("dictionary", Arrays.asList(message1, message2));
 Library librarySpy = mock(Library.class);
 when(librarySpy.getDictionaries()).thenReturn(Arrays.asList(dictionary));
 treeView = new TreeView(librarySpy);

 FixInfoTreeNode versionNode = (FixInfoTreeNode) treeModel.getChildAt(0, 0);
 assertEquals("dictionary", versionNode.getUserObject().toString());
 assertEquals(2, versionNode.getChildCount());

 assertNotNull(versionNode.getChildAt(0));
 assertEquals("message1", ((FixInfoTreeNode) versionNode.getChildAt(0)).getUserObject().toString());
 assertNotNull(((FixInfoTreeNode) versionNode.getChildAt(0)).getChildCount());
 assertEquals(0, ((FixInfoTreeNode) versionNode.getChildAt(0)).getChildCount());

 assertNotNull(versionNode.getChildAt(1));
 assertEquals("message2", ((FixInfoTreeNode) versionNode.getChildAt(1)).getUserObject().toString());
 assertNotNull(((FixInfoTreeNode) versionNode.getChildAt(1)).getChildCount());
 assertEquals(0, ((FixInfoTreeNode) versionNode.getChildAt(1)).getChildCount());
 }

 @Test
 public void testLoadItems() {
 MessageInfo message = new MessageInfo("message", MessageType.HEARTBEAT, new ArrayList<>());
 FieldInfo field1 = new FieldInfo(1, "field1", "Field1");
 FieldInfo field2 = new FieldInfo(2, "field2", "Field2");
 List<FieldInfo> fieldList = new ArrayList<>();
 fieldList.add(field1);
 fieldList.add(field2);
 message.setFields(fieldList);

 ComponentInfo component = new ComponentInfo("component", "Component");
 List<FixInfo> itemList = new ArrayList<>();
 itemList.add(message);
 itemList.add(component);

 FixInfoTreeNode branchNode = new FixInfoTreeNode("branchNode");
 branchNode.setUserObject("branchNode");
 assertEquals(0, branchNode.getChildCount());

 treeView.loadItems(branchNode, message, 0);
 assertEquals(2, branchNode.getChildCount());
 assertNotNull(branchNode.getChildAt(0));
 assertEquals("message", ((FixInfoTreeNode) branchNode.getChildAt(0)).getUserObject().toString());
 assertNotNull(((FixInfoTreeNode) branchNode.getChildAt(0)).getChildCount());
 assertEquals(0, ((FixInfoTreeNode) branchNode.getChildAt(0)).getChildCount());

 assertNotNull(branchNode.getChildAt(1));
 assertEquals("component", ((FixInfoTreeNode) branchNode.getChildAt(1)).getUserObject().toString());
 assertNotNull(((FixInfoTreeNode) branchNode.getChildAt(1)).getChildCount());
 assertEquals(0, ((FixInfoTreeNode) branchNode.getChildAt(1)).getChildCount());
 }

 @Test
 public void testExpandAll() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
 treeView.expandAll();
 assertEquals(7, rootNode.getChildCount());
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(i);
 assertTrue(node.isExpanded());
 }
 }

 @Test
 public void testCollapseAll() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
 treeView.collapseAll();
 assertEquals(2, rootNode.getChildCount());
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(i);
 assertFalse(node.isExpanded());
 }
 }

 @Test
 public void testSearch() {
 MessageInfo message1 = new MessageInfo("message1", MessageType.HEARTBEAT, new ArrayList<>());
 ComponentInfo component1 = new ComponentInfo("component1", "Component1");
 FieldInfo field1 = new FieldInfo(1, "field1", "Field1");
 FieldInfo field2 = new FieldInfo(2, "field2", "Field2");
 List<FieldInfo> fieldList = new ArrayList<>();
 fieldList.add(field1);
 fieldList.add(field2);
 message1.setFields(fieldList);

 DictionaryInfo dictionary = new DictionaryInfo("dictionary", Arrays.asList(message1, message2));
 Library librarySpy = mock(Library.class);
 when(librarySpy.getDictionaries()).thenReturn(Arrays.asList(dictionary));
 treeView = new TreeView(librarySpy);

 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
 FixInfoTreeNode messageNode1 = (FixInfoTreeNode) treeModel.getChildAt(0, 0);
 FixInfoTreeNode messageNode2 = (FixInfoTreeNode) treeModel.getChildAt(0, 1);
 FixInfoTreeNode componentNode = (FixInfoTreeNode) treeModel.getChildAt(1, 0);

 treeView.search(rootNode, "message1");
 assertEquals(1, rootNode.getChildCount());
 assertTrue(messageNode1.isHighlighted());
 assertTrue(messageNode2.isExpanded());
 assertFalse(componentNode.isHighlighted());
 }

 @Test
 public void testClear() {
 MessageInfo message1 = new MessageInfo("message1", MessageType.HEARTBEAT, new ArrayList<>());
 ComponentInfo component1 = new ComponentInfo("component1", "Component1");
 FieldInfo field1 = new FieldInfo(1, "field1", "Field1");
 FieldInfo field2 = new FieldInfo(2, "field2", "Field2");
 List<FieldInfo> fieldList = new ArrayList<>();
 fieldList.add(field1);
 fieldList.add(field2);
 message1.setFields(fieldList);

 DictionaryInfo dictionary = new DictionaryInfo("dictionary", Arrays.asList(message1, message2));
 Library librarySpy = mock(Library.class);
 when(librarySpy.getDictionaries()).thenReturn(Arrays.asList(dictionary));
 treeView = new TreeView(librarySpy);

 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeModel.getRoot();
 FixInfoTreeNode messageNode1 = (FixInfoTreeNode) treeModel.getChildAt(0, 0);
 FixInfoTreeNode messageNode2 = (FixInfoTreeNode) treeModel.getChildAt(0, 1);
 FixInfoTreeNode componentNode = (FixInfoTreeNode) treeModel.getChildAt(1, 0);

 treeView.search(rootNode, "message1");
 assertEquals(1, rootNode.getChildCount());
 assertTrue(messageNode1.isHighlighted());
 assertTrue(messageNode2.isExpanded());
 assertFalse(componentNode.isHighlighted());

 treeView.clear(rootNode);
 assertEquals(2, rootNode.getChildCount());
 assertFalse(messageNode1.isHighlighted());
 assertFalse(messageNode2.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 }
}