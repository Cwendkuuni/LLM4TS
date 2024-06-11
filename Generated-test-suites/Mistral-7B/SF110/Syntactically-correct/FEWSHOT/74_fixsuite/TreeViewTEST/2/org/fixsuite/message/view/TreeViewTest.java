package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.FieldInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

    @InjectMocks
    private TreeView treeView;

    private DefaultTreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Dictionaries");
        return new DefaultTreeModel(root);
    }

    private void setUpTree() {
        DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        MessageInfo message = Mockito.mock(MessageInfo.class);
        ComponentInfo component = Mockito.mock(ComponentInfo.class);
        FieldInfo field = Mockito.mock(FieldInfo.class);

        Mockito.when(dictionary.getMessages()).thenReturn(Arrays.asList(message).iterator());
        Mockito.when(message.getItems()).thenReturn(Arrays.asList(component, field).iterator());

        treeView.library = Mockito.mock(Library.class);
        Mockito.when(treeView.library.getDictionaries()).thenReturn(Arrays.asList(dictionary).iterator());

        DefaultMutableTreeNode node = treeView.loadFIXTree();
        treeView.fixTree.setModel(new TreeModel() {
            @Override
            public Object getChild(Object parent, int index) {
                return null;
            }

            @Override
            public int getChildCount(Object parent) {
                return 0;
            }

            @Override
            public boolean isLeaf(Object node) {
                return true;
            }

            @Override
            public Object getRoot() {
                return node;
            }
        });
        treeView.fixTree.setModel(new DefaultTreeModel(node));
    }

    @Test
    public void testLoadFIXTree() {
        setUpTree();
        assertEquals("Dictionaries", treeView.fixTree.getModel().getRoot().getUserObject());
    }

    @Test
    public void testLoadMessages() {
        setUpTree();
        DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) treeView.fixTree.getModel().getRoot().getChildAt(0);
        assertEquals("Version 1.0", versionNode.getUserObject());
        int childCount = versionNode.getChildCount();
        assertEquals(2, childCount);
        assertTrue(versionNode.getChildAt(0).isLeaf());
        assertTrue(versionNode.getChildAt(1).isLeaf());
    }

    @Test
    public void testLoadItems() {
        setUpTree();
        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) treeView.fixTree.getModel().getRowCount() > 0 ? treeView.fixTree.getModel().getChild(0) : null;
        assertEquals("Component", componentNode.getUserObject());
        int childCount = componentNode.getChildCount();
        assertEquals(2, childCount);
        assertTrue(componentNode.getChildAt(0).isLeaf());
        assertTrue(componentNode.getChildAt(1).isLeaf());
    }

    @Test
    public void testExpandAll() {
        setUpTree();
        treeView.expandAll();
        int rowCount = treeView.fixTree.getRowCount();
        for (int i = 0; i < rowCount; ++i) {
            treeView.fixTree.expandRow(i);
        }
        assertEquals(rowCount, treeView.fixTree.getRowCount());
    }

    @Test
    public void testCollapseAll() {
        setUpTree();
        treeView.collapseAll();
        int rowCount = treeView.fixTree.getRowCount();
        for (int i = rowCount - 1; i >= 0; --i) {
            treeView.fixTree.collapseRow(i);
        }
        assertEquals(0, treeView.fixTree.getRowCount());
    }

    @Test
    public void testSearch() {
        setUpTree();
        treeView.searchTextField.setText("Component");
        treeView.searchButton.doClick();
        int rowCount = treeView.fixTree.getRowCount();
        assertEquals(1, rowCount);
        treeView.clearButton.doClick();
        rowCount = treeView.fixTree.getRowCount();
        assertEquals(0, rowCount);
    }

    @Test
    public void testClear() {
        setUpTree();
        DefaultMutableTreeNode node = treeView.fixTree.getModel().getRoot();
        treeView.clear(node);
        int rowCount = treeView.fixTree.getRowCount();
        assertEquals(0, rowCount);
    }

    @Test
    public void testValueChanged() {
        setUpTree();
        treeView.fixTree.setSelectionPath(new TreePath(new Object[] { treeView.fixTree.getModel().getRoot(), treeView.fixTree.getModel().getChild(0), treeView.fixTree.getModel().getChild(0) }));
        treeView.valueChanged(Mockito.mock(TreeSelectionEvent.class));
        LibrarySuite.getInstance().getDetailsPanel().assertDisplayedDetails(Mockito.any(FixInfo.class), Mockito.any(ComponentInfo.class));
    }

}