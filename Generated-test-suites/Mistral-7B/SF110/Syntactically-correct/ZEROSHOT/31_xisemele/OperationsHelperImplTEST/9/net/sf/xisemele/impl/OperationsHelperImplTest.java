package net.sf.xisemele.impl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setup() {
 document = mock(Document.class);
 operationsHelper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren() {
 Node node = mock(Node.class);
 NodeList nodeList = mock(NodeList.class);
 when(node.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getLength()).thenReturn(3);
 when(nodeList.item(0)).thenReturn(node);
 when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);

 List<Node> result = operationsHelper.children(node);
 assertEquals(Arrays.asList(node), result);
 }

 @Test
 public void testChildren_Empty() {
 Node node = mock(Node.class);
 NodeList nodeList = mock(NodeList.class);
 when(node.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getLength()).thenReturn(0);

 List<Node> result = operationsHelper.children(node);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testChildren_MixedTypes() {
 Node node = mock(Node.class);
 Node textNode = mock(Node.class);
 NodeList nodeList = mock(NodeList.class);
 when(node.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getLength()).thenReturn(3);
 when(nodeList.item(0)).thenReturn(node);
 when(nodeList.item(1)).thenReturn(textNode);
 when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);

 List<Node> result = operationsHelper.children(node);
 assertEquals(Arrays.asList(node), result);
 }

 @Test
 public void testFind_SimplePath() {
 Node documentElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(documentElement);
 Node root = mock(Element.class);
 when(documentElement).thenReturn(root);
 Node child1 = mock(Element.class);
 when(root.getElementsByTagName("child1")).thenReturn(new NodeList() {
 @Override
 public Node item(int index) {
 return index == 0 ? child1 : null;
 }

 @Override
 public int getLength() {
 return 1;
 }
 });
 Node child2 = mock(Element.class);
 when(child1.getElementsByTagName("child2")).thenReturn(new NodeList() {
 @Override
 public Node item(int index) {
 return index == 0 ? child2 : null;
 }

 @Override
 public int getLength() {
 return 1;
 }
 });

 List<String> path = Arrays.asList("documentElement", "child1", "child2");
 Node result = operationsHelper.find(document, String.join("/", path));
 assertEquals(child2, result);
 }

 @Test
 public void testFind_EmptyPath() {
 Node result = operationsHelper.find(document, "");
 assertNull(result);
 }

 @Test
 public void testFind_InvalidPath() {
 Node result = operationsHelper.find(document, "/child1/child2/child3/child4/child5/child6/child7/child8/child9/child10");
 assertNull(result);
 }

 @Test
 public void testFind_NullDocument() {
 Node result = operationsHelper.find(null, "/documentElement");
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("name1");
 when(node2.getNodeName()).thenReturn("name2");

 List<Node> nodes = Arrays.asList(node1, node2);
 Node result = operationsHelper.nodeWithName(nodes, "name1");
 assertEquals(node1, result);
 }

 @Test
 public void testNodeWithName_NullList() {
 Node result = operationsHelper.nodeWithName(null, "name");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_EmptyList() {
 Node result = operationsHelper.nodeWithName(Collections.emptyList(), "name");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_NullName() {
 Node result = operationsHelper.nodeWithName(Arrays.asList(mock(Node.class)), null);
 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 Node node1 = mock(Element.class);
 Node node2 = mock(Element.class);
 Node node3 = mock(Element.class);
 when(node1.getNodeName()).thenReturn("name1");
 when(node2.getNodeName()).thenReturn("name2");
 when(node3.getNodeName()).thenReturn("name1");
 when(node1.getChildNodes()).thenReturn(new NodeList() {
 @Override
 public Node item(int index) {
 return index == 0 ? node2 : (index == 1 ? node3 : null);
 }

 @Override
 public int getLength() {
 return 3;
 }
 });

 List<Node> result = operationsHelper.sublistWithName(Arrays.asList(node1), "name1");
 assertEquals(Arrays.asList(node1, node3), result);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> result = operationsHelper.sublistWithName(Collections.emptyList(), "name");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithName_NullList() {
 List<Node> result = operationsHelper.sublistWithName(null, "name");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> result = operationsHelper.sublistWithName(Arrays.asList(mock(Node.class)), null);
 assertTrue(result.isEmpty());
 }
}