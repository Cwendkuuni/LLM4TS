package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeViewTest {

 private Library library;
 private TreeView treeView;
 @Mock
 private LibrarySuite librarySuite;

 @Before
 public void setUp() {
 library = new Library();
 treeView = new TreeView(library);
 Mockito.when(librarySuite.getInstance()).thenReturn(librarySuite);
 }

 @Test
 public void testInitGUI() {
 assertEquals(new BorderLayout(), treeView.getLayout());
 assertNotNull(treeView.mainScrollPane);
 assertNotNull(treeView.fixTree);
 assertNotNull(treeView.searchLabel);
 assertNotNull(treeView.searchTextField);
 assertNotNull(treeView.searchButton);
 assertNotNull(treeView.clearButton);
 assertNotNull(treeView.expandButton);
 assertNotNull(treeView.collapseButton);
 assertNotNull(treeView.toolbar);
 }

 @Test
 public void testLoadFIXTree() {
 final DictionaryInfo dict1 = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "MsgType1"), new MessageInfo("Msg2", "MsgType2")));
 final DictionaryInfo dict2 = new DictionaryInfo("Dict2", Arrays.asList(new MessageInfo("Msg3", "MsgType3"), new MessageInfo("Msg4", "MsgType4")));
 library.addDictionary(dict1);
 library.addDictionary(dict2);
 final FixInfoTreeNode root = treeView.loadFIXTree();
 assertEquals(2, root.getChildCount());
 }

 @Test
 public void testLoadMessages() {
 final DictionaryInfo dict = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "MsgType1"), new MessageInfo("Msg2", "MsgType2")));
 library.addDictionary(dict);
 final FixInfoTreeNode versionNode = new FixInfoTreeNode("Dict1_Ver1");
 treeView.loadMessages(versionNode, dict);
 assertEquals(2, versionNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 final MessageInfo msg = new MessageInfo("Msg1", "MsgType1");
 final FixInfoTreeNode branchNode = new FixInfoTreeNode("Msg1_Ver1");
 final FixInfoTreeNode leafNode = new FixInfoTreeNode(new FieldInfo("Field1", 1, "FieldName1"));
 treeView.loadItems(branchNode, msg, 0);
 assertEquals(1, branchNode.getChildCount());
 }

 @Test
 public void testExpandAll() {
 final JTree treeSpy = Mockito.spy(treeView.fixTree);
 treeView.expandAll();
 for (int i = 0; i < treeSpy.getRowCount(); ++i) {
 Mockito.verify(treeSpy).expandRow(i);
 }
 }

 @Test
 public void testCollapseAll() {
 final JTree treeSpy = Mockito.spy(treeView.fixTree);
 treeView.collapseAll();
 for (int i = treeSpy.getRowCount() - 1; i >= 0; --i) {
 Mockito.verify(treeSpy).collapseRow(i);
 }
 }

 @Test
 public void testSearch() {
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 final String searchString = "Field1";
 treeView.search(rootNode, searchString);
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)rootNode.getChildAt(i);
 if (node.getUserObject() instanceof FieldInfo && node.isHighlighted()) {
 Mockito.verify(librarySuite).getDetailsPanel().displayDetails(ArgumentMatchers.isInstanceOf(FieldInfo.class), ArgumentMatchers.any(ComponentInfo.class));
 }
 }
 }

 @Test
 public void testClear() {
 final FixInfoTreeNode rootNode = treeView.loadFIXTree();
 treeView.clear(rootNode);
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 final FixInfoTreeNode node = (FixInfoTreeNode)rootNode.getChildAt(i);
 assertFalse(node.isHighlighted());
 }
 }

 @Test
 public void testSearchButton() {
 treeView.searchButton.doClick();
 treeView.searchTextField.sendKeyEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
 treeView.search("SearchString");
 }
}