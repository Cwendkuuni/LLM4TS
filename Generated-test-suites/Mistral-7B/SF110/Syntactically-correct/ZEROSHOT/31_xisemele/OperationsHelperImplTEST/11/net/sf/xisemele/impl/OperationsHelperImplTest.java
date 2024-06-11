package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private static final String XML_FILE_PATH = "path/to/your/xml/file.xml";

 @Test
 public void testChildren() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final Node root = document.getDocumentElement();

 final List<Node> expectedChildren = Arrays.asList(
 new Node[] { Helper.createNode("child1", Element.ELEMENT_NODE, document),
 new Node[] { Helper.createNode("grandChild1", Element.ELEMENT_NODE, document),
 Helper.createNode("grandChild2", Element.ELEMENT_NODE, document) },
 new Node[] { Helper.createNode("child2", Element.ELEMENT_NODE, document),
 Helper.createNode("child3", Element.ELEMENT_NODE, document) }
 });

 final List<Node> actualChildren = new OperationsHelperImpl().children(root);

 assertEquals("Size of the lists must be equal.", expectedChildren.size(), actualChildren.size());

 for (int i = 0; i < expectedChildren.size(); i++) {
 final Node expectedChild = expectedChildren.get(i);
 final Node actualChild = actualChildren.get(i);
 assertEquals("Children nodes must be equal.", expectedChild, actualChild);
 }
 }

 @Test
 public void testFindNullDocument() {
 final Document document = null;
 final String path = "/root/child1";

 final Node actualNode = new OperationsHelperImpl().find(document, path);

 assertNull("find() should return null when document is null.", actualNode);
 }

 @Test
 public void testFindNullPath() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final String path = null;

 final Node actualNode = new OperationsHelperImpl().find(document, path);

 assertNull("find() should return null when path is null.", actualNode);
 }

 @Test
 public void testFindNonExistentPath() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final String path = "/root/notExistentNode";

 final Node actualNode = new OperationsHelperImpl().find(document, path);

 assertNull("find() should return null when the node does not exist.", actualNode);
 }

 @Test
 public void testFindExistentPath() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final String path = "/root/child1/grandChild1";

 final Node actualNode = new OperationsHelperImpl().find(document, path);

 assertNotNull("find() should return a non-null node.", actualNode);
 }

 @Test
 public void testNodeWithNameNullNodesList() {
 final List<Node> nodes = null;
 final String name = "name";

 final Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, name);

 assertNull("nodeWithName() should return null when nodes list is null.", actualNode);
 }

 @Test
 public void testNodeWithNameEmptyNodesList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "name";

 final Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, name);

 assertNull("nodeWithName() should return null when nodes list is empty.", actualNode);
 }

 @Test
 public void testNodeWithNameNonExistentName() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
 final String name = "notExistentName";

 final Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, name);

 assertNull("nodeWithName() should return null when the node with the given name does not exist.", actualNode);
 }

 @Test
 public void testNodeWithNameExistentName() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
 final String name = "child1";

 final Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, name);

 assertNotNull("nodeWithName() should return a non-null node.", actualNode);
 }

 @Test
 public void testSublistWithNameEmptyNodesList() {
 final List<Node> nodes = Collections.emptyList();
 final String name = "name";

 final List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, name);

 assertSame("sublistWithName() should return an empty list when nodes list is empty.", Collections.emptyList(), actualSublist);
 }

 @Test
 public void testSublistWithNameNullNodesList() {
 final List<Node> nodes = null;
 final String name = "name";

 final List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, name);

 assertSame("sublistWithName() should return an empty list when nodes list is null.", Collections.emptyList(), actualSublist);
 }

 @Test
 public void testSublistWithNameNonExistentName() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
 final String name = "notExistentName";

 final List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, name);

 assertSame("sublistWithName() should return an empty list when the node with the given name does not exist.", Collections.emptyList(), actualSublist);
 }

 @Test
 public void testSublistWithNameExistentName() {
 final Document document = Helper.createDocumentFromFile(XML_FILE_PATH);
 final List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
 final String name = "child1";

 final List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, name);

 final List<Node> expectedSublist = Arrays.asList(new Node[] { Helper.createNode("child1", Element.ELEMENT_NODE, document),
 Helper.createNode("grandChild1", Element.ELEMENT_NODE, document),
 Helper.createNode("grandChild2", Element.ELEMENT_NODE, document) });

 assertEquals("sublistWithName() should return the correct sublist.", expectedSublist, actualSublist);
 }
}