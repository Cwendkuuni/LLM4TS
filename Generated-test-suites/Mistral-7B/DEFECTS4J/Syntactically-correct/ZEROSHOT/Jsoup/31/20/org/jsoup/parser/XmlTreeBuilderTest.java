package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 }

 @Test
 public void testInitialiseParse() {
 // test if document is placed on stack
 assertEquals(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 // create a start tag token
 Token startTagToken = new Token(Tag.valueOf("tag"), null);

 // call process method with start tag
 xmlTreeBuilder.process(startTagToken.asStartTag());

 // check if element is inserted into document
 Element element = (Element) xmlTreeBuilder.currentElement();
 assertEquals("tag", element.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 // create a start tag token
 Token startTagToken = new Token(Tag.valueOf("tag"), null);
 Element startTagElement = xmlTreeBuilder.insert(startTagToken.asStartTag());
 xmlTreeBuilder.stack.add(startTagElement);

 // create an end tag token
 Token endTagToken = new Token(Tag.valueOf("tag").getName(), null).asEndTag();

 // call process method with end tag
 xmlTreeBuilder.process(endTagToken);

 // check if element is removed from stack
 assertEquals(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 // create a comment token
 Token commentToken = new Token("--", null);

 // call process method with comment token
 xmlTreeBuilder.process(commentToken.asComment());

 // check if comment is inserted into document
 Comment comment = (Comment) xmlTreeBuilder.currentElement();
 assertEquals("--", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 // create a character token
 Token characterToken = new Token("A", null);

 // call process method with character token
 xmlTreeBuilder.process(characterToken.asCharacter());

 // check if text node is inserted into document
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement();
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 // create a doctype token
 Token doctypeToken = new Token("!DOCTYPE", null).asDoctype();

 // call process method with doctype token
 xmlTreeBuilder.process(doctypeToken);

 // check if document type is inserted into document
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement();
 assertEquals("", documentTypeNode.systemId());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 // create a unknown token
 Token unknownToken = new Token(1234, null);

 // call process method with unknown token
 xmlTreeBuilder.process(unknownToken);
 }
}