package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Before;
import org.junit.Test;

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
 // Arrange
 final DictionaryInfo dict1 = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "MsgType1")));
 final DictionaryInfo dict2 = new DictionaryInfo("Dict2", Arrays.asList(new MessageInfo("Msg2", "MsgType2"), new MessageInfo("Msg3", "MsgType3")));
 library.setDictionaries(Arrays.asList(dict1, dict2));

 // Act
 final TreeModel model = treeView.loadFIXTree();

 // Assert
 final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
 final List<DefaultMutableTreeNode> children = Arrays.asList(root.getChildren());
 final List<String> expectedNames = Arrays.asList("Dictionaries", "Dict1", "Msg1", "Dict2", "Msg2", "Msg3");

 assertEquals(expectedNames.size(), children.size());
 for (int i = 0; i < children.size(); ++i) {
 final DefaultMutableTreeNode node = children.get(i);
 assertEquals(expectedNames.get(i), node.getUserObject().toString());
 }
 }

 @Test
 public void testLoadMessages() {
 // Arrange
 final DictionaryInfo dict = new DictionaryInfo("Dict", Arrays.asList(new MessageInfo("Msg", "MsgType")));
 library.setDictionaries(Collections.singletonList(dict));

 // Act
 final DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode("Version");
 treeView.loadMessages(versionNode, dict);

 // Assert
 final List<DefaultMutableTreeNode> messages = Arrays.asList(versionNode.getChildren());
 final int messageCount = messages.size();

 assertEquals(1, messageCount);
 final DefaultMutableTreeNode msgNode = messages.get(0);
 final List<String> expectedNames = Arrays.asList("Msg");

 assertEquals(expectedNames.size(), msgNode.getChildCount());
 for (int i = 0; i < msgNode.getChildCount(); ++i) {
 final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)msgNode.getChildAt(i);
 assertEquals(expectedNames.get(i), childNode.getUserObject().toString());
 }
 }

 @Test
 public void testLoadItems() {
 // Arrange
 final MessageInfo message = new MessageInfo("Msg", "MsgType");
 final List<FixInfo> items = Arrays.asList(new FieldInfo("Field1", "Tag1"), new FieldInfo("Field2", "Tag2"), new ComponentInfo("Comp", "CompName"));
 message.setItems(items);

 // Act
 final DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode("Message");
 treeView.loadItems(branchNode, message, 0);

 // Assert
 final List<DefaultMutableTreeNode> children = Arrays.asList(branchNode.getChildren());
 final int itemCount = children.size();

 assertEquals(3, itemCount);
 for (int i = 0; i < children.size(); ++i) {
 final DefaultMutableTreeNode node = children.get(i);
 final FixInfo fixInfo = (FixInfo)node.getUserObject();
 if (fixInfo instanceof FieldInfo) {
 final FieldInfo fieldInfo = (FieldInfo)fixInfo;
 assertEquals(fieldInfo.getName(), node.getUserObject().toString());
 }
 else if (fixInfo instanceof ComponentInfo) {
 final ComponentInfo componentInfo = (ComponentInfo)fixInfo;
 assertEquals(componentInfo.getName(), node.getUserObject().toString());
 }
 }
 }

 @Test
 public void testExpandAll() {
 // Arrange
 final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
 for (int i = 0; i < 10; ++i) {
 final DefaultMutableTreeNode node = new DefaultMutableTreeNode("Node" + i);
 root.add(node);
 }
 final TreeModel model = new DefaultTreeModel(root);
 final JTree tree = new JTree(model);

 // Act
 treeView.expandAll(tree);

 // Assert
 for (int i = 0; i < tree.getRowCount(); ++i) {
 assertTrue(tree.isExpanded(new TreePath(new Object[] { tree.getModel().getRow(i)})));
 }
 }

 @Test
 public void testSearch() {
 // Arrange
 final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
 final DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode("Version");
 root.add(versionNode);
 final DefaultMutableTreeNode msgNode1 = new DefaultMutableTreeNode("Msg1");
 final DefaultMutableTreeNode msgNode2 = new DefaultMutableTreeNode("Msg2");
 versionNode.add(msgNode1);
 versionNode.add(msgNode2);final DefaultMutableTreeNode fieldNode1 = new DefaultMutableTreeNode("Field1");
 final DefaultMutableTreeNode fieldNode2 = new DefaultMutableTreeNode("Field2");
 msgNode1.add(fieldNode1);
 msgNode1.add(fieldNode2);
 final TreeModel model = new DefaultTreeModel(root);
 final JTree tree = new JTree(model);
 final TreeCellRenderer renderer = treeView.new FixInfoTreeNode();
 tree.setCellRenderer(renderer);

 // Act
 treeView.search(tree, "Field");

 // Assert
 for (int i = 0; i < tree.getRowCount(); ++i) {
 final TreePath path = tree.getPathForLocation(i, 0);
 final Object node = path.getLastPathComponent();
 final DefaultMutableTreeNode actualNode = (DefaultMutableTreeNode)node;
 if (actualNode.getLevel() == 3 && actualNode.getUserObject() instanceof FieldInfo) {
 final FieldInfo fieldInfo = (FieldInfo)actualNode.getUserObject();
 assertTrue(fieldInfo.getName().matches("Field[0-9]*"));
 assertTrue(renderer.isHighlighted(tree, actualNode));
 }
 else {
 assertFalse(renderer.isHighlighted(tree, actualNode));
 }
 }
 }

 @Test
 public void testClear() {
 // Arrange
 final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
 final DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode("Version");
 root.add(versionNode);
 final DefaultMutableTreeNode msgNode1 = new DefaultMutableTreeNode("Msg1");
 final DefaultMutableTreeNode fieldNode1 = new DefaultMutableTreeNode("Field1");
 msgNode1.add(fieldNode1);
 versionNode.add(msgNode1);
 final TreeModel model = new DefaultTreeModel(root);
 final JTree tree = new JTree(model);
 final TreeCellRenderer renderer = treeView.new FixInfoTreeNode();
 tree.setCellRenderer(renderer);

 // Act
 treeView.search(tree, "Field");
 final DefaultMutableTreeNode nodeToClear = msgNode1;
 treeView.clear(nodeToClear);

 // Assert
 for (int i = 0; i < tree.getRowCount(); ++i) {
 final TreePath path = tree.getPathForLocation(i, 0);
 final Object node = path.getLastPathComponent();
 final DefaultMutableTreeNode actualNode = (DefaultMutableTreeNode)node;
 assertFalse(renderer.isHighlighted(tree, actualNode));
 }
 }
}