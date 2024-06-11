package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.view.FixInfoCellRenderer;
import org.fixsuite.message.view.FixInfoTreeNode;
import org.fixsuite.message.view.Library;
import org.fixsuite.message.view.LibrarySuite;
import org.fixsuite.message.view.TreeView;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testLoadFIXTree() {
 DictionaryInfo dictionary1 = new DictionaryInfo("dictionary1", "1.0");
 DictionaryInfo dictionary2 = new DictionaryInfo("dictionary2", "2.0");
 library.setDictionaries(Arrays.asList(dictionary1, dictionary2));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 assertEquals(2, rootNode.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 DictionaryInfo dictionary = new DictionaryInfo("dictionary", "1.0");
 MessageInfo message1 = new MessageInfo("message1", "messageType1", "Name1");
 MessageInfo message2 = new MessageInfo("message2", "messageType2", "Name2");
 library.setDictionaries(Arrays.asList(dictionary));
 dictionary.setMessages(Arrays.asList(message1, message2));

 FixInfoTreeNode versionNode = new FixInfoTreeNode("version");
 treeView.loadMessages(versionNode, dictionary);
 assertEquals(2, versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name");
 ComponentInfo component1 = new ComponentInfo("component1", "ComponentName1");
 ComponentInfo component2 = new ComponentInfo("component2", "ComponentName2");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 FieldInfo field2 = new FieldInfo("field2", "FieldName2", 2, "FieldName2");
 message.setItems(Arrays.asList(component1, component2, field1, field2));

 FixInfoTreeNode branchNode = new FixInfoTreeNode("branch");
 treeView.loadItems(branchNode, message, 0);
 assertEquals(4, branchNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.expandAll();
 assertEquals(rootNode.getRowCount(), treeView.fixTree.getRowCount());
 }

 @Test
 public void testCollapseAll() {
 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.collapseAll();
 assertEquals(0, treeView.fixTree.getRowCount());
 }

 @Test
 public void testSearch() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 MessageInfo messageWithSameName = new MessageInfo("message", "messageType", "Name");
 library.setDictionaries(Arrays.asList(new DictionaryInfo("dictionary", "1.0")));
 dictionary.setMessages(Arrays.asList(message, messageWithSameName));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, "Name");
 assertEquals(2, rootNode.getRowCount());
 treeView.search(rootNode, "FieldName1");
 assertEquals(1, rootNode.getRowCount());
 }

 @Test
 public void testClear() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 MessageInfo messageWithSameName = new MessageInfo("message", "messageType", "Name");
 library.setDictionaries(Arrays.asList(new DictionaryInfo("dictionary", "1.0")));
 dictionary.setMessages(Arrays.asList(message, messageWithSameName));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, "Name");
 assertEquals(2, rootNode.getRowCount());
 treeView.clear(rootNode);
 assertEquals(0, rootNode.getRowCount());
 }

 @Test
 public void testSearchWithRegex() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name1|Name2");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 library.setDictionaries(Arrays.asList(new DictionaryInfo("dictionary", "1.0")));
 dictionary.setMessages(Arrays.asList(message));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, "Name1|Name2");
 assertEquals(1, rootNode.getRowCount());
 }

 @Test
 public void testSearchWithEmptyString() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 library.setDictionaries(Arrays.asList(new DictionaryInfo("dictionary", "1.0")));
 dictionary.setMessages(Arrays.asList(message));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, "");
 assertEquals(2, rootNode.getRowCount());
 }

 @Test
 public void testSearchWithNull() {
 MessageInfo message = new MessageInfo("message", "messageType", "Name");
 FieldInfo field1 = new FieldInfo("field1", "FieldName1", 1, "FieldName1");
 library.setDictionaries(Arrays.asList(new DictionaryInfo("dictionary", "1.0")));
 dictionary.setMessages(Arrays.asList(message));

 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 try {
 treeView.search(rootNode, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testKeyPressed() {
 TreeCellRenderer renderer = mock(TreeCellRenderer.class);
 when(renderer.getTreeCellRendererComponent(any(JTree.class), anyObject(), anyBoolean(), anyBoolean(), anyBoolean(), anyInt(), anyBoolean())).thenReturn(new JLabel(""));
 treeView.fixTree.setCellRenderer(renderer);

 KeyEvent keyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, false);
 treeView.searchTextField.dispatchEvent(keyEvent);
 verify(renderer).getTreeCellRendererComponent(treeView.fixTree, anyObject(), true, true, false, anyInt(), false);
 }

 @After
 public void tearDown() {
 treeView = null;
 library = null;
 }

}