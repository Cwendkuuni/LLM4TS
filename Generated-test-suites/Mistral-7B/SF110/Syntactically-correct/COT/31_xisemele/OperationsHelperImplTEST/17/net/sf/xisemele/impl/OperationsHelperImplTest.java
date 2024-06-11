package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createMockNode();
 List<Node> children = helper.children(node);
 Node expectedFirstChild = createMockNode();
 expectedFirstChild.setNodeValue("firstChild");
 children.set(0, expectedFirstChild);
 Node expectedSecondChild = createMockNode();
 expectedSecondChild.setNodeValue("secondChild");
 children.add(expectedSecondChild);

 Assert.assertArrayEquals(children.toArray(), new Node[]{expectedFirstChild, expectedSecondChild});
 }

 @Test
 public void testChildrenWithEmptyNodeList() {
 Node node = createMockNode();
 List<Node> children = helper.children(node);
 Assert.assertEquals(0, children.size());
 }

 @Test
 public void testFind() {
 Document document = createMockDocument();
 String path = "/html/body";
 Node expectedResult = createMockNode();
 expectedResult.setNodeName("body");
 expectedResult.setNodeValue("body content");
 Node root = document.getDocumentElement();
 Node html = createMockNode();
 html.setNodeName("html");
 root.appendChild(html);
 Node body = createMockNode();
 body.setNodeName("body");
 html.appendChild(body);

 when(document.getDocumentElement().getNodeName()).thenReturn("html");
 when(html.getChildNodes().item(0).getNodeName()).thenReturn("head");
 when(html.getChildNodes().item(1).getNodeName()).thenReturn("body");

 Node result = helper.find(document, path);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testFindWithNullDocument() {
 Assert.assertNull(helper.find(null, ""));
 }

 @Test
 public void testFindWithNullPath() {
 Document document = createMockDocument();
 Assert.assertNull(helper.find(document, null));
 }

 @Test
 public void testFindWithEmptyPath() {
 Document document = createMockDocument();
 Assert.assertSame(document.getDocumentElement(), helper.find(document, ""));
 }

 @Test
 public void testFindWithInvalidPath() {
 Document document = createMockDocument();
 Assert.assertNull(helper.find(document, "/html/body/nonExistentNode"));
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(createMockNode());
 nodes.add(createMockNode());
 Node expectedNode = createMockNode();
 expectedNode.setNodeName("nameToFind");

 Node result = helper.nodeWithName(nodes, "nameToFind");
 Assert.assertSame(expectedNode, result);
 }

 @Test
 public void testNodeWithNameWithNullList() {
 Assert.assertNull(helper.nodeWithName(null, "name"));
 }

 @Test
 public void testNodeWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<>();
 Assert.assertNull(helper.nodeWithName(nodes, "name"));
 }

 @Test
 public void testNodeWithNameWithNullName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(createMockNode());
 Assert.assertNull(helper.nodeWithName(nodes, null));
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(createMockNode());
 nodes.add(createMockNode());
 nodes.add(createMockNode());
 List<Node> expectedSublist = new ArrayList<>();
 expectedSublist.add(nodes.get(1));

 List<Node> result = helper.sublistWithName(nodes, "nameOfSecondNode");
 Assert.assertArrayEquals(expectedSublist.toArray(), result.toArray());
 }

 @Test
 public void testSublistWithNameWithNullList() {
 Assert.assertNull(helper.sublistWithName(null, "name"));
 }

 @Test
 public void testSublistWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<>();
 Assert.assertEmpty(helper.sublistWithName(nodes, "name"));
 }

 @Test
 public void testSublistWithNameWithNullName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(createMockNode());
 Assert.assertEmpty(helper.sublistWithName(nodes, null));
 }

 private Node createMockNode() {
 Node mockNode = Mockito.mock(Node.class);
 return mockNode;
 }

 private Document createMockDocument() {
 Document mockDocument = Mockito.mock(Document.class);
 return mockDocument;
}

}