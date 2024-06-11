import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestNode();
 List<Node> expectedChildren = Arrays.asList(createTestNode1(), createTestNode2());
 List<Node> actualChildren = helper.children(node);
 assertEquals(expectedChildren, actualChildren);
 }

 private Node createTestNode() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Element child1 = document.createElement("child1");
 Element child2 = document.createElement("child2");
 root.appendChild(child1);
 root.appendChild(child2);
 return child1;
 }

 private Node createTestNode1() {
 Element node = document.createElement("node1");
 return node;
 }

 private Node createTestNode2() {
 Element node = document.createElement("node2");
 return node;
 }

 private Document createTestDocument() {
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
 } catch (DocumentException e) {
 e.printStackTrace();
 }
 return document;
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenWithNullNode() {
 List<Node> actualChildren = helper.children(null);
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Element child1 = document.createElement("child1");
 Element child2 = document.createElement("child2");
 Element child3 = document.createElement("child3");
 root.appendChild(child1);
 root.appendChild(child2);
 root.appendChild(child3);

 Node actualNode = helper.find(document, "/root/child1");
 Node expectedNode = child1;
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testFindWithInvalidPath() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);

 Node actualNode = helper.find(document, "/root/nonExistentNode");
 assertNull(actualNode);
 }

 @Test(expected = NullPointerException.class)
 public void testFindWithNullDocument() {
 Node actualNode = helper.find(null, "/root/child1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithEmptyPath() {
 Node actualNode = helper.find(createTestDocument(), "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithInvalidPathFormat() {
 Node actualNode = helper.find(createTestDocument(), "/root/child1/");
 }

 @Test
 public void testNodeWithName() {
 Node nodeList = createTestNodeList();
 Node expectedNode = createTestNode1();
 Node actualNode = helper.nodeWithName(nodeList, "node1");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithNameWithNullList() {
 Node actualNode = helper.nodeWithName(null, "node1");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithNameWithNullName() {
 Node nodeList = createTestNodeList();
 String nullName = null;
 Node actualNode = helper.nodeWithName(nodeList, nullName);
 assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 Node nodeList = createTestNodeList();
 List<Node> expectedSublist = Arrays.asList(createTestNode1(), createTestNode2());
 List<Node> actualSublist = helper.sublistWithName(nodeList, "node1");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithNameWithNullList() {
 List<Node> actualSublist = helper.sublistWithName(null, "node1");
 assertTrue(actualSublist.isEmpty());
 }

 @Test
 public void testSublistWithNameWithEmptyList() {
 List<Node> actualSublist = helper.sublistWithName(createTestNodeList(), "");
 assertTrue(actualSublist.isEmpty());
 }

 @Test
 public void testSublistWithNameWithNullName() {
 List<Node> nodeList = createTestNodeList();
 String nullName = null;
 List<Node> actualSublist = helper.sublistWithName(nodeList, nullName);
 assertTrue(actualSublist.isEmpty());
 }

 private Node createTestNodeList() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Element child1 = document.createElement("child1");
 Element child2 = document.createElement("child2");
 root.appendChild(child1);
 root.appendChild(child2);
 NodeList nodeList = root.getChildNodes();
 return nodeList;
 }

}