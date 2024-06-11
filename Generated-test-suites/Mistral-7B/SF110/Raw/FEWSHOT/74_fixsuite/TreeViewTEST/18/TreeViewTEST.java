package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TreeViewTest {

    @Test
    public void testTreeViewInitialization() {
        Library libraryMock = PowerMockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
    }

    @Test
    public void testLoadFIXTree() {
        Library libraryMock = PowerMockito.mock(Library.class);
        DictionaryInfo dictionary1Mock = PowerMockito.mock(DictionaryInfo.class);
        DictionaryInfo dictionary2Mock = PowerMockito.mock(DictionaryInfo.class);
        MessageInfo message1Mock = PowerMockito.mock(MessageInfo.class);
        MessageInfo message2Mock = PowerMockito.mock(MessageInfo.class);
        FieldInfo field1Mock = PowerMockito.mock(FieldInfo.class);
        FieldInfo field2Mock = PowerMockito.mock(FieldInfo.class);
        ComponentInfo component1Mock = PowerMockito.mock(ComponentInfo.class);
        ComponentInfo component2Mock = PowerMockito.mock(ComponentInfo.class);

        PowerMockito.when(libraryMock.getDictionaries()).thenReturn(Arrays.asList(dictionary1Mock, dictionary2Mock));
        PowerMockito.when(dictionary1Mock.getMessages()).thenReturn(Arrays.asList(message1Mock, message2Mock));
        PowerMockito.when(message1Mock.getItems()).thenReturn(Arrays.asList(field1Mock, component1Mock));
        PowerMockito.when(message2Mock.getItems()).thenReturn(Arrays.asList(field2Mock, component2Mock));
        PowerMockito.when(field1Mock.getTagNumber()).thenReturn(1);
        PowerMockito.when(field2Mock.getTagNumber()).thenReturn(2);
        PowerMockito.when(component1Mock.getName()).thenReturn("Component1");
        PowerMockito.when(component2Mock.getName()).thenReturn("Component2");

        TreeModel treeModel = treeView.loadFIXTree();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        assertEquals(2, rootNode.getChildCount());

        DefaultMutableTreeNode versionNode1 = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals(2, versionNode1.getChildCount());

        DefaultMutableTreeNode node1_1 = (DefaultMutableTreeNode) versionNode1.getChildAt(0);
        assertEquals(2, node1_1.getChildCount());

        DefaultMutableTreeNode node1_1_1 = (DefaultMutableTreeNode) node1_1.getChildAt(0);
        assertEquals(2, node1_1_1.getChildCount());
        assertEquals(message1Mock, node1_1_1.getUserObject());

        DefaultMutableTreeNode node1_1_2 = (DefaultMutableTreeNode) node1_1.getChildAt(1);
        assertEquals(2, node1_1_2.getChildCount());
        assertEquals(component1Mock, node1_1_2.getUserObject());

        DefaultMutableTreeNode versionNode2 = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        assertEquals(2, versionNode2.getChildCount());

        DefaultMutableTreeNode node2_1 = (DefaultMutableTreeNode) versionNode2.getChildAt(0);
        assertEquals(2, node2_1.getChildCount());

        DefaultMutableTreeNode node2_1_1 = (DefaultMutableTreeNode) node2_1.getChildAt(0);
        assertEquals(2, node2_1_1.getChildCount());
        assertEquals(message2Mock, node2_1_1.getUserObject());

        DefaultMutableTreeNode node2_1_2 = (DefaultMutableTreeNode) node2_1.getChildAt(1);
        assertEquals(2, node2_1_2.getChildCount());
        assertEquals(component2Mock, node2_1_2.getUserObject());
    }

    @Test
    public void testSearch() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        JTree tree = treeView.fixTree;
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        MessageInfo message1Mock = Mockito.mock(MessageInfo.class);
        FieldInfo field1Mock = Mockito.mock(FieldInfo.class);
        FieldInfo field2Mock = Mockito.mock(FieldInfo.class);
        ComponentInfo component1Mock = Mockito.mock(ComponentInfo.class);
        ComponentInfo component2Mock = Mockito.mock(ComponentInfo.class);

        PowerMockito.when(libraryMock.getDictionaries()).thenReturn(Arrays.asList(Mockito.mock(DictionaryInfo.class)));
        PowerMockito.when(libraryMock.getDictionaries().get(0).getMessages()).thenReturn(Arrays.asList(message1Mock, message2Mock));
        PowerMockito.when(message1Mock.getItems()).thenReturn(Arrays.asList(field1Mock, component1Mock));
        PowerMockito.when(message2Mock.getItems()).thenReturn(Arrays.asList(field2Mock, component2Mock));
        PowerMockito.when(field1Mock.getName()).thenReturn("Field1");
        PowerMockito.when(field1Mock.getTagNumber()).thenReturn(1);
        PowerMockito.when(field2Mock.getName()).thenReturn("Field2");
        PowerMockito.when(field2Mock.getTagNumber()).thenReturn(2);
        PowerMockito.when(component1Mock.getName()).thenReturn("Component1");
        PowerMockito.when(component2Mock.getName()).thenReturn("Component2");

        treeView.search((DefaultMutableTreeNode) treeModel.getRoot(), "Field");

        DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) treeModel.nodeForPath(new TreePath(new Object[]{"Dictionaries", "0", "Message1", "Field1"}));
        assertTrue(node1.isHighlighted());

        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) treeModel.nodeForPath(new TreePath(new Object[]{"Dictionaries", "0", "Message1", "Field2"}));
        assertTrue(node2.isHighlighted());

        DefaultMutableTreeNode node3 = (DefaultMutableTreeNode) treeModel.nodeForPath(new TreePath(new Object[]{"Dictionaries", "0", "Message2", "Field1"}));
        assertFalse(node3.isHighlighted());

        DefaultMutableTreeNode node4 = (DefaultMutableTreeNode) treeModel.nodeForPath(new TreePath(new Object[]{"Dictionaries", "0", "Message2", "Component1"}));
        assertFalse(node4.isHighlighted());
    }

    @Test
    public void testClear() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        JTree tree = treeView.fixTree;
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();

        TreeModel originalTreeModel = treeModel;

        treeView.clear((DefaultMutableTreeNode) treeModel.getRoot());

        assertNotSame(originalTreeModel, treeModel);
        assertTrue(treeModel.getRowCount() == 0);
    }

    @Test
    public void testKeyPressed() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        JTree tree = treeView.fixTree;
        treeView.searchTextField = Mockito.spy(new JTextField());

        treeView.actionPerformed(Mockito.mock(ActionEvent.class));

        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));

        verify(treeView).search((DefaultMutableTreeNode) treeView.fixTree.getModel().getRoot(), treeView.searchTextField.getText().trim());
    }
}