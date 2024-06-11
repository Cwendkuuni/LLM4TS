package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private JFrame frame;

    @Before
    public void setUp() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo("1.0");
        dictionary.addMessage(new MessageInfo("TestMessage", "TestMessageType"));
        library.addDictionary(dictionary);

        treeView = new TreeView(library);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(treeView);
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.getMainScrollPane());
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
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        assertEquals(1, rootNode.getChildCount());
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals(1, dictionaryNode.getChildCount());
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals(0, messageNode.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals(1, dictionaryNode.getChildCount());
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals(1, messageNode.getChildCount());
        DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        assertEquals(0, fieldNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);

        DefaultMutableTreeNode fieldNode1 = new DefaultMutableTreeNode(new FieldInfo("TestField", 1));
        DefaultMutableTreeNode fieldNode2 = new DefaultMutableTreeNode(new FieldInfo("AnotherTestField", 1));
        messageNode.add(fieldNode1);
        messageNode.add(fieldNode2);

        treeView.loadItems(messageNode, (MessageInfo) messageNode.getUserObject(), 0);

        assertEquals(2, messageNode.getChildCount());
        assertTrue(messageNode.isLeaf());
    }

    @Test
    public void testExpandAll() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        treeView.expandAll();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertTrue(node.isExpanded());
        }
    }

    @Test
    public void testCollapseAll() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        treeView.collapseAll();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertFalse(node.isExpanded());
        }
    }

    @Test
    public void testSearch() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();

        String searchString = "Test";

        treeView.search(rootNode, searchString);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode) node.getChildAt(j);
                    if (leafNode.getUserObject() instanceof FieldInfo) {
                        FieldInfo fieldInfo = (FieldInfo) leafNode.getUserObject();
                        assertTrue(fieldInfo.getName().matches(searchString) || fieldInfo.getTagNumber().matches(searchString));
                    }
                }
            }
        }
    }

    @Test
    public void testClear() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();

        String searchString = "Test";
        treeView.search(rootNode, searchString);

        treeView.clear(rootNode);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    DefaultMutableTreeNode leafNode = (DefaultMutableTreeNode) node.getChildAt(j);
                    assertFalse(leafNode.isHighlighted());
                }
            } else {
                assertNull(node.getUserObject());
            }
        }
    }

    @Test
    public void testValueChanged() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();

        List<Class<? extends FixInfo>> testFixInfoTypes = Arrays.asList(MessageInfo.class, ComponentInfo.class, FieldInfo.class);

        for (Class<? extends FixInfo> fixInfoType : testFixInfoTypes) {
            try {
                FixInfo fixInfo = fixInfoType.newInstance();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fixInfo);
                rootNode.add(node);

                treeView.valueChanged(new TreeSelectionEvent(treeView.getFixTree(), new TreePath(node.getPath())));

                LibrarySuite librarySuite = LibrarySuite.getInstance();
                assertNotNull(librarySuite);
                assertNull(librarySuite.getDetailsPanel().getDisplayedInfo());

                TreeCellRenderer cellRenderer = treeView.getFixTree().getCellRenderer();
                if (fixInfoType.isInstance(node.getUserObject())) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof MessageInfo) {
                        MessageInfo messageInfo = (MessageInfo) userObject;
                        assertEquals(messageInfo.getName(), librarySuite.getDetailsPanel().getDisplayedInfo().getName());
                        assertEquals(messageInfo.getMessageType(), librarySuite.getDetailsPanel().getDisplayedInfo().getMessageType());
                    } else if (userObject instanceof ComponentInfo) {
                        ComponentInfo componentInfo = (ComponentInfo) userObject;
                        assertEquals(componentInfo.getName(), librarySuite.getDetailsPanel().getDisplayedInfo().getName());
                    } else if (userObject instanceof FieldInfo) {
                        FieldInfo fieldInfo = (FieldInfo) userObject;
                        assertEquals(fieldInfo.getName(), librarySuite.getDetailsPanel().getDisplayedInfo().getName());
                        assertEquals(fieldInfo.getTagNumber(), librarySuite.getDetailsPanel().getDisplayedInfo().getTagNumber());
                    }
                }

                treeView.getFixTree().setModel(new DefaultTreeModel(Collections.EMPTY_NODE));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testActionPerformed() {
        treeView.addActionListener(e -> {});

        treeView.expandButton.doClick();
        treeView.collapseButton.doClick();
        treeView.searchButton.doClick();
        treeView.clearButton.doClick();
    }

    @Test
    public void testKeyPressed() {
        treeView.addKeyListener(e -> {});

        treeView.getFixTree().requestFocus();
        treeView.getFixTree().dispatchEvent(new KeyEvent(treeView.getFixTree(), KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER));
    }
}