package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax. SAXBuilder;
import org.xml.sax.*;

import java.io.StringReader;

public class JDOMResultTest {

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        assertEquals(Collections.emptyList(), result.getResult());
    }

    @Test
    public void testGetResultList() {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        String xml = "<root><child/><child2/></root>";
        builder.setEntityResolver(JDOMResult.class.getResource("/").openStream(), "xml-entity-resolver");
        try {
            builder.build(new InputSource(new StringReader(xml)));
            Element root = builder.getDocument().getRootElement();
            result.setResult(root.getContent());
            List<Content> nodes = result.getResult();
            assertEquals(2, nodes.size());
            assertTrue(nodes.get(0) instanceof Element);
            assertTrue(nodes.get(1) instanceof Text);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetResultDocument() {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        String xml = "<root><child/><child2/></root>";
        try {
            builder.setEntityResolver(JDOMResult.class.getResource("/").openStream(), "xml-entity-resolver");
            Document doc = builder.build(new InputSource(new StringReader(xml)));
            result.setDocument(doc);
            List<Content> nodes = result.getResult();
            assertEquals(2, nodes.size());
            assertTrue(nodes.get(0) instanceof Element);
            assertTrue(nodes.get(1) instanceof Text);
        } catch (Exception e) {
            fail(e.getMessage());
        }
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
        SAXBuilder builder = new SAXBuilder();
        String xml = "<root><child/><child2/></root>";
        try {
            builder.setEntityResolver(JDOMResult.class.getResource("/").openStream(), "xml-entity-resolver");
            Document doc = builder.build(new InputSource(new StringReader(xml)));
            result.setDocument(doc);
            result.retrieveResult();
            assertTrue(result.getDocument() != null);
            assertTrue(result.getResultlist() == null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}