package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.xisemele.impl.OperationsHelperImpl;
import net.sf.xisemele.impl.OperationsHelper;

public class OperationsHelperImplTest {

 private final OperationsHelper helper = new OperationsHelperImpl();

 @org.junit.Test
 public void testChildren() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final List<Node> children = helper.children(root);
 final List<Node> expectedChildren = new ArrayList<>(Arrays.asList(
 new Element("child1"), new Element("child2"), new Element("child3") ));
 assertEquals(expectedChildren, children);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testChildrenNullNode() {
 helper.children(null);
 }

 @org.junit.Test
 public void testFind() {
 final Document doc = createDocument();
 final Element root = doc.getDocumentElement();
 root.setNodeName("root");
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 child1.appendChild(child2);
 final Node leaf = new Element("leaf");
 child2.appendChild(leaf);

 final Node result = helper.find(doc, "/root/child1/child2/leaf");
 assertEquals(leaf, result);
 }

 @org.junit.Test
 public void testFindNoResult() {
 final Document doc = createDocument();
 final Element root = doc.getDocumentElement();
 root.setNodeName("root");
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 child1.appendChild(child2);

 final Node result = helper.find(doc, "/root/child1/child4/leaf");
 assertNull(result);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFindEmptyDocument() {
 final Document doc = createDocument();
 helper.find(doc, "/");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFindEmptyPath() {
 helper.find(createDocument(), "");
 }

 @org.junit.Test
 public void testNodeWithName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 root.appendChild(child2);
 final Node result = helper.nodeWithName(Arrays.asList(root, child1, child2), "child2");
 assertEquals(child2, result);
 }

 @org.junit.Test
 public void testNodeWithNameNullList() {
 helper.nodeWithName(null, "child2");
 }

 @org.junit.Test
 public void testNodeWithNameNullNode() {
 helper.nodeWithName(Arrays.asList(new Element("root")), null);
 }

 @org.junit.Test
 public void testNodeWithNameNoResult() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 root.appendChild(child2);

 final Node result = helper.nodeWithName(Arrays.asList(root, child1, child2), "child3");
 assertNull(result);
 }

 @org.junit.Test
 public void testSublistWithName() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 final Node child3 = new Element("child3");
 root.appendChild(child2);
 root.appendChild(child3);
 final Node result = helper.sublistWithName(Arrays.asList(root, child1, child2, child3), "child2");
 assertEquals(Arrays.asList(child2), result);
 }

 @org.junit.Test
 public void testSublistWithNameEmptyList() {
 final Node result = helper.sublistWithName(Arrays.asList(), "child2");
 assertEquals(Arrays.asList(), result);
 }

 @org.junit.Test
 public void testSublistWithNameNullList() {
 helper.sublistWithName(null, "child2");
 }

 @org.junit.Test
 public void testSublistWithNameNullNode() {
 helper.sublistWithName(Arrays.asList(new Element("root")), null);
 }

 @org.junit.Test
 public void testSublistWithNameNoResult() {
 final Document doc = createDocument();
 final Node root = doc.getDocumentElement();
 final Node child1 = new Element("child1");
 root.appendChild(child1);
 final Node child2 = new Element("child2");
 root.appendChild(child2);

 final List<Node> result = helper.sublistWithName(Arrays.asList(root, child1, child2), "child3");
 assertEquals(Arrays.asList(), result);
 }

 private Document createDocument() {
 final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 return factory.newDocumentBuilder().newDocument();
 }

 private static class Element implements Node {
 private String nodeName;

 public Element(String name) {
 nodeName = name;
 }

 @Override
 public Node getNodeName() {
 return nodeName;
 }

 @Override
 public String getNodeValue() {
 return null;
 }

 @Override
 public int getNodeType() {
 return 1;
 }

 @Override
 public NamedNodeMap getAttributes() {
 return null;
 }

 @Override
 public NodeList getChildNodes() {
 return new NodeList() {
 @Override
 public Node item(int index) {
 return new Element("child" + (index + 1));
 }

 @Override
 public int getLength() {
 return 3;
 }
 };
 }

 @Override
 public Node getParentNode() {
 return null;
 }

 @Override
 public Node insertBefore(Node newChild, Node refChild) {
 return null;
 }

 @Override
 public Node replaceChild(Node newChild, Node oldChild) {
 return null;
 }

 @Override
 public Node removeChild(int index) {
 return null;
 }

 @Override
 public void normalize() {
 }

 @Override
 public Node cloneNode(boolean deep) {
 return new Element(nodeName);
 }
 }
}