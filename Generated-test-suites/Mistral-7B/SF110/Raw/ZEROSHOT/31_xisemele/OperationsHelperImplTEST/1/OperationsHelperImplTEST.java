package net.sf.xisemele.impl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setUp() {
 // Create a sample XML document for testing
 String xml = "<root><child1><child2>Test</child2></child1></root>";
 document = new org.apache.xerces.parsers.DOMParser().parseFromString(xml, null);
 operationsHelper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren() {
 Node root = document.getDocumentElement();
 List<Node> children = operationsHelper.children(root);
 List<String> expectedChildrenNames = new ArrayList<String>() {{
 add("child1");
 }};
 List<String> childrenNames = new ArrayList<String>();
 for (Node child : children) {
 childrenNames.add(child.getNodeName());
 }
 assertEquals(expectedChildrenNames, childrenNames);
 }

 @Test
 public void testFind() {
 Node child1 = operationsHelper.find(document, "root/child1");
 assertEquals("child1", child1.getNodeName());
 Node child2 = operationsHelper.find(document, "root/child1/child2");
 assertEquals("child2", child2.getNodeName());
 }

 @Test
 public void testFind_NonExistentPath() {
 assertNull(operationsHelper.find(document, "nonExistentPath"));
 }

 @Test
 public void testFind_EmptyDocument() {
 Document emptyDocument = new Document();
 assertNull(operationsHelper.find(emptyDocument, "root"));
 }

 @Test
 public void testNodeWithName() {
 List<Node> children = operationsHelper.children(document.getDocumentElement());
 Node child1 = operationsHelper.nodeWithName(children, "child1");
 assertEquals("child1", child1.getNodeName());
 }

 @Test
 public void testNodeWithName_NonExistentNode() {
 List<Node> children = operationsHelper.children(document.getDocumentElement());
 Node nonExistentNode = operationsHelper.nodeWithName(children, "nonExistentNode");
 assertNull(nonExistentNode);
 }

 @Test
 public void testSublistWithName() {
 List<Node> children = operationsHelper.children(document.getDocumentElement());
 List<Node> sublist = operationsHelper.sublistWithName(children, "child1");
 List<String> expectedSublistNames = new ArrayList<String>() {{
 add("child2");
 }};
 List<String> sublistNames = new ArrayList<String>();
 for (Node node : sublist) {
 sublistNames.add(node.getNodeName());
 }
 assertEquals(expectedSublistNames, sublistNames);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> emptyList = new ArrayList<>();
 List<Node> sublist = operationsHelper.sublistWithName(emptyList, "child1");
 assertEquals(0, sublist.size());
 }

 @Test
 public void testSublistWithName_NonExistentName() {
 List<Node> children = operationsHelper.children(document.getDocumentElement());
 List<Node> sublist = operationsHelper.sublistWithName(children, "nonExistentNode");
 assertEquals(0, sublist.size());
 }
}