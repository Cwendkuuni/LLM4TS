package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResultWhenResultlistIsNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element1"));
        result.add(new Element("element2"));
        jdomResult.setResult(result);

        List<Content> getResult = jdomResult.getResult();
        assertEquals(result.size(), getResult.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getName(), getResult.get(i).getName());
        }
    }

    @Test
    public void testGetResultWhenResultdocIsNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = JDOMFactory.instance.newDocument("rootElement");
        Element root = doc.getRootElement();
        root.addContent(new Element("element1"));
        root.addContent(new Element("element2"));
        jdomResult.setDocument(doc);

        List<Content> getResult = jdomResult.getResult();
        assertEquals(2, getResult.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(i + 1, getResult.get(i).getName().getLocalName().intValue());
        }
    }

    @Test
    public void testGetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = JDOMFactory.instance.newDocument("rootElement");
        Element root = doc.getRootElement();
        root.addContent(new Element("element1"));
        root.addContent(new Element("element2"));
        jdomResult.setDocument(doc);

        Document getDocument = jdomResult.getDocument();
        assertNotNull(getDocument);
        assertEquals(2, getDocument.getNumChildren());
        for (int i = 0; i < 2; i++) {
            assertEquals(i + 1, getDocument.getChild(i).getName().getLocalName().intValue());
        }
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);

        JDOMFactory getFactory = jdomResult.getFactory();
        assertEquals(factory, getFactory);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        DocumentBuilder documentBuilder = new DocumentBuilder();
        jdomResult.setHandler(documentBuilder);

        String xmlString = "<root><element1>Content 1</element1><element2><subElement>Content 2</subElement></element2></root>";
        SAXContentHandler handler = new SAXContentHandler();
        documentBuilder.parse(new StringReader(xmlString), handler);
        List<Content> result = handler.getResult();

        jdomResult.retrieveResult();
        List<Content> getResult = jdomResult.getResult();
        assertEquals(result.size(), getResult.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getName().getLocalName(), getResult.get(i).getName().getLocalName());
        }
    }
}