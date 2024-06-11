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
 private Library library;
 private JTree fixTree;
 private JTextField searchTextField;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 searchTextField = treeView.getSearchTextField();
 }

 @Test
 public void testInitGUI() {
 // Check if the JPanel has the correct layout
 assertEquals("Unexpected layout for TreeView", new BorderLayout.LayoutManager(), treeView.getLayout());

 // Check if the mainScrollPane is added to the JPanel
 assertTrue("mainScrollPane not added to TreeView", treeView.getComponentCount() > 0);
 JComponent mainComponent = (JComponent) treeView.getComponent(0);
 assertEquals("mainScrollPane not initialized correctly", mainScrollPane.class, mainComponent.getClass());

 // Check if the JTree is added to the mainScrollPane
 assertTrue("fixTree not added to mainScrollPane", mainComponent.getComponentCount() > 0);
 JComponent treeComponent = (JComponent) mainComponent.getComponent(0);
 assertEquals("fixTree not initialized correctly", JTree.class, treeComponent.getClass());

 // Check if the searchLabel, searchTextField, searchButton, clearButton, expandButton, and collapseButton are added to the toolbar
 JToolBar toolbar = (JToolBar) treeView.getComponent(1);
 assertTrue("searchLabel not added to Toolbar", toolbar.getComponentCount() > 0);
 JComponent searchLabelComponent = (JComponent) toolbar.getComponent(0);
 assertEquals("searchLabel not initialized correctly", JLabel.class, searchLabelComponent.getClass());
 assertEquals("Unexpected label text for searchLabel", "Search ", searchLabelComponent.getText());

 assertTrue("searchTextField not added to Toolbar", toolbar.getComponentCount() > 1);
 JComponent searchTextFieldComponent = (JComponent) toolbar.getComponent(1);
 assertEquals("searchTextField not initialized correctly", JTextField.class, searchTextFieldComponent.getClass());
 assertEquals("Unexpected initial text for searchTextField", "", searchTextFieldComponent.getText());

 assertTrue("searchButton not added to Toolbar", toolbar.getComponentCount() > 2);
 JComponent searchButtonComponent = (JComponent) toolbar.getComponent(2);
 assertEquals("searchButton not initialized correctly", JButton.class, searchButtonComponent.getClass());
 assertEquals("Unexpected label text for searchButton", "Go", searchButtonComponent.getText());

 assertTrue("clearButton not added to Toolbar", toolbar.getComponentCount() > 3);
 JComponent clearButtonComponent = (JComponent) toolbar.getComponent(3);
 assertEquals("clearButton not initialized correctly", JButton.class, clearButtonComponent.getClass());

 assertTrue("expandButton not added to Toolbar", toolbar.getComponentCount() > 4);
 JComponent expandButtonComponent = (JComponent) toolbar.getComponent(4);
 assertEquals("expandButton not initialized correctly", JButton.class, expandButtonComponent.getClass());

 assertTrue("collapseButton not added to Toolbar", toolbar.getComponentCount() > 5);
 JComponent collapseButtonComponent = (JComponent) toolbar.getComponent(5);
 assertEquals("collapseButton not initialized correctly", JButton.class, collapseButtonComponent.getClass());
 }

 @Test
 public void testLoadFIXTree() {
 // Test if loadFIXTree method is called correctly in the constructor
 TreeModel treeModel = fixTree.getModel();
 assertEquals("Unexpected number of nodes in the tree model after loading FIXTree", library.getDictionaries().size() + 1, treeModel.getChildCount(treeModel.getRoot()));

 // Check if the root node has the correct name
 Object root = treeModel.getChild(treeModel.getRoot(), 0);
 assertEquals("Unexpected name for the root node", "Dictionaries", root.toString());

 // Check if the first dictionary node and its children are loaded correctly
 Object dictionaryNode = treeModel.getChild(root, 0);
 assertEquals("Unexpected name for the first dictionary node", library.getDictionaries().get(0).getVersion(), dictionaryNode.toString());

 List<MessageInfo> messages = library.getDictionaries().get(0).getMessages();
 for (MessageInfo message : messages) {
 Object messageNode = treeModel.getChild(dictionaryNode, treeModel.getIndexOfChild(dictionaryNode, dictionaryNode));
 assertEquals("Unexpected name for a message node", message.getName(), messageNode.toString());
 }
 }

 @Test
 public void testExpandAll() {
 // Test expandAll method
 treeView.expandAll();
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertTrue("Node at row " + i + " was not expanded", fixTree.isExpanded(new TreePath(new Object[] {fixTree.getModel().getRoot(), fixTree.getModel().getChild(fixTree.getModel().getRoot(), i)})));
 }
 }

 @Test
 public void testCollapseAll() {
 // Test collapseAll method
 treeView.collapseAll();
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertFalse("Node at row " + i + " was not collapsed", fixTree.isExpanded(new TreePath(new Object[] {fixTree.getModel().getRoot(), fixTree.getModel().getChild(fixTree.getModel().getRoot(), i)})));
 }
 }

 @Test
 public void testSearch() {
 // Test search method
 String searchString = "Message";
 searchTextField.setText(searchString);
 searchButton.doClick();

 // Check if all nodes are cleared before the search
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertFalse("Node at row " + i + " was not cleared before the search", ((DefaultMutableTreeNode) fixTree.getModel().getNodeAt(i)).isHighlighted());
 }

 // Check if the nodes matching the searchString are highlighted
 for (MessageInfo message : library.getDictionaries().get(0).getMessages()) {
 if (message.getName().matches(searchString) || message.getMessageType().matches(searchString)) {
 TreePath nodePath = new TreePath(new Object[] {fixTree.getModel().getRoot(), findTreeNode(fixTree.getModel().getRoot(), message), message});
 assertTrue("Node for message " + message + " was not highlighted", ((DefaultMutableTreeNode)nodePath.getLastPathComponent()).isHighlighted());
 }
 }

 for (DictionaryInfo dictionary : library.getDictionaries()) {
 if (dictionary.getName().matches(searchString)) {
 TreePath nodePath = new TreePath(new Object[] {fixTree.getModel().getRoot(), findTreeNode(fixTree.getModel().getRoot(), dictionary), dictionary});
 assertTrue("Node for dictionary " + dictionary + " was not highlighted", ((DefaultMutableTreeNode)nodePath.getLastPathComponent()).isHighlighted());
 }
 }

 // Check if the nodes not matching the searchString are not highlighted
 for (int i = 0; i < library.getDictionaries().size(); i++) {
 DictionaryInfo dictionary = library.getDictionaries().get(i);
 for (MessageInfo message : dictionary.getMessages()) {
 if (!message.getName().matches(searchString) && !message.getMessageType().matches(searchString)) {
 TreePath nodePath = new TreePath(new Object[] {fixTree.getModel().getRoot(), findTreeNode(fixTree.getModel().getRoot(), dictionary), findTreeNode(findTreeNode(fixTree.getModel().getRoot(), dictionary), message), message});
 assertFalse("Node for message " + message + " was highlighted although it shouldn't be", ((DefaultMutableTreeNode)nodePath.getLastPathComponent()).isHighlighted());
 }
 }
 }
 }

 @Test
 public void testClear() {
 // Test clear method
 String searchString = "Message";
 searchTextField.setText(searchString);
 searchButton.doClick();

 // Check if all nodes are highlighted after the search
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertTrue("Node at row " + i + " was not highlighted after the search", ((DefaultMutableTreeNode) fixTree.getModel().getNodeAt(i)).isHighlighted());
 }

 // Test clear method
 clearButton.doClick();

 // Check if all nodes are cleared after the clear
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertFalse("Node at row " + i + " was not cleared after the clear", ((DefaultMutableTreeNode) fixTree.getModel().getNodeAt(i)).isHighlighted());
 }
 }

 private Object findTreeNode(Object parent, Object node) {
 for (int i = 0; i < ((DefaultMutableTreeNode)parent).getChildCount(); i++) {
 if (((DefaultMutableTreeNode)parent).getChildAt(i).getUserObject().equals(node)) {
 return parent;
 }
 DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getChildAt(i);
 Object result = findTreeNode(child, node);
 if (result != null) {
 return result;
 }
 }
 return null;
 }
}