package net.sf.xisemele.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setup() {
 operationsHelper = new OperationsHelperImpl();
 document = mock(Document.class);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testChildren_EmptyNodeList() {
 final Node emptyNode = mock(Node.class);
 when(emptyNode.getChildNodes()).thenReturn(Collections.emptyList().asList().subList(0, 1).iterator().next());

 final List<Node> children = operationsHelper.children(emptyNode);
 assertSame(Collections.emptyList(), children);
 }

 @Test
 public void testChildren_SingleChild() {
 final Node childNode = mock(Node.class);
 when(childNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 final NodeList nodeList = new NodeList() {
 @Override
 public int getLength() {
 return 1;
 }

 @Override
 public Node item(int index) {
 return childNode;
 }
 };
 final Node parentNode = mock(Node.class);
 when(parentNode.getChildNodes()).thenReturn(nodeList);

 final List<Node> children = operationsHelper.children(parentNode);
 assertSame(Collections.singletonList(childNode), children);
 }

 @Test
 public void testChildren_MultipleChildren() {
 final Node parentNode = mock(Node.class);
 final Node childNode1 = mock(Node.class);
 final Node childNode2 = mock(Node.class);
 final Node childNode3 = mock(Node.class);
 when(childNode1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(childNode2.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(childNode3.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 final NodeList nodeList = new NodeList() {
 @Override
 public int getLength() {
 return 3;
 }

 @Override
 public Node item(int index) {
 if (index == 0) {
 return childNode1;
 } else if (index == 1) {
 return childNode2;
 } else {
 return childNode3;
 }
 }
 };
 when(parentNode.getChildNodes()).thenReturn(nodeList);

 final List<Node> children = operationsHelper.children(parentNode);
 assertEquals(Arrays.asList(childNode1, childNode2, childNode3), children);
 }

 @Test
 public void testFind_DocumentWithRootAndChild() {
 final Element rootElement = mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("root");
 when(document.getDocumentElement()).thenReturn(rootElement);
 final Element childElement = mock(Element.class);
 when(childElement.getNodeName()).thenReturn("child");
 final NodeList nodeList = new NodeList() {
 @Override
 public int getLength() {
 return 1;
 }

 @Override
 public Node item(int index) {
 return childElement;
 }
 };
 when(rootElement.getChildNodes()).thenReturn(nodeList);

 final String path = "root/child";
 final Node expectedNode = childElement;
 final Node actualNode = operationsHelper.find(document, path);
 assertSame(expectedNode, actualNode);
 }

 @Test
 public void testFind_DocumentWithNoMatchingPath() {
 final Element rootElement = mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("root");
 when(document.getDocumentElement()).thenReturn(rootElement);
 final NodeList nodeList = new NodeList() {
 @Override
 public int getLength() {
 return 0;
 }
 };
 when(rootElement.getChildNodes()).thenReturn(nodeList);

 final String path = "root/child";
 final Node actualNode = operationsHelper.find(document, path);
 assertNull(actualNode);
 }

 @Test
 public void testFind_DocumentWithDeepPath() {
 final Element rootElement = mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("root");
 final Element childElement1 = mock(Element.class);
 when(childElement1.getNodeName()).thenReturn("child1");
 when(rootElement.getChildNodes()).thenReturn(Collections.singletonList(childElement1).iterator().asList());
 final Element childElement2 = mock(Element.class);
 when(childElement2.getNodeName()).thenReturn("child2");
 when(childElement1.getChildNodes()).thenReturn(Collections.singletonList(childElement2).iterator().asList());
 final Node actualNode = operationsHelper.find(document, "root/child1/child2");
 assertSame(childElement2, actualNode);
 }

 @Test
 public void testNodeWithName_NullNodeList() {
 final List<Node> nodes = null;
 final String name = "element";
 final Node actualNode = operationsHelper.nodeWithName(nodes, name);
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_EmptyNodeList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "element";
 final Node actualNode = operationsHelper.nodeWithName(nodes, name);
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_SingleNodeWithMatchingName() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("element");
 final List<Node> nodes = Collections.singletonList(node);
 final Node actualNode = operationsHelper.nodeWithName(nodes, "element");
 assertSame(node, actualNode);
 }

 @Test
 public void testNodeWithName_SingleNodeWithDifferentName() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("differentName");
 final List<Node> nodes = Collections.singletonList(node);
 final Node actualNode = operationsHelper.nodeWithName(nodes, "element");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_MultipleNodesWithMatchingName() {
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 final Node node2 = mock(Node.class);
 when(node2.getNodeName()).thenReturn("element");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final Node actualNode = operationsHelper.nodeWithName(nodes, "element");
 assertSame(node1, actualNode);
 }

 @Test
 public void testSublistWithName_NullNodeList() {
 final List<Node> nodes = null;
 final String name = "element";
 final List<Node> expectedSublist = Collections.emptyList();
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, name);
 assertSame(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_EmptyNodeList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "element";
 final List<Node> expectedSublist = Collections.emptyList();
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, name);
 assertSame(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_SingleNodeWithMatchingName() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("element");
 final List<Node> nodes = Collections.singletonList(node);
 final List<Node> expectedSublist = Collections.singletonList(node);
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "element");
 assertSame(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_SingleNodeWithDifferentName() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("differentName");
 final List<Node> nodes = Collections.singletonList(node);
 final List<Node> expectedSublist = Collections.emptyList();
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "element");
 assertSame(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithMatchingName() {
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element");
 final Node node2 = mock(Node.class);
 when(node2.getNodeName()).thenReturn("element");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final List<Node> expectedSublist = Arrays.asList(node1, node2);
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "element");
 assertSame(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithDifferentName() {
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("element1");
 final Node node2 = mock(Node.class);
 when(node2.getNodeName()).thenReturn("element2");
 final List<Node> nodes = Arrays.asList(node1, node2);
 final List<Node> expectedSublist = Collections.emptyList();
 final List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "element");
 assertSame(expectedSublist, actualSublist);
 }
}