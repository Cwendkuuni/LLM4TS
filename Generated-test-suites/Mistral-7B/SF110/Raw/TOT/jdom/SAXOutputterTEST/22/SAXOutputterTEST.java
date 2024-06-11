package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SAXOutputterTest {

    @Test
    public void testEmptyDocument() throws JDOMException, SAXException {
        final Document document = new Document();
        final SAXOutputter saxOutputter = new SAXOutputter();
        final ContentHandler contentHandler = mock(ContentHandler.class);

        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.output(document);

        verify(contentHandler).startDocument();
        verify(contentHandler).endDocument();
        assertEquals("", contentHandler.getBuffer().toString());
    }

    @Test
    public void testDocumentWithRootElement() throws JDOMException, SAXException {
        final Document document = new Document(new Element("root"));
        final SAXOutputter saxOutputter = new SAXOutputter();
        final ContentHandler contentHandler = mock(ContentHandler.class);

        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.output(document);

        verify(contentHandler).startDocument();
        verify(contentHandler).startElement("root");
        verify(contentHandler).endElement("root");
        verify(contentHandler).endDocument();
        assertEquals("<root></root>", contentHandler.getBuffer().toString());
    }

    @Test
    public void testDocumentWithRootElementAndContent() throws JDOMException, SAXException {
        final Document document = new Document(new Element("root").addContent(new Element("child").setText("test")));
        final SAXOutputter saxOutputter = new SAXOutputter();
        final ContentHandler contentHandler = mock(ContentHandler.class);

        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.output(document);

        verify(contentHandler).startDocument();
        verify(contentHandler).startElement("root");
        verify(contentHandler).startElement("child");
        verify(contentHandler).characters("test".toCharArray(), 0, "test".length());
        verify(contentHandler).endElement("child");
        verify(contentHandler).endElement("root");
        verify(contentHandler).endDocument();
        assertEquals("<root><child>test</child></root>", contentHandler.getBuffer().toString());
    }

    @Test(expected = JDOMException.class)
    public void testNullDocument() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(null);
    }

    @Test(expected = SAXException.class)
    public void testErrorHandler() throws JDOMException, SAXException {
        final Document document = new Document();
        final SAXOutputter saxOutputter = new SAXOutputter();
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        final ContentHandler contentHandler = mock(ContentHandler.class);

        saxOutputter.setErrorHandler(errorHandler);
        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.output(document);

        verify(errorHandler).warning(any(SAXParseException.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContentHandler() {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(null);
        saxOutputter.output(new Document());
    }
}