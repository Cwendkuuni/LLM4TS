package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

 @Test
 public void testChildren() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(textNode);

 final List<Node> result = OperationsHelperImpl.getInstance().children(root);

 assertEquals(3, result.size());
 assertEquals(element1, result.get(0));
 assertEquals(element2, result.get(1));
 assertEquals(textNode, result.get(2));
 }

 @Test
 public void testFind() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node element3 = doc.createElement("element3");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(element3);
 element1.appendChild(textNode);

 final Node result = OperationsHelperImpl.getInstance().find(doc, "/element1/element3");

 assertSame(element3, result);
 }

 @Test
 public void testFind_emptyDocument() {
 final Document doc = createDocument();
 final Node result = OperationsHelperImpl.getInstance().find(doc, "/notExistingNode");

 assertNull(result);
 }

 @Test
 public void testFind_wrongPath() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(textNode);

 final Node result = OperationsHelperImpl.getInstance().find(doc, "/notExistingPath");

 assertNull(result);
 }

 @Test
 public void testNodeWithName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(textNode);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "element1");

 assertSame(element1, result);
 }

 @Test
 public void testNodeWithName_emptyList() {
 final List<Node> nodes = Collections.emptyList();
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "element1");

 assertNull(result);
 }

 @Test
 public void testNodeWithName_wrongName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(textNode);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final Node result = OperationsHelperImpl.getInstance().nodeWithName(nodes, "element2");

 assertNull(result);
 }

 @Test
 public void testSublistWithName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node element3 = doc.createElement("element3");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(element3);
 element1.appendChild(textNode);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "element1");

 assertEquals(2, result.size());
 assertSame(element1, result.get(0));
 assertSame(element3, result.get(1));
 }

 @Test
 public void testSublistWithName_emptyList() {
 final List<Node> nodes = Collections.emptyList();
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "element1");

 assertEquals(0, result.size());
 }

 @Test
 public void testSublistWithName_wrongName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node element1 = doc.createElement("element1");
 final Node element2 = doc.createElement("element2");
 final Node element3 = doc.createElement("element3");
 final Node textNode = doc.createTextNode("textNode");

 root.appendChild(element1);
 root.appendChild(element2);
 element1.appendChild(element3);
 element1.appendChild(textNode);

 final List<Node> nodes = OperationsHelperImpl.getInstance().children(root);
 final List<Node> result = OperationsHelperImpl.getInstance().sublistWithName(nodes, "element2");

 assertEquals(0, result.size());
 }

 private Document createDocument() {
 final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
 return documentBuilder.newDocument();
 }
}