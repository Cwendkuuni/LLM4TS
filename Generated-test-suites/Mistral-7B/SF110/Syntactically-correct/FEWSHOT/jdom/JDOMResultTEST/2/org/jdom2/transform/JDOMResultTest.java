package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_Empty() {
        JDOMResult result = new JDOMResult();
        List<Content> actual = result.getResult();
        assertNull(actual);
    }

    @Test
    public void testGetResult_WithList() {
        JDOMResult result = new JDOMResult();
        Element element = new Element("root");
        result.setResult(new ArrayList<Content>() {{
            add(element);
        }});
        List<Content> actual = result.getResult();
        assertEquals(1, actual.size());
        assertEquals(element, actual.get(0));
    }

    @Test
    public void testGetResult_WithDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
        result.setDocument(document);
        List<Content> actual = result.getResult();
        assertEquals(1, actual.size());
        assertEquals(document, actual.get(0));
    }

    @Test
    public void testGetDocument_Empty() {
        JDOMResult result = new JDOMResult();
        Document actual = result.getDocument();
        assertNull(actual);
    }

    @Test
    public void testGetDocument_WithList() {
        JDOMResult result = new JDOMResult();
        Element element = new Element("root");
        result.setResult(new ArrayList<Content>() {{
            add(element);
        }});
        Document actual = result.getDocument();
        assertNotNull(actual);
        assertEquals(element, actual.getRootElement());
    }

    @Test
    public void testGetDocument_WithDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new SAXBuilder().buildDocument(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"));
        result.setDocument(document);
        Document actual = result.getDocument();
        assertEquals(document, actual);
    }

    @Test
    public void testSetFactory_Null() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getFactory());
        result.setFactory(null);
        assertNull(result.getFactory());
    }

    @Test
    public void testSetFactory_JDOMFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory actual = result.getFactory();
        assertEquals(factory, actual);
    }
}