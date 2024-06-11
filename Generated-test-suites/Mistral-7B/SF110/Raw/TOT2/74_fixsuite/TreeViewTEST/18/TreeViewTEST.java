package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testTreeViewInitialization() {
        // Check that the TreeView is initialized correctly
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree() {
        // Test the loadFIXTree method
        treeView.loadFIXTree();
        assertNotNull(treeView.getFixTree().getModel());
        assertFalse(treeView.getFixTree().isExpanded(0));
    }

    @Test
    public void testSearch() {
        // Test the search method with valid search string
        treeView.searchTextField.setText("MessageType:44");
        treeView.searchButton.doClick();
        TreePath searchedNode = treeView.fixTree.getLastSelectedPathComponent();
        assertNotNull(searchedNode);
        MessageInfo searchedMessage = (MessageInfo) ((FixInfoTreeNode)searchedNode).getUserObject();
        assertEquals("MessageType:44", searchedMessage.getMessageType());

        // Test the search method with an empty search string
        treeView.searchTextField.setText("");
        treeView.searchButton.doClick();
        searchedNode = treeView.fixTree.getLastSelectedPathComponent();
        assertNull(searchedNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithInvalidSearchString() {
        // Test the search method with an invalid search string
        treeView.searchTextField.setText("InvalidSearchString");
        treeView.searchButton.doClick();
    }
}