package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper;
 private Document document;

 @Before
 public void setUp() {
 String xml = "<root><element1><element2><element3></element2></element1></root>";
 try {
 document = new DOMParser().parseFromString(xml, "text/xml");
 helper = new OperationsHelperImpl();
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testChildren() {
 Node root = document.getDocumentElement();
 List<Node> children = helper.children(root);
 assertEquals(3, children.size());
 assertTrue(children.contains(element2));
 assertTrue(children.contains(element1));
 assertTrue(children.contains(document.getDocumentElement()));
 }

 @Test
 public void testFind() {
 Node node = helper.find(document, "root/element1/element2");
 assertEquals("element2", node.getNodeName());
 }

 @Test
 public void testFindNull() {
 Node node = helper.find(document, "nonExistentPath");
 assertNull(node);
 }

 @Test
 public void testNodeWithName() {
 List<Node> children = helper.children(document.getDocumentElement());
 Node node = helper.nodeWithName(children, "element1");
 assertEquals("element1", node.getNodeName());
 }

 @Test
 public void testNodeWithNameNull() {
 List<Node> children = helper.children(document.getDocumentElement());
 Node node = helper.nodeWithName(children, "nonExistentName");
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 List<Node> children = helper.children(document.getDocumentElement());
 List<Node> sublist = helper.sublistWithName(children, "element1");
 assertEquals(1, sublist.size());
 assertTrue(sublist.contains(element1));
 }

 @Test
 public void testSublistWithNameEmpty() {
 List<Node> children = helper.children(document.getDocumentElement());
 List<Node> sublist = helper.sublistWithName(children, "nonExistentName");
 assertEquals(0, sublist.size());
 }

 private Node element1;
 private Node element2;
 private Node element3;

 @Before
 public void setUpInnerNodes() {
 element1 = helper.find(document, "root/element1");
 element2 = helper.find(document, "root/element1/element2");
 element3 = helper.find(document, "root/element1/element2/element3");
 }

 @Test
 public void testChildrenOfElement1() {
 List<Node> children = helper.children(element1);
 assertEquals(2, children.size());
 assertTrue(children.contains(element2));
 assertTrue(children.contains(document.getDocumentElement()));
 }

 @Test
 public void testFindElement3() {
 Node node = helper.find(document, "root/element1/element2/element3");
 assertEquals("element3", node.getNodeName());
 }

 @Test
 public void testFindElement1() {
 Node node = helper.find(document, "root/element1");
 assertEquals("element1", node.getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 helper.find(null, "root/element1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyPath() {
 helper.find(document, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPath() {
 helper.find(document, null);
 }
}