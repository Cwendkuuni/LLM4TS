package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_WhenResultlistIsNotNull_ReturnsResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element1"));
        jdomResult.setResult(result);

        List<Content> actualResult = jdomResult.getResult();

        assertSame(result, actualResult);
    }

    @Test
    public void testGetResult_WhenResultdocIsNotNull_ReturnsDocumentContent() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root", null, null);
        root.addContent(new Element("element1"));
        doc.setContent(new Element[]{root});

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(doc);

        List<Content> actualResult = jdomResult.getResult();

        assertSame(root.getContent(), actualResult);
    }

    @Test
    public void testGetDocument_WhenResultlistIsNotNull_ReturnsDocumentCreatedFromResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("element1"));
        jdomResult.setResult(result);

        Document actualResult = jdomResult.getDocument();

        assertNotNull(actualResult);
        assertEquals("element1", actualResult.getRootElement().getName());
    }

    @Test
    public void testGetDocument_WhenResultdocIsNotNull_ReturnsResultdoc() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root", null, null);
        doc.setContent(new Element[]{root});

        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(doc);

        Document actualResult = jdomResult.getDocument();

        assertSame(doc, actualResult);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocument_WhenBothResultlistAndResultdocAreNull_ThrowsRuntimeException() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();

        Document actualResult = jdomResult.getDocument();

        fail("Should have thrown RuntimeException");
    }

    @Test
    public void testGetFactory_WhenFactoryIsNotNull_ReturnsFactory() {
        JDOMFactory factory = new DefaultJDOMFactory();
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(factory);

        JDOMFactory actualResult = jdomResult.getFactory();

        assertSame(factory, actualResult);
    }

    @Test
    public void testGetFactory_WhenFactoryIsNull_ReturnsDefaultJDOMFactory() {
        JDOMResult jdomResult = new JDOMResult();

        JDOMFactory actualResult = jdomResult.getFactory();

        assertInstanceOf(DefaultJDOMFactory.class, actualResult);
    }
}