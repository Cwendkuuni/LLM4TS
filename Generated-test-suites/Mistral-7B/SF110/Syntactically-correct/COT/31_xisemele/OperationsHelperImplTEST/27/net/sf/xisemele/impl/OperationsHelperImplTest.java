package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private static final String XML_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<child1>\n" +
         "<grandchild1/>\n" +
         "<grandchild2>\n" +
         "<subchild1/>\n" +
         "<subchild2>\n" +
         "<subsubchild1/>\n" +
         "</subchild2>\n" +
         "</child1>\n" +
         "</root>";

 @Test
 public void testChildren() {
     Document document = newJaxbDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     List<Node> children = new OperationsHelperImpl().children(root);
     assertEquals(3, children.size());
     assertTrue(children.contains(root.getElementsByTagName("child1").item(0)));
     assertTrue(children.contains(root.getElementsByTagName("child1").item(0).getElementsByTagName("grandchild1").item(0)));
     assertTrue(children.contains(root.getElementsByTagName("child1").item(0).getElementsByTagName("grandchild2").item(0)));
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenNullNode() {
     List<Node> children = new OperationsHelperImpl().children(null);
 }

 @Test
 public void testFind() {
     Document document = newJaxbDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     String path = "root/child1/grandchild1";
     Node node = new OperationsHelperImpl().find(document, path);
     assertNotNull(node);
     assertEquals("grandchild1", node.getNodeName());
 }

 @Test
 public void testFindNullDocument() {
     String path = "root/child1/grandchild1";
     Node node = new OperationsHelperImpl().find(null, path);
     assertNull(node);
 }

 @Test
 public void testFindNullPath() {
     Document document = newJaxbDocument(XML_SAMPLE);
     Node node = new OperationsHelperImpl().find(document, null);
     assertNull(node);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidPath() {
     Document document = newJaxbDocument(XML_SAMPLE);
     String path = "root/child1/grandchildX";
     new OperationsHelperImpl().find(document, path);
 }

 @Test
 public void testFindEmptyDocument() {
     Document document = new Document();
     String path = "root/child1/grandchild1";
     Node node = new OperationsHelperImpl().find(document, path);
     assertNull(node);
 }

 @Test
 public void testNodeWithName() {
     Document document = newJaxbDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     List<Node> nodes = new OperationsHelperImpl().children(root);
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "child1");
     assertNotNull(node);
     assertEquals("child1", node.getNodeName());
 }

 @Test
 public void testNodeWithNameNullNodes() {
     List<Node> nodes = null;
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "child1");
     assertNull(node);
 }

 @Test
 public void testNodeWithNameEmptyList() {
     Document document = new Document();
     List<Node> nodes = new OperationsHelperImpl().children(document);
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "child1");
     assertNull(node);
 }

 @Test
 public void testNodeWithNameNonExistentName() {
     Document document = newJaxbDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "nonExistentName");
     assertNull(node);
 }

 @Test
 public void testSublistWithName() {
     Document document = newJaxbDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     List<Node> nodes = new OperationsHelperImpl().children(root);
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "grandchild2");
     assertEquals(2, sublist.size());
     assertTrue(sublist.contains(root.getElementsByTagName("child1").item(0).getElementsByTagName("grandchild2").item(0)));
     assertTrue(sublist.contains(root.getElementsByTagName("child1").item(0).getElementsByTagName("grandchild2").item(1)));
 }

 @Test
 public void testSublistWithNameEmptyList() {
     Document document = new Document();
     List<Node> nodes = new OperationsHelperImpl().children(document);
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "grandchild2");
     assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithNameNullNodes() {
     List<Node> nodes = null;
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "grandchild2");
     assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithNameNonExistentName() {
     Document document = newJaxbDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "nonExistentName");
     assertEquals(0, sublist.size());
 }

 private Document newJaxbDocument(String xml) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = null;
     try {
         builder = factory.newDocumentBuilder();
     } catch (ParserConfigurationException e) {
         e.printStackTrace();
     }
     Document document = null;
     try {
         document = builder.parse(new InputSource(new StringReader(xml)));
     } catch (SAXException | IOException e) {
         e.printStackTrace();
     }
     return document;
 }

}