package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.junit.Before;
import org.junit.Test;

import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 library.addDictionary(new DictionaryInfo("test", Arrays.asList(new MessageInfo("TestMessage", "TestType"), new ComponentInfo("TestComponent", "TestComponentName"), new FieldInfo("TestField", "TestFieldName", 1, "TestFieldName"))));
 treeView = new TreeView(library);
 }

 @Test
 public void testLoadTree() {
 FixInfoTreeNode root = (FixInfoTreeNode) treeView.getFixTree().getModel().getRoot();
 assertEquals("Dictionaries", root.getUserObject());
 assertNotNull(root);
 assertEquals(1, root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 FixInfoTreeNode versionNode = (FixInfoTreeNode) treeView.getFixTree().getModel().getChild(0);
 assertEquals("test", versionNode.getUserObject().toString());
 assertNotNull(versionNode);
 assertEquals(1, versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 FixInfoTreeNode messageNode = (FixInfoTreeNode) treeView.getFixTree().getModel().getChildAt(0, 0);
 assertEquals("TestMessage", messageNode.getUserObject().toString());
 assertNotNull(messageNode);
 assertEquals(1, messageNode.getChildCount());
 }

 @Test
 public void testSearch() {
 treeView.getSearchTextField().setText("test");
 treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), 0, "Clicked"));
 FixInfoTreeNode testComponentNode = (FixInfoTreeNode) treeView.getFixTree().getModel().getChildAt(0, 1);
 assertTrue(testComponentNode.isHighlighted());
 }

 @Test
 public void testClear() {
 treeView.getSearchTextField().setText("test");
 treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), 0, "Clicked"));
 treeView.actionPerformed(new ActionEvent(treeView.getClearButton(), 0, "Clicked"));
 assertFalse(testComponentNode.isHighlighted());
 }

 @Test(expected = NullPointerException.class)
 public void testNullLibrary() {
 TreeView treeView2 = new TreeView(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyLibrary() {
 Library emptyLibrary = new Library();
 TreeView treeView3 = new TreeView(emptyLibrary);
 }

 @Test
 public void testExpandAll() {
 Collections.synchronizedList(treeView.getFixTree().getModel()).forEach(node -> {
 treeView.expandAll();
 assertTrue(node.isExpanded());
 });
 }

 @Test
 public void testCollapseAll() {
 Collections.synchronizedList(treeView.getFixTree().getModel()).forEach(node -> {
 treeView.collapseAll();
 assertFalse(node.isExpanded());
 });
 }
}