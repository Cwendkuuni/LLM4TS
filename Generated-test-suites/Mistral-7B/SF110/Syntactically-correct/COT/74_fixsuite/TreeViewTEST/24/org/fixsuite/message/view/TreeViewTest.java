package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.TreeView.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    private JTree tree;
    private TreeView treeView;
    private FixInfoTreeNode rootNode;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
        tree = treeView.getFixTree();
        rootNode = (FixInfoTreeNode) tree.getModel().getRoot();
    }

    @After
    public void tearDown() {
        if (tree != null) {
            tree.setCellRenderer(null);
            tree = null;
        }
        if (treeView != null) {
            treeView = null;
        }
        if (library != null) {
            library = null;
        }
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertNotNull(treeView.getToolBar());
    }

    @Test
    public void testLoadFIXTreeMethod() {
        final FixInfoTreeNode expectedRootNode = new FixInfoTreeNode("Dictionaries");
        assertEquals(expectedRootNode, treeView.loadFIXTree());
        final int expectedNumberOfNodes = 4; // Assuming there's a dictionary with 4 messages and items
        assertEquals(expectedNumberOfNodes, rootNode.getChildCount());
    }

    @Test
    public void testLoadMessagesMethod() {
        final DictionaryInfo dictionary = library.getDictionaries().get(0);
        final MessageInfo message = dictionary.getMessages().get(0);

        final FixInfoTreeNode expectedNode = new FixInfoTreeNode(message.getVersion());
        treeView.loadMessages(expectedNode, dictionary);
        final int expectedNumberOfNodes = message.getItems().size() + 1; // 1 for the message node itself
        assertEquals(expectedNumberOfNodes, expectedNode.getChildCount());
    }

    @Test
    public void testLoadItemsMethod() {
        final CompositeFixInfo compositeFixInfo = new CompositeFixInfo();
        final FixInfoTreeNode expectedNode = new FixInfoTreeNode("Test Node");

        treeView.loadItems(expectedNode, compositeFixInfo, 0);
        final int expectedNumberOfNodes = compositeFixInfo.getItems().size() + 1; // 1 for the compositeFixInfo node itself
        assertEquals(expectedNumberOfNodes, expectedNode.getChildCount());
    }

    @Test
    public void testExpandAllMethod() {
        treeView.expandAll();
        final int expectedNumberOfNodes = rootNode.getChildCount();
        for (int i = 0; i < expectedNumberOfNodes; ++i) {
            treeView.fixTree.expandRow(i);
        }
        assertTrue(tree.isExpanded(new TreePath(rootNode.getPath())));
    }

    @Test
    public void testCollapseAllMethod() {
        treeView.collapseAll();
        final int expectedNumberOfNodes = rootNode.getChildCount();
        for (int i = expectedNumberOfNodes - 1; i >= 0; --i) {
            treeView.fixTree.collapseRow(i);
        }
        assertFalse(tree.isExpanded(new TreePath(rootNode.getPath())));
    }

    @Test
    public void testSearchMethod() {
        final String searchString = "Test";
        treeView.search((FixInfoTreeNode) rootNode, searchString);

        final List<FixInfoTreeNode> nodesToBeHighlighted = Arrays.asList(rootNode,
                findChildNodeWithTagNumber("10", rootNode),
                findChildNodeWithName("Test Component", findChildNodeWithName("Test Dictionary", rootNode)));
        for (FixInfoTreeNode node : nodesToBeHighlighted) {
            assertTrue(node.isHighlighted());
        }
    }

    @Test
    public void testClearMethod() {
        treeView.search((FixInfoTreeNode) rootNode, "Test");
        treeView.clear((FixInfoTreeNode) rootNode);

        for (FixInfoTreeNode node : Arrays.asList(rootNode)) {
            assertFalse(node.isHighlighted());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullLibraryException() {
        new TreeView(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryWithEmptyDictionariesException() {
        library = new Library();
        new TreeView(library);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDictionaryException() {
        library.clear();
        library.addDictionary(null);
        new TreeView(library);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMessageInfoException() {
        final DictionaryInfo dictionary = library.getDictionaries().get(0);
        dictionary.clearMessages();
        dictionary.addMessage(null);
        treeView = new TreeView(library);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadFIXTreeWithEmptyLibraryException() {
        library.clear();
        new TreeView(library);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadMessagesWithEmptyDictionaryException() {
        final DictionaryInfo dictionary = library.getDictionaries().get(0);
        dictionary.clearMessages();
        treeView.loadMessages(rootNode, dictionary);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadItemsWithEmptyCompositeFixInfoException() {
        final CompositeFixInfo compositeFixInfo = new CompositeFixInfo();
        compositeFixInfo.clearItems();
        treeView.loadItems(rootNode, compositeFixInfo, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSearchWithEmptySearchStringException() {
        treeView.search((FixInfoTreeNode) rootNode, "");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTreeCellRendererException() {
        treeView.setCellRenderer(null);
    }

    @Test
    public void testValueChangedMethod() {
        final FixInfoTreeNode messageNode = findChildNodeWithMessageType("Heartbeat", rootNode);
        final FixInfoTreeNode componentNode = findChildNodeWithName("Test Component", findChildNodeWithName("Test Dictionary", rootNode));

        // Test selection of MessageInfo
        treeView.valueChanged(new TreeSelectionEvent(treeView.fixTree, new TreePath(messageNode.getPath())));
        LibrarySuite.getInstance().getDetailsPanel().assertDisplayed(messageNode.getUserObject(), messageNode.getUserObject().getClass());

        // Test selection of ComponentInfo
        treeView.valueChanged(new TreeSelectionEvent(treeView.fixTree, new TreePath(componentNode.getPath())));
        LibrarySuite.getInstance().getDetailsPanel().assertDisplayed(componentNode.getUserObject(), componentNode.getUserObject().getClass());

        // Test selection of FieldInfo
        final FixInfoTreeNode fieldNode = findChildNodeWithTagNumber("10", messageNode);
        treeView.valueChanged(new TreeSelectionEvent(treeView.fixTree, new TreePath(fieldNode.getPath())));
        LibrarySuite.getInstance().getDetailsPanel().assertDisplayed(fieldNode.getUserObject(), fieldNode.getUserObject().getClass());

        // Test selection of null object
        treeView.valueChanged(new TreeSelectionEvent(treeView.fixTree, new TreePath(null)));
        LibrarySuite.getInstance().getDetailsPanel().clearDisplay();
    }

    @Test
    public void testActionPerformedMethod() {
        treeView.expandAll();
        treeView.actionPerformed(new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "collapseButton"));
        assertTrue(tree.isExpanded(new TreePath(rootNode.getPath())));

        treeView.search((FixInfoTreeNode) rootNode, "Test");
        treeView.actionPerformed(new ActionEvent(treeView.clearButton, ActionEvent.ACTION_PERFORMED, "clearButton"));
        for (FixInfoTreeNode node : Arrays.asList(rootNode)) {
            assertFalse(node.isHighlighted());
        }

        treeView.actionPerformed(new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "searchButton"));
        final String searchString = "Test";
        treeView.search((FixInfoTreeNode) rootNode, searchString);
        final List<FixInfoTreeNode> nodesToBeHighlighted = Arrays.asList(rootNode,
                findChildNodeWithTagNumber("10", rootNode),
                findChildNodeWithName("Test Component", findChildNodeWithName("Test Dictionary", rootNode)));
        for (FixInfoTreeNode node : nodesToBeHighlighted) {
            assertTrue(node.isHighlighted());
        }
    }

    @Test
    public void testKeyPressedMethod() {
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "searchButton"));
        treeView.keyPressed(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
        final String searchString = "Test";
        treeView.search((FixInfoTreeNode) rootNode, searchString);
        final List<FixInfoTreeNode> nodesToBeHighlighted = Arrays.asList(rootNode,
                findChildNodeWithTagNumber("10", rootNode),
                findChildNodeWithName("Test Component", findChildNodeWithName("Test Dictionary", rootNode)));
        for (FixInfoTreeNode node : nodesToBeHighlighted) {
            assertTrue(node.isHighlighted());
        }
    }

    private FixInfoTreeNode findChildNodeWithTagNumber(final String tagNumber, final FixInfoTreeNode parentNode) {
        for (int i = 0; i < parentNode.getChildCount(); ++i) {
            final FixInfoTreeNode childNode = (FixInfoTreeNode) parentNode.getChildAt(i);
            if (childNode.getUserObject() instanceof FieldInfo && ((FieldInfo) childNode.getUserObject()).getTagNumber() != null && ((FieldInfo) childNode.getUserObject()).getTagNumber().equals(tagNumber)) {
                return childNode;
            }
        }
        return null;
    }

    private FixInfoTreeNode findChildNodeWithName(final String name, final FixInfoTreeNode parentNode) {
        for (int i = 0; i < parentNode.getChildCount(); ++i) {
            final FixInfoTreeNode childNode = (FixInfoTreeNode) parentNode.getChildAt(i);
            if (childNode.getUserObject() instanceof MessageInfo && ((MessageInfo) childNode.getUserObject()).getName().equals(name)) {
                return childNode;
            }
            else if (childNode.getUserObject() instanceof ComponentInfo && ((ComponentInfo) childNode.getUserObject()).getName().equals(name)) {
                return childNode;
            }
            else if (childNode.getChildCount() > 0) {
                final FixInfoTreeNode grandChildNode = findChildNodeWithName(name, childNode);
                if (grandChildNode != null) {
                    return grandChildNode;
                }
            }
        }
        return null;
    }
}