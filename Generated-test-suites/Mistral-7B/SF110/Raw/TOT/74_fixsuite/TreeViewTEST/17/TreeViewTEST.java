import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library mockLibrary;

 @Before
 public void setUp() {
 mockLibrary = new Library(); // Assuming Library has necessary methods to populate data structures
 treeView = new TreeView(mockLibrary);
 }

 @Test
 public void testLoadFIXTree() {
 FixInfoTreeNode rootNode = treeView.loadFIXTree();
 assertNotNull(rootNode);
 assertEquals("Dictionaries", rootNode.getUserObject().toString());
 }

 @Test
 public void testLoadMessages_WithDictionaryMessages() {
 DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", "Version1");
 MessageInfo message = new MessageInfo("Message1", "MessageType1", null, null, null, null, null, null);
 CompositeFixInfo compositeFixInfo = new CompositeFixInfo(new ArrayList<>());
 compositeFixInfo.add(new FieldInfo("Field1", "TagNumber1", "Name1", "Type1", null, null, null));
 message.setItems(compositeFixInfo);
 dictionary.addMessage(message);
 mockLibrary.addDictionary(dictionary);

 FixInfoTreeNode versionNode = treeView.loadFIXTree().findChild("Dictionaries");
 FixInfoTreeNode node = versionNode.findChild("Version1");
 assertNotNull(node);
 assertEquals("Message1", node.getUserObject().toString());
 }

 @Test
 public void testLoadItems_WithComponentInfo() {
 ComponentInfo componentInfo = new ComponentInfo("Component1", "Name1");
 FixInfoTreeNode node = new FixInfoTreeNode(componentInfo);
 treeView.loadItems(node, componentInfo, 0);

 assertEquals(1, node.getChildCount());
 FixInfoTreeNode childNode = (FixInfoTreeNode)node.getChildAt(0);
 assertEquals("Component1", childNode.getUserObject().toString());
 }

 @Test
 public void testLoadItems_WithMultipleComponentInfos() {
 ComponentInfo component1 = new ComponentInfo("Component1", "Name1");
 ComponentInfo component2 = new ComponentInfo("Component2", "Name2");
 FixInfoTreeNode parentNode = new FixInfoTreeNode("Parent");
 treeView.loadItems(parentNode, null, 0);
 treeView.loadItems(parentNode, component1, 0);
 treeView.loadItems(parentNode, component2, 0);

 assertEquals(3, parentNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 TreeModel model = treeView.fixTree.getModel();
 int initialRowCount = model.getRowCount();
 treeView.expandAll();
 int expandedRowCount = model.getRowCount();
 assertEquals(initialRowCount, expandedRowCount);
 }

 @Test
 public void testSearch_ValidSearchString() {
 String searchString = "Name1";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().findChildRecursively("Name1");
 assertTrue(node != null);
 }

 @Test
 public void testClear() {
 treeView.searchTextField.setText("Name1");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();
 FixInfoTreeNode node = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().findChildRecursively("Name1");
 assertNull(node);
 }

 @Test
 public void testSearchWithRegex() {
 String searchString = "Name[12]";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 FixInfoTreeNode node1 = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().findChildRecursively("Name1");
 FixInfoTreeNode node2 = (FixInfoTreeNode)treeView.fixTree.getModel().getRoot().findChildRecursively("Name2");
 assertTrue(node1 != null && node2 != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithInvalidSearchString() {
 treeView.searchTextField.setText("InvalidSearchString");
 treeView.searchButton.doClick();
 }

 @Test
 public void testSearchWithEmptySearchString() {
 treeView.searchTextField.setText("");
 treeView.searchButton.doClick();
 }
}