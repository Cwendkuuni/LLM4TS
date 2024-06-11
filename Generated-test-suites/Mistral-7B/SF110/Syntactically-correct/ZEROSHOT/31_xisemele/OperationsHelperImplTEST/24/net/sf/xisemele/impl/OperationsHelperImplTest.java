package net.sf.xisemele.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setUp() {
 operationsHelper = new OperationsHelperImpl();
 document = mock(Document.class);
 }

 @Test
 public void testChildren_EmptyNodeList() {
 final Node emptyNode = mock(Node.class);
 when(emptyNode.getChildNodes()).thenReturn(Collections.emptyList().asList().subList(0, 0).iterator().asList());

 final List<Node> children = operationsHelper.children(emptyNode);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildren_NodeWithChildren() {
 final Node parentNode = mock(Node.class);
 when(parentNode.getChildNodes()).thenReturn(Arrays.asList(mock(Element.class), mock(Element.class)).subList(0, 2).iterator().asList());

 final List<Node> children = operationsHelper.children(parentNode);
 assertTrue(children.size() == 2);
 }

 @Test
 public void testFind_DocumentWithRootAndChildren() {
 when(document.getDocumentElement().getNodeName()).thenReturn("root");
 when(document.getDocumentElement().getChildNodes()).thenReturn(Arrays.asList(mock(Element.class), mock(Element.class)).subList(0, 2).iterator().asList());

 final Node child1 = mock(Element.class);
 when(child1.getNodeName()).thenReturn("child1");
 when(child1.getChildNodes()).thenReturn(Collections.emptyList());

 final Node child2 = mock(Element.class);
 when(child2.getNodeName()).thenReturn("child2");
 when(child2.getChildNodes()).thenReturn(Collections.emptyList());

 when(document.getDocumentElement().getChildNodes().item(0)).thenReturn(child1);
 when(child1.getChildNodes().item(0)).thenReturn(document);

 final Node result = operationsHelper.find(document, "root/child1");
 assertSame(document, result);
 }

 @Test
 public void testFind_DocumentWithNoMatchingNode() {
 when(document.getDocumentElement().getNodeName()).thenReturn("root");
 when(document.getDocumentElement().getChildNodes()).thenReturn(Arrays.asList(mock(Element.class), mock(Element.class)).subList(0, 2).iterator().asList());

 final Node child1 = mock(Element.class);
 when(child1.getNodeName()).thenReturn("child1");
 when(child1.getChildNodes()).thenReturn(Collections.emptyList());

 when(document.getDocumentElement().getChildNodes().item(0)).thenReturn(child1);

 final Node result = operationsHelper.find(document, "root/child3");
 assertNull(result);
 }

 @Test
 public void testFind_NodeWithNoChildren() {
 final Node leafNode = mock(Element.class);
 when(leafNode.getNodeName()).thenReturn("leaf");
 when(leafNode.getChildNodes()).thenReturn(Collections.emptyList());

 final Node parentNode = mock(Element.class);
 when(parentNode.getNodeName()).thenReturn("parent");
 when(parentNode.getChildNodes()).thenReturn(Collections.singletonList(leafNode));

 final Node result = operationsHelper.find(parentNode, "parent/leaf");
 assertSame(leafNode, result);
 }

 @Test
 public void testNodeWithName_EmptyList() {
 final List<Node> emptyList = Collections.emptyList();
 final Node result = operationsHelper.nodeWithName(emptyList, "nodeName");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_ListWithMatchingNode() {
 final Node node1 = mock(Element.class);
 when(node1.getNodeName()).thenReturn("matchingNode");

 final Node node2 = mock(Element.class);
 when(node2.getNodeName()).thenReturn("otherNode");

 final List<Node> nodes = Arrays.asList(node1, node2);
 final Node expectedResult = node1;

 final Node result = operationsHelper.nodeWithName(nodes, "matchingNode");
 assertSame(expectedResult, result);
 }

 @Test
 public void testNodeWithName_ListWithNoMatchingNode() {
 final Node node1 = mock(Element.class);
 when(node1.getNodeName()).thenReturn("matchingNode");

 final Node node2 = mock(Element.class);
 when(node2.getNodeName()).thenReturn("otherNode");

 final List<Node> nodes = Arrays.asList(node1, node2);

 final Node result = operationsHelper.nodeWithName(nodes, "nonMatchingNode");
 assertNull(result);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 final List<Node> emptyList = Collections.emptyList();
 final List<Node> result = operationsHelper.sublistWithName(emptyList, "nodeName");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithName_ListWithMatchingNodes() {
 final Node node1 = mock(Element.class);
 when(node1.getNodeName()).thenReturn("matchingNode");

 final Node node2 = mock(Element.class);
 when(node2.getNodeName()).thenReturn("otherNode");

 final List<Node> nodes = Arrays.asList(node1, node1, node2);
 final List<Node> expectedResult = Arrays.asList(node1, node1);

 final List<Node> result = operationsHelper.sublistWithName(nodes, "matchingNode");
 assertSame(expectedResult, result);
 }

 @Test
 public void testSublistWithName_ListWithNoMatchingNodes() {
 final Node node1 = mock(Element.class);
 when(node1.getNodeName()).thenReturn("matchingNode");

 final Node node2 = mock(Element.class);
 when(node2.getNodeName()).thenReturn("otherNode");

 final List<Node> nodes = Arrays.asList(node1, node2);

 final List<Node> result = operationsHelper.sublistWithName(nodes, "nonMatchingNode");
 assertTrue(result.isEmpty());
 }
}