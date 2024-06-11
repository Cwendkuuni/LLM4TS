package org.jdom2.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.JDOMInputFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testEmptyJDOMResult() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getDocument());
        assertNull(result.getFactory());
        assertNull(result.getResult());
    }

    @Test
    public void testSetResultAndGetResult() throws SAXException, JDOMException {
        JDOMResult result = new JDOMResult();

        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element1"));
        expected.add(new Element("element2"));
        SAXResult handlerResult = new SAXResult() {
            @Override
            public List<Content> getResult() {
                return expected;
            }
        };
        result.setHandler(handlerResult);
        result.retrieveResult();

        List<Content> actual = result.getResult();
        assertIterableEquals(expected, actual);
    }

    @Test
    public void testSetDocumentAndGetDocument() throws SAXException, JDOMException {
        JDOMResult result = new JDOMResult();

        Document expected = new JDOMFactory().document(new Element("document"));
        result.setDocument(expected);

        Document actual = result.getDocument();
        assertSame(expected, actual);
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getFactory());

        JDOMResult resultWithFactory = new JDOMResult();
        resultWithFactory.setFactory(new DefaultJDOMFactory());
        JDOMFactory actual = resultWithFactory.getFactory();
        assertNotNull(actual);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();

        Document expectedDocument = new JDOMFactory().document(new Element("document"));
        List<Content> expectedContent = expectedDocument.getContent();

        result.setHandler(new DocumentHandler() {
            @Override
            public void startDocument() throws SAXException {
                result.retrieveResult();
            }
        });

        result.setDocument(expectedDocument);
        List<Content> actual = result.getResult();
        assertIterableEquals(expectedContent, actual);
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();

        ContentHandler handler = new ContentHandler() {
            @Override
            public void startElement(String ns, String localName, String qName, Attributes atts) throws SAXException {
                assertFalse(result.queried);
            }
        };

        result.setHandler(handler);
        result.getResult();
        assertTrue(result.queried);
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();

        LexicalHandler handler = new LexicalHandler() {};

        result.setLexicalHandler(handler);
        result.getResult();
        assertTrue(result.queried);
    }

}

class DocumentHandler extends DefaultHandler {
    @Override
    public void startDocument() throws SAXException {
        // Empty
    }
}

class FragmentHandler extends SAXHandler {
    private Element dummyRoot;

    public FragmentHandler(JDOMFactory factory) {
        super(factory);
        this.pushElement(this.dummyRoot = new Element("root", null, null));
    }

    @Override
    public List<Content> getResult() {
        // Empty
    }
}