package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private FixInfoTreeNode rootNode;

 @Before
 public void setUp() {
 Library library = new Library(); // Assuming Library class has been initialized
 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
 }

 @Test
 public void testTreeView_LoadsFixTreeWithCorrectData() {
 // Check that the root node exists and has children
 assertNotNull(rootNode);
 assertEquals("Dictionaries", rootNode.getUserObject());
 int numChildren = rootNode.getChildCount();
 assertEquals(4, numChildren); // Assuming there are 4 dictionaries

 // Check that each dictionary node has the correct number of children (messages)
 for (int i = 0; i < numChildren; i++) {
 FixInfoTreeNode dictNode = (FixInfoTreeNode) rootNode.getChildAt(i);
 int numMessages = dictNode.getChildCount();
 assertEquals(10, numMessages); // Assuming each dictionary has 10 messages
 }
 }

 @Test
 public void testTreeView_SearchFunctionality() {
 // Set up some data for testing
 rootNode.setUserObject("Test Dictionaries");
 FixInfoTreeNode messageNode1 = new FixInfoTreeNode(new MessageInfo("TestMessage1", "TestMessageType1"));
 messageNode1.setUserObject(messageNode1);
 messageNode1.add(new FixInfoTreeNode(new FieldInfo("TestField1", 1, "TestName1")));
 messageNode1.add(new FixInfoTreeNode(new FieldInfo("TestField2", 2, "TestName2")));
 rootNode.add(messageNode1);

 // Perform search with valid search term
 treeView.searchTextField.setText("TestMessageType1");
 treeView.searchButton.doClick();
 TreePath resultPath = treeView.fixTree.getSelectionPath();
 assertNotNull(resultPath);
 assertEquals(messageNode1.getPath(), resultPath.getPath());

 // Perform search with invalid search term
 treeView.searchTextField.setText("InvalidSearchTerm");
 treeView.searchButton.doClick();
 TreePath resultPath2 = treeView.fixTree.getSelectionPath();
 assertNull(resultPath2);
 }

 @Test
 public void testTreeView_CollapseAllFunctionality() {
 // Check that all nodes are expanded initially
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(i);
 assertTrue(node.isExpanded());
 }

 // Collapse all nodes
 treeView.collapseAll();
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(i);
 assertFalse(node.isExpanded());
 }

 // Expand all nodes back to original state
 treeView.expandAll();
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(i);
 assertTrue(node.isExpanded());
 }
 }
}