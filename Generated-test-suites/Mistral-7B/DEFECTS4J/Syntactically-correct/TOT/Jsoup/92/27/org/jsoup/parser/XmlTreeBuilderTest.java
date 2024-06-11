package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParseEmptyXml() {
 String xml = "<>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 assertEquals(1, doc.childNodes().size());
 assertNotNull(doc.body());
 }

 @Test
 public void testParseSimpleXml() {
 String xml = "<root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 Element root = doc.select("root").first();
 Element child = root.select("child").first();
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseSelfClosingTag() {
 String xml = "<img src=\"image.png\" />";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 Element img = doc.select("img").first();
 assertNotNull(img);
 }

 @Test(expected = RuntimeException.class)
 public void testParseInvalidXml() {
 String xml = "<root>Hello World<child>";
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 } catch (Exception e) {
 // expected
 }
 Validate.fail("Unexpected exception: " + e.getMessage());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child>Fragment</child>";
 List<Node> fragments = xmlTreeBuilder.parseFragment(xmlFragment, null, "http://example.com", parser);
 Element fragment = (Element) fragments.get(0);
 assertEquals("Fragment", fragment.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = Jsoup.parse("<root><child></child></root>").select("root").first();
 String xmlFragment = "<child>Fragment</child>";
 List<Node> fragments = xmlTreeBuilder.parseFragment(xmlFragment, context, "http://example.com", parser);
 Element fragment = (Element) fragments.get(0);
 assertEquals("Fragment", fragment.text());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithCustomSettings() {
 ParseSettings settings = ParseSettings.empty();
 settings.setPreserveCase(false);
 XmlTreeBuilder customXmlTreeBuilder = new XmlTreeBuilder();
 customXmlTreeBuilder.setDefaultSettings(settings);
 Document doc = customXmlTreeBuilder.parse(new StringReader("<root><child>Hello World</child></root>"), "http://example.com");
 Element child = doc.select("child").first();
 assertEquals("hello world", child.text());
 }

 @Test
 public void testParseDocumentWithDoctype() {
 String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
 "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
 "<head>\n" +
 "<title>Title</title>\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>Hello World</p>\n" +
 "</body>\n";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 DocumentType doctype = doc.doctype();
 assertEquals("html", doctype.name());
 assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.publicIdentifier());
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.systemIdentifier());
 }
}