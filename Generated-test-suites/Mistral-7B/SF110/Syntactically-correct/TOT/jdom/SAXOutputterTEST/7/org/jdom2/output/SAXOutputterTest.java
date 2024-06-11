package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SAXOutputterTest {

 private ContentHandler contentHandlerMock;
 private ErrorHandler errorHandlerMock;
 private DTDHandler dtdHandlerMock;
 private EntityResolver entityResolverMock;
 private LexicalHandler lexicalHandlerMock;
 private DeclHandler declHandlerMock;
 private SAXOutputter saxOutputter;

 @Before
 public void setup() {
 contentHandlerMock = mock(ContentHandler.class);
 errorHandlerMock = mock(ErrorHandler.class);
 dtdHandlerMock = mock(DTDHandler.class);
 entityResolverMock = mock(EntityResolver.class);
 lexicalHandlerMock = mock(LexicalHandler.class);
 declHandlerMock = mock(DeclHandler.class);
 saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock, declHandlerMock);
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Text("Hello World"));

 saxOutputter.output(document);

 verify(contentHandlerMock).startDocument();
 verify(contentHandlerMock).startElement("root");
 verify(contentHandlerMock).characters(isA(CharSequence.class), eq("Hello World"));
 verify(contentHandlerMock).endElement("root");
 verify(contentHandlerMock).endDocument();
 }

 @Test
 public void testOutputListOfContents() throws JDOMException {
 List<Content> nodes = new ArrayList<>();
 nodes.add(new Text("List item 1"));
 nodes.add(new Text("List item 2"));

 saxOutputter.output(nodes);

 verify(contentHandlerMock, times(2)).startElement("text");
 verify(contentHandlerMock, times(2)).characters(isA(CharSequence.class), anyObject());
 verify(contentHandlerMock, times(2)).endElement("text");
 }

 @Test
 public void testOutputElement() throws JDOMException {
 Element element = new Element("element");
 element.addContent(new Text("Content"));

 saxOutputter.output(element);

 verify(contentHandlerMock).startDocument();
 verify(contentHandlerMock).startElement("element");
 verify(contentHandlerMock).characters(isA(CharSequence.class), eq("Content"));
 verify(contentHandlerMock).endElement("element");
 verify(contentHandlerMock).endDocument();
 }

 @Test
 public void testOutputFragmentListOfContents() throws JDOMException {
 List<Content> nodes = new ArrayList<>();
 nodes.add(new Text("List item 1"));
 nodes.add(new Text("List item 2"));

 saxOutputter.outputFragment(nodes);

 verify(contentHandlerMock, times(2)).startElement("text");
 verify(contentHandlerMock, times(2)).characters(isA(CharSequence.class), anyObject());
 verify(contentHandlerMock, times(2)).endElement("text");
 }

 @Test
 public void testOutputFragmentText() throws JDOMException {
 Text text = new Text("Test Text");

 saxOutputter.outputFragment(text);

 verify(contentHandlerMock).startDocument();
 verify(contentHandlerMock).startElement("text");
 verify(contentHandlerMock).characters(isA(CharSequence.class), eq("Test Text"));
 verify(contentHandlerMock).endElement("text");
 verify(contentHandlerMock).endDocument();
 }

 @Test(expected = JDOMException.class)
 public void testErrorWithInvalidContent() throws JDOMException {
 Content invalidContent = new Object();
 saxOutputter.outputFragment(invalidContent);
 }
}