package org.fixsuite.message.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.view.TreeView.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView.FixInfoCellRenderer;

public class TreeViewTest
{
    private TreeView treeView;
    private JTree jTree;
    private DefaultTreeModel treeModel;
    private TreeCellRenderer cellRenderer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp()
    {
        final List<DictionaryInfo> dictionaryInfos = Arrays.asList(new DictionaryInfo("Dict1", "Version1"), new DictionaryInfo("Dict2", "Version2"));
        final Library library = new Library(dictionaryInfos);
        treeView = new TreeView(library);
        jTree = treeView.getFixTree();
        treeModel = (DefaultTreeModel)jTree.getModel();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testInitGUI()
    {
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree()
    {
        final FixInfoTreeNode rootNode = (FixInfoTreeNode)treeModel.getRoot();
        assertEquals("Dictionaries", rootNode.getUserObject());
        assertFalse(rootNode.isLeaf());

        int count = 0;
        while (!rootNode.isLeaf())
        {
            final FixInfoTreeNode node = (FixInfoTreeNode)rootNode.getChildAt(count);
            assertEquals(String.valueOf(dictionaryInfos.get(count).getVersion()), node.getUserObject());
            assertFalse(node.isLeaf());
            count++;
        }

        assertEquals(2, rootNode.getChildCount());
    }

    @Test
    public void testLoadMessages()
    {
        final FixInfoTreeNode dictNode = (FixInfoTreeNode)treeModel.getChild(0);
        assertEquals(dictionaryInfos.get(0).getVersion(), dictNode.getUserObject());

        int count = 0;
        while (!dictNode.isLeaf())
        {
            final FixInfoTreeNode node = (FixInfoTreeNode)dictNode.getChildAt(count);
            final MessageInfo messageInfo = dictionaryInfos.get(0).getMessages().get(count);
            assertEquals(messageInfo.getName(), node.getUserObject().toString());
            assertFalse(node.isLeaf());
            count++;
        }

        assertEquals(dictionaryInfos.get(0).getMessages().size(), dictNode.getChildCount());
    }

    @Test
    public void testLoadItems()
    {
        final FixInfoTreeNode dictNode = (FixInfoTreeNode)treeModel.getChild(0);
        final MessageInfo messageInfo = dictionaryInfos.get(0).getMessages().get(0);

        int count = 0;
        while (!messageInfo.getItems().isEmpty())
        {
            final FixInfoTreeNode node = (FixInfoTreeNode)dictNode.getChildAt(count);
            final FixInfo info = messageInfo.getItems().get(count);
            final Object expectedUserObject = info instanceof ComponentInfo ? ((ComponentInfo)info).getName() : info.getMessageType();
            assertEquals(expectedUserObject, node.getUserObject());
            assertTrue(node.isLeaf());
            count++;
        }

        assertEquals(messageInfo.getItems().size(), dictNode.getChildCount());
    }

    @Test
    public void testExpandAll()
    {
        treeView.expandAll();
        final int treeSize = treeModel.getRowCount();
        for (int i = 0; i < treeSize; i++)
        {
            treeView.fixTree.expandRow(i);
        }

        for (int i = 0; i < treeSize; i++)
        {
            assertTrue(treeView.fixTree.isExpanded(new TreePath(new Object[] { i })));
        }
    }

    @Test
    public void testCollapseAll()
    {
        treeView.collapseAll();
        for (int i = 0; i < treeModel.getRowCount(); i++)
        {
            treeView.fixTree.collapseRow(i);
        }

        for (int i = 0; i < treeModel.getRowCount(); i++)
        {
            assertFalse(treeView.fixTree.isExpanded(new TreePath(new Object[] { i })));
        }
    }

    @Test
    public void testSearch()
    {
        String searchString = "message";

        treeView.searchTextField.setText(searchString);
        treeView.searchButton.doClick();

        for (int i = 0; i < treeModel.getRowCount(); i++)
        {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeModel.getChild(i);
            final FixInfoTreeNode fixInfoTreeNode = (FixInfoTreeNode)node;
            if (fixInfoTreeNode.isHighlighted())
            {
                if (fixInfoTreeNode.getUserObject() instanceof MessageInfo)
                {
                    final MessageInfo messageInfo = (MessageInfo)fixInfoTreeNode.getUserObject();
                    assertTrue(messageInfo.getName().matches(searchString) || messageInfo.getMessageType().matches(searchString));
                }
                else if (fixInfoTreeNode.getUserObject() instanceof FieldInfo)
                {
                    final FieldInfo fieldInfo = (FieldInfo)fixInfoTreeNode.getUserObject();
                    assertTrue(fieldInfo.getName().matches(searchString) || fieldInfo.getTagNumber().matches(searchString));
                }
                else if (fixInfoTreeNode.getUserObject() instanceof ComponentInfo)
                {
                    final ComponentInfo componentInfo = (ComponentInfo)fixInfoTreeNode.getUserObject();
                    assertTrue(componentInfo.getName().matches(searchString));
                }
            }
            else
            {
                assertFalse(fixInfoTreeNode.isHighlighted());
            }
        }
    }

    @Test
    public void testClear()
    {
        treeView.searchTextField.setText("message");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeModel.getRowCount(); i++)
        {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeModel.getChild(i);
            final FixInfoTreeNode fixInfoTreeNode = (FixInfoTreeNode)node;
            if (fixInfoTreeNode.isHighlighted())
            {
                treeView.clear((FixInfoTreeNode)node
                        .getPath()[0]);
                assertFalse(fixInfoTreeNode.isHighlighted());
            }
        }

        assertEquals(0, treeModel.getRowCount());
    }

    @Test
    public void testCellRenderer()
    {
        treeView.searchTextField.setText("component");
        treeView.searchButton.doClick();

        for (int i = 0; i < treeModel.getRowCount(); i++)
        {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeModel.getChild(i);
            final FixInfoTreeNode fixInfoTreeNode = (FixInfoTreeNode)node;
            if (fixInfoTreeNode.isHighlighted())
            {
                final ComponentInfo componentInfo = (ComponentInfo)fixInfoTreeNode.getUserObject();
                final ComponentInfo expectedComponentInfo = dictionaryInfos.get(0).getComponents().get(i);
                assertEquals(expectedComponentInfo.getName(), cellRenderer.getTreeCellRendererComponent(jTree, fixInfoTreeNode, true, false, false, i, false).getText());
            }
        }
    }

    @After
    public void tearDown()
    {
        treeView = null;
    }
}