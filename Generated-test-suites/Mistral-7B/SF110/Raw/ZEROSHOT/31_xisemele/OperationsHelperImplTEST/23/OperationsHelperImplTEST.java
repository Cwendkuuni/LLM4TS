package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 final Document document = createDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));
 root.appendChild(createElement("child3"));

 List<Node> children = operationsHelper.children(root);
 assertEquals(3, children.size());
 assertEquals("child1", children.get(0).getNodeName());
 assertEquals("child2", children.get(1).getNodeName());
 assertEquals("child3", children.get(2).getNodeName());
 }

 @Test
 public void testFindNull() {
 final Document document = createDocument();
 assertNull(operationsHelper.find(document, null));
 }

 @Test
 public void testFindEmptyPath() {
 final Document document = createDocument();
 final Element root = document.getDocumentElement();
 assertSame(root, operationsHelper.find(document, ""));
 }

 @Test
 public void testFindDocumentElement() {
 final Document document = createDocument();
 final Element root = document.getDocumentElement();
 assertSame(root, operationsHelper.find(document, root.getNodeName()));
 }

 @Test
 public void testFindChild1() {
 final Document document = createDocument();
 final Element root = document.getDocumentElement();
 final Element child1 = createElement("child1");
 root.appendChild(child1);

 assertSame(child1, operationsHelper.find(document, "child1"));
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullDocument() {
 final String path = "child1";
 assertNull(operationsHelper.find(null, path));
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullPath() {
 final Document document = createDocument();
 assertNull(operationsHelper.find(document, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidPath() {
 final Document document = createDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));

 assertNull(operationsHelper.find(document, "child2/child3"));
 }

 @Test
 public void testNodeWithNameNull() {
 final List<Node> nodes = Arrays.asList(new Node[0]);
 assertNull(operationsHelper.nodeWithName(nodes, null));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 final List<Node> nodes = Arrays.asList(new Node[0]);
 assertNull(operationsHelper.nodeWithName(nodes, ""));
 }

 @Test
 public void testNodeWithNameEmptyName() {
 final Node node = createElement("node");
 final List<Node> nodes = Arrays.asList(node);
 assertNull(operationsHelper.nodeWithName(nodes, ""));
 }

 @Test
 public void testNodeWithNameSingleNode() {
 final Node node = createElement("node");
 final List<Node> nodes = Arrays.asList(node);
 assertSame(node, operationsHelper.nodeWithName(nodes, node.getNodeName()));
 }

 @Test
 public void testNodeWithNameMultipleNodes() {
 final Node node1 = createElement("node1");
 final Node node2 = createElement("node2");
 final List<Node> nodes = Arrays.asList(node1, node2);
 assertNull(operationsHelper.nodeWithName(nodes, "node3"));
 assertSame(node1, operationsHelper.nodeWithName(nodes, node1.getNodeName()));
 assertSame(node2, operationsHelper.nodeWithName(nodes, node2.getNodeName()));
 }

 @Test
 public void testSublistWithNameEmptyList() {
 final List<Node> nodes = Arrays.asList(new Node[0]);
 assertEquals(0, operationsHelper.sublistWithName(nodes, "").size());
 }

 @Test
 public void testSublistWithNameEmptyName() {
 final List<Node> nodes = Arrays.asList(new Node[0]);
 assertEquals(0, operationsHelper.sublistWithName(nodes, "").size());
 }

 @Test
 public void testSublistWithNameSingleNode() {
 final Node node = createElement("node");
 final List<Node> nodes = Arrays.asList(node);
 assertEquals(1, operationsHelper.sublistWithName(nodes, node.getNodeName()).size());
 assertEquals(node, operationsHelper.sublistWithName(nodes, node.getNodeName()).get(0));
 }

 @Test
 public void testSublistWithNameMultipleNodes() {
 final Node node1 = createElement("node1");
 final Node node2 = createElement("node2");
 final List<Node> nodes = Arrays.asList(node1, node2);
 assertEquals(0, operationsHelper.sublistWithName(nodes, "node3").size());
 assertEquals(2, operationsHelper.sublistWithName(nodes, "node1").size());
 assertEquals(node1, operationsHelper.sublistWithName(nodes, "node1").get(0));
 assertEquals(node2, operationsHelper.sublistWithName(nodes, "node2").get(0));
 }

 private Document createDocument() {
 final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 final Document document;
 try {
 document = documentFactory.newDocument();
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 return document;
 }

 private Element createElement(final String name) {
 final Document document = createDocument();
 final Element element = document.createElement(name);
 document.appendChild(element);
 return element;
 }
}