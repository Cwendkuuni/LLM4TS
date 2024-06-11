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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("unchecked")
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Parser parser;
 private AnnotatedClassLoader classLoader;

 @Before
 public void setup() throws Exception {
 classLoader = new AnnotatedClassLoader();
 parser = mock(Parser.class);
 xmlTreeBuilder = new XmlTreeBuilder() {
 @Override
 protected Parser parser() {
 return parser;
 }
 };
 xmlTreeBuilder.setClassLoader(classLoader);
 document = mock(Document.class);
 PowerMockito.whenNew(Document.class).withArguments(any(), anyString()).thenReturn(document);
 }

 @Test
 public void testParse() throws IOException {
 String xmlString = "<root><child><subchild/></child></root>";
 ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
 ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());
 Document parsedDoc = xmlTreeBuilder.parse(reader, "http://www.example.com");
 Element root = parsedDoc.rootElement();
 Element child = root.child(0);
 Element subchild = child.child(0);

 assertThat(parsedDoc, is(instanceOf(Document.class)));
 assertThat(root.nodeName(), is("root"));
 assertThat(child.nodeName(), is("child"));
 assertThat(subchild, is(instanceOf(Element.class)));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child><subchild/></child></root>";
 String baseUri = "http://www.example.com";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri);
 Document parsedDoc = new Document();
 parsedDoc.setRootElements(fragmentNodes);
 Element root = parsedDoc.rootElement();
 Element child = root.child(0);
 Element subchild = child.child(0);

 assertThat(parsedDoc, is(instanceOf(Document.class)));
 assertThat(root.nodeName(), is("root"));
 assertThat(child.nodeName(), is("child"));
 assertThat(subchild, is(instanceOf(Element.class)));
 }

 @Test
 public void testProcessStartTag() throws IOException {
 Token startTagToken = new Token(Token.StartTag.class, "<root>", 1, 1, 1, 1, null, null);
 xmlTreeBuilder.process(startTagToken);
 Element root = (Element) Mockito.capture(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));

 assertThat(root.nodeName(), is("root"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</root>", 1, 1, 1, 1, null, null);
 xmlTreeBuilder.stack.add(document);
 xmlTreeBuilder.stack.add(document);
 xmlTreeBuilder.process(endTagToken);
 verify(document, Mockito.times(1)).remove();
 }

 @Test
 public void testProcessComment() {
 String commentText = "-- This is a comment";
 Token commentToken = new Token(Token.Comment.class, "-- This is a comment", 1, 1, 1, 1, null, null);
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) Mockito.capture(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));

 assertThat(comment.getData(), is(commentText));
 }

 @Test
 public void testProcessCharacter() {
 char character = 'A';
 Token characterToken = new Token(Token.Character.class, character, 1, 1, 1, 1, null, null);
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) Mockito.capture(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));

 assertThat(textNode.text(), is(Character.toString(character)));
 }

 @Test
 public void testProcessDoctype() {
 String doctypeName = "html";
 String publicId = "public id";
 String systemId = "system id";
 String pubSysKey = null;
 Token doctypeToken = new Token(Token.Doctype.class, doctypeName, 1, 1, 1, 1, publicId, systemId);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) Mockito.capture(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));

 assertThat(doctypeNode.nodeName(), is("html"));
 assertThat(doctypeNode.getPublicIdentifier(), is(publicId));
 assertThat(doctypeNode.getSystemIdentifier(), is(systemId));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidToken() {
 Token invalidToken = new Token(5, null, 1, 1, 1, 1, null, null);
 xmlTreeBuilder.process(invalidToken);
 }
}