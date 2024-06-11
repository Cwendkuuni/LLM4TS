package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private FixInfoTreeNode rootNode;
 private FixInfoTreeNode messageNode;
 private FixInfoTreeNode componentNode;
 private FixInfoTreeNode fieldNode;

 @Before
 public void setUp() {
 // Create a mock library for testing
 Library library = Mockito.mock(Library.class);
 // Initialize the TreeView with the mock library
 treeView = new TreeView(library);
 // Get the JTree component from the TreeView
 fixTree = treeView.getFixTree();
 // Initialize the TreeView with sample data
 rootNode = new FixInfoTreeNode("Dictionaries");
 messageNode = new FixInfoTreeNode(new MessageInfo("TestMessage", "TestMessageType"));
 componentNode = new FixInfoTreeNode(new ComponentInfo("TestComponent"));
 fieldNode = new FixInfoTreeNode(new FieldInfo("TestField", 1));
 rootNode.add(messageNode);
 messageNode.add(componentNode);
 componentNode.add(fieldNode);
 DefaultTreeModel model = new DefaultTreeModel(rootNode);
 fixTree.setModel(model);
 treeView.initGUI();
 }

 @Test
 public void testLoadFIXTree() {
 // Test that the loadFIXTree method populates the tree with the correct data
 assertEquals(3, fixTree.getRowCount());
 assertTrue(fixTree.getModel().getRoot().getAllowsChildren());
 assertFalse(messageNode.getAllowsChildren());
 assertFalse(componentNode.getAllowsChildren());
 assertFalse(fieldNode.getAllowsChildren());
 }

 @Test
 public void testExpandAll() {
 // Test that the expandAll method expands all nodes in the tree
 treeView.expandAll();
 assertEquals(3, fixTree.getRowCount());
 for (int i = 0; i < 3; i++) {
 assertTrue(fixTree.isExpanded(new TreePath(fixTree.getModel().getPathForRow(i))));
 }
 }

 @Test
 public void testCollapseAll() {
 // Test that the collapseAll method collapses all nodes in the tree
 treeView.collapseAll();
 assertEquals(3, fixTree.getRowCount());
 for (int i = 0; i < 3; i++) {
 assertFalse(fixTree.isExpanded(new TreePath(fixTree.getModel().getPathForRow(i))));
 }
 }

 @Test
 public void testSearch() {
 // Test that the search method highlights the correct nodes in the tree
 treeView.search("Test");
 assertFalse(rootNode.isHighlighted());
 assertTrue(messageNode.isHighlighted());
 assertTrue(componentNode.isHighlighted());
 assertTrue(fieldNode.isHighlighted());
 // Test that the search method expands the correct nodes in the tree
 assertEquals(4, fixTree.getRowCount());
 assertTrue(fixTree.isExpanded(new TreePath(fixTree.getModel().getPathForRow(3))));
 }

 @Test
 public void testClear() {
 // Test that the clear method clears the highlighting of all nodes in the tree
 treeView.clear();
 assertFalse(rootNode.isHighlighted());
 assertFalse(messageNode.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 assertFalse(fieldNode.isHighlighted());
 }

 @Test
 public void testActionPerformed() {
 // Test that the actionPerformed method correctly handles tree expansion, collapse, search, and clear actions
 treeView.expandAll();
 treeView.search("Test");
 treeView.clear();
 treeView.collapseAll();
 treeView.actionPerformed(new ActionEvent(treeView, 0, "Expand"));
 assertEquals(4, fixTree.getRowCount());
 treeView.actionPerformed(new ActionEvent(treeView, 0, "Collapse"));
 assertEquals(3, fixTree.getRowCount());
 treeView.actionPerformed(new ActionEvent(treeView, 0, "Search"));
 assertFalse(rootNode.isHighlighted());
 assertTrue(messageNode.isHighlighted());
 assertTrue(componentNode.isHighlighted());
 assertTrue(fieldNode.isHighlighted());
 treeView.actionPerformed(new ActionEvent(treeView, 0, "Clear"));
 assertFalse(rootNode.isHighlighted());
 assertFalse(messageNode.isHighlighted());
 assertFalse(componentNode.isHighlighted());
 assertFalse(fieldNode.isHighlighted());
 }

 @Test
 public void testKeyPressed() {
 // Test that the keyPressed method correctly handles Enter key presses and triggers the search action
 treeView.searchTextField.setText("Test");
 treeView.fixTree.requestFocus();
 treeView.keyPressed(new KeyEvent(treeView, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
 assertFalse(rootNode.isHighlighted());
 assertTrue(messageNode.isHighlighted());
 assertTrue(componentNode.isHighlighted());
 assertTrue(fieldNode.isHighlighted());
 }
}