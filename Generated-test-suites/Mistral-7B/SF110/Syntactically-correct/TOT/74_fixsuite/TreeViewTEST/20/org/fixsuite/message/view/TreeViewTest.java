package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.junit.Before;
import org.junit.Test;

import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;

public class TreeViewTest
{
    private Library library;
    private TreeView treeView;

    @Before
    public void setUp()
    {
        library = new Library();
        library.addDictionary(new DictionaryInfo("TestDict1", Arrays.asList(new MessageInfo("Msg1", "Test Type1"), new MessageInfo("Msg2", "Test Type2"), new MessageInfo("Msg3", "Test Type3"))));
        library.addDictionary(new DictionaryInfo("TestDict2", Arrays.asList(new MessageInfo("Msg4", "Test Type4"), new MessageInfo("Msg5", "Test Type5"), new MessageInfo("Msg6", "Test Type6"))));
        treeView = new TreeView(library);
    }

    @Test
    public void testInitGUI()
    {
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
        assertInstanceOf(JToolBar.class, treeView.getToolbar());
        assertFalse(treeView.isExpanded());
        assertFalse(treeView.isCollapsed());
    }

    @Test
    public void testLoadFIXTree()
    {
        final FixInfoTreeNode rootNode = treeView.getFixTree().getModel().getRoot();
        assertEquals("Dictionaries", rootNode.getUserObject());
        assertEquals(2, rootNode.getChildCount());

        for (int i = 0; i < rootNode.getChildCount(); ++i)
        {
            final FixInfoTreeNode childNode = (FixInfoTreeNode)rootNode.getChildAt(i);
            assertInstanceOf(DictionaryInfo.class, childNode.getUserObject());
            assertEquals(library.getDictionaryNames().get(i), childNode.getUserObject().getVersion());
        }
    }

    @Test
    public void testLoadMessages()
    {
        final FixInfoTreeNode dictNode1 = treeView.getFixTree().getModel().getPathToRoot(treeView.getFixTree().getModel().getPathForRow(0)).getLastPathComponent();
        final List<MessageInfo> msgList1 = ((DictionaryInfo)dictNode1.getUserObject()).getMessages();
        assertEquals(3, msgList1.size());

        for (int i = 0; i < msgList1.size(); ++i)
        {
            final MessageInfo msgInfo1 = msgList1.get(i);
            final FixInfoTreeNode msgNode1 = findMessageNode(dictNode1, msgInfo1.getName());
            assertNotNull(msgNode1);
            assertEquals(msgInfo1.getName(), msgNode1.getUserObject().getName());
            assertEquals(msgInfo1.getMessageType(), msgNode1.getUserObject().getMessageType());
        }
    }

    @Test
    public void testLoadItems()
    {
        final FixInfoTreeNode dictNode1 = treeView.getFixTree().getModel().getPathToRoot(treeView.getFixTree().getModel().getPathForRow(0)).getLastPathComponent();
        final MessageInfo msgInfo1 = msgList1.get(0);
        final FixInfoTreeNode msgNode1 = findMessageNode(dictNode1, msgInfo1.getName());

        final FixInfoTreeNode itemNode1 = ((MessageInfo)msgNode1.getUserObject()).getItems().get(0);
        final FixInfoTreeNode itemNode2 = ((MessageInfo)msgNode1.getUserObject()).getItems().get(1);

        assertEquals(2, msgNode1.getChildCount());
        assertEquals(2, itemNode1.getChildCount());
        assertEquals("Tag1", ((FieldInfo)itemNode1.getChildAt(0).getUserObject()).getName());
        assertEquals("Tag2", ((FieldInfo)itemNode1.getChildAt(1).getUserObject()).getName());
        assertEquals("Component1", ((ComponentInfo)itemNode2.getUserObject()).getName());

        assertFalse(itemNode1.isLeaf());
        assertTrue(itemNode2.isLeaf());
    }

    @Test
    public void testExpandAll()
    {
        treeView.expandAll();
        assertTrue(treeView.isExpanded());
        assertEquals(10, treeView.getFixTree().getRowCount());
    }

    @Test
    public void testCollapseAll()
    {
        treeView.expandAll();
        treeView.collapseAll();
        assertFalse(treeView.isExpanded());
        assertEquals(3, treeView.getFixTree().getRowCount());
    }

    @Test
    public void testSearch()
    {
        treeView.search("Type");
        assertEquals(6, treeView.getFixTree().getRowCount());

        List<FixInfoTreeNode> nodes = treeView.getSearchResultNodes();
        assertEquals(6, nodes.size());

        for (int i = 0; i < nodes.size(); ++i)
        {
            final FixInfoTreeNode node = nodes.get(i);
            if (i < 3)
            {
                assertTrue(node.isHighlighted());
                assertEquals("TestDict1", ((DictionaryInfo)node.getParent(1).getUserObject()).getVersion());
            }
            else
            {
                assertFalse(node.isHighlighted());
                assertEquals("TestDict2", ((DictionaryInfo)node.getParent(1).getUserObject()).getVersion());
            }
        }
    }

    @Test
    public void testClear()
    {
        treeView.search("Type");
        treeView.clear();
        assertEquals(3, treeView.getFixTree().getRowCount());
        assertNull(treeView.getFixTree().getSelectionPath());
    }

    @Test
    public void testKeyPress()
    {
        treeView.requestFocus();
        treeView.doClick();
        treeView.searchTextField.setText("Type");
        treeView.searchTextField.sendKeyEvent(new KeyEvent(treeView.searchTextField, 10));
        assertEquals(6, treeView.getFixTree().getRowCount());
    }

    private MessageInfo getMessageInfo(final String name, final String messageType)
    {
        for (final MessageInfo msgInfo : library.getAllMessages())
        {
            if (msgInfo.getName().equals(name) && msgInfo.getMessageType().equals(messageType))
            {
                return msgInfo;
            }
        }
        throw new RuntimeException("Message not found");
    }

    private FixInfoTreeNode findMessageNode(final FixInfoTreeNode parentNode, final String name)
    {
        for (final FixInfoTreeNode node : parentNode.getChildren())
        {
            if (node.getUserObject() instanceof MessageInfo)
            {
                final MessageInfo msgInfo = (MessageInfo)node.getUserObject();
                if (msgInfo.getName().equals(name))
                {
                    return node;
                }
            }
            else if (node.getChildCount() > 0)
            {
                final FixInfoTreeNode childNode = findMessageNode((FixInfoTreeNode)node, name);
                if (childNode != null)
                {
                    return childNode;
                }
            }
        }
        return null;
    }

    private List<MessageInfo> msgList1 = Arrays.asList(getMessageInfo("Msg1", "Test Type1"), getMessageInfo("Msg2", "Test Type2"), getMessageInfo("Msg3", "Test Type3"));
}