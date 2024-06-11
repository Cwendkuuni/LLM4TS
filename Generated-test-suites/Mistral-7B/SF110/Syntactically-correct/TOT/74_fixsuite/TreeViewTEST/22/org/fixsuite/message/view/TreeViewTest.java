package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

import org.junit.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testTreeViewInitialization() {
 // Test if the TreeView has been initialized correctly
 assertNotNull(treeView.getMainScrollPane());
 assertNotNull(treeView.getFixTree());
 assertNotNull(treeView.getSearchLabel());
 assertNotNull(treeView.getSearchTextField());
 assertNotNull(treeView.getSearchButton());
 assertNotNull(treeView.getClearButton());
 assertNotNull(treeView.getExpandButton());
 assertNotNull(treeView.getCollapseButton());
 }

 @Test
 public void testLoadFIXTree() {
 // Test if the loadFIXTree method loads the FIXTree correctly
 final FixInfoTreeNode rootNode = (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot();
 assertNotNull(rootNode);
 assertEquals("Dictionaries", rootNode.getUserObject());
 }

 @Test
 public void testLoadMessages() {
 // Test if the loadMessages method loads the messages correctly for a dictionary
 final DictionaryInfo dictionary = new DictionaryInfo("TestDictionary", "1.0");
 library.addDictionary(dictionary);
 final FixInfoTreeNode versionNode = (FixInfoTreeNode)treeView.loadMessages(rootNode, dictionary)[0];
 assertNotNull(versionNode);
 assertEquals("1.0", versionNode.getUserObject());
 }

 @Test
 public void testLoadItems() {
 // Test if the loadItems method loads the items correctly for a message
 final MessageInfo message = new MessageInfo("TestMessage", "TESTMSGTYPE", new List<FixInfo>());
 dictionary.getMessages().add(message);
 final FixInfoTreeNode node = (FixInfoTreeNode)treeView.loadItems(versionNode, message, 0)[0];
 assertNotNull(node);
 assertEquals(message, node.getUserObject());
 }

 @Test
 public void testExpandAll() {
 // Test if the expandAll method expands all nodes in the tree
 treeView.expandAll();
 final int initialHeight = treeView.getHeight();
 final int expandedHeight = treeView.getFixTree().getHeight();
 assertTrue("Tree should be expanded", expandedHeight > initialHeight);
 }

 @Test
 public void testCollapseAll() {
 // Test if the collapseAll method collapses all nodes in the tree
 treeView.collapseAll();
 final int expandedHeight = treeView.getFixTree().getHeight();
 final int collapsedHeight = treeView.getFixTree().getHeight();
 assertTrue("Tree should be collapsed", collapsedHeight < expandedHeight);
 }

 @Test
 public void testSearch() {
 // Test if the search method searches for a specific string in the tree
 treeView.search("Test", treeView.getFixTree().getModel().getRoot());
 final int initialSize = ((DefaultMutableTreeModel)treeView.getFixTree().getModel()).getChildCount((DefaultMutableTreeNode)treeView.getFixTree().getModel().getRoot());
 treeView.search("Test", treeView.getFixTree().getModel().getRoot());
 final int finalSize = ((DefaultMutableTreeModel)treeView.getFixTree().getModel()).getChildCount((DefaultMutableTreeNode)treeView.getFixTree().getModel().getRoot());
 assertEquals("Tree should contain the same number of nodes after searching", initialSize, finalSize);
 }

 @Test
 public void testClear() {
 // Test if the clear method clears the highlights in the tree
 treeView.search("Test", treeView.getFixTree().getModel().getRoot());
 treeView.clear((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot());
 final boolean initialHighlighted = ((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot()).isHighlighted();
 assertFalse("Tree should not be highlighted after clearing", initialHighlighted);
 }
}