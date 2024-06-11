package net.sf.xisemele.impl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private Document document;
 private OperationsHelper operationsHelper;

 @Before
 public void setUp() {
 document = createDocument();
 operationsHelper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren() {
 Node root = document.getDocumentElement();
 List<Node> expectedChildren = new ArrayList<>();
 expectedChildren.add(document.createProcessingInstruction("xml", "version='1.0'"));
 expectedChildren.add(root.getElementsByTagName("root").item(0));

 List<Node> children = operationsHelper.children(root);
 assertEquals(expectedChildren.size(), children.size());

 for (int i = 0; i < expectedChildren.size(); i++) {
 assertEquals(expectedChildren.get(i).getNodeName(), children.get(i).getNodeName());
 }
 }

 @Test
 public void testFindNullDocument() {
 assertNull(operationsHelper.find(null, "/root"));
 }

 @Test
 public void testFindNullPath() {
 assertNull(operationsHelper.find(document, null));
 }

 @Test
 public void testFindRoot() {
 Node root = document.getDocumentElement();
 Node found = operationsHelper.find(document, "/");
 assertEquals(root, found);
 }

 @Test
 public void testFindNonExistentNode() {
 assertNull(operationsHelper.find(document, "/nonexistentnode"));
 }

 @Test
 public void testFindExistentNode() {
 Node root = document.getDocumentElement();
 Node rootChild = root.getElementsByTagName("root").item(0);
 Node expectedChild = rootChild.getElementsByTagName("child").item(0);
 Node found = operationsHelper.find(document, "/root/child/grandchild");
 assertEquals(expectedChild, found);
 }

 @Test
 public void testNodeWithNameNullList() {
 assertNull(operationsHelper.nodeWithName(null, "name"));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 assertNull(operationsHelper.nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithNameExistentNode() {
 Node root = document.getDocumentElement();
 Node rootChild = root.getElementsByTagName("root").item(0);
 Node expectedChild = rootChild.getElementsByTagName("child").item(0);
 List<Node> nodes = new ArrayList<>(Arrays.asList(rootChild));
 Node found = operationsHelper.nodeWithName(nodes, "child");
 assertEquals(expectedChild, found);
 }

 @Test
 public void testNodeWithNameNonExistentNode() {
 Node root = document.getDocumentElement();
 Node rootChild = root.getElementsByTagName("root").item(0);
 List<Node> nodes = new ArrayList<>(Arrays.asList(rootChild));
 assertNull(operationsHelper.nodeWithName(nodes, "grandchild"));
 }

 @Test
 public void testSublistWithNameNullList() {
 assertEmpty(operationsHelper.sublistWithName(null, "name"));
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> expectedSublist = new ArrayList<>();
 List<Node> foundSublist = operationsHelper.sublistWithName(nodes, "name");
 assertEquals(expectedSublist, foundSublist);
 }

 @Test
 public void testSublistWithNameExistentNodes() {
 Node root = document.getDocumentElement();
 Node rootChild = root.getElementsByTagName("root").item(0);
 Node child = rootChild.getElementsByTagName("child").item(0);
 Node grandchild = child.getElementsByTagName("grandchild").item(0);
 List<Node> nodes = new ArrayList<>(Arrays.asList(rootChild, child));
 List<Node> expectedSublist = new ArrayList<>(Arrays.asList(child));
 List<Node> foundSublist = operationsHelper.sublistWithName(nodes, "child");
 assertEquals(expectedSublist, foundSublist);
 }

 @Test
 public void testSublistWithNameNonExistentNodes() {
 Node root = document.getDocumentElement();
 Node rootChild = root.getElementsByTagName("root").item(0);
 List<Node> nodes = new ArrayList<>(Arrays.asList(rootChild));
 List<Node> expectedSublist = new ArrayList<>();
 List<Node> foundSublist = operationsHelper.sublistWithName(nodes, "grandchild");
 assertEquals(expectedSublist, foundSublist);
 }

 private Document createDocument() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = null;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 e.printStackTrace();
 }
 Document document = null;
 try {
 document = builder.newDocument();
 } catch (DocumentInitializationException e) {
 e.printStackTrace();
 }
 // Add processing instruction
 document.insertBefore(document.createProcessingInstruction("xml", "version='1.0'"), document.getDocumentElement());
 // Add root element
 Element root = document.createElement("root");
 document.appendChild(root);
 // Add child element
 Element child = document.createElement("child");
 root.appendChild(child);
 // Add grandchild element
 Element grandchild = document.createElement("grandchild");
 child.appendChild(grandchild);
 // Add text node
 Text text = document.createTextNode("text");
 grandchild.appendChild(text);
 return document;
 }
}