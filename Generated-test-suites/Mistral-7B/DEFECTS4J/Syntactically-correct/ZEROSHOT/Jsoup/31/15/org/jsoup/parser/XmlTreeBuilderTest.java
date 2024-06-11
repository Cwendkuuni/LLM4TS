package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 tokeniser = new Tokenizer("<?xml version=\"1.0\"?>\n<root><element attr=\"value\"><subElement/></element></root>");
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\"?>\n<root><element attr=\"value\"><subElement/></element></root>", null, null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = tokeniser.nextToken();
 Element element = xmlTreeBuilder.insert(startTagToken.asStartTag());
 assertNotNull(element);
 assertTrue(element.nodeName().equals("element"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = tokeniser.nextToken();
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = tokeniser.nextToken();
 xmlTreeBuilder.insert(characterToken.asCharacter());
 Element root = xmlTreeBuilder.stack.peek();
 TextNode textNode = (TextNode) root.childNodes().get(0);
 assertTrue(textNode.text().equals("<?xml version=\"1.0\"?"));
 }

 @Test
 public void testProcessComment() {
 Token commentToken = tokeniser.nextToken();
 xmlTreeBuilder.insert(commentToken.asComment());
 Comment comment = (Comment) doc.childNodes().get(1);
 assertTrue(comment.data().equals("-->"));
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = tokeniser.nextToken();
 xmlTreeBuilder.insert(doctypeToken.asDoctype());
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertTrue(doctypeNode.name().equals("xml"));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 tokeniser.nextToken(); // this line should throw an AssertionError
 }
}