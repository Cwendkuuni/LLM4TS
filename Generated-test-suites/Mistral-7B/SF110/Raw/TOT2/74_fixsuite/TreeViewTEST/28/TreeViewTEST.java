import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import javax.swing.tree.*;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private Library library;

 @Before
 public void setUp() {
 library = new Library(); // Assuming Library has proper setup
 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode)fixTree.getModel().getRoot();
 assertEquals("Dictionaries", rootNode.getUserObject());
 }

 @Test
 public void testLoadMessages() {
 // Mock DictionaryInfo and MessageInfo objects
 DictionaryInfo dictionary = new DictionaryInfo();
 MessageInfo message = new MessageInfo();
 FixInfoTreeNode expectedNode = new FixInfoTreeNode("Version");

 treeView.loadMessages(expectedNode, dictionary);
 assertEquals(1, expectedNode.getChildCount());
 treeView.loadItems(expectedNode, message, 0);
 assertEquals(1, expectedNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 // Mock FixInfo, CompositeFixInfo, and FieldInfo objects
 FixInfo info = new FixInfo();
 CompositeFixInfo compositeInfo = new CompositeFixInfo();
 FieldInfo fieldInfo = new FieldInfo();
 FixInfoTreeNode parentNode = new FixInfoTreeNode("ParentNode");
 FixInfoTreeNode expectedNode = new FixInfoTreeNode("LeafNode");

 treeView.loadItems(expectedNode, info, 0);
 assertEquals(0, expectedNode.getChildCount());

 treeView.loadItems(parentNode, compositeInfo, 0);
 assertEquals(1, parentNode.getChildCount());
 assertEquals("ComponentName", parentNode.getUserObject());

 treeView.loadItems(expectedNode, fieldInfo, 0);
 assertEquals(1, expectedNode.getChildCount());
 assertEquals("FieldName", expectedNode.getUserObject());
 }

 @Test
 public void testExpandAll() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode)fixTree.getModel().getRoot();
 treeView.expandAll();
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertTrue(fixTree.isRowExpanded(i));
 }
 }

 @Test
 public void testCollapseAll() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode)fixTree.getModel().getRoot();
 treeView.collapseAll();
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertFalse(fixTree.isRowExpanded(i));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNullSearchString() {
 treeView.search(fixTree.getModel().getRoot(), null);
 }

 @Test
 public void testSearch() {
 // Mock FixInfoTreeNode and MessageInfo objects
 FixInfoTreeNode rootNode = (FixInfoTreeNode)fixTree.getModel().getRoot();
 MessageInfo message = new MessageInfo();
 message.setName("TestMessage");
 message.setMessageType("TestMessageType");
 FixInfoTreeNode messageNode = new FixInfoTreeNode(message);
 rootNode.add(messageNode);

 treeView.search(rootNode, "TestMessageType");
 assertTrue(messageNode.isHighlighted());

 treeView.search(rootNode, "TestMessage");
 assertTrue(messageNode.isHighlighted());
 }

 @Test
 public void testClear() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode)fixTree.getModel().getRoot();
 treeView.clear(rootNode);
 for (int i = 0; i < fixTree.getRowCount(); i++) {
 assertFalse(fixTree.getModel().getNodeAt(i).isHighlighted());
 }
 }
}