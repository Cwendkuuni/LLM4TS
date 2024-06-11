package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 treeNodeAndValue setupTreeNodeAndValue(FixInfoTreeNode node, Object userObject) {
 FixInfoTreeNode treeNode = node;
 Object value = userObject;
 if (userObject instanceof FixInfo) {
 FixInfo fixInfo = (FixInfo)userObject;
 if (fixInfo instanceof MessageInfo) {
 value = ((MessageInfo)fixInfo).getName();
 treeNode = new FixInfoTreeNode(value);
 } else if (fixInfo instanceof ComponentInfo) {
 value = ((ComponentInfo)fixInfo).getName();
 treeNode = new FixInfoTreeNode(value);
 } else if (fixInfo instanceof FieldInfo) {
 value = ((FieldInfo)fixInfo).getName();
 treeNode = new FixInfoTreeNode(value);
 int tagNumber = ((FieldInfo)fixInfo).getTagNumber();
 treeNode.setUserObject(new Object() {
 @Override
 public String toString() {
 return value + " (" + tagNumber + ")";
 }
 });
 }
 }
 return new Object() {
 @Override
 public FixInfoTreeNode getNode() {
 return treeNode;
 }
 @Override
 public Object getValue() {
 return value;
 }
 };
 }

 @Test
 public void testLoadFIXTree() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 FixInfoTreeNode root = treeView.loadFIXTree();
 assertEquals("Dictionaries", root.getUserObject());
 }

 @Test
 public void testLoadMessages() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 DictionaryInfo dictionary = new DictionaryInfo("test1", "1.0");
 MessageInfo message = new MessageInfo("test1Message", "TEST", "Test Message");
 CompositeFixInfo composite = new CompositeFixInfo();
 composite.add(message);
 dictionary.setMessages(new ArrayList<MessageInfo>() {{
 add(message);
 add(new MessageInfo("test2Message", "TEST2", "Test Message 2"));
 }});
 treeView.loadMessages(root, dictionary);
 int count = 0;
 String name = "";
 FixInfoTreeNode node = root;
 while (node != null) {
 if (node.getUserObject() instanceof FixInfoTreeNode) {
 FixInfoTreeNode fixNode = (FixInfoTreeNode)node.getUserObject();
 if (fixNode.getValue().equals("test1Message (TEST)")) {
 assertEquals("1.0", fixNode.getUserObject().toString());
 name = "test1Message";
 } else if (fixNode.getValue().equals("test2Message (TEST2)")) {
 assertEquals("1.0", fixNode.getParent().getUserObject().toString());
 name = "test2Message";
 }
 ++count;
 }
 node = node.getNextSibling();
 }
 assertEquals(2, count);
 assertEquals(name, "test1Message");
 }

 @Test
 public void testLoadItems() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 CompositeFixInfo composite = new CompositeFixInfo();
 MessageInfo message1 = new MessageInfo("test1Message1", "TEST1", "Test Message 1");
 FieldInfo field1 = new FieldInfo("testField1", "35", "Test Field 1");
 composite.add(message1);
 message1.addItem(field1);
 MessageInfo message2 = new MessageInfo("test1Message2", "TEST1", "Test Message 2");
 FieldInfo field2 = new FieldInfo("testField2", "35", "Test Field 2");
 message2.addItem(field2);
 composite.add(message2);
 FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.loadItems(root, composite, 0);
 int count = 0;
 String name = "";
 FixInfoTreeNode node = root;
 while (node != null) {
 if (node.getUserObject() instanceof FixInfoTreeNode) {
 FixInfoTreeNode fixNode = (FixInfoTreeNode)node.getUserObject();
 if (fixNode.getValue().equals("test1Message1 (TEST1)")) {
 assertEquals("test1", fixNode.getParent().getUserObject().toString());
 name = "test1Message1";
 } else if (fixNode.getValue().equals("test1Message2 (TEST1)")) {
 assertEquals("test1", fixNode.getParent().getParent().getUserObject().toString());
 name = "test1Message2";
 } else if (fixNode.getValue().toString().startsWith("testField1")) {
 assertEquals("test1Message1 (TEST1)", fixNode.getParent().getUserObject().toString());
 name = "testField1";
 } else if (fixNode.getValue().toString().startsWith("testField2")) {
 assertEquals("test1Message2 (TEST1)", fixNode.getParent().getUserObject().toString());
 name = "testField2";
 }
 ++count;
 }
 node = node.getNextSibling();
 }
 assertEquals(4, count);
 assertEquals(name, "testField1");
 }

 @Test
 public void testSearch() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 DictionaryInfo dictionary = new DictionaryInfo("test1", "1.0");
 MessageInfo message1 = new MessageInfo("test1Message1", "TEST1", "Test Message 1");
 FieldInfo field1 = new FieldInfo("testField1", "35", "Test Field 1");
 message1.addItem(field1);
 dictionary.setMessages(new ArrayList<MessageInfo>() {{
 add(message1);
 add(new MessageInfo("test2Message", "TEST2", "Test Message 2"));
 }});
 treeView.loadMessages(treeView.loadFIXTree(), dictionary);
 FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.expandAll();
 treeView.search(root, "test1Message1 (TEST1)");
 FixInfoTreeNode node = (FixInfoTreeNode)root.getFirstChild();
 assertTrue(node.isHighlighted());
 assertEquals("test1Message1 (TEST1)", node.getValue());
 treeView.search(root, "testField1");
 node = (FixInfoTreeNode)node.getNextSibling();
 assertTrue(node.isHighlighted());
 assertEquals("testField1 (35)", node.getValue());
 }

 @Test
 public void testClear() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 DictionaryInfo dictionary = new DictionaryInfo("test1", "1.0");
 MessageInfo message1 = new MessageInfo("test1Message1", "TEST1", "Test Message 1");
 FieldInfo field1 = new FieldInfo("testField1", "35", "Test Field 1");
 message1.addItem(field1);
 dictionary.setMessages(new ArrayList<MessageInfo>() {{
 add(message1);
 add(new MessageInfo("test2Message", "TEST2", "Test Message 2"));
 }});
 treeView.loadMessages(treeView.loadFIXTree(), dictionary);
 FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.expandAll();
 treeView.search(root, "test1Message1 (TEST1)");
 FixInfoTreeNode node1 = (FixInfoTreeNode)root.getFirstChild();
 assertTrue(node1.isHighlighted());
 assertEquals("test1Message1 (TEST1)", node1.getValue());
 treeView.clear(node1);
 assertFalse(node1.isHighlighted());
 treeView.search(root, "test1Message1 (TEST1)");
 node1 = (FixInfoTreeNode)root.getFirstChild();
 assertFalse(node1.isHighlighted());
 }

 @Test
 public void testExpandAll() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 DictionaryInfo dictionary = new DictionaryInfo("test1", "1.0");
 MessageInfo message1 = new MessageInfo("test1Message1", "TEST1", "Test Message 1");
 FieldInfo field1 = new FieldInfo("testField1", "35", "Test Field 1");
 message1.addItem(field1);
 dictionary.setMessages(new ArrayList<MessageInfo>() {{
 add(message1);
 add(new MessageInfo("test2Message", "TEST2", "Test Message 2"));
 }});
 treeView.loadMessages(treeView.loadFIXTree(), dictionary);
 FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.expandAll();
 FixInfoTreeNode node = (FixInfoTreeNode)root.getFirstChild();
 assertTrue(node.isExpanded());
 node = (FixInfoTreeNode)node.getFirstChild();
 assertTrue(node.isExpanded());
 node = (FixInfoTreeNode)node.getFirstChild();
 assertTrue(node.isExpanded());
 }

 @Test
 public void testCollapseAll() {
 Library library = new Library();
 TreeView treeView = new TreeView(library);
 DictionaryInfo dictionary = new DictionaryInfo("test1", "1.0");
 MessageInfo message1 = new MessageInfo("test1Message1", "TEST1", "Test Message 1");
 FieldInfo field1 = new FieldInfo("testField1", "35", "Test Field 1");
 message1.addItem(field1);
 dictionary.setMessages(new ArrayList<MessageInfo>() {{
 add(message1);
 add(new MessageInfo("test2Message", "TEST2", "Test Message 2"));
 }});
 treeView.loadMessages(treeView.loadFIXTree(), dictionary);
 FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.expandAll();
 treeView.collapseAll();
 FixInfoTreeNode node = (FixInfoTreeNode)root.getFirstChild();
 assertFalse(node.isExpanded());
 node = (FixInfoTreeNode)node.getFirstChild();
 assertFalse(node.isExpanded());
 node = (FixInfoTreeNode)node.getFirstChild();
 assertFalse(node.isExpanded());
 }
}