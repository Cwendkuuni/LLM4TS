package org.apache.commons.cli; // This is not the correct package for TreeView tests, please replace it with the actual package name.

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

    @Test
    public void testLoadFIXTree() {
        TreeView tv = new TreeView(new Library()); // Replace with a valid Library instance.
        assertNotNull(tv.fixTree);
    }

    @Test
    public void testLoadMessages() {
        // Replace this test with a more comprehensive one that checks the structure and content of the loaded messages.
        TreeView tv = new TreeView(new Library());
        FixInfoTreeNode root = tv.fixTree.getModel().getRoot();
        assertNotNull(root.getChildCount());
        assertTrue(root.getChildAt(0).getUserObject() instanceof DictionaryInfo);
    }

    @Test
    public void testLoadItems() {
        // Replace this test with a more comprehensive one that checks the structure and content of the loaded items.
        TreeView tv = new TreeView(new Library());
        FixInfoTreeNode root = tv.fixTree.getModel().getRoot();
        FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(dictionaryNode.getChildCount());
        assertTrue(dictionaryNode.getChildAt(0).getUserObject() instanceof MessageInfo);
    }

    @Test
    public void testExpandAll() {
        TreeView tv = new TreeView(new Library());
        tv.expandAll();
        int expandedCount = 0;
        for (int i = 0; i < tv.fixTree.getRowCount(); ++i) {
            if (tv.fixTree.isExpanded(new TreePath(new Object[] { tv.fixTree.getModel().getRoot(), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0) }))) {
                expandedCount++;
            }
        }
        assertEquals(tv.fixTree.getRowCount(), expandedCount);
    }

    @Test
    public void testCollapseAll() {
        TreeView tv = new TreeView(new Library());
        tv.collapseAll();
        int collapsedCount = 0;
        for (int i = 0; i < tv.fixTree.getRowCount(); ++i) {
            if (!tv.fixTree.isExpanded(new TreePath(new Object[] { tv.fixTree.getModel().getRoot(), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0), tv.fixTree.getModel().getChild(0) }))) {
                collapsedCount++;
            }
        }
        assertEquals(tv.fixTree.getRowCount(), collapsedCount);
    }

    @Test
    public void testSearch() {
        TreeView tv = new TreeView(new Library());
        String searchString = "Field1";
        tv.search(tv.fixTree.getModel().getRoot(), searchString);
        int matchedCount = 0;
        for (int i = 0; i < tv.fixTree.getRowCount(); ++i) {
            if (tv.fixTree.isExpanded(new TreePath(tv.fixTree.getModel().getPathForLocation(i, 0)))) {
                FixInfoTreeNode node = (FixInfoTreeNode)tv.fixTree.getModel().getChild(tv.fixTree.getModel().getPathForLocation(i, 0));
                if (node.getUserObject() instanceof FieldInfo && ((FieldInfo)node.getUserObject()).getName().matches(searchString)) {
                    matchedCount++;
                }
            }
        }
        assertEquals(1, matchedCount);
    }

    @Test
    public void testClear() {
        TreeView tv = new TreeView(new Library());
        tv.search(tv.fixTree.getModel().getRoot(), "Field1");
        tv.clear(tv.fixTree.getModel().getRoot());
        int clearedCount = 0;
        for (int i = 0; i < tv.fixTree.getRowCount(); ++i) {
            if (!tv.fixTree.isExpanded(new TreePath(tv.fixTree.getModel().getPathForLocation(i, 0)))) {
                clearedCount++;
            }
        }
        assertEquals(tv.fixTree.getRowCount(), clearedCount);
    }
}