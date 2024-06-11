package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expectedResult = new ArrayList<>();
        result.setResult(expectedResult);
        assertSame(expectedResult, result.getResult());
    }

    @Test
    public void testGetResult_emptyResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> emptyResult = Collections.emptyList();
        result.setDocument(null);
        result.retrieveResult();
        assertSame(emptyResult, result.getResult());
    }

    @Test
    public void testGetResult_document() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        Document document = factory.document(new String[]{"<root/>"});
        result.setDocument(document);
        result.retrieveResult();
        List<Content> expectedResult = new ArrayList<>(Arrays.asList(document.getRootElement()));
        assertSame(expectedResult, result.getResult());
    }

    @Test
    public void testGetResult_documentAndResultList() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        Document document = factory.document(new String[]{"<root/>"});
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(document.getRootElement());
        result.setDocument(document);
        result.setResult(expectedResult);
        result.retrieveResult();
        assertSame(expectedResult, result.getResult());
    }

    @Test
    public void testGetResult_resultList() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(factory.createElement("root"));
        result.setResult(expectedResult);
        result.retrieveResult();
        assertSame(expectedResult, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        result.setDocument(document);
        assertSame(document, result.getDocument());
    }

    @Test
    public void testGetDocument_noResultList() {
        JDOMResult result = new JDOMResult();
        result.retrieveResult();
        assertNull(result.getDocument());
    }

    @Test
    public void testGetDocument_resultList() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        List<Content> contents = new ArrayList<>(Arrays.asList(factory.createElement("root"), factory.createElement("child")));
        result.setResult(contents);
        result.retrieveResult();
        Document document = result.getDocument();
        assertNotNull(document);
        Element root = document.getRootElement();
        assertEquals(2, root.getChildren().size());
        assertEquals("root", root.getName());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        result.retrieveResult();
        assertNull(result.getDocument());
    }

    @Test
    public void testSetHandler() {
        // Not implemented in JDOMResult
    }

    @Test
    public void testSetLexicalHandler() {
        // Not implemented in JDOMResult
    }

    @Test
    public void testFragmentHandler_getResult() throws SAXException {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        SAXBuilder builder = new SAXBuilder(factory);
        builder.setValidation(false);
        Element root = new Element("root");
        root.addContent(new Text("Hello, World!"));
        builder.build(new StringReader("<root><text>Hello, World!</text></root>"), root);
        result.setFactory(factory);
        FragmentHandler fragmentHandler = new FragmentHandler(result.getFactory());
        root.setContentHandler(fragmentHandler);
        builder.build(new InputSource(new StringReader("<root><text>Hello, World!</text></root>")), root);
        List<Content> resultList = fragmentHandler.getResult();
        assertEquals(1, resultList.size());
        Element expectedElement = resultList.get(0);
        assertEquals("root", expectedElement.getName());
        assertEquals("Hello, World!", expectedElement.getTextNormalize());
    }
}