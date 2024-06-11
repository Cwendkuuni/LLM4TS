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
 Node node = createNode("root", null, null);
 Node child1 = createNode("child1", null, null);
 Node child2 = createNode("child2", null, null);
 node.appendChild(child1);
 node.appendChild(child2);

 List<Node> result = operationsHelper.children(node);
 List<Node> expected = Arrays.asList(child1, child2);
 assertEquals(expected, result);
 }

 @Test
 public void testChildrenEmpty() {
 Node node = createNode("root", null, null);
 List<Node> result = operationsHelper.children(node);
 List<Node> expected = new ArrayList<>();
 assertEquals(expected, result);
 }

 @Test
 public void testFind() {
 Document document = createDocument();
 Node root = document.getDocumentElement();
 Element child1 = createElement("child1");
 Element child2 = createElement("child2");
 Element grandChild = createElement("grandChild");
 root.appendChild(child1);
 child1.appendChild(grandChild);
 root.appendChild(child2);

 Node result = operationsHelper.find(document, "root/child1/grandChild");
 Node expected = grandChild;
 assertEquals(expected, result);
 }

 @Test
 public void testFindNull() {
 Document document = createDocument();
 Node result = operationsHelper.find(document, null);
 assertNull(result);
 }

 @Test
 public void testFindNotFound() {
 Document document = createDocument();
 Node result = operationsHelper.find(document, "root/nonexistent");
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 Node node = createNode("root", Arrays.asList(createNode("child1"), createNode("child2")), null);
 Node result = operationsHelper.nodeWithName(operationsHelper.children(node), "child1");
 Node expected = createNode("child1");
 assertEquals(expected, result);
 }

 @Test
 public void testNodeWithNameNull() {
 Node node = createNode("root", Arrays.asList(createNode("child1"), createNode("child2")), null);
 Node result = operationsHelper.nodeWithName(operationsHelper.children(node), null);
 assertNull(result);
 }

 @Test
 public void testNodeWithNameNotFound() {
 Node node = createNode("root", Arrays.asList(createNode("child1"), createNode("child2")), null);
 Node result = operationsHelper.nodeWithName(operationsHelper.children(node), "nonexistent");
 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 Node node = createNode("root", Arrays.asList(createNode("child1"), createNode("child2"), createNode("child3")), null);
 List<Node> result = operationsHelper.sublistWithName(operationsHelper.children(node), "child1");
 List<Node> expected = Arrays.asList(createNode("child1"));
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameEmpty() {
 Node node = createNode("root", Arrays.asList(createNode("child1")), null);
 List<Node> result = operationsHelper.sublistWithName(operationsHelper.children(node), "child2");
 List<Node> expected = new ArrayList<>();
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameNotFound() {
 Node node = createNode("root", Arrays.asList(createNode("child1"), createNode("child2"), createNode("child3")), null);
 List<Node> result = operationsHelper.sublistWithName(operationsHelper.children(node), "nonexistent");
 List<Node> expected = new ArrayList<>();
 assertEquals(expected, result);
 }

 private Document createDocument() {
 Document document = null;
 try {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 return document;
 }

 private Node createNode(String name, List<Node> children, Node parent) {
 Element element = document.createElement(name);
 if (children != null) {
 for (Node child : children) {
 element.appendChild(child);
 }
 }
 if (parent != null) {
 parent.appendChild(element);
 }
 return element;
 }

}