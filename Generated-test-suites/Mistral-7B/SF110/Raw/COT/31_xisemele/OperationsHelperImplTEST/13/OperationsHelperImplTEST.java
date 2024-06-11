package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.names.QName;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private static final String XML_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<node1>\n" +
         "<node2>\n" +
         "<node3>\n" +
         "</node3>\n" +
         "</node2>\n" +
         "</node1>\n" +
         "</root>";

 @Test
 public void testChildren() {
     Document document = newDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     List<Node> children = new OperationsHelperImpl().children(root);
     assertEquals(3, children.size());
     assertEquals(root.getNodeName(), children.get(0).getNodeName());
     assertEquals("node1", children.get(1).getNodeName());
     assertEquals("node2", children.get(2).getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChildrenWithNullNode() {
     List<Node> children = new OperationsHelperImpl().children(null);
     fail("Expected IllegalArgumentException, but got " + children);
 }

 @Test
 public void testFind() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> result = new OperationsHelperImpl().find(document, "/root/node1");
     assertNotNull(result);
     assertEquals(1, result.size());
     assertEquals("node1", result.get(0).getNodeName());
 }

 @Test
 public void testFindWithNullDocument() {
     List<Node> result = new OperationsHelperImpl().find(null, "/root/node1");
     assertNull(result);
 }

 @Test
 public void testFindWithEmptyPath() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> result = new OperationsHelperImpl().find(document, "");
     assertNotNull(result);
     assertEquals(1, result.size());
     assertEquals(document.getDocumentElement().getNodeName(), result.get(0).getNodeName());
 }

 @Test
 public void testFindWithInvalidPath() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> result = new OperationsHelperImpl().find(document, "/root/nodeX");
     assertNull(result);
 }

 @Test
 public void testNodeWithName() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
     Node node3 = new OperationsHelperImpl().nodeWithName(children, "node3");
     assertNotNull(node3);
     assertEquals("node3", node3.getNodeName());
 }

 @Test
 public void testNodeWithNameWithNullList() {
     List<Node> nodes = null;
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "node1");
     assertNull(node);
 }

 @Test
 public void testNodeWithNameWithWrongName() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
     Node node = new OperationsHelperImpl().nodeWithName(children, "nodeX");
     assertNull(node);
 }

 @Test
 public void testSublistWithName() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(children, "node1");
     assertEquals(1, sublist.size());
     assertEquals("node1", sublist.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameWithNullList() {
     List<Node> nodes = null;
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "node1");
     assertEmpty(sublist);
 }

 @Test
 public void testSublistWithNameWithWrongName() {
     Document document = newDocument(XML_SAMPLE);
     List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(children, "nodeX");
     assertEmpty(sublist);
 }

 private Document newDocument(String xml) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = factory.newDocumentBuilder();
     Document document = builder.parse(new InputSource(new StringReader(xml)));
     return document;
 }
}