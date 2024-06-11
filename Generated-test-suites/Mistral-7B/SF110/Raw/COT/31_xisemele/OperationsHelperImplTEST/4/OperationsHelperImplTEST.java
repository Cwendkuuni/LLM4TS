package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createNode();
 List<Node> expectedChildren = Arrays.asList(node1, node2, node3);
 List<Node> actualChildren = operationsHelper.children(node);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testFindNullDocument() {
 assertNull(operationsHelper.find(null, ""));
 }

 @Test
 public void testFindNullPath() {
 Document document = createDocument();
 assertNull(operationsHelper.find(document, ""));
 }

 @Test
 public void testFindRootNode() {
 Document document = createDocument();
 assertEquals(document.getDocumentElement(), operationsHelper.find(document, ""));
 }

 @Test
 public void testFindNonExistentNode() {
 Document document = createDocument();
 Node documentElement = document.getDocumentElement();
 assertNull(operationsHelper.find(document, documentElement.getNodeName() + "/nonexistent"));
 }

 @Test
 public void testFind() {
 Document document = createDocument();
 Node rootElement = document.getDocumentElement();
 Node element1 = createElement("element1");
 Node element2 = createElement("element2");
 Node element3 = createElement("element3");
 rootElement.appendChild(element1);
 element1.appendChild(element2);
 element1.appendChild(element3);
 Node node = operationsHelper.find(document, "element1/element2");
 assertEquals(element2, node);
 }

 @Test
 public void testNodeWithNameNullNodes() {
 assertNull(operationsHelper.nodeWithName(null, ""));
 }

 @Test
 public void testNodeWithNameNullName() {
 List<Node> nodes = createNodes();
 assertNull(operationsHelper.nodeWithName(nodes, ""));
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createNodes();
 Node expectedNode = node1;
 Node actualNode = operationsHelper.nodeWithName(nodes, node1.getNodeName());
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testSublistWithNameNullNodes() {
 assertEmpty(operationsHelper.sublistWithName(null, ""));
 }

 @Test
 public void testSublistWithNameNullName() {
 List<Node> nodes = createNodes();
 assertEmpty(operationsHelper.sublistWithName(nodes, ""));
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createNodes();
 List<Node> expectedSublist = Arrays.asList(node2, node3);
 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, node1.getNodeName());
 assertEquals(expectedSublist, actualSublist);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameNullNameThrowsException() {
 List<Node> nodes = createNodes();
 operationsHelper.sublistWithName(nodes, null);
 }

 @Test
 private Node createNode() {
 Document document = createDocument();
 Element element = createElement("node");
 document.appendChild(element);
 return element;
 }

 @Test
 private Document createDocument() {
 Document document = new DocumentImpl();
 return document;
 }

 @Test
 private Element createElement(String tagName) {
 Element element = document.createElement(tagName);
 return element;
 }

 @Test
 private List<Node> createNodes() {
 Document document = createDocument();
 Element rootElement = document.createElement("root");
 document.appendChild(rootElement);
 Node element1 = document.createElement("element1");
 Node element2 = document.createElement("element2");
 Node element3 = document.createElement("element3");
 rootElement.appendChild(element1);
 rootElement.appendChild(element2);
 rootElement.appendChild(element3);
 return Arrays.asList(element1, element2, element3);
 }

}