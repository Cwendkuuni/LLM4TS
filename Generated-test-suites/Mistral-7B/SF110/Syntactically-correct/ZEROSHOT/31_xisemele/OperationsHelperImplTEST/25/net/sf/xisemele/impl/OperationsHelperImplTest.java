package net.sf.xisemele.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.w3c.dom.*;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setUp() {
 operationsHelper = new OperationsHelperImpl();
 document = Mockito.mock(Document.class);
 }

 @Test
 public void testChildren() {
 Node node = Mockito.mock(Node.class);
 NodeList nodeList = Mockito.mock(NodeList.class);
 Mockito.when(node.getChildNodes()).thenReturn(nodeList);
 Mockito.when(nodeList.getLength()).thenReturn(3);
 Mockito.when(nodeList.item(0)).thenReturn(node);
 Mockito.when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 List<Node> expected = Arrays.asList(node, node, node);
 List<Node> actual = operationsHelper.children(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testChildren_NoChildren() {
 Node node = Mockito.mock(Node.class);
 NodeList nodeList = Mockito.mock(NodeList.class);
 Mockito.when(node.getChildNodes()).thenReturn(nodeList);
 Mockito.when(nodeList.getLength()).thenReturn(0);
 List<Node> actual = operationsHelper.children(node);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testFind_ExactMatch() {
 Node documentElement = Mockito.mock(Element.class);
 Mockito.when(document.getDocumentElement()).thenReturn(documentElement);
 Node element = Mockito.mock(Element.class);
 NodeList nodeList = Mockito.mock(NodeList.class);
 Node child = Mockito.mock(Element.class);
 Mockito.when(documentElement.getElementsByTagName("elementName")).thenReturn(nodeList);
 Mockito.when(nodeList.getItem(0)).thenReturn(child);
 List<String> path = Arrays.asList("elementName");
 Node actual = operationsHelper.find(document, "/elementName");
 assertEquals(child, actual);
 }

 @Test
 public void testFind_NoMatch() {
 Node documentElement = Mockito.mock(Element.class);
 Mockito.when(document.getDocumentElement()).thenReturn(documentElement);
 NodeList nodeList = Mockito.mock(NodeList.class);
 Mockito.when(documentElement.getElementsByTagName("elementName")).thenReturn(nodeList);
 List<String> path = Arrays.asList("nonexistentElementName");
 Node actual = operationsHelper.find(document, "/nonexistentElementName");
 assertNull(actual);
 }

 @Test
 public void testFind_MultipleMatches() {
 Node documentElement = Mockito.mock(Element.class);
 Node element1 = Mockito.mock(Element.class);
 Node element2 = Mockito.mock(Element.class);
 NodeList nodeList1 = Mockito.mock(NodeList.class);
 NodeList nodeList2 = Mockito.mock(NodeList.class);
 Mockito.when(document.getDocumentElement()).thenReturn(documentElement);
 Mockito.when(documentElement.getElementsByTagName("elementName")).thenReturn(nodeList1);
 Mockito.when(nodeList1.getLength()).thenReturn(2);
 Mockito.when(nodeList1.getItem(0)).thenReturn(element1);
 Mockito.when(nodeList1.getItem(1)).thenReturn(element2);
 Mockito.when(element1.getElementsByTagName("subElementName")).thenReturn(nodeList2);
 Mockito.when(nodeList2.getLength()).thenReturn(1);
 Mockito.when(nodeList2.getItem(0)).thenReturn(Mockito.mock(Element.class));
 List<String> path = Arrays.asList("elementName", "subElementName");
 Node actual = operationsHelper.find(document, path.toString());
 assertNotNull(actual);
 assertEquals(Mockito.mock(Element.class), actual);
 }

 @Test
 public void testNodeWithName() {
 NodeList nodeList = Mockito.mock(NodeList.class);
 Node node1 = Mockito.mock(Element.class);
 Node node2 = Mockito.mock(Element.class);
 Mockito.when(nodeList.getLength()).thenReturn(2);
 Mockito.when(nodeList.getItem(0)).thenReturn(node1);
 Mockito.when(nodeList.getItem(1)).thenReturn(node2);
 List<Node> nodes = Arrays.asList(node1, node2);
 String name = "name";
 Node expected = node1;
 Node actual = operationsHelper.nodeWithName(nodes, name);
 assertEquals(expected, actual);
 }

 @Test
 public void testNodeWithName_NoMatch() {
 NodeList nodeList = Mockito.mock(NodeList.class);
 Node node1 = Mockito.mock(Element.class);
 Node node2 = Mockito.mock(Element.class);
 Mockito.when(nodeList.getLength()).thenReturn(2);
 Mockito.when(nodeList.getItem(0)).thenReturn(node1);
 Mockito.when(nodeList.getItem(1)).thenReturn(node2);
 List<Node> nodes = Arrays.asList(node1, node2);
 String name = "nonexistentName";
 Node actual = operationsHelper.nodeWithName(nodes, name);
 assertNull(actual);
 }

 @Test
 public void testSublistWithName() {
 NodeList nodeList = Mockito.mock(NodeList.class);
 Node node1 = Mockito.mock(Element.class);
 Node node2 = Mockito.mock(Element.class);
 Node node3 = Mockito.mock(Element.class);
 Mockito.when(nodeList.getLength()).thenReturn(3);
 Mockito.when(nodeList.getItem(0)).thenReturn(node1);
 Mockito.when(nodeList.getItem(1)).thenReturn(node2);
 Mockito.when(nodeList.getItem(2)).thenReturn(node3);
 List<Node> nodes = Arrays.asList(node1, node2, node3);
 String name = "name";
 List<Node> expected = Arrays.asList(node1, node2);
 List<Node> actual = operationsHelper.sublistWithName(nodes, name);
 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithName_NoMatch() {
 NodeList nodeList = Mockito.mock(NodeList.class);
 Node node1 = Mockito.mock(Element.class);
 Node node2 = Mockito.mock(Element.class);
 Node node3 = Mockito.mock(Element.class);
 Mockito.when(nodeList.getLength()).thenReturn(3);
 Mockito.when(nodeList.getItem(0)).thenReturn(node1);
 Mockito.when(nodeList.getItem(1)).thenReturn(node2);
 Mockito.when(nodeList.getItem(2)).thenReturn(node3);
 List<Node> nodes = Arrays.asList(node1, node2, node3);
 String name = "nonexistentName";
 List<Node> actual = operationsHelper.sublistWithName(nodes, name);
 assertTrue(actual.isEmpty());
 }
}