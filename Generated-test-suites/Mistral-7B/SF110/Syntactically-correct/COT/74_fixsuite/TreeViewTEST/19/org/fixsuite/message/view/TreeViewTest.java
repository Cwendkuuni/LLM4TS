package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private JTree fixTree;
    private TreeCellRenderer renderer;

    @Before
    public void setUp() {
        Library library = new Library() {
            @Override
            public List<DictionaryInfo> getDictionaries() {
                return Arrays.asList(new DictionaryInfo() {
                    @Override
                    public String getName() {
                        return "d1";
                    }

                    @Override
                    public List<MessageInfo> getMessages() {
                        return Arrays.asList(new MessageInfo() {
                            @Override
                            public String getName() {
                                return "m1";
                            }

                            @Override
                            public String getMessageType() {
                                return "MT1";
                            }
                        }, new MessageInfo() {
                            @Override
                            public String getName() {
                                return "m2";
                            }

                            @Override
                            public String getMessageType() {
                                return "MT2";
                            }
                        });
                    }
                });
            }
        };

        treeView = new TreeView(library);
        fixTree = treeView.getFixTree();
        renderer = treeView.getFixTree().getCellRenderer();

        JFrame frame = new JFrame();
        frame.setContentPane(treeView);
        frame.pack();
        frame.setVisible(true); // Need to make the tree visible to interact with it
    }

    @After
    public void tearDown() {
        treeView.dispose();
    }

    @Test
    public void testConstructor() {
        assertNotNull(treeView);
    }

    @Test
    public void testLoadFIXTree() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        assertEquals("Dictionaries", rootNode.getUserObject());
        assertEquals(1, rootNode.getChildCount());

        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals("d1", dictionaryNode.getUserObject());
        assertEquals(2, dictionaryNode.getChildCount());

        DefaultMutableTreeNode messageNode1 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("m1", messageNode1.getUserObject());
        assertEquals(0, messageNode1.getChildCount());

        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(1);
        assertEquals("m2", messageNode2.getUserObject());
        assertEquals(0, messageNode2.getChildCount());
    }

    @Test
    public void testLoadItems() {
        TreeModel model = treeView.getFixTree().getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);

        // Test loading a component
        DefaultMutableTreeNode componentNode1 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("C1", componentNode1.getUserObject());
        assertEquals(1, componentNode1.getChildCount());

        DefaultMutableTreeNode fieldNode1 = (DefaultMutableTreeNode) componentNode1.getChildAt(0);
        assertEquals("F1", fieldNode1.getUserObject());
        assertEquals(0, fieldNode1.getChildCount());

        // Test loading a message with a field
        DefaultMutableTreeNode messageNode2 = (DefaultMutableTreeNode) dictionaryNode.getChildAt(1);
        assertEquals("m2", messageNode2.getUserObject());
        assertEquals(1, messageNode2.getChildCount());

        DefaultMutableTreeNode fieldNode2 = (DefaultMutableTreeNode) messageNode2.getChildAt(0);
        assertEquals("F2", fieldNode2.getUserObject());
        assertEquals(0, fieldNode2.getChildCount());

        // Test loading a message with a composite field
        DefaultMutableTreeNode compositeFieldNode3 = (DefaultMutableTreeNode) messageNode2.getChildAt(0);
        assertEquals("CF1", compositeFieldNode3.getUserObject());
        assertEquals(2, compositeFieldNode3.getChildCount());

        DefaultMutableTreeNode fieldNode3_1 = (DefaultMutableTreeNode) compositeFieldNode3.getChildAt(0);
        assertEquals("F3_1", fieldNode3_1.getUserObject());
        assertEquals(0, fieldNode3_1.getChildCount());

        DefaultMutableTreeNode fieldNode3_2 = (DefaultMutableTreeNode) compositeFieldNode3.getChildAt(1);
        assertEquals("F3_2", fieldNode3_2.getUserObject());
        assertEquals(0, fieldNode3_2.getChildCount());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        assertFalse(fixTree.isExpanded(fixTree.getRowCount() - 1));

        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            assertTrue(fixTree.isExpanded(i));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.expandAll();
        treeView.collapseAll();

        for (int i = 0; i < fixTree.getRowCount(); ++i) {
            assertFalse(fixTree.isExpanded(i));
        }
    }

    @Test
    public void testSearch() {
        String searchString = "M";

        // Perform search
        treeView.search(fixTree.getModel().getRoot(), searchString);

        // Check search results
        List<DefaultMutableTreeNode> searchedNodes = new ArrayList<>();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        while (currentNode != null) {
            if (currentNode.isHighlighted()) {
                searchedNodes.add(currentNode);
            }
            currentNode = (DefaultMutableTreeNode) currentNode.getParent();
        }

        assertEquals(5, searchedNodes.size());
        assertTrue(searchedNodes.contains(dictionaryNode));
        assertTrue(searchedNodes.contains(messageNode1));
        assertTrue(searchedNodes.contains(messageNode2));
        assertTrue(searchedNodes.contains(fieldNode1));
        assertTrue(searchedNodes.contains(fieldNode2));
    }

    @Test
    public void testClear() {
        String searchString = "F";
        treeView.search(fixTree.getModel().getRoot(), searchString);

        // Perform clear
        treeView.clear((DefaultMutableTreeNode) fixTree.getModel().getRoot());

        // Check search results after clear
        List<DefaultMutableTreeNode> clearNodes = new ArrayList<>();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        while (currentNode != null) {
            clearNodes.add(currentNode);
            currentNode = (DefaultMutableTreeNode) currentNode.getParent();
        }

        Collections.reverse(clearNodes);
        assertEquals(6, clearNodes.size());
        assertFalse(clearNodes.get(0).isHighlighted());
        assertFalse(clearNodes.get(1).isHighlighted());
        assertFalse(clearNodes.get(2).isHighlighted());
        assertFalse(clearNodes.get(3).isHighlighted());
        assertFalse(clearNodes.get(4).isHighlighted());
        assertFalse(clearNodes.get(5).isHighlighted());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullNode() {
        treeView.search(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNullSearchString() {
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), null);
    }

    @Test
    public void testSearchWithEmptySearchString() {
        treeView.search((DefaultMutableTreeNode) fixTree.getModel().getRoot(), "");

        // Check no search results
        List<DefaultMutableTreeNode> emptySearchNodes = new ArrayList<>();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) fixTree.getModel().getRoot();
        while (currentNode != null) {
            emptySearchNodes.add(currentNode);
            currentNode = (DefaultMutableTreeNode) currentNode.getParent();
        }

        Collections.reverse(emptySearchNodes);
        assertEquals(6, emptySearchNodes.size());
        assertFalse(emptySearchNodes.get(0).isHighlighted());
        assertFalse(emptySearchNodes.get(1).isHighlighted());
        assertFalse(emptySearchNodes.get(2).isHighlighted());
        assertFalse(emptySearchNodes.get(3).isHighlighted());
        assertFalse(emptySearchNodes.get(4).isHighlighted());
        assertFalse(emptySearchNodes.get(5).isHighlighted());
    }

    @Test
    public void testKeyPressedEvent() {
        treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, false, false, false, false));
        treeView.actionPerformed(new ActionEvent(treeView.searchButton, 0, "Go", false));
    }
}