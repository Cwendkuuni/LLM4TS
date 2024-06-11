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
 List<Node> expected = Arrays.asList(createTestNode(), createTestNode2(), createTestNode3());
 List<Node> result = helper.children(testNode);
 assertEquals(expected, result);
 }

 @Test
 public void testChildrenWithEmptyNodeList() {
 Node testNode = createDocument();
 List<Node> result = helper.children(testNode);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testChildrenWithNullNode() {
 List<Node> result = helper.children(null);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 List<String> path = Arrays.asList("html", "body", "p", "textNode1");
 Node expected = document.createTextNode("Test text1");
 Node result = helper.find(document, "/html/body/p/textNode1");
 assertEquals(expected, result);
 }

 @Test
 public void testFindWithEmptyDocument() {
 Document document = createEmptyDocument();
 Node result = helper.find(document, "/html/body/p/textNode1");
 assertNull(result);
 }

 @Test
 public void testFindWithNullDocument() {
 Node result = helper.find(null, "/html/body/p/textNode1");
 assertNull(result);
 }

 @Test
 public void testFindWithInvalidPath() {
 Document document = createTestDocument();
 Node result = helper.find(document, "/html/body/p/textNodeX");
 assertNull(result);
 }

 @Test
 public void testFindWithInvalidFirstNodeName() {
 Document document = createTestDocument();
 Node result = helper.find(document, "/html/invalidNode/p/textNode1");
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> nodes = helper.children(testNode);
 Node expected = nodes.get(1);
 Node result = helper.nodeWithName(nodes, "p");
 assertEquals(expected, result);
 }

 @Test
 public void testNodeWithNameWithNullNodes() {
 List<Node> nodes = null;
 Node result = helper.nodeWithName(nodes, "p");
 assertNull(result);
 }

 @Test
 public void testNodeWithNameWithEmptyNodes() {
 List<Node> nodes = Collections.emptyList();
 Node result = helper.nodeWithName(nodes, "p");
 assertNull(result);
 }

 @Test
 public void testNodeWithNameWithNullNodeName() {
 List<Node> nodes = Arrays.asList(createTestNode(), createTestNode2());
 Node result = helper.nodeWithName(nodes, null);
 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 Node testNode = createTestNodeWithChildren();
 List<Node> nodes = helper.children(testNode);
 List<Node> expected = Arrays.asList(createTestNode3());
 List<Node> result = helper.sublistWithName(nodes, "p");
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameWithEmptyNodes() {
 List<Node> nodes = Collections.emptyList();
 List<Node> result = helper.sublistWithName(nodes, "p");
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testSublistWithNameWithNullNodes() {
 List<Node> nodes = null;
 List<Node> result = helper.sublistWithName(nodes, "p");
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testSublistWithNameWithNullNodeName() {
 List<Node> nodes = Arrays.asList(createTestNode(), createTestNode2());
 List<Node> result = helper.sublistWithName(nodes, null);
 assertEquals(Collections.emptyList(), result);
 }

 private Node createTestNode() {
 Element element = document.createElement("p");
 element.setTextContent("Test text");
 return document.createElement("textNode1");
 }

 private Node createTestNode2() {
 Element element = document.createElement("div");
 element.setTextContent("Test div");
 return document.createElement("textNode2");
 }

 private Node createTestNode3() {
 Element element = document.createElement("p");
 element.setTextContent("Test text3");
 return document.createElement("textNode3");
 }

 private Document createTestDocument() {
 Document document = createDocument();
 Element html = document.createElement("html");
 Element body = document.createElement("body");
 html.appendChild(body);
 body.appendChild(createTestNode());
 body.appendChild(createTestNode2());
 body.appendChild(createTestNode3());
 document.appendChild(html);
 return document;
 }

 private Document createEmptyDocument() {
 Document document = createDocument();
 return document;
 }

 private Document createDocument() {
 Document document = document.createDocument("", "", null);
 return document;
 }
}