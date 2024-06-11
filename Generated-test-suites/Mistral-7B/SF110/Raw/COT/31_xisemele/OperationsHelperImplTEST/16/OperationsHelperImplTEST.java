package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper;
 private Document document;

 @Before
 public void setUp() {
 operationsHelper = new OperationsHelperImpl();
 document = mock(Document.class);
 }

 // Test children method
 @Test
 public void testChildren() {
 final Node node = mock(Node.class);
 when(node.getChildNodes()).thenReturn(mock(NodeList.class));
 when(mock(NodeList.class).getLength()).thenReturn(5);

 final List<Node> result = operationsHelper.children(node);
 assertEquals(5, result.size());
 }

 // Test children method with no child nodes
 @Test
 public void testChildrenWithNoChildNodes() {
 final Node node = mock(Node.class);
 when(node.getChildNodes()).thenReturn(mock(NodeList.class));
 when(mock(NodeList.class).getLength()).thenReturn(0);

 final List<Node> result = operationsHelper.children(node);
 assertEquals(0, result.size());
 }

 // Test children method with empty list
 @Test
 public void testChildrenWithEmptyList() {
 final List<Node> nodes = Collections.emptyList();
 final List<Node> result = operationsHelper.children(mock(Node.class));
 assertEquals(nodes, result);
 }

 // Test find method with valid path
 @Test
 public void testFindWithValidPath() {
 final Document document = mock(Document.class);
 final Element root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getNodeName()).thenReturn("html");

 final List<Node> children = new ArrayList<>();
 children.add(mock(Element.class));
 children.add(mock(Element.class));
 children.add(mock(Element.class));
 children.add(mock(Element.class));
 children.add(mock(Element.class));
 when(root.getChildNodes()).thenReturn(mock(NodeList.class));
 when(mock(NodeList.class).getLength()).thenReturn(children.size());
 when(mock(NodeList.class).item(0)).thenReturn(root);
 when(mock(NodeList.class).item(1)).thenReturn(children.get(1));
 when(mock(NodeList.class).item(2)).thenReturn(children.get(2));
 when(mock(NodeList.class).item(3)).thenReturn(children.get(3));
 when(mock(NodeList.class).item(4)).thenReturn(children.get(4));

 final Node result = operationsHelper.find(document, "/html/body/p");
 assertSame(children.get(3), result);
 }

 // Test find method with invalid path
 @Test
 public void testFindWithInvalidPath() {
 final Document document = mock(Document.class);
 when(document.getDocumentElement()).thenReturn(mock(Element.class));

 final Node result = operationsHelper.find(document, "/html/body/p/div");
 assertNull(result);
 }

 // Test find method with empty document
 @Test
 public void testFindWithEmptyDocument() {
 final Document document = mock(Document.class);
 when(document.getDocumentElement()).thenReturn(mock(Element.class));
 when(mock(Element.class).getNodeName()).thenReturn("html");

 final Node result = operationsHelper.find(document, "/html/body/p");
 assertNull(result);
 }

 // Test nodeWithName method with valid name
 @Test
 public void testNodeWithNameWithValidName() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 final List<Node> nodes = Arrays.asList(node1, node2);

 final Node result = operationsHelper.nodeWithName(nodes, "Node1");
 assertSame(node1, result);
 }

 // Test nodeWithName method with invalid name
 @Test
 public void testNodeWithNameWithInvalidName() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 final List<Node> nodes = Arrays.asList(node1, node2);

 final Node result = operationsHelper.nodeWithName(nodes, "Node3");
 assertNull(result);
 }

 // Test sublistWithName method with valid name and list
 @Test
 public void testSublistWithNameWithValidNameAndList() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 final Node node3 = mock(Node.class);
 final List<Node> nodes = Arrays.asList(node1, node2, node3);

 final List<Node> result = operationsHelper.sublistWithName(nodes, "Node2");
 assertEquals(1, result.size());
 assertSame(node2, result.get(0));
 }

 // Test sublistWithName method with invalid name
 @Test
 public void testSublistWithNameWithInvalidName() {
 final Node node1 = mock(Node.class);
 final Node node2 = mock(Node.class);
 final List<Node> nodes = Arrays.asList(node1, node2);

 final List<Node> result = operationsHelper.sublistWithName(nodes, "Node3");
 assertEquals(0, result.size());
 }

 // Test sublistWithName method with empty list
 @Test
 public void testSublistWithNameWithEmptyList() {
 final List<Node> nodes = Collections.emptyList();
 final List<Node> result = operationsHelper.sublistWithName(nodes, "Node1");
 assertEquals(0, result.size());
 }
}