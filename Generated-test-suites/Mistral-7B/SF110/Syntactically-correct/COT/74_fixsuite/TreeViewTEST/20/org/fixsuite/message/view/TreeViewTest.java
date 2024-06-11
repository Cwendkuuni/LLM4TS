package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private TreeCellRenderer cellRenderer;
 private FixInfoTreeNode rootNode;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 cellRenderer = treeView.getFixTree().getCellRenderer();
 rootNode = (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot();
 }

 @Test
 public void testTreeViewConstructor() {
 assertNotNull(treeView);
 }

 @Test
 public void testInitGUI() {
 assertEquals(new BorderLayout(), treeView.getLayout());
 assertNotNull(treeView.getMainScrollPane());
 assertNotNull(treeView.getFixTree());
 assertNotNull(treeView.getFixTree().getCellRenderer());
 assertNotNull(treeView.getSearchLabel());
 assertNotNull(treeView.getSearchTextField());
 assertNotNull(treeView.getSearchButton());
 assertNotNull(treeView.getClearButton());
 assertNotNull(treeView.getExpandButton());
 assertNotNull(treeView.getCollapseButton());
 assertNotNull(treeView.getMainScrollPane().getViewport());
 assertNotNull(treeView.getToolBar());
 }

 @Test
 public void testLoadFIXTree() {
 final int numberOfDictionaries = 3;
 List<DictionaryInfo> dictionaryList = Arrays.asList(new DictionaryInfo("Dict1", null), new DictionaryInfo("Dict2", null), new DictionaryInfo("Dict3", null));
 library.setDictionaries(dictionaryList);
 FixInfoTreeNode expectedRoot = new FixInfoTreeNode("Dictionaries");
 for (int i = 0; i < numberOfDictionaries; ++i) {
 final FixInfoTreeNode dictionaryNode = new FixInfoTreeNode("Version" + (i + 1));
 expectedRoot.add(dictionaryNode);
 }
 assertEquals(expectedRoot, rootNode);
 }

 @Test
 public void testLoadMessages() {
 final int numberOfMessages = 5;
 DictionaryInfo dictionary = new DictionaryInfo("DictName", null);
 List<MessageInfo> messageList = Arrays.asList(new MessageInfo("Msg1", "MsgType1", null), new MessageInfo("Msg2", "MsgType2", null), new MessageInfo("Msg3", "MsgType3", null), new MessageInfo("Msg4", "MsgType4", null), new MessageInfo("Msg5", "MsgType5", null));
 dictionary.setMessages(messageList);
 FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
 FixInfoTreeNode messageNode1 = new FixInfoTreeNode(new MessageInfo("Msg1"));
 FixInfoTreeNode messageNode2 = new FixInfoTreeNode(new MessageInfo("Msg2"));
 FixInfoTreeNode messageNode3 = new FixInfoTreeNode(new MessageInfo("Msg3"));
 FixInfoTreeNode messageNode4 = new FixInfoTreeNode(new MessageInfo("Msg4"));
 FixInfoTreeNode messageNode5 = new FixInfoTreeNode(new MessageInfo("Msg5"));
 versionNode.add(messageNode1);
 versionNode.add(messageNode2);
 versionNode.add(messageNode3);
 versionNode.add(messageNode4);
 versionNode.add(messageNode5);
 expectedRoot.add(versionNode);
 library.setDictionaries(Collections.singletonList(dictionary));
 loadMessagesAndVerify(rootNode, numberOfMessages);
 }

 @Test
 public void testLoadItems() {
 final int numberOfItems = 3;
 MessageInfo message = new MessageInfo("MsgName", "MsgType", null);
 CompositeFixInfo composite = new CompositeFixInfo("CompName", null, Arrays.asList(message, message, message));
 FixInfoTreeNode branchNode = new FixInfoTreeNode(new FixInfo(composite));
 FixInfoTreeNode leafNode1 = new FixInfoTreeNode(new FieldInfo("FieldName1", 1, "FieldName1"));
 FixInfoTreeNode leafNode2 = new FixInfoTreeNode(new FieldInfo("FieldName2", 2, "FieldName2"));
 FixInfoTreeNode leafNode3 = new FixInfoTreeNode(new FieldInfo("FieldName3", 3, "FieldName3"));
 leafNode1.add(leafNode2);
 leafNode1.add(leafNode3);
 branchNode.add(leafNode1);
 expectedBranchNode.add(branchNode);
 loadItemsAndVerify(branchNode, numberOfItems);
 }

 @Test
 public void testExpandAll() {
 expandAll();
 assertFalse(treeView.getFixTree().isRootVisible());
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode childNode = (FixInfoTreeNode)rootNode.getChildAt(i);
 assertTrue(childNode.isLeaf());
 assertTrue(childNode.isExpanded());
 }
 }

 @Test
 public void testCollapseAll() {
 collapseAll();
 assertTrue(treeView.getFixTree().isRootVisible());
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode childNode = (FixInfoTreeNode)rootNode.getChildAt(i);
 assertTrue(childNode.isLeaf());
 assertFalse(childNode.isExpanded());
 }
 }

 @Test
 public void testSearch() {
 final String searchString = "Name";
 final int numberOfMatches = 3;
 search(rootNode, searchString);
 for (int i = 0; i < numberOfMatches; ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)findNodeByName("Name", rootNode);
 assertTrue(node.isHighlighted());
 }
 }

 @Test
 public void testClear() {
 clear(rootNode);
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)rootNode.getChildAt(i);
 assertFalse(node.isHighlighted());
 }
 }

 @Test
 public void testActionPerformedCollapseAll() {
 collapseButton.doClick();
 expandAll();
 }

 @Test
 public void testActionPerformedExpandAll() {
 expandButton.doClick();
 collapseAll();
 }

 @Test
 public void testActionPerformedSearch() {
 final String searchString = "Name";
 searchTextField.setText(searchString);
 searchButton.doClick();
 clearAll();
 search(rootNode, searchString);
 }

 @Test
 public void testActionPerformedClear() {
 clearButton.doClick();
 clearAll();
 }

 @Test
 public void testKeyPressedSearch() {
 final String searchString = "Name";
 searchTextField.setText(searchString);
 searchTextField.requestFocus();
 keyPressed(searchTextField, KeyEvent.VK_ENTER);
 clearAll();
 search(rootNode, searchString);
 }

 private void expandAll() {
 treeView.expandAll();
 }

 private void collapseAll() {
 treeView.collapseAll();
 }

 private void search(FixInfoTreeNode node, String searchString) {
 treeView.search(node, searchString);
 }

 private void clear(FixInfoTreeNode node) {
 treeView.clear(node);
 }

 private void loadItemsAndVerify(FixInfoTreeNode branchNode, int numberOfChildren) {
 assertEquals(numberOfChildren, branchNode.getChildCount());
 for (int i = 0; i < numberOfChildren; ++i) {
 final FixInfoTreeNode childNode = (FixInfoTreeNode)branchNode.getChildAt(i);
 assertEquals(new FixInfoTreeNode(new FieldInfo("ChildName" + i, i + 1, "ChildName" + i)).getClass(), childNode.getClass());
 }
 }

 private void loadMessagesAndVerify(FixInfoTreeNode node, int numberOfMessages) {
 assertEquals(numberOfMessages, node.getChildCount());
 for (int i = 0; i < numberOfMessages; ++i) {
 final FixInfoTreeNode messageNode = (FixInfoTreeNode)node.getChildAt(i);
 assertEquals(new FixInfoTreeNode(new MessageInfo("Message" + i, "MsgType" + i, null)).getClass(), messageNode.getClass());
 }
 }

 private FixInfoTreeNode expectedBranchNode = new FixInfoTreeNode(new FixInfo(new CompositeFixInfo("ComponentName", null, Collections.emptyList())));
 private FixInfoTreeNode findNodeByName(String name, FixInfoTreeNode parentNode) {
 FixInfoTreeNode node = null;
 for (int i = 0; i < parentNode.getChildCount(); ++i) {
 final FixInfoTreeNode childNode = (FixInfoTreeNode)parentNode.getChildAt(i);
 if (childNode.getUserObject() instanceof FieldInfo && ((FieldInfo)childNode.getUserObject()).getName().equals(name)) {
 node = childNode;
 break;
 }
 if (childNode.getChildCount() > 0) {
 node = findNodeByName(name, (FixInfoTreeNode)childNode);
 if (node != null) {
 break;
 }
 }
 }
 return node;
 }

 private void clearAll() {
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)rootNode.getChildAt(i);
 treeView.clear(node);
 }
 }

 private class TestLibrary extends Library {
 private TestLibrary() {
 super();
 }
 }

 private Library library;
}