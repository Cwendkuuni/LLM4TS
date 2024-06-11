package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class XmlTreeBuilderTest {

 private ParseSettings settings;
 private Parser xmlParser;

 @Before
 public void setup() {
 settings = new ParseSettings();
 xmlParser = Jsoup.parser().xmlParser();
 }

 @Test
 public void testParseEmptyXml() throws IOException, ParseError {
 String xml = "<>";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 }

 @Test
 public void testParseXmlWithRootElement() throws IOException, ParseError {
 String xml = "<root></root>";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 Element root = doc.select("root").first();
 assertNotNull(root);
 }

 @Test
 public void testParseXmlWithSelfClosingTag() throws IOException, ParseError {
 String xml = "<tag />";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 Element tag = doc.select("tag").first();
 assertNotNull(tag);
 assertNull(tag.childNodes());
 }

 @Test
 public void testParseXmlWithComment() throws IOException, ParseError {
 String xml = "<!-- This is a comment -->";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 Comment comment = doc.select("!-- This is a comment -->").first();
 assertNotNull(comment);
 }

 @Test
 public void testParseXmlWithCDataSection() throws IOException, ParseError {
 String xml = "<![CDATA[ <html><body><h1>Hello World</h1></body></html> ]]>";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 CDataNode cdata = doc.select("![CDATA[ <html><body><h1>Hello World</h1></body></html> ]]").first();
 assertNotNull(cdata);
 }

 @Test
 public void testParseXmlWithDoctype() throws IOException, ParseError {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 DocumentType doctype = doc.doctype();
 assertNotNull(doctype);
 }

 @Test
 public void testParseXmlWithAttributes() throws IOException, ParseError {
 String xml = "<element attr1='value1' attr2='value2' />";
 Document doc = xmlParser.parse(new StringReader(xml), "", ParseErrorList.noTracking(), settings);
 assertNotNull(doc);
 assertNotNull(doc.childNodes());
 assertNotNull(doc.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 Element element = doc.select("element").first();
 assertNotNull(element);
 assertEquals("value1", element.attr("attr1"));
 assertEquals("value2", element.attr("attr2"));
 }

 @Test
 public void testParseXmlWithErrorHandling() throws IOException, ParseError {
 String xml = "<root><tag></tag><tag></tag><!>"; // missing ">" in comment
 ParseErrorList errors = new ParseErrorList();
 Document doc = assertThrows(ParseError.class, () -> xmlParser.parse(new StringReader(xml), "", errors, settings)).getCause();
 List<Node> errorsNodes = doc.childNodes();
 assertNotNull(errorsNodes);
 assertNotNull(errors.size());
 assertEquals(2, errorsNodes.size());
 assertEquals(ParseError.ErrorType.MISMATCHED_TAG, errorsNodes.get(0).nodeName());
 assertEquals("Error parsing start tag : line 2, col 4: unclosed tag", errors.toString());
 }

 @Test
 public void testParseXmlWithFragment() throws IOException, ParseError {
 String xmlFragment = "<root><tag>Hello</tag><tag attr='value'>World</tag></root>";
 List<Node> nodes = xmlParser.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings).get();
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals("root", nodes.get(0).nodeName());
 }
}