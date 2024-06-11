package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() throws Exception {
 final String xml = "<root><child1><child2></child2></child1></root>";
 Document document = getDocumentFromString(xml);
 Node root = document.getRootElement();
 List<Node> children = operationsHelper.children(root);
 List<String> expected = new ArrayList<>();
 expected.add("child1");
 expected.add("child2");
 for (Node node : children) {
 assertEquals(expected.remove(0), node.getNodeName());
 }
 assertFalse(expected.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChildrenNullNode() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 List<Node> children = operationsHelper.children(null);
 }

 @Test
 public void testFind() throws Exception {
 final String xml = "<root><child1><grandchild></grandchild></child1></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 Node result = operationsHelper.find(document, "root/child1/grandchild");
 Node expected = children.get(0).getChildren().get(0);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullDocument() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 Node result = operationsHelper.find(null, "root/child1/grandchild");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNullPath() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 Document document = getDocumentFromString("<root></root>");
 Node result = operationsHelper.find(document, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindInvalidPath() throws Exception {
 final String xml = "<root><child1></child1></root>";
 Document document = getDocumentFromString(xml);
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 Node result = operationsHelper.find(document, "root/child2/grandchild");
 }

 @Test
 public void testNodeWithName() throws Exception {
 final String xml = "<root><child1><grandchild></grandchild></child1></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 Node result = operationsHelper.nodeWithName(children, "child1");
 Node expected = document.getRootElement();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeWithNameNullList() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 Node result = operationsHelper.nodeWithName(null, "child1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeWithNameInvalidName() throws Exception {
 final String xml = "<root><child1></child1></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 Node result = operationsHelper.nodeWithName(children, "grandchild");
 }

 @Test
 public void testSublistWithName() throws Exception {
 final String xml = "<root><child1><grandchild1></grandchild1><grandchild2></grandchild2></child1></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 List<Node> result = operationsHelper.sublistWithName(children, "grandchild1");
 List<String> expected = new ArrayList<>();
 expected.add("grandchild1");
 for (Node node : result) {
 assertEquals(expected.remove(0), node.getNodeName());
 }
 assertFalse(expected.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameNullList() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();List<Node> result = operationsHelper.sublistWithName(null, "grandchild1");
 }

 @Test
 public void testSublistWithNameEmptyList() throws Exception {
 final String xml = "<root></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 List<Node> result = operationsHelper.sublistWithName(children, "grandchild1");
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSublistWithNameInvalidName() throws Exception {
 final String xml = "<root><child1><grandchild1></grandchild1><grandchild2></grandchild2></child1></root>";
 Document document = getDocumentFromString(xml);
 List<Node> children = operationsHelper.children(document.getRootElement());
 List<Node> result = operationsHelper.sublistWithName(children, "child1");
 }

 private Document getDocumentFromString(String xml) throws JDOMException, IOException {
 SAXBuilder saxBuilder = new SAXBuilder();
 return saxBuilder.build(new StringReader(xml));
 }

}