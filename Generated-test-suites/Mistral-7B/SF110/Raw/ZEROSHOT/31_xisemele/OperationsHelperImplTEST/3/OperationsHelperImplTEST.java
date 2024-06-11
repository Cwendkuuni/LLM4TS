package net.sf.xisemele.impl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper helper;
 private Document document;

 @Before
 public void setUp() {
 helper = new OperationsHelperImpl();
 String xml = "<root><child1><child2>value1</child2></child1><child3><child4>value2</child4></child3></root>";
 try {
 document = new javax.xml.parsers.DocumentBuilderFactory().newDocumentBuilder().parse(new java.io.StringReader(xml));
 } catch (Exception e) {
 fail("Failed to parse XML");
 }
 }

 @Test
 public void testChildren() {
 Node root = document.getDocumentElement();
 List<Node> children = helper.children(root);
 assertEquals(3, children.size());
 assertEquals("child1", children.get(0).getNodeName());
 assertEquals("child3", children.get(1).getNodeName());
 }

 @Test
 public void testFind() {
 Node child1 = helper.find(document, "root/child1");
 assertNotNull(child1);
 assertEquals("child1", child1.getNodeName());

 Node child2 = helper.find(document, "root/child1/child2");
 assertNotNull(child2);
 assertEquals("child2", child2.getNodeName());

 Node nullNode = helper.find(document, "root/nonexistent");
 assertNull(nullNode);
 }

 @Test
 public void testNodeWithName() {
 Node child1 = helper.find(document, "root/child1");
 Node child2 = helper.nodeWithName(helper.children(child1), "child2");
 assertNotNull(child2);
 assertEquals("child2", child2.getNodeName());
 }

 @Test
 public void testSublistWithName() {
 Node child1 = helper.find(document, "root/child1");
 List<Node> child1Children = helper.children(child1);
 List<Node> sublist = helper.sublistWithName(child1Children, "child2");
 assertEquals(1, sublist.size());
 assertEquals("child2", sublist.get(0).getNodeName());
 }
}