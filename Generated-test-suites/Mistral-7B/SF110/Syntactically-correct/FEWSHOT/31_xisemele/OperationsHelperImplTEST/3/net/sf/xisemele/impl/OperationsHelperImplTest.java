package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 @Test
 public void testChildren() {
 final Document document = createDocument();
 final Element root = document.createElement("root");
 document.appendChild(root);
 final Element child1 = document.createElement("child1");
 root.appendChild(child1);
 final Element child2 = document.createElement("child2");
 child1.appendChild(child2);

 final Node node = document.getDocumentElement();
 final List<Node> result = OperationsHelperImpl.getInstance().children(node);

 assertEquals(3, result.size());
 assertTrue(result.contains(node));
 assertTrue(result.contains(child1));
 assertTrue(result.contains(child2));
 }

 @Test
 public void testChildrenEmpty() {
 final Document document = createDocument();
 final Node node = document.getDocumentElement();
 final List<Node> result = OperationsHelperImpl.getInstance().children(node);

 assertEquals(0, result.size());
 }

 @Test
 public void testFind() {
 final Document document = createDocument();
 final Element root = document.createElement("root");
 document.appendChild(root);
 final Element child1 = document.createElement("child1");
 root.appendChild(child1);
 final Element child2 = document.createElement("child2");
 child1.appendChild(child2);

 final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child1/child2");

 assertEquals("child2", result.getNodeName());
 }

 @Test
 public void testFindNullDocument() {
 final Node result = OperationsHelperImpl.getInstance().find(null, "/root/child1/child2");

 assertNull(result);
 }

 @Test
 public void testFindNullPath() {
 final Document document = createDocument();
 final Node result = OperationsHelperImpl.getInstance().find(document, null);

 assertNull(result);
 }

 @Test
 public void testFindInvalidPath() {
 final Document document = createDocument();
 final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child1/child3");

 assertNull(result);
 }

 @Test
 public void testFindParentNode() {
 final Document document = createDocument();
 final Element root = document.createElement("root");
 document.appendChild(root);
 final Element child1 = document.createElement("child1");
 root.appendChild(child1);
 final Element child2 = document.createElement("child2");
 child1.appendChild(child2);

 final Node result = OperationsHelperImpl.getInstance().find(document, "/root/child1");

 assertEquals(child1, result);
 }

 @Test
 public void testFindEmptyDocument() {
 final Document document = createDocument();
 final Node result = OperationsHelperImpl.getInstance().find(document, "/root");

 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 final Document document = createDocument();
 final Element root = document.createElement("root");
 document.appendChild(root);
 final Element child1 = document.createElement("child1");
 root.appendChild(child1);
 final Element child2 = document.createElement("child2");
 child1.appendChild(child2);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "child2");

 assertEquals(child2, result);
 }

 @Test
 public void testNodeWithNameNullList() {
 final List<Node> nodes = null;
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "child2");

 assertNull(result);
 }

 @Test
 public void testNodeWithNameNullName() {
 final List<Node> nodes = Arrays.asList(new Element[] { new Element("root"), new Element("child1"), new Element("child2") });
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, null);

 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 final Document document = createDocument();
 final Element root = document.createElement("root");
 document.appendChild(root);
 final Element child1 = document.createElement("child1");
 root.appendChild(child1);
 final Element child2 = document.createElement("child2");
 child1.appendChild(child2);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "child2");

 assertEquals(1, result.size());
 assertEquals(child2, result.get(0));
 }

 @Test
 public void testSublistWithNameEmptyList() {
 final List<Node> nodes = Collections.emptyList();
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "child2");

 assertEquals(0, result.size());
 }

 @Test
 public void testSublistWithNameNullName() {
 final List<Node> nodes = Arrays.asList(new Element[] { new Element("root"), new Element("child1"), new Element("child2") });
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, null);

 assertEquals(0, result.size());
 }

 private Document createDocument() {
 final Document document = documentFactory.newDocument();
 return document;
 }
}