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
 NodeList nodeList = mock(NodeList.class);
 when(document.getDocumentElement()).thenReturn(rootNode);
 when(rootNode.getChildNodes()).thenReturn(nodeList);
 Node childNode1 = mock(Node.class);
 Node childNode2 = mock(Node.class);
 when(nodeList.getItem(0)).thenReturn(childNode1);
 when(nodeList.getItem(1)).thenReturn(childNode2);

 List<Node> expectedChildren = new ArrayList<>();
 expectedChildren.add(childNode1);
 expectedChildren.add(childNode2);

 List<Node> result = operationsHelper.children(document);

 Assert.assertNotNull(result);
 Assert.assertEquals(expectedChildren, result);
 }

 @Test
 public void testChildrenWhenEmpty() {
 Document document = mock(Document.class);
 Node rootNode = mock(Node.class);
 NodeList nodeList = mock(NodeList.class);
 when(document.getDocumentElement()).thenReturn(rootNode);
 when(rootNode.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getLength()).thenReturn(0);

 List<Node> result = operationsHelper.children(document);

 Assert.assertNotNull(result);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testChildrenWhenNoElementNode() {
 Document document = mock(Document.class);
 Node rootNode = mock(Node.class);
 NodeList nodeList = mock(NodeList.class);
 when(document.getDocumentElement()).thenReturn(rootNode);
 when(rootNode.getChildNodes()).thenReturn(nodeList);
 when(nodeList.getItem(0)).thenReturn(mock(Text.class));

 List<Node> result = operationsHelper.children(document);

 Assert.assertNotNull(result);
 Assert.assertEquals(0, result.size());
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenWithNullDocument() {
 List<Node> result = operationsHelper.children(null);
 }

 @Test
 public void testFind() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 Node childElement1 = mock(Node.class);
 Node childElement2 = mock(Node.class);
 Node leafNode = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getNodeName()).thenReturn("root");
 when(rootElement.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement1.getNodeName()).thenReturn("child1");
 when(childElement1.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement2.getNodeName()).thenReturn("child2");
 when(childElement2.getChildNodes()).thenReturn(mock(NodeList.class));
 when(leafNode.getNodeName()).thenReturn("leaf");
 when(childElement1.getChildNodes().getItem(0)).thenReturn(leafNode);

 List<String> path = Arrays.asList("root", "child1", "leaf");
 Node expectedResult = leafNode;

 Node result = operationsHelper.find(document, String.join("/", path.subList(0, path.size() - 1)));

 Assert.assertNotNull(result);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindWhenPathIsEmpty() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 List<String> path = Collections.emptyList();
 Node result = operationsHelper.find(document, String.join("/", path));

 Assert.assertNull(result);
 }

 @Test
 public void testFindWhenPathIsSingleElement() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 Node childElement = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getNodeName()).thenReturn("root");
 when(rootElement.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement.getNodeName()).thenReturn("child");
 when(childElement.getChildNodes()).thenReturn(mock(NodeList.class));
 when(rootElement.getChildNodes().getItem(0)).thenReturn(childElement);

 List<String> path = Arrays.asList("root", "child");
 Node expectedResult = childElement;

 Node result = operationsHelper.find(document, String.join("/", path.subList(0, path.size())));

 Assert.assertNotNull(result);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindWhenPathDoesNotExist() {
 Document document = mock(Document.class);
 Node rootElement = mock(Node.class);
 Node childElement1 = mock(Node.class);
 Node childElement2 = mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getNodeName()).thenReturn("root");
 when(rootElement.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement1.getNodeName()).thenReturn("child1");
 when(childElement1.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement1.getChildNodes().getItem(0)).thenReturn(childElement2);
 when(childElement2.getNodeName()).thenReturn("child2");
 when(childElement2.getChildNodes()).thenReturn(mock(NodeList.class));
 when(childElement2.getChildNodes().getItem(0)).thenReturn(mock(Text.class));

 List<String> path = Arrays.asList("root", "child1", "child3");
 Node result = operationsHelper.find(document, String.join("/", path.subList(0, path.size())));

 Assert.assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testFindWithNullDocument() {
 operationsHelper.find(null, "");
 }

 @Test(expected = NullPointerException.class)
 public void testFindWithNullPath() {
 operationsHelper.find(mock(Document.class), null);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
 Node expectedResult = nodes.get(1);

 Node result = operationsHelper.nodeWithName(nodes, "name1");

 Assert.assertNull(result);

 result = operationsHelper.nodeWithName(nodes, "name2");

 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testNodeWithNameWhenNullList() {
 Node result = operationsHelper.nodeWithName(null, "name");

 Assert.assertNull(result);
 }

 @Test
 public void testNodeWithNameWhenNameDoesNotExist() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

 Node result = operationsHelper.nodeWithName(nodes, "nonExistentName");

 Assert.assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
 Node expectedFirstResult = nodes.get(0);
 Node expectedSecondResult = nodes.get(1);

 List<Node> result1 = operationsHelper.sublistWithName(nodes, "name1");

 Assert.assertNotNull(result1);
 Assert.assertEquals(1, result1.size());
 Assert.assertEquals(expectedFirstResult, result1.get(0));

 List<Node> result2 = operationsHelper.sublistWithName(nodes, "name2");

 Assert.assertNotNull(result2);
 Assert.assertEquals(1, result2.size());
 Assert.assertEquals(expectedSecondResult, result2.get(0));
 }

 @Test
 public void testSublistWithNameWhenNullList() {
 List<Node> result = operationsHelper.sublistWithName(null, "name");

 Assert.assertNotNull(result);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testSublistWithNameWhenNameDoesNotExist() {
 List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));

 List<Node> result = operationsHelper.sublistWithName(nodes, "nonExistentName");

 Assert.assertNotNull(result);
 Assert.assertEquals(0, result.size());
 }

}