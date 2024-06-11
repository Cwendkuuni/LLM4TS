package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fixsuite.message.InfoType;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.Before;
import org.junit.Test;

import org.fixsuite.message.view.Library;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.FixInfoTreeNode;
import org.fixsuite.message.view.FixInfoCellRenderer;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 DictionaryInfo dict1 = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "Type1"), new MessageInfo("Msg2", "Type2")));
 library.addDictionary(dict1);
 DictionaryInfo dict2 = new DictionaryInfo("Dict2", Arrays.asList(new MessageInfo("Msg3", "Type3"), new MessageInfo("Msg4", "Type4"), new MessageInfo("Msg5", "Type5")));
 library.addDictionary(dict2);
 treeView = new TreeView(library);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode root = treeView.loadFIXTree();
 assertEquals("Dictionaries", root.getUserObject().toString());
 assertNotNull(root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 FixInfoTreeNode versionNode = (FixInfoTreeNode)treeView.findChild(treeView.getRoot(), "Dict1");
 assertNotNull(versionNode);
 assertNotNull(versionNode.getChildCount());
 List<MessageInfo> messages = new ArrayList<>();
 for (int i = 0; i < versionNode.getChildCount(); i++) {
 MessageInfo message = (MessageInfo)versionNode.getChildAt(i).getUserObject();
 messages.add(message);
 }
 assertEquals(2, messages.size());
 assertNotNull(messages.get(0).getName());
 assertNotNull(messages.get(0).getMessageType());
 assertNotNull(messages.get(1).getName());
 assertNotNull(messages.get(1).getMessageType());
 }

 @Test
 public void testLoadItems() {
 FixInfoTreeNode componentNode = (FixInfoTreeNode)treeView.findChild(treeView.getRoot(), "Dict2");
 assertNotNull(componentNode);
 assertNotNull(componentNode.getChildCount());
 FixInfoTreeNode leafNode = (FixInfoTreeNode)componentNode.getChildAt(0);
 List<FixInfo> items = new ArrayList<>();
 for (int i = 0; i < leafNode.getChildCount(); i++) {
 FixInfo info = (FixInfo)leafNode.getChildAt(i).getUserObject();
 items.add(info);
 }
 assertEquals(5, items.size());
 for (FixInfo info : items) {
 if (info instanceof CompositeFixInfo) {
 List<FixInfo> subItems = new ArrayList<>();
 for (int j = 0; j < ((CompositeFixInfo)info).getItems().size(); j++) {
 subItems.add(((CompositeFixInfo)info).getItems().get(j));
 }
 assertNotNull(subItems);
 assertFalse(subItems.isEmpty());
 }
 }
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int numExpanded = 0;
 for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
 if (treeView.isExpanded(i)) {
 numExpanded++;
 }
 }
 assertEquals(11, numExpanded);
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int numExpanded = 0;
 for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
 if (treeView.isExpanded(i)) {
 numExpanded++;
 }
 }
 assertEquals(0, numExpanded);
 }

 @Test
 public void testSearch() {
 treeView.search("Type");
 int numHighlighted = 0;
 for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.getModel().getPathForRow(i);
 if (node.isHighlighted()) {
 numHighlighted++;
 }
 }
 assertEquals(5, numHighlighted);
 }

 @Test
 public void testClear() {
 treeView.search("Type");
 treeView.clear();
 int numHighlighted = 0;
 for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.getModel().getPathForRow(i);
 if (node.isHighlighted()) {
 numHighlighted++;
 }
 }
 assertEquals(0, numHighlighted);
 }
}