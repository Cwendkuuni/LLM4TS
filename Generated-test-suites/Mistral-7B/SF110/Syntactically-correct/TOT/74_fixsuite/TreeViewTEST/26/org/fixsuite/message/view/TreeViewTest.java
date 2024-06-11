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
 assertEquals("Dictionaries", root.getUserObject().toString());
 assertNotNull(root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
 treeView.loadMessages(versionNode, new DictionaryInfo("Dictionary 1", new ArrayList<>()));
 assertNotNull(versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 FixInfoTreeNode branchNode = new FixInfoTreeNode("Message 1.1");
 treeView.loadItems(branchNode, new MessageInfo("Message 1.1", new ArrayList<>(), null), 0);
 assertNotNull(branchNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 int count = 0;
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 assertTrue(treeView.fixTree.isExpanded(row));
 count++;
 }
 assertEquals(treeView.fixTree.getRowCount(), count);
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();
 int count = 0;
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 assertFalse(treeView.fixTree.isExpanded(row));
 count++;
 }
 assertEquals(treeView.fixTree.getRowCount(), count);
 }

 @Test
 public void testSearch() {
 FixInfoTreeNode rootNode = treeView.fixTree.getModel().getRoot();
 treeView.search(rootNode, "Message.*");
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getNodeForIndex(row);
 if (node.getUserObject() instanceof MessageInfo) {
 assertTrue(node.isHighlighted());
 }
 }
 }

 @Test
 public void testClear() {
 FixInfoTreeNode rootNode = treeView.fixTree.getModel().getRoot();
 treeView.search(rootNode, "Message.*");
 treeView.clear(rootNode);
 for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getNodeForIndex(row);
 assertFalse(node.isHighlighted());
 }
 }
}