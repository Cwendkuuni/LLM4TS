package org.fixsuite.message.view.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private TreeCellRenderer cellRenderer;

 @Before
 public void setUp() {
 // Assuming Library is initialized with some data for testing purposes
 TreeView treeView = new TreeView(new Library());
 this.treeView = treeView;
 this.fixTree = treeView.getFixTree();
 this.cellRenderer = treeView.getFixTree().getCellRenderer();
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.loadFIXTree();
 assertNotNull(rootNode);
 assertFalse(rootNode.isLeaf());
 }

 @Test
 public void testLoadMessages() {
 DictionaryInfo dictionary = new DictionaryInfo("MockDictionary", Arrays.asList(new MessageInfo("MockMessage", "MockMessageType")));
 FixInfoTreeNode versionNode = new FixInfoTreeNode("MockVersion");
 treeView.loadMessages(versionNode, dictionary);

 MessageInfo message = (MessageInfo) versionNode.getChildAt(0).getUserObject();
 assertNotNull(message);
 assertFalse(message.isLeaf());
 }

 @Test
 public void testLoadItems() {
 FixInfo info = new MessageInfo("MockMessage", "MockMessageType");
 CompositeFixInfo compositeInfo = new CompositeFixInfo("MockComponent", Arrays.asList(info));
 FixInfoTreeNode branchNode = new FixInfoTreeNode("MockComponent");
 treeView.loadItems(branchNode, compositeInfo, 0);

 List<FixInfoTreeNode> children = new ArrayList<>();
 for (int i = 0; i < branchNode.getChildCount(); i++) {
 children.add((FixInfoTreeNode) branchNode.getChildAt(i));
 }

 assertFalse(branchNode.isLeaf());
 assertEquals(1, branchNode.getChildCount());
 assertNotNull(children.get(0).getUserObject());
 assertFalse(children.get(0).isLeaf());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int rowCount = fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 assertTrue(fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRoot(), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0) })));
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int rowCount = fixTree.getRowCount();
 for (int i = rowCount - 1; i >= 0; i--) {
 assertFalse(fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRoot(), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0), fixTree.getModel().getChild(0) })));
 }
 }

 @Test
 public void testSearch() {
 final String searchString = "Component";
 treeView.search(treeView.getFixTree().getModel().getRoot(), searchString);
 int rowCount = fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) fixTree.getModel().getChild(0);
 if (node.getUserObject() instanceof ComponentInfo) {
 assertNotNull(node.getUserObject());
 assertTrue(node.isHighlighted());
 } else {
 assertNull(node.getUserObject());
 assertFalse(node.isHighlighted());
 }
 }
 }

 @Test
 public void testClear() {
 final String searchString = "Component";
 treeView.search(treeView.getFixTree().getModel().getRoot(), searchString);
 int rowCount = fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) fixTree.getModel().getChild(0);
 if (node.getUserObject() instanceof ComponentInfo) {
 assertTrue(node.isHighlighted());
 } else {
 assertFalse(node.isHighlighted());
 }
 treeView.clear(node);
 assertFalse(node.isHighlighted());
 }
 }

 @Test
 public void testKeyPressSearchButton() {
 TreePath path = new TreePath(new Object[] { fixTree.getModel().getRoot() });
 MouseEvent mouseEvent = new MouseEvent(fixTree, 0, System.currentTimeMillis(), 0, 0, 0, 1, false);
 KeyEvent keyEvent = new KeyEvent(fixTree, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
 treeView.valueChanged(new TreeSelectionEvent(fixTree, path, TreeSelectionModel.LEAF_DESCRIPTOR, 0, new Object[] { fixTree.getModel().getChildAt(0).getUserObject() }));
 treeView.actionPerformed(new ActionEvent(fixTree, 0, "Search"));
 fixTree.dispatchEvent(keyEvent);
 treeView.expandAll();
 int rowCount = fixTree.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 FixInfoTreeNode node = (FixInfoTreeNode) fixTree.getModel().getChild(0);
 if (node.getUserObject() instanceof ComponentInfo) {
 assertNotNull(node.getUserObject());
 assertTrue(node.isHighlighted());
 } else {
 assertNull(node.getUserObject());
 assertFalse(node.isHighlighted());
 }
 }
 }

 @Test
 public void testCellRenderer() {
 final Object userObject = new FieldInfo("MockName", 1);
 final Component comp = new JLabel("MockComponent");
 comp.setOpaque(true);
 final TreeCellRenderer renderer = new FixInfoCellRenderer();

 TreeModel treeModel = new DefaultTreeModel(new Object[] { new Object[] {"MockComponent", userObject} });
 JTree tree = new JTree(treeModel);
 tree.setCellRenderer(renderer);
 tree.setRowHeight(20);
 tree.setRootVisible(false);

 TreePath path = new TreePath(new Object[] { treeModel.getRoot(), "MockComponent" });
 tree.expandPath(path);
 tree.setSelectionPath(path);
 tree.validate();

 TreeCellRenderer cell = tree.getCellRenderer();
 JLabel label = (JLabel) cell.getTreeCellRendererComponent(tree, userObject, false, false, false, 0, false);
 assertEquals("MockName (1)", label.getText());
 }
}