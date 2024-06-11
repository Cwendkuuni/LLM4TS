package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.CompositeFixInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private static final String SEARCH_STRING = "test";
    private Library library;
    private TreeView treeView;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }

    @Test
    public void testInitGUI() {
        assertTrue(treeView.getLayout() instanceof BorderLayout);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
    }

    @Test
    public void testLoadFIXTree() {
        final FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals(1, root.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        final FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
        treeView.loadMessages(versionNode, createMockDictionaryInfo());
        assertEquals(2, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        final FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        treeView.loadItems(branchNode, createMockMessageInfo(), 0);
        assertEquals(2, branchNode.getChildCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadItemsWithInvalidLevel() {
        final FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        treeView.loadItems(branchNode, createMockMessageInfo(), 5);
    }

    @Test
    public void testExpandAll() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.expandAll();
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertTrue(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.collapseAll();
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testSearch() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.search(treeView.fixTree.getModel().getRoot(), SEARCH_STRING);
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            final TreeCellRenderer cellRenderer = treeView.fixTree.getCellRenderer();
            final Object cellValue = treeView.fixTree.getModel().getChild(0).getChildAt(i).getUserObject();
            if (cellValue instanceof FieldInfo || cellValue instanceof MessageInfo || cellValue instanceof ComponentInfo) {
                assertTrue(cellRenderer.isSelected(treeView.fixTree.getCellRendererComponent(treeView.fixTree, cellValue, false, false, false, i, false)));
                assertTrue(((DefaultMutableTreeNode)treeView.fixTree.getModel().getChild(0).getChildAt(i)).isHighlighted());
            }
        }
    }

    @Test
    public void testClear() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.search("test", treeView.fixTree.getModel().getRoot());
        treeView.clear(treeView.fixTree.getModel().getRoot());
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
            assertFalse(((DefaultMutableTreeNode)treeView.fixTree.getModel().getChild(0).getChildAt(i)).isHighlighted());
        }
    }

    @Test
    public void testValueChanged() {
        final JTree testTree = new JTree(treeView.loadFIXTree());
        final TreeSelectionModel selectionModel = testTree.getSelectionModel();
        treeView.valueChanged(new TreeSelectionEvent(testTree, 0, TreeSelectionModel.LEAF, null, null));
        assertEquals(1, selectionModel.getLeadSelectionPath().getPathCount());
    }

    @Test
    public void testActionPerformed_SearchButton() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.searchTextField.setText(SEARCH_STRING);
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "Go"));
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            final TreeCellRenderer cellRenderer = treeView.fixTree.getCellRenderer();
            final Object cellValue = treeView.fixTree.getModel().getChild(0).getChildAt(i).getUserObject();
            if (cellValue instanceof FieldInfo || cellValue instanceof MessageInfo || cellValue instanceof ComponentInfo) {
                assertTrue(cellRenderer.isSelected(treeView.fixTree.getCellRendererComponent(treeView.fixTree, cellValue, false, false, false, i, false)));
                assertTrue(((DefaultMutableTreeNode)treeView.fixTree.getModel().getChild(0).getChildAt(i)).isHighlighted());
            }
        }
    }

    @Test
    public void testActionPerformed_ClearButton() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.search("test", treeView.fixTree.getModel().getRoot());
        treeView.actionPerformed(new ActionEvent(treeView.clearButton, 0, "Clear"));
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
            assertFalse(((DefaultMutableTreeNode)treeView.fixTree.getModel().getChild(0).getChildAt(i)).isHighlighted());
        }
    }

    @Test
    public void testActionPerformed_ExpandButton() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.actionPerformed(new ActionEvent(treeView.expandButton, 0, "Expand"));
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertTrue(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testActionPerformed_CollapseButton() {
        treeView.fixTree = new JTree(treeView.loadFIXTree());
        treeView.actionPerformed(new ActionEvent(treeView.collapseButton, 0, "Collapse"));
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testKeyPressed_EnterKey() {
        treeView.searchTextField.setText(SEARCH_STRING);
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "Go"));
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            final TreeCellRenderer cellRenderer = treeView.fixTree.getCellRenderer();
            final Object cellValue = treeView.fixTree.getModel().getChild(0).getChildAt(i).getUserObject();
            if (cellValue instanceof FieldInfo || cellValue instanceof MessageInfo || cellValue instanceof ComponentInfo) {
                assertTrue(cellRenderer.isSelected(treeView.fixTree.getCellRendererComponent(treeView.fixTree, cellValue, false, false, false, i, false)));
                assertTrue(((DefaultMutableTreeNode)treeView.fixTree.getModel().getChild(0).getChildAt(i)).isHighlighted());
            }
        }
    }

    private DictionaryInfo createMockDictionaryInfo() {
        List<MessageInfo> messages = new ArrayList<>();
        messages.add(createMockMessageInfo());
        messages.add(createMockMessageInfo());
        return new DictionaryInfo("Name", messages);
    }

    private MessageInfo createMockMessageInfo() {
        List<FieldInfo> fields = new ArrayList<>();
        fields.add(createMockFieldInfo());
        fields.add(createMockFieldInfo());
        CompositeFixInfo component = new CompositeFixInfo("Component");
        component.setItems(Collections.singletonList(new FieldInfo("Field 1", 1)));
        return new MessageInfo("Message 1", Arrays.asList(component, new FieldInfo("Field 2", 2)));
    }

    private FieldInfo createMockFieldInfo() {
        return new FieldInfo("Field", 1);
    }
}