package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

 private OperationsHelperImpl helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestNode();
 List<Node> expected = Arrays.asList(createTestNode1(), createTestNode2());
 List<Node> actual = helper.children(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testChildrenNullNode() {
 List<Node> actual = helper.children(null);
 assertNull(actual);
 }

 @Test
 public void testFindNullDocument() {
 assertNull(helper.find(null, ""));
 }

 @Test
 public void testFindEmptyDocument() {
 Document document = createTestDocument();
 assertNull(helper.find(document, ""));
 }

 @Test
 public void testFindSimplePath() {
 Document document = createTestDocument();
 Node root = document.getDocumentElement();
 Node expected = root.getElementsByTagName("test").item(0);
 Node actual = helper.find(document, "root/test");
 assertEquals(expected, actual);
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestDocument();
 assertNull(helper.find(document, null));
 }

 @Test
 public void testFindEmptyPath() {
 Document document = createTestDocument();
 assertNull(helper.find(document, ""));
 }

 @Test
 public void testFindNonExistentPath() {
 Document document = createTestDocument();
 assertNull(helper.find(document, "root/nonExistent"));
 }

 @Test
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = null;
 Node actual = helper.nodeWithName(nodes, "test");
 assertNull(actual);
 }

 @Test
 public void testNodeWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<>();
 Node actual = helper.nodeWithName(nodes, "test");
 assertNull(actual);
 }

 @Test
 public void testNodeWithNameSingleNode() {
 List<Node> nodes = Arrays.asList(createTestNode1());
 Node expected = createTestNode1();
 Node actual = helper.nodeWithName(nodes, "test");
 assertEquals(expected, actual);
 }

 @Test
 public void testNodeWithNameMultipleNodes() {
 List<Node> nodes = Arrays.asList(createTestNode1(), createTestNode2());
 Node expected = createTestNode1();
 Node actual = helper.nodeWithName(nodes, "test1");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeWithNameInvalidNodeType() {
 List<Node> nodes = Arrays.asList(createTestDocument());
 helper.nodeWithName(nodes, "test");
 }

 @Test
 public void testSublistWithNameNullNodes() {
 List<Node> nodes = null;
 List<Node> actual = helper.sublistWithName(nodes, "test");
 assertNull(actual);
 }

 @Test
 public void testSublistWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<>();
 List<Node> expected = new ArrayList<>();
 List<Node> actual = helper.sublistWithName(nodes, "test");
 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithNameSingleNode() {
 List<Node> nodes = Arrays.asList(createTestNode1());
 List<Node> expected = Arrays.asList(createTestNode1());
 List<Node> actual = helper.sublistWithName(nodes, "test");
 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithNameMultipleNodes() {
 List<Node> nodes = Arrays.asList(createTestNode1(), createTestNode2());
 List<Node> expected = Arrays.asList(createTestNode1());
 List<Node> actual = helper.sublistWithName(nodes, "test1");
 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithNameEmptySublist() {
 List<Node> nodes = Arrays.asList(createTestNode1());
 List<Node> sublist = Arrays.asList();
 List<Node> actual = helper.sublistWithName(nodes, "test");
 assertEquals(sublist, actual);
 }

 @Test
 public void testSublistWithNameNullName() {
 List<Node> nodes = Arrays.asList(createTestNode1());
 List<Node> actual = helper.sublistWithName(nodes, null);
 assertNull(actual);
 }

 private Node createTestNode() {
 Element node = document.createElement("test");
 document.appendChild(node);
 return node;
 }

 private Node createTestNode1() {
 Element node = document.createElement("test1");
 node.setAttribute("test1Attribute", "test1Value");
 document.appendChild(node);
 return node;
 }

 private Node createTestNode2() {
 Element node = document.createElement("test2");
 node.setAttribute("test2Attribute", "test2Value");
 document.appendChild(node);
 return node;
 }

 private Document createTestDocument() {
 Document document = document.createDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 return document;
 }
}