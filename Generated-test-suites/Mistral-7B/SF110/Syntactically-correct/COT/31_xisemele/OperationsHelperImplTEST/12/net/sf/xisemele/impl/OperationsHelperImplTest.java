import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestNode();
 List<Node> children = helper.children(node);
 List<Node> expectedChildren = Arrays.asList(node.getElementsByTagName("child1"), node.getElementsByTagName("child2"));
 assertThat(children, is(equalTo(expectedChildren)));
 }

 @Test
 public void testChildrenWhenNoChildNodes() {
 Node node = createTestNode();
 node.getChildNodes().clear();
 List<Node> children = helper.children(node);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildrenWhenNodeIsNull() {
 List<Node> children = helper.children(null);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testFind() {
 Document document = createTestDocument();
 String path = "/root/child1/child2";
 Node found = helper.find(document, path);
 Node expectedNode = document.getElementsByTagName("child2").item(0);
 assertThat(found, is(notNullValue()));
 assertThat(found, isInstanceOf(Element.class));
 assertThat(found.getNodeName(), is(equalTo("child2")));
 }

 @Test
 public void testFindWhenPathIsEmpty() {
 Document document = createTestDocument();
 Node found = helper.find(document, "");
 assertNull(found);
 }

 @Test
 public void testFindWhenPathDoesNotExist() {
 Document document = createTestDocument();
 String path = "/root/nonExistentNode";
 Node found = helper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testFindWhenDocumentIsNull() {
 String path = "/root/child1";
 Node found = helper.find(null, path);
 assertNull(found);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = Arrays.asList(createTestNode(), createTestNode2(), createTestNode3());
 Node node = helper.nodeWithName(nodes, "child1");
 Node expectedNode = createTestNode();
 assertThat(node, is(notNullValue()));
 assertThat(node, isInstanceOf(Element.class));
 assertThat(node.getNodeName(), is(equalTo("child1")));
 }

 @Test
 public void testNodeWithNameWhenNoMatchingNode() {
 List<Node> nodes = Arrays.asList(createTestNode(), createTestNode2(), createTestNode3());
 Node node = helper.nodeWithName(nodes, "child4");
 assertNull(node);
 }

 @Test
 public void testNodeWithNameWhenNodesIsNull() {
 Node node = helper.nodeWithName(null, "child1");
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = Arrays.asList(createTestNode1(), createTestNode2(), createTestNode3());
 List<Node> sublist = helper.sublistWithName(nodes, "child1");
 List<Node> expectedSublist = Arrays.asList(createTestNode1());
 assertThat(sublist, is(notNullValue()));
 assertThat(sublist, is(equalTo(expectedSublist)));
 }

 @Test
 public void testSublistWithNameWhenNoMatchingNodes() {
 List<Node> nodes = Arrays.asList(createTestNode1(), createTestNode2(), createTestNode3());
 List<Node> sublist = helper.sublistWithName(nodes, "child4");
 assertTrue(sublist.isEmpty());
 }

 @Test
 public void testSublistWithNameWhenNodesIsNull() {
 List<Node> sublist = helper.sublistWithName(null, "child1");
 assertTrue(sublist.isEmpty());
 }

 private Node createTestNode() {
 Element element = document.createElement("child1");
 element.setAttribute("name", "child1");
 element.setTextContent("Child 1");
 Node node = document.createElement("root");
 node.appendChild(element);
 return node;
 }

 private Node createTestNode1() {
 Element element = document.createElement("root");
 element.setAttribute("name", "root");
 Node node = document.createElement("child1");
 element.appendChild(node);
 return node;
 }

 private Node createTestNode2() {
 Element element = document.createElement("child2");
 element.setAttribute("name", "child2");
 Node node = document.createElement("root");
 node.appendChild(element);
 return node;
 }

 private Node createTestNode3() {
 Element element = document.createElement("child3");
 element.setAttribute("name", "child3");
 Node node = document.createElement("root");
 node.appendChild(element);
 return node;
 }

 private Document createTestDocument() {
 Document document = document.createDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 return document;
 }
}