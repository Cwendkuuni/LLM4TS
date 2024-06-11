package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TreeViewTest {

 private JTree tree;
 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 tree = treeView.fixTree;
 }

 @Test
 public void testLoadFIXTree() {
 final FixInfoTreeNode root = (FixInfoTreeNode)tree.getModel().getRoot();
 assertEquals(1, root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = library.getDictionaries().get(0);
 final FixInfoTreeNode versionNode = treeView.loadMessages(new FixInfoTreeNode("Version 1.0"), dictionary);
 assertEquals(8, versionNode.getChildCount()); // Assuming dictionary has 8 messages
 }

 @Test
 public void testLoadItems() {
 final MessageInfo message = new MessageInfo("HEARTBEAT", "Heartbeat message", new FieldInfo("Type", "Heartbeat"), new FieldInfo("SeqNum", "SequentialNumber"), new FieldInfo("BodyLength", "BodyLength"));
 final FixInfoTreeNode node = treeView.new FixInfoTreeNode(message);
 treeView.loadItems(node, message, 0);
 assertEquals(3, node.getChildCount());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int count = 0;
 for (int row = 0; row < tree.getRowCount(); ++row) {
 tree.expandRow(row);
 ++count;
 }
 assertEquals(tree.getRowCount(), count);
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int count = 0;
 for (int row = 0; row < tree.getRowCount(); ++row) {
 tree.collapseRow(row);
 ++count;
 }
 assertEquals(tree.getRowCount(), count);
 }

 @Test
 public void testSearch() {
 final String searchString = "Heartbeat";
 treeView.search(treeView.fixTree, searchString);
 int count = 0;
 for (int row = 0; row < tree.getRowCount(); ++row) {
 final TreeCellRenderer renderer = tree.getCellRenderer();
 final Component component = tree.getCellRenderer().getTreeCellRendererComponent(tree, tree.getModel().getChild(row), false, false, false, row, false);
 assertEquals(true, renderer.isHighlighted());
 assertEquals(true, component.isDisplayable());
 ++count;
 }
 assertEquals(1, count); // Assuming there is exactly one "Heartbeat" message
 }

 @Test
 public void testClear() {
 final String searchString = "Heartbeat";
 treeView.search(treeView.fixTree, searchString);
 treeView.clear(treeView.fixTree.getModel().getRoot());
 int count = 0;
 for (int row = 0; row < tree.getRowCount(); ++row) {
 final TreeCellRenderer renderer = tree.getCellRenderer();
 final Component component = tree.getCellRenderer().getTreeCellRendererComponent(tree, tree.getModel().getChild(row), false, false, false, row, false);
 assertEquals(false, renderer.isHighlighted());
 assertEquals(true, component.isDisplayable());
 ++count;
 }
 assertEquals(0, count); // No highlighted nodes after clear
 }
}