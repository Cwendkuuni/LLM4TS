package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

 private TreeView treeView;
 private DefaultMutableTreeNode rootNode;

 @Before
 public void setup() {
 library = new Library();
 treeView = new TreeView(library);
 rootNode = new DefaultMutableTreeNode("Dictionaries");
 treeView.loadFIXTree(rootNode);
 }

 @Test
 public void testLoadFIXTree() {
 // Test with empty library
 Library emptyLibrary = new Library();
 TreeView emptyTreeView = new TreeView(emptyLibrary);
 DefaultMutableTreeNode emptyRootNode = new DefaultMutableTreeNode("Dictionaries");
 emptyTreeView.loadFIXTree(emptyRootNode);
 assertEquals(0, emptyRootNode.getChildCount());

 // Test with non-empty library
 DictionaryInfo dict1 = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "Type1"), new MessageInfo("Msg2", "Type2")));
 DictionaryInfo dict2 = new DictionaryInfo("Dict2", Arrays.asList(new MessageInfo("Msg3", "Type3"), new MessageInfo("Msg4", "Type4")));
 library.addDictionary(dict1);
 library.addDictionary(dict2);

 // Test loading root node
 DefaultMutableTreeNode expectedRootNode = new DefaultMutableTreeNode("Dictionaries");
 expectedRootNode.add(new DefaultMutableTreeNode("Dict1_v1.0"));
 expectedRootNode.add(new DefaultMutableTreeNode("Dict2_v1.0"));
 assertEquals("Root node is not loaded correctly", expectedRootNode, rootNode);

 // Test loading messages
 MessageInfo msg1 = new MessageInfo("Msg1", "Type1");
 MessageInfo msg2 = new MessageInfo("Msg2", "Type2");
 MessageInfo msg3 = new MessageInfo("Msg3", "Type3");
 MessageInfo msg4 = new MessageInfo("Msg4", "Type4");

 // Test loading Dict1
 DefaultMutableTreeNode dict1Node = (DefaultMutableTreeNode)findNode(rootNode, "Dict1_v1.0");
 assertEquals(2, dict1Node.getChildCount());
 DefaultMutableTreeNode msg1Node = (DefaultMutableTreeNode)findNode(dict1Node, "Msg1");
 DefaultMutableTreeNode msg2Node = (DefaultMutableTreeNode)findNode(dict1Node, "Msg2");
 assertEquals("Message node is not loaded correctly for Dict1", msg1, msg1Node.getUserObject());
 assertEquals("Message node is not loaded correctly for Dict1", msg2, msg2Node.getUserObject());

 // Test loading Dict2
 DefaultMutableTreeNode dict2Node = (DefaultMutableTreeNode)findNode(rootNode, "Dict2_v1.0");
 assertEquals(2, dict2Node.getChildCount());
 DefaultMutableTreeNode msg3Node = (DefaultMutableTreeNode)findNode(dict2Node, "Msg3");
 DefaultMutableTreeNode msg4Node = (DefaultMutableTreeNode)findNode(dict2Node, "Msg4");
 assertEquals("Message node is not loaded correctly for Dict2", msg3, msg3Node.getUserObject());
 assertEquals("Message node is not loaded correctly for Dict2", msg4, msg4Node.getUserObject());
 }

 @Test
 public void testSearch() {
 // Test searching for a message by name
 MessageInfo msg1 = new MessageInfo("Msg1", "Type1");
 MessageInfo msg2 = new MessageInfo("Msg2", "Type2");
 MessageInfo msg3 = new MessageInfo("Msg3", "Type3");
 MessageInfo msg4 = new MessageInfo("Msg4", "Type4");
 library.addDictionary(new DictionaryInfo("Dict1", Arrays.asList(msg1, msg2)));
 library.addDictionary(new DictionaryInfo("Dict2", Arrays.asList(msg3, msg4)));
 treeView.loadFIXTree(rootNode);

 String searchString = "Msg1";
 TreePath expectedPath = new TreePath(new Object[] { rootNode, new DefaultMutableTreeNode("Dict1_v1.0"), msg1Node });
 treeView.search(rootNode, searchString);
 assertEquals("Search result is not correct", expectedPath, treeView.fixTree.getSelectionPath());

 // Test searching for a message by message type
 searchString = "Type1";
 expectedPath = new TreePath(new Object[] { rootNode, new DefaultMutableTreeNode("Dict1_v1.0"), msg1Node });
 treeView.search(rootNode, searchString);
 assertEquals("Search result is not correct", expectedPath, treeView.fixTree.getSelectionPath());

 // Test searching for a non-existing message
 searchString = "NonExistingMsg";
 assertEquals("Search should not find a non-existing message", null, treeView.fixTree.getSelectionPath());
 }

 @Test
 public void testClear() {
 // Test clearing the search results
 TreePath expectedPath = new TreePath(new Object[] { rootNode });
 treeView.clear(rootNode);
 assertEquals("Search results are not cleared correctly", expectedPath, treeView.fixTree.getSelectionPath());
 }

 private Object findNode(DefaultMutableTreeNode parent, String name) {
 for (int i = 0; i < parent.getChildCount(); ++i) {
 DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getChildAt(i);
 if (node.getUserObject().toString().equals(name)) {
 return node;
 }
 }
 return null;
}
}