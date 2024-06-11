package net.sf.xisemele.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestNode();
 List<Node> children = helper.children(node);
 assertEquals(3, children.size());
 Assert.assertEquals("tag1", children.get(0).getNodeName());
 Assert.assertEquals("tag2", children.get(1).getNodeName());
 Assert.assertEquals("tag3", children.get(2).getNodeName());
 }

 @Test
 public void testChildrenEmpty() {
 Node node = createTestNodeWithNoChildren();
 List<Node> children = helper.children(node);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 String path = "/tag1/tag2/tag3";
 Node expected = document.getElementsByTagName("tag3").item(0);
 Node result = helper.find(document, path);
 assertEquals(expected, result);
 }

 @Test
 public void testFindNullDocument() {
 Node result = helper.find(null, "/tag1/tag2/tag3");
 assertNull(result);
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestDocument();
 Node result = helper.find(document, null);
 assertNull(result);
 }

 @Test
 public void testFindInvalidPath() {
 Document document = createTestDocument();
 String path = "/tag1/tag2/tag4";
 Node result = helper.find(document, path);
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createTestNodes();
 Node expected = nodes.get(1);
 Node result = helper.nodeWithName(nodes, "tag2");
 assertEquals(expected, result);
 }

 @Test
 public void testNodeWithNameNullList() {
 List<Node> nodes = null;
 Node result = helper.nodeWithName(nodes, "tag2");
 assertNull(result);
 }

 @Test
 public void testNodeWithNameNullName() {
 List<Node> nodes = createTestNodes();
 String name = null;
 Node result = helper.nodeWithName(nodes, name);
 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createTestNodes();
 String name = "tag2";
 List<Node> expected = Arrays.asList(nodes.get(1), nodes.get(2));
 List<Node> result = helper.sublistWithName(nodes, name);
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = Collections.emptyList();
 String name = "tag2";
 List<Node> result = helper.sublistWithName(nodes, name);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithNameNullList() {
 List<Node> nodes = null;
 String name = "tag2";
 List<Node> result = helper.sublistWithName(nodes, name);
 assertNull(result);
 }

 @Test
 public void testSublistWithNameNullName() {
 List<Node> nodes = createTestNodes();
 String name = null;
 List<Node> result = helper.sublistWithName(nodes, name);
 assertTrue(result.isEmpty());
 }

 private Node createTestNode() {
 Document document = createTestDocument();
 Element node = document.createElement("tag1");
 node.setAttribute("data", "value1");
 document.getDocumentElement().appendChild(node);
 Node child1 = document.createElement("tag2");
 Node child2 = document.createElement("tag3");
 node.appendChild(child1);
 node.appendChild(child2);
 return node;
 }

 private Node createTestNodeWithNoChildren() {
 Document document = createTestDocument();
 Element node = document.createElement("tag1");
 document.getDocumentElement().appendChild(node);
 return node;
 }

 private Document createTestDocument() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = null;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 Document document = null;
 try {
 document = builder.newDocument();
 } catch (DocumentException e) {
 e.printStackTrace();
 }
 return document;
 }

 private List<Node> createTestNodes() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Element tag1 = document.createElement("tag1");
 tag1.setAttribute("data", "value1");
 root.appendChild(tag1);
 Element tag2 = document.createElement("tag2");
 tag2.setAttribute("data", "value2");
 tag1.appendChild(tag2);
 Element tag3 = document.createElement("tag3");
 tag3.setAttribute("data", "value3");
 tag2.appendChild(tag3);
 List<Node> nodes = new ArrayList<Node>();
 nodes.add(root);
 nodes.add(tag1);
 nodes.add(tag2);
 nodes.add(tag3);
 return nodes;
 }

}