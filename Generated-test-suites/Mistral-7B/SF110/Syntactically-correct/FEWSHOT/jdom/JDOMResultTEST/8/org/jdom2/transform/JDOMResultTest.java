package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.*;

public class JDOMResultTest {

    @Test
    public void testEmptyResult() {
        JDOMResult result = new JDOMResult();
        assertEquals(Collections.emptyList(), result.getResult());
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("foo"));
        result.setResult(list);
        assertEquals(list, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new JDOMBuilder().setContent(new Element("root")).build();
        result.setDocument(doc);
        Document resultDoc = result.getDocument();
        assertEquals(resultDoc.getRootElement(), result.getResult().get(0));
    }

    @Test
    public void testGetDocumentWithEmptyResult() {
        JDOMResult result = new JDOMResult();
        assertEquals(null, result.getDocument());
    }

    @Test
    public void testGetDocumentWithResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("foo"));
        result.setResult(list);
        JDOMFactory factory = new JDOMFactory();
        Document doc = factory.document(null);
        doc.setContent(list);
        result.setFactory(factory);
        Document resultDoc = result.getDocument();
        assertEquals(resultDoc.getRootElement(), list.get(0));
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        DocumentBuilder builder = new DocumentBuilder();
        SAXContentHandler handler = new MockSAXContentHandler();
        result.setHandler(handler);
        result.setLexicalHandler(builder);
        String xml = "<root><foo></foo></root>";
        InputSource inputSource = new InputSource(new StringReader(xml));
        SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setEntityResolver(new NullEntityResolver());
        saxBuilder.build(inputSource, result);
        List<Content> resultList = result.getResult();
        assertEquals(1, resultList.size());
        assertEquals("foo", resultList.get(0).getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentWithNullFactory() {
        JDOMResult result = new JDOMResult();
        result.setFactory(null);
        result.getDocument();
    }

    private static class MockSAXContentHandler implements ContentHandler {
        private List<Content> result = new ArrayList<>();

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            result.add(new Element(qName));
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        @Override
        public void setDocumentLocator(Locator locator) throws SAXException {
        }

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }

        @Override
        public void endDTD() throws SAXException {
        }

        @Override
        public void startEntity(String name) throws SAXException {
        }

        @Override
        public void endEntity(String name) throws SAXException {
        }

        @Override
        public void startCDATA() throws SAXException {
        }

        @Override
        public void endCDATA() throws SAXException {
        }

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException {
        }

        public List<Content> getResult() {
            return result;
        }
    }
}