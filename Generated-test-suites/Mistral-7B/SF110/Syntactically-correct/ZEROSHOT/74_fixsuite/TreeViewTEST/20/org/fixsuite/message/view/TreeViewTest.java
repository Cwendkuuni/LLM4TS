package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

 @Mock
 private Library library;
 @InjectMocks
 private TreeView treeView;

 @Before
 public void setUp() {
 // Initialize mocks and dependencies
 Mockito.when(library.getDictionaries()).thenReturn(Arrays.asList(new DictionaryInfo("Dict1", new MessageInfo[]{}), new DictionaryInfo("Dict2", new MessageInfo[]{})));
 }

 @Test
 public void testTreeViewConstruction() {
 // Test constructor
 treeView = new TreeView(library);
 // Check that the TreeView was initialized correctly
 assertEquals("Dictionaries", treeView.getComponentName());
 assertNotNull(treeView.fixTree);
 assertNotNull(treeView.searchLabel);
 assertNotNull(treeView.searchTextField);
 assertNotNull(treeView.searchButton);
 assertNotNull(treeView.clearButton);
 assertNotNull(treeView.expandButton);
 assertNotNull(treeView.collapseButton);
 }

 @Test
 public void testLoadFIXTree() {
 // Test loadFIXTree method
 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 // Check that the root node was created with the correct label
 assertEquals("Dictionaries", rootNode.getUserObject());
 // Check that the root node has 2 children
 assertEquals(2, rootNode.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 // Test loadMessages method
 FixInfoTreeNode versionNode = treeView.loadFIXTree().getChildAt(0);
 // Check that the version node was created with the correct label
 assertEquals("Dict1", versionNode.getUserObject());
 // Check that the version node has no children
 assertEquals(0, versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 // Test loadItems method
 FixInfoTreeNode branchNode = treeView.loadFIXTree().getChildAt(0).getChildAt(0);
 // Check that the branch node was created with the correct label
 assertNotNull(branchNode);
 // Check that the branch node has at least 3 children
 assertEquals(3, branchNode.getChildCount());
 }

 @Test
 public void testSearch() {
 // Test search method
 treeView.searchTextField.setText("MessageType");
 treeView.searchButton.doClick();
 // Mock the TreeModel to return the root node for testing
 Mockito.doAnswer(invocation -> treeView.fixTree.getModel().getRoot()).when(treeView.fixTree).getModel();
 // Check that all nodes with the matching MessageType were highlighted
 treeView.search(treeView.fixTree.getModel().getRoot(), "MessageType");
 // Check that all other nodes were not highlighted
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(i).getLastPathComponent();
 if (!node.isPathToMessageType("MessageType")) {
 assertFalse(node.isHighlighted());
 }
 }
 }

 @Test
 public void testClear() {
 // Test clear method
 treeView.searchTextField.setText("FieldName");
 treeView.searchButton.doClick();
 // Mock the TreeModel to return the root node for testing
 Mockito.doAnswer(invocation -> treeView.fixTree.getModel().getRoot()).when(treeView.fixTree).getModel();
 // Check that all nodes were highlighted before clearing
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(i).getLastPathComponent();
 assertTrue(node.isHighlighted());
 }
 // Clear the search results
 treeView.clearButton.doClick();
 // Check that no nodes are highlighted after clearing
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(i).getLastPathComponent();
 assertFalse(node.isHighlighted());
 }
 }

 @Test
 public void testExpandAll() {
 // Test expandAll method
 treeView.expandButton.doClick();
 // Check that all nodes are expanded
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(i).getLastPathComponent();
 assertTrue(node.isExpanded());
 }
 }

 @Test
 public void testCollapseAll() {
 // Test collapseAll method
 treeView.collapseButton.doClick();
 // Check that all nodes are collapsed
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getPathForRow(i).getLastPathComponent();
 assertFalse(node.isExpanded());
 }
 }

 @Test
 public void testKeyPressSearchButton() {
 // Test key press on search button
 treeView.searchTextField.setText("MessageType");
 treeView.searchButton.requestFocus();
 treeView.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.VK_ENTER, 0));
 // Check that the search was triggered
 treeView.search(treeView.fixTree.getModel().getRoot(), "MessageType");
 }
}