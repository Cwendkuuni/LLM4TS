package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;

public class TreeViewTest {

    @Test
    public void testTreeView() {
        Library library = new Library(); // Assuming you have a way to initialize a Library object
        TreeView treeView = new TreeView(library);
        assertNotNull(treeView);
    }

    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertNotNull(rootNode);
    }

    @Test
    public void testLoadMessages() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        DictionaryInfo dictionary = new DictionaryInfo(); // Assuming you have a way to initialize a DictionaryInfo object
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(dictionary.getMessages().size(), versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        MessageInfo message = new MessageInfo(); // Assuming you have a way to initialize a MessageInfo object
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Message");
        treeView.loadItems(branchNode, message, 0);
        assertEquals(message.getItems().size(), branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.expandAll();
        int count = 0;
        for (int row = 0; row < rootNode.getChildCount(); ++row) {
            assertTrue(rootNode.isExpanded(row));
            count++;
        }
        assertEquals(rootNode.getChildCount(), count);
    }

    @Test
    public void testCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.collapseAll();
        int count = 0;
        for (int row = 0; row < rootNode.getChildCount(); ++row) {
            assertFalse(rootNode.isExpanded(row));
            count++;
        }
        assertEquals(rootNode.getChildCount(), count);
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        String searchString = "test";
        treeView.search(rootNode, searchString);
        int count = 0;
        for (int row = 0; row < rootNode.getChildCount(); ++row) {
            FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(row);
            if (node.isHighlighted()) {
                count++;
                assertEquals(searchString, node.getUserObject().toString());
            }
        }
        assertEquals(0, count);

        MessageInfo message = new MessageInfo();
        message.setName("TestMessage");
        message.setMessageType("TestMessageType");
        FixInfoTreeNode messageNode = new FixInfoTreeNode(message);
        rootNode.add(messageNode);
        treeView.search(messageNode, searchString);
        count = 0;
        for (int row = 0; row < messageNode.getChildCount(); ++row) {
            FixInfoTreeNode node = (FixInfoTreeNode) messageNode.getChildAt(row);
            if (node.isHighlighted()) {
                count++;
                String name = ((FieldInfo) node.getUserObject()).getName();
                String tagNumber = "" + ((FieldInfo) node.getUserObject()).getTagNumber();
                assertTrue(name.matches(searchString) || tagNumber.matches(searchString));
            }
        }
        assertEquals(message.getItems().size(), count);
    }

    @Test
    public void testClear() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.clear(rootNode);
        int count = 0;
        for (int row = 0; row < rootNode.getChildCount(); ++row) {
            FixInfoTreeNode node = (FixInfoTreeNode) rootNode.getChildAt(row);
            assertFalse(node.isHighlighted());
            count++;
        }
        assertEquals(rootNode.getChildCount(), count);
    }
}