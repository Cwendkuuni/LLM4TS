package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {TreeView.class})
public class TreeViewTest {

    @Test
    public void testLoadFIXTree() {
        Library libraryMock = Mockito.mock(Library.class);
        PowerMockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<DictionaryInfo>() {{
            add(new DictionaryInfo("1.0", "D1", new HashMap<>()));
            add(new DictionaryInfo("2.0", "D2", new HashMap<>()));
        }});

        TreeView treeView = new TreeView(libraryMock);

        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        assertEquals("Dictionaries", rootNode.getUserObject());
        assertEquals(2, rootNode.getChildCount());
    }

    @Test
    public void testSearch() {
        Library libraryMock = Mockito.mock(Library.class);
        PowerMockito.when(libraryMock.getDictionaries()).thenReturn(new ArrayList<DictionaryInfo>() {{
            add(new DictionaryInfo("1.0", "D1", new HashMap<>()) {{
                add(new MessageInfo("M1", "Message1"));
                add(new MessageInfo("M2", "Message2"));
            }});
            add(new DictionaryInfo("2.0", "D2", new HashMap<>()) {{
                add(new MessageInfo("M3", "Message3"));
            }});
        }});

        TreeView treeView = new TreeView(libraryMock);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        treeView.search(rootNode, "M[1-2]");

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(i);
            if (childNode.getUserObject() instanceof MessageInfo) {
                MessageInfo messageInfo = (MessageInfo) childNode.getUserObject();
                assertTrue(messageInfo.getName().matches("M[1-2]") || messageInfo.getMessageType().matches("M[1-2]"));
                assertTrue(childNode.isHighlighted());
            }
        }
    }

    @Test
    public void testClear() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        treeView.search(rootNode, "M1");
        treeView.clear(rootNode);

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(i);
            assertFalse(childNode.isHighlighted());
        }
    }

    @Test
    public void testExpandAll() {
        Library libraryMock = Mockito.mock(Library.class);
        TreeView treeView = new TreeView(libraryMock);
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        treeView.expandAll();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(i);
            assertTrue(childNode.isExpanded());
            if (childNode.getChildCount() > 0) {
                for (int j = 0; j < childNode.getChildCount(); j++) {
                    FixInfoTreeNode grandChildNode = (FixInfoTreeNode) childNode.getChildAt(j);
                    assertTrue(grandChildNode.isExpanded());
                }
            }
        }
    }
}