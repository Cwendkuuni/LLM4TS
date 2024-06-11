import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 private TreeView treeView;
 private JTree fixTree;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 fixTree = treeView.getFixTree();
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = (FixInfoTreeNode) fixTree.getModel().getRoot();
 assertEquals("Dictionaries", rootNode.getUserObject());
 assertTrue(rootNode.getChildCount() > 0);
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dictionary = library.getDictionaries().get(0);
 FixInfoTreeNode versionNode = treeView.loadMessages(new FixInfoTreeNode("Version"), dictionary);
 assertEquals(dictionary.getVersion(), versionNode.getUserObject());
 assertTrue(versionNode.getChildCount() > 0);
 }

 @Test
 public void testLoadItems() {
 final MessageInfo message = new MessageInfo("Message1", "MessageType1", new FieldInfo("Tag1", "Name1", 1), new FieldInfo("Tag2", "Name2", 2));
 FixInfoTreeNode leafNode = new FixInfoTreeNode(message);
 treeView.loadItems(leafNode, message, 0);
 assertEquals(message, leafNode.getUserObject());
 assertTrue(leafNode.getChildCount() == 0);

 final CompositeFixInfo compositeMessage = new CompositeFixInfo("CompositeMessage", new FieldInfo("Tag3", "Name3", 3), new MessageInfo("NestedMessage", "NestedMessageType", new FieldInfo("Tag4", "Name4", 4), new FieldInfo("Tag5", "Name5", 5)));
 FixInfoTreeNode branchNode = treeView.loadItems(new FixInfoTreeNode(compositeMessage), compositeMessage, 0);
 assertEquals(compositeMessage, branchNode.getUserObject());
 assertTrue(branchNode.getChildCount() > 0);
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();
 assertEquals(fixTree.getRowCount(), fixTree.getModel().getChildCount(fixTree.getModel().getRoot()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNullSearchString() {
 treeView.search(fixTree.getModel().getRoot(), null);
 }

 @Test
 public void testSearchWithEmptySearchString() {
 treeView.search(fixTree.getModel().getRoot(), "");
 assertEquals(0, fixTree.getRowCount());
 }

 @Test
 public void testSearchWithValidSearchString() {
 final String searchString = "Name1";
 treeView.search(fixTree.getModel().getRoot(), searchString);
 final int expectedRowCount = 1; // Replace with the actual number of rows that match the search string
 assertEquals(expectedRowCount, fixTree.getRowCount());
 }

 @Test
 public void testClear() {
 final int initialRowCount = fixTree.getRowCount();
 treeView.search(fixTree.getModel().getRoot(), "Name1");
 treeView.clear(fixTree.getModel().getRoot());
 assertEquals(initialRowCount, fixTree.getRowCount());
 }

 @After
 public void tearDown() {
 // Add any tear-down code here
 }
}