package net.sf.xisemele.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> result = helper.children(testNode);
 List<Node> expected = Arrays.asList(testNode.getElementsByTagName("child1"), testNode.getElementsByTagName("child2")[0]);
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i).getNodeName(), result.get(i).getNodeName());
 }
 }

 @Test
 public void testChildrenWithEmptyNodeList() {
 Node testNode = createTestNodeWithEmptyNodeList();
 List<Node> result = helper.children(testNode);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testChildrenWithCommentsAndTextNodes() {
 Node testNode = createTestNodeWithCommentsAndTextNodes();
 List<Node> result = helper.children(testNode);
 List<Node> expected = Arrays.asList(testNode.getElementsByTagName("child1"), testNode.getElementsByTagName("child2")[0], testNode.getElementsByTagName("comment1")[0], testNode.getElementsByTagName("textNode1")[0]);
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i).getNodeName(), result.get(i).getNodeName());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenWithNullNode() {
 List<Node> result = helper.children(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testFind() {
 Document testDocument = createTestDocument();
 List<Node> result = helper.findAllNodesWithName(testDocument, "child1");
 Node testNode = result.get(0);
 Node expected = testDocument.getDocumentElement().getElementsByTagName("child1")[0];
 assertEquals(expected.getNodeName(), testNode.getNodeName());
 Node found = helper.find(testDocument, "child1/child2/child3");
 assertEquals(expected, found);
 }

 @Test
 public void testFindWithEmptyDocument() {
 Document testDocument = createEmptyDocument();
 Node found = helper.find(testDocument, "child1/child2/child3");
 assertNull(found);
 }

 @Test
 public void testFindWithInvalidPath() {
 Document testDocument = createTestDocument();
 Node found = helper.find(testDocument, "invalidPath");
 assertNull(found);
 }

 @Test
 public void testNodeWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> children = helper.children(testNode);
 Node expected = children.get(0);
 Node found = helper.nodeWithName(children, "child1");
 assertEquals(expected, found);
 }

 @Test
 public void testNodeWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<>();
 Node found = helper.nodeWithName(nodes, "child1");
 assertNull(found);
 }

 @Test
 public void testSublistWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> children = helper.children(testNode);
 List<Node> expected = Arrays.asList(children.get(0), children.get(1));
 List<Node> result = helper.sublistWithName(children, "child1");
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> result = helper.sublistWithName(nodes, "child1");
 assertTrue(result.isEmpty());
 }

 private Node createTestNode() {
 Element element = document.createElement("root");
 element.setAttribute("id", "testNode");
 Document testDocument = document.createDocument();
 testDocument.appendChild(element);
 Node testNode = testDocument.getDocumentElement();
 testNode.appendChild(document.createComment("comment1"));
 element.appendChild(document.createTextNode("textNode1"));
 element.appendChild(document.createElement("child1"));
 element.appendChild(document.createElement("child2"));
 Node child2FirstChild = element.getElementsByTagName("child2")[0];
 child2FirstChild.appendChild(document.createTextNode("child2TextNode"));
 return testNode;
 }

 private Node createTestNodeWithEmptyNodeList() {
 Element element = document.createElement("root");
 Document testDocument = document.createDocument();
 testDocument.appendChild(element);
 return testDocument.getDocumentElement();
 }

 private Node createTestNodeWithCommentsAndTextNodes() {
 Element element = document.createElement("root");
 element.setAttribute("id", "testNode");
 Document testDocument = document.createDocument();
 testDocument.appendChild(element);
 Node testNode = testDocument.getDocumentElement();
 testNode.appendChild(document.createComment("comment1"));
 testNode.appendChild(document.createComment("comment2"));
 testNode.appendChild(document.createTextNode("textNode1"));
 testNode.appendChild(document.createElement("child1"));
 testNode.appendChild(document.createElement("child2"));
 Node child2FirstChild = element.getElementsByTagName("child2")[0];
 child2FirstChild.appendChild(document.createTextNode("child2TextNode"));
 return testNode;
 }

 private Node createTestNodeWithChildren() {
 Element element = document.createElement("root");
 element.setAttribute("id", "testNode");
 Document testDocument = document.createDocument();
 testDocument.appendChild(element);
 Node testNode = testDocument.getDocumentElement();
 testNode.appendChild(document.createComment("comment1"));
 testNode.appendChild(document.createElement("child1"));
 testNode.appendChild(document.createElement("child2"));
 Node child2FirstChild = element.getElementsByTagName("child2")[0];
 child2FirstChild.appendChild(document.createElement("child3"));
 child2FirstChild.appendChild(document.createTextNode("child2TextNode"));
 return testNode;
 }

 private Document document = document.newDocument();
}