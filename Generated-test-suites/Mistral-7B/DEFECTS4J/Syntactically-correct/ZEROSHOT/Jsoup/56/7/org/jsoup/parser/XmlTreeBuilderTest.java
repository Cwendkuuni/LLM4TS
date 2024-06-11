package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase, settings);
 }

 @Test
 public void testParseWithBaseUri() {
 String input = "<xml><tag attr=\"value\"><subTag/></tag></xml>";
 String baseUri = "http://example.com/";
 Document xmlDoc = xmlTreeBuilder.parse(input, baseUri);
 assertNotNull(xmlDoc);
 assertEquals(baseUri, xmlDoc.baseUri());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml><tag attr=\"value\"></tag>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 Element tag = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("tag", tag.tagName());
 assertEquals(baseUri, tag.baseUri());
 List<Attribute> attributes = tag.attributes();
 assertEquals(1, attributes.size());
 assertEquals("value", attributes.get(0).getValue());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml><tag><subTag/></tag></xml>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
 Element tag = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element subTag = (Element) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals("tag", tag.tagName());
 assertEquals("subTag", subTag.tagName());
 assertNull(tag.nextSibling());
 assertNull(subTag.previousSibling());
 }

 @Test
 public void testProcessComment() {
 String input = "<!-- This is a comment --><xml><tag attr=\"value\"></tag></xml>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("This is a comment", comment.data());
 assertEquals(baseUri, comment.baseUri());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml>&lt;tag&gt;<tag>text</tag></xml>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 TextNode textNode1 = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 TextNode textNode2 = (TextNode) xmlTreeBuilder.doc.childNodes().get(2);
 assertEquals("&lt;tag&gt;", textNode1.text());
 assertEquals("text", textNode2.text());
 assertEquals(baseUri, textNode1.baseUri());
 assertEquals(baseUri, textNode2.baseUri());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("-//W3C//DTD XML 1.0//EN", doctypeNode.publicId());
 assertEquals("xml.dtd", doctypeNode.systemId());
 assertEquals(baseUri, doctypeNode.baseUri());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml><tag><subTag><tag/></subTag></tag></xml>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.runParser();
 Element tag = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element subTag = (Element) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals(2, xmlTreeBuilder.stack.size());
 xmlTreebuilder.popStackToClose(tag.asEndTag());
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<tag attr=\"value\"><subTag/></tag>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
 assertEquals(2, nodes.size());
 Element tag = (Element) nodes.get(0);
 Element subTag = (Element) nodes.get(1);
 assertEquals("tag", tag.tagName());
 assertEquals("subTag", subTag.tagName());
 }
}