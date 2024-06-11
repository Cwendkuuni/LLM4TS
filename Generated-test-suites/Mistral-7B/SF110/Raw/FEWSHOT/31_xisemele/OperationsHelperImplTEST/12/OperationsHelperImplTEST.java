package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private Document mockDocument;
 private Node mockRoot;

 @Before
 public void setUp() {
 mockDocument = mock(Document.class);
 mockRoot = mock(Element.class);
 when(mockDocument.getDocumentElement()).thenReturn(mockRoot);
 }

 @Test
 public void testChildren() {
 List<Node> children = new ArrayList<>();
 children.add(mockRoot);
 children.add(createMockNode("child1"));
 children.add(createMockNode("child2"));

 Node node = createMockNode("parent");
 NodeList nodeList = createMockNodeList(children.toArray(new Node[0]));
 when(mockRoot.getChildNodes()).thenReturn(nodeList);

 List<Node> result = new OperationsHelperImpl().children(node);

 assertEquals(2, result.size());
 assertEquals("child1", result.get(0).getNodeName());
 assertEquals("child2", result.get(1).getNodeName());
 }

 @Test
 public void testChildrenEmpty() {
 Node node = createMockNode("parent");
 NodeList nodeList = createMockNodeList(new Node[0]);
 when(mockRoot.getChildNodes()).thenReturn(nodeList);

 List<Node> result = new OperationsHelperImpl().children(node);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testChildrenNull() {
 List<Node> result = new OperationsHelperImpl().children(null);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testFind() {
 List<Node> nodes = createMockNodes(10, "child");
 Node result = new OperationsHelperImpl().find(mockDocument, "/child/child");

 assertNull(result);

 result = new OperationsHelperImpl().find(mockDocument, "/child");
 assertEquals("child", result.getNodeName());
 }

 @Test
 public void testFindNoMatch() {
 List<Node> nodes = createMockNodes(10, "child");
 Node result = new OperationsHelperImpl().find(mockDocument, "/grandchild");

 assertNull(result);
 }

 @Test
 public void testFindRoot() {
 Node result = new OperationsHelperImpl().find(mockDocument, "/");

 assertEquals(mockRoot.getNodeName(), result.getNodeName());
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createMockNodes(10, "child");
 Node result = new OperationsHelperImpl().nodeWithName(nodes, "child");

 assertEquals("child", result.getNodeName());
 }

 @Test
 public void testNodeWithNameNull() {
 List<Node> nodes = createMockNodes(10, "child");
 Node result = new OperationsHelperImpl().nodeWithName(null, "child");

 assertNull(result);
 }

 @Test
 public void testNodeWithNameEmptyList() {
 Node result = new OperationsHelperImpl().nodeWithName(new ArrayList<>(), "child");

 assertNull(result);
 }

 @Test
 public void testNodeWithNameNoMatch() {
 List<Node> nodes = createMockNodes(10, "child");
 Node result = new OperationsHelperImpl().nodeWithName(nodes, "grandchild");

 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createMockNodes(10, "child");
 List<Node> result = new OperationsHelperImpl().sublistWithName(nodes, "child");

 assertEquals(10, result.size());
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> result = new OperationsHelperImpl().sublistWithName(new ArrayList<>(), "child");

 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithNameNull() {
 List<Node> result = new OperationsHelperImpl().sublistWithName(null, "child");

 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithNameNoMatch() {
 List<Node> nodes = createMockNodes(10, "child");
 List<Node> result = new OperationsHelperImpl().sublistWithName(nodes, "grandchild");

 assertTrue(result.isEmpty());
 }

 private Node createMockNode(String name) {
 Element element = mock(Element.class);
 when(element.getNodeName()).thenReturn(name);
 return element;
 }

 private NodeList createMockNodeList(Node... nodes) {
 NodeList nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn((short) nodes.length);
 for (int i = 0; i < nodes.length; i++) {
 when(nodeList.item(i)).thenReturn(nodes[i]);
 }
 return nodeList;
 }

 private List<Node> createMockNodes(int size, String name) {
 List<Node> nodes = new ArrayList<>();
 for (int i = 0; i < size; i++) {
 nodes.add(createMockNode(name));
 }
 return nodes;
 }
}