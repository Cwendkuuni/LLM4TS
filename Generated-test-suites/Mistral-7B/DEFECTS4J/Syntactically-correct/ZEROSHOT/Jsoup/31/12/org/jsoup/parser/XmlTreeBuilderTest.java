package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("tag", null);
 Element el = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", el.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token token = new Token(123);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertNode() {
 Node node = new Element("tag");
 xmlTreeBuilder.insertNode(node);
 assertEquals(node, doc.childNodes().get(0));
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(commentToken.getData(), doc.childNodes().get(0).nodeValue());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 assertEquals("a", doc.childNodes().get(0).nodeValue());
 }

 @Test
 public void testInsertDocumentType() {
 Token documentType = new Token.Doctype("doctype");
 xmlTreeBuilder.insert(documentType);
 assertTrue(doc.childNodes().get(0) instanceof DocumentType);
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.stack.push(new Element("tag"));
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testPopStackToCloseKnownTag() {
 xmlTreeBuilder.stack.push(new Element("tag"));
 xmlTreeBuilder.stack.push(new Element("parent"));
 xmlTreeBuilder.stack.push(new Element("parent2"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 assertEquals(2, xmlTreeBuilder.stack.size());
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 }
}