package org.jdom2.output;

import org.junit.Before;
import org.junit.Test;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.SAXOutputter;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SAXOutputterTest {

 private Document document;
 private SAXOutputter outputter;
 private ContentHandler contentHandlerMock;

 @Before
 public void setUp() {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 contentHandlerMock = Mockito.mock(ContentHandler.class);
 outputter = new SAXOutputter(contentHandlerMock);
 }

 @Test
 public void testOutputDocument() throws SAXException {
 // Test outputting a document with no errors
 outputter.output(document);
 Mockito.verify(contentHandlerMock).startDocument();
 Mockito.verify(contentHandlerMock).startElement("", "", "document");
 Mockito.verify(contentHandlerMock).endElement("", "", "document");
 Mockito.verify(contentHandlerMock).endDocument();
 }

 @Test(expected = JDOMException.class)
 public void testOutputDocumentWithError() throws JDOMException, SAXException {
 // Test outputting a document with an error
 document.setError("Test Error");
 outputter.output(document);
 Mockito.verify(contentHandlerMock).startDocument();
 Mockito.verify(contentHandlerMock).startElement("", "", "document");
 outputter.handleError(new JDOMException("Test Error"));
 Mockito.verify(contentHandlerMock).endDocument();
 }

 @Test
 public void testOutputList() throws SAXException {
 // Test outputting a list of elements
 List<Element> list = new ArrayList<>();
 list.add(new Element("element1"));
 list.add(new Element("element2"));
 outputter.output(list);
 Mockito.verify(contentHandlerMock, Mockito.times(2)).startElement("", "", "element");
 Mockito.verify(contentHandlerMock, Mockito.times(2)).endElement("", "", "element");
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullList() throws JDOMException, SAXException {
 // Test outputting a null list
 outputter.output(null);
 }

 @Test
 public void testOutputNullDocument() throws SAXException {
 // Test outputting a null document
 outputter.output(null);
 Mockito.verify(contentHandlerMock).startDocument();
 Mockito.verify(contentHandlerMock).endDocument();
 }

 @Test
 public void testOutputNullContent() throws SAXException {
 // Test outputting a null content
 outputter.outputFragment(null);
 }
}