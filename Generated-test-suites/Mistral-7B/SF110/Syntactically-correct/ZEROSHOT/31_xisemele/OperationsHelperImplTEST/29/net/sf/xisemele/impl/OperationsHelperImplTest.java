package net.sf.xisemele.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.junit.Test;

public class OperationsHelperImplTest {

 OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() throws Exception {
 Node root = new Element("root").getDocument().getDocumentElement();
 root.addContent(new Element("child1").setText("child1"));
 root.addContent(new Element("child2").setText("child2"));
 root.addContent(new Element("grandchild").setText("grandchild"));

 List<Node> children = operationsHelper.children(root);
 List<String> expected = new ArrayList<>();
 expected.add("child1");
 expected.add("child2");

 for (Node node : children) {
 assertEquals(expected.remove(0), node.getName());
 }

 assertTrue(expected.isEmpty());
 }

 @Test
 public void testChildrenWithEmptyNodeList() {
 Node emptyNode = new Document().getDocumentElement();
 List<Node> children = operationsHelper.children(emptyNode);
 assertTrue(children.isEmpty());
 }

 @Test
 public void testFind() throws Exception {
 Document document = new Document(new File("test.xml")); // Assuming test.xml is valid
 List<String> path = new ArrayList<String>(){{
 add("root"); add("child"); add("grandchild");}};

 Node found = operationsHelper.find(document, String.join("/", path));
 Node expected = document.getRootElement().getChild("child").getChild("grandchild");

 assertEquals(expected.getName(), found.getName());
 assertEquals(expected.getText(), found.getText());
 }

 @Test
 public void testFindWithEmptyPath() {
 Document document = new Document();
 String path = "";
 Node found = operationsHelper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testFindWithNonExistingPath() {
 Document document = new Document();
 String path = "/root/child/grandchild";
 Node found = operationsHelper.find(document, path);
 assertNull(found);
 }

 @Test
 public void testNodeWithName() {
 Node root = new Element("root").getDocument().getDocumentElement();
 root.addContent(new Element("child1").setText("child1"));
 root.addContent(new Element("child2").setText("child2"));

 List<Node> nodes = operationsHelper.children(root);
 Node nodeWithName = operationsHelper.nodeWithName(nodes, "child1");
 Node expected = nodes.get(0);

 assertEquals(expected.getName(), nodeWithName.getName());
 assertEquals(expected.getText(), nodeWithName.getText());
 }

 @Test
 public void testNodeWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<Node>();
 String name = "node";
 Node nodeWithName = operationsHelper.nodeWithName(nodes, name);
 assertNull(nodeWithName);
 }

 @Test
 public void testSublistWithName() throws Exception {
 Document document = new Document(new File("test.xml")); // Assuming test.xml is valid
 String name = "child";
 List<Node> nodes = operationsHelper.children(document.getRootElement());
 List<Node> sublist = operationsHelper.sublistWithName(nodes, name);

 List<String> expected = new ArrayList<>();
 for (Node node : sublist) {
 expected.add(node.getText());
 }

 List<String> actual = new ArrayList<>();
 for (Node node : nodes) {
 if (node.getName().equals(name)) {
 actual.add(node.getText());
 }
 }

 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithNameWithEmptyList() {
 List<Node> nodes = new ArrayList<Node>();
 String name = "node";
 List<Node> sublist = operationsHelper.sublistWithName(nodes, name);
 assertTrue(sublist.isEmpty());
 }

}