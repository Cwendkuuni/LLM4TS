package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));
 root.appendChild(createElement("child3"));
 root.appendChild(createElement("grandchild1"));
 root.appendChild(createElement("grandchild2"));

 final List<Node> children = operationsHelper.children(root);

 assertEquals(5, children.size());
 assertEquals("child1", children.get(0).getNodeName());
 assertEquals("child2", children.get(1).getNodeName());
 assertEquals("child3", children.get(2).getNodeName());
 assertEquals("grandchild1", children.get(3).getNodeName());
 assertEquals("grandchild2", children.get(4).getNodeName());
 }

 @Test
 public void testFindNullDocument() {
 assertNull(operationsHelper.find(null, ""));
 }

 @Test
 public void testFindNullPath() {
 final Document document = createMockDocument();
 assertNull(operationsHelper.find(document, ""));
 }

 @Test
 public void testFindEmptyDocument() {
 final Document document = createMockDocument();
 assertNull(operationsHelper.find(document, "/"));
 }

 @Test
 public void testFindRoot() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();

 assertSame(root, operationsHelper.find(document, ""));
 }

 @Test
 public void testFindSingleLevel() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));

 assertSame(root.getChildNodes().item(0), operationsHelper.find(document, "/child1"));
 }

 @Test
 public void testFindMultipleLevels() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();

 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));
 childElement(root, "child1").appendChild(createElement("grandchild1"));
 childElement(root, "child1").appendChild(createElement("grandchild2"));

 assertSame(childElement(root, "child1").getChildNodes().item(0), operationsHelper.find(document, "/child1/grandchild1"));
 }

 @Test
 public void testFindNonExistentPath() {
 final Document document = createMockDocument();
 assertNull(operationsHelper.find(document, "/nonExistentPath"));
 }

 @Test
 public void testNodeWithNameNullNodes() {
 assertNull(operationsHelper.nodeWithName(null, ""));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 assertNull(operationsHelper.nodeWithName(Collections.emptyList(), ""));
 }

 @Test
 public void testNodeWithNameExistent() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));

 assertSame(root.getChildNodes().item(0), operationsHelper.nodeWithName(root.getChildNodes(), "child1"));
 }

 @Test
 public void testNodeWithNameNonExistent() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));

 assertNull(operationsHelper.nodeWithName(root.getChildNodes(), "child3"));
 }

 @Test
 public void testSublistWithNameNullNodes() {
 assertEmpty(operationsHelper.sublistWithName(null, ""));
 }

 @Test
 public void testSublistWithNameEmptyList() {
 assertEmpty(operationsHelper.sublistWithName(Collections.emptyList(), ""));
 }

 @Test
 public void testSublistWithNameExistent() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("parent1"));
 root.appendChild(createElement("parent2"));
 final Element parent1 = childElement(root, "parent1");
 parent1.appendChild(createElement("child1"));
 parent1.appendChild(createElement("child2"));
 parent1.appendChild(createElement("child3"));

 final List<Node> sublist = operationsHelper.sublistWithName(root.getChildNodes(), "parent1/child1");

 assertEquals(2, sublist.size());
 assertEquals("child1", sublist.get(0).getNodeName());
 assertEquals("child2", sublist.get(1).getNodeName());
 }

 @Test
 public void testSublistWithNameNonExistent() {
 final Document document = createMockDocument();
 final Element root = document.getDocumentElement();
 root.appendChild(createElement("parent1"));
 root.appendChild(createElement("parent2"));

 final List<Node> sublist = operationsHelper.sublistWithName(root.getChildNodes(), "parent3/child1");

 assertEmpty(sublist);
 }

 private Node createElement(String nodeName) {
 final Document document = createMockDocument();
 return document.createElement(nodeName);
 }

 private Element childElement(Node parent, String nodeName) {
 final Element child = createElement(nodeName);
 parent.appendChild(child);
 return child;
 }

 private Document createMockDocument() {
 final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 return documentBuilderFactory.newDocumentBuilder().newDocument();
 }
}