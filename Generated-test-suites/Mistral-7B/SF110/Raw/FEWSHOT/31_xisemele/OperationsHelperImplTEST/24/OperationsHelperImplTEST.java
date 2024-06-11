package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit. Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private Document createMockDocument() {
 final Document document = mock(Document.class);
 final Element root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 return document;
 }

 @Test
 public void testChildren_Empty() {
 final Node node = mock(Node.class);
 final List<Node> children = new OperationsHelperImpl().children(node);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildren_SingleChild() {
 final Node node = mock(Node.class);
 final Node child = mock(Node.class);
 when(node.getChildNodes()).thenReturn(createMockNodeList(child));
 final List<Node> children = new OperationsHelperImpl().children(node);
 assertEquals(1, children.size());
 assertEquals(child, children.get(0));
 }

 @Test
 public void testChildren_MultipleChildren() {
 final Node node = mock(Node.class);
 final Node child1 = mock(Node.class);
 final Node child2 = mock(Node.class);
 when(node.getChildNodes()).thenReturn(createMockNodeList(child1, child2));
 final List<Node> children = new OperationsHelperImpl().children(node);
 assertEquals(2, children.size());
 }

 @Test
 public void testChildren_MixedTypes() {
 final Node node = mock(Node.class);
 final Node childElement = mock(Element.class);
 final Node childText = mock(Text.class);
 when(node.getChildNodes()).thenReturn(createMockNodeList(childElement, childText));
 final List<Node> children = new OperationsHelperImpl().children(node);
 assertEquals(2, children.size());
 }

 @Test
 public void testFind_NullDocument() {
 final Node node = mock(Node.class);
 final String path = "/one/two/three";
 final List<Node> children = new ArrayList<Node>();
 children.add(node);
 when(node.getChildNodes()).thenReturn(createMockNodeList(node));
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.find(null, path));
 }

 @Test
 public void testFind_NullPath() {
 final Document document = createMockDocument();
 final Node node = mock(Node.class);
 final String path = null;
 final List<Node> children = new ArrayList<Node>();
 children.add(node);
 when(node.getChildNodes()).thenReturn(createMockNodeList(node));
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.find(document, path));
 }

 @Test
 public void testFind_EmptyDocument() {
 final Document document = createMockDocument();
 final String path = "/one/two/three";
 final List<Node> children = Collections.emptyList();
 when(document.getDocumentElement().getChildNodes()).thenReturn(children.iterator());
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.find(document, path));
 }

 @Test
 public void testFind_SimplePath() {
 final Document document = createMockDocument();
 final Node root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final Element element1 = mock(Element.class);
 final Element element2 = mock(Element.class);
 when(root.getElementsByTagName("element1")).thenReturn(createMockNodeList(element1));
 when(element1.getElementsByTagName("element2")).thenReturn(createMockNodeList(element2));
 final OperationsHelper helper = new OperationsHelperImpl();
 final Node result = helper.find(document, "element1/element2");
 assertEquals(element2, result);
 }

 @Test
 public void testFind_ComplexPath() {
 final Document document = createMockDocument();
 final Element root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final Element element1 = mock(Element.class);
 final Element element2 = mock(Element.class);
 when(root.getElementsByTagName("element1")).thenReturn(createMockNodeList(element1));
 final Element element3 = mock(Element.class);
 when(element1.getElementsByTagName("element2")).thenReturn(createMockNodeList(element3));
 final Element element4 = mock(Element.class);
 when(element3.getElementsByTagName("element3")).thenReturn(createMockNodeList(element4));
 final OperationsHelper helper = new OperationsHelperImpl();
 final Node result = helper.find(document, "element1/element2/element3");
 assertEquals(element4, result);
 }

 @Test
 public void testFind_NullResult() {
 final Document document = createMockDocument();
 final String path = "/one/two/three";
 final List<Node> children = new ArrayList<Node>();
 when(document.getDocumentElement().getChildNodes()).thenReturn(children.iterator());
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.find(document, path));
 }

 @Test
 public void testNodeWithName_NullList() {
 final List<Node> nodes = null;
 final String name = "element";
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.nodeWithName(nodes, name));
 }

 @Test
 public void testNodeWithName_EmptyList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "element";
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.nodeWithName(nodes, name));
 }

 @Test
 public void testNodeWithName_SingleNodeMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("other");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 final Node result = helper.nodeWithName(nodes, "element");
 assertEquals(node1, result);
 }

 @Test
 public void testNodeWithName_MultipleNodesMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("element");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 final Node result = helper.nodeWithName(nodes, "element");
 assertEquals(node1, result);
 }

 @Test
 public void testNodeWithName_NoMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("other");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNull(helper.nodeWithName(nodes, "other"));
 }

 @Test
 public void testSublistWithName_NullList() {
 final List<Node> nodes = null;
 final String name = "element";
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNotNull(helper.sublistWithName(nodes, name));
 assertEquals(Collections.emptyList(), helper.sublistWithName(nodes, name));
 }

 @Test
 public void testSublistWithName_EmptyList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "element";
 final OperationsHelper helper = new OperationsHelperImpl();
 assertNotNull(helper.sublistWithName(nodes, name));
 assertEquals(Collections.emptyList(), helper.sublistWithName(nodes, name));
 }

 @Test
 public void testSublistWithName_SingleNodeMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("other");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 final List<Node> result = helper.sublistWithName(nodes, "element");
 assertEquals(Arrays.asList(node1), result);
 }

 @Test
 public void testSublistWithName_MultipleNodesMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("element");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 final List<Node> result = helper.sublistWithName(nodes, "element");
 assertEquals(Arrays.asList(node1, node2), result);
 }

 @Test
 public void testSublistWithName_NoMatch() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 when(node2.getNodeName()).thenReturn("other");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final OperationsHelper helper = new OperationsHelperImpl();
 final List<Node> result = helper.sublistWithName(nodes, "other");
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testSublistWithName_MixedTypes() {
 final Node nodeElement = mock(Element.class);
 final Node nodeText = mock(Text.class);
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 final List<Node> nodes = Arrays.asList(nodeElement, nodeText, node1);
 final OperationsHelper helper = new OperationsHelperImpl();
 final List<Node> result = helper.sublistWithName(nodes, "element");
 assertEquals(Arrays.asList(nodeElement, node1), result);
 }

 private NodeList createMockNodeList(Node... nodes) {
 final NodeList nodeList = mock(NodeList.class);
 for (Node node : nodes) {
 when(nodeList.item(nodeIndex)).thenReturn(node);
 }
 return nodeList;
 }

}