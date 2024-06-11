package org.fixsuite.message.view.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.event.*;

import org.fixsuite.message.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

 private Library libraryMock;
 private TreeView treeView;
 private JTree fixTreeSpy;

 @Before
 public void setup() {
 libraryMock = Mockito.mock(Library.class);
 treeView = new TreeView(libraryMock);
 fixTreeSpy = Mockito.spy(treeView.fixTree);
 }

 @Test
 public void testLoadFIXTree() {
 // Given
 final DictionaryInfo dictionary1 = new DictionaryInfo("TestDictionary1", Arrays.asList(new MessageInfo("TestMessage1", "TestMessageType1"), new MessageInfo("TestMessage2", "TestMessageType2")));
 final DictionaryInfo dictionary2 = new DictionaryInfo("TestDictionary2", Arrays.asList(new MessageInfo("TestMessage3", "TestMessageType3"), new MessageInfo("TestMessage4", "TestMessageType4")));
 Mockito.when(libraryMock.getDictionaries()).thenReturn(Arrays.asList(dictionary1, dictionary2));

 // When
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();

 // Then
 assertEquals("Dictionaries", rootNode.getUserObject());
 final int numChildren = rootNode.getChildCount();
 assertEquals(2, numChildren);

 // Verify
 Mockito.verify(libraryMock).getDictionaries();
 }

 @Test
 public void testLoadMessages() {
 // Given
 final DictionaryInfo dictionary = Mockito.mock(DictionaryInfo.class);
 Mockito.when(dictionary.getMessages()).thenReturn(Arrays.asList(new MessageInfo("TestMessage", "TestMessageType"), new MessageInfo("TestMessage2", "TestMessageType2")));
 Mockito.when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(dictionary));

 // When
 final FixInfoTreeNode versionNode = Mockito.mock(FixInfoTreeNode.class);
 treeView.loadMessages(versionNode, dictionary);

 // Then
 final int numChildren = versionNode.getChildCount();
 assertEquals(2, numChildren);

 // Verify
 Mockito.verify(libraryMock).getDictionaries();
 }

 @Test
 public void testLoadItems() {
 // Given
 final MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
 final CompositeFixInfo leafNode = new CompositeFixInfo(Arrays.asList(new FixInfo("TestFixInfo1"), new FixInfo("TestFixInfo2")));

 // When
 final FixInfoTreeNode branchNode = Mockito.mock(FixInfoTreeNode.class);
 treeView.loadItems(branchNode, leafNode, 0);

 // Then
 final int numChildren = branchNode.getChildCount();
 assertEquals(2, numChildren);

 // Verify
 Mockito.verify(libraryMock, never()).getDictionaries();
 }

 @Test
 public void testExpandAll() {
 // Given
 final TreeModel treeModelSpy = Mockito.spy(fixTreeSpy.getModel());
 treeView.expandAll();

 // Then
 final int numExpandedNodes = fixTreeSpy.getModel().getNodeCount();
 for (int i = 0; i < numExpandedNodes; ++i) {
 Mockito.verify(fixTreeSpy).expandRow(ArgumentMatchers.eq(i));
 }
 }

 @Test
 public void testCollapseAll() {
 // Given
 final int numRows = fixTreeSpy.getRowCount();
 for (int i = numRows - 1; i >= 0; --i) {
 fixTreeSpy.collapseRow(i);
 }

 // When
 treeView.collapseAll();

 // Then
 final int numCollapsedNodes = fixTreeSpy.getRowCount();
 for (int i = 0; i < numCollapsedNodes; ++i) {
 Mockito.verify(fixTreeSpy).collapseRow(ArgumentMatchers.eq(i));
 }
 }

 @Test
 public void testSearch() {
 // Given
 final DictionaryInfo dictionary = new DictionaryInfo("TestDictionary", Arrays.asList(new MessageInfo("TestMessage1", "TestMessageType1"), new MessageInfo("TestMessage2", "TestMessageType2")));
 Mockito.when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(dictionary));
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 final FixInfoTreeNode messageNode1 = treeView.loadMessages(rootNode, dictionary);
 final FixInfoTreeNode messageNode2 = treeView.loadMessages(messageNode1, dictionary);
 final FixInfoTreeNode fieldNode1 = treeView.loadItems(messageNode2, new MessageInfo("TestMessage1", "TestMessageType1"), 0);
 final FixInfoTreeNode fieldNode2 = treeView.loadItems(messageNode2, new MessageInfo("TestMessage1", "TestMessageType1"), 0);
 treeView.fixTree.expandAll();

 // When
 treeView.search(rootNode, "TestMessageType1");

 // Then
 assertEquals(2, messageNode1.getChildCount());
 assertTrue(messageNode1.getChildAt(0).isHighlighted());
 assertTrue(messageNode1.getChildAt(1).isHighlighted());
 assertEquals(2, messageNode2.getChildCount());
 assertTrue(fieldNode1.isHighlighted());
 assertTrue(fieldNode2.isHighlighted());
 }

 @Test
 public void testClear() {
 // Given
 final DictionaryInfo dictionary = new DictionaryInfo("TestDictionary", Arrays.asList(new MessageInfo("TestMessage1", "TestMessageType1"), new MessageInfo("TestMessage2", "TestMessageType2")));
 Mockito.when(libraryMock.getDictionaries()).thenReturn(Collections.singletonList(dictionary));
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 final FixInfoTreeNode messageNode1 = treeView.loadMessages(rootNode, dictionary);
 final FixInfoTreeNode messageNode2 = treeView.loadMessages(messageNode1, dictionary);
 final FixInfoTreeNode fieldNode1 = treeView.loadItems(messageNode2, new MessageInfo("TestMessage1", "TestMessageType1"), 0);
 treeView.fixTree.expandAll();
 treeView.search(rootNode, "TestMessageType1");

 // When
 treeView.clear(rootNode);

 // Then
 assertEquals(0, rootNode.getChildCount());
 }

 @Test
 public void testKeyPressed() {
 // Given
 final TreeModel treeModelSpy = Mockito.spy(fixTreeSpy.getModel());
 final TreeSelectionModel selectionModelSpy = Mockito.spy(fixTreeSpy.getSelectionModel());
 treeView.fixTree.addKeyListener(treeView);
 treeView.fixTree.addTreeSelectionListener(treeView);
 treeView.fixTree.expandAll();
 treeView.search(rootNode, "TestMessageType1");
 final TreePath lastSelectedPath = selectionModelSpy.getLastSelectedPath();

 // When
 treeView.keyPressed(new KeyEvent(treeView.fixTree, 10, 0, 0));

 // Then
 assertEquals(lastSelectedPath.getLastPathComponent(), rootNode);
 }
}