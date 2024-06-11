import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> expectedChildren = Arrays.asList(createTestNode1(), createTestNode2(), createTestNode3());
 List<Node> actualChildren = helper.children(testNode);
 Assert.assertEquals(expectedChildren, actualChildren);
 }

 private Node createTestNode() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Node child1 = document.createElement("child1");
 root.appendChild(child1);
 Node child2 = document.createElement("child2");
 node.appendChild(child2);
 Node child3 = document.createElement("child3");
 child1.appendChild(child3);
 return child1;
 }

 private Node createTestNode1() {
 Element element = document.createElement("element1");
 return element;
 }

 private Node createTestNode2() {
 Text text = document.createTextNode("text2");
 return text;
 }

 private Node createTestNode3() {
 Element element = document.createElement("element3");
 return element;
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

 @Test
 public void testFind() {
 Document document = createTestDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 Element child1 = document.createElement("child1");
 root.appendChild(child1);
 Element child2 = document.createElement("child2");
 child1.appendChild(child2);
 Element child3 = document.createElement("child3");
 child2.appendChild(child3);

 List<String> path = Arrays.asList("root", "child1", "child2", "child3");
 Node expectedNode = child3;
 Node actualNode = helper.find(document, String.join("/", path.toArray(new String[0])));
 Assert.assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testFindNullDocument() {
 Node actualNode = helper.find(null, "path");
 Assert.assertNull(actualNode);
 }

 @Test
 public void testFindNullPath() {
 Document document = createTestDocument();
 Node actualNode = helper.find(document, null);
 Assert.assertNull(actualNode);
 }

 @Test
 public void testFindPathNotExist() {
 Document document = createTestDocument();
 List<String> path = Arrays.asList("root", "nonexistent");
 Node actualNode = helper.find(document, String.join("/", path.toArray(new String[0])));
 Assert.assertNull(actualNode);
 }

 @Test
 public void testNodeWithName() {
 Node nodeList = createTestNode().getChildNodes();
 List<Node> nodes = new ArrayList<>(nodeList);
 String name = "child1";
 Node expectedNode = nodeList.item(0);
 Node actualNode = helper.nodeWithName(nodes, name);
 Assert.assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = null;
 String name = "child1";
 Node actualNode = helper.nodeWithName(nodes, name);
 Assert.assertNull(actualNode);
 }

 @Test
 public void testNodeWithNameNullName() {
 List<Node> nodes = createTestNode().getChildNodes();
 String name = null;
 Node actualNode = helper.nodeWithName(nodes, name);
 Assert.assertNull(actualNode);
 }

 @Test
 public void testNodeWithNameNameNotFound() {
 List<Node> nodes = createTestNode().getChildNodes();
 String name = "nonexistent";
 Node actualNode = helper.nodeWithName(nodes, name);
 Assert.assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 Node nodeList = createTestNode().getChildNodes();
 List<Node> nodes = new ArrayList<>(nodeList);
 String name = "child1";
 List<Node> expectedSublist = Arrays.asList(nodeList.item(0));
 List<Node> actualSublist = helper.sublistWithName(nodes, name);
 Assert.assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<>();
 String name = "child1";
 List<Node> actualSublist = helper.sublistWithName(nodes, name);
 Assert.assertEquals(0, actualSublist.size());
 }

 @Test
 public void testSublistWithNameNullName() {
 List<Node> nodes = createTestNode().getChildNodes();
 String name = null;
 List<Node> actualSublist = helper.sublistWithName(nodes, name);
 Assert.assertEquals(0, actualSublist.size());
 }

 @Test
 public void testSublistWithNameNameNotFound() {
 List<Node> nodes = createTestNode().getChildNodes();
 String name = "nonexistent";
 List<Node> actualSublist = helper.sublistWithName(nodes, name);
 Assert.assertEquals(0, actualSublist.size());
 }
}