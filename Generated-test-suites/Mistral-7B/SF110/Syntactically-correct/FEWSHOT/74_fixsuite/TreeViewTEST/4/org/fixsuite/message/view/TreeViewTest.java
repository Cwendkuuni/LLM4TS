package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.fixsuite.message.info.*;
import org.junit.Test;

public class TreeViewTest {

    @Test
    public void testTreeStructure() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        TreeModel model = treeView.fixTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();

        int numberOfDictionaries = library.getDictionaries().size();
        assertEquals(numberOfDictionaries, rootNode.getChildCount());

        for (int i = 0; i < numberOfDictionaries; i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertEquals("Dictionaries", dictionaryNode.getUserObject());

            int numberOfVersions = ((DictionaryInfo) library.getDictionaries().get(i)).getVersions().size();
            assertEquals(numberOfVersions, dictionaryNode.getChildCount());

            for (int j = 0; j < numberOfVersions; j++) {
                DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(String.valueOf(((DictionaryInfo) library.getDictionaries().get(i)).getVersions().get(j)), versionNode.getUserObject());

                int numberOfMessages = ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().size();
                assertEquals(numberOfMessages, versionNode.getChildCount());

                for (int k = 0; k < numberOfMessages; k++) {
                    DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(k);
                    assertEquals(String.valueOf(((MessageInfo) ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().get(k))), messageNode.getUserObject());

                    int numberOfComponents = ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().size();
                    assertEquals(numberOfComponents, messageNode.getChildCount());

                    for (int l = 0; l < numberOfComponents; l++) {
                        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(l);
                        assertEquals(String.valueOf(((ComponentInfo) ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().get(l))), componentNode.getUserObject());

                        int numberOfFields = ((ComponentInfo) library.getDictionaries().get(i).getMessages().get(k).getComponents().get(l)).getFields().size();
                        assertEquals(numberOfFields, componentNode.getChildCount());

                        for (int m = 0; m < numberOfFields; m++) {
                            DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(m);
                            assertEquals(String.valueOf(((FieldInfo) ((ComponentInfo) library.getDictionaries().get(i).getMessages().get(k).getComponents().get(l)).getFields().get(m))), fieldNode.getUserObject());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testExpandAndCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        JTree tree = treeView.fixTree;

        treeView.expandAll();
        assertTrue(tree.isExpanded(tree.getRowCount() - 1));
        assertTrue(tree.isExpanded(tree.getRowCount() - 2));
        assertTrue(tree.isExpanded(tree.getRowCount() - 3));

        treeView.collapseAll();
        assertFalse(tree.isExpanded(tree.getRowCount() - 1));
        assertFalse(tree.isExpanded(tree.getRowCount() - 2));
        assertFalse(tree.isExpanded(tree.getRowCount() - 3));
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);

        treeView.searchTextField.setText("55");
        treeView.searchButton.doClick();

        JTree tree = treeView.fixTree;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();

        // Search for tag number
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertEquals("Dictionaries", dictionaryNode.getUserObject());

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(String.valueOf(((DictionaryInfo) library.getDictionaries().get(i)).getVersions().get(j)), versionNode.getUserObject());

                for (int k = 0; k < versionNode.getChildCount(); k++) {
                    DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(k);
                    assertEquals(String.valueOf(((MessageInfo) ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().get(k))), messageNode.getUserObject());

                    for (int l = 0; l < messageNode.getChildCount(); l++) {
                        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(l);
                        assertEquals(String.valueOf(((ComponentInfo) ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().get(l))), componentNode.getUserObject());

                        for (int m = 0; m < componentNode.getChildCount(); m++) {
                            DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(m);
                            if (String.valueOf(((FieldInfo) fieldNode.getUserObject()).getTagNumber()).equals("55")) {
                                assertTrue(fieldNode.isHighlighted());
                                assertTrue(tree.isExpanded(fieldNode.getParent().getPath().getLastPathComponent().getPathString().lastIndexOf("/") + 1, fieldNode.getPath().getLastPathComponent().toString().length()));
                            } else {
                                assertFalse(fieldNode.isHighlighted());
                            }
                        }
                    }
                }
            }
        }

        treeView.searchTextField.setText("foo");
        treeView.searchButton.doClick();

        // Search for field name
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertEquals("Dictionaries", dictionaryNode.getUserObject());

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(String.valueOf(((DictionaryInfo) library.getDictionaries().get(i)).getVersions().get(j)), versionNode.getUserObject());

                for (int k = 0; k < versionNode.getChildCount(); k++) {
                    DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(k);
                    assertEquals(String.valueOf(((MessageInfo) ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().get(k))), messageNode.getUserObject());

                    for (int l = 0; l < messageNode.getChildCount(); l++) {
                        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(l);
                        assertEquals(String.valueOf(((ComponentInfo) ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().get(l))), componentNode.getUserObject());

                        for (int m = 0; m < componentNode.getChildCount(); m++) {
                            DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(m);
                            if (String.valueOf(((FieldInfo) fieldNode.getUserObject()).getName()).equals("foo")) {
                                assertTrue(fieldNode.isHighlighted());
                                assertTrue(tree.isExpanded(fieldNode.getParent().getPath().getLastPathComponent().getPathString().lastIndexOf("/") + 1, fieldNode.getPath().getLastPathComponent().toString().length()));
                            } else {
                                assertFalse(fieldNode.isHighlighted());
                            }
                        }
                    }
                }
            }
        }

        treeView.searchTextField.setText("");
        treeView.searchButton.doClick();

        // Search for empty search string
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertEquals("Dictionaries", dictionaryNode.getUserObject());

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(String.valueOf(((DictionaryInfo) library.getDictionaries().get(i)).getVersions().get(j)), versionNode.getUserObject());

                for (int k = 0; k < versionNode.getChildCount(); k++) {
                    DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(k);
                    assertEquals(String.valueOf(((MessageInfo) ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().get(k))), messageNode.getUserObject());

                    for (int l = 0; l < messageNode.getChildCount(); l++) {
                        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(l);
                        assertEquals(String.valueOf(((ComponentInfo) ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().get(l))), componentNode.getUserObject());

                        for (int m = 0; m < componentNode.getChildCount(); m++) {
                            DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(m);
                            assertFalse(fieldNode.isHighlighted());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testClear() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.searchTextField.setText("55");
        treeView.searchButton.doClick();

        JTree tree = treeView.fixTree;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();

        // Clear search results
        treeView.clearButton.doClick();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            assertEquals("Dictionaries", dictionaryNode.getUserObject());

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                DefaultMutableTreeNode versionNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(String.valueOf(((DictionaryInfo) library.getDictionaries().get(i)).getVersions().get(j)), versionNode.getUserObject());

                for (int k = 0; k < versionNode.getChildCount(); k++) {
                    DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(k);
                    assertEquals(String.valueOf(((MessageInfo) ((DictionaryInfo) library.getDictionaries().get(i)).getMessages().get(k))), messageNode.getUserObject());

                    for (int l = 0; l < messageNode.getChildCount(); l++) {
                        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(l);
                        assertEquals(String.valueOf(((ComponentInfo) ((MessageInfo) library.getDictionaries().get(i).getMessages().get(k)).getComponents().get(l))), componentNode.getUserObject());

                        for (int m = 0; m < componentNode.getChildCount(); m++) {
                            DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(m);
                            assertFalse(fieldNode.isHighlighted());
                        }
                    }
                }
            }
        }
    }
}