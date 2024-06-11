package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node xmlNode = getSampleXMLNode();
 List<Node> expectedChildren = new ArrayList<Node>() {{
 add(new Element("element1"));
 add(new Element("element2"));
 add(new Element("element3"));
 add(new Element("element4"));
 add(new Element("element5"));
 add(new Element("element6"));
 add(new Element("element7"));
 add(new Element("element8"));
 add(new Element("element9"));
 add(new Element("element10"));
 add(new Element("element11"));
 add(new Element("element12"));
 add(new Element("element13"));
 add(new Element("element14"));
 add(new Element("element15"));
 add(new Element("element16"));
 add(new Element("element17"));
 add(new Element("element18"));
 add(new Element("element19"));
 add(new Element("element20"));
 add(new Element("element21"));
 add(new Element("element22"));
 add(new Element("element23"));
 add(new Element("element24"));
 add(new Element("element25"));
 add(new Element("element26"));
 add(new Element("element27"));
 add(new Element("element28"));
 add(new Element("element29"));
 add(new Element("element30"));
 add(new Element("element31"));
 add(new Element("element32"));
 add(new Element("element33"));
 add(new Element("element34"));
 add(new Element("element35"));
 add(new Element("element36"));
 add(new Element("element37"));
 add(new Element("element38"));
 add(new Element("element39"));
 add(new Element("element40"));
 add(new Element("element41"));
 add(new Element("element42"));
 add(new Element("element43"));
 add(new Element("element44"));
 add(new Element("element45"));
 add(new Element("element46"));
 add(new Element("element47"));
 add(new Element("element48"));
 add(new Element("element49"));
 add(new Element("element50"));
 add(new Element("element51"));
 add(new Element("element52"));
 add(new Element("element53"));
 add(new Element("element54"));
 add(new Element("element55"));
 add(new Element("element56"));
 add(new Element("element57"));
 add(new Element("element58"));
 add(new Element("element59"));
 add(new Element("element60"));
 add(new Element("element61"));
 add(new Element("element62"));
 add(new Element("element63"));
 add(new Element("element64"));
 add(new Element("element65"));
 add(new Element("element66"));
 add(new Element("element67"));
 add(new Element("element68"));
 add(new Element("element69"));
 add(new Element("element70"));
 add(new Element("element71"));
 add(new Element("element72"));
 add(new Element("element73"));
 add(new Element("element74"));
 add(new Element("element75"));
 add(new Element("element76"));
 add(new Element("element77"));
 add(new Element("element78"));
 add(new Element("element79"));
 add(new Element("element80"));
 add(new Element("element81"));
 add(new Element("element82"));
 add(new Element("element83"));
 add(new Element("element84"));
 add(new Element("element85"));
 add(new Element("element86"));
 add(new Element("element87"));
 add(new Element("element88"));
 add(new Element("element89"));
 add(new Element("element90"));
 add(new Element("element91"));
 add(new Element("element92"));
 add(new Element("element93"));
 add(new Element("element94"));
 add(new Element("element95"));
 add(new Element("element96"));
 add(new Element("element97"));
 add(new Element("element98"));
 add(new Element("element99"));
 add(new Element("element100"));
 }};
 List<Node> actualChildren = operationsHelper.children(xmlNode);
 assertEquals("Number of children is incorrect", expectedChildren.size(), actualChildren.size());
 for (int i = 0; i < expectedChildren.size(); i++) {
 Node expectedChild = expectedChildren.get(i);
 Node actualChild = actualChildren.get(i);
 assertEquals("Children at index " + i + " are not equal", expectedChild, actualChild);
 }
 }

 @Test
 public void testFind() {
 Document xmlDocument = null;
 try {
 xmlDocument = buildXMLDocumentFromString("<root><element1><element2>" +
 "sample text<element3><element4>and more text</element4></element3></element2></element1></root>");
 Node expectedNode = xmlDocument.getRootElement().getChild("element1").getChild("element2");
 Node actualNode = operationsHelper.find(xmlDocument, "root/element1/element2");
 assertEquals("Find method failed", expectedNode, actualNode);
 } catch (JDOMException e) {
 e.printStackTrace();
 }
 Node nullNode = null;
 Node actualNode = operationsHelper.find(null, "path");
 assertNull("Find method returned non-null value for null document", actualNode);
 Node emptyDocumentNode = new Element("root");
 actualNode = operationsHelper.find(emptyDocumentNode, "path");
 assertNull("Find method returned non-null value for empty document", actualNode);
 Node nonExistentNode = new Element("root");
 actualNode = operationsHelper.find(nonExistentNode, "nonExistentPath");
 assertNull("Find method returned non-null value for non-existent node", actualNode);
 Node existentNode = new Element("root").addContent(new Element("element1")).addContent(new Element("element2"));
 actualNode = operationsHelper.find(existentNode, "root/element1/element2");
 assertNotNull("Find method returned null for existing node", actualNode);
 }

 @Test
 public void testNodeWithName() {
 Document xmlDocument = buildXMLDocumentFromString("<root><element1>" +
 "<element2>sample text<element3>and more text</element3></element2></element1></root>");
 List<Node> nodes = xmlDocument.getRootElement().getChildren();
 Node expectedNode = nodes.get(0);
 Node actualNode = operationsHelper.nodeWithName(nodes, "element1");
 assertEquals("Nodes with name 'element1' are not equal", expectedNode, actualNode);
 List<Node> emptyList = new ArrayList<Node>();
 actualNode = operationsHelper.nodeWithName(emptyList, "element1");
 assertNull("Node with name 'element1' was found in empty list", actualNode);
 }

 @Test
 public void testSublistWithName() {
 Document xmlDocument = buildXMLDocumentFromString("<root><element1>" +
 "<element2>sample text<element3>and more text</element3></element2></element1></root>");
 List<Node> nodes = xmlDocument.getRootElement().getChildren();
 List<Node> expectedSublist = new ArrayList<Node>() {{
 add(nodes.get(0));
 add(nodes.get(0).getChild("element2"));
 add(nodes.get(0).getChild("element2").getChild("element3"));
 }};
 List<Node> actualSublist = operationsHelper.sublistWithName(nodes, "element1");
 assertEquals("Sublists with name 'element1' are not equal", expectedSublist, actualSublist);
 List<Node> emptyList = new ArrayList<Node>();
 actualSublist = operationsHelper.sublistWithName(emptyList, "element1");
 assertNotNull("Empty list should not return null", actualSublist);
 assertFalse("Empty list should be empty", actualSublist.isEmpty());
 }

 private Node getSampleXMLNode() {
 String xmlString = "<root><element1><element2>" +
 "sample text<element3><element4>and more text</element4></element3></element2></element1></root>";
 return new SAXBuilder().build(new StringReader(xmlString)).getRootElement();
 }

 private Document buildXMLDocumentFromString(String xmlString) {
 SAXBuilder builder = new SAXBuilder();
 try {
 return builder.build(new StringReader(xmlString));
 } catch (JDOMException e) {
 throw new RuntimeException(e);
 }
 }
}