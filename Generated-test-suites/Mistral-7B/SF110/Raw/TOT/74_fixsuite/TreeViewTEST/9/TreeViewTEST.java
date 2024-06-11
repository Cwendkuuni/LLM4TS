import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode root = treeView.loadFIXTree();
 assertNotNull(root);
 assertEquals("Dictionaries", root.getUserObject());
 }

 @Test
 public void testLoadMessages() {
 FixInfoTreeNode versionNode = new FixInfoTreeNode("1.0");
 treeView.loadMessages(versionNode, new DictionaryInfo("1.0", Arrays.asList(new MessageInfo("Message1", "MessageType1"))));
 assertEquals(1, versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 FixInfoTreeNode branchNode = new FixInfoTreeNode("Message1");
 treeView.loadItems(branchNode, new CompositeFixInfo(Arrays.asList(new MessageInfo("Message1.1", "MessageType1.1"), new MessageInfo("Message1.2", "MessageType1.2"))), 0);
 assertEquals(2, branchNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 assertTrue(treeView.fixTree.isExpanded(treeView.fixTree.getRowCount() - 1));
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 assertFalse(treeView.fixTree.isExpanded(treeView.fixTree.getRowCount() - 1));
 }

 @Test
 public void testSearch() {
 String searchString = "Message1";
 treeView.search(treeView.fixTree.getModel().getRoot(), searchString);
 TreePath searchedNode = null;
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 TreePath nodePath = treeView.fixTree.getModel().getPathToRoot(treeView.fixTree.getRowForPath(new TreePath(i)));
 if (nodePath.getLastPathComponent().toString().equals("Message1")) {
 searchedNode = new TreePath(nodePath);
 break;
 }
 }
 assertNotNull(searchedNode);
 assertEquals(searchString, treeView.fixTree.getModel().getValueAt(treeView.fixTree.getRowForPath(searchedNode), 0).toString());
 }

 @Test
 public void testClear() {
 String searchString = "Message1";
 treeView.search(treeView.fixTree.getModel().getRoot(), searchString);
 treeView.clear(treeView.fixTree.getModel().getRoot());
 assertFalse(treeView.fixTree.isRowSelected(treeView.fixTree.getRowCount() - 1));
 }
}