package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.*;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testParseSimpleXml() {
 String xml = "<root><child>Hello World</child></root>";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<img src='image.jpg' />";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 Element img = (Element) nodes.get(0);
 assertEquals("img", img.tagName());
 assertNotNull(img.attributes().get("src"));
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<root><!-- This is a comment --></root>";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 Comment comment = (Comment) nodes.get(1);
 assertEquals("This is a comment", comment.data());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidTag() {
 String xml = "<invalid>";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 // This test should throw an IllegalArgumentException as "invalid" is not a known tag.
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"html.dtd\">";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("html", doctype.nodeName());
 assertEquals("html.dtd", doctype.getSystemIdentifier());
 }

 @Test
 public void testParseXmlWithEmptyFragment() {
 String xml = "";
 List<Node> nodes = builder.parseFragment(xml, null, new Parser(builder));
 assertNotNull(nodes);
 assertEquals(0, nodes.size());
 }
}