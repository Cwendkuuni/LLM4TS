package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import java.util.*;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeView.class})
public class TreeViewTest {

    @Test
    public void testTreeViewInitialization() {
        Library library = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(library);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertEquals(library, treeView.library);
    }

    @Test
    public void testLoadFIXTree() {
        Library library = Mockito.mock(Library.class);
        PowerMockito.whenNew(FixInfoTreeNode.class).withArguments((Object)"Dictionaries").thenReturn(new FixInfoTreeNode("Dictionaries"));

        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.fixTree.getModel().getRoot();
        assertEquals("Dictionaries", rootNode.getUserObject());

        PowerMockito.when(library.getDictionaries()).thenReturn(Collections.singletonList(new DictionaryInfo("Version1")));
        PowerMockito.when(new DictionaryInfo("Version1").getMessages()).thenReturn(Collections.singletonList(new MessageInfo("Message1")));

        treeView.initGUI();
        assertEquals(1, rootNode.getChildCount());
        FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(0);
        assertEquals("Version1", dictionaryNode.getUserObject());
        assertEquals(1, dictionaryNode.getChildCount());
        FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("Message1", messageNode.getUserObject());
    }

    @Test
    public void testSearch() {
        Library library = Mockito.mock(Library.class);
        PowerMockito.whenNew(FixInfoTreeNode.class).withArguments((Object)"SearchResult").thenReturn(new FixInfoTreeNode("SearchResult"));

        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.fixTree.getModel().getRoot();
        FixInfoTreeNode searchResultNode = new FixInfoTreeNode("SearchResult");

        PowerMockito.when(library.getDictionaries()).thenReturn(Collections.singletonList(new DictionaryInfo("Version1")));
        PowerMockito.when(new DictionaryInfo("Version1").getMessages()).thenReturn(Collections.singletonList(new MessageInfo("Message1")));
        PowerMockito.when(new MessageInfo("Message1").getName()).thenReturn("MessageWithSearchString");

        treeView.initGUI();
        treeView.searchTextField.setText("SearchString");
        treeView.searchButton.doClick();

        treeView.search(rootNode, "SearchString");
        verify(searchResultNode).setHighlighted(true);
        treeView.fixTree.expandPath(new TreePath(searchResultNode.getPath()));
    }

    @Test
    public void testClear() {
        Library library = Mockito.mock(Library.class);
        PowerMockito.whenNew(FixInfoTreeNode.class).withArguments((Object)"ClearedNode").thenReturn(new FixInfoTreeNode("ClearedNode"));

        TreeView treeView = new TreeView(library);
        FixInfoTreeNode rootNode = treeView.fixTree.getModel().getRoot();
        FixInfoTreeNode clearedNode = new FixInfoTreeNode("ClearedNode");

        treeView.initGUI();
        treeView.clear(rootNode);
        verify(clearedNode).setHighlighted(false);

        PowerMockito.when(new DictionaryInfo("DictionaryName").getMessages()).thenReturn(Collections.singletonList(new MessageInfo("Message1")));
        PowerMockito.when(new DictionaryInfo("DictionaryName").getName()).thenReturn("DictionaryName");
        PowerMockito.when(new MessageInfo("Message1").getName()).thenReturn("MessageName");

        treeView.initGUI();
        treeView.clear(rootNode);
        treeView.searchTextField.setText("DictionaryName");
        treeView.searchButton.doClick();

        treeView.search(rootNode, "DictionaryName");
        verify(clearedNode).setHighlighted(false);
    }
}