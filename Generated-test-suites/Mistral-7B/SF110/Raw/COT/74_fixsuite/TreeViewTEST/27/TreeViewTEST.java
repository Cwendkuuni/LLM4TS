package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

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
        assertTrue(treeView.getComponentCount() > 0);
        assertEquals(treeView.getComponentCount(), 2); // JScrollPane and JToolBar
    }

    @Test
    public void testLoadFIXTree() {
        final FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertSame(root.getUserObject(), "Dictionaries");
        assertTrue(root.getChildCount() > 0);
    }

    @Test
    public void testLoadMessages() {
        final DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", null);
        final MessageInfo message1 = new MessageInfo("Message1", "MessageType1", null, null, null, new ArrayList<>());
        final MessageInfo message2 = new MessageInfo("Message2", "MessageType2", null, null, null, new ArrayList<>());
        dictionary.addMessageInfo(message1);
        dictionary.addMessageInfo(message2);
        final FixInfoTreeNode versionNode = new FixInfoTreeNode("Version1");
        treeView.loadMessages(versionNode, dictionary);
        assertTrue(versionNode.getChildCount() > 0);
        assertSame(versionNode.getChildAt(0).getUserObject(), message1);
        assertSame(versionNode.getChildAt(1).getUserObject(), message2);
    }

    @Test
    public void testLoadItems() {
        final CompositeFixInfo compositeFixInfo = new CompositeFixInfo("Component1", null, new ArrayList<>());
        final FieldInfo fieldInfo1 = new FieldInfo("Field1", "Tag1", "Name1", "Type1", null);
        final FieldInfo fieldInfo2 = new FieldInfo("Field2", "Tag2", "Name2", "Type2", null);
        compositeFixInfo.addFixInfo(fieldInfo1);
        compositeFixInfo.addFixInfo(fieldInfo2);
        final FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        treeView.loadItems(branchNode, compositeFixInfo, 0);
        assertTrue(branchNode.getChildCount() > 0);
        assertSame(branchNode.getChildAt(0).getUserObject(), fieldInfo1);
        assertSame(branchNode.getChildAt(1).getUserObject(), fieldInfo2);
    }

    @Test
    public void testExpandAll() {
        final JTree tree = treeView.getFixTree();
        treeView.expandAll();
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertTrue(tree.isExpanded(new TreePath(tree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testCollapseAll() {
        final JTree tree = treeView.getFixTree();
        treeView.collapseAll();
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(tree.isExpanded(new TreePath(tree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testSearch() {
        final JTree tree = treeView.getFixTree();
        final String searchString = "Field1";
        treeView.search(tree.getModel().getRoot(), searchString);
        for (int i = 0; i < tree.getRowCount(); i++) {
            final TreeCellRenderer renderer = tree.getCellRenderer();
            final Object userObject = tree.getModel().getUserObjectAt(i);
            if (userObject instanceof FieldInfo && ((FieldInfo)userObject).getName().matches(searchString)) {
                assertTrue(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            } else {
                assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            }
            assertEquals(renderer.getBackground(), ((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted() ? Color.BLUE : null);
        }
    }

    @Test
    public void testClear() {
        final JTree tree = treeView.getFixTree();
        treeView.clear((FixInfoTreeNode)tree.getModel().getRoot());
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
        }
    }

    @Test
    public void testValueChanged() {
        final List<Object> detailsDisplayed = new ArrayList<>();
        LibrarySuite.getInstance().setDetailsPanel(new DetailsPanel() {
            @Override
            public void displayDetails(final FixInfo fixInfo, final ComponentInfo componentInfo) {
                detailsDisplayed.add(fixInfo);
            }

            @Override
            public void clearDisplay() {
                detailsDisplayed.clear();
            }
        });
        final FixInfoTreeNode node = new FixInfoTreeNode(new MessageInfo("Message1", "MessageType1", null, null, null, Collections.emptyList()));
        treeView.valueChanged(new TreeSelectionEvent(treeView, new TreePath(new Object[] {node})));
        assertEquals(detailsDisplayed.size(), 1);
        assertEquals(detailsDisplayed.get(0), new MessageInfo("Message1", "MessageType1", null, null, null, Collections.emptyList()));
    }

    @Test
    public void testActionPerformedExpandButton() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.expandButton, 0, "expandButton"));
        treeView.expandAll();
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertTrue(tree.isExpanded(new TreePath(tree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testActionPerformedCollapseButton() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.collapseButton, 0, "collapseButton"));
        treeView.collapseAll();
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(tree.isExpanded(new TreePath(tree.getPathForLocation(i, 0))));
        }
    }

    @Test
    public void testActionPerformedSearchButton() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "searchButton"));
        treeView.search((FixInfoTreeNode)tree.getModel().getRoot(), "Field1");
        for (int i = 0; i < tree.getRowCount(); i++) {
            final TreeCellRenderer renderer = tree.getCellRenderer();
            final Object userObject = tree.getModel().getUserObjectAt(i);
            if (userObject instanceof FieldInfo && ((FieldInfo)userObject).getName().matches("Field1")) {
                assertTrue(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            } else {
                assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            }
            assertEquals(renderer.getBackground(), ((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted() ? Color.BLUE : null);
        }
    }

    @Test
    public void testActionPerformedClearButton() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.clearButton, 0, "clearButton"));
        treeView.clear((FixInfoTreeNode)tree.getModel().getRoot());
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
        }
    }

    @Test
    public void testActionPerformedSearchTextFieldEmpty() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "searchButton"));
        treeView.search((FixInfoTreeNode)tree.getModel().getRoot(), "");
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
        }
    }

    @Test
    public void testActionPerformedSearchTextFieldNull() {
        final JTree tree = treeView.getFixTree();
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "searchButton"));
        assertThrows(NullPointerException.class, () -> treeView.search((FixInfoTreeNode)tree.getModel().getRoot(), null));
    }

    @Test
    public void testKeyPressedSearchTextField() {
        final JTree tree = treeView.getFixTree();
        treeView.searchButton.doClick();
        treeView.searchTextField.setText("Field1");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 10, true, false, 0, 0));
        for (int i = 0; i < tree.getRowCount(); i++) {
            final TreeCellRenderer renderer = tree.getCellRenderer();
            final Object userObject = tree.getModel().getUserObjectAt(i);
            if (userObject instanceof FieldInfo && ((FieldInfo)userObject).getName().matches("Field1")) {
                assertTrue(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            } else {
                assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            }
            assertEquals(renderer.getBackground(), ((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted() ? Color.BLUE : null);
        }
    }

    @Test
    public void testKeyPressedSearchTextFieldNotFound() {
        final JTree tree = treeView.getFixTree();
        treeView.searchButton.doClick();
        treeView.searchTextField.setText("FieldX");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 10, true, false, 0, 0));
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
        }
    }

    @Test
    public void testKeyTypedSearchTextField() {
        final JTree tree = treeView.getFixTree();
        treeView.searchButton.doClick();
        treeView.searchTextField.setText("Field1");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 2, true, false, 0, 0));
        for (int i = 0; i < tree.getRowCount(); i++) {
            assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
        }
    }

    @Test
    public void testKeyReleasedSearchTextField() {
        final JTree tree = treeView.getFixTree();
        treeView.searchButton.doClick();
        treeView.searchTextField.setText("Field1");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 0, true, false, 0, 0));
        for (int i = 0; i < tree.getRowCount(); i++) {
            final TreeCellRenderer renderer = tree.getCellRenderer();
            final Object userObject = tree.getModel().getUserObjectAt(i);
            if (userObject instanceof FieldInfo && ((FieldInfo)userObject).getName().matches("Field1")) {
                assertTrue(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            } else {
                assertFalse(((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted());
            }
            assertEquals(renderer.getBackground(), ((FixInfoTreeNode)tree.getPathForRow(i)).isHighlighted() ? Color.BLUE : null);
        }
    }

    @Test
    public void testCreateAndShowFrame() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(treeView);
        frame.pack();
        frame.setVisible(true);
        frame.dispose();
    }
}