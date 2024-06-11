package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Test;
import org.jmock.lib.legacy.TestSpecification;

public class TreeViewTest extends MockObjectTestCase {

 private Library library;
 private TreeView treeView;
 private FixInfoTreeNode rootNode;

 @Before
 public void setUp() {
 library = mock(Library.class);
 treeView = new TreeView(library);
 rootNode = new FixInfoTreeNode("Dictionaries");
 }

 @Test
 public void testLoadFIXTree() {
 final DictionaryInfo dictionary1 = new DictionaryInfo("Dict1", Arrays.asList(new MessageInfo("Msg1", "Type1"), new MessageInfo("Msg2", "Type2")));
 final DictionaryInfo dictionary2 = new DictionaryInfo("Dict2", Arrays.asList(new MessageInfo("Msg3", "Type3"), new MessageInfo("Msg4", "Type4")));

 when(library.getDictionaries()).thenReturn(Arrays.asList(dictionary1, dictionary2));

 treeView.loadFIXTree();

 assertNotNull(rootNode.getChildAt(0));
 assertNotNull(rootNode.getChildAt(1));

 assertEquals("Dict1", rootNode.getChildAt(0).getUserObject().toString());
 assertEquals("Dict2", rootNode.getChildAt(1).getUserObject().toString());

 List<MessageInfo> messages1 = (List<MessageInfo>) rootNode.getChildAt(0).getChildAt(0).getUserObject();
 List<MessageInfo> messages2 = (List<MessageInfo>) rootNode.getChildAt(1).getChildAt(0).getUserObject();

 assertEquals(2, messages1.size());
 assertEquals(2, messages2.size());

 assertEquals("Msg1", messages1.get(0).getName());
 assertEquals("Msg2", messages1.get(1).getName());
 assertEquals("Type1", messages1.get(0).getMessageType());
 assertEquals("Type1", messages1.get(1).getMessageType());

 assertEquals("Msg3", messages2.get(0).getName());
 assertEquals("Msg4", messages2.get(1).getName());
 assertEquals("Type3", messages2.get(0).getMessageType());
 assertEquals("Type4", messages2.get(1).getMessageType());
 }

 @Test
 public void testLoadMessages() {
 final MessageInfo message1 = new MessageInfo("Msg1", "Type1");
 final MessageInfo message2 = new MessageInfo("Msg2", "Type2");
 final CompositeFixInfo composite1 = new CompositeFixInfo("Comp1", Arrays.asList(message1, message2));
 final DictionaryInfo dictionary = new DictionaryInfo("Dict1", Arrays.asList(composite1));

 when(library.getDictionaries()).thenReturn(Collections.singletonList(dictionary));

 treeView.loadFIXTree();

 assertEquals(1, rootNode.getChildAt(0).getChildCount());

 assertNotNull(rootNode.getChildAt(0).getChildAt(0));
 assertEquals("Comp1", rootNode.getChildAt(0).getChildAt(0).getUserObject().toString());

 List<FixInfo> items1 = (List<FixInfo>) rootNode.getChildAt(0).getChildAt(0).getChildAt(0).getUserObject();
 assertEquals(2, items1.size());

 assertEquals("Msg1", items1.get(0).getName());
 assertEquals("Msg2", items1.get(1).getName());
 }

 @Test
 public void testLoadItems() {
 final FieldInfo field1 = new FieldInfo("Field1", 1, "Name1");
 final FieldInfo field2 = new FieldInfo("Field2", 2, "Name2");
 final CompositeFixInfo composite = new CompositeFixInfo("Comp1", Arrays.asList(field1, field2));
 final MessageInfo message = new MessageInfo("Msg1", "Type1", Arrays.asList(composite));

 treeView.loadItems((FixInfoTreeNode) rootNode, message, 0);

 assertEquals(2, rootNode.getChildAt(0).getChildCount());

 assertNotNull(rootNode.getChildAt(0).getChildAt(0));
 assertEquals("Field1", rootNode.getChildAt(0).getChildAt(0).getUserObject().toString());
 assertEquals(1, ((FixInfoTreeNode) rootNode.getChildAt(0).getChildAt(0)).getChildCount());
 assertEquals("Name1", ((FixInfoTreeNode) rootNode.getChildAt(0).getChildAt(0)).getChildAt(0).getUserObject().toString());

 assertNotNull(rootNode.getChildAt(0).getChildAt(1));
 assertEquals("Field2", rootNode.getChildAt(0).getChildAt(1).getUserObject().toString());
 assertEquals(1, ((FixInfoTreeNode) rootNode.getChildAt(0).getChildAt(1)).getChildCount());
 assertEquals("Name2", ((FixInfoTreeNode) rootNode.getChildAt(0).getChildAt(1)).getChildAt(0).getUserObject().toString());
 }

 @Test
 public void testExpandAll() {
 treeView.expandAll();

 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 treeView.fixTree.expandPath(new TreePath(rootNode.getPath()));
 }

 assertTrue(treeView.fixTree.isExpanded(new TreePath(rootNode.getPath())));
 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 assertFalse(treeView.fixTree.isExpanded(new TreePath(rootNode.getChildAt(i).getPath())));
 for (int j = 0; j < rootNode.getChildAt(i).getChildCount(); ++j) {
 assertTrue(treeView.fixTree.isExpanded(new TreePath(rootNode.getChildAt(i).getPath())));
 }
 }
 }

 @Test
 public void testCollapseAll() {
 treeView.collapseAll();

 for (int i = 0; i < rootNode.getChildCount(); ++i) {
 assertFalse(treeView.fixTree.isExpanded(new TreePath(rootNode.getPath())));
 for (int j = 0; j < rootNode.getChildAt(i).getChildCount(); ++j) {
 assertFalse(treeView.fixTree.isExpanded(new TreePath(rootNode.getChildAt(i).getPath())));
 }
 }
 }

 @Test
 public void testSearch() {
 final FixInfoTreeNode node1 = new FixInfoTreeNode("Node1");
 final FixInfoTreeNode node2 = new FixInfoTreeNode("Node2");
 final FixInfoTreeNode node3 = new FixInfoTreeNode("Node3");
 final FixInfoTreeNode node4 = new FixInfoTreeNode("Node4");
 final FixInfoTreeNode node5 = new FixInfoTreeNode("Node5");
 final FixInfoTreeNode node6 = new FixInfoTreeNode("Node6");

 node1.add(node2);
 node1.add(node3);
 node2.add(node4);
 node2.add(node5);
 node3.add(node6);
 rootNode.add(node1);

 treeView.search((FixInfoTreeNode) rootNode, "Node");

 assertEquals(1, node1.getChildAt(0).isHighlighted());
 assertEquals(1, node1.getChildAt(1).isHighlighted());
 assertEquals(1, node2.isHighlighted());
 assertEquals(1, node4.isHighlighted());
 assertEquals(1, node5.isHighlighted());
 assertEquals(1, node6.isHighlighted());
 }

 @Test
 public void testClear() {
 final FixInfoTreeNode node1 = new FixInfoTreeNode("Node1");
 final FixInfoTreeNode node2 = new FixInfoTreeNode("Node2");
 final FixInfoTreeNode node3 = new FixInfoTreeNode("Node3");
 final FixInfoTreeNode node4 = new FixInfoTreeNode("Node4");
 final FixInfoTreeNode node5 = new FixInfoTreeNode("Node5");
 final FixInfoTreeNode node6 = new FixInfoTreeNode("Node6");

 node1.add(node2);
 node1.add(node3);
 node2.add(node4);
 node2.add(node5);
 node3.add(node6);
 rootNode.add(node1);

 treeView.search((FixInfoTreeNode) rootNode, "Node");
 treeView.clear((FixInfoTreeNode) rootNode);

 assertEquals(0, node1.getChildAt(0).isHighlighted());
 assertEquals(0, node1.getChildAt(1).isHighlighted());
 assertEquals(0, node2.isHighlighted());
 assertEquals(0, node4.isHighlighted());
 assertEquals(0, node5.isHighlighted());
 assertEquals(0, node6.isHighlighted());
 }

 @Test
 public void testActionPerformed() {
 final TreeSelectionEvent event = mock(TreeSelectionEvent.class);
 final ActionEvent actionEvent = mock(ActionEvent.class);

 treeView.expandButton.addActionListener(treeView);
 treeView.collapseButton.addActionListener(treeView);
 treeView.searchButton.addActionListener(treeView);
 treeView.clearButton.addActionListener(treeView);

 doCallRealMethod().when(treeView).expandAll();
 doCallRealMethod().when(treeView).collapseAll();
 doCallRealMethod().when(treeView).search((FixInfoTreeNode) treeView.fixTree.getModel().getRoot(), "SearchString");
 doCallRealMethod().when(treeView).clear((FixInfoTreeNode) treeView.fixTree.getModel().getRoot());

 treeView.actionPerformed(actionEvent);
 treeView.expandButton.doClick();
 treeView.expandButton.doClick();
 treeView.searchTextField.setText("SearchString");
 treeView.searchButton.doClick();
 treeView.clearButton.doClick();
 treeView.collapseButton.doClick();
 treeView.collapseButton.doClick();

 verify(library).getDictionaries();
 verifyNoMoreInteractions(library);
 }
}