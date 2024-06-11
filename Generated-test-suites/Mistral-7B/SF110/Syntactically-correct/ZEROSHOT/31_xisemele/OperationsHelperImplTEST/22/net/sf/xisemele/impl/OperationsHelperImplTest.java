package net.sf.xisemele.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.w3c.dom.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper;
 private Document document;

 @Before
 public void setup() {
 document = Mockito.mock(Document.class);
 operationsHelper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren_EmptyNodeList() {
 Node emptyNode = Mockito.mock(Node.class);
 when(emptyNode.getChildNodes()).thenReturn(new NodeList() {
 @Override
 public int getLength() {
 return 0;
 }
 });
 Node parentNode = Mockito.mock(Node.class);
 when(parentNode.getChildNodes()).thenReturn(Arrays.asList(emptyNode).iterator().next());

 List<Node> result = operationsHelper.children(parentNode);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testChildren_SingleChild() {
 Node childNode = Mockito.mock(Element.class);
 when(childNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 Node parentNode = Mockito.mock(Node.class);
 when(parentNode.getChildNodes()).thenReturn(Arrays.asList(childNode).iterator().next());

 List<Node> result = operationsHelper.children(parentNode);
 assertEquals(1, result.size());
 assertEquals(childNode, result.get(0));
 }

 @Test
 public void testChildren_MultipleChildren() {
 Node childNode1 = Mockito.mock(Element.class);
 Node childNode2 = Mockito.mock(Element.class);
 Node parentNode = Mockito.mock(Node.class);
 when(parentNode.getChildNodes()).thenReturn(Arrays.asList(childNode1, childNode2).iterator().asList().iterator());

 List<Node> result = operationsHelper.children(parentNode);
 assertEquals(2, result.size());
 assertTrue(result.contains(childNode1));
 assertTrue(result.contains(childNode2));
 }

 @Test
 public void testFind_ExactMatch() {
 String path = "/rootElement/childElement";
 Node rootElement = Mockito.mock(Element.class);
 Node childElement = Mockito.mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("rootElement");
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getChildNodes()).thenReturn(Arrays.asList(childElement).iterator().next());

 Node result = operationsHelper.find(document, path);
 assertEquals(childElement, result);
 }

 @Test
 public void testFind_NoMatch() {
 String path = "/rootElement/childElement/grandChildElement";
 Node rootElement = Mockito.mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("rootElement");
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node result = operationsHelper.find(document, path);
 assertNull(result);
 }

 @Test
 public void testFind_PartialMatch() {
 String path = "/rootElement/childElement/";
 Node rootElement = Mockito.mock(Element.class);
 Element childElement = Mockito.mock(Element.class);
 when(rootElement.getNodeName()).thenReturn("rootElement");
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getChildNodes()).thenReturn(Arrays.asList(childElement).iterator().next());

 Node childOfChildElement = Mockito.mock(Element.class);
 when(childElement.getChildNodes()).thenReturn(Arrays.asList(childOfChildElement).iterator().next());

 Node result = operationsHelper.find(document, path);
 assertNull(result);
 }

 @Test
 public void testFind_RootMatch() {
 String path = "/";
 Node rootElement = Mockito.mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node result = operationsHelper.find(document, path);
 assertEquals(rootElement, result);
 }

 @Test
 public void testNodeWithName_EmptyList() {
 List<Node> emptyList = new ArrayList<>();
 String name = "childElement";

 Node result = operationsHelper.nodeWithName(emptyList, name);
 assertNull(result);
 }

 @Test
 public void testNodeWithName_SingleMatch() {
 List<Node> nodes = Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class));
 String name = "childElement";
 Node result = operationsHelper.nodeWithName(nodes, name);
 assertTrue(result instanceof Element);
 assertEquals(name, result.getNodeName());
 }

 @Test
 public void testNodeWithName_NoMatch() {
 List<Node> nodes = Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class));
 String name = "otherName";
 Node result = operationsHelper.nodeWithName(nodes, name);
 assertNull(result);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> emptyList = new ArrayList<>();
 String name = "childElement";

 List<Node> result = operationsHelper.sublistWithName(emptyList, name);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSublistWithName_SingleMatch() {
 List<Node> nodes = Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class));
 String name = "childElement";
 List<Node> result = operationsHelper.sublistWithName(nodes, name);
 assertEquals(1, result.size());
 assertTrue(result.get(0) instanceof Element);
 assertEquals(name, result.get(0).getNodeName());
 }

 @Test
 public void testSublistWithName_MultipleMatches() {
 List<Node> nodes = Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class));
 String name = "childElement";
 List<Node> result = operationsHelper.sublistWithName(nodes, name);
 assertEquals(2, result.size());
 }

 @Test
 public void testSublistWithName_NoMatch() {
 List<Node> nodes = Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class), Mockito.mock(Element.class));
 String name = "otherName";
 List<Node> result = operationsHelper.sublistWithName(nodes, name);
 assertTrue(result.isEmpty());
 }
}