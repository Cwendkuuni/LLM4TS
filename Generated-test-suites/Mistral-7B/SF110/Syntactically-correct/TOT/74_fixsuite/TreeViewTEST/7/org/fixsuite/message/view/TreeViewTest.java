package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private JTextField searchTextField;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 searchTextField = treeView.getSearchTextField();
 }

 @Test
 public void testTreeStructure() {
 final int expectedNumberOfNodes = library.size() * 3; // Each dictionary, message and field is a node
 assertEquals(expectedNumberOfNodes, fixTree.getModel().getRowCount());
 }

 @Test
 public void testSearchFunctionality() {
 final String searchString = "FIELD";
 searchTextField.setText(searchString);
 searchTextField.doClick();
 treeView.searchButton.doClick();
 int count = 0;
 for (int i = 0; i < fixTree.getRowCount(); ++i) {
 final TreePath treePath = fixTree.getPathForRow(i);
 final Object lastComponent = treePath.getLastPathComponent();
 if (lastComponent instanceof FixInfoTreeNode && ((FixInfoTreeNode)lastComponent).isHighlighted()) {
 ++count;
 }
 }
 assertEquals("At least one node should be highlighted", 1, count);
 for (final DictionaryInfo dictionary : library.getDictionaries()) {
 for (final MessageInfo message : dictionary.getMessages()) {
 for (final FixInfo fixInfo : message.getItems()) {
 if (fixInfo instanceof FieldInfo) {
 final String fieldName = ((FieldInfo)fixInfo).getName();
 final String fieldTag = "" + ((FieldInfo)fixInfo).getTagNumber();
 assertTrue("Field name or tag should match the search string: " + searchString,
 fieldName.matches(searchString) || fieldTag.matches(searchString));
 }
 }
 }
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSearchWithNullLibrary() {
 treeView = new TreeView(null);
 searchTextField.setText("FIELD");
 searchTextField.doClick();
 treeView.searchButton.doClick();
}

 @Test(expected = IllegalArgumentException.class)
 public void testLoadTreeWithNullLibrary() {
 treeView = new TreeView(null);
}

 @Test
 public void testTreeExpansion() {
 treeView.expandAll();
 for (int i = 0; i < fixTree.getRowCount(); ++i) {
 assertTrue("All nodes should be expanded", fixTree.isExpanded(new TreePath(new Object[]{fixTree.getModel().getRoot(), fixTree.getModel().getChild(i)})));
 }
 treeView.collapseAll();
 for (int i = 0; i < fixTree.getRowCount(); ++i) {
 assertTrue("All nodes should be collapsed", !fixTree.isExpanded(new TreePath(new Object[]{fixTree.getModel().getRoot(), fixTree.getModel().getChild(i)})));
 }
}
}