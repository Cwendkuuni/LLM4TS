package org.fixsuite.message.view;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
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

 private Library library;
 private TreeView treeView;
 private JTree fixTree;
 private DefaultMutableTreeNode rootNode;
 private DefaultMutableTreeNode messageNode;
 private DefaultMutableTreeNode fieldNode;

 @Before
 public void setUp() {
 library = Mockito.mock(Library.class);
 treeView = new TreeView(library);
 fixTree = treeView.fixTree;

 rootNode = new DefaultMutableTreeNode("Dictionaries");
 messageNode = new DefaultMutableTreeNode("1.0");
 fieldNode = new DefaultMutableTreeNode(new FieldInfo("1", "Test", "TestTag"));

 PowerMockito.when(library.getDictionaries()).thenReturn(Arrays.asList(new DictionaryInfo("1.0", "testDict")));

 treeView.initGUI();
 treeView.loadFIXTree();
 }

 @Test
 public void testLoadFIXTree() {
 assertEquals("Dictionaries", fixTree.getModel().getRoot().getUserObject());
 assertEquals(1, fixTree.getModel().getChildCount());
 assertEquals("1.0", fixTree.getModel().getChildAt(0).getUserObject().toString());
 }

 @Test
 public void testLoadMessages() {
 PowerMockito.when(library.getMessages("1.0")).thenReturn(Arrays.asList(new MessageInfo("TestMessage", "TestMessageType"), new MessageInfo("AnotherMessage", "AnotherMessageType")));

 treeView.loadMessages(messageNode, PowerMockito.mock(DictionaryInfo.class));
 assertEquals(2, messageNode.getChildCount());
 }

 @Test
 public void testLoadItems() {
 MessageInfo message = new MessageInfo("TestMessage", "TestMessageType");
 messageNode.add(fieldNode);

 PowerMockito.when(message.getItems()).thenReturn(Arrays.asList(new FieldInfo("1", "TestField", "TestFieldTag"), new FieldInfo("2", "AnotherField", "AnotherFieldTag")));

 treeView.loadItems(fieldNode, message, 0);
 assertEquals(2, fieldNode.getChildCount());
 }

 @Test
 public void testSearch() {
 PowerMockito.when(library.getMessages("1.0")).thenReturn(Arrays.asList(new MessageInfo("TestMessage", "TestMessageType"), new MessageInfo("AnotherMessage", "AnotherMessageType")));

 treeView.searchTextField.setText("TestMessageType");
 treeView.searchButton.doClick();

 assertTrue(messageNode.isExpanded());
 assertTrue(messageNode.getChildAt(0).isHighlighted());
 }

 @Test
 public void testClear() {
 treeView.searchTextField.setText("TestMessageType");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();

 assertFalse(messageNode.getChildAt(0).isHighlighted());
 }

 @Test
 public void testExpandAllAndCollapseAll() {
 treeView.expandAll();
 assertFalse(fixTree.isExpanded(0));
 treeView.collapseAll();
 assertFalse(fixTree.isExpanded(0));
 }

 @Test
 public void testKeyPress() {
 Mockito.doAnswer(invocationOnMock -> {
 treeView.searchTextField.doClick();
 treeView.searchTextField.sendKeys(KeyEvent.KEY_CODE, KeyEvent.VK_ENTER, true, true, 0);
 return null;
 }).when(treeView.searchTextField).dispatchEvent(Mockito.any(KeyEvent.class));

 treeView.searchTextField.requestFocus();
 treeView.searchTextField.dispatchEvent(new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 0));
 assertTrue(messageNode.isExpanded());
 }
}