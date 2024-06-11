package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class ParserTest {

 @Test
 public void testParseHtml() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
 Document doc = Parser.parse(html, "http://test.com");

 Element htmlNode = doc.body();
 Element headNode = doc.head();
 Element titleNode = headNode.title();

 Assert.assertEquals("Test Title", titleNode.text());
 Assert.assertEquals("Test Heading", htmlNode.select("h1").text());
 Assert.assertEquals("Test Paragraph.", htmlNode.select("p").text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Heading</h1><p>Test Paragraph.</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

 Element htmlNode = doc.body();
 Element h1Node = htmlNode.selectFirst("h1");
 Element pNode = htmlNode.selectFirst("p");

 Assert.assertEquals("Test Heading", h1Node.text());
 Assert.assertEquals("Test Paragraph.", pNode.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div><p><span>Test</span></p></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");

 Element htmlNode = doc.body();
 Element divNode = htmlNode.selectFirst("div");
 Element pNode = divNode.selectFirst("p");
 Element spanNode = pNode.selectFirst("span");

 Assert.assertNotNull(spanNode);
 Assert.assertEquals("Test", spanNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidBaseUri() {
 Parser.parse("</html>", null);
 }
}