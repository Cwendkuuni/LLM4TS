package net.sf.xisemele.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.jaxb.JAXBContext;
import org.xml.jaxb.unmarshaller.JAXBContextFactory;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

 private OperationsHelper helper;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 JAXBContext context = JAXBContextFactory.createContext(new Class[] { net.sf.xisemele.impl.Document.class });
 helper = (OperationsHelper) context.createUnmarshaller().unmarshal(OperationsHelperImpl.class.getResource("/OperationsHelperImpl.xml")).getValue();
 }

 @Test
 public void testChildren() {
 Document doc = helper.createDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 Element a = doc.createElement("a");
 a.setTextContent("A");
 Element b = doc.createElement("b");
 b.setTextContent("B");
 root.appendChild(a);
 root.appendChild(b);

 List<Node> nodes = helper.children(root);
 assertEquals(2, nodes.size());
 assertEquals("a", nodes.get(0).getNodeName());
 assertEquals("b", nodes.get(1).getNodeName());
 }

 @Test
 public void testFind() {
 Document doc = helper.createDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 Element a = doc.createElement("a");
 a.setTextContent("A");
 Element b = doc.createElement("b");
 b.setTextContent("B");
 root.appendChild(a);
 root.appendChild(b);
 Element c = doc.createElement("c");
 c.setTextContent("C");
 b.appendChild(c);

 Node node = helper.find(doc, "root/a");
 assertEquals("a", node.getNodeName());
 node = helper.find(doc, "root/b/c");
 assertEquals("c", node.getNodeName());
 node = helper.find(doc, "root/d");
 assertNull(node);
 }

 @Test
 public void testNodeWithName() {
 Document doc = helper.createDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 Element a = doc.createElement("a");
 a.setTextContent("A");
 Element b = doc.createElement("b");
 b.setTextContent("B");
 root.appendChild(a);
 root.appendChild(b);
 List<Node> nodes = Arrays.asList(a, b);

 Node node = helper.nodeWithName(nodes, "a");
 assertEquals("a", node.getNodeName());
 node = helper.nodeWithName(nodes, "b");
 assertEquals("b", node.getNodeName());
 node = helper.nodeWithName(nodes, "c");
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 Document doc = helper.createDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 Element a = doc.createElement("a");
 a.setTextContent("A");
 Element b = doc.createElement("b");
 b.setTextContent("B");
 root.appendChild(a);
 root.appendChild(b);
 List<Node> nodes = Arrays.asList(a, b);

 List<Node> sublist = helper.sublistWithName(nodes, "a");
 assertEquals(1, sublist.size());
 assertEquals("a", sublist.get(0).getNodeName());
 sublist = helper.sublistWithName(nodes, "b");
 assertEquals(1, sublist.size());
 assertEquals("b", sublist.get(0).getNodeName());
 sublist = helper.sublistWithName(nodes, "c");
 assertNull(sublist);
 }
}