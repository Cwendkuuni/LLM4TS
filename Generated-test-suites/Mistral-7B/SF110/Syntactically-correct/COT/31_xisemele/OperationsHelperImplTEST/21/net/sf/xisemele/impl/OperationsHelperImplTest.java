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
 Node testNode = createTestNode();
 List<Node> children = operationsHelper.children(testNode);
 assertEquals(3, children.size());
 assertTrue(children.contains(testNode.getAttributes().getNamedItem("id")));
 assertTrue(children.contains(testNode.getFirstChild()));
 assertTrue(children.contains(testNode.getLastChild()));
 }

 @Test
 public void testFindNullDocument() {
 assertNull(operationsHelper.find(null, ""));
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestDocument();
 assertNull(operationsHelper.find(document, null));
 }

 @Test
 public void testFindEmptyDocument() {
 Document document = createTestDocument();
 assertNull(operationsHelper.find(document, ""));
 }

 @Test
 public void testFindRoot() {
 Document document = createTestDocument();
 Node root = document.getDocumentElement();
 Node found = operationsHelper.find(document, "/" + root.getNodeName());
 assertEquals(root, found);
 }

 @Test
 public void testFindChild() {
 Document document = createTestDocument();
 Node child = document.getElementsByTagName("child").item(0);
 Node found = operationsHelper.find(document, "/child[1]");
 assertEquals(child, found);
 }

 @Test
 public void testFindNonExistingNode() {
 Document document = createTestDocument();
 assertNull(operationsHelper.find(document, "/nonExistingNode"));
 }

 @Test
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = new ArrayList<>();
 assertNull(operationsHelper.nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<>();
 assertFalse(operationsHelper.nodeWithName(nodes, "name").isNull());
 }

 @Test
 public void testNodeWithNameSingleNode() {
 Node node = createTestNode();
 List<Node> nodes = new ArrayList<Node>() {{ add(node); }};
 Node found = operationsHelper.nodeWithName(nodes, node.getNodeName());
 assertEquals(node, found);
 }

 @Test
 public void testNodeWithNameMultipleNodes() {
 Node node1 = createTestNode("node1");
 Node node2 = createTestNode("node1");
 List<Node> nodes = new ArrayList<Node>() {{ add(node1); add(node2); }};
 Node found = operationsHelper.nodeWithName(nodes, "node1");
 assertEquals(node1, found);
 }

 @Test
 public void testSublistWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<>();
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "name");
 assertTrue(sublist.isEmpty());
 }

 @Test
 public void testSublistWithNameSingleNode() {
 Node node = createTestNode();
 List<Node> nodes = new ArrayList<Node>() {{ add(node); }};
 List<Node> sublist = operationsHelper.sublistWithName(nodes, node.getNodeName());
 assertEquals(1, sublist.size());
 assertEquals(node, sublist.get(0));
 }

 @Test
 public void testSublistWithNameMultipleNodes() {
 Node node1 = createTestNode("node1");
 Node node2 = createTestNode("node1");
 List<Node> nodes = new ArrayList<Node>() {{ add(node1); add(node2); }};
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "node1");
 assertEquals(2, sublist.size());
 assertTrue(sublist.containsAll(Arrays.asList(node1, node2)));
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenNullNode() {
 operationsHelper.children(null);
 }

 private Document createTestDocument() {
 Document document = createTestDocument("test.xml");
 return document;
 }

 private Node createTestNode() {
 return createTestNode("testNode");
 }

 private Node createTestNode(String nodeName) {
 Element element = document.createElement(nodeName);
 element.setAttribute("id", "testId");
 document.appendChild(element);
 return element;
 }

}