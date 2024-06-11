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

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> expectedList = Arrays.asList(new Node[] { testNode });
 List<Node> resultList = helper.children(testNode);
 assertEquals(expectedList, resultList);
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenNullNode() {
 List<Node> resultList = helper.children(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testFind() {
 Document testDocument = createTestDocument();
 String path = "/test1/test2/test3";
 Node expectedNode = testDocument.getElementsByTagName("test3").item(0);
 Node resultNode = helper.find(testDocument, path);
 assertEquals(expectedNode, resultNode);
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullDocument() {
 Node resultNode = helper.find(null, "/test1");
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidPath() {
 Document testDocument = createTestDocument();
 Node resultNode = helper.find(testDocument, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = null;
 Node resultNode = helper.nodeWithName(nodes, "test1");
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNameNullName() {
 List<Node> nodes = Arrays.asList(new Node[] { createTestNode() });
 String name = null;
 Node resultNode = helper.nodeWithName(nodes, name);
 fail("Expected NullPointerException");
 }

 @Test
 public void testNodeWithNameExistingNode() {
 Node testNode = createTestNode();
 List<Node> nodes = Arrays.asList(new Node[] { testNode, createTestNode() });
 Node expectedNode = testNode;
 Node resultNode = helper.nodeWithName(nodes, "test1");
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testNodeWithNameNonExistingNode() {
 Node testNode1 = createTestNode();
 Node testNode2 = createTestNode();
 List<Node> nodes = Arrays.asList(new Node[] { testNode1, testNode2 });
 Node resultNode = helper.nodeWithName(nodes, "test3");
 assertNull(resultNode);
 }

 @Test
 public void testSublistWithNameEmptyNodesList() {
 List<Node> nodes = new ArrayList<>();
 String name = "test1";
 List<Node> expectedList = new ArrayList<>();
 List<Node> resultList = helper.sublistWithName(nodes, name);
 assertEquals(expectedList, resultList);
 }

 @Test
 public void testSublistWithNameExistingNodes() {
 Node testNode1 = createTestNode();
 Node testNode2 = createTestNode();
 List<Node> nodes = Arrays.asList(new Node[] { testNode1, testNode2 });
 String name = "test1";
 Node expectedNode = testNode1;
 List<Node> resultList = helper.sublistWithName(nodes, name);
 assertEquals(Arrays.asList(expectedNode), resultList);
 }

 @Test
 public void testSublistWithNameNonExistingNodes() {
 Node testNode1 = createTestNode();
 Node testNode2 = createTestNode();
 List<Node> nodes = Arrays.asList(new Node[] { testNode1, testNode2 });
 String name = "test3";
 List<Node> resultList = helper.sublistWithName(nodes, name);
 assertEquals(new ArrayList<>(), resultList);
 }

 private Document createTestDocument() {
 Element testElement = new Element("testDocument");
 Document document = new org.w3c.dom.Document();
 document.appendChild(testElement);
 Element testElement1 = new Element("test1");
 testElement.appendChild(testElement1);
 Element testElement2 = new Element("test2");
 testElement1.appendChild(testElement2);
 Element testElement3 = new Element("test3");
 testElement2.appendChild(testElement3);
 return document;
 }

 private Node createTestNode() {
 Element testElement = new Element("testNode");
 Document document = new org.w3c.dom.Document();
 document.appendChild(testElement);
 return testElement;
 }
}