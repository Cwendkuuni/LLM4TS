package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 builder.initialiseParse(input, baseUri, errors);
 assertEquals("http://example.com", builder.doc().baseUri());
 assertNotNull(builder.doc());
 }

 @Test(expected = AssertionError.class)
 public void testProcessInvalidTokenType() {
 Token token = new Token(-1, "Invalid token type");
 builder.process(token);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag attr='value'>");
 Element tag = builder.insert(startTagToken);
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.attribute(0).getValue());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 builder.popStackToClose(endTagToken);
 assertTrue(builder.stack().isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "-- comment --");
 builder.insert(commentToken);
 assertNotNull(builder.childNodes().get(0));
 assertEquals("-- comment --", builder.childNodes().get(0).text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, "char");
 builder.insert(characterToken);
 assertNotNull(builder.childNodes().get(0));
 assertEquals("char", builder.childNodes().get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE tag [>\n<!ATTLIST tag attr CDATA #REQUIRED>\n<!ELEMENT tag (#PCDATA)>\n<!DOCTYPE tag SYSTEM \"http://example.com/file.dtd\">\n<!DOCTYPE tag PUBLIC \"TagDTD\" \"-//W3C//DTD Tag DTD 1.0//EN\" >\n<tag>test</tag>\n<!DOCTYPE xml [>\n<!ATTLIST xml xml-version CDATA #IMPLIED>\n<!ELEMENT xml (#PCDATA)>\n<!DOCTYPE xml SYSTEM \"xmlversion.dtd\">\n<!DOCTYPE xml PUBLIC \"XmlDTD\" \"-//W3C//DTD Xml DTD 1.0//EN\" >\n<xml version='1.0'>\n</xml>", null);
 builder.insert(doctypeToken);
 assertEquals(3, builder.childNodes().size());
 Element tagElement = (Element) builder.childNodes().get(1);
 assertEquals("tag", tagElement.nodeName());
 assertEquals("1.0", tagElement.attribute(0).getValue());
 }
}