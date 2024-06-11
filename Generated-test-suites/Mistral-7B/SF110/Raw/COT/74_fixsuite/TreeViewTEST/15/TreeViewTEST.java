package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreePath;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setup() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testConstructor() {
 assertNotNull(treeView);
 }

 @Test
 public void testInitGUI() {
 // Check if components are initialized
 assertEquals(300, treeView.getPreferredSize().width);
 assertEquals(400, treeView.getPreferredSize().height);
 assertNotNull(treeView.mainScrollPane);
 assertNotNull(treeView.fixTree);
 assertNotNull(treeView.searchLabel);
 assertNotNull(treeView.searchTextField);
 assertNotNull(treeView.searchButton);
 assertNotNull(treeView.clearButton);
 assertNotNull(treeView.expandButton);
 assertNotNull(treeView.collapseButton);
 }

 @Test
 public void testLoadFIXTree() {
 // Check if loadFIXTree method is called in constructor
 assertEquals(1, treeView.fixTree.getRowCount());
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = new DictionaryInfo();
 dictionary.setName("Test Dictionary");
 library.addDictionary(dictionary);
 final MessageInfo message = new MessageInfo();
 message.setName("Test Message");
 message.setMessageType("Test Type");
 final CompositeFixInfo composite = new CompositeFixInfo();
 composite.setName("Test Composite");
 composite.getItems().add(message);
 dictionary.setMessages(Collections.singletonList(composite));

 // Test loading messages for one dictionary
 assertEquals(2, treeView.fixTree.getRowCount());
 final FixInfoTreeNode rootNode = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot();
 final FixInfoTreeNode dictionaryNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(rootNode, 0);
 final FixInfoTreeNode compositeNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(dictionaryNode, 0);
 final FixInfoTreeNode messageNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(compositeNode, 0);
 assertEquals("Test Message", messageNode.getUserObject().toString());

 // Test loading messages for multiple dictionaries
 final DictionaryInfo dictionary2 = new DictionaryInfo();
 dictionary2.setName("Test Dictionary 2");
 library.addDictionary(dictionary2);
 final MessageInfo message2 = new MessageInfo();
 message2.setName("Test Message 2");
 message2.setMessageType("Test Type 2");
 final CompositeFixInfo composite2 = new CompositeFixInfo();
 composite2.setName("Test Composite 2");
 composite2.getItems().add(message2);
 dictionary2.setMessages(Collections.singletonList(composite2));
 assertEquals(4, treeView.fixTree.getRowCount());
 final FixInfoTreeNode dictionary2Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(rootNode, 1);
 final FixInfoTreeNode composite2Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(dictionary2Node, 0);
 final FixInfoTreeNode message2Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(composite2Node, 0);
 assertEquals("Test Message 2", message2Node.getUserObject().toString());
 }

 @Test
 public void testLoadItems() {
 final DictionaryInfo dictionary = new DictionaryInfo();
 library.addDictionary(dictionary);
 final MessageInfo message = new MessageInfo();
 message.setName("Test Message");
 message.setMessageType("Test Type");
 final List<FixInfo> items = Arrays.asList(new FieldInfo(1, "Test Field"), new ComponentInfo("Test Component"), new FieldInfo(2, "Test Field 2"), new FieldInfo(3, "Test Field 3"));
 message.setItems(items);
 dictionary.setMessages(Collections.singletonList(message));

 // Test loading items for a single message
 assertEquals(5, treeView.fixTree.getRowCount());
 final FixInfoTreeNode messageNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 1);
 final FixInfoTreeNode field1Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(messageNode, 0);
 assertEquals("Test Field", field1Node.getUserObject().toString());
 final FixInfoTreeNode componentNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(messageNode, 1);
 assertEquals("Test Component", componentNode.getUserObject().toString());
 final FixInfoTreeNode field2Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(componentNode, 0);
 assertEquals("Test Field 2", field2Node.getUserObject().toString());
 final FixInfoTreeNode field3Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(componentNode, 1);
 assertEquals("Test Field 3", field3Node.getUserObject().toString());

 // Test loading items for multiple messages
 final MessageInfo message2 = new MessageInfo();
 message2.setName("Test Message 2");
 message2.setMessageType("Test Type 2");
 final List<FixInfo> items2 = Arrays.asList(new FieldInfo(1, "Test Field 2"), new FieldInfo(2, "Test Field 3"), new FieldInfo(3, "Test Field 4"));
 message2.setItems(items2);
 dictionary.setMessages(Arrays.asList(message, message2));
 assertEquals(8, treeView.fixTree.getRowCount());
 final FixInfoTreeNode message2Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(messageNode, 1);
 final FixInfoTreeNode field21Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(message2Node, 0);
 assertEquals("Test Field 2", field21Node.getUserObject().toString());
 final FixInfoTreeNode field22Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(message2Node, 1);
 assertEquals("Test Field 3", field22Node.getUserObject().toString());
 final FixInfoTreeNode field23Node = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(message2Node, 2);
 assertEquals("Test Field 4", field23Node.getUserObject().toString());
 }

 @Test
 public void testExpandAll() {
 assertEquals(5, treeView.fixTree.getRowCount());
 treeView.expandAll();
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 assertTrue(treeView.fixTree.isExpanded(new TreePath(new Object[]{treeView.fixTree.getModel().getRoot(), treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 0), treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 0), 0)})));
 }
 }

 @Test
 public void testCollapseAll() {
 assertEquals(5, treeView.fixTree.getRowCount());
 treeView.expandAll();
 treeView.collapseAll();
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 assertFalse(treeView.fixTree.isExpanded(new TreePath(new Object[]{treeView.fixTree.getModel().getRoot(), treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 0), treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 0), 0)})));
 }
 }

 @Test
 public void testSearch() {
 // Test searching for a message name
 treeView.expandAll();
 treeView.searchTextField.setText("Test Message");
 treeView.searchButton.doClick();
 final TreePath selectedPath = treeView.fixTree.getLastSelectedPathComponent();
 final FixInfoTreeNode expectedNode = (FixInfoTreeNode)treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getChild(treeView.fixTree.getModel().getRoot(), 0), 1);
 assertEquals(expectedNode, selectedPath);

 // Test searching for a message type
 treeView.clearSearchResults();
 treeView.expandAll();
 treeView.searchTextField.setText("Test Type");
 treeView.searchButton.doClick();
 final TreePath[] selectedPaths = treeView.fixTree.getSelectionPaths();
 int count = 0;
 for (TreePath path : selectedPaths) {
 if (path.getLastPathComponent() instanceof FixInfoTreeNode) {
 final FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if ("Test Message".equals(((MessageInfo)node.getUserObject()).getMessageType())) {
 count++;
 }
 }
 }
 assertEquals(1, count);

 // Test searching for a field name
 treeView.clearSearchResults();
 treeView.expandAll();
 treeView.searchTextField.setText("Test Field");
 treeView.searchButton.doClick();
 final TreePath[] selectedPaths2 = treeView.fixTree.getSelectionPaths();
 int count2 = 0;
 for (TreePath path : selectedPaths2) {
 if (path.getLastPathComponent() instanceof FixInfoTreeNode) {
 final FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if ("Test Field".equals(((FieldInfo)node.getUserObject()).getName())) {
 count2++;
 }
 }
 }
 assertEquals(3, count2);

 // Test searching for a field tag number
 treeView.clearSearchResults();
 treeView.expandAll();
 treeView.searchTextField.setText("1");
 treeView.searchButton.doClick();
 final TreePath[] selectedPaths3 = treeView.fixTree.getSelectionPaths();
 int count3 = 0;
 for (TreePath path : selectedPaths3) {
 if (path.getLastPathComponent() instanceof FixInfoTreeNode) {
 final FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if ("Test Field".equals(((FieldInfo)node.getUserObject()).getName())) {
 final FixInfoTreeNode parentNode = (FixInfoTreeNode)node.getParent();
 if (parentNode.getUserObject() instanceof ComponentInfo && "Test Component".equals(((ComponentInfo)parentNode.getUserObject()).getName())) {
 count3++;
 }
 }
 }
 }
 assertEquals(2, count3);
 }

 private void clearSearchResults() {
 treeView.searchTextField.setText("");
 treeView.searchButton.doClick();
 }
}