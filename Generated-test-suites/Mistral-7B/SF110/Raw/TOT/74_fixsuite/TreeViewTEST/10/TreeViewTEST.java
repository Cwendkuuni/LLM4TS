package org.fixsuite.message.view;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

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
 FixInfoTreeNode rootNode = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot();
 assertEquals("Dictionaries", rootNode.getUserObject());
 assertNotNull(rootNode.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 DictionaryInfo dictionary = library.getDictionaries().get(0);
 FixInfoTreeNode versionNode = treeView.findNodeWithValue(treeView.fixTree, dictionary.getVersion());
 assertNotNull(versionNode);
 int messageCount = versionNode.getChildCount();
 assertEquals(dictionary.getMessages().size(), messageCount);
 }

 @Test
 public void testLoadItems() {
 MessageInfo message = library.getMessage("10=TAG1|11=VALUE1|12=TAG2|13=VALUE2");
 FixInfoTreeNode leafNode = (FixInfoTreeNode)treeView.findNodeWithValue(treeView.fixTree, message);
 assertNotNull(leafNode);
 int itemCount = leafNode.getChildCount();
 assertEquals(message.getItems().size(), itemCount);
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int expandedCount = 0;
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 TreePath path = treeView.fixTree.getPathForRow(i);
 FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if (node.isExpanded()) {
 expandedCount++;
 }
 }
 assertEquals(treeView.fixTree.getRowCount(), expandedCount);
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int collapsedCount = 0;
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 TreePath path = treeView.fixTree.getPathForRow(i);
 FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if (!node.isExpanded()) {
 collapsedCount++;
 }
 }
 assertEquals(treeView.fixTree.getRowCount(), collapsedCount);
 }

 @Test
 public void testSearch() {
 String searchString = "TAG1";
 treeView.search(".*" + searchString + ".*");
 int count = 0;
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 TreePath path = treeView.fixTree.getPathForRow(i);
 FixInfoTreeNode node = (FixInfoTreeNode)path.getLastPathComponent();
 if (node.isHighlighted()) {
 count++;
 FixInfo fixInfo = (FixInfo)node.getUserObject();
 if (fixInfo instanceof MessageInfo) {
 MessageInfo messageInfo = (MessageInfo)fixInfo;
 assertTrue(messageInfo.getName().matches(searchString) || messageInfo.getMessageType().matches(searchString));
 }
 else if (fixInfo instanceof FieldInfo) {
 FieldInfo fieldInfo = (FieldInfo)fixInfo;
 assertTrue(fieldInfo.getName().matches(searchString) || fieldInfo.getTagNumber().matches(searchString));
 }
 }
 }
 assertEquals(treeView.fixTree.getRowCount(), count);
 }

 @Test
 public void testClear() {
 treeView.search(".*TAG1.*");
 int countBeforeClear = treeView.fixTree.getRowCount();
 treeView.clear((FixInfoTreeNode)treeView.fixTree.getModel().getRoot());
 int countAfterClear = treeView.fixTree.getRowCount();
 assertEquals(0, countAfterClear - countBeforeClear);
 }
}