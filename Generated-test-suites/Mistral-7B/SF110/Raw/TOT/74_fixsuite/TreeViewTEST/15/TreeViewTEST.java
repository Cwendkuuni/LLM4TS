package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private Library library;
 private TreeView treeView;
 private TreeCellRenderer renderer;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 renderer = treeView.getFixTree().getCellRenderer();
 }

 @Test
 public void testInitGUI() {
 assertEquals(new BorderLayout(), treeView.getLayout());
 assertTrue(treeView.mainScrollPane != null);
 assertTrue(treeView.fixTree != null);
 assertTrue(treeView.searchLabel != null);
 assertTrue(treeView.searchTextField != null);
 assertTrue(treeView.searchButton != null);
 assertTrue(treeView.clearButton != null);
 assertTrue(treeView.expandButton != null);
 assertTrue(treeView.collapseButton != null);
 assertTrue(treeView.toolbar != null);
 }

 @Test
 public void testLoadFIXTree() {
 final List<DictionaryInfo> dictionaries = library.getDictionaries();
 final int numberOfDictionaries = dictionaries.size();
 final FixInfoTreeNode root = treeView.loadFIXTree();
 assertEquals(numberOfDictionsaries, root.getChildCount());
 for (int i = 0; i < numberOfDictionsaries; ++i) {
 final DictionaryInfo dictionary = dictionaries.get(i);
 final FixInfoTreeNode dictionaryNode = (FixInfoTreeNode)treeView.findChild(root, dictionary.getVersion());
 assertSame(dictionary, dictionaryNode.getUserObject());
 }
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = library.getDictionaries().get(0);
 final int numberOfMessages = dictionary.getMessages().size();
 final FixInfoTreeNode versionNode = (FixInfoTreeNode)treeView.findChild(treeView.loadFIXTree(), dictionary.getVersion());
 assertEquals(numberOfMessages, versionNode.getChildCount());
 for (int i = 0; i < numberOfMessages; ++i) {
 final MessageInfo message = dictionary.getMessages().get(i);
 final FixInfoTreeNode messageNode = (FixInfoTreeNode)treeView.findChild(versionNode, message);
 assertSame(message, messageNode.getUserObject());
 }
 }

 @Test
 public void testLoadItems() {
 final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType", Arrays.asList(new FieldInfo("TestField", 1), new FieldInfo("TestField2", 2)));
 final FixInfoTreeNode leafNode = treeView.loadItems(new FixInfoTreeNode((Object)"Test"), message, 0);
 assertEquals(2, leafNode.getChildCount());
 for (int i = 0; i < 2; ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)leafNode.getChildAt(i);
 assertSame(i < 1 ? (FieldInfo)message.getItems().get(0) : (FieldInfo)message.getItems().get(1), node.getUserObject());
 }
 }

 @Test
 public void testExpandAll() {
 final FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.expandAll();
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 treeView.fixTree.expandRow(row);
 }
 assertEquals(true, treeView.fixTree.isExpanded(new TreePath(root.getPath())));
 }

 @Test
 public void testCollapseAll() {
 final FixInfoTreeNode root = treeView.loadFIXTree();
 treeView.collapseAll();
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 treeView.fixTree.collapseRow(row);
 }
 assertEquals(true, treeView.fixTree.isCollapsed(new TreePath(root.getPath())));
 }

 @Test
 public void testSearch() {
 final String searchString = "Test";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 final FixInfoTreeNode root = treeView.loadFIXTree();
 final List<String> expectedMatches = Arrays.asList("TestMessage", "TestField", "TestField2");
 final int numberOfMatches = root.getChildCount();
 for (int i = 0; i < numberOfMatches; ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)root.getChildAt(i);
 if (node.getUserObject() instanceof MessageInfo && ((MessageInfo)node.getUserObject()).getName().matches(searchString)
 || ((MessageInfo)node.getUserObject()).getMessageType().matches(searchString)) {
 expectedMatches.remove(Collections.singletonList(node.getUserObject().toString()));
 node.setHighlighted(true);
 treeView.fixTree.expandPath(new TreePath(node.getPath()));
 }
 }
 assertEquals(0, expectedMatches.size());
 }

 @Test
 public void testClear() {
 final String searchString = "Test";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 final FixInfoTreeNode root = treeView.loadFIXTree();
 for (int i = 0; i < root.getChildCount(); ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)root.getChildAt(i);
 if (node.isHighlighted()) {
 node.setHighlighted(false);
 }
 }
 }

 @Test
 public void testFindChild() {
 final DictionaryInfo dictionary = library.getDictionaries().get(0);
 final FixInfoTreeNode dictionaryNode = (FixInfoTreeNode)treeView.findChild(treeView.loadFIXTree(), dictionary.getVersion());
 assertSame(dictionary, dictionaryNode.getUserObject());
 final MessageInfo message = dictionary.getMessages().get(0);
 final FixInfoTreeNode messageNode = (FixInfoTreeNode)treeView.findChild(dictionaryNode, message);
 assertSame(message, messageNode.getUserObject());
 final FieldInfo field = message.getItems().get(0);
 final FixInfoTreeNode fieldNode = (FixInfoTreeNode)treeView.findChild(messageNode, field);
 assertSame(field, fieldNode.getUserObject());
 }

 @Test
 public void testGetTreeCellRenderer() {
 assertEquals(renderer, treeView.getFixTree().getCellRenderer());
 }

 @Test
 public void testGetModel() {
 assertEquals(new DefaultTreeModel(treeView.loadFIXTree()), treeView.getFixTree().getModel());
 }
}