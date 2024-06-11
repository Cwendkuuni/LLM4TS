package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testTreeViewConstruction() {
 assertNotNull(treeView);
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
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 assertNotNull(rootNode);
 assertNotNull(rootNode.getUserObject());
 assertEquals("Dictionaries", rootNode.getUserObject().toString());
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = new DictionaryInfo("Test Dictionary", null);
 library.addDictionary(dictionary);
 final FixInfoTreeNode versionNode = treeView.loadFIXTree().getFirstChild();
 assertNotNull(versionNode);
 assertEquals("Test Dictionary", versionNode.getUserObject().toString());
 treeView.loadMessages(versionNode, dictionary);
 assertFalse(versionNode.isLeaf());
 }

 @Test
 public void testLoadItems() {
 final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType", new ArrayList<>());
 dictionary.addMessage(message);
 final FixInfoTreeNode branchNode = (FixInfoTreeNode)versionNode.getFirstChild();
 assertNotNull(branchNode);
 assertEquals("TestMessageType", branchNode.getUserObject().toString());
 treeView.loadItems(branchNode, message, 0);
 assertFalse(branchNode.isLeaf());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 final TreeModel model = treeView.getFixTree().getModel();
 for (int i = 0; i < model.getRowCount(); i++) {
 assertTrue(model.isNodeExpanded(new TreePath(new Object[0][0], i)));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 final TreeModel model = treeView.getFixTree().getModel();
 for (int i = 0; i < model.getRowCount(); i++) {
 assertFalse(model.isNodeExpanded(new TreePath(new Object[0][0], i)));
 }
 }

 @Test
 public void testSearch() {
 final String searchString = "TestMessageType";
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.search(rootNode, searchString);
 final TreeModel model = treeView.getFixTree().getModel();
 int count = 0;
 for (int i = 0; i < model.getRowCount(); i++) {
 if (model.isNodeDescendant(new TreePath(new Object[0][0], i), new TreePath(rootNode.getPath())) &&
 ((FixInfoTreeNode)model.getNodeAt(i)).isHighlighted()) {
 count++;
 }
 }
 assertEquals(1, count);
 }

 @Test
 public void testClear() {
 final String searchString = "TestMessageType";
 treeView.search(rootNode, searchString);
 treeView.clear(rootNode);
 final TreeModel model = treeView.getFixTree().getModel();
 int count = 0;
 for (int i = 0; i < model.getRowCount(); i++) {
 if (model.isNodeDescendant(new TreePath(new Object[0][0], i), new TreePath(rootNode.getPath())) &&
 ((FixInfoTreeNode)model.getNodeAt(i)).isHighlighted()) {
 count++;
 }
 }
 assertEquals(0, count);
 }

 @Test
 public void testKeyEvent() {
 final String searchString = "TestMessageType";
 treeView.getSearchTextField().setText(searchString);
 treeView.getSearchTextField().dispatchEvent(new KeyEvent(treeView.getSearchTextField(), 10, true, KeyEvent.KEY_PRESSED));
 treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), ActionEvent.ACTION_PERFORMED, null));
 final TreeModel model = treeView.getFixTree().getModel();
 int count = 0;
 for (int i = 0; i < model.getRowCount(); i++) {
 if (model.isNodeDescendant(new TreePath(new Object[0][0], i), new TreePath(rootNode.getPath())) &&
 ((FixInfoTreeNode)model.getNodeAt(i)).isHighlighted()) {
 count++;
 }
 }
 assertEquals(1, count);
 }
}