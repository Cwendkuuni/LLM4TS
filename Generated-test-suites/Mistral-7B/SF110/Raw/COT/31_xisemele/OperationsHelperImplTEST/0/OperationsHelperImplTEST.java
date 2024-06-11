package net.sf.xisemele.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> expectedChildren = new ArrayList<Node>() {{
 add(createTestNode());
 add(createTestNode());
 add(createTestNode());
 }};
 List<Node> actualChildren = operationsHelper.children(testNode);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testChildren_EmptyNodeList() {
 Node testNode = createTestNode("");
 List<Node> actualChildren = operationsHelper.children(testNode);
 assertTrue(actualChildren.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChildren_NullNode() {
 operationsHelper.children(null);
 }

 @Test
 public void testFind() {
 Document testDocument = createTestDocument();
 String testPath = "/testNode1/testNode2/testNode3";
 Node testNode = createTestNode("testNode3");
 testDocument.getDocumentElement().appendChild(testNode);
 Node expectedNode = testNode;
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testFind_NullDocument() {
 Document testDocument = null;
 String testPath = "/testNode1/testNode2/testNode3";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test
 public void testFind_EmptyDocument() {
 Document testDocument = createTestDocument();
 String testPath = "/testNode1/testNode2/testNode3";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test
 public void testFind_PathDoesNotExist() {
 Document testDocument = createTestDocument();
 String testPath = "/testNode1/testNode4/testNode3";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test
 public void testFind_PathExistsButLastNodeNameDoesNotMatch() {
 Document testDocument = createTestDocument();
 String testPath = "/testNode1/testNode2/testNode2";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFind_NullPath() {
 operationsHelper.find(null, " testPath ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFind_EmptyPath() {
 operationsHelper.find(createTestDocument(), "");
 }

 @Test
 public void testNodeWithName() {
 List<Node> testNodes = new ArrayList<Node>() {{
 add(createTestNode("testNode1"));
 add(createTestNode("testNode2"));
 add(createTestNode("testNode3"));
 }};
 String testName = "testNode2";
 Node expectedNode = testNodes.get(1);
 Node actualNode = operationsHelper.nodeWithName(testNodes, testName);
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithName_NullNodeList() {
 List<Node> testNodes = null;
 String testName = "testName";
 Node actualNode = operationsHelper.nodeWithName(testNodes, testName);
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_NullName() {
 List<Node> testNodes = createTestListNode();
 String testName = null;
 Node actualNode = operationsHelper.nodeWithName(testNodes, testName);
 assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 List<Node> testNodes = new ArrayList<Node>() {{
 add(createTestNode("testNode1"));
 add(createTestNode("testNode2"));
 add(createTestNode("testNode3"));
 }};
 String testName = "testNode";
 List<Node> expectedSublist = new ArrayList<Node>() {{
 add(createTestNode("testNode2"));
 add(createTestNode("testNode3"));
 }};
 List<Node> actualSublist = operationsHelper.sublistWithName(testNodes, testName);
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_EmptyNodeList() {
 List<Node> testNodes = new ArrayList<Node>();
 String testName = "testName";
 List<Node> actualSublist = operationsHelper.sublistWithName(testNodes, testName);
 assertTrue(actualSublist.isEmpty());
 }

 @Test
 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithName_NullNodeList() {
 List<Node> testNodes = null;
 String testName = "testName";
 operationsHelper.sublistWithName(testNodes, testName);
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> testNodes = createTestListNode();
 String testName = null;
 List<Node> actualSublist = operationsHelper.sublistWithName(testNodes, testName);
 assertNull(actualSublist);
 }

 private Document createTestDocument() {
 Document document = createTestDocument("");
 Element root = document.getDocumentElement();
 root.setNodeName("testDocument");
 return document;
 }

 private Node createTestNode(String nodeName) {
 Node node = document.createElement(nodeName);
 node.setNodeName(nodeName);
 return node;
 }

 private Node createTestNode() {
 return createTestNode("testNode");
 }

 private List<Node> createTestListNode() {
 return new ArrayList<Node>() {{
 add(createTestNode());
 }};
 }
}