package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;

/**
 * Test class for OperationsHelperImpl
 */
public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 this.operationsHelper = new OperationsHelperImpl();
 }

 // Test children method
 @Test
 public void testChildren() {
 Document document = createMockDocument();
 Node root = document.getDocumentElement();
 Node child1 = createMockElement("child1");
 Node child2 = createMockElement("child2");
 Node child3 = createMockElement("child3");
 Node sibling1 = createMockElement("sibling1");
 Node sibling2 = createMockElement("sibling2");

 root.appendChild(child1);
 root.appendChild(child2);
 child1.appendChild(child3);
 child1.appendChild(sibling1);
 child2.appendChild(sibling2);

 List<Node> children = operationsHelper.children(root);
 List<Node> expectedChildren = Arrays.asList(child1, child2);
 assertEquals(expectedChildren, children);
 }

 // Test children method with empty NodeList
 @Test
 public void testChildrenEmptyNodeList() {
 Document document = createMockDocument();
 Node root = document.getDocumentElement();

 List<Node> children = operationsHelper.children(root);
 assertTrue(children.isEmpty());
 }

 // Test find method
 @Test
 public void testFind() {
 Document document = createMockDocument();
 Element root = document.getDocumentElement();
 Element child1 = createMockElement("child1");
 Element child2 = createMockElement("child2");
 Element target = createMockElement("target");
 Element grandChild = createMockElement("grandChild");

 root.appendChild(child1);
 child1.appendChild(target);
 target.appendChild(grandChild);

 Node result = operationsHelper.find(document, "child1/target");
 assertEquals(target, result);
 }

 // Test find method with empty document
 @Test(expected = NullPointerException.class)
 public void testFindEmptyDocument() {
 Document document = null;
 Node result = operationsHelper.find(document, "root/child");
 }

 // Test find method with wrong path
 @Test
 public void testFindWrongPath() {
 Document document = createMockDocument();
 Element root = document.getDocumentElement();

 Node result = operationsHelper.find(document, "nonExistentPath");
 assertNull(result);
 }

 // Test nodeWithName method
 @Test
 public void testNodeWithName() {
 Document document = createMockDocument();
 Element root = document.getDocumentElement();
 Element child1 = createMockElement("child1");
 Element child2 = createMockElement("child2");

 root.appendChild(child1);
 root.appendChild(child2);

 Node result = operationsHelper.nodeWithName(Arrays.asList(root.getChildNodes()), "child1");
 assertEquals(child1, result);
 }

 // Test nodeWithName method with empty list
 @Test
 public void testNodeWithNameEmptyList() {
 Document document = createMockDocument();
 Node result = operationsHelper.nodeWithName(Arrays.asList(), "child");
 assertNull(result);
 }

 // Test nodeWithName method with null list
 @Test
 public void testNodeWithNameNullList() {
 Node result = operationsHelper.nodeWithName(null, "child");
 assertNull(result);
 }

 // Test sublistWithName method
 @Test
 public void testSublistWithName() {
 Document document = createMockDocument();
 Element root = document.getDocumentElement();
 Element child1 = createMockElement("child1");
 Element child2 = createMockElement("child2");
 Element target = createMockElement("target");
 Element grandChild = createMockElement("grandChild");

 root.appendChild(child1);
 child1.appendChild(target);
 target.appendChild(grandChild);

 List<Node> nodes = Arrays.asList(root.getChildNodes());
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
 List<Node> expectedSublist = Arrays.asList(target);
 assertEquals(expectedSublist, sublist);
 }

 // Test sublistWithName method with empty list
 @Test
 public void testSublistWithNameEmptyList() {
 Document document = createMockDocument();
 List<Node> nodes = Arrays.asList();
 List<Node> sublist = operationsHelper.sublistWithName(nodes, "child");
 assertTrue(sublist.isEmpty());
 }

 // Test sublistWithName method with null list
 @Test
 public void testSublistWithNameNullList() {
 List<Node> sublist = operationsHelper.sublistWithName(null, "child");
 assertTrue(sublist.isEmpty());
 }

 private Document createMockDocument() {
 Document document = Mockito.mock(Document.class);
 return document;
 }

 private Element createMockElement(String name) {
 Element element = Mockito.mock(Element.class, Mockito.withSettings().name(name));
 return element;
 }

}