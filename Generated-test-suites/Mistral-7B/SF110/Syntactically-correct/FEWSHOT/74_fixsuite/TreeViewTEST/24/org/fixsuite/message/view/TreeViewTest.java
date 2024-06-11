package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 TreeView treeView;
 Library library;

 @Before
 public void setup() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode root = treeView.loadFIXTree();
 assertNotNull(root);
 assertFalse(root.isLeaf());
 }

 @Test
 public void testLoadMessages() {
 DictionaryInfo dictionary = new DictionaryInfo("MockDictionary", "1.0.0");
 MessageInfo message = new MessageInfo("MockMessage", "1.0.0", "MockName", "MockMessageType");
 dictionary.addMessage(message);
 library.addDictionary(dictionary);

 FixInfoTreeNode versionNode = new FixInfoTreeNode("1.0.0");
 FixInfoTreeNode node = new FixInfoTreeNode(message);

 treeView.loadMessages(versionNode, dictionary);
 assertTrue(versionNode.getChildCount() > 0);
 assertEquals(message, versionNode.getChildAt(0).getUserObject());
 }

 @Test
 public void testLoadItems() {
 MessageInfo message = new MessageInfo("MockMessage", "1.0.0", "MockName", "MockMessageType");
 CompositeFixInfo component = new CompositeFixInfo("MockComponent");
 FieldInfo field = new FieldInfo("MockField", 1, "MockName", "MockName");
 component.addItem(field);
 message.addComponent(component);

 FixInfoTreeNode branchNode = new FixInfoTreeNode("Component");
 FixInfoTreeNode leafNode = new FixInfoTreeNode(message);

 treeView.loadItems(branchNode, message, 0);
 assertTrue(branchNode.getChildCount() > 0);
 assertEquals(message, branchNode.getChildAt(0).getUserObject());

 treeView.loadItems(leafNode, field, 1);
 assertTrue(leafNode.getChildCount() > 0);
 assertEquals(field, leafNode.getChildAt(0).getUserObject());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int rowCount = treeView.fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 assertTrue(treeView.fixTree.isExpanded(i));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int rowCount = treeView.fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 assertFalse(treeView.fixTree.isExpanded(i));
 }
 }

 @Test
 public void testSearch() {
 MessageInfo message = new MessageInfo("MockMessage", "1.0.0", "MockName", "MockMessageType");
 library.addDictionary(new DictionaryInfo("MockDictionary", "1.0.0"));
 library.addMessage(message);

 String searchString = "MockName";
 treeView.search(treeView.fixTree.getModel().getRoot(), searchString);
 int nodeCount = treeView.fixTree.getModel().getRoot().getChildCount();
 for (int i = 0; i < nodeCount; i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().getChildAt(i);
 if (node.getUserObject() instanceof MessageInfo) {
 MessageInfo nodeMessage = (MessageInfo)node.getUserObject();
 assertEquals(message, nodeMessage);
 assertTrue(node.isHighlighted());
 }
 }
 }

 @Test
 public void testClear() {
 MessageInfo message = new MessageInfo("MockMessage", "1.0.0", "MockName", "MockMessageType");
 library.addDictionary(new DictionaryInfo("MockDictionary", "1.0.0"));
 library.addMessage(message);
 treeView.search(treeView.fixTree.getModel().getRoot(), "MockName");
 int nodeCount = treeView.fixTree.getModel().getRoot().getChildCount();
 for (int i = 0; i < nodeCount; i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().getChildAt(i);
 if (node.getUserObject() instanceof MessageInfo) {
 assertFalse(node.isHighlighted());
 }
 }
 }
}