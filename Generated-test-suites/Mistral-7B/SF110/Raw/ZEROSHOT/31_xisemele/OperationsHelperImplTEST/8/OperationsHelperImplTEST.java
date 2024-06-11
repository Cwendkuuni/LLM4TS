package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 Node testNode = createTestNode();
 List<Node> children = operationsHelper.children(testNode);
 assertEquals(3, children.size());
 assertEquals("element1", children.get(0).getNodeName());
 assertEquals("element2", children.get(1).getNodeName());
 assertEquals("element3", children.get(2).getNodeName());
 }

 @Test
 public void testFindNull() {
 assertNull(operationsHelper.find(null, "/element1"));
 }

 @Test
 public void testFindEmptyDocument() {
 Document document = createEmptyDocument();
 assertNull(operationsHelper.find(document, "/element1"));
 }

 @Test
 public void testFindSimplePath() {
 Document document = createTestDocument();
 Node found = operationsHelper.find(document, "/element1");
 assertNotNull(found);
 assertEquals("element1", found.getNodeName());
 }

 @Test
 public void testFindComplexPath() {
 Document document = createTestDocument();
 Node found = operationsHelper.find(document, "/element1/element2/element3");
 assertNotNull(found);
 assertEquals("element3", found.getNodeName());
 }

 @Test
 public void testFindNonExistentPath() {
 Document document = createTestDocument();
 assertNull(operationsHelper.find(document, "/nonexistent/element1"));
 }

 @Test
 public void testNodeWithNameNull() {
 List<Node> nodes = new ArrayList<>();
 assertNull(operationsHelper.nodeWithName(nodes, null));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 assertNull(operationsHelper.nodeWithName(nodes, "element1"));
 }

 @Test
 public void testNodeWithNameExistingNode() {
 Document document = createTestDocument();
 List<Node> nodes = operationsHelper.children(document.getDocumentElement());
 Node node = operationsHelper.nodeWithName(nodes, "element2");
 assertNotNull(node);
 assertEquals("element2", node.getNodeName());
 }

 @Test
 public void testNodeWithNameNonExistentNode() {
 Document document = createTestDocument();
 List<Node> nodes = operationsHelper.children(document.getDocumentElement());
 assertNull(operationsHelper.nodeWithName(nodes, "nonexistent"));
 }

 @Test
 public void testSublistWithNameNull() {
 List<Node> nodes = new ArrayList<>();
 assertNotNull(operationsHelper.sublistWithName(nodes, "element1"));
 assertTrue(operationsHelper.sublistWithName(nodes, "element1").isEmpty());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "element1");
 assertNotNull(sublist);
 assertTrue(sublist.isEmpty());
 }

 @Test
 public void testSublistWithNameExistingNodes() {
 Document document = createTestDocument();
 List<Node> nodes = operationsHelper.children(document.getDocumentElement());
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "element2");
 assertEquals(1, sublist.size());
 assertEquals("element2", sublist.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameNonExistentNodes() {
 Document document = createTestDocument();
 List<Node> nodes = operationsHelper.children(document.getDocumentElement());
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "nonexistent");
 assertTrue(sublist.isEmpty());
 }

 private Document createTestDocument() {
 Element root = new Element("root");
 root.setAttribute("id", "root-id");
 Element element1 = new Element("element1");
 element1.setAttribute("id", "element1-id");
 root.appendChild(element1);
 Element element2 = new Element("element2");
 element2.setAttribute("id", "element2-id");
 element1.appendChild(element2);
 Element element3 = new Element("element3");
 element3.setAttribute("id", "element3-id");
 element2.appendChild(element3);
 DocumentBuilder documentBuilder = document.implementation().newDocumentBuilder();
 return documentBuilder.newDocument();
 }

 private Node createTestNode() {
 Element element1 = new Element("element1");
 Element element2 = new Element("element2");
 Element element3 = new Element("element3");
 element1.appendChild(element2);
 element2.appendChild(element3);
 DocumentBuilder documentBuilder = document.implementation().newDocumentBuilder();
 Document document = documentBuilder.newDocument();
 document.appendChild(element1);
 return document;
 }

 private Document createEmptyDocument() {
 DocumentBuilder documentBuilder = document.implementation().newDocumentBuilder();
 return documentBuilder.newDocument();
 }
}