package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private JTree emptyTree;
    private JTextField searchTextField;

    @Before
    public void setup() {
        Library library = new Library();
        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        emptyTree = new JTree();
        searchTextField = treeView.getSearchTextField();
    }

    @Test
    public void testInitGUI() {
        // Check that components are added to the JPanel
        assertEquals(1, treeView.getComponentCount());
        JComponent center = (JComponent) treeView.getComponent(0);
        assertEquals(BorderLayout.CENTER, center.getLayout());
        JScrollPane mainScrollPane = (JScrollPane) center.getComponent(0);
        assertEquals(1, mainScrollPane.getComponentCount());
        JTree tree = (JTree) mainScrollPane.getComponent(0);
        assertSame(fixTree, tree);

        // Check that components are added to the JToolBar
        JComponent north = treeView.getComponent(1);
        assertEquals(JToolBar.class, north.getClass());
        List<Component> components = new ArrayList<>(Arrays.asList(
                treeView.getClearButton(),
                treeView.getSearchLabel(),
                treeView.getSearchTextField(),
                treeView.getSearchButton(),
                treeView.getExpandButton(),
                treeView.getCollapseButton()
        ));
        for (Component component : components) {
            assertTrue(north.getComponentCount() > 0);
            Component northComponent = north.getComponent(0);
            assertSame(component, northComponent);
            north.remove(0);
        }
    }

    @Test
    public void testLoadFIXTree() {
        // Test that loadFIXTree returns a non-null JTree
        assertNotNull(treeView.loadFIXTree());

        // Test that the JTree contains the expected nodes
        FixInfoTreeNode root = (FixInfoTreeNode) treeView.loadFIXTree().getModel().getRoot();
        List<DictionaryInfo> dictionaries = treeView.getLibrary().getDictionaries();
        for (DictionaryInfo dictionary : dictionaries) {
            FixInfoTreeNode versionNode = (FixInfoTreeNode) findNode(root, dictionary.getVersion());
            assertNotNull(versionNode);
            MessageInfo message = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
            FixInfoTreeNode messageNode = (FixInfoTreeNode) findNode(versionNode, message);
            assertNull(messageNode);

            ComponentInfo component = new ComponentInfo("TestComponent", null, null);
            List<MessageInfo> messages = dictionary.getMessages();
            for (MessageInfo m : messages) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) findNode(versionNode, m);
                assertNull(messageNode);

                FixInfoTreeNode componentNode = (FixInfoTreeNode) findNode(versionNode, component);
                assertNull(componentNode);

                List<FieldInfo> fields = m.getItems();
                for (FieldInfo field : fields) {
                    FixInfoTreeNode fieldNode = (FixInfoTreeNode) findNode(messageNode, field);
                    assertNull(fieldNode);
                }
            }

            List<ComponentInfo> components = component.getItems();
            for (ComponentInfo c : components) {
                FixInfoTreeNode componentNode = (FixInfoTreeNode) findNode(versionNode, c);
                assertNull(componentNode);

                List<FieldInfo> fields = c.getItems();
                for (FieldInfo field : fields) {
                    FixInfoTreeNode fieldNode = (FixInfoTreeNode) findNode(componentNode, field);
                    assertNull(fieldNode);
                }
            }
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            assertEquals(false, fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRow(i) })));
        }
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            assertEquals(true, fixTree.isExpanded(new TreePath(new Object[] { fixTree.getModel().getRow(i) })));
        }
    }

    @Test
    public void testSearch() {
        MessageInfo message = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        ComponentInfo component = new ComponentInfo("TestComponent", null, null);
        FieldInfo field = new FieldInfo("TestField", 1, "TestFieldName");

        List<DictionaryInfo> dictionaries = treeView.getLibrary().getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);
        MessageInfo messageInDictionary = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        dictionary.addMessage(messageInDictionary);
        dictionary.addComponent(component);
        component.addField(field);

        MessageInfo messageInTree = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        ComponentInfo componentInTree = new ComponentInfo("TestComponent", null, null);
        FieldInfo fieldInTree = new FieldInfo("TestField", 1, "TestFieldName");
        FixInfoTreeNode messageNodeInTree = new FixInfoTreeNode(messageInTree);
        FixInfoTreeNode componentNodeInTree = new FixInfoTreeNode(componentInTree);
        FixInfoTreeNode fieldNodeInTree = new FixInfoTreeNode(fieldInTree);
        messageNodeInTree.add(fieldNodeInTree);
        componentNodeInTree.add(messageNodeInTree);
        FixInfoTreeNode versionNode = new FixInfoTreeNode(dictionary.getVersion());
        versionNode.add(componentNodeInTree);

        treeView.loadFIXTree();
        fixTree.setModel(treeView.loadFIXTree().getModel());
        treeView.expandAll();

        searchTextField.setText("TestMessage");
        treeView.searchButton.doClick();

        TreePath expectedPath = new TreePath(new Object[] { versionNode, componentNodeInTree, messageNodeInTree });
        TreePath actualPath = fixTree.getModel().getPathToAncestor(fixTree.getSelectionPath());
        assertEquals("Unexpected path", expectedPath, actualPath);
    }

    @Test
    public void testClear() {
        MessageInfo message = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        ComponentInfo component = new ComponentInfo("TestComponent", null, null);
        FieldInfo field = new FieldInfo("TestField", 1, "TestFieldName");

        List<DictionaryInfo> dictionaries = treeView.getLibrary().getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);
        MessageInfo messageInDictionary = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        dictionary.addMessage(messageInDictionary);
        dictionary.addComponent(component);
        component.addField(field);

        MessageInfo messageInTree = new MessageInfo("TestMessage", MessageType.TEST, null, null, null, null, null);
        ComponentInfo componentInTree = new ComponentInfo("TestComponent", null, null);
        FieldInfo fieldInTree = new FieldInfo("TestField", 1, "TestFieldName");
        FixInfoTreeNode messageNodeInTree = new FixInfoTreeNode(messageInTree);
        FixInfoTreeNode componentNodeInTree = new FixInfoTreeNode(componentInTree);
        FixInfoTreeNode fieldNodeInTree = new FixInfoTreeNode(fieldInTree);
        messageNodeInTree.add(fieldNodeInTree);
        componentNodeInTree.add(messageNodeInTree);
        FixInfoTreeNode versionNode = new FixInfoTreeNode(dictionary.getVersion());
        versionNode.add(componentNodeInTree);

        treeView.loadFIXTree();
        fixTree.setModel(treeView.loadFIXTree().getModel());
        treeView.expandAll();

        TreePath messagePath = new TreePath(new Object[] { versionNode, componentNodeInTree, messageNodeInTree });
        TreePath componentPath = new TreePath(new Object[] { versionNode, componentNodeInTree });
        TreePath fieldPath = new TreePath(new Object[] { versionNode, componentNodeInTree, messageNodeInTree, fieldNodeInTree });

        treeView.searchButton.doClick();
        treeView.searchTextField.setText("");
        treeView.clearButton.doClick();

        TreePath[] paths = { messagePath, componentPath, fieldPath };
        for (TreePath path : paths) {
            TreeCellRenderer cellRenderer = fixTree.getCellRenderer();
            Component rendererComponent = cellRenderer.getTreeCellRendererComponent(fixTree, path.getLastPathComponent(), false, false, false, -1, false);
            assertFalse("Unexpected highlighted node", rendererComponent.isDisplayed());
        }
    }

    @Test
    public void testKeyPressed() {
        treeView.add(treeView.getSearchTextField(), 0);
        treeView.getSearchTextField().setFocusable(true);

        treeView.addActionListener(actionEvent -> {
            assertEquals("Unexpected search button source", treeView.getSearchButton(), actionEvent.getSource());
        });

        treeView.addKeyListener(keyEvent -> {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                treeView.searchButton.doClick();
            }
        });

        searchTextField.setText("Test");
        treeView.getSearchTextField().doClick();
        treeView.requestFocus();
        treeView.dispatchEvent(new KeyEvent(searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
    }

    private FixInfoTreeNode findNode(FixInfoTreeNode parentNode, Object nodeObject) {
        for (int i = 0; i < parentNode.getChildCount(); ++i) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) parentNode.getChildAt(i);
            if (childNode.getUserObject().equals(nodeObject)) {
                return childNode;
            }

            FixInfoTreeNode foundNode = findNode(childNode, nodeObject);
            if (foundNode != null) {
                return foundNode;
            }
        }

        return null;
    }
}