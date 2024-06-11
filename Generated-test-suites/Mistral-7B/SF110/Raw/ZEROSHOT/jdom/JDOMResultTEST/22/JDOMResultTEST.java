package org.jdom2.transform;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilderFactory;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testJDOMResultInstance() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSettingResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(result, actualResult);
    }

    @Test
    public void testSettingDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
    }

    @Test
    public void testGettingDocumentFromResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        JDOMFactory factory = new JDOMFactory();
        Document actualDocument = jdomResult.getDocument();
        Document document = factory.document(null);
        document.setContent(result);
        assertEquals(document, actualDocument);
    }

    @Test(expected = RuntimeException.class)
    public void testGettingDocumentWhenNeitherResultListNorDocumentIsSet() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
    }

    @Test
    public void testSettingFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        assertEquals(factory, actualFactory);
    }

    @Test
    public void testRetrievingResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(new ArrayList<>());
        List<Content> result = jdomResult.getResult();
        assertTrue(result.isEmpty());
        jdomResult.retrieveResult();
        result = jdomResult.getResult();
        assertTrue(result.isEmpty());
        Document document = new Document();
        jdomResult.setDocument(document);
        result = jdomResult.getResult();
        assertTrue(result.isEmpty());
        List<Content> testResult = new ArrayList<>();
        testResult.add(new Element("test"));
        jdomResult.setResult(testResult);
        result = jdomResult.getResult();
        assertEquals(testResult, result);
    }

    @Test
    public void testSetHandlerAndLexicalHandlerMethodsAreEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        // Empty test for setHandler and setLexicalHandler methods
    }

    @Test
    public void testDocumentBuilder() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        String xmlString = "<root><test>Hello World</test></root>";
        StringReader stringReader = new StringReader(xmlString);
        JDOMBuilderFactory builderFactory = new JDOMBuilderFactory();
        ContentHandler handler = new DefaultHandler();
        jdomResult.setFactory(factory);
        jdomResult.setHandler(handler);
        jdomResult.setLexicalHandler(handler);
        jdomResult.setInputSource(new InputSource(stringReader));
        jdomResult.setFeature(JDOM_FEATURE, true);
        jdomResult.parse(builderFactory);
        Document actualDocument = jdomResult.getDocument();
        Element root = actualDocument.getRootElement();
        assertEquals("root", root.getName());
        List<Content> result = jdomResult.getResult();
        assertEquals(1, result.size());
        assertEquals("Hello World", result.get(0).getText());
    }
}