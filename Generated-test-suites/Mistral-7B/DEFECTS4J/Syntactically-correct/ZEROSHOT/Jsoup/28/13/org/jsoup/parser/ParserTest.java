package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 @InjectMocks
 private Parser parser;

 private TreeBuilder treeBuilderMock;
 private Document doc;

 @Before
 public void setUp() {
 treeBuilderMock = Mockito.mock(TreeBuilder.class);
 doc = Mockito.mock(Document.class);
 parser = new Parser(treeBuilderMock);
 }

 @Test
 public void testParseInput_ValidHtml_ReturnsDocument() {
 String html = "<html><body></body></html>";
 String baseUri = "test";
 List<ParseError> expectedErrors = new ArrayList<>();
 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.eq(expectedErrors))).thenReturn(doc);

 Document actualDoc = parser.parseInput(html, baseUri);

 assertEquals(doc, actualDoc);
 assertNull(parser.getErrors());
 }

 @Test
 public void testParseInput_ValidHtmlWithErrors_ReturnsDocumentAndErrors() {
 String html = "<html><body><script>alert('foo');</script></body></html>";
 String baseUri = "test";
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(Mockito.mock(ParseError.class));
 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.eq(expectedErrors))).thenReturn(doc);

 Document actualDoc = parser.parseInput(html, baseUri);
 List<ParseError> actualErrors = parser.getErrors();

 assertEquals(doc, actualDoc);
 assertEquals(1, actualErrors.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInput_NullHtml_ThrowsNullPointerException() {
 String html = null;
 String baseUri = "test";

 parser.parseInput(html, baseUri);
 }

 @Test
 public void testParseInput_EmptyHtml_ReturnsDocument() {
 String html = "";
 String baseUri = "test";
 List<ParseError> expectedErrors = new ArrayList<>();
 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.eq(expectedErrors))).thenReturn(doc);

 Document actualDoc = parser.parseInput(html, baseUri);

 assertEquals(doc, actualDoc);
 assertNull(parser.getErrors());
 }

 @Test
 public void testParseInput_InvalidHtml_ThrowsIllegalArgumentException() {
 String html = "<html<body>";
 String baseUri = "test";
 List<ParseError> expectedErrors = new ArrayList<>();
 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.eq(expectedErrors))).thenThrow(new IllegalArgumentException());

 try {
 parser.parseInput(html, baseUri);
 } catch (IllegalArgumentException e) {
 // expected
 }

 List<ParseError> actualErrors = parser.getErrors();
 assertTrue(actualErrors.isEmpty());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new HtmlTreeBuilder());
 Parser parser2 = new Parser(new XmlTreeBuilder());

 assertEquals(HtmlTreeBuilder.class, parser1.getTreeBuilder().getClass());
 assertEquals(XmlTreeBuilder.class, parser2.getTreeBuilder().getClass());

 parser1.setTreeBuilder(new XmlTreeBuilder());
 assertEquals(XmlTreeBuilder.class, parser1.getTreeBuilder().getClass());
 }

 @Test
 public void testIsTrackErrors_DefaultIsFalse() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_MaxErrorsGreaterThanZero_IsTrue() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_MaxErrorsZero_IsFalse() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors_NoErrors_ReturnsEmptyList() {
 assertNotNull(parser.getErrors());
 assertTrue(parser.getErrors().isEmpty());
 }

 @Test
 public void testGetErrors_WithErrors_ReturnsListWithErrors() {
 String html = "<html><body><script>alert('foo');</script></body></html>";
 String baseUri = "test";
 List<ParseError> expectedErrors = new ArrayList<>();
 ParseError parseError1 = Mockito.mock(ParseError.class);
 expectedErrors.add(parseError1);
 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.eq(expectedErrors))).thenReturn(doc);

 parser.parseInput(html, baseUri);

 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(1, actualErrors.size());
 assertEquals(parseError1, actualErrors.get(0));
 }

 @Test
 public void testParse_ValidHtml_ReturnsDocument() {
 String html = "<html><body></body></html>";
 String baseUri = "test";
 Document actualDoc = Parser.parse(html, baseUri);

 assertEquals(doc, actualDoc);
 }

 @Test
 public void testParseFragment_ValidHtml_ReturnsListOfNodes() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = Mockito.mock(Element.class);
 String baseUri = "test";
 List<Node> expectedNodes = List.of(Mockito.mock(Element.class));
 Mockito.when(treeBuilderMock.parseFragment(fragmentHtml, context, baseUri, Mockito.eq(expectedErrors))).thenReturn(expectedNodes);

 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseBodyFragment_ValidHtml_ReturnsDocument() {
 String bodyHtml = "<body><div>Hello World</div></body>";
 String baseUri = "test";
 Document actualDoc = Parser.parseBodyFragment(bodyHtml, baseUri);

 assertEquals(doc, actualDoc);
 }

 @Test
 public void testParseBodyFragmentRelaxed_ValidHtml_ReturnsDocument() {
 String bodyHtml = "<body><div>Hello World</div></body>";
 String baseUri = "test";
 Document actualDoc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

 assertEquals(doc, actualDoc);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentRelaxed_Deprecated_ThrowsUnsupportedOperationException() {
 String bodyHtml = "<body><div>Hello World</div></body>";
 String baseUri = "test";

 Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 }
}