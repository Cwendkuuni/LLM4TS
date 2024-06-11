package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.junit.Test;

public class OperationsHelperImplTest {

 private OperationsHelper helper = new OperationsHelperImpl();

 @Test
 public void testChildren() throws JDOMException {
 Node root = new Element("root").getDocument().getRootElement();
 root.addContent(new Element("child1").setName("child1"));
 root.addContent(new Element("child2").setName("child2"));
 root.addContent(new Element("grandChild").setName("grandChild"));

 List<Node> children = helper.children(root);
 assertEquals(3, children.size());
 assertSame(root.getChild(0), children.get(0));
 assertSame(root.getChild(1), children.get(1));
 assertSame(root.getChild(1).getChild(0), children.get(2));
 }

 @Test
 public void testFindNull() {
 Document document = null;
 assertNull(helper.find(document, "/nonExistentPath"));
 }

 @Test
 public void testFindEmptyDocument() throws JDOMException {
 Document document = new Document();
 assertNull(helper.find(document, "/nonExistentPath"));
 }

 @Test
 public void testFindRoot() throws JDOMException {
 Document document = new Document(new Element("root"));
 Element root = document.getRootElement();
 Node found = helper.find(document, "/");
 assertSame(root, found);
 }

 @Test
 public void testFindChild() throws JDOMException {
 Document document = new Document(new Element("root")
 .addContent(new Element("child1").setText("child1"))
 .addContent(new Element("child2").setText("child2"))
 );
 Node child = helper.find(document, "/child1");
 assertSame(document.getRootElement().getChild("child1"), child);
 }

 @Test
 public void testFindGrandChild() throws JDOMException {
 Document document = new Document(new Element("root")
 .addContent(new Element("child1").setText("child1"))
 .addContent(new Element("child2").setText("child2"))
 .addContent(new Element("grandChild").setText("grandChild"))
 );
 Node grandChild = helper.find(document, "/child1/grandChild");
 assertSame(document.getRootElement().getChild("child1").getChild("grandChild"), grandChild);
 }

 @Test
 public void testFindNonExistent() throws JDOMException {
 Document document = new Document(new Element("root")
 .addContent(new Element("child1").setText("child1"))
 .addContent(new Element("child2").setText("child2"))
 );
 assertNull(helper.find(document, "/nonExistentPath"));
 }

 @Test
 public void testNodeWithName() {
 Node root = new Element("root").getDocument().getRootElement();
 root.addContent(new Element("child1").setName("child1"));
 root.addContent(new Element("child2").setName("child2"));
 root.addContent(new Element("grandChild").setName("grandChild"));

 List<Node> children = helper.children(root);
 Node nodeWithName = helper.nodeWithName(children, "child1");
 assertSame(children.get(0), nodeWithName);
 }

 @Test
 public void testNodeWithNameNull() {
 List<Node> nodes = null;
 assertNull(helper.nodeWithName(nodes, "child1"));
 }

 @Test
 public void testNodeWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 assertNull(helper.nodeWithName(nodes, "child1"));
 }

 @Test
 public void testNodeWithNameNotFound() {
 Node root = new Element("root").getDocument().getRootElement();
 List<Node> children = helper.children(root);
 assertNull(helper.nodeWithName(children, "grandChild"));
 }

 @Test
 public void testSublistWithNameEmptyList() {
 List<Node> nodes = new ArrayList<>();
 List<Node> sublist = helper.sublistWithName(nodes, "child1");
 assertEmpty(sublist);
 }

 @Test
 public void testSublistWithNameNull() {
 List<Node> nodes = null;
 assertNull(helper.sublistWithName(nodes, "child1"));
 }

 @Test
 public void testSublistWithName() throws JDOMException {
 Document document = new Document(new Element("root")
 .addContent(new Element("child1").setText("child1"))
 .addContent(new Element("child2").setText("child2"))
 .addContent(new Element("grandChild").setText("grandChild"))
 );
 List<Node> nodes = helper.children(document.getRootElement());
 List<Node> sublist = helper.sublistWithName(nodes, "child1");
 assertEquals(1, sublist.size());
 assertSame(nodes.get(0), sublist.get(0));
 }

 @Test
 public void testSublistWithNameNotFound() throws JDOMException {
 Document document = new Document(new Element("root")
 .addContent(new Element("child1").setText("child1"))
 .addContent(new Element("child2").setText("child2"))
 .addContent(new Element("grandChild").setText("grandChild"))
 );
 List<Node> nodes = helper.children(document.getRootElement());
 List<Node> sublist = helper.sublistWithName(nodes, "child3");
 assertEmpty(sublist);
 }
}