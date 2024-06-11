package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private OperationsHelperImpl operationsHelper;
 private Document document;

 @Before
 public void setup() {
 operationsHelper = new OperationsHelperImpl();
 String xml = "<root><child1><child2/><child3><child4/></child3></child1></root>";
 document = new org.apache.xerces.parsers.DOMParser().parseFromString(xml, null);
 }

 @Test
 public void testChildren() {
 Node root = document.getDocumentElement();
 List<Node> children = operationsHelper.children(root);
 List<String> expected = Arrays.asList("child1", "child3");
 for (String name : expected) {
 assertTrue(operationsHelper.getNodeWithName(children, name) != null);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testChildrenNullNode() {
 operationsHelper.children(null);
 }

 @Test
 public void testFind() {
 Node child3 = operationsHelper.find(document, "root/child1/child3");
 assertNotNull(child3);
 assertEquals("child3", child3.getNodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullDocument() {
 operationsHelper.find(null, "root/child1/child3");
 }

 @Test(expected = NullPointerException.class)
 public void testFindNullPath() {
 operationsHelper.find(document, null);
 }

 @Test
 public void testFindNonExistentPath() {
 assertNull(operationsHelper.find(document, "root/child1/child5"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindEmptyDocument() {
 document = new Document();
 assertNull(operationsHelper.find(document, "root/child1/child3"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindEmptyPath() {
 assertNull(operationsHelper.find(document, ""));
 }

 @Test
 public void testNodeWithName() {
 List<Node> children = operationsHelper.children(root);
 Node child4 = operationsHelper.nodeWithName(children, "child4");
 assertNotNull(child4);
 assertEquals("child4", child4.getNodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNameNullList() {
 operationsHelper.nodeWithName(null, "child4");
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNameNullName() {
 operationsHelper.nodeWithName(children, null);
 }

 @Test
 public void testSublistWithName() {
 List<Node> children = operationsHelper.children(root);
 List<Node> sublist = operationsHelper.sublistWithName(children, "child3");
 List<String> expected = Arrays.asList("child4");
 for (String name : expected) {
 assertTrue(operationsHelper.getNodeWithName(sublist, name) != null);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSublistWithNameNullList() {
 operationsHelper.sublistWithName(null, "child3");
 }

 @Test(expected = NullPointerException.class)
 public void testSublistWithNameNullName() {
 operationsHelper.sublistWithName(children, null);
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> emptyList = new ArrayList<>();
 assertFalse(operationsHelper.sublistWithName(emptyList, "child3").isEmpty());
 }
}