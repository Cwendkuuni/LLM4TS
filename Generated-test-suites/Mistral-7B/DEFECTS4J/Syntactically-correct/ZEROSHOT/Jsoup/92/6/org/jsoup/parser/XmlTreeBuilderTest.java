package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for XmlTreeBuilder
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 @Mock
 private Parser parser;
 @Mock
 private Element context;
 @Mock
 private Document document;
 @Mock
 private TextNode textNode;
 @Mock
 private Element element;
 @Mock
 private Comment comment;
 @Mock
 private CDataNode cDataNode;
 @Mock
 private DocumentType documentType;
 @Mock
 private Token startTag;
 @Mock
 private Token endTag;
 @Mock
 private Token character;
 @Mock
 private Token doctype;
 @Mock
 private Token bogusComment;
 @Mock
 private Stack<Node> stack;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.stack = stack;
 xmlTreeBuilder.parser = parser;
 xmlTreeBuilder.context = context;
 xmlTreeBuilder.document = document;
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 }

 @Test
 public void testParse() {
 // Given
 String input = "<test><test1><test2></test2></test1></test>";
 // When
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(input, null, null);
 // Then
 assertEquals(5, actualNodes.size());
 verifyNodesOrder(actualNodes, "test", "test1", "test2", null, null);
 }

 @Test
 public void testParseWithContext() {
 // Given
 String input = "<test><test1><test2></test2></test1></test>";
 // When
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(input, context, null, parser);
 // Then
 assertEquals(5, actualNodes.size());
 verifyNodesOrder(actualNodes, "test", "test1", "test2", context, null);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessWithUnexpectedTokenType() {
 // Given
 xmlTreeBuilder.parse(new StringReader(""), null, parser);
 Token unexpectedToken = mock(Token.class);
 when(unexpectedToken.type).thenReturn(-1);
 // When
 // Then
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcessStartTag() {
 // Given
 String input = "<test>";
 Token startTagToken = new StartTag("test", null, null);
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(startTagToken);
 // And
 assertEquals(1, stack.size());
 verify(stack).add(document);
 }

 @Test
 public void testProcessEndTag() {
 // Given
 String input = "<test><test1></test1></test>";
 Token endTagToken = new EndTag("test1");
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(new StartTag("test"));
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 // And
 assertEquals(1, stack.size());
 verify(stack, Mockito.never()).remove(Mockito.any());
 }

 @Test
 public void testProcessEndTagWithNonExistentTag() {
 // Given
 String input = "<test><test1></test1></test>";
 Token endTagToken = new EndTag("test3");
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(new StartTag("test"));
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 // And
 assertEquals(1, stack.size());
 verify(stack, Mockito.never()).remove(Mockito.any());
 }

 @Test
 public void testProcessComment() {
 // Given
 String input = "<!-- test -->";
 Token commentToken = new Comment("test");
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(commentToken);
 // And
 assertEquals(1, stack.size());
 verify(stack).add(comment);
 }

 @Test
 public void testProcessBogusCommentAsXmlDeclaration() {
 // Given
 String input = "<?xml version='1.0' encoding='UTF-8'?>";
 Token bogusCommentToken = bogusComment;
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(bogusCommentToken);
 // And
 verify(document).setXmlDeclaration(new XmlDeclaration("1.0", "UTF-8"));
 }

 @Test
 public void testProcessCharacter() {
 // Given
 String input = "<test>test</test>";
 Token characterToken = new Character('t');
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(characterToken);
 // And
 assertEquals(2, stack.size());
 verify(stack).add(textNode);
 }

 @Test
 public void testProcessCData() {
 // Given
 String input = "<test><![CDATA[test]]></test>";
 Token cDataToken = new Character('[');
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(cDataToken);
 // And
 assertEquals(2, stack.size());
 verify(stack).add(cDataNode);
 }

 @Test
 public void testProcessDoctype() {
 // Given
 String input = "<!DOCTYPE test [ <!ENTITY testEntity \"test\"> ]>";
 Token doctypeToken = new Doctype("test", null, null, null, null);
 when(parser.parse(Mockito.any(Reader.class), Mockito.anyString())).thenReturn(document);
 when(parser.getInput()).thenReturn(new StringReader(input));
 // When
 xmlTreeBuilder.initialiseParse(parser.getInput(), null, parser);
 // Then
 xmlTreeBuilder.process(doctypeToken);
 // And
 assertEquals(1, stack.size());
 verify(stack).add(documentType);
 }

 private void verifyNodesOrder(List<Node> actualNodes, String... expectedTagNames) {
 for (int i = 0; i < expectedTagNames.length; i++) {
 Node expectedNode = i < expectedTagNames.length - 1 ? element : context;
 assertEquals(expectedTagNames[i], actualNodes.get(i).nodeName());
 assertEquals(expectedNode, actualNodes.get(i).parent());
 }
 }
}