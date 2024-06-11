package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parse.ParseSettings;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseSimpleXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.rootElement();
 Element child = root.child(0);
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXmlWithEmptyElements() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><empty/><empty/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.rootElement();
 List<Element> emptyElements = root.children();
 assertEquals(2, emptyElements.size());
 for (Element emptyElement : emptyElements) {
 assertNull(emptyElement.text());
 }
 }

 @Test
 public void testParseXmlWithSelfClosingTags() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><img src=\"image.png\" /> <link href=\"link.html\" /> </root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.rootElement();
 List<Element> imgAndLink = root.children();
 assertEquals(2, imgAndLink.size());
 Element img = imgAndLink.get(0);
 assertEquals("img", img.tagName());
 assertFalse(img.hasText());
 Element link = imgAndLink.get(1);
 assertEquals("link", link.tagName());
 assertFalse(link.hasText());
 }

 @Test
 public void testParseXmlWithComments() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><!-- This is a comment --><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.rootElement();
 Element child = root.child(0);
 assertEquals("Hello World", child.text());
 Comment comment = root.child(0).sibling(1);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlWithCDataSection() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><![CDATA[This is a CData section<br>]]><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.rootElement();
 Element child = root.child(1);
 assertEquals("Hello World", child.text());
 CDataNode cdataNode = (CDataNode) root.child(0);
 assertEquals("This is a CData section<br>", cdataNode.getData());
 }

 @Test(expected = RuntimeException.class)
 public void testParseXmlWithInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child>Hello<script> World </child>";
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 } catch (Exception e) {
 //Expected exception, no need to assert
 }
 Validate.fail("Should have thrown RuntimeException");
 }

 @Test(expected = RuntimeException.class)
 public void testParseXmlWithInvalidDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE root [<!ENTITY % foo SYSTEM \"foo.txt\"> <!ENTITY foo \"%foo;%\">]>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Validate.fail("Should have thrown RuntimeException");
 }
}