package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;

public class OperationsHelperImplTest {

 OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> children = operationsHelper.children(testNode);
 assertEquals(2, children.size());
 }

 @Test
 public void testChildrenEmpty() {
 Node testNode = createTestNodeWithEmptyChildren();
 List<Node> children = operationsHelper.children(testNode);
 assertEquals(0, children.size());
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 Node foundNode = operationsHelper.find(document, "/test/testChild1/testChild2");
 assertNotNull(foundNode);
 assertEquals("testChild2", foundNode.getNodeName());
 }

 @Test
 public void testFindNull() {
 Document document = createTestDocument();
 Node foundNode = operationsHelper.find(document, "/testNull");
 assertNull(foundNode);
 }

 @Test
 public void testFindRoot() {
 Document document = createTestDocument();
 Node foundNode = operationsHelper.find(document, "/");
 assertNotNull(foundNode);
 assertEquals("test", foundNode.getNodeName());
 }

 @Test
 public void testNodeWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> nodes = operationsHelper.children(testNode);
 Node nodeWithName = operationsHelper.nodeWithName(nodes, "testChild1");
 assertNotNull(nodeWithName);
 assertEquals("testChild1", nodeWithName.getNodeName());
 }

 @Test
 public void testNodeWithNameNull() {
 Node testNode = createTestNodeWithChildren();
 Node nodeWithName = operationsHelper.nodeWithName(null, "testChild1");
 assertNull(nodeWithName);
 }

 @Test
 public void testSublistWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> nodes = operationsHelper.children(testNode);
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "testChild1");
 assertEquals(1, sublist.size());
 assertNotNull(sublist.get(0));
 assertEquals("testChild1", sublist.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameEmpty() {
 Node testNode = createTestNodeWithChildren();
 List<Node> nodes = operationsHelper.children(testNode);
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "testChild3");
 assertEquals(0, sublist.size());
 }

 private Node createTestNodeWithEmptyChildren() {
 Document document = createTestDocument();
 Element root = document.getDocumentElement();
 return root;
 }

 private Node createTestNodeWithChildren() {
 Document document = createTestDocument();
 Element root = document.getDocumentElement();
 Element testChild1 = document.createElement("testChild1");
 Element testChild2 = document.createElement("testChild2");
 root.appendChild(testChild1);
 testChild1.appendChild(testChild2);
 return root;
 }

 private Node createTestNode() {
 Document document = createTestDocument();
 Element root = document.getDocumentElement();
 Element testChild1 = document.createElement("testChild1");
 root.appendChild(testChild1);
 return testChild1;
 }

 private Document createTestDocument() {
 try {
 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
 Document document = documentBuilder.newDocument();
 Element root = document.createElement("test");
 document.appendChild(root);
 return document;
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 return null;
 }
}