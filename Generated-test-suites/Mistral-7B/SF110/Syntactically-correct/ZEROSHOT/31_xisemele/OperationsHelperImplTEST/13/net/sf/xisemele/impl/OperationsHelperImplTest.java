package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setup() {
 OperationsHelper operationsHelper = new OperationsHelperImpl();
 this.operationsHelper = mock(OperationsHelper.class);
 when(operationsHelper.children(Mockito.any(Node.class))).thenCallRealMethod();
 when(operationsHelper.find(Mockito.any(Document.class), Mockito.anyString())).thenCallRealMethod();
 when(operationsHelper.nodeWithName(Mockito.anyList(), Mockito.anyString())).thenCallRealMethod();
 when(operationsHelper.sublistWithName(Mockito.anyList(), Mockito.anyString())).thenCallRealMethod();
 this.document = createTestDocument();
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testChildren() throws JDOMException {
 Node root = new Element("root");
 document.setDocumentElement(root);
 Node element1 = new Element("element1");
 Node element2 = new Element("element2");
 Node textNode = new Text("text");
 element1.addContent(textNode);
 root.addContent(element1);
 root.addContent(element2);

 List<Node> nodes = operationsHelper.children(document.getRootElement());
 List<Node> expected = new ArrayList<>(Arrays.asList(element1, element2));
 assertEquals(expected, nodes);
 }

 @Test
 public void testChildren_EmptyList() throws JDOMException {
 Node root = new Element("root");
 document.setDocumentElement(root);

 List<Node> nodes = operationsHelper.children(document.getRootElement());
 assertEquals(0, nodes.size());
 }

 @Test
 public void testFind() throws JDOMException {
 Element root = (Element) document.getDocumentElement();
 Element element1 = new Element("element1");
 Node element2 = new Element("element2");
 element1.addContent(element2);
 root.addContent(element1);

 Node found = operationsHelper.find(document, "element1/element2");
 assertSame(element2, found);
 }

 @Test
 public void testFind_NullDocument() {
 Node found = operationsHelper.find(null, "root/element1");
 assertNull(found);
 }

 @Test
 public void testFind_InvalidPath() throws JDOMException {
 Node found = operationsHelper.find(document, "root/root");
 assertNull(found);
 }

 @Test
 public void testFind_NotLastElement() throws JDOMException {
 Element root = (Element) document.getDocumentElement();
 Element element1 = new Element("element1");
 Node element2 = new Element("element2");
 Node element3 = new Element("element3");
 element1.addContent(element2);
 element2.addContent(element3);
 root.addContent(element1);

 Node found = operationsHelper.find(document, "element1/element2");
 assertSame(element2, found);
 }

 @Test
 public void testFind_TraversesParents() throws JDOMException {
 Element root = (Element) document.getDocumentElement();
 Element element1 = new Element("element1");
 Node element2 = new Element("element2");
 Element element3 = new Element("element3");
 element1.addContent(element2);
 element2.addContent(element3);
 root.addContent(element1);

 Node found = operationsHelper.find(document, "root/element1/element2/element3");
 assertSame(element3, found);
 }

 @Test
 public void testFind_NullName() {
 Node found = operationsHelper.find(document, null);
 assertNull(found);
 }

 @Test
 public void testNodeWithName() throws JDOMException {
 List<Node> nodes = Arrays.asList(document.getRootElement().getChildren().toArray());
 Node expected = nodes.get(0);
 Node found = operationsHelper.nodeWithName(nodes, "root");
 assertSame(expected, found);
 }

 @Test
 public void testNodeWithName_NullList() {
 Node found = operationsHelper.nodeWithName(null, "root");
 assertNull(found);
 }

 @Test
 public void testNodeWithName_NullName() {
 Node found = operationsHelper.nodeWithName(Arrays.asList(document.getRootElement().getChildren().toArray()), null);
 assertNull(found);
 }

 @Test
 public void testNodeWithName_NotPresent() throws JDOMException {
 List<Node> nodes = Arrays.asList(document.getRootElement().getChildren().toArray());
 Node found = operationsHelper.nodeWithName(nodes, "notPresent");
 assertNull(found);
 }

 @Test
 public void testSublistWithName() throws JDOMException {
 List<Node> nodes = Arrays.asList(document.getRootElement().getChildren().toArray());
 List<Node> expected = Arrays.asList(nodes.get(0));
 List<Node> found = operationsHelper.sublistWithName(nodes, "root");
 assertEquals(expected, found);
 }

 @Test
 public void testSublistWithName_EmptyList() throws JDOMException {
 List<Node> nodes = new ArrayList<>();
 List<Node> found = operationsHelper.sublistWithName(nodes, "root");
 assertEquals(0, found.size());
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> found = operationsHelper.sublistWithName(Arrays.asList(document.getRootElement().getChildren().toArray()), null);
 assertEquals(0, found.size());
 }

 @Test
 public void testSublistWithName_NullList() {
 List<Node> found = operationsHelper.sublistWithName(null, "root");
 assertEquals(0, found.size());
 }

 @Test
 public void testSublistWithName_NotPresent() throws JDOMException {
 List<Node> nodes = Arrays.asList(document.getRootElement().getChildren().toArray());
 List<Node> found = operationsHelper.sublistWithName(nodes, "notPresent");
 assertEquals(0, found.size());
 }

 private Document createTestDocument() throws JDOMException {
 return documentFromString("<root><element1><element2></element2></element1></root>");
 }

}