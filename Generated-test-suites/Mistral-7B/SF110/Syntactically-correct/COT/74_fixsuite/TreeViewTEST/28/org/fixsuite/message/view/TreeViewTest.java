import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import javax.swing.tree.*;
import javax.swing.*;

public class TreeViewTest {

    private static final String SEARCH_STRING = "Test";
    private TreeView treeView;
    private JTree fixTree;
    private JTree model;

    @Before
    public void setUp() {
        Library library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        model = (JTree) fixTree.getModel();
    }

    @Test
    public void testTreeViewConstructor() {
        // Check if TreeView is initialized correctly
        Assert.assertNotNull(treeView);
        Assert.assertNotNull(treeView.getMainScrollPane());
        Assert.assertNotNull(treeView.getFixTree());
        Assert.assertNotNull(treeView.getSearchLabel());
        Assert.assertNotNull(treeView.getSearchTextField());
        Assert.assertNotNull(treeView.getSearchButton());
        Assert.assertNotNull(treeView.getClearButton());
        Assert.assertNotNull(treeView.getExpandButton());
        Assert.assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree() {
        // Test if loadFIXTree method returns a non-null tree root
        Assert.assertNotNull(treeView.loadFIXTree());
    }

    @Test
    public void testLoadMessages() {
        final FixInfoTreeNode root = treeView.loadFIXTree();
        // Test if loadMessages populates the root node with children
        Assert.assertTrue(root.getChildCount() > 0);
    }

    @Test
    public void testLoadItems() {
        final FixInfoTreeNode root = treeView.loadFIXTree();
        final FixInfoTreeNode versionNode = (FixInfoTreeNode) root.getChildAt(0);
        // Test if loadItems populates the version node with children
        Assert.assertTrue(versionNode.getChildCount() > 0);
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        // Check if all tree nodes are expanded
        for (int row = 0; row < model.getRowCount(); ++row) {
            Assert.assertTrue(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        // Check if all tree nodes are collapsed
        for (int row = 0; row < model.getRowCount(); ++row) {
            Assert.assertFalse(fixTree.isExpanded(row));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithEmptySearchString() {
        treeView.search(model.getModel(), "");
    }

    @Test
    public void testSearch() {
        // Search for an existing string
        treeView.search(model.getModel(), SEARCH_STRING);
        // Check if the searched node is highlighted
        FixInfoTreeNode searchedNode = null;
        for (int row = 0; row < model.getRowCount(); ++row) {
            Object userObject = model.getUserObjectAt(row);
            if (userObject instanceof FixInfo) {
                FixInfo fixInfo = (FixInfo) userObject;
                if (fixInfo instanceof MessageInfo && ((MessageInfo) fixInfo).getName().equals(SEARCH_STRING) ||
                        fixInfo instanceof FieldInfo && ((FieldInfo) fixInfo).getName().equals(SEARCH_STRING) ||
                        fixInfo instanceof ComponentInfo && ((ComponentInfo) fixInfo).getName().equals(SEARCH_STRING)) {
                    searchedNode = (FixInfoTreeNode) model.getPathForRow(row);
                    break;
                }
            }
        }
        Assert.assertNotNull(searchedNode);
        Assert.assertTrue(searchedNode.isHighlighted());
        // Clear the search results
        treeView.clear(model.getModel());
    }

    @Test
    public void testClear() {
        treeView.search(model.getModel(), SEARCH_STRING);
        treeView.clear(model.getModel());
        // Check if all tree nodes are unhighlighted
        for (int row = 0; row < model.getRowCount(); ++row) {
            Object userObject = model.getUserObjectAt(row);
            if (userObject instanceof FixInfo) {
                FixInfoTreeNode node = (FixInfoTreeNode) model.getPathForRow(row);
                Assert.assertFalse(node.isHighlighted());
            }
        }
    }
}