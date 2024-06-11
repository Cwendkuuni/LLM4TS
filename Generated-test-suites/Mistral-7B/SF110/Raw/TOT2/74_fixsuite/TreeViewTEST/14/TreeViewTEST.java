package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
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
    public void testTreeViewConstruction() {
        assertNotNull(treeView);
        assertTrue(treeView instanceof JPanel);
        assertTrue(treeView.getLayout() instanceof BorderLayout);
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertNotNull(treeView.toolbar);
    }

    @Test
    public void testLoadFIXTree() {
        // Basic functionality test for loadFIXTree method
        assertNotNull(treeView.loadFIXTree());
        // Edge case test for empty library
        library.clear();
        assertNull(treeView.loadFIXTree());
    }

    @Test
    public void testLoadMessages() {
        // Basic functionality test for loadMessages method
        final DictionaryInfo dictionary = new DictionaryInfo("TestDictionary");
        library.addDictionary(dictionary);
        treeView = new TreeView(library);
        final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
        dictionary.addMessage(message);
        final FixInfoTreeNode rootNode = treeView.loadFIXTree();
        final FixInfoTreeNode messageNode = assertTreeHasNode(rootNode, message.getName());
        assertEquals(message.getName(), messageNode.getUserObject().toString());
    }

    @Test
    public void testLoadItems() {
        // Basic functionality test for loadItems method
        final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
        final FieldInfo field1 = new FieldInfo("TestField1", 1, "TestField1Name");
        final FieldInfo field2 = new FieldInfo("TestField2", 2, "TestField2Name");
        message.addItem(field1);
        message.addItem(field2);
        final CompositeFixInfo compositeFixInfo = new CompositeFixInfo("TestComponent");
        compositeFixInfo.addItem(message);
        final FixInfoTreeNode rootNode = treeView.loadFIXTree();
        final FixInfoTreeNode messageNode = assertTreeHasNode(rootNode, message.getName());
        final FixInfoTreeNode field1Node = assertTreeHasNode(messageNode, field1.getName());
        final FixInfoTreeNode field2Node = assertTreeHasNode(messageNode, field2.getName());
        assertFalse(field1Node.isLeaf());
        assertFalse(field2Node.isLeaf());
    }

    @Test
    public void testExpandAll() {
        // Basic functionality test for expandAll method
        treeView.expandAll();
        // Edge case test for empty tree
        treeView = new TreeView(library);
        treeView.expandAll();
    }

    @Test
    public void testCollapseAll() {
        // Basic functionality test for collapseAll method
        treeView.collapseAll();
        // Edge case test for empty tree
        treeView = new TreeView(library);
        treeView.collapseAll();
    }

    @Test
    public void testSearch() {
        // Basic functionality test for search method
        final DictionaryInfo dictionary = new DictionaryInfo("TestDictionary");
        library.addDictionary(dictionary);
        treeView = new TreeView(library);
        final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
        dictionary.addMessage(message);
        final FieldInfo field1 = new FieldInfo("TestField1", 1, "TestField1Name");
        message.addItem(field1);
        final FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.search("TestMessageType");
        final FixInfoTreeNode messageNode = assertTreeHasNode(rootNode, message.getName());
        assertTrue(messageNode.isHighlighted());
        treeView.search("TestField1Name");
        final FixInfoTreeNode field1Node = assertTreeHasNode(messageNode, field1.getName());
        assertTrue(field1Node.isHighlighted());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullTree() {
        // Error case test for search method with null tree
        treeView = null;
        treeView.search("TestMessageType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullSearchString() {
        // Error case test for search method with null search string
        treeView.search(null);
    }

    

    private FixInfoTreeNode assertTreeHasNode(final FixInfoTreeNode node, final String name) {
        if (node == null) {
            fail("Node is null");
        }
        int count = 0;
        while (count < node.getChildCount()) {
            final FixInfoTreeNode childNode = (FixInfoTreeNode)node.getChildAt(count);
            if (childNode.getUserObject().toString().equals(name)) {
                return childNode;
            }
            ++count;
        }
        fail("Node not found");
        return null;
    }
}