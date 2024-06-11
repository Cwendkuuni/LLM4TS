import org.junit.Test;
import static org.junit.Assert.*;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.*;

public class TreeViewTest {
    @Test
    public void testTreeViewConstruction() {
        // Given
        Library library = new Library();
        TreeView treeView = new TreeView(library);

        // When

        // Then
        assertNotNull(treeView);
        assertEquals(new Dimension(300, 400), treeView.getPreferredSize());
    }

    @Test
    public void testLoadFIXTreeMethod() {
        // Given
        Library library = new Library();
        TreeView treeView = new TreeView(library);

        // When
        FixInfoTreeNode root = treeView.loadFIXTree();

        // Then
        assertNotNull(root);
        assertTrue(root.getChildCount() > 0);
    }

    @Test
    public void testExpandAllMethod() {
        // Given
        Library library = new Library();
        TreeView treeView = new TreeView(library);

        // When
        treeView.expandAll();

        // Then
        assertTrue(treeView.fixTree.getModel().getRowCount() > 0);
        for (int i = 0; i < treeView.fixTree.getModel().getRowCount(); i++) {
            treeView.fixTree.expandRow(i);
        }
    }

    @Test
    public void testSearchMethod() {
        // Given
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        String searchString = "test";

        // When
        treeView.searchButton.doClick();
        treeView.searchTextField.setText(searchString);
        treeView.searchTextField.sendKeys(Keys.ENTER);

        // Then
        // Add assertions for search results here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFIXTreeWithNullLibrary() {
        // Given
        TreeView treeView = new TreeView(null);

        // When
        // This test should throw an IllegalArgumentException
    }
}