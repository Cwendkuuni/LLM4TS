package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private TreeModel treeModel;

 @Before
 public void setUp() {
 Library library = new Library();
 DictionaryInfo dictionary = new DictionaryInfo("TestDict", null);
 MessageInfo message1 = new MessageInfo("TestMessage1", "1", Arrays.asList(new FieldInfo("TestField1", 1), new FieldInfo("TestField2", 2)));
 MessageInfo message2 = new MessageInfo("TestMessage2", "2", Arrays.asList(new FieldInfo("TestField3", 3), new FieldInfo("TestField4", 4)));
 ComponentInfo component = new ComponentInfo("TestComponent", "TestComponentName");
 dictionary.addMessage(message1);
 dictionary.addMessage(message2);
 library.addDictionary(dictionary);

 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 treeModel = treeView.getFixTree().getModel();
 }

 @Test
 public void testLoadFIXTree() {
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
 assertEquals("Dictionaries", root.getUserObject());
 assertFalse(root.isLeaf());

 DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) root.getChildAt(0);
 assertEquals("TestDict", versionNode.getUserObject());
 assertFalse(versionNode.isLeaf());

 DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) versionNode.getChildAt(0);
 assertEquals("TestMessage1", messageNode1.getUserObject());
 assertFalse(messageNode1.isLeaf());

 DefaultMutableTreeNode fieldNode1 = (DefaultMutableTreeNode) messageNode1.getChildAt(0);
 assertEquals("TestField1", fieldNode1.getUserObject());
 assertTrue(fieldNode1.isLeaf());

 DefaultMutableTreeNode fieldNode2 = (DefaultMutableTreeNode) messageNode1.getChildAt(1);
 assertEquals("TestField2", fieldNode2.getUserObject());
 assertTrue(fieldNode2.isLeaf());

 DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) versionNode.getChildAt(1);
 assertEquals("TestMessage2", messageNode2.getUserObject());
 assertFalse(messageNode2.isLeaf());

 DefaultMutableTreeNode fieldNode3 = (DefaultMutableTreeNode) messageNode2.getChildAt(0);
 assertEquals("TestField3", fieldNode3.getUserObject());
 assertTrue(fieldNode3.isLeaf());

 DefaultMutableTreeNode fieldNode4 = (DefaultMutableTreeNode) messageNode2.getChildAt(1);
 assertEquals("TestField4", fieldNode4.getUserObject());
 assertTrue(fieldNode4.isLeaf());

 DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) versionNode.getChildAt(2);
 assertEquals("TestComponent", componentNode.getUserObject());
 assertTrue(componentNode.isLeaf());
 }

 @Test
 public void testSearch() {
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
 String searchString = "TestField";

 treeView.search(root, searchString);

 DefaultMutableTreeNode testNode = null;
 for (int i = 0; i < root.getChildCount(); i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 if ("TestField1".equals(node.getUserObject().toString()) || "TestField2".equals(node.getUserObject().toString()) || "TestField3".equals(node.getUserObject().toString()) || "TestField4".equals(node.getUserObject().toString())) {
 testNode = node;
 break;
 }
 }

 assertTrue(testNode != null);
 assertTrue(testNode.isHighlighted());
 }

 @Test
 public void testClear() {
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
 String searchString = "TestField";

 treeView.search(root, searchString);
 treeView.clear(root);

 DefaultMutableTreeNode testNode = null;
 for (int i = 0; i < root.getChildCount(); i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 if (node.isLeaf()) {
 if (node.getUserObject() instanceof FieldInfo) {
 String fieldName = ((FieldInfo) node.getUserObject()).getName();
 if (fieldName.matches(searchString)) {
 testNode = node;
 break;
 }
 }
 }
 }

 assertFalse(testNode != null);
 assertFalse(testNode.isHighlighted());
 }

 @Test
 public void testExpandAndCollapse() {
 DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

 treeView.expandAll();
 assertTrue(root.isExpanded());
 for (int i = 0; i < root.getChildCount(); i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 assertTrue(node.isExpanded());
 }

 treeView.collapseAll();
 for (int i = 0; i < root.getChildCount(); i++) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
 assertFalse(node.isExpanded());
 }
 }

 @Test
 public void testKeyEvents() {
 fixTree.requestFocus();
 treeView.getFixTree().addTreeSelectionListener(e -> {
 // This listener is not tested here
 });

 treeView.getSearchTextField().requestFocus();
 treeView.getSearchTextField().sendKeyEvent(new KeyEvent(treeView.getSearchTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
 treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), ActionEvent.ACTION_PERFORMED, null));
 }
}