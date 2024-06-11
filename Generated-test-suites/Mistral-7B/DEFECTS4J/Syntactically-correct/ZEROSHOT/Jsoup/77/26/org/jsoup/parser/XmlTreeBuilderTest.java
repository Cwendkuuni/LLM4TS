package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><child><grandchild/></child></root>"), "http://test.com");
 Equals verboseEquals = new Equals();
 assertEquals(verboseEquals, xmlDoc.rootElement());
 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 assertNotNull(root.child(0));
 assertEquals("child", root.child(0).tagName());
 assertNotNull(root.child(0).child(0));
 assertEquals("grandchild", root.child(0).child(0).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 XmlTreeBuilder.parse(new StringReader("<root/>"), null); // should throw an IllegalArgumentException
 }

 @Test
 public void testParseFragment() {
 List<Node> fragment = XmlTreeBuilder.parseFragment("<root><child/></root>", "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 Element root = (Element) fragment.get(0);
 assertEquals("root", root.tagName());
 assertNotNull(root.child(0));
 assertEquals("child", root.child(0).tagName());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("root", null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0);
 Element el = XmlTreeBuilder.instance().insert(startTag);
 assertEquals("root", el.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root", null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0);
 XmlTreeBuilder.instance().popStackToClose(endTag);
 assertEquals(0, XmlTreeBuilder.instance().stack.size());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("<!--test-->", null);
 XmlTreeBuilder.instance().insert(comment);
 Comment commentNode = (Comment) XmlTreeBuilder.instance().doc.lastChild();
 assertEquals("test", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token.Character('A', null, 0, false, null, null, null, null, null, null, null, null, null, null, null, null, 0);
 TextNode textNode = (TextNode) XmlTreeBuilder.instance().insert(character);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token.Doctype("html", null, null);
 DocumentType doctypeNode = (DocumentType) XmlTreeBuilder.instance().insert(doctype);
 assertEquals("html", doctypeNode.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testValidateFail() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.Validate.fail("Unexpected token type: " + 123); // should throw an AssertionError
 }
}