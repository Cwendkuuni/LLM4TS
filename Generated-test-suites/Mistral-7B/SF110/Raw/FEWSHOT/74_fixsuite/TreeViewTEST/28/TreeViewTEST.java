package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
public class TreeViewTest {

    private Library library;
    private TreeView treeView;
    private JTree fixTree;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        library = Mockito.mock(Library.class);
        treeView = new TreeView(library);
        fixTree = treeView.fixTree;
    }

    @Test
    public void testLoadFIXTree() {
        final DictionaryInfo dictionary = new DictionaryInfo("Dictionary1", "1.0.0");
        Mockito.when(library.getDictionaries()).thenReturn(Arrays.asList(dictionary));
        final FixInfoTreeNode root = treeView.loadFIXTree();
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());
        final FixInfoTreeNode child = (FixInfoTreeNode) root.getChildAt(0);
        assertEquals(dictionary.getVersion(), child.getUserObject());
        assertEquals("1.0.0", child.getUserObject().toString());
    }

    @Test
    public void testLoadMessages() {
        final DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
        Mockito.when(library.getDictionaries()).thenReturn(Collections.singletonList(dictionary));
        Mockito.when(dictionary.getMessages()).thenReturn(Collections.singletonList(new MessageInfo("Message1", "MessageType1")));

        final FixInfoTreeNode root = treeView.loadFIXTree();
        final FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(0);
        treeView.loadMessages(node, dictionary);
        assertEquals(1, node.getChildCount());
        final FixInfoTreeNode child = (FixInfoTreeNode) node.getChildAt(0);
        assertEquals("Message1", child.getUserObject().getName());
        assertEquals("MessageType1", child.getUserObject().getMessageType());
        assertEquals("Message1 (MessageType1)", child.toString());
    }

    @Test
    public void testLoadItems() {
        final CompositeFixInfo compositeFixInfo = new CompositeFixInfo("Component1");
        Mockito.when(compositeFixInfo.getItems()).thenReturn(Arrays.asList(new FieldInfo("Field1", "Tag1", "Name1"), new FieldInfo("Field2", "Tag2", "Name2")));

        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(1);

        treeView.loadItems(root, compositeFixInfo, 0);
        assertEquals(2, root.getChildCount());
        for (int i = 0; i < root.getChildCount(); ++i) {
            final FixInfoTreeNode child = (FixInfoTreeNode) root.getChildAt(i);
            assertEquals("Field" + (i + 1), child.getUserObject().getName());
            assertEquals("Tag" + (i + 1), "" + ((FieldInfo) child.getUserObject()).getTagNumber());
        }
    }

    @Test
    public void testExpandAll() {
        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(3);
        treeView.expandAll();
        for (int i = 0; i < root.getChildCount(); ++i) {
            final FixInfoTreeNode child = (FixInfoTreeNode) root.getChildAt(i);
            Mockito.verify(child).expand();
        }
    }

    @Test
    public void testCollapseAll() {
        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(3);
        treeView.collapseAll();
        for (int i = 0; i < root.getChildCount(); ++i) {
            final FixInfoTreeNode child = (FixInfoTreeNode) root.getChildAt(i);
            Mockito.verify(child).collapse();
        }
    }

    @Test
    public void testSearch() {
        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(3);

        final MessageInfo messageInfo1 = new MessageInfo("Message1", "MessageType1");
        final MessageInfo messageInfo2 = new MessageInfo("Message2", "MessageType2");
        final FieldInfo fieldInfo1 = new FieldInfo("Field1", "Tag1", "Name1");
        final FieldInfo fieldInfo2 = new FieldInfo("Field2", "Tag2", "Name2");

        final FixInfoTreeNode messageNode1 = new FixInfoTreeNode(messageInfo1);
        final FixInfoTreeNode messageNode2 = new FixInfoTreeNode(messageInfo2);
        final FixInfoTreeNode fieldNode1 = new FixInfoTreeNode(fieldInfo1);
        final FixInfoTreeNode fieldNode2 = new FixInfoTreeNode(fieldInfo2);

        Mockito.when(root.getChildAt(0)).thenReturn(messageNode1);
        Mockito.when(messageNode1.getChildAt(0)).thenReturn(fieldNode1);
        Mockito.when(messageNode1.getChildCount()).thenReturn(1);
        Mockito.when(messageNode2.getChildCount()).thenReturn(1);

        treeView.search(root, "Name1");
        Mockito.verify(fieldNode1).setHighlighted(true);
        Mockito.verify(messageNode1).setHighlighted(true);

        treeView.search(root, "Tag1");
        Mockito.verify(fieldNode1).setHighlighted(true);
        Mockito.verify(messageNode1).setHighlighted(true);

        treeView.search(root, "MessageType1");
        Mockito.verify(messageNode1).setHighlighted(true);

        treeView.search(root, "Name2");
        assertNull(fieldNode2.getHighlighted());
        Mockito.verify(messageNode2, Mockito.never()).setHighlighted();

        treeView.search(root, "MessageType2");
        assertNull(messageNode2.getHighlighted());
        Mockito.verify(fieldNode2, Mockito.never()).setHighlighted();

        treeView.search(root, "Field");
        for (int i = 0; i < root.getChildCount(); ++i) {
            final FixInfoTreeNode child = (FixInfoTreeNode) root.getChildAt(i);
            if (child instanceof FixInfoTreeNode && !child.isHighlighted()) {
                Mockito.verify(child, Mockito.never()).setHighlighted();
            }
        }
        Mockito.verify(messageNode1).setHighlighted();
        Mockito.verify(fieldNode1).setHighlighted();
        Mockito.verify(messageNode2).setHighlighted(false);
        Mockito.verify(fieldNode2).setHighlighted(false);
    }

    @Test
    public void testClear() {
        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(3);

        final MessageInfo messageInfo1 = new MessageInfo("Message1", "MessageType1");
        final MessageInfo messageInfo2 = new MessageInfo("Message2", "MessageType2");
        final FieldInfo fieldInfo1 = new FieldInfo("Field1", "Tag1", "Name1");
        final FieldInfo fieldInfo2 = new FieldInfo("Field2", "Tag2", "Name2");

        final FixInfoTreeNode messageNode1 = new FixInfoTreeNode(messageInfo1);
        final FixInfoTreeNode messageNode2 = new FixInfoTreeNode(messageInfo2);
        final FixInfoTreeNode fieldNode1 = new FixInfoTreeNode(fieldInfo1);
        final FixInfoTreeNode fieldNode2 = new FixInfoTreeNode(fieldInfo2);

        Mockito.when(root.getChildAt(0)).thenReturn(messageNode1);
        Mockito.when(messageNode1.getChildAt(0)).thenReturn(fieldNode1);
        Mockito.when(messageNode1.getChildCount()).thenReturn(1);
        Mockito.when(messageNode2.getChildCount()).thenReturn(1);

        treeView.search(root, "Name1");
        treeView.clear(root);
        Mockito.verify(fieldNode1, Mockito.never()).setHighlighted();
        Mockito.verify(messageNode1).setHighlighted(false);
        Mockito.verify(messageNode2).setHighlighted(false);
        Mockito.verify(fieldNode2).setHighlighted(false);
    }

    @Test
    public void testKeyEvents() {
        final FixInfoTreeNode root = Mockito.mock(FixInfoTreeNode.class);
        Mockito.when(root.getUserObject()).thenReturn("Dictionaries");
        Mockito.when(root.getChildCount()).thenReturn(3);

        final KeyEvent keyEvent = PowerMockito.mock(KeyEvent.class);
        PowerMockito.when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        treeView.valueChanged(Mockito.mock(TreeSelectionEvent.class));
        treeView.keyPressed(keyEvent);
        Mockito.verify(treeView).search(root, "Name1");
    }
}