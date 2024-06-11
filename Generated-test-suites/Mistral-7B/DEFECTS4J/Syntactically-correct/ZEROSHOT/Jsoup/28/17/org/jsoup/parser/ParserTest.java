package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 @Mock
 private TreeBuilder treeBuilder;

 @InjectMocks
 private Parser parser;

 @Before
 public void setUp() {
 parser.setTreeBuilder(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body></body></html>";
 String baseUri = "http://example.com";

 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals(html, doc.html());

 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullHtml() {
 String baseUri = "http://example.com";
 parser.parseInput(null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullBaseUri() {
 String html = "<html></html>";
 parser.parseInput(html, null);
 }

 @Test
 public void testParseInputTrackErrors() {
 String html = "<html><head></head><body><div><p>Hello</p></div></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);

 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());

 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals(html, doc.html());

 errors = parser.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Tag start without end tag: p", error.getMessage());
 }

 @Test
 public void testParseInputTrackErrorsMaxLimit() {
 String html = "<html><head></head><body>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(2);

 for (int i = 0; i < 3; i++) {
 parser.parseInput(html, baseUri);
 }

 List<ParseError> errors = parser.getErrors();
 assertEquals(3, errors.size());
 }

 @Test
 public void testParseInputTrackErrorsEmptyList() {
 String html = "<html><head></head><body></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(0);

 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals(html, doc.html());

 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);
 verify(treeBuilder).parseInput(anyString(), anyString(), any(ParseErrorList.class));
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 verify(treeBuilder).setTrackErrors(1);
 }

 @Test
 public void testSetTrackErrorsNegativeNumber() {
 parser.setTrackErrors(-1);
 verify(treeBuilder).setTrackErrors(0);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Hello</p></div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals(fragmentHtml, nodes.get(0).outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullFragmentHtml() {
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 parser.parseFragment(null, context, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullContext() {
 String fragmentHtml = "<div><p>Hello</p></div>";
 String baseUri = "http://example.com";
 parser.parseFragment(fragmentHtml, null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullBaseUri() {
 String fragmentHtml = "<div><p>Hello</p></div>";
 Element context = mock(Element.class);
 parser.parseFragment(fragmentHtml, context, null);
 }

 @Test
 public void testParseFragmentTrackErrors() {
 String fragmentHtml = "<div><p>Hello</p><p>World</p></div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);

 List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals(fragmentHtml, nodes.get(0).outerHtml());

 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Tag 'p' is not closed", error.getMessage());
 }

 @Test
 public void testParseFragmentTrackErrorsMaxLimit() {
 String fragmentHtml = "<div><p>Hello</p><p>World</p><p>!</p></div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 parser.setTrackErrors(2);

 List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals(fragmentHtml, nodes.get(0).outerHtml());

 List<ParseError> errors = parser.getErrors();
 assertEquals(3, errors.size());
 }

 @Test
 public void testParseFragmentEmptyList() {
 String fragmentHtml = "<div></div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 parser.setTrackErrors(0);

 List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertNotNull(nodes.get(0));
 assertEquals(fragmentHtml, nodes.get(0).outerHtml());

 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><head></head><body><div><p>Hello</p><p>World</p></div></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(doc);
 assertEquals(bodyHtml, doc.html());

 Element body = doc.body();
 assertEquals(2, body.childNodes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 String baseUri = "http://example.com";
 parser.parseBodyFragment(null, baseUri);
 }

 @Test
 public void testParseBodyFragmentEmptyBodyHtml() {
 String baseUri = "http://example.com";
 Document doc = parser.parseBodyFragment("", baseUri);
 assertNotNull(doc);
 assertEquals("", doc.html());

 Element body = doc.body();
 assertEquals(0, body.childNodes().size());
 }
}