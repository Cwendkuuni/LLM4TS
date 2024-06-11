import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OperationsHelperImplTest {

 private Document doc;
 private OperationsHelper helper;

 @Before
 public void setUp() {
 doc = createSampleDocument();
 helper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren_EmptyNodeList() {
 Node emptyNode = createEmptyNode();
 List<Node> children = helper.children(emptyNode);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testChildren_SingleChildNode() {
 Node childNode = createSampleChildNode();
 List<Node> children = helper.children(childNode);
 assertEquals(1, children.size());
 assertSame(childNode, children.get(0));
 }

 @Test
 public void testChildren_MultipleChildNodes() {
 Node parentNode = createSampleParentNode();
 List<Node> children = helper.children(parentNode);
 List<String> expectedChildrenNames = Arrays.asList("child1", "child2", "child3");
 List<Node> actualChildren = new ArrayList<>();
 for (Node node : children) {
 actualChildren.add(nodeWithName(node, "child" + (getChildIndex(node, parentNode) + 1)));
 }
 assertEquals(expectedChildrenNames, actualChildren);
 }

 @Test(expected = NullPointerException.class)
 public void testChildren_NullNode() {
 helper.children(null);
 }

 @Test
 public void testFind_EmptyDocument() {
 Document emptyDoc = createEmptyDocument();
 assertNull(helper.find(emptyDoc, "/"));
 }

 @Test
 public void testFind_RootNode() {
 Node rootNode = doc.getDocumentElement();
 assertSame(rootNode, helper.find(doc, "/"));
 }

 @Test
 public void testFind_SingleLevelPath() {
 String path = "/child1";
 Node expectedNode = nodeWithName(doc.getDocumentElement(), "child1");
 Node actualNode = helper.find(doc, path);
 assertSame(expectedNode, actualNode);
 }

 @Test
 public void testFind_MultiLevelPath() {
 String path = "/child1/child2/child3";
 Node expectedNode = nodeWithName(nodeWithName(nodeWithName(doc.getDocumentElement(), "child1"), "child2"), "child3");
 Node actualNode = helper.find(doc, path);
 assertSame(expectedNode, actualNode);
 }

 @Test
 public void testFind_NonExistentPath() {
 assertNull(helper.find(doc, "/nonExistentNode"));
 }

 @Test
 public void testFind_NullDocument() {
 assertNull(helper.find(null, "/"));
 }

 @Test
 public void testNodeWithName_NullList() {
 List<Node> emptyList = Collections.emptyList();
 assertNull(helper.nodeWithName(emptyList, "nodeName"));
 }

 @Test
 public void testNodeWithName_EmptyList() {
 List<Node> emptyList = new ArrayList<>();
 assertNull(helper.nodeWithName(emptyList, "nodeName"));
 }

 @Test
 public void testNodeWithName_SingleNodeMatch() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"));
 Node expectedNode = nodeWithName(nodes, "child1");
 Node actualNode = helper.nodeWithName(nodes, "child1");
 assertSame(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithName_NoMatch() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"));
 Node expectedNode = null;
 Node actualNode = helper.nodeWithName(nodes, "child3");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_NullListAndName() {
 assertNull(helper.nodeWithName(null, "nodeName"));
 }

 @Test
 public void testNodeWithName_NullName() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"));
 assertNull(helper.nodeWithName(nodes, null));
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> emptyList = Collections.emptyList();
 List<Node> expectedSublist = Collections.emptyList();
 List<Node> actualSublist = helper.sublistWithName(emptyList, "nodeName");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_EmptySublist() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"));
 List<Node> expectedSublist = Collections.emptyList();
 List<Node> actualSublist = helper.sublistWithName(nodes, "nodeNameUnknown");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_SingleMatch() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"), nodeWithName(doc.getDocumentElement(), "child1"));
 List<Node> expectedSublist = Arrays.asList(nodeWithName(nodes, "child1"));
 List<Node> actualSublist = helper.sublistWithName(nodes, "child1");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_MultipleMatches() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"), nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child1"));
 List<Node> expectedSublist = Arrays.asList(nodeWithName(nodes, "child1"), nodeWithName(nodes, "child1"));
 List<Node> actualSublist = helper.sublistWithName(nodes, "child1");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_NullList() {
 assertNull(helper.sublistWithName(null, "nodeName"));
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> nodes = Arrays.asList(nodeWithName(doc.getDocumentElement(), "child1"), nodeWithName(doc.getDocumentElement(), "child2"));
 assertNull(helper.sublistWithName(nodes, null));
 }

 private Document createSampleDocument() {
 Element docElement = doc.createElement("root");
 docElement.setAttribute("data", "data");
 Element child1 = doc.createElement("child1");
 child1.setAttribute("data", "data1");
 Element child2 = doc.createElement("child2");
 child2.setAttribute("data", "data2");
 Element child3 = doc.createElement("child3");
 child3.setAttribute("data", "data3");
 docElement.appendChild(child1);
 docElement.appendChild(child2);
 docElement.appendChild(child3);
 Document document = doc.createDocument();
 document.appendChild(docElement);
 return document;
 }

 private Node createEmptyNode() {
 Document document = doc.createDocument();
 return document.createProcessingInstruction("xml", "");
 }

 private Node createSampleParentNode() {
 Document document = doc.createDocument();
 Element parentNode = document.createElement("parent");
 document.appendChild(parentNode);
 return parentNode;
 }

 private Node createSampleChildNode() {
 Document document = doc.createDocument();
 Element childNode = document.createElement("child");
 document.appendChild(childNode);
 return childNode;
 }

 private Node nodeWithName(Node node, String name) {
 if (node == null) {
 throw new NullPointerException("node cannot be null");
 }
 if (!node.getNodeName().equals(name)) {
 throw new IllegalArgumentException("node does not have name: " + name);
 }
 return node;
 }

 private int getChildIndex(Node node, Node parent) {
 for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
 if (parent.getChildNodes().item(i).equals(node)) {
 return i;
 }
 }
 throw new IllegalStateException("node not a child of parent");
 }
}