package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testInitGUI() {
 // Check that the components have been added to the JPanel
 assertTrue(treeView.getComponentCount() > 0);
 JScrollPane mainScrollPane = (JScrollPane) treeView.getComponent(0);
 JTree fixTree = (JTree) mainScrollPane.getViewport().getComponent(0);
 JToolBar toolbar = (JToolBar) treeView.getComponent(1);
 assertNotNull(mainScrollPane);
 assertNotNull(fixTree);
 assertNotNull(toolbar);
 }

 @Test
 public void testLoadFIXTree() {
 // TODO: Test the loading of the FIXTree
 }

 @Test
 public void testLoadMessages() {
 // TODO: Test the loading of messages into the tree
 }

 @Test
 public void testLoadItems() {
 // TODO: Test the loading of items into the tree
 }

 @Test
 public void testExpandAll() {
 // TODO: Test expanding all nodes in the tree
 treeView.expandAll();
 TreeCellRenderer renderer = fixTree.getCellRenderer();
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
 assertFalse(root.isLeaf());
 int numNodes = root.getChildCount();
 for (int i = 0; i < numNodes; i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 assertTrue(node.isExpanded());
 TreePath path = new TreePath(node.getPath());
 Component component = fixTree.getComponentForPath(path);
 assertNotNull(component);
 assertSame(renderer.getTreeCellRendererComponent(fixTree, node.getUserObject(), true, true, false, -1, false).getBackground(), component.getBackground());
 }
 }

 @Test
 public void testCollapseAll() {
 // TODO: Test collapsing all nodes in the tree
 treeView.collapseAll();
 TreeCellRenderer renderer = fixTree.getCellRenderer();
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
 assertFalse(root.isLeaf());
 int numNodes = root.getChildCount();
 for (int i = 0; i < numNodes; i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 assertFalse(node.isExpanded());
 }
 }

 @Test
 public void testSearch() {
 // TODO: Test searching for nodes in the tree
 treeView.searchTextField.setText("TagNumber\\d+");
 treeView.searchButton.doClick();
 TreeCellRenderer renderer = fixTree.getCellRenderer();
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
 int numNodes = root.getChildCount();
 for (int i = 0; i < numNodes; i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 if (node.getUserObject() instanceof FieldInfo) {
 FieldInfo field = (FieldInfo) node.getUserObject();
 String tagNumber = "" + field.getTagNumber();
 String name = field.getName();
 assertTrue(name.matches("TagNumber\\d+") || tagNumber.matches("TagNumber\\d+"));
 TreePath path = new TreePath(node.getPath());
 Component component = fixTree.getComponentForPath(path);
 assertNotNull(component);
 assertSame(renderer.getTreeCellRendererComponent(fixTree, node.getUserObject(), false, false, false, -1, false).getBackground(), component.getBackground());
 node.setHighlighted(true);
 }
 }
 }

 @Test
 public void testClear() {
 // TODO: Test clearing the search results
 treeView.searchTextField.setText("TagNumber\\d+");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();
 assertTrue(treeView.fixTree.getModel().getRowCount() == 0);
 }
}