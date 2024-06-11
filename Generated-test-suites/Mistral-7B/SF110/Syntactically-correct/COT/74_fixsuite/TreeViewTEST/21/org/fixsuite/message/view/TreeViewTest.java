package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeView.class})
public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private Library library;
    private List<DictionaryInfo> dictionaryList;

    @Before
    public void setUp() {
        library = PowerMockito.mock(Library.class);
        dictionaryList = Arrays.asList(new DictionaryInfo("Dictionary1", "Version1"), new DictionaryInfo("Dictionary2", "Version2"));
        PowerMockito.whenNew(Library.class).withNoArguments().thenReturn(library);
        PowerMockito.when(library.getDictionaries()).thenReturn(dictionaryList);

        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
    }

    

    // Test case for constructor
    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }

    // Test case for initGUI method
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

    // Test case for loadFIXTree method
    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) fixTree.getModel().getRoot();
        assertNotNull(rootNode);
        assertEquals("Dictionaries", rootNode.getUserObject());
        assertTrue(rootNode.getChildCount() > 0);
    }

    // Test case for loadMessages method
    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = null;
        for (DefaultMutableTreeNode node : treeView.getFixTree().getModel().getDescendants(treeView.getFixTree().getModel().getRoot())) {
            if (node.getUserObject() instanceof FixInfoTreeNode && ((FixInfoTreeNode) node).getUserObject() instanceof DictionaryInfo) {
                versionNode = (FixInfoTreeNode) node;
                break;
            }
        }
        assertNotNull(versionNode);

        List<MessageInfo> messageList = Collections.emptyList();
        PowerMockito.when(library.getDictionaries().get(0).getMessages()).thenReturn(messageList);
        treeView.loadMessages(versionNode, library.getDictionaries().get(0));
        assertEquals(0, versionNode.getChildCount());

        PowerMockito.when(library.getDictionaries().get(0).getMessages()).thenReturn(Arrays.asList(new MessageInfo("Message1", "Type1"), new MessageInfo("Message2", "Type2")));
        treeView.loadMessages(versionNode, library.getDictionaries().get(0));
        assertEquals(2, versionNode.getChildCount());
    }

    // Test case for loadItems method
    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = null;
        for (DefaultMutableTreeNode node : treeView.getFixTree().getModel().getDescendants(treeView.getFixTree().getModel().getRoot())) {
            if (node.getUserObject() instanceof FixInfoTreeNode && ((FixInfoTreeNode) node).getUserObject() instanceof DictionaryInfo) {
                FixInfoTreeNode versionNode = (FixInfoTreeNode) node;
                branchNode = (FixInfoTreeNode) versionNode.getChildAt(0);
                break;
            }
        }
        assertNotNull(branchNode);

        MessageInfo message = new MessageInfo("Message1", "Type1");
        ComponentInfo component = new ComponentInfo("Component1", new String[]{"Tag1", "Tag2", "Tag3"});
        List<FixInfo> itemList = Arrays.asList(message, component);
        PowerMockito.when(message.getItems()).thenReturn(itemList);

        treeView.loadItems(branchNode, message, 0);
        assertEquals(2, branchNode.getChildCount());
    }

    // Test case for expandAll method
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertTrue(fixTree.isRowExpanded(row));
        }
    }

    // Test case for collapseAll method
    @Test
    public void testCollapseAll() {
        treeView.expandAll();
        treeView.collapseAll();
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertFalse(fixTree.isRowExpanded(row));
        }
    }

    // Test case for search method
    @Test
    public void testSearch() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) fixTree.getModel().getRoot();
        treeView.search(rootNode, ".*Message.*");
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertTrue(fixTree.isRowSelected(row));
        }
    }

    // Test case for clear method
    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) fixTree.getModel().getRoot();
        treeView.search(rootNode, ".*Message.*");
        treeView.clear(rootNode);
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertFalse(fixTree.isRowSelected(row));
        }
    }

    // Test case for valueChanged method
    @Test
    public void testValueChanged() {
        PowerMockito.mockStatic(LibrarySuite.class);
        PowerMockito.when(LibrarySuite.getInstance().getDetailsPanel()).thenReturn(PowerMockito.mock(LibraryDetailsPanel.class));

        FixInfoTreeNode node = (FixInfoTreeNode) treeView.getFixTree().getModel().getRoot();
        treeView.valueChanged(new TreeSelectionEvent(treeView.getFixTree(), new TreePath(node.getPath()), TreeSelectionModel.LEAF, new Object[]{}, new int[0], null));

        LibraryDetailsPanel detailsPanel = PowerMockito.mock(LibraryDetailsPanel.class);
        PowerMockito.when(LibrarySuite.getInstance().getDetailsPanel()).thenReturn(detailsPanel);

        treeView.valueChanged(new TreeSelectionEvent(treeView.getFixTree(), new TreePath(node.getPath()), TreeSelectionModel.LEAF, new Object[]{"Message1"}, new int[0], null));
        PowerMockito.verify(detailsPanel).displayDetails((FixInfo) node.getUserObject(), (ComponentInfo) node.getUserObjectAt(0));
    }

    // Test case for actionPerformed method
    @Test
    public void testActionPerformed() {
        treeView.expandAll();
        treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), ActionEvent.ACTION_PERFORMED, "Go"));
        treeView.search((FixInfoTreeNode) treeView.getFixTree().getModel().getRoot(), ".*Message.*");

        treeView.collapseAll();
        treeView.clear((FixInfoTreeNode) treeView.getFixTree().getModel().getRoot());
        treeView.actionPerformed(new ActionEvent(treeView.getSearchButton(), ActionEvent.ACTION_PERFORMED, "Go"));
        treeView.search((FixInfoTreeNode) treeView.getFixTree().getModel().getRoot(), ".*Message.*");
    }

    // Test case for keyPressed method
    @Test
    public void testKeyPressed() {
       treeView.search((FixInfoTreeNode) treeView.getFixTree().getModel().getRoot(), ".*Message.*");
        treeView.keyPressed(new KeyEvent(treeView, 0, true, false, KeyEvent.VK_ENTER, 'G', 0, 0, false));
        assertTrue(treeView.getFixTree().isSelectionPathValid());
    }

    
}