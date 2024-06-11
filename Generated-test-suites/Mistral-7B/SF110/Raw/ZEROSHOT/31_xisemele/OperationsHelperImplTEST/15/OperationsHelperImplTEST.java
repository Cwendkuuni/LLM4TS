package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node xmlNode = createTestXML();
 List<Node> children = operationsHelper.children(xmlNode);
 assertEquals(5, children.size());
 }

 @Test
 public void testChildrenNullNode() {
 List<Node> children = operationsHelper.children(null);
 assertNull(children);
 }

 @Test
 public void testChildrenEmptyNodeList() {
 Node emptyNode = createEmptyElement();
 List<Node> children = operationsHelper.children(emptyNode);
 assertEquals(0, children.size());
 }

 @Test
 public void testFind() {
 Document document = createTestXMLDocument();
 Node foundNode = operationsHelper.find(document, "Root/Element1/Element2");
 assertNotNull(foundNode);
 assertEquals("Element2", foundNode.getNodeName());
 }

 @Test
 public void testFindNullDocument() {
 Document document = null;
 Node foundNode = operationsHelper.find(document, "Root/Element1/Element2");
 assertNull(foundNode);
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestXMLDocument();
 Node foundNode = operationsHelper.find(document, null);
 assertNull(foundNode);
 }

 @Test
 public void testFindWrongElement() {
 Document document = createTestXMLDocument();
 Node foundNode = operationsHelper.find(document, "Root/WrongElement");
 assertNull(foundNode);
 }

 @Test
 public void testFindRootElement() {
 Document document = createTestXMLDocument();
 Node rootElement = document.getDocumentElement();
 Node foundNode = operationsHelper.find(document, "Root");
 assertEquals(rootElement, foundNode);
 }

 @Test
 public void testNodeWithName() {
 Node xmlNode = createTestXML();
 List<Node> children = operationsHelper.children(xmlNode);
 Node nameNode = operationsHelper.nodeWithName(children, "Element3");
 assertEquals("Element3", nameNode.getNodeName());
 assertNull(operationsHelper.nodeWithName(children, "Element4"));
 }

 @Test
 public void testNodeWithNameNullList() {
 List<Node> nodes = null;
 Node foundNode = operationsHelper.nodeWithName(nodes, "Element3");
 assertNull(foundNode);
 }

 @Test
 public void testSublistWithName() {
 Node xmlNode = createTestXML();
 List<Node> children = operationsHelper.children(xmlNode);
 List<Node> sublist = operationsHelper.sublistWithName(children, "Element3");
 assertEquals(1, sublist.size());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "Element3");
 assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithNameNullList() {
 List<Node> nodes = null;
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "Element3");
 assertNull(sublist);
 }

 private Node createTestXML() {
 Document document = createTestXMLDocument();
 return document.getDocumentElement();
 }

 private Document createTestXMLDocument() {
 Element root = new Element("Root");
 root.setAttribute("id", "1");
 Element element1 = new Element("Element1");
 root.appendChild(element1);
 element1.setAttribute("id", "2");
 Element element2 = new Element("Element2");
 element1.appendChild(element2);
 element2.setAttribute("id", "3");
 Element element3 = new Element("Element3");
 element2.appendChild(element3);
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 Document document = factory.newDocumentBuilder().newDocument();
 document.appendChild(root);
 return document;
 }

 private Element createEmptyElement() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 Document document = factory.newDocumentBuilder().newDocument();
 Element element = document.createElement("Element");
 document.appendChild(element);
 return element;
 }

}