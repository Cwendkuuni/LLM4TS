package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document(); // initialize parse with an empty document to test stack behaviour
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", null);
 assertTrue(doc.content().size() == 0); // document should be empty
 assertTrue(xmlTreeBuilder.stack.size() == 1); // document should be on the stack
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 xmlTreeBuilder.initialiseParse(null, "", null);
 Validate.fail("Unexpected execution path: method should throw IllegalArgumentException");
 }

 @Test
 public void testProcessStartTag() {
 Element root = xmlTreeBuilder.insert(new Token.StartTag("root", new ArrayList<Attribute>(), false));
 assertEquals("root", root.nodeName());
 assertTrue(doc.content().size() == 1);
 assertTrue(xmlTreeBuilder.stack.size() == 1);
 }

 @Test
 public void testProcessSelfClosingStartTag() {
 Element root = xmlTreeBuilder.insert(new Token.StartTag("root", new ArrayList<Attribute>(), true));
 assertEquals("root", root.nodeName());
 assertTrue(doc.content().size() == 1);
 assertTrue(xmlTreeBuilder.stack.size() == 0);
 }

 @Test
 public void testProcessEndTag() {
 Element root = xmlTreeBuilder.insert(new Token.StartTag("root", new ArrayList<Attribute>(), false));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
 assertEquals(0, doc.content().size());
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEndTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("nonExistentTag"));
 Validate.fail("Unexpected execution path: method should throw IllegalArgumentException");
 }

 @Test
 public void testProcessComment() {
 xmlTreeBuilder.insert(new Token.Comment("This is a comment"));
 Element comment = (Element) doc.content().get(0);
 assertEquals("This is a comment", comment.text().text());
 }

 @Test
 public void testProcessCharacter() {
 xmlTreeBuilder.insert(new Token.Character('A'));
 TextNode textNode = (TextNode) doc.content().get(0);
 assertEquals("A", textNode.text().text());
 }

 @Test
 public void testProcessDoctype() {
 xmlTreeBuilder.insert(new Token.Doctype("html", "", "", ""));
 DocumentType documentType = (DocumentType) doc.content().get(0);
 assertEquals("html", documentType.name());
 assertNull(documentType.publicIdentifier());
 assertNull(documentType.systemIdentifier());
 }

 @Test
 public void testProcessCharacterAndComment() {
 xmlTreeBuilder.insert(new Token.Character('A'));
 xmlTreeBuilder.insert(new Token.Comment("This is a comment"));
 TextNode textNode = (TextNode) doc.content().get(0);
 assertEquals("A", textNode.text().text());
 Element comment = (Element) doc.content().get(1);
 assertEquals("This is a comment", comment.text().text());
 }

 @Test
 public void testProcessMultipleTags() {
 List<Token> tokens = Arrays.asList(
 new Token.StartTag("root", new ArrayList<Attribute>(), false),
 new Token.StartTag("tag1", new ArrayList<Attribute>(), false),
 new Token.StartTag("tag2", new ArrayList<Attribute>(), false),
 new Token.EndTag("tag2"),
 new Token.EndTag("tag1"),
 new Token.EndTag("root")
 );
 for (Token token : tokens) {
 xmlTreeBuilder.process(token);
 }
 Element root = (Element) doc.content().get(0);
 List<Element> tag1 = root.children();
 List<Element> tag2 = tag1.get(0).children();
 assertEquals(2, root.children().size());
 assertEquals(1, tag1.size());
 assertEquals(1, tag2.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessAfterEOF() {
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(new Token.EOF());
 Validate.fail("Unexpected execution path: method should throw IllegalStateException");
 }
}