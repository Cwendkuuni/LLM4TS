package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TreeViewTest {

    private Library libraryMock;
    private TreeView treeView;
    private JTree fixTree;
    private DefaultMutableTreeNode rootNode;

    @Before
    public void setUp() {
        libraryMock = mock(Library.class);
        treeView = new TreeView(libraryMock);
        MockitoAnnotations.initMocks(this);
        fixTree = treeView.getFixTree();
        rootNode = new DefaultMutableTreeNode("Dictionaries");
        treeView.loadFIXTree(rootNode);
    }

    @Test
    public void testLoadFIXTree() {
        TreeModel model = treeView.getFixTreeModel();
        assertEquals(rootNode, model.getRoot());
        verify(libraryMock).getDictionaries();
    }

    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionaryMock = mock(DictionaryInfo.class);
        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(dictionaryMock));

        MessageInfo message1 = new MessageInfo("Message1", "MT53");
        MessageInfo message2 = new MessageInfo("Message2", "MT54");
        when(dictionaryMock.getMessages()).thenReturn(Arrays.asList(message1, message2));

        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version1");
        treeView.loadMessages(versionNode, dictionaryMock);

        assertEquals(2, versionNode.getChildCount());

        FixInfoTreeNode node1 = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertEquals("Message1", node1.getUserObject());

        FixInfoTreeNode node2 = (FixInfoTreeNode) versionNode.getChildAt(1);
        assertEquals("Message2", node2.getUserObject());
    }

    @Test
    public void testLoadItems() {
        ComponentInfo componentInfo = new ComponentInfo("Component1", "C1");
        FieldInfo field1 = new FieldInfo("Field1", 1, "F1_Name");
        FieldInfo field2 = new FieldInfo("Field2", 2, "F2_Name");
        CompositeFixInfo compositeFixInfo = new CompositeFixInfo("CompositeFixInfo");
        compositeFixInfo.addItem(field1);
        compositeFixInfo.addItem(field2);

        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(mock(DictionaryInfo.class)));

        FixInfoTreeNode branchNode = new FixInfoTreeNode("BranchNode");
        treeView.loadItems(branchNode, compositeFixInfo, 0);

        assertEquals(3, branchNode.getChildCount());
        assertEquals("Field1", branchNode.getChildAt(0).getUserObject().getClass().getName());
        assertEquals("Field2", branchNode.getChildAt(1).getUserObject().getClass().getName());
        assertEquals("CompositeFixInfo", branchNode.getChildAt(2).getUserObject().getClass().getName());
    }

    @Test
    public void testSearch() {
        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(mock(DictionaryInfo.class)));

        String searchString = "Name";
        treeView.search(rootNode, searchString);

        verify(fixTree, times(1)).expandPath(argThat(new TreePathMatcher(new Object[]{"Dictionaries", "Version1", "Message1", "Field1"})));
        verify(fixTree, times(1)).expandPath(argThat(new TreePathMatcher(new Object[]{"Dictionaries", "Version1", "Message2", "Field1"})));
        verify(fixTree, times(1)).expandPath(argThat(new TreePathMatcher(new Object[]{"Dictionaries", "Version1", "Message2", "Field2"})));
    }

    @Test
    public void testClear() {
        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(mock(DictionaryInfo.class)));

        treeView.clear(rootNode);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertFalse(node.isHighlighted());
            treeView.clear(node);
        }
    }

    @Test
    public void testExpandAndCollapse() {
        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(mock(DictionaryInfo.class)));

        treeView.expandAll();
        verify(fixTree, times(30)).expandPath(any());

        treeView.collapseAll();
        verify(fixTree, times(30)).collapseRow(anyInt());
    }

    @Test
    public void testSearchButtonActionPerformed() {
        when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(mock(DictionaryInfo.class)));

        String searchString = "Name";

        treeView.searchButton.doClick();

        treeView.search(rootNode, searchString);
    }

    @Test
    public void testKeyPressed() {
        JFrame frame = new JFrame();
        frame.add(treeView);
        frame.setVisible(true);

        treeView.searchTextField.setText("Search");
        treeView.searchTextField.requestFocus();
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 0));

        treeView.search(rootNode, "Search");

        frame.dispose();
    }

    private static Matcher<TreePath> treePathMatcher(final Object[] expectedPath) {
        return new ArgumentMatcher<TreePath>() {
            @Override
            public boolean matches(TreePath actual) {
                Object[] actualPath = actual.getPath();
                if (actualPath.length != expectedPath.length) {
                    return false;
                }
                for (int i = 0; i < actualPath.length; ++i) {
                    if (!actualPath[i].equals(expectedPath[i])) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}