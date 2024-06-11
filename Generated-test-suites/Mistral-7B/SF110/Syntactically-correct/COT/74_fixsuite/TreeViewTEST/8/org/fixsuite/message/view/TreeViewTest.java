package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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

    private TreeView treeView;
    private JFrame frame;
    private JTree tree;
    private TreeCellRenderer cellRenderer;

    @Before
    public void setUp() {
        Library library = new Library();
        DictionaryInfo dictionary1 = new DictionaryInfo("Dictionary1", "1.0.0");
        DictionaryInfo dictionary2 = new DictionaryInfo("Dictionary2", "2.0.0");
        List<DictionaryInfo> dictionaries = Arrays.asList(dictionary1, dictionary2);
        library.setDictionaries(Collections.unmodifiableList(dictionaries));

        treeView = new TreeView(library);
        frame = new JFrame();
        frame.getContentPane().add(treeView);
        frame.pack();
        frame.setVisible(true);
        tree = treeView.getFixTree();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testTreeViewConstruction() {
        assertNotNull(treeView);
        assertEquals(300, treeView.getPreferredSize().width);
        assertEquals(400, treeView.getPreferredSize().height);
    }

    @Test
    public void testLoadFIXTree() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        assertEquals(2, root.getChildCount());
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) root.getChildAt(i);
            assertEquals("Dictionaries", versionNode.getUserObject());
            assertEquals(2, versionNode.getChildCount());
            for (int j = 0; j < versionNode.getChildCount(); j++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) versionNode.getChildAt(j);
                if (i == 0 && j == 0) {
                    assertEquals("Dictionary1", node.getUserObject());
                    assertEquals(16, node.getChildCount()); // Approx. number of child nodes for a Dictionary
                } else if (i == 1 && j == 0) {
                    assertEquals("Dictionary2", node.getUserObject());
                    assertEquals(16, node.getChildCount()); // Approx. number of child nodes for a Dictionary
                }
            }
        }
    }

    @Test
    public void testLoadMessages() {
        DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getRoot().getChildAt(0);
        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) versionNode.getChildAt(0);
        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) versionNode.getChildAt(1);
        assertEquals("Message1", messageNode1.getUserObject());
        assertEquals("Message2", messageNode2.getUserObject());

        List<MessageInfo> messages = Arrays.asList((MessageInfo) messageNode1.getUserObject(), (MessageInfo) messageNode2.getUserObject());
        for (MessageInfo messageInfo : messages) {
            assertEquals(Arrays.asList("Name1", "Type1"), messageInfo.getNames());
            assertEquals("Type1", messageInfo.getMessageType());
            assertEquals("Name1", messageInfo.getName());
        }
    }

    @Test
    public void testLoadItems() {
        DefaultMutableTreeNode branchNode = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getRoot().getChildAt(0).getChildAt(0);
        DefaultMutableTreeNode leafNode1 = (DefaultMutableTreeNode) branchNode.getChildAt(0);
        DefaultMutableTreeNode leafNode2 = (DefaultMutableTreeNode) branchNode.getChildAt(1);
        DefaultMutableTreeNode leafNode3 = (DefaultMutableTreeNode) leafNode1.getChildAt(0);
        DefaultMutableTreeNode leafNode4 = (DefaultMutableTreeNode) leafNode2.getChildAt(0);

        List<FixInfo> items = Arrays.asList((FieldInfo) leafNode1.getUserObject(), (FieldInfo) leafNode2.getUserObject(),
                (FieldInfo) leafNode3.getUserObject(), (FieldInfo) leafNode4.getUserObject());

        for (FixInfo fixInfo : items) {
            if (fixInfo instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) fixInfo;
                assertEquals("TagNumber1", String.valueOf(fieldInfo.getTagNumber()));
                assertEquals("Name1", fieldInfo.getName());
            }
        }
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int row = 0; row < tree.getRowCount(); ++row) {
            assertTrue(tree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int row = tree.getRowCount() - 1; row >= 0; --row) {
            assertFalse(tree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        String searchString = "Name1";
        treeView.search(searchString);

        for (int row = 0; row < tree.getRowCount(); ++row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getPathForLocation(row, 0);
            if (node != null && node.getUserObject() instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) node.getUserObject();
                assertTrue(fieldInfo.getName().matches(searchString));
                assertTrue(fieldInfo.isHighlighted());
            } else {
                assertFalse(node.isHighlighted());
            }
        }
    }

    @Test
    public void testClear() {
        String searchString = "Name1";
        treeView.search(searchString);
        treeView.clear(treeView.getFixTree().getModel().getRoot());

        for (int row = 0; row < tree.getRowCount(); ++row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getPathForLocation(row, 0);
            if (node != null) {
                assertFalse(node.isHighlighted());
            }
        }
    }

    @Test
    public void testValueChanged() {
        String searchString = "Name1";
        treeView.search(searchString);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getPathForLocation(0, 0);
        if (node != null && node.getUserObject() instanceof FieldInfo) {
            assertEquals("Name1", node.getUserObject().getClass().getSimpleName());
            assertEquals("Name1", ((FieldInfo) node.getUserObject()).getName());

            treeView.valueChanged(new TreeSelectionEvent(treeView, new TreePath(node.getPath())));

            LibrarySuite.getInstance().getDetailsPanel().assertDetails();
            LibrarySuite.getInstance().getDetailsPanel().assertClear();
        }
    }

    @Test
    public void testActionPerformedSearchButton() {
        String searchString = "Name1";
        treeView.searchTextField.setText(searchString);
        treeView.actionPerformed(new ActionEvent(treeView, 0, "Go"));

        for (int row = 0; row < tree.getRowCount(); ++row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getPathForLocation(row, 0);
            if (node != null && node.getUserObject() instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) node.getUserObject();
                assertTrue(fieldInfo.getName().matches(searchString));
                assertTrue(fieldInfo.isHighlighted());
            } else {
                assertFalse(node.isHighlighted());
            }
        }
    }

    @Test
    public void testActionPerformedClearButton() {
        String searchString = "Name1";
        treeView.search(searchString);
        treeView.actionPerformed(new ActionEvent(treeView, 0, "Clear"));

        for (int row = 0; row < tree.getRowCount(); ++row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getPathForLocation(row, 0);
            if (node != null) {
                assertFalse(node.isHighlighted());
            }
        }
    }

    @Test
    public void testActionPerformedExpandButton() {
        treeView.actionPerformed(new ActionEvent(treeView, 0, "Expand"));

        for (int row = 0; row < tree.getRowCount(); ++row) {
            assertTrue(tree.isExpanded(row));
        }
    }

    @Test
    public void testActionPerformedCollapseButton() {
        treeView.actionPerformed(new ActionEvent(treeView, 0, "Collapse"));

        for (int row = 0; row < tree.getRowCount(); ++row) {
            assertFalse(tree.isExpanded(row));
        }
    }

    @Test
    public void testKeyPressedSearchTextField() {
        String searchString = "Name1";
        treeView.searchTextField.setText(searchString);
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));

        for (int row = 0; row < tree.getRowCount(); ++row) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getPathForLocation(row, 0);
            if (node != null && node.getUserObject() instanceof FieldInfo) {
                FieldInfo fieldInfo = (FieldInfo) node.getUserObject();
                assertTrue(fieldInfo.getName().matches(searchString));
                assertTrue(fieldInfo.isHighlighted());
            } else {
                assertFalse(node.isHighlighted());
            }
        }
    }
}