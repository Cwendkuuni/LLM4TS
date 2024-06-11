package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {

 private static final String XML_FILE_PATH = "src/test/resources/yourTestXmlFile.xml"; // Replace with your test XML file path

 @Test
 public void testChildren() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 Node rootNode = document.getDocumentElement();
 List<Node> children = new OperationsHelperImpl().children(rootNode);
 assertEquals(5, children.size());
 }

 @Test
 public void testFind() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 List<Node> children = new OperationsHelperImpl().children(document.getDocumentElement());
 Node targetNode = new OperationsHelperImpl().find(document, "root/child1/grandChild");
 assertNotNull(targetNode);
 assertEquals("grandChild", targetNode.getNodeName());
 }

 @Test
 public void testFind_NullDocument() {
 assertNull(new OperationsHelperImpl().find(null, "path"));
 }

 @Test
 public void testFind_NullPath() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 assertNull(new OperationsHelperImpl().find(document, null));
 }

 @Test
 public void testFind_InvalidPath() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 assertNull(new OperationsHelperImpl().find(document, "notexist/path"));
 }

 @Test
 public void testNodeWithName() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 List<Node> rootChildren = new OperationsHelperImpl().children(document.getDocumentElement());
 Node targetNode = new OperationsHelperImpl().nodeWithName(rootChildren, "child2");
 assertNotNull(targetNode);
 assertEquals("child2", targetNode.getNodeName());
 }

 @Test
 public void testNodeWithName_NullList() {
 List<Node> emptyList = new ArrayList<>();
 assertNull(new OperationsHelperImpl().nodeWithName(emptyList, "nodeName"));
 }

 @Test
 public void testNodeWithName_NullName() {
 List<Node> rootChildren = new OperationsHelperImpl().children(document.getDocumentElement());
 assertNull(new OperationsHelperImpl().nodeWithName(rootChildren, null));
 }

 @Test
 public void testSublistWithName() {
 Document document = HelperMethods.loadDocument(XML_FILE_PATH);
 List<Node> rootChildren = new OperationsHelperImpl().children(document.getDocumentElement());
 List<Node> targetSublist = new OperationsHelperImpl().sublistWithName(rootChildren, "child2");
 assertEquals(1, targetSublist.size());
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> emptyList = new ArrayList<>();
 assertEquals(0, new OperationsHelperImpl().sublistWithName(emptyList, "nodeName").size());
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> rootChildren = new OperationsHelperImpl().children(document.getDocumentElement());
 assertEquals(0, new OperationsHelperImpl().sublistWithName(rootChildren, null).size());
 }
}