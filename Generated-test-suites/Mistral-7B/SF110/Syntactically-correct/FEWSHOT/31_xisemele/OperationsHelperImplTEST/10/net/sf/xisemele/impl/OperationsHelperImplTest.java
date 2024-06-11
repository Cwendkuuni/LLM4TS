package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document mockDocument;

 @Before
 public void setup() {
 operationsHelper = new OperationsHelperImpl();
 mockDocument = mock(Document.class);
 }

 @Test
 public void testChildrenEmpty() {
 final Node emptyNode = mock(Node.class);
 final List<Node> emptyChildren = operationsHelper.children(emptyNode);
 assertNotNull(emptyChildren);
 assertTrue(emptyChildren.isEmpty());
 }

 @Test
 public void testChildrenOneElement() {
 final Element element = mock(Element.class);
 when(element.getNodeName()).thenReturn("test");
 final Node node = mock(Node.class);
 when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(node.getNextSibling()).thenReturn(null);
 when(node.getPreviousSibling()).thenReturn(null);
 when(mockDocument.getDocumentElement()).thenReturn(element);
 final List<Node> children = operationsHelper.children(mockDocument);
 assertEquals(1, children.size());
 assertSame(node, children.get(0));
 }

 @Test
 public void testChildrenMultipleElements() {
 final Element element = mock(Element.class);
 when(element.getNodeName()).thenReturn("test");
 final Node node1 = mock(Node.class);
 when(node1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(node1.getNextSibling()).thenReturn(node2);
 when(node1.getPreviousSibling()).thenReturn(null);
 final Node node2 = mock(Node.class);
 when(node2.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(node2.getNextSibling()).thenReturn(null);
 when(node2.getPreviousSibling()).thenReturn(node1);
 when(mockDocument.getDocumentElement()).thenReturn(element);
 final List<Node> children = operationsHelper.children(mockDocument);
 assertEquals(2, children.size());
 assertSame(node1, children.get(0));
 assertSame(node2, children.get(1));
 }

 @Test
 public void testFindNullDocument() {
 assertNull(operationsHelper.find(null, ""));
 }

 @Test
 public void testFindEmptyDocument() {
 assertNull(operationsHelper.find(mockDocument, ""));
 }

 @Test
 public void testFindRootElement() {
 when(mockDocument.getDocumentElement()).thenReturn(mockElement("root", "root"));
 assertSame(mockElement("root", "root"), operationsHelper.find(mockDocument, ""));
 }

 @Test
 public void testFindElementWithOneLevel() {
 when(mockDocument.getDocumentElement()).thenReturn(mockElement("root", "root"));
 when(mockElement("root", "root").getElementsByTagName("test")).thenReturn(Arrays.asList(mockElement("test1", "test1")));
 assertSame(mockElement("test1", "test1"), operationsHelper.find(mockDocument, "root/test1"));
 }

 @Test
 public void testFindElementWithMultipleLevels() {
 when(mockDocument.getDocumentElement()).thenReturn(mockElement("root", "root"));
 when(mockElement("root", "root").getElementsByTagName("test1"))
         .thenReturn(Arrays.asList(mockElement("test1_1", "test1_1"), mockElement("test1_2", "test1_2")));
 when(mockElement("test1_1", "test1_1").getElementsByTagName("test2"))
         .thenReturn(Arrays.asList(mockElement("test2_1", "test2_1")));
 assertSame(mockElement("test2_1", "test2_1"), operationsHelper.find(mockDocument, "root/test1/test2"));
 }

 @Test
 public void testFindNullName() {
 assertNull(operationsHelper.find(mockDocument, null));
 }

 @Test
 public void testFindNullDocumentAndNullName() {
 assertNull(operationsHelper.find(null, null));
 }

 @Test
 public void testFindElementWithInvalidPath() {
 assertNull(operationsHelper.find(mockDocument, "/invalid/path"));
 }

 @Test
 public void testNodeWithNameNullList() {
 assertNull(operationsHelper.nodeWithName(null, "name"));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 assertNull(operationsHelper.nodeWithName(Collections.emptyList(), "name"));
 }

 @Test
 public void testNodeWithNameSingleNode() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("name");
 assertSame(node, operationsHelper.nodeWithName(Collections.singletonList(node), "name"));
 }

 @Test
 public void testNodeWithNameMultipleNodes() {
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("name1");
 final Node node2 = mock(Node.class);
 when(node2.getNodeName()).thenReturn("name2");
 final List<Node> nodes = new ArrayList<>(Arrays.asList(node1, node2));
 assertNull(operationsHelper.nodeWithName(nodes, "name3"));
 assertSame(node1, operationsHelper.nodeWithName(nodes, "name1"));
 assertSame(node2, operationsHelper.nodeWithName(nodes, "name2"));
 }

 @Test
 public void testSublistWithNameNullList() {
 assertNotNull(operationsHelper.sublistWithName(null, "name"));
 assertEquals(0, operationsHelper.sublistWithName(null, "name").size());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 assertNotNull(operationsHelper.sublistWithName(Collections.emptyList(), "name"));
 assertEquals(0, operationsHelper.sublistWithName(Collections.emptyList(), "name").size());
 }

 @Test
 public void testSublistWithNameSingleNode() {
 final Node node = mock(Node.class);
 when(node.getNodeName()).thenReturn("name");
 final List<Node> sublist = operationsHelper.sublistWithName(Collections.singletonList(node), "name");
 assertEquals(1, sublist.size());
 assertSame(node, sublist.get(0));
 }

 @Test
 public void testSublistWithNameMultipleNodes() {
 final Node node1 = mock(Node.class);
 when(node1.getNodeName()).thenReturn("name1");
 final Node node2 = mock(Node.class);
 when(node2.getNodeName()).thenReturn("name2");
 final List<Node> nodes = new ArrayList<>(Arrays.asList(node1, node2));
 final List<Node> sublist = operationsHelper.sublistWithName(nodes, "name1");
 assertEquals(1, sublist.size());
 assertSame(node1, sublist.get(0));
 final List<Node> sublist2 = operationsHelper.sublistWithName(nodes, "name2");
 assertEquals(1, sublist2.size());
 assertSame(node2, sublist2.get(0));
 }

 private Node mockElement(String tagName, String nodeName) {
 final Element element = mock(Element.class);
 when(element.getNodeName()).thenReturn(tagName);
 when(element.getTagName()).thenReturn(nodeName);
 return element;
 }
}