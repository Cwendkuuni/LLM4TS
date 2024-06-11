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
import net.sf.xisemele.impl.OperationsHelper;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestNode();
 List<Node> children = helper.children(node);
 assertEquals(3, children.size());
 }

 @Test
 public void testChildrenEmpty() {
 Node node = createTestNode();
 node.getChildNodes().setLength(0);
 List<Node> children = helper.children(node);
 assertEquals(0, children.size());
 }

 @Test
 public void testChildrenNull() {
 List<Node> children = helper.children(null);
 assertNull(children);
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 String path = "/root/child1/child2";
 Node found = helper.find(document, path);
 assertNotNull(found);
 assertEquals("child2", found.getNodeName());
 }

 @Test
 public void testFindNullDocument() {
 Document document = null;
 String path = "/root/child1/child2";
 Node found = helper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestDocument();
 String path = null;
 Node found = helper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testFindPathNotExist() {
 Document document = createTestDocument();
 String path = "/root/notExist";
 Node found = helper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createTestNodes();
 Node node = helper.nodeWithName(nodes, "node3");
 assertNotNull(node);
 assertEquals("node3", node.getNodeName());
 }

 @Test
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = null;
 Node node = helper.nodeWithName(nodes, "node3");
 assertNull(node);
 }

 @Test
 public void testNodeWithNameNullName() {
 List<Node> nodes = createTestNodes();
 String name = null;
 Node node = helper.nodeWithName(nodes, name);
 assertNull(node);
 }

 @Test
 public void testNodeWithNameNameNotExist() {
 List<Node> nodes = createTestNodes();
 String name = "notExist";
 Node node = helper.nodeWithName(nodes, name);
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createTestNodes();
 List<Node> sublist = helper.sublistWithName(nodes, "node2");
 assertEquals(1, sublist.size());
 assertNotNull(sublist.get(0));
 assertEquals("node2", sublist.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> sublist = helper.sublistWithName(nodes, "node2");
 assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithNameNullList() {
 List<Node> nodes = null;
 List<Node> sublist = helper.sublistWithName(nodes, "node2");
 assertNull(sublist);
 }

 @Test
 public void testSublistWithNameNullName() {
 List<Node> nodes = createTestNodes();
 String name = null;
 List<Node> sublist = helper.sublistWithName(nodes, name);
 assertNull(sublist);
 }

 private Document createTestDocument() {
 Document document = createTestDOM();
 return document;
 }

 private Node createTestNode() {
 Node node = document.createProcessingInstruction("xml", "version=\"1.0\"");
 Node element = document.createElement("root");
 element.appendChild(node);
 Node child1 = document.createElement("child1");
 element.appendChild(child1);
 Node child2 = document.createElement("child2");
 child1.appendChild(child2);
 node = document.createProcessingInstruction("xml", "version=\"1.0\"");
 child2.appendChild(node);
 Node child3 = document.createElement("child3");
 child2.appendChild(child3);
 return child3;
 }

 private List<Node> createTestNodes() {
 Document document = createTestDocument();
 Element root = document.getDocumentElement();
 return new ArrayList<>(Arrays.asList(root.getChildNodes()));
 }

 private Document createTestDOM() {
 Document document = document.implementation.createDocument("", "", null);
 return document;
 }

}