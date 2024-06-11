package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;
import org.mockito.Mockito;

public class OperationsHelperImplTest {

 private Document createMockDocument() {
 Element documentElement = mock(Element.class);
 Document document = mock(Document.class);
 when(document.getDocumentElement()).thenReturn(documentElement);
 return document;
 }

 @Test
 public void testChildren_EmptyNode() {
 Node node = mock(Node.class);
 when(node.getChildNodes()).thenReturn(NodeList.EMPTY_NODE_LIST);
 List<Node> children = new OperationsHelperImpl().children(node);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildren_NoChildrenNodes() {
 Node node = mock(Node.class);
 when(node.getChildNodes()).thenReturn(NodeList.EMPTY_NODE_LIST);
 List<Node> children = new OperationsHelperImpl().children(node);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildren_SingleElementChild() {
 Node node = mock(Node.class);
 Node child = mock(Node.class);
 when(node.getChildNodes()).thenReturn(NodeList.nodeList(child));
 List<Node> children = new OperationsHelperImpl().children(node);
 assertEquals(1, children.size());
 assertSame(child, children.get(0));
 }

 @Test
 public void testChildren_MultipleElementChildren() {
 Node node = mock(Node.class);
 Node child1 = mock(Node.class);
 Node child2 = mock(Node.class);
 when(node.getChildNodes()).thenReturn(NodeList.nodeList(child1, child2));
 List<Node> children = new OperationsHelperImpl().children(node);
 assertEquals(2, children.size());
 assertSame(child1, children.get(0));
 assertSame(child2, children.get(1));
 }

 @Test
 public void testFind_NotFound() {
 Document document = createMockDocument();
 List<Node> children = Arrays.asList(mock(Element.class), mock(Node.class), mock(Element.class));
 when(document.getDocumentElement().getChildNodes()).thenReturn(NodeList.nodeList(children.toArray(new Node[children.size()])));
 assertNull(new OperationsHelperImpl().find(document, "/nonexistent"));
 }

 @Test
 public void testFind_Found() {
 Document document = createMockDocument();
 Element root = mock(Element.class);
 Element element1 = mock(Element.class);
 Element element2 = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getTagName()).thenReturn("root");
 when(root.getChildNodes()).thenReturn(NodeList.nodeList(element1, element2));
 when(element1.getTagName()).thenReturn("element1");
 when(element2.getTagName()).thenReturn("element2");
 when(element2.getChildNodes()).thenReturn(NodeList.nodeList(mock(Element.class)));
 assertSame(element2, new OperationsHelperImpl().find(document, "/root/element1/element2"));
 }

 @Test
 public void testNodeWithName_EmptyList() {
 List<Node> nodes = Collections.emptyList();
 assertNull(new OperationsHelperImpl().nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithName_NotFound() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
 assertNull(new OperationsHelperImpl().nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithName_Found() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Element.class), mock(Node.class));
 assertSame(nodes.get(1), new OperationsHelperImpl().nodeWithName(nodes, "element"));
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> nodes = Collections.emptyList();
 assertSame(Collections.emptyList(), new OperationsHelperImpl().sublistWithName(nodes, "name"));
 }

 @Test
 public void testSublistWithName_NotFound() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
 assertSame(Collections.emptyList(), new OperationsHelperImpl().sublistWithName(nodes, "name"));
 }

 @Test
 public void testSublistWithName_SingleElementFound() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Element.class), mock(Node.class));
 assertSame(Arrays.asList(nodes.get(1)), new OperationsHelperImpl().sublistWithName(nodes, "element"));
 }

 @Test
 public void testSublistWithName_MultipleElementsFound() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Element.class), mock(Element.class), mock(Element.class));
 assertSame(Arrays.asList(nodes.get(1), nodes.get(3)), new OperationsHelperImpl().sublistWithName(nodes, "element"));
 }

}