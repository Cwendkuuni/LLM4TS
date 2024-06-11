import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeViewTest {

 private TreeView treeView;
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 library.addDictionary(new DictionaryInfo("FIX 4.4", "FIX", "2008-01-13", "FIX44.xml"));
 library.addDictionary(new DictionaryInfo("FIX 5.0", "FIX", "2012-11-07", "FIX50.xml"));
 treeView = new TreeView(library);
 }

 @Test
 public void testTreeStructure() {
 // Test the tree structure by checking the number of nodes at each level
 assertEquals(1, treeView.getComponentCount());
 assertEquals(2, treeView.getComponentAt(0).getComponentCount());
 for (int i = 0; i < 2; i++) {
 assertEquals(library.size(), ((JTree) treeView.getComponentAt(0).getComponentAt(i)).getModel().getChildCount());
 for (int j = 0; j < library.size(); j++) {
 FixInfoTreeNode node = (FixInfoTreeNode) ((JTree) treeView.getComponentAt(0).getComponentAt(i)).getModel().getChildAt(j);
 assertEquals(dictionarySize(library.get(j)), node.getChildCount());
 }
 }
 }

 @Test
 public void testSearch() {
 // Test search functionality with valid search strings
 String searchString = "44";
 treeView.searchTextField.setText(searchString);
 treeView.searchButton.doClick();
 FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
 FixInfoTreeNode dictionaryNode = null;
 for (int i = 0; i < rootNode.getChildCount(); i++) {
 if (library.get(i).getName().equals("FIX 4.4")) {
 dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(i);
 break;
 }
 }
 assertNotNull(dictionaryNode);
 for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
 FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
 assertFalse(messageNode.isHighlighted());
 if (messageNode.getUserObject() instanceof MessageInfo) {
 MessageInfo message = (MessageInfo) messageNode.getUserObject();
 assertEquals(searchString, message.getMessageType().replaceAll("[^0-9]", ""));
 messageNode.setHighlighted(true);
 }
 }
 // Test search functionality with invalid search strings
 treeView.searchTextField.setText("InvalidSearchString");
 treeView.searchButton.doClick();
 assertFalse(treeView.fixTree.isSelectionEmpty());
}

 @Test
 public void testClear() {
 // Test clear functionality
 treeView.searchTextField.setText("44");
 treeView.searchButton.doClick();
 int selectedCountBeforeClear = treeView.fixTree.getSelectionModel().getSelectionCount();
 treeView.clearButton.doClick();
 int selectedCountAfterClear = treeView.fixTree.getSelectionModel().getSelectionCount();
 assertEquals(0, selectedCountAfterClear);
}

 @Test
 public void testExpandAndCollapse() {
 // Test expandAll functionality
 treeView.expandButton.doClick();
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertTrue(treeView.fixTree.isRowExpanded(i));
 }
 // Test collapseAll functionality
 treeView.collapseButton.doClick();
 for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
 assertFalse(treeView.fixTree.isRowExpanded(i));
 }
}

 private int dictionarySize(DictionaryInfo dictionary) {
 int size = 0;
 for (MessageInfo message : dictionary.getMessages()) {
 size += message.getItems().size();
 }
 return size;
}

}