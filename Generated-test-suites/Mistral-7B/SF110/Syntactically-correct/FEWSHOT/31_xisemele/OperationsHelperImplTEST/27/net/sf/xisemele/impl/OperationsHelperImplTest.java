package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class OperationsHelperImplTest {

 private OperationsHelper impl;
 private Document document;

 @Before
 public void setUp() {
 impl = new OperationsHelperImpl();
 document = mock(Document.class);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testChildren() {
 final Node root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final Node child = mock(Element.class);
 final NodeList nodes = mock(NodeList.class);
 when(root.getChildNodes()).thenReturn(nodes);
 when(nodes.getLength()).thenReturn(2);
 when(nodes.item(0)).thenReturn(child);

 final List<Node> result = impl.children(document);
 assertEquals(Collections.singletonList(child), result);
 }

 @Test
 public void testChildrenEmpty() {
 final Node root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final NodeList nodes = mock(NodeList.class);
 when(nodes.getLength()).thenReturn(0);

 final List<Node> result = impl.children(document);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testChildrenNull() {
 final List<Node> result = impl.children(null);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testFind() {
 final Node root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final Element child1 = mock(Element.class);
 when(child1.getNodeName()).thenReturn("/a/b");
 when(root.getChildNodes()).thenReturn(mock(NodeList.class));
 final NodeList nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn(2);
 when(nodeList.item(0)).thenReturn(child1);

 final Node result = impl.find(document, "/a/b");
 assertSame(child1, result);
 }

 @Test
 public void testFindNull() {
 final Node result = impl.find(document, null);
 assertNull(result);
 }

 @Test
 public void testFindNotFound() {
 final Node root = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 final NodeList nodeList = mock(NodeList.class);
 when(root.getChildNodes()).thenReturn(nodeList);

 final Node result = impl.find(document, "/a/b");
 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 final Node root = mock(Element.class);
 final Node child = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getChildNodes()).thenReturn(Collections.singletonList(child));

 final Node result = impl.nodeWithName(Collections.singletonList(child), "/a/b");
 assertNull(result);
 }

 @Test
 public void testNodeWithNameFound() {
 final Node root = mock(Element.class);
 final Node child = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getChildNodes()).thenReturn(Collections.singletonList(child));
 when(child.getNodeName()).thenReturn("/a/b");

 final Node result = impl.nodeWithName(Collections.singletonList(child), "/a/b");
 assertSame(child, result);
 }

 @Test
 public void testSublistWithName() {
 final Node root = mock(Element.class);
 final Node child1 = mock(Element.class);
 final Node child2 = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getChildNodes()).thenReturn(Arrays.asList(child1, child2));

 final List<Node> result = impl.sublistWithName(Arrays.asList(child1, child2), "/a/b");
 assertEquals(Collections.singletonList(child2), result);
 }

 @Test
 public void testSublistWithNameNotFound() {
 final Node root = mock(Element.class);
 final Node child1 = mock(Element.class);
 final Node child2 = mock(Element.class);
 when(document.getDocumentElement()).thenReturn(root);
 when(root.getChildNodes()).thenReturn(Arrays.asList(child1, child2));

 final List<Node> result = impl.sublistWithName(Arrays.asList(child1, child2), "/a/c");
 assertEquals(Collections.emptyList(), result);
 }
}