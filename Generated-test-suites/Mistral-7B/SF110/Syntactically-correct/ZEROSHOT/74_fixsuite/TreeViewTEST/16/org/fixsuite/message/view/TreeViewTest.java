package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.fixsuite.message.info.*;
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
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
 assertEquals("Dictionaries", rootNode.getUserObject());
 int numberOfNodes = 0;
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(i);
 int numberOfMessageNodes = dictionaryNode.getChildCount();
 for (int j = 0; j < numberOfMessageNodes; ++j) {
 FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
 FixInfo fixInfo = (FixInfo) messageNode.getUserObject();
 assertNotNull(fixInfo);
 ++numberOfNodes;
 }
 }
 assertEquals("Number of nodes: " + numberOfNodes, numberOfNodes, 27);
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 for (int i = 0; i < treeView.fixTree.getRowCount(); ++i) {
 assertTrue(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(0, i))));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 for (int i = 0; i < treeView.fixTree.getRowCount(); ++i) {
 assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(0, i))));
 }
 }

 @Test
 public void testSearch() {
 String searchString = "256";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
 int numberOfNodesFound = 0;
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(i);
 for (int j = 0; j < dictionaryNode.getChildCount(); ++j) {
 FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
 FixInfo fixInfo = (FixInfo) messageNode.getUserObject();
 if (fixInfo instanceof FieldInfo) {
 FieldInfo fieldInfo = (FieldInfo) fixInfo;
 if (fieldInfo.getTagNumber().matches(searchString)) {
 numberOfNodesFound++;
 }
 }
 }
 }
 assertEquals("Number of nodes found: " + numberOfNodesFound, numberOfNodesFound, 2);
 }

 @Test
 public void testClear() {
 treeView.searchTextField.setText("256");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();
 int numberOfNodesFound = 0;
 for (int i = 0; i < treeView.fixTree.getRowCount(); ++i) {
 FixInfoTreeNode node = (FixInfoTreeNode) treeView.fixTree.getModel().getPathForLocation(i, 0);
 assertFalse(node.isHighlighted());
 }
 }

 @Test
 public void testKeyEvent() {
 treeView.searchTextField.setText("256");
 TreePath path = treeView.fixTree.getPathForLocation(0, 0);
 TreeModel model = treeView.fixTree.getModel();
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getPathForRow(0, path);
 treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
 treeView.fixTree.requestFocus();
 treeView.fixTree.dispatchEvent(new KeyEvent(treeView.fixTree, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
 assertEquals("Number of selected nodes: " + treeView.fixTree.getSelectionModel().getLeadSelectionPath().getLastPathComponent().getPathCount(), 1, 1);
 }
}