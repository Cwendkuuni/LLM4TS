package net.sf.xisemele.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OperationsHelperImplTest {

 @Test
 public void testChildren() {
 Document document = mock(Document.class);
 Node rootElement = Mockito.mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 NodeList nodeList = Mockito.mock(NodeList.class);
 when(rootElement.getChildNodes()).thenReturn(nodeList);

 Node child1 = Mockito.mock(Node.class);
 when(nodeList.item(0)).thenReturn(child1);
 Node child2 = Mockito.mock(Node.class);
 when(nodeList.item(1)).thenReturn(child2);

 List<Node> expectedChildren = new ArrayList<>();
 expectedChildren.add(child1);
 expectedChildren.add(child2);

 List<Node> actualChildren = new OperationsHelperImpl().children(document);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testChildren_NoElementChildren() {
 Document document = mock(Document.class);
 Node rootElement = Mockito.mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 NodeList nodeList = Mockito.mock(NodeList.class);
 when(rootElement.getChildNodes()).thenReturn(nodeList);

 List<Node> expectedChildren = new ArrayList<>();
 List<Node> actualChildren = new OperationsHelperImpl().children(document);
 assertEquals(expectedChildren, actualChildren);
 }

 @Test
 public void testFind() {
 Document document = mock(Document.class);
 Node rootElement = Mockito.mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node child1 = Mockito.mock(Node.class);
 child1.setNodeName("element1");
 when(rootElement.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 when(rootElement.getChildNodes().item(0)).thenReturn(child1);

 Node child2 = Mockito.mock(Node.class);
 child2.setNodeName("element2");
 when(child1.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 when(child1.getChildNodes().item(0)).thenReturn(child2);

 Node target = Mockito.mock(Node.class);
 when(child2.getNodeName()).thenReturn("target");
 when(child2.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
 when(child2.getChildNodes().item(0)).thenReturn(target);

 Node actualNode = new OperationsHelperImpl().find(document, "/element1/element2/target");
 assertEquals(target, actualNode);
 }

 @Test
 public void testFind_InvalidPath() {
 Document document = mock(Document.class);
 Node rootElement = Mockito.mock(Node.class);
 when(document.getDocumentElement()).thenReturn(rootElement);

 Node actualNode = new OperationsHelperImpl().find(document, "/notValidPath");
 assertNull(actualNode);
 }

 @Test
 public void testFind_InvalidDocumentElement() {
 Document document = mock(Document.class);
 Node rootElement = Mockito.mock(Node.class);
 when(document.getDocumentElement()).thenReturn(null);

 Node actualNode = new OperationsHelperImpl().find(document, "/notValidPath");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = new ArrayList<>();
 Node node1 = Mockito.mock(Node.class);
 node1.setNodeName("name1");
 nodes.add(node1);

 Node node2 = Mockito.mock(Node.class);
 node2.setNodeName("name2");
 nodes.add(node2);

 Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, "name1");
 assertEquals(node1, actualNode);
 }

 @Test
 public void testNodeWithName_NullList() {
 List<Node> nodes = null;
 Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, "name");
 assertNull(actualNode);
 }

 @Test
 public void testNodeWithName_NullName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(Mockito.mock(Node.class));

 String name = null;
 Node actualNode = new OperationsHelperImpl().nodeWithName(nodes, name);
 assertNull(actualNode);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = new ArrayList<>();
 Node node1 = Mockito.mock(Node.class);
 node1.setNodeName("name1");
 nodes.add(node1);

 Node node2 = Mockito.mock(Node.class);
 node2.setNodeName("name2");
 nodes.add(node2);

 List<Node> expectedSublist = new ArrayList<>();
 expectedSublist.add(node1);

 List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, "name1");
 assertEquals(expectedSublist, actualSublist);
 }

 @Test
 public void testSublistWithName_EmptyList() {
 List<Node> nodes = new ArrayList<>();

 List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, "name");
 assertEquals(0, actualSublist.size());
 }

 @Test
 public void testSublistWithName_NullName() {
 List<Node> nodes = new ArrayList<>();
 nodes.add(Mockito.mock(Node.class));

 String name = null;
 List<Node> actualSublist = new OperationsHelperImpl().sublistWithName(nodes, name);
 assertEquals(0, actualSublist.size());
 }
}