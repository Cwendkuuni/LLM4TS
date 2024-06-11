package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert. assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private static final String XML_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
         "<root>" +
         "<element1>Element 1</element1>" +
         "<element2><subElement1>Sub Element 1</subElement1>" +
         "<subElement2>Sub Element 2</subElement2></element2>" +
         "</root>";

 @Test
 public void testChildren() {
     Document document = createDocumentFromString(XML_SAMPLE);
     Element root = (Element) document.getDocumentElement();
     List<Node> children = new OperationsHelperImpl().children(root);
     assertEquals(3, children.size());
     assertEquals("element1", children.get(0).getNodeName());
     assertEquals("element2", children.get(1).getNodeName());
 }

 @Test
 public void testFindNullDocument() {
     assertNull(new OperationsHelperImpl().find(null, "/root"));
 }

 @Test
 public void testFindNullPath() {
     Document document = createDocumentFromString(XML_SAMPLE);
     assertNull(new OperationsHelperImpl().find(document, null));
 }

 @Test
 public void testFindRoot() {
     Document document = createDocumentFromString(XML_SAMPLE);
     Node root = document.getDocumentElement();
     assertEquals(root, new OperationsHelperImpl().find(document, "/root"));
 }

 @Test
 public void testFindElement2() {
     Document document = createDocumentFromString(XML_SAMPLE);
     assertEquals(document.getElementsByTagName("element2").item(0), new OperationsHelperImpl().find(document, "/root/element2"));
 }

 @Test
 public void testFindSubElement1() {
     Document document = createDocumentFromString(XML_SAMPLE);
     assertEquals(document.getElementsByTagName("subElement1").item(0), new OperationsHelperImpl().find(document, "/root/element2/subElement1"));
 }

 @Test
 public void testFindNullNode() {
     Document document = createDocumentFromString(XML_SAMPLE);
     assertNull(new OperationsHelperImpl().find(document.getDocumentElement(), "/root/nullNode"));
 }

 @Test
 public void testNodeWithNameNullList() {
     assertNull(new OperationsHelperImpl().nodeWithName(null, "element1"));
 }

 @Test
 public void testNodeWithNameEmptyList() {
     assertNull(new OperationsHelperImpl().nodeWithName(new ArrayList<>(), "element1"));
 }

 @Test
 public void testNodeWithNameElement1() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     assertEquals(nodes.get(0), new OperationsHelperImpl().nodeWithName(nodes, "element1"));
 }

 @Test
 public void testNodeWithNameElement2() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     Node element2 = nodes.get(1);
     List<Node> childNodes = new OperationsHelperImpl().children(element2);
     assertEquals(childNodes.get(0), new OperationsHelperImpl().nodeWithName(childNodes, "subElement1"));
 }

 @Test
 public void testNodeWithNameNullName() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     assertNull(new OperationsHelperImpl().nodeWithName(nodes, null));
 }

 @Test
 public void testSublistWithNameEmptyList() {
     assertFalse(new OperationsHelperImpl().sublistWithName(new ArrayList<>(), "element1").isEmpty());
 }

 @Test
 public void testSublistWithNameNullList() {
     assertEquals(0, new OperationsHelperImpl().sublistWithName(null, "element1").size());
 }

 @Test
 public void testSublistWithNameEmptyName() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     assertEquals(0, new OperationsHelperImpl().sublistWithName(nodes, null).size());
 }

 @Test
 public void testSublistWithNameElement1() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "element1");
     assertEquals(1, sublist.size());
 }

 @Test
 public void testSublistWithNameElement2() {
     Document document = createDocumentFromString(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "element2");
     assertEquals(2, sublist.size());
 }

 private Document createDocumentFromString(String xmlString) {
     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
     DocumentBuilder documentBuilder = null;
     try {
         documentBuilder = documentBuilderFactory.newDocumentBuilder();
     } catch (ParserConfigurationException e) {
         e.printStackTrace();
     }
     Document document = null;
     try {
         document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
     } catch (SAXException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } catch (ParserConfigurationException e) {
         e.printStackTrace();
     } catch (NullPointerException e) {
         e.printStackTrace();
     }
     return document;
 }

}