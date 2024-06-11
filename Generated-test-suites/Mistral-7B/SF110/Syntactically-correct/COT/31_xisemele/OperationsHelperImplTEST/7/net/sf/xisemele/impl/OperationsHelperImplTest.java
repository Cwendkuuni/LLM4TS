import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.ArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();
 private Document mockDocument;

 @Test
 public void testChildren() {
 mockDocument = mock(Document.class);
 final Node node = mock(Node.class);
 when(mockDocument.getDocumentElement()).thenReturn(node);
 when(node.getChildNodes()).thenReturn(mockNodeList(new Node[] {newElement("child1"), newElement("child2"), newElement("child3")}));

 final List<Node> result = operationsHelper.children(mockDocument);
 assertEquals(3, result.size());
 }

 @Test (expected = NullPointerException.class)
 public void testChildrenNullNode() {
 mockDocument = mock(Document.class);
 when(mockDocument.getDocumentElement()).thenReturn(null);
 operationsHelper.children(mockDocument);
 }

 @Test
 public void testFind() {
 final Document document = createDocument("root", new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 final String path = "root/child1/child3";
 final Node result = operationsHelper.find(document, path);
 final Node expected = document.getElementsByTagName("child3").item(0);
 assertEquals(expected, result);
 }

 @Test
 public void testFindNullDocument() {
 final String path = "root/child1/child3";
 operationsHelper.find(null, path);
 }

 @Test
 public void testFindNullPath() {
 final Document document = createDocument("root", new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 operationsHelper.find(document, null);
 }

 @Test
 public void testFindNonExistingPath() {
 final Document document = createDocument("root", new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 final String path = "root/child1/nonexistent";
 final Node result = operationsHelper.find(document, path);
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 final List<Node> nodes = createListOfNodes(new Element("parent", "parent"), new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 final String name = "child1";
 final Node result = operationsHelper.nodeWithName(nodes, name);
 final Node expected = nodes.get(1);
 assertEquals(expected, result);
 }

 @Test
 public void testNodeWithNameNullList() {
 final String name = "child1";
 operationsHelper.nodeWithName(null, name);
 }

 @Test
 public void testNodeWithNameNullName() {
 final List<Node> nodes = createListOfNodes(new Element("parent", "parent"), new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 operationsHelper.nodeWithName(nodes, null);
 }

 @Test
 public void testSublistWithName() {
 final List<Node> nodes = createListOfNodes(new Element("parent", "parent"), new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 final String name = "child1";
 final List<Node> result = operationsHelper.sublistWithName(nodes, name);
 final ArrayList<Node> expected = new ArrayList<Node>() {{ add(nodes.get(1)); }};
 assertEquals(expected, result);
 }

 @Test
 public void testSublistWithNameEmptyList() {
 final String name = "child1";
 final List<Node> nodes = new ArrayList<>();
 final List<Node> result = operationsHelper.sublistWithName(nodes, name);
 assertEmpty(result);
 }

 @Test
 public void testSublistWithNameNullName() {
 final List<Node> nodes = createListOfNodes(new Element("parent", "parent"), new Element("child1", "child1"), new Element("child2", "child2"), new Element("child3", "child3"));
 operationsHelper.sublistWithName(nodes, null);
 }

 private Document createDocument(String rootName, Node... childNodes) {
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
 } catch (DocumentBuilderException e) {
 e.printStackTrace();
 }
 Element rootElement = document.createElement(rootName);
 document.appendChild(rootElement);
 for (Node childNode : childNodes) {
 rootElement.appendChild(childNode);
 }
 return document;
 }

 private Node newElement(String tagName, String nodeName) {
 Element element = document.createElement(tagName);
 element.setNodeName(nodeName);
 return element;
 }

 private NodeList mockNodeList(Node... nodes) {
 NodeList nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn((short) nodes.length);
 for (int i = 0; i < nodes.length; i++) {
 when(nodeList.item(i)).thenReturn(nodes[i]);
 }
 return nodeList;
 }

 private void assertEquals(Node expected, Node result) {
 assertEquals(expected.getNodeName(), result.getNodeName());
 assertEquals(expected.getTextContent(), result.getTextContent());
 }

 private void assertEquals(ArrayList<Node> expected, List<Node> result) {
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i).getNodeName(), result.get(i).getNodeName());
 assertEquals(expected.get(i).getTextContent(), result.get(i).getTextContent());
 }
 }

 private void assertEmpty(List<Node> result) {
 assertEquals(0, result.size());
 }
}