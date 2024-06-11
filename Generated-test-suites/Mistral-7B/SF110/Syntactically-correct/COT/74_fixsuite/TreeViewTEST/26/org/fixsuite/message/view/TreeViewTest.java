package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeView.class})
public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private TreeCellRenderer cellRenderer;
    private List<DictionaryInfo> dictionaries;

    @Before
    public void setUp() {
        dictionaries = Arrays.asList(new DictionaryInfo("Dict1", null), new DictionaryInfo("Dict2", null), new DictionaryInfo("Dict3", null));
        library = Mockito.mock(Library.class);
        PowerMockito.when(library.getDictionaries()).thenReturn(dictionaries);

        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        cellRenderer = treeView.getFixTree().getCellRenderer();
    }

    @Test
    public void testTreeViewConstruction() {
        assertNotNull(treeView);
        assertTrue(treeView instanceof JPanel);
        assertEquals(1, treeView.getComponentCount());
        assertTrue(treeView.getComponent(0) instanceof JScrollPane);
        assertTrue(treeView.getComponent(0).getComponent() instanceof JTree);
        assertEquals(0, treeView.getSearchTextField().getText().length());
    }

    @Test
    public void testInitGUI() {
        assertTrue(treeView.getSearchLabel() != null);
        assertTrue(treeView.getSearchTextField() != null);
        assertTrue(treeView.getSearchButton() != null);
        assertTrue(treeView.getClearsButton() != null);
        assertTrue(treeView.getExpandButton() != null);
        assertTrue(treeView.getCollapseButton() != null);
        assertNotNull(treeView.getToolBar());
    }

    @Test
    public void testLoadFIXTree() {
        TreeModel model = treeView.loadFIXTree();
        assertEquals(3, model.getChildCount());
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        assertEquals(3, root.getChildCount());

        for (DictionaryInfo dictionary : dictionaries) {
            DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) root.getChildAt(0);
            assertTrue(versionNode.getUserObject() instanceof String);
            assertEquals(dictionary.getVersion(), versionNode.getUserObject());
            assertEquals(dictionaries.size(), versionNode.getChildCount());
            for (MessageInfo message : dictionary.getMessages()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) versionNode.getChildAt(0);
                assertTrue(node.getUserObject() instanceof MessageInfo);
                assertEquals(message.getName(), node.getUserObject());
                assertEquals(message.getMessageType(), node.getUserObject());
                assertEquals(0, node.getChildCount());
            }
        }

        // Test loading nested CompositeFixInfo
        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) root.getChildAt(1);
        assertEquals(1, componentNode.getChildCount());
        DefaultMutableTreeNode compositeNode = (DefaultMutableTreeNode) componentNode.getChildAt(0);
        assertEquals(5, compositeNode.getChildCount());
        for (int i = 0; i < compositeNode.getChildCount(); i++) {
            DefaultMutableTreeNode infoNode = (DefaultMutableTreeNode) compositeNode.getChildAt(i);
            assertTrue(infoNode.getUserObject() instanceof FixInfo);
        }
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertTrue(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            assertFalse(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        String searchString = "Comp";
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), searchString);

        for (int row = 0; row < fixTree.getRowCount(); ++row) {
            if (fixTree.isRowSelected(row)) {
                FixInfoTreeNode node = (FixInfoTreeNode) fixTree.getModel().getPathForRow(row)[0];
                if (node.getUserObject() instanceof ComponentInfo) {
                    ComponentInfo component = (ComponentInfo) node.getUserObject();
                    assertTrue(component.getName().matches(searchString) || component.getName().matches(".*" + searchString + ".*"));
                }
            }
        }
    }

    @Test
    public void testClear() {
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), "Comp");
        treeView.clear((DefaultMutableTreeNode) fixTree.getModel().getRoot());
        assertEquals(0, fixTree.getRowCount());
    }

    @Test
    public void testSearchButton() {
        treeView.searchTextField.setText("Comp");
        treeView.searchButton.doClick();
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), "Comp");
    }

    @Test
    public void testClearButton() {
        treeView.searchTextField.setText("Comp");
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), "Comp");
        treeView.clearButton.doClick();
        assertEquals(0, fixTree.getRowCount());
    }

    @Test
    public void testKeyPressEvent() {
        JFrame frame = new JFrame();
        treeView.initGUI();
        frame.getContentPane().add(treeView, java.awt.BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);

        treeView.searchTextField.setText("Comp");
        treeView.searchButton.doClick();
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), "Comp");

        robot.keyPress(KeyEvent.VK_BACK_SPACE);

        assertEquals(0, fixTree.getRowCount());
    }
}