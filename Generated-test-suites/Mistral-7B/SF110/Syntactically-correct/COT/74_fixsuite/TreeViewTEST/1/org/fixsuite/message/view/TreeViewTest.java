package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private Library library;
    private TreeView treeView;
    private JFrame frame;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(treeView);
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
        assertEquals(new BorderLayout(), treeView.getLayout());
    }

    @Test
    public void testInitGUI() {
        assertTrue(treeView.mainScrollPane != null);
        assertTrue(treeView.fixTree != null);
        assertTrue(treeView.searchLabel != null);
        assertTrue(treeView.searchTextField != null);
        assertTrue(treeView.searchButton != null);
        assertTrue(treeView.clearButton != null);
        assertTrue(treeView.expandButton != null);
        assertTrue(treeView.collapseButton != null);
        assertTrue(treeView.toolbar != null);
    }

    @Test
    public void testLoadFIXTree() {
        final TreeModel model = treeView.fixTree.getModel();
        assertNotNull(model);
        assertSame(model.getRoot(), treeView.fixTree.getModel().getRoot());
        assertEquals(Collections.singletonList(treeView.fixTree.getModel().getChild(0)), Arrays.asList(model.getChildren(0)));
    }

    @Test
    public void testLoadMessages() {
        final DictionaryInfo dictionary = new DictionaryInfo("dictionary1", "version1");
        library.addDictionary(dictionary);
        final MessageInfo message1 = new MessageInfo("message1", "messageType1", new String[]{"field1"}, new String[]{"tagNumber1"});
        dictionary.addMessage(message1);

        final FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        final DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) treeView.fixTree.getModel().getChild(0);
        assertEquals(Collections.singletonList(dictionaryNode), Arrays.asList(rootNode.getChildren()));

        assertEquals(Collections.singletonList(message1), Arrays.asList(dictionaryNode.getChildren()));
    }

    @Test
    public void testLoadItems() {
        final DictionaryInfo dictionary = new DictionaryInfo("dictionary1", "version1");
        library.addDictionary(dictionary);
        final MessageInfo message = new MessageInfo("message1", "messageType1", new String[]{"field1"}, new String[]{"tagNumber1"});
        dictionary.addMessage(message);

        final FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        final DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) treeView.fixTree.getModel().getChild(0);
        final DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) treeView.fixTree.getModel().getChild(0).getChild(0);

        final FieldInfo field1 = new FieldInfo("field1", "fieldName1", "tagNumber1");
        message.addField(field1);

        final FixInfoTreeNode fieldNode1 = (FixInfoTreeNode) treeView.fixTree.getModel().getChild(0).getChild(0).getChildAt(0);
        assertEquals(1, fieldNode1.getChildCount());
        assertEquals("fieldName1", fieldNode1.getUserObject().toString());
        assertEquals("tagNumber1", fieldNode1.getUserObject().getClass().getSimpleName());

        final DefaultMutableTreeNode fieldNode2 = (DefaultMutableTreeNode) fieldNode1.getChildAt(0);
        assertNull(fieldNode2.getUserObject());
        assertEquals("FieldInfo", fieldNode2.getClass().getSimpleName());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final boolean expanded = treeView.fixTree.isRowExpanded(row);
            assertTrue(expanded);
            ++count;
        }
        assertEquals(count, library.getDictionaries().size() * 2); // each dictionary and message have 2 nodes
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        int count = 0;
        for (int row = treeView.fixTree.getRowCount() - 1; row >= 0; --row) {
            final boolean collapsed = treeView.fixTree.isRowExpanded(row);
            assertFalse(collapsed);
            ++count;
        }
        assertEquals(count, library.getDictionaries().size() * 2); // each dictionary and message have 2 nodes
    }

    @Test
    public void testSearch() {
        final String searchString = "field";

        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();

        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final boolean highlighted = ((DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(row)).isHighlighted();
            if (highlighted) {
                final Object userObject = treeView.fixTree.getModel().getNodeAt(row).getUserObject();
                if (userObject instanceof FieldInfo) {
                    final FieldInfo fieldInfo = (FieldInfo) userObject;
                    assertTrue(fieldInfo.getName().matches(searchString) || fieldInfo.getTagNumber().matches(searchString));
                } else if (userObject instanceof MessageInfo) {
                    final MessageInfo messageInfo = (MessageInfo) userObject;
                    assertTrue(messageInfo.getName().matches(searchString) || messageInfo.getMessageType().matches(searchString));
                }
                ++count;
            }
        }
        assertEquals(count, 1); // there should be only one matching node

        // Test search in the subtree
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(1);
        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();
        count = 0;
        for (int row = 0; row < node.getChildCount(); ++row) {
            final boolean highlighted = ((FixInfoTreeNode) node.getChildAt(row)).isHighlighted();
            if (highlighted) {
                ++count;
            }
        }
        assertEquals(1, count); // there should be only one matching node in the subtree
    }

    @Test(expected = IllegalStateException.class)
    public void testClearWhenNoSearchResult() {
        treeView.clearButton.doClick();
    }

    @Test
    public void testClear() {
        final String searchString = "field";
        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();

        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final boolean highlighted = ((DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(row)).isHighlighted();
            if (highlighted) {
                treeView.clear((FixInfoTreeNode) treeView.fixTree.getModel().getNodeAt(row));
                assertFalse(((DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(row)).isHighlighted());
                ++count;
            }
        }
        assertEquals(count, 1); // there should be only one highlighted node

        treeView.clearButton.doClick();
        count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final boolean highlighted = ((DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(row)).isHighlighted();
            assertFalse(highlighted);
            ++count;
        }
        assertEquals(count, library.getDictionaries().size() * 2); // each dictionary and message have 2 nodes
    }

    @Test
    public void testSearchWithEmptySearchText() {
        treeView.searchTextField.setText("");
        treeView.searchButton.doClick();

        int count = 0;
        for (int row = 0; row < treeView.fixTree.getRowCount(); ++row) {
            final boolean highlighted = ((DefaultMutableTreeNode) treeView.fixTree.getModel().getNodeAt(row)).isHighlighted();
            if (highlighted) {
                ++count;
            }
        }
        assertEquals(count, library.getDictionaries().size() * 2); // no nodes should be highlighted with empty search text
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullSearchText() {
        treeView.searchTextField.setText(null);
        treeView.searchButton.doClick();
    }

    @Test
    public void testActionPerformed() {
        treeView.expandButton.doClick();
        assertTrue(treeView.fixTree.isExpanded(0));

        treeView.collapseButton.doClick();
        assertFalse(treeView.fixTree.isExpanded(0));

        treeView.searchTextField.setText("messageType1");
        treeView.searchButton.doClick();
        treeView.searchTextField.setText("");
    }

    @Test
    public void testKeyPressed() {
        treeView.searchTextField.setText("messageType1");
        treeView.searchTextField.requestFocus();
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        treeView.searchButton.doClick();
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
    }

    @Test
    public void testTreeCellRenderer() {
        final JTree tree = new JTree(treeView.loadFIXTree());
        final TreeCellRenderer renderer = tree.getCellRenderer();

        final JComponent component = renderer.getTreeCellRendererComponent(tree, new FieldInfo("field1", "fieldName1", "tagNumber1"), false, false, false, 0, false);
        assertEquals("fieldName1 (tagNumber1)", component.toString());
        assertEquals(Color.BLACK, component.getForeground());

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getChild(0).getChild(0);
        node.setUserObject(new MessageInfo("message1", "messageType1"));
        ((FixInfoTreeNode) node).setHighlighted(true);

        final JComponent component2 = renderer.getTreeCellRendererComponent(tree, node, false, false, false, 0, false);
        assertEquals("messageType1", component2.toString());
        assertEquals(Color.BLUE, component2.getForeground());
    }
}