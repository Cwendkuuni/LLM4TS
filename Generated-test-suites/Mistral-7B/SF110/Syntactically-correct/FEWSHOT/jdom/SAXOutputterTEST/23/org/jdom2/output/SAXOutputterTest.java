package org.jdom2.output;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SAXOutputterTest {

    @InjectMocks
    private SAXOutputter saxOutputter;

    @Mock
    private ContentHandler contentHandlerMock;

    @Mock
    private ErrorHandler errorHandlerMock;

    @Mock
    private DTDHandler dtdHandlerMock;

    @Mock
    private EntityResolver entityResolverMock;

    @Mock
    private LexicalHandler lexicalHandlerMock;

    @Mock
    private DeclHandler declHandlerMock;

    @Mock
    private SAXOutputProcessor processorMock;

    @Mock
    private Format formatMock;

    @Mock
    private Document documentMock;

    @Mock
    private Element elementMock;

    @Test
    public void testSAXOutputterInitialization() {
        // default constructor
        SAXOutputter saxOutputter = new SAXOutputter();
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getFormat());

        // constructor with ContentHandler
        saxOutputter = new SAXOutputter(contentHandlerMock);
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getFormat());

        // constructor with ContentHandler, ErrorHandler, DTDHandler, EntityResolver
        saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock);
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
        assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
        assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
        assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getFormat());

        // constructor with ContentHandler, ErrorHandler, DTDHandler, EntityResolver, LexicalHandler
        saxOutputter = new SAXOutputter(contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock);
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
        assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
        assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
        assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getFormat());

        // constructor with SAXOutputProcessor, Format, ContentHandler, ErrorHandler, DTDHandler, EntityResolver, LexicalHandler
        saxOutputter = new SAXOutputter(processorMock, formatMock, contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock);
        assertNull(saxOutputter.getDeclareNamespaces());
        assertTrue(saxOutputter.getReportDtdEvents());
        assertEquals(processorMock, saxOutputter.getSAXOutputProcessor());
        assertEquals(formatMock, saxOutputter.getFormat());
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
        assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
        assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
        assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());

        // constructor with SAXOutputProcessor, Format, ContentHandler, ErrorHandler, DTDHandler, EntityResolver, LexicalHandler, DeclHandler
        saxOutputter = new SAXOutputter(processorMock, formatMock, contentHandlerMock, errorHandlerMock, dtdHandlerMock, entityResolverMock, lexicalHandlerMock, declHandlerMock);
        assertNull(saxOutputter.getDeclareNamespaces());
        assertTrue(saxOutputter.getReportDtdEvents());
        assertEquals(processorMock, saxOutputter.getSAXOutputProcessor());
        assertEquals(formatMock, saxOutputter.getFormat());
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());
        assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());
        assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());
        assertEquals(entityResolverMock, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());
        assertEquals(declHandlerMock, saxOutputter.getDeclHandler());
    }

    @Test
    public void testSettersAndGetters() {
        // setters and getters for declareNamespaces
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());

        // setters and getters for reportDtdEvents
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());

        // setters and getters for SAXOutputProcessor
        saxOutputter.setSAXOutputProcessor(processorMock);
        assertEquals(processorMock, saxOutputter.getSAXOutputProcessor());

        // setters and getters for Format
        saxOutputter.setFormat(formatMock);
        assertEquals(formatMock, saxOutputter.getFormat());

        // setters and getters for ContentHandler
        saxOutputter.setContentHandler(contentHandlerMock);
        assertEquals(contentHandlerMock, saxOutputter.getContentHandler());

        // setters and getters for ErrorHandler
        saxOutputter.setErrorHandler(errorHandlerMock);
        assertEquals(errorHandlerMock, saxOutputter.getErrorHandler());

        // setters and getters for DTDHandler
        saxOutputter.setDTDHandler(dtdHandlerMock);
        assertEquals(dtdHandlerMock, saxOutputter.getDTDHandler());

        // setters and getters for EntityResolver
        saxOutputter.setEntityResolver(entityResolverMock);
        assertEquals(entityResolverMock, saxOutputter.getEntityResolver());

        // setters and getters for LexicalHandler
        saxOutputter.setLexicalHandler(lexicalHandlerMock);
        assertEquals(lexicalHandlerMock, saxOutputter.getLexicalHandler());

        // setters and getters for DeclHandler
        saxOutputter.setDeclHandler(declHandlerMock);
        assertEquals(declHandlerMock, saxOutputter.getDeclHandler());
    }

    @Test
    public void testOutputDocument() throws SAXException, JDOMException {
        // given
        when(processorMock.process(any(SAXTarget.class), eq(formatMock), any(Document.class))).thenReturn(true);

        // when
        saxOutputter.output(documentMock);

        // then
        verify(processorMock).process(any(SAXTarget.class), eq(formatMock), eq(documentMock));
    }

    @Test
    public void testOutputList() throws SAXException, JDOMException {
        // given
        List<Content> nodes = new ArrayList<>();
        nodes.add(elementMock);
        when(processorMock.processAsDocument(any(SAXTarget.class), eq(formatMock), anyList())).thenReturn(true);

        // when
        saxOutputter.output(nodes);

        // then
        verify(processorMock).processAsDocument(any(SAXTarget.class), eq(formatMock), eq(nodes));
    }

    @Test
    public void testOutputElement() throws SAXException, JDOMException {
        // given
        when(processorMock.processAsDocument(any(SAXTarget.class), eq(formatMock), any(Element.class))).thenReturn(true);

        // when
        saxOutputter.output(elementMock);

        // then
        verify(processorMock).processAsDocument(any(SAXTarget.class), eq(formatMock), eq(elementMock));
    }

    @Test
    public void testOutputFragmentList() throws SAXException, JDOMException {
        // given
        List<Content> nodes = new ArrayList<>();
        nodes.add(textMock);
        when(processorMock.process(any(SAXTarget.class), eq(formatMock), anyList())).thenReturn(true);

        // when
        saxOutputter.outputFragment(nodes);

        // then
        verify(processorMock).process(any(SAXTarget.class), eq(formatMock), eq(nodes));
    }

    @Test
    public void testOutputFragmentContent() throws SAXException, JDOMException {
        // given
        Content node = textMock;
        when(processorMock.process(any(SAXTarget.class), eq(formatMock), any(Content.class)))
                .thenAnswer(invocationOnMock -> {
                    Content content = invocationOnMock.getArgument(0);
                    processContent(content);
                    return true;
                });

        // when
        saxOutputter.outputFragment(node);

        // then
        verify(processorMock).process(any(SAXTarget.class), eq(formatMock), eq(node));
    }

    @Test
    public void testErrorHandling() throws SAXException, JDOMException {
        // given
        JDOMException exception = new JDOMException("Test exception");
        when(processorMock.process(any(SAXTarget.class), eq(formatMock), anyList()))
                .thenThrow(new SAXParseException("Test exception", null, exception));
        when(errorHandlerMock.error(any(SAXParseException.class))).thenReturn();

        // when
        saxOutputter.setErrorHandler(errorHandlerMock);
        saxOutputter.output(documentMock);

        // then
        verify(errorHandlerMock).error(any(SAXParseException.class));
    }

    private void processContent(Content content) {
        // implementation details
    }
}