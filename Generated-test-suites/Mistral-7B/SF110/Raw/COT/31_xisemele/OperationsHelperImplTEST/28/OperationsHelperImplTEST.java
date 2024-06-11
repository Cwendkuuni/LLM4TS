package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

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

 private static final String XML_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n" +
         "<child1>\n<grandchild1/>\n<grandchild2/>\n</child1>\n<child2>\n<grandchild3/>\n</child2>\n</root>";

 @Test
 public void testChildrenMethod() {
         Document document = createDocumentFromString(XML_SAMPLE);
         Node root = document.getDocumentElement();
         List<Node> children = new OperationsHelperImpl().children(root);
         List<String> expectedChildrenNames = Arrays.asList("child1", "child2");
         List<String> childrenNames = new ArrayList<>();
         for (Node child : children) {
             childrenNames.add(child.getNodeName());
         }
         assertEquals(expectedChildrenNames, childrenNames);
 }

 @Test
 public void testFindMethod_ValidPath() {
         Document document = createDocumentFromString(XML_SAMPLE);
         Node found = new OperationsHelperImpl().find(document, "root/child1/grandchild1");
         assertNotNull(found);
         assertEquals("grandchild1", found.getNodeName());
 }

 @Test
 public void testFindMethod_InvalidPath() {
         Document document = createDocumentFromString(XML_SAMPLE);
         assertNull(new OperationsHelperImpl().find(document, "invalidPath"));
 }

 @Test
 public void testFindMethod_NullDocument() {
         assertNull(new OperationsHelperImpl().find(null, "root/child1/grandchild1"));
 }

 @Test
 public void testFindMethod_EmptyDocument() {
         Document document = createDocument();
         assertNull(new OperationsHelperImpl().find(document, "root/child1/grandchild1"));
 }

 @Test
 public void testNodeWithNameMethod_NullList() {
         List<Node> nodes = null;
         assertNull(new OperationsHelperImpl().nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithNameMethod_EmptyList() {
         List<Node> nodes = new ArrayList<>();
         assertNull(new OperationsHelperImpl().nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithNameMethod_ValidName() {
         Document document = createDocumentFromString(XML_SAMPLE);
         List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
         Node expectedNode = nodes.get(0);
         Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, "child1");
         assertNotSame(expectedNode, actualNode);
         assertEquals("child1", actualNode.getNodeName());
 }

 @Test
 public void testNodeWithNameMethod_InvalidName() {
         Document document = createDocumentFromString(XML_SAMPLE);
         List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
         assertNull(new OperationsHelperImpl().nodeWithName(nodes, "invalidName"));
 }

 @Test(expected = NullPointerException.class)
 public void testSublistWithNameMethod_NullList() {
         List<Node> nodes = null;
         new OperationsHelperImpl().sublistWithName(nodes, "name");
     }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameMethod_EmptyList() {
         List<Node> nodes = new ArrayList<>();
         new OperationsHelperImpl().sublistWithName(nodes, "name");
     }

 @Test
 public void testSublistWithNameMethod_ValidName() {
         Document document = createDocumentFromString(XML_SAMPLE);
         List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
         List<Node> expectedSublist = Arrays.asList(nodes.get(0).getChildNodes().item(0),
                 nodes.get(0).getChildNodes().item(1));
         List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, "grandchild1");
         assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithNameMethod_InvalidName() {
         Document document = createDocumentFromString(XML_SAMPLE);
         List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
         assertNotNull(nodes);
         List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, "invalidName");
         assertEquals(0, actualSublist.size());
 }

 private Document createDocument() {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         Document document;
         try {
             document = factory.newDocumentBuilder().newDocument();
         } catch (ParserConfigurationException e) {
             fail("Failed creating empty document.");
         }
         return document;
 }

 private Document createDocumentFromString(String xmlString) {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder;
         try {
             builder = factory.newDocumentBuilder();
         } catch (ParserConfigurationException e) {
             throw new RuntimeException(e);
         }
         Document document;
         try {
             document = builder.parse(new InputSource(new StringReader(xmlString)));
         } catch (SAXException | IOException e) {
             throw new RuntimeException(e);
         }
         return document;
 }
}