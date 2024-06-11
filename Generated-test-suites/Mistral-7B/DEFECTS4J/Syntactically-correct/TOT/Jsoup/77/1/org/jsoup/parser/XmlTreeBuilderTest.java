package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlWithValidInput() {
 String xmlInput = "<root><element attr=\"value\">Content</element></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = doc.select("root").first();
 Element element = root.select("element").first();
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXmlWithEmptyInput() {
 Document doc = XmlTreeBuilder.parse(new StringReader(""), "http://test.com");
 assertTrue(doc.body().text().isEmpty());
 }

 @Test
 public void testParseXmlWithInvalidInput() {
 String invalidXmlInput = "<root><element <attr=\"value\">Content</element>";
 TryParseResult result = XmlTreeBuilder.parseTry(new StringReader(invalidXmlInput), "http://test.com", ParseSettings.defaultSettings());
 assertTrue(result.hasParseErrors());
 List<ParseError> errors = result.getParseErrors();
 assertEquals(1, errors.size());
 assertEquals("Unclosed element: element at line 1 column 18", errors.get(0).message());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.png\" /></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element img = doc.select("img").first();
 assertTrue(img.hasAttr("src"));
 assertEquals("image.png", img.attr("src"));
 }

 @Test
 public void testParseXmlWithComment() {
 String xmlInput = "<root><!-- This is a comment --></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element comment = doc.select("--[^>]*-->").first();
 assertNotNull(comment);
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xmlInput = "<root><![CDATA[This is some <b>html</b> inside a CDATA section]]></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element cdata = doc.body();
 String content = cdata.text();
 assertTrue(content.contains("<b>html</b>"));
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 DocumentType doctype = doc.doctype();
 assertEquals("html", doctype.nodeName());
 }

 @Test
 public void testParseXmlWithUnknownTag() {
 String xmlInput = "<root><unknown></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element unknown = doc.select("unknown").first();
 assertNotNull(unknown);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
 List<Node> fragmentNodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseSettings.defaultSettings());
 Element element = (Element) fragmentNodes.get(0);
 assertEquals("element", element.nodeName());
 assertEquals("Content", element.text());
 }
}