import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xisemele.impl.OperationsHelperImpl;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node testNode = createTestNode();
 List<Node> expectedChildren = Arrays.asList(createTestNode1(), createTestNode2());
 List<Node> actualChildren = operationsHelper.children(testNode);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testChildrenWithEmptyNodeList() {
 Node testNode = createTestNode();
 List<Node> actualChildren = operationsHelper.children(testNode);
 assertEquals(0, actualChildren.size());
 }

 @Test
 public void testChildrenWithSingleNode() {
 Node testNode = createTestNode();
 List<Node> expectedChildren = Arrays.asList(testNode);
 Node child = createTestNode1();
 testNode.appendChild(child);
 List<Node> actualChildren = operationsHelper.children(testNode);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testFind() {
 Document testDocument = createTestDocument();
 String testPath = "root/node1/node2";
 Node expectedNode = createTestNode3();
 testDocument.getDocumentElement().appendChild(expectedNode);
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testFindWithEmptyDocument() {
 Document testDocument = createTestDocument();
 String testPath = "root/node1/node2";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test
 public void testFindWithInvalidPath() {
 Document testDocument = createTestDocument();
 String testPath = "root/node1/node3";
 Node actualNode = operationsHelper.find(testDocument, testPath);
 assertNull(actualNode);
 }

 @Test
 public void testFindWithNullDocument() {
 Node actualNode = operationsHelper.find(null, "root/node1/node2");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName() {
 Node testNode = createTestNode();
 Node expectedNode = createTestNode1();
 testNode.setNodeName("node1");
 List<Node> nodes = Arrays.asList(testNode, expectedNode);
 Node actualNode = operationsHelper.nodeWithName(nodes, "node1");
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testNodeWithNameWithNullList() {
 Node actualNode = operationsHelper.nodeWithName(null, "node1");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithNameWithEmptyList() {
 List<Node> nodes = Collections.emptyList();
 Node actualNode = operationsHelper.nodeWithName(nodes, "node1");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithNameWithNullNodeName() {
 Node testNode = createTestNode();
 List<Node> nodes = Arrays.asList(testNode);
 String nullNodeName = null;
 Node actualNode = operationsHelper.nodeWithName(nodes, nullNodeName);
 assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 Node testNode = createTestNode();
 Node expectedNode1 = createTestNode1();
 Node expectedNode2 = createTestNode2();
 testNode.appendChild(expectedNode1);
 testNode.appendChild(expectedNode2);
 List<Node> nodes = Arrays.asList(testNode, createTestNode3());
 List<Node> expectedSublist = Arrays.asList(expectedNode1, expectedNode2);
 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "node1");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithNameWithNullList() {
 List<Node> nodes = null;
 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "node1");
 assertEquals(0, actualSublist.size());
 }

 @Test
 @ExpectedException(NullPointerException.class)
 public void testSublistWithNameWithNullNodeName() {
 Node testNode = createTestNode();
 List<Node> nodes = Arrays.asList(testNode);
 String nullNodeName = null;
 operationsHelper.sublistWithName(nodes, nullNodeName);
 }

 private Node createTestNode() {
 Element testNode = document.createElement("node");
 document.getDocumentElement().appendChild(testNode);
 return testNode;
 }

 private Node createTestNode1() {
 Element testNode1 = document.createElement("node1");
 document.getDocumentElement().appendChild(testNode1);
 return testNode1;
 }

 private Node createTestNode2() {
 Element testNode2 = document.createElement("node2");
 document.getDocumentElement().appendChild(testNode2);
 return testNode2;
 }

 private Node createTestNode3() {
 Element testNode3 = document.createElement("node3");
 document.getDocumentElement().appendChild(testNode3);
 return testNode3;
 }

 private Document createTestDocument() {
 Document testDocument = new DocumentImpl();
 return testDocument;
 }

}