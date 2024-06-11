package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    private Library library;
    private TreeView treeView;

    @Before
    public void setUp() {
        this.library = new Library(); // Assuming Library class has no-args constructor
        this.treeView = new TreeView(this.library);
    }

    @Test
    public void testConstructor() {
        // Testing if constructor is called successfully
        assert (this.treeView != null);
    }

    @Test
    public void testInitGUI() {
        // Checking if components are added correctly to the JPanel
        assertTrue(this.treeView.getComponentCount() > 0);
    }

    @Test
    public void testLoadFIXTree() {
        // Testing if loadFIXTree method populates the tree correctly
        assertEquals(this.treeView.getFixTree().getRowCount(), this.library.getDictionaries().size());
    }

    @Test
    public void testSearch() {
        // Testing search functionality with valid search string
        String searchString = "MessageType=HELLO";
        this.treeView.searchTextField.setText(searchString);
        this.treeView.searchButton.doClick();
        int count = 0;
        FixInfoTreeNode node = (FixInfoTreeNode)this.treeView.fixTree.getModel().getRoot();
        while (count < this.treeView.fixTree.getRowCount()) {
            if (node.isHighlighted()) {
                MessageInfo messageInfo = (MessageInfo)node.getUserObject();
                assertTrue(messageInfo.getMessageType().matches(searchString));
            }
            node = (FixInfoTreeNode)node.getNextSibling();
            count++;
        }
    }

    @Test
    public void testClear() {
        // Testing clear functionality with valid search string
        String searchString = "MessageType=HELLO";
        this.treeView.searchTextField.setText(searchString);
        this.treeView.searchButton.doClick();
        this.treeView.clearButton.doClick();
        int count = 0;
        FixInfoTreeNode node = (FixInfoTreeNode)this.treeView.fixTree.getModel().getRoot();
        while (count < this.treeView.fixTree.getRowCount()) {
            assertFalse(node.isHighlighted());
            node = (FixInfoTreeNode)node.getNextSibling();
            count++;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFIXTreeWithNullLibrary() {
        // Testing exception handling for null library
        new TreeView(null);
    }
}