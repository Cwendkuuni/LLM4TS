package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library libraryMock;

 @Before
 public void setUp() {
 libraryMock = Mockito.mock(Library.class);
 treeView = new TreeView(libraryMock);
 }

 @Test
 public void testInitGUI() {
 // Check if the JPanel has the expected layout
 assertEquals("Unexpected layout", new BorderLayout.LayoutManager(), treeView.getLayout());

 // Check if the JScrollPane exists and has the expected size
 Assert.assertNotNull("JScrollPane not initialized", treeView.mainScrollPane);
 Dimension expectedDimension = new Dimension(300, 400);
 assertEquals("Unexpected JScrollPane size", expectedDimension, treeView.mainScrollPane.getPreferredSize());

 // Check if the fixTree exists
 Assert.assertNotNull("JTree not initialized", treeView.fixTree);

 // Check if the searchLabel exists
 Assert.assertNotNull("JLabel not initialized", treeView.searchLabel);

 // Check if the searchTextField exists
 Assert.assertNotNull("JTextField not initialized", treeView.searchTextField);

 // Check if the searchButton exists
 Assert.assertNotNull("JButton not initialized", treeView.searchButton);

 // Check if the clearButton exists
 Assert.assertNotNull("JButton not initialized", treeMock.clearButton);

 // Check if the expandButton exists
 Assert.assertNotNull("JButton not initialized", treeView.expandButton);

 // Check if the collapseButton exists
 Assert.assertNotNull("JButton not initialized", treeView.collapseButton);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
 int numberOfNodes = 0;
 // Check if the root node exists and has the correct name
 assertEquals("Unexpected root node name", "Dictionaries", rootNode.getUserObject());

 // Recursively check the number of nodes in the tree
 numberOfNodes += rootNode.getChildCount();
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(i);
 numberOfNodes += testLoadMessages(childNode, 1);
 }
 // Check if the correct number of nodes have been loaded
 assertEquals("Unexpected number of nodes loaded", 10, numberOfNodes);
 }

 @Test
 public int testLoadMessages(FixInfoTreeNode parentNode, int depth) {
 int numberOfNodes = 0;
 // Check if the parent node has the correct name (depth should be displayed as a number before the name)
 if (parentNode != null) {
 String expectedName = "FixMsgType_" + depth + " (" + parentNode.getUserObject().getClass().getSimpleName() + ")";
 assertEquals("Unexpected node name at depth " + depth, expectedName, parentNode.getUserObject().toString());
 }

 for (int i = 0; i < parentNode.getChildCount(); i++) {
 FixInfoTreeNode childNode = (FixInfoTreeNode) parentNode.getChildAt(i);
 numberOfNodes += testLoadItems(childNode, 0);
 }
 return numberOfNodes;
 }

 @Test
 public int testLoadItems(FixInfoTreeNode branchNode, int level) {
 int numberOfNodes = 0;
 // Check if the branch node has the correct name (level should be displayed as a number before the name)
 if (branchNode != null) {
 String expectedName = "FixComp_" + level + " (" + branchNode.getUserObject().getClass().getSimpleName() + ")";
 assertEquals("Unexpected node name at level " + level, expectedName, branchNode.getUserObject().toString());
 }

 for (int i = 0; i < branchNode.getChildCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) branchNode.getChildAt(i);
 numberOfNodes++;
 if (node.getUserObject() instanceof FixInfo) {
 // Check if FixInfo nodes have the correct number of children (0, 1 or more)
 int childCount = ((FixInfo) node.getUserObject()).getItems().size();
 if (childCount > 0) {
 numberOfNodes += testLoadItems((FixInfoTreeNode) node.getChildAt(0), level + 1);
 }
 }
 }
 return numberOfNodes;
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 // Check if all nodes are expanded
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertTrue("Node not expanded", treeView.fixTree.isExpanded(new TreePath(new Object[] { treeView.fixTree.getModel().getRoot(), treeView.fixTree.getModel().getChild(i) })));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 // Check if all nodes are collapsed
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertFalse("Node not collapsed", treeView.fixTree.isExpanded(new TreePath(new Object[] { treeView.fixTree.getModel().getRoot(), treeView.fixTree.getModel().getChild(i) })));
 }
 }

 @Test
 public void testSearch() {
 String searchString = "Test"; // Replace with a search string
 treeView.searchButton.doClick();
 treeView.searchTextField.setText(searchString);
 treeView.search(treeView.fixTree.getModel().getRoot(), treeView.searchTextField.getText().trim());
 // Check if the nodes containing the searchString are highlighted
 for (int i = 0; i < treeView.fixTree.getModel().getRowCount(); i++) {
 TreePath nodePath = treeView.fixTree.getPathForLocation(0, 0);
 Object node = nodePath.getLastPathComponent();
 if (node instanceof FixInfoTreeNode) {
 FixInfoTreeNode fixInfoTreeNode = (FixInfoTreeNode) node;
 if (fixInfoTreeNode.isHighlighted()) {
 FixInfo fixInfo = (FixInfo) fixInfoTreeNode.getUserObject();
 assertTrue("Unexpected node highlighted: " + fixInfo.toString(), fixInfo.toString().contains(searchString));
 }
 }
 }
 }

 @Test
 public void testClear() {
 treeView.searchButton.doClick();
 treeView.searchTextField.setText("Test");
 treeView.search(treeView.fixTree.getModel().getRoot(), treeView.searchTextField.getText().trim());
 treeView.clearButton.doClick();
 // Check if no nodes are highlighted after a clear search
 for (int i = 0; i < treeView.fixTree.getModel().getRowCount(); i++) {
 TreePath nodePath = treeView.fixTree.getPathForLocation(0, 0);
 Object node = nodePath.getLastPathComponent();
 if (node instanceof FixInfoTreeNode) {
 FixInfoTreeNode fixInfoTreeNode = (FixInfoTreeNode) node;
 assertFalse("Unexpected node highlighted after clear search: " + fixInfoTreeNode.getUserObject().toString(), fixInfoTreeNode.isHighlighted());
 }
 }
 }
}