package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> children = OperationsHelperImpl.getInstance().children(testNode);
 assertEquals(2, children.size());
 }

 @Test
 public void testChildrenEmpty() {
 Node testNode = createTestNodeWithNoChildren();
 List<Node> children = OperationsHelperImpl.getInstance().children(testNode);
 assertEquals(0, children.size());
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 Node found = OperationsHelperImpl.getInstance().find(document, "/test/test2/test3");
 assertNull(found);

 document = createTestDocumentWithTestNode();
 Node found2 = OperationsHelperImpl.getInstance().find(document, "/test/test2/test3");
 assertNotNull(found2);
 assertEquals("test3", found2.getNodeName());
 }

 @Test
 public void testFindRoot() {
 Document document = createTestDocument();
 Node found = OperationsHelperImpl.getInstance().find(document, "/");
 assertEquals("test", found.getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidPath() {
 Document document = createTestDocument();
 OperationsHelperImpl.getInstance().find(document, "/test/test2/test3/foo");
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createTestNodes();
 Node node = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test3");
 assertNull(node);

 Node node2 = createTestNodeWithNameTest3();
 Node node3 = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test3");
 assertNotNull(node3);
 assertEquals("test3", node3.getNodeName());
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createTestNodes();
 List<Node> sublist = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test3");
 assertEquals(0, sublist.size());

 List<Node> nodesWithTest3 = createTestNodesWithTest3();
 List<Node> sublist2 = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test3");
 assertEquals(1, sublist2.size());
 assertEquals("test3", sublist2.get(0).getNodeName());
 }

 @Test
 public void testSublistWithNameEmpty() {
 List<Node> nodes = createTestNodes();
 List<Node> sublist = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test4");
 assertEquals(0, sublist.size());
 }

 private static Document createTestDocument() {
 Document document = new org.apache.xerces.impl.xs.doc.DocumentImpl();
 Element test = document.createElement("test");
 document.appendChild(test);
 return document;
 }

 private static Document createTestDocumentWithTestNode() {
 Document document = createTestDocument();
 Element test2 = document.createElement("test2");
 test.appendChild(test2);
 Element test3 = document.createElement("test3");
 test2.appendChild(test3);
 return document;
 }

 private static Node createTestNode() {
 Node testNode = new org.apache.xerces.impl.xs.doc.ElementImpl("test", null, null);
 return testNode;
 }

 private static Node createTestNodeWithNoChildren() {
 Node testNode = new org.apache.xerces.impl.xs.doc.ElementImpl("test", null, null);
 return testNode;
 }

 private static Node createTestNodeWithNameTest3() {
 Node testNode = new org.apache.xerces.impl.xs.doc.ElementImpl("test3", null, null);
 return testNode;
 }

 private static List<Node> createTestNodes() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(new org.apache.xerces.impl.xs.doc.ElementImpl("test1", null, null));
 nodes.add(new org.apache.xerces.impl.xs.doc.ElementImpl("test2", null, null));
 return nodes;
 }

 private static List<Node> createTestNodesWithTest3() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(new org.apache.xerces.impl.xs.doc.ElementImpl("test1", null, null));
 nodes.add(new org.apache.xerces.impl.xs.doc.ElementImpl("test2", null, null));
 nodes.add(new org.apache.xerces.impl.xs.doc.ElementImpl("test3", null, null));
 return nodes;
 }
}