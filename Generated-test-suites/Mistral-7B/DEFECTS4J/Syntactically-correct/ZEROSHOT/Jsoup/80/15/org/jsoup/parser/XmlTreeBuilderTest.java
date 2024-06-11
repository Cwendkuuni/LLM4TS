package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Document doc;
 private List<Node> nodes;

 @Before
 public void setUp() {
 doc = new XmlTreeBuilder().parse(new StringReader("<root><child/><child><grandChild/></child></root>"), "http://localhost");
 nodes = doc.childNodes();
 }

 @After
 public void tearDown() {
 doc = null;
 nodes = null;
 }

 @Test
 public void testParse() {
 assertNotNull(doc);
 assertEquals(3, nodes.size());
 }

 @Test
 public void testParseWithBaseUri() {
 doc = new XmlTreeBuilder().parse(new StringReader("<root xmlns:xsi=\"http://schemas.xmlsoap.org/wsdl/extensions/\"><child><grandChild xsi:type=\"xs:string\"/></child></root>"), "http://localhost/test");
 nodes = doc.childNodes();
 Element root = (Element) nodes.get(0);
 assertNotNull(root);
 assertNotNull(root.namespaceURI());
 assertEquals("http://schemas.xmlsoap.org/wsdl/extensions/", root.namespaceURI());
 }

 @Test
 public void testParseFragment() {
 String fragment = "<root><child/><child><grandChild/></child></root>";
 nodes = new XmlTreeBuilder().parseFragment(fragment, "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(n -> (Node) n).collect(Collectors.toList());
 assertNotNull(nodes);
 assertEquals(3, nodes.size());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessUnexpectedTokenType() {
 new XmlTreeBuilder().parse(new StringReader("<root><child>abc</child>"), "http://localhost").runParser();
 Validate.expect(RuntimeException.class);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child/><child><grandChild/></child></root>";
 Element root = doc.child(0);
 Element child1 = root.child(0);
 Element child2 = root.child(1);
 Element grandChild = child2.child(0);
 assertEquals("root", root.tagName());
 assertEquals("child", child1.tagName());
 assertEquals("grandChild", grandChild.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Element child1 = (Element) nodes.get(1);
 Element child2 = (Element) nodes.get(2);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse(new StringReader("<root><child/><child></child></root>"), "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Element root = builder.doc.child(0);
 assertEquals(3, root.childNodes().size());
 assertEquals(child1.tagName(), root.child(0).tagName());
 assertEquals(child2.tagName(), root.child(1).tagName());
 builder.popStackToClose(new Token.EndTag("child"));
 assertEquals(2, root.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 Element comment = (Element) nodes.get(2);
 assertEquals("Comment", comment.nodeName());
 }

 @Test
 public void testProcessCharacter() {
 Element text = (Element) nodes.get(1);
 assertEquals("child", text.tagName());
 assertEquals("", text.childNodes().get(0).toString());
 }

 @Test
 public void testProcessDoctype() {
 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("root", doctype.parentNode().tagName());
 assertEquals("root", doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 }
}