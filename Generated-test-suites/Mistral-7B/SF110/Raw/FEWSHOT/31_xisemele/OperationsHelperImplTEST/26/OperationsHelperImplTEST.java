package net.sf.xisemele.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Assert;
import org.mockito.Mockito;

public class OperationsHelperImplTest {

 @Test
 public void testChildren_EmptyNode() {
 List<Node> children = OperationsHelperImpl.getInstance().children(null);
 Assert.assertThat(children, is(Collections.emptyList()));
 }

 @Test
 public void testChildren_SingleChild() {
 Document document = mock(Document.class);
 Element rootElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 Node childNode = mock(Node.class);
 when(rootElement.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 when(rootElement.getChildNodes().getLength()).thenReturn(1);
 when(rootElement.getChildNodes().item(0)).thenReturn(childNode);

 List<Node> children = OperationsHelperImpl.getInstance().children(document);
 Assert.assertThat(children, hasSize(1));
 Assert.assertThat(children, hasItem(childNode));
 }

 @Test
 public void testChildren_MultipleChildren() {
 Document document = mock(Document.class);
 Element rootElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 Node childNode1 = mock(Node.class);
 Node childNode2 = mock(Node.class);
 when(rootElement.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 when(rootElement.getChildNodes().getLength()).thenReturn(2);
 when(rootElement.getChildNodes().item(0)).thenReturn(childNode1);
 when(rootElement.getChildNodes().item(1)).thenReturn(childNode2);

 List<Node> children = OperationsHelperImpl.getInstance().children(document);
 Assert.assertThat(children, hasSize(2));
 Assert.assertThat(children, hasItems(childNode1, childNode2));
 }

 @Test
 public void testFind_RootElement() {
 Document document = mock(Document.class);
 Element rootElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node found = OperationsHelperImpl.getInstance().find(document, "/");
 assertSame(rootElement, found);
 }

 @Test
 public void testFind_ExistingElement() {
 Document document = mock(Document.class);
 Element rootElement = mock(Element.class);
 Element childElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);
 when(rootElement.getTagName()).thenReturn("root");

 NodeList childNodes = Mockito.mock(NodeList.class);
 Element grandChildElement = mock(Element.class);
 when(childNodes.getLength()).thenReturn(1);
 when(childNodes.item(0)).thenReturn(childElement);
 when(childElement.getTagName()).thenReturn("child");
 NodeList grandChildNodes = Mockito.mock(NodeList.class);
 when(childElement.getChildNodes()).thenReturn(grandChildNodes);
 when(grandChildNodes.getLength()).thenReturn(1);
 when(grandChildNodes.item(0)).thenReturn(grandChildElement);
 when(grandChildElement.getNodeName()).thenReturn("grandchild");

 when(rootElement.getChildNodes()).thenReturn(childNodes);

 Node found = OperationsHelperImpl.getInstance().find(document, "/root/child/grandchild");
 assertSame(grandChildElement, found);
 }

 @Test
 public void testFind_NonExistingElement() {
 Document document = mock(Document.class);
 Element rootElement = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node found = OperationsHelperImpl.getInstance().find(document, "/nonExistingElement");
 assertNull(found);
 }

 @Test
 public void testNodeWithName_NullList() {
 List<Node> nodes = null;
 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_EmptyList() {
 List<Node> nodes = Collections.emptyList();
 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_SingleNodeWithMatchingName() {
 Node node = mock(Node.class);
 List<Node> nodes = Collections.singletonList(node);
 when(node.getNodeName()).thenReturn("test");

 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertSame(node, result);
 }

 @Test
 public void testNodeWithName_SingleNodeWithDifferentName() {
 Node node = mock(Node.class);
 List<Node> nodes = Collections.singletonList(node);
 when(node.getNodeName()).thenReturn("differentName");

 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testNodeWithName_MultipleNodesWithMatchingName() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test");

 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertSame(node1, result);
 }

 @Test
 public void testNodeWithName_MultipleNodesWithDifferentName() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test");
 when(node2.getNodeName()).thenReturn("differentName");

 Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "test");
 assertSame(node1, result);
 }

 @Test
 public void testSublistWithName_NullList() {
 List<Node> nodes = null;
 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> nodes = Collections.emptyList();
 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testSublistWithName_SingleNodeWithMatchingName() {
 Node node = mock(Node.class);
 List<Node> nodes = Collections.singletonList(node);
 when(node.getNodeName()).thenReturn("test");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertSame(nodes, result);
 }

 @Test
 public void testSublistWithName_SingleNodeWithDifferentName() {
 Node node = mock(Node.class);
 List<Node> nodes = Collections.singletonList(node);
 when(node.getNodeName()).thenReturn("differentName");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertNull(result);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithMatchingName() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertSame(Arrays.asList(node1), result);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithMatchingNameAndDifferentNames() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test1");
 when(node2.getNodeName()).thenReturn("test2");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertSame(Collections.singletonList(node1), result);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithMatchingNameAndSameName() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test");
 when(node2.getNodeName()).thenReturn("test");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertSame(nodes, result);
 }

 @Test
 public void testSublistWithName_MultipleNodesWithDifferentNames() {
 Node node1 = mock(Node.class);
 Node node2 = mock(Node.class);
 List<Node> nodes = Arrays.asList(node1, node2);
 when(node1.getNodeName()).thenReturn("test1");
 when(node2.getNodeName()).thenReturn("test2");

 List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "test");
 assertNull(result);
 }
}