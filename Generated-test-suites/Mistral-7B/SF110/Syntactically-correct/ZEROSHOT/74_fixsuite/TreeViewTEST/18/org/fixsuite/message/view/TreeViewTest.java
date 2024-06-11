package org.fixsuite.message.view.test;

import static org.junit.Assert.*;
import org.fixsuite.message.view.*;
import org.fixsuite.message.info.*;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObject;
import org.jmock.lib.legacy.Mock;
import org.jmock.lib.legacy.Rule;
import org.jmock.lib.legacy.Sequencer;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest
{
    private Mock<Library> libraryMock;
    private TreeView treeView;

    @Before
    public void setUp()
    {
        libraryMock = new Mock<Library>();
        libraryMock.sequenceNumbers = new ArrayList<>();
        libraryMock.sequenceNumbers.add(new DictionaryInfo("Dict1", "1.0"));
        libraryMock.sequenceNumbers.add(new DictionaryInfo("Dict2", "1.1"));
        libraryMock.sequenceNumbers.add(new MessageInfo("Msg1", 1, new ComponentInfo("Comp1", "Comp1Name"), new FieldInfo("Field1", 1, "Field1Name", "Field1Tag")));
        libraryMock.sequenceNumbers.add(new MessageInfo("Msg2", 2, new ComponentInfo("Comp2", "Comp2Name"), new FieldInfo("Field2", 2, "Field2Name", "Field2Tag")));
        libraryMock.sequenceNumbers.add(new MessageInfo("Msg3", 3, new ComponentInfo("Comp3", "Comp3Name"), new FieldInfo("Field3", 3, "Field3Name", "Field3Tag")));

        Rule treeModelRule = new Rule() {
            @Override
            public Object createMock(Class type)
            {
                if (DefaultMutableTreeModel.class.equals(type)) {
                    return (DefaultMutableTreeModel)ClassImposteriser.createMock(type);
                }
                return super.createMock(type);
            }
        };
        Rule treeCellRendererRule = new Rule() {
            @Override
            public Object createMock(Class type)
            {
                if (TreeCellRenderer.class.equals(type)) {
                    return new FixInfoCellRenderer();
                }
                return super.createMock(type);
            }
        };

        Rule treeSelectionListenerRule = new Rule() {
            @Override
            public Object createMock(Class type)
            {
                if (TreeSelectionListener.class.equals(type)) {
                    return mock(TreeSelectionListener.class);
                }
                return super.createMock(type);
            }
        };

        Rule actionListenerRule = new Rule() {
            @Override
            public Object createMock(Class type)
            {
                if (ActionListener.class.equals(type)) {
                    return mock(ActionListener.class);
                }
                return super.createMock(type);
            }
        };

        Rule keyListenerRule = new Rule() {
            @Override
            public Object createMock(Class type)
            {
                if (KeyListener.class.equals(type)) {
                    return mock(KeyListener.class);
                }
                return super.createMock(type);
            }
        };

        libraryMock.treeModel = (DefaultMutableTreeModel)MockObject.mock(DefaultMutableTreeModel.class);
        libraryMock.treeCellRenderer = mock(TreeCellRenderer.class);
        libraryMock.treeSelectionListener = mock(TreeSelectionListener.class);
        libraryMock.actionListener = mock(ActionListener.class);
        libraryMock.keyListener = mock(KeyListener.class);

        treeView = new TreeView(libraryMock.proxy());

        treeView.initGUI();
    }

    @Test
    public void testLoadFIXTree()
    {
        assertEquals(3, treeView.getModel().getChildCount());

        DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)treeView.getModel().getChild(0);
        assertEquals("Dictionaries", node1.getUserObject());
        assertEquals(2, node1.getChildCount());

        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)node1.getChild(0);
        assertEquals("Dict1", node2.getUserObject());

        DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)node1.getChild(1);
        assertEquals("Dict2", node3.getUserObject());

        DefaultMutableTreeNode node4 = (DefaultMutableTreeNode)node2.getChild(0);
        assertEquals(1, node4.getChildCount());

        DefaultMutableTreeNode node5 = (DefaultMutableTreeNode)node4.getChild(0);
        assertEquals("Msg1", node5.getUserObject());

        DefaultMutableTreeNode node6 = (DefaultMutableTreeNode)node3.getChild(0);
        assertEquals(1, node6.getChildCount());

        DefaultMutableTreeNode node7 = (DefaultMutableTreeNode)node6.getChild(0);
        assertEquals("Msg2", node7.getUserObject());

        DefaultMutableTreeNode node8 = (DefaultMutableTreeNode)node1.getChild(1);
        assertEquals(1, node8.getChildCount());

        DefaultMutableTreeNode node9 = (DefaultMutableTreeNode)node8.getChild(0);
        assertEquals("Msg3", node9.getUserObject());
    }

    @Test
    public void testLoadMessages()
    {
        verify(libraryMock.treeModel).insertNode(eq(new FixInfoTreeNode("Dict1 (1.0)")), argThat(new IsInstanceOf(DefaultMutableTreeNode.class)), anyInt());
        verify(libraryMock.treeModel).insertNode(eq(new FixInfoTreeNode("Msg1 (Comp1Name)")), argThat(new IsInstanceOf(DefaultMutableTreeNode.class)), anyInt());

        verify(libraryMock.treeModel).insertNode(eq(new FixInfoTreeNode("Dict2 (1.1)")), argThat(new IsInstanceOf(DefaultMutableTreeNode.class)), anyInt());
        verify(libraryMock.treeModel).insertNode(eq(new FixInfoTreeNode("Msg2 (Comp2Name)")), argThat(new IsInstanceOf(DefaultMutableTreeNode.class)), anyInt());
        verify(libraryMock.treeModel).insertNode(eq(new FixInfoTreeNode("Msg3 (Comp3Name)")), argThat(new IsInstanceOf(DefaultMutableTreeNode.class)), anyInt());
    }

    @Test
    public void testLoadItems()
    {
        assertEquals(1, ((DefaultMutableTreeNode)treeView.getModel().getChild(0)).getChildCount());

        assertEquals(1, ((DefaultMutableTreeNode)treeView.getModel().getChild(0).getChild(0)).getChildCount());

        assertEquals(3, ((DefaultMutableTreeNode)treeView.getModel().getChild(1)).getChildCount());

        DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)treeView.getModel().getChild(0).getChild(0);
        assertEquals("Msg1", node1.getUserObject());
        assertEquals(1, node1.getChildCount());

        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)node1.getChild(0);
        assertEquals("Field1Name", node2.getUserObject());

        DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)treeView.getModel().getChild(1).getChild(0);
        assertEquals("Msg2", node3.getUserObject());
        assertEquals(1, node3.getChildCount());

        DefaultMutableTreeNode node4 = (DefaultMutableTreeNode)node3.getChild(0);
        assertEquals("Field2Name", node4.getUserObject());

        DefaultMutableTreeNode node5 = (DefaultMutableTreeNode)treeView.getModel().getChild(1).getChild(1);
        assertEquals("Msg3", node5.getUserObject());
        assertEquals(1, node5.getChildCount());

        DefaultMutableTreeNode node6 = (DefaultMutableTreeNode)node5.getChild(0);
        assertEquals("Field3Name", node6.getUserObject());
    }

    @Test
    public void testExpandAll()
    {
        treeView.expandAll();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            treeView.expandRow(i);
        }

        assertEquals(true, treeView.isExpanded(0));
        assertEquals(true, treeView.isExpanded(1));
        assertEquals(true, treeView.isExpanded(2));
        assertEquals(true, treeView.isExpanded(3));
        assertEquals(true, treeView.isExpanded(4));
        assertEquals(true, treeView.isExpanded(5));
        assertEquals(true, treeView.isExpanded(6));
    }

    @Test
    public void testCollapseAll()
    {
        treeView.expandAll();
        treeView.collapseAll();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            treeView.collapseRow(i);
        }

        assertEquals(false, treeView.isExpanded(0));
        assertEquals(false, treeView.isExpanded(1));
        assertEquals(false, treeView.isExpanded(2));
        assertEquals(false, treeView.isExpanded(3));
        assertEquals(false, treeView.isExpanded(4));
        assertEquals(false, treeView.isExpanded(5));
        assertEquals(false, treeView.isExpanded(6));
    }

    @Test
    public void testSearch()
    {
        treeView.searchTextField.setText("Name");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);

            if (((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof MessageInfo || ((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof FieldInfo) {
                assertTrue(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
            else {
                assertFalse(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
        }

        treeView.searchTextField.setText("Tag");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);

            if (((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof FieldInfo) {
                assertTrue(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
            else {
                assertFalse(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
        }

        treeView.searchTextField.setText("");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);
            assertFalse(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
        }
    }

    @Test
    public void testClear()
    {
        treeView.searchTextField.setText("Name");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);

            if (((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof MessageInfo || ((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof FieldInfo) {
                assertTrue(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
        }

        treeView.clearButton.doClick();

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);
            assertFalse(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
        }
    }

    @Test
    public void testKeyEvent()
    {
        treeView.searchTextField.setText("Name");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 10, true, true, 0, 'Enter'));

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);

            if (((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof MessageInfo || ((FixInfoTreeNode)node.getLastPathComponent()).getUserObject() instanceof FieldInfo) {
                assertTrue(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
            }
        }

        treeView.searchTextField.setText("");
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, 10, true, true, 0, 'Enter'));

        for (int i = 0; i < treeView.getModel().getRowCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeView.getModel().getPathForRow(i);
            assertFalse(((FixInfoTreeNode)node.getLastPathComponent()).isHighlighted());
        }
    }
}