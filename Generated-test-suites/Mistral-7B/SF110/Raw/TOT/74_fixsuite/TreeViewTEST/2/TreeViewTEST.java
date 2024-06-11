package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.CompositeFixInfo;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;

public class TreeViewTest
{
    private static final String TEST_DATA = "testData.xml";
    private static final String SEARCH_STRING = "Test";

    @org.junit.Before
    public void setUp()
    {
        // Initialize the library with test data
        Library library = new Library(TEST_DATA);
    }

    @org.junit.Test
    public void testInitGUI()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Check if the GUI components are initialized correctly
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @org.junit.Test
    public void testLoadFIXTree()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Check if the FIX tree is loaded correctly
        TreeModel treeModel = treeView.getFixTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        assertEquals(1, root.getChildCount());

        DefaultMutableTreeNode dictionariesNode = (DefaultMutableTreeNode)root.getChildAt(0);
        assertEquals(2, dictionariesNode.getChildCount());

        DefaultMutableTreeNode dictionary1Node = (DefaultMutableTreeNode)dictionariesNode.getChildAt(0);
        assertEquals("Dictionary 1", dictionary1Node.getUserObject());

        DefaultMutableTreeNode messagesNode = (DefaultMutableTreeNode)dictionariesNode.getChildAt(1);
        assertEquals("Dictionary 2", messagesNode.getUserObject());
    }

    @org.junit.Test
    public void testLoadMessages()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Check if messages are loaded correctly under the first dictionary node
        DefaultMutableTreeNode dictionary1Node = (DefaultMutableTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0);
        List<MessageInfo> messages = Arrays.asList(new MessageInfo("Message 1.1"), new MessageInfo("Message 1.2"), new MessageInfo("Message 1.3"));
        assertEquals(messages.size(), dictionary1Node.getChildCount());

        for (int i = 0; i < messages.size(); i++)
        {
            DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode)dictionary1Node.getChildAt(i);
            MessageInfo message = (MessageInfo)messageNode.getUserObject();
            assertEquals(message.getName(), messageNode.getUserObject().toString());
            assertEquals(message.getMessageType(), message.getUserObject());
        }
    }

    @org.junit.Test
    public void testLoadItems()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Check if items are loaded correctly under the first message node
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0).getChildAt(0);
        List<FixInfo> items = Arrays.asList(new FieldInfo("Field 1.1.1", "Field 1.1.1", 1), new FieldInfo("Field 1.1.2", "Field 1.1.2", 2), new FieldInfo("Field 1.1.3", "Field 1.1.3", 3), new ComponentInfo("Component 1.1"), new CompositeFixInfo("Composite 1.1"));
        assertEquals(items.size(), messageNode.getChildCount());

        for (int i = 0; i < items.size(); i++)
        {
            DefaultMutableTreeNode itemNode = (DefaultMutableTreeNode)messageNode.getChildAt(i);
            FixInfo info = (FixInfo)itemNode.getUserObject();
            assertEquals(info.getClass().getSimpleName(), info.getClass().getSimpleName());
        }
    }

    @org.junit.Test
    public void testExpandAll()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Expand all nodes and check if they are expanded
        treeView.expandAll();
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row)
        {
            assertTrue(treeView.getFixTree().isExpanded(row));
        }
    }

    @org.junit.Test
    public void testCollapseAll()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Collapse all nodes and check if they are collapsed
        treeView.collapseAll();
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row)
        {
            assertFalse(treeView.getFixTree().isExpanded(row));
        }
    }

    @org.junit.Test
    public void testSearch()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Perform a search with no search string
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), "");
        assertTrue(treeView.getFixTree().getModel().getRowCount() == Collections.EMPTY_LIST.size());

        // Perform a search with a search string that matches no nodes
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), "Xyz");
        assertTrue(treeView.getFixTree().getModel().getRowCount() == Collections.EMPTY_LIST.size());

        // Perform a search with a search string that matches some nodes
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), SEARCH_STRING);
        List<FixInfoTreeNode> matchingNodes = Arrays.asList((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0).getChildAt(0),
                                                            (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0).getChildAt(0).getChildAt(1),
                                                            (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0).getChildAt(1).getChildAt(0),
                                                            (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(1).getChildAt(0).getChildAt(0),
                                                            (FixInfoTreeNode)treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(1).getChildAt(0).getChildAt(1));
        for (FixInfoTreeNode node : matchingNodes)
        {
            assertTrue(node.isHighlighted());
        }

        // Clear the search results and check if no nodes are highlighted
        treeView.clear((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot());
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row)
        {
            assertFalse(treeView.getFixTree().getModel().getNodeAt(row).isHighlighted());
        }
    }

    @org.junit.Test
    public void testClear()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Perform a search with a search string and clear the search results
        treeView.search((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot(), SEARCH_STRING);
        treeView.clear((FixInfoTreeNode)treeView.getFixTree().getModel().getRoot());

        // Check if no nodes are highlighted
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row)
        {
            assertFalse(treeView.getFixTree().getModel().getNodeAt(row).isHighlighted());
        }
    }

    @org.junit.Test
    public void testRenderComponent()
    {
        // Create a TreeView instance with the library
        TreeView treeView = new TreeView(new Library(TEST_DATA));

        // Create a test cell renderer
        FixInfoCellRenderer renderer = new FixInfoCellRenderer();

        // Set the cell renderer for the JTree
        treeView.getFixTree().setCellRenderer(renderer);

        // Create a test node with a FieldInfo object
        DefaultMutableTreeNode testNode = new DefaultMutableTreeNode(new FieldInfo("Test Field", 1));

        // Render the component and check if it displays the FieldInfo name and tag number
        Component component = renderer.getTreeCellRendererComponent(null, testNode, false, false, false, -1, null);
        assertEquals("Test Field (1)", component.toString());
    }
}