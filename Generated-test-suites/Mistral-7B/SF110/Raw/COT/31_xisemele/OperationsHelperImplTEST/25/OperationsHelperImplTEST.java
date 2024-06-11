package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private static final String XML_SAMPLE = "<?xml version=\"1.0\"?>\n<root>\n<child1>\n<grandchild1></grandchild1>\n<child2>\n<subchild1></subchild1>\n<subchild2></subchild2>\n</child2>\n</root>";

 @Test
 public void testChildren() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 Node root = document.getDocumentElement();
 List<Node> children = helper.children(root);
 assertEquals(3, children.size());
 assertTrue(children.get(0) instanceof Element);
 assertEquals("child1", children.get(0).getNodeName());
 // Remaining assertions and checks for the other children
 }

 @Test
 public void testFind() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 List<Node> result = helper.find(document, "/root/child1/grandchild1");
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals("grandchild1", result.get(0).getNodeName());

 List<Node> result2 = helper.find(document, "/root/child1/grandchild2");
 assertNull(result2);
 }

 @Test
 public void testFindWithEmptyPath() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 List<Node> result = helper.find(document, "/");
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals("root", result.get(0).getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullDocument() {
 OperationsHelper helper = new OperationsHelperImpl();
 List<Node> result = helper.find(null, "/root/child1/grandchild1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithNullPath() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 List<Node> result = helper.find(document, null);
 }

 @Test
 public void testNodeWithName() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 Node root = document.getDocumentElement();
 List<Node> nodes = helper.children(root);
 List<Node> result = helper.sublistWithName(nodes, "child1");
 Node node = helper.nodeWithName(result, "child1");
 assertNotNull(node);
 assertEquals("child1", node.getNodeName());
 }

 @Test
 public void testNodeWithNameNotFound() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 Node root = document.getDocumentElement();
 List<Node> nodes = helper.children(root);
 List<Node> result = helper.sublistWithName(nodes, "child3");
 Node node = helper.nodeWithName(result, "child1");
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 Node root = document.getDocumentElement();
 List<Node> nodes = helper.children(root);
 List<Node> result = helper.sublistWithName(nodes, "child2");
 assertEquals(2, result.size());
 assertEquals("subchild1", result.get(0).getNodeName());
 assertEquals("subchild2", result.get(1).getNodeName());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 OperationsHelper helper = new OperationsHelperImpl();
 Document document = createDocument(XML_SAMPLE);
 Node root = document.getDocumentElement();
 List<Node> nodes = new ArrayList<Node>();
 List<Node> result = helper.sublistWithName(nodes, "child2");
 assertEquals(0, result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameNullList() {
 OperationsHelper helper = new OperationsHelperImpl();
 List<Node> nodes = null;
 List<Node> result = helper.sublistWithName(nodes, "child2");
 }

 private Document createDocument(String xml) {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 try {
 DocumentBuilder builder = factory.newDocumentBuilder();
 Document document = builder.parse(new InputSource(new StringReader(xml)));
 return document;
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

}