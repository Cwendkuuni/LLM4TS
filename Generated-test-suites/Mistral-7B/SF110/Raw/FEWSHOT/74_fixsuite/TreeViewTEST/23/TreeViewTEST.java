package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeView.class})
public class TreeViewTest {

    @Test
    public void testTreeViewConstruction() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);

        assertEquals(library, treeView.library);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);

        assertEquals(BorderLayout.CENTER, treeView.getLayout());
        assertEquals(library, treeView.library);
        assertSame(treeView, treeView.add(treeView.mainScrollPane, "Center"));
    }

    @Test
    public void testLoadFIXTree() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        DictionaryInfo dictionary1 = Mockito.mock(DictionaryInfo.class);
        DictionaryInfo dictionary2 = Mockito.mock(DictionaryInfo.class);
        MessageInfo message1 = Mockito.mock(MessageInfo.class);
        MessageInfo message2 = Mockito.mock(MessageInfo.class);
        CompositeFixInfo composite1 = Mockito.mock(CompositeFixInfo.class);
        CompositeFixInfo composite2 = Mockito.mock(CompositeFixInfo.class);
        FixInfoTreeNode root = treeView.loadFIXTree();

        Mockito.when(library.getDictionaries()).thenReturn(Arrays.asList(dictionary1, dictionary2));
        Mockito.when(dictionary1.getMessages()).thenReturn(Arrays.asList(message1));
        Mockito.when(dictionary1.getMessages()).thenReturn(Arrays.asList(composite1));
        Mockito.when(composite1.getItems()).thenReturn(Arrays.asList(message2));
        Mockito.when(dictionary2.getMessages()).thenReturn(Arrays.asList(message1, composite2));
        Mockito.when(composite2.getItems()).thenReturn(Arrays.asList(message2));

        assertEquals(1, root.getChildCount());
        assertEquals("Dictionaries", root.getUserObject());

        FixInfoTreeNode versionNode1 = (FixInfoTreeNode) root.getChildAt(0);
        assertEquals("Version1", versionNode1.getUserObject());

        assertEquals(2, versionNode1.getChildCount());

        FixInfoTreeNode node1 = (FixInfoTreeNode) versionNode1.getChildAt(0);
        assertEquals("Message1", node1.getUserObject());

        assertEquals(1, node1.getChildCount());
        assertEquals("Leaf1", node1.getChildAt(0).getUserObject());

        FixInfoTreeNode node2 = (FixInfoTreeNode) versionNode1.getChildAt(1);
        assertEquals("Component1", node2.getUserObject());

        assertEquals(1, node2.getChildCount());
        assertEquals("Leaf1", node2.getChildAt(0).getUserObject());

        FixInfoTreeNode versionNode2 = (FixInfoTreeNode) root.getChildAt(1);
        assertEquals("Version2", versionNode2.getUserObject());

        assertEquals(2, versionNode2.getChildCount());

        FixInfoTreeNode node3 = (FixInfoTreeNode) versionNode2.getChildAt(0);
        assertEquals("Message1", node3.getUserObject());

        assertEquals(1, node3.getChildCount());
        assertEquals("Leaf2", node3.getChildAt(0).getUserObject());

        FixInfoTreeNode node4 = (FixInfoTreeNode) versionNode2.getChildAt(1);
        assertEquals("Composite1", node4.getUserObject());

        assertEquals(1, node4.getChildCount());
        assertEquals("Message2", node4.getChildAt(0).getUserObject());
    }

    @Test
    public void testSearch() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "Message";

        treeView.search(root, searchString);

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            if (node.isHighlighted()) {
                assertTrue(node.getUserObject() instanceof MessageInfo);
            }
        }
    }

    @Test
    public void testClear() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "Message";

        treeView.search(root, searchString);

        treeView.clear(root);

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testExpandAll() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();

        treeView.expandAll();

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertTrue(node.isExpanded());
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    assertTrue(((FixInfoTreeNode) node.getChildAt(j)).isExpanded());
                }
            }
        }
    }

    @Test
    public void testCollapseAll() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();

        treeView.collapseAll();

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertFalse(node.isExpanded());
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    assertFalse(((FixInfoTreeNode) node.getChildAt(j)).isExpanded());
                }
            }
        }
    }

    @Test
    public void testValueChanged() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);

        TreeModel model = Mockito.mock(TreeModel.class);
        JTree tree = Mockito.mock(JTree.class);
        Mockito.when(tree.getModel()).thenReturn(model);
        Mockito.when(model.getRoot()).thenReturn(treeView.loadFIXTree());

        Mockito.when(tree.getLastSelectedPathComponent()).thenReturn(Mockito.mock(DefaultMutableTreeNode.class));

        treeView.valueChanged(Mockito.mock(TreeSelectionEvent.class));

        LibrarySuite librarySuite = PowerMockito.mock(LibrarySuite.class);
        Mockito.when(LibrarySuite.getInstance()).thenReturn(librarySuite);

        MessageInfo messageInfo = Mockito.mock(MessageInfo.class);
        ComponentInfo componentInfo = Mockito.mock(ComponentInfo.class);

        Mockito.when(messageInfo.getName()).thenReturn("Message1");
        Mockito.when(messageInfo.getMessageType()).thenReturn("MessageType1");

        Mockito.when(componentInfo.getName()).thenReturn("Component1");

        Mockito.when(librarySuite.getDetailsPanel()).thenReturn(Mockito.mock(DetailsPanel.class));

        treeView.valueChanged(Mockito.mock(TreeSelectionEvent.class));

        Mockito.verify(librarySuite.getDetailsPanel()).displayDetails(messageInfo, componentInfo);
    }

    @Test
    public void testActionPerformedCollapseAll() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();

        treeView.collapseAll();

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertFalse(node.isExpanded());
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    assertFalse(((FixInfoTreeNode) node.getChildAt(j)).isExpanded());
                }
            }
        }
    }

    @Test
    public void testActionPerformedExpandAll() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();

        treeView.expandAll();

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertTrue(node.isExpanded());
            if (node.getChildCount() > 0) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    assertTrue(((FixInfoTreeNode) node.getChildAt(j)).isExpanded());
                }
            }
        }
    }

    @Test
    public void testActionPerformedSearchButton() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "Message";

        treeView.searchTextField.setText(searchString);

        treeView.actionPerformed(Mockito.mock(ActionEvent.class));

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            if (node.isHighlighted()) {
                assertTrue(node.getUserObject() instanceof MessageInfo);
            }
        }
    }

    @Test
    public void testActionPerformedClearButton() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "Message";

        treeView.searchTextField.setText(searchString);

        treeView.search(root, searchString);

        treeView.actionPerformed(Mockito.mock(ActionEvent.class));

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testKeyPressedSearchButton() {
        Library library = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "Message";

        treeView.searchTextField.setText(searchString);

        treeView.searchTextField.doClick();
        treeView.searchTextField.sendKeyEvent(new KeyEvent(treeView, 10, 0, 0));

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(i);
            if (node.isHighlighted()) {
                assertTrue(node.getUserObject() instanceof MessageInfo);
            }
        }
    }
}