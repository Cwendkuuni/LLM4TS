package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private Document createDocument(String xml) {
 return JaxbHelper.parseStringToDocument(xml);
 }

 @Test
 public void testChildrenEmpty() {
 Node emptyNode = createDocument("").getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(emptyNode);
 assertEquals(0, children.size());
 }

 @Test
 public void testChildrenOneElement() {
 String xml = "<root><element>test</element></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 assertEquals(1, children.size());
 assertEquals("element", children.get(0).getNodeName());
 }

 @Test
 public void testChildrenMultipleElements() {
 String xml = "<root><element1>test1</element1><element2>test2</element2></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 assertEquals(2, children.size());
 assertEquals("element1", children.get(0).getNodeName());
 assertEquals("element2", children.get(1).getNodeName());
 }

 @Test
 public void testFindRoot() {
 String xml = "<root><element>test</element></root>";
 Document document = createDocument(xml);
 Node found = new OperationsHelperImpl().find(document, "/root");
 assertNotNull(found);
 assertEquals("root", found.getNodeName());
 }

 @Test
 public void testFindEmpty() {
 String xml = "<root></root>";
 Document document = createDocument(xml);
 Node found = new OperationsHelperImpl().find(document, "/nonexistent");
 assertNull(found);
 }

 @Test
 public void testFindMultipleLevels() {
 String xml = "<root><element1><element2>test</element2></element1></root>";
 Document document = createDocument(xml);
 Node found = new OperationsHelperImpl().find(document, "/root/element1/element2");
 assertNotNull(found);
 assertEquals("element2", found.getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullDocument() {
 new OperationsHelperImpl().find(null, "/root");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullPath() {
 Document document = createDocument("<root><element>test</element></root>");
 new OperationsHelperImpl().find(document, null);
 }

 @Test
 public void testNodeWithNameEmptyList() {
 List<Node> emptyList = new ArrayList<Node>();
 Node node = new OperationsHelperImpl().nodeWithName(emptyList, "test");
 assertNull(node);
 }

 @Test
 public void testNodeWithNameSingleElement() {
 String xml = "<root><element>test</element></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 Node node = new OperationsHelperImpl().nodeWithName(children, "element");
 assertNotNull(node);
 assertEquals("element", node.getNodeName());
 }

 @Test
 public void testNodeWithNameMultipleElements() {
 String xml = "<root><element1>test1</element1><element2>test2</element2></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 Node node = new OperationsHelperImpl().nodeWithName(children, "element2");
 assertNotNull(node);
 assertEquals("element2", node.getNodeName());
 node = new OperationsHelperImpl().nodeWithName(children, "element1");
 assertNotNull(node);
 assertEquals("element1", node.getNodeName());
 node = new OperationsHelperImpl().nodeWithName(children, "nonexistent");
 assertNull(node);
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> emptyList = new ArrayList<Node>();
 List<Node> sublist = new OperationsHelperImpl().sublistWithName(emptyList, "test");
 assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithNameSingleElement() {
 String xml = "<root><element>test</element></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 List<Node> sublist = new OperationsHelperImpl().sublistWithName(children, "element");
 assertEquals(1, sublist.size());
 assertEquals("element", sublist.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameMultipleElements() {
 String xml = "<root><element1><element2>test1</element2><element3>test2</element3></element1></root>";
 Node rootNode = createDocument(xml).getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 List<Node> sublist1 = new OperationsHelperImpl().sublistWithName(children, "element1");
 assertEquals(1, sublist1.size());
 assertEquals("element1", sublist1.get(0).getNodeName());
 List<Node> sublist2 = new OperationsHelperImpl().sublistWithName(sublist1, "element2");
 assertEquals(1, sublist2.size());
 assertEquals("element2", sublist2.get(0).getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameNullList() {
 new OperationsHelperImpl().sublistWithName(null, "test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameNullName() {
 List<Node> children = new ArrayList<Node>();
 children.add(createDocument("<element>test</element>").getDocumentElement());
 new OperationsHelperImpl().sublistWithName(children, null);
 }

}