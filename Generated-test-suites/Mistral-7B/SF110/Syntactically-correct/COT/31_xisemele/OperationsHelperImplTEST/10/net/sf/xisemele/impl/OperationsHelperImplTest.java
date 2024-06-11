import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Document document = mock(Document.class);
 Node rootNode = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootNode);
 NodeList nodeList = mock(NodeList.class);
 when(rootNode.getChildNodes()).thenReturn(nodeList);
 Node childNode1 = mock(Node.class);
 Node childNode2 = mock(Node.class);
 when(nodeList.getLength()).thenReturn(2);
 when(nodeList.item(0)).thenReturn(childNode1);
 when(nodeList.item(1)).thenReturn(childNode2);
 when(childNode1.getNodeType()).thenReturn(1);
 when(childNode2.getNodeType()).thenReturn(1);

 List<Node> expectedChildren = new ArrayList<>();
 expectedChildren.add(childNode1);
 expectedChildren.add(childNode2);

 List<Node> actualChildren = operationsHelper.children(document);

 Assert.assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testChildren_EmptyList() {
 Document document = mock(Document.class);
 Node rootNode = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootNode);
 NodeList nodeList = mock(NodeList.class);
 when(rootNode.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getLength()).thenReturn(0);

 List<Node> expectedChildren = new ArrayList<>();

 List<Node> actualChildren = operationsHelper.children(document);

 Assert.assertEquals(expectedChildren, actualChildren);
 }

 @Test(expected = NullPointerException.class)
 public void testChildren_NullDocument() {
 Document document = null;

 List<Node> actualChildren = operationsHelper.children(document);
 }

 @Test
 public void testFind() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 Node childNode1 = mock(Node.class);
 Node childNode2 = mock(Node.class);
 Node childNode3 = mock(Node.class);
 when(rootElement.getNodeName()).thenReturn("root");
 when(childNode1.getNodeName()).thenReturn("child1");
 when(childNode2.getNodeName()).thenReturn("child2");
 when(childNode3.getNodeName()).thenReturn("child3");
 when(childNode1.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childNode2.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childNode3.getChildNodes()).thenReturn(mock(NodeList.class));

 Node expectedFindNode = childNode2;

 Node actualFindNode = operationsHelper.find(document, "root/child1/child2");

 Assert.assertEquals(expectedFindNode, actualFindNode);
 }

 @Test
 public void testFind_NullDocument() {
 Document document = null;

 Node actualFindNode = operationsHelper.find(document, "root/child1/child2");

 Assert.assertNull(actualFindNode);
 }

 @Test
 public void testFind_InvalidPath() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node actualFindNode = operationsHelper.find(document, "root/invalidPath");

 Assert.assertNull(actualFindNode);
 }

 @Test
 public void testNodeWithName() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 Node rootChild1 = mock(Node.class);
 Node rootChild2 = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getChildNodes()).thenReturn(new NodeList() {
 @Override public Node item(int index) {
 if (index == 0) {
 return rootChild1;
 } else {
 return rootChild2;
 }
 }

 @Override public int getLength() {
 return 2;
 }
 });
 when(rootChild1.getNodeName()).thenReturn("name1");
 when(rootChild2.getNodeName()).thenReturn("name2");

 Node expectedNode = rootChild1;

 Node actualNode = operationsHelper.nodeWithName(operationsHelper.children(document), "name1");

 Assert.assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithName_NullList() {
 List<Node> nodes = null;

 Node actualNode = operationsHelper.nodeWithName(nodes, "name1");

 Assert.assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_NullName() {
 List<Node> nodes = mock(List.class);
 when(nodes.size()).thenReturn(1);
 when(nodes.get(0).getNodeName()).thenReturn("name1");

 Node actualNode = operationsHelper.nodeWithName(nodes, null);

 Assert.assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 Node rootChild1 = mock(Node.class);
 Node rootChild2 = mock(Node.class);
 Node child1Child1 = mock(Node.class);
 Node child1Child2 = mock(Node.class);
 Node child2Child1 = mock(Node.class);
 Node child2Child2 = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getChildNodes()).thenReturn(new NodeList() {
 @Override public Node item(int index) {
 if (index == 0) {
 return rootChild1;
 } else {
 return rootChild2;
 }
 }

 @Override public int getLength() {
 return 2;
 }
 });
 when(rootChild1.getNodeName()).thenReturn("parent1");
 when(rootChild1.getChildNodes()).thenReturn(new NodeList() {
 @Override public Node item(int index) {
 if (index == 0) {
 return child1Child1;
 } else {
 return child1Child2;
 }
 }

 @Override public int getLength() {
 return 2;
 }
 });
 when(child1Child1.getNodeName()).thenReturn("child1");
 when(child1Child2.getNodeName()).thenReturn("child2");
 when(rootChild2.getNodeName()).thenReturn("parent2");
 when(rootChild2.getChildNodes()).thenReturn(new NodeList() {
 @Override public Node item(int index) {
 if (index == 0) {
 return child2Child1;
 } else {
 return child2Child2;
 }
 }

 @Override public int getLength() {
 return 2;
 }
 });
 when(child2Child1.getNodeName()).thenReturn("child3");
 when(child2Child2.getNodeName()).thenReturn("child4");

 List<Node> expectedSublist = new ArrayList<>();
 expectedSublist.add(child1Child1);
 expectedSublist.add(child1Child2);

 List<Node> actualSublist = operationsHelper.sublistWithName(operationsHelper.children(document), "parent1/child1");

 Assert.assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getChildNodes()).thenReturn(new NodeList());

 List<Node> actualSublist = operationsHelper.sublistWithName(operationsHelper.children(document), "name1");

 Assert.assertEquals(new ArrayList<>(), actualSublist);
 }

 @Test
 public void testSublistWithName_NullList() {
 List<Node> nodes = null;

 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "name1");

 Assert.assertEquals(new ArrayList<>(), actualSublist);
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> nodes = mock(List.class);
 when(nodes.size()).thenReturn(1);
 when(nodes.get(0).getNodeName()).thenReturn("name1");

 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, null);

 Assert.assertEquals(new ArrayList<>(), actualSublist);
 }
}