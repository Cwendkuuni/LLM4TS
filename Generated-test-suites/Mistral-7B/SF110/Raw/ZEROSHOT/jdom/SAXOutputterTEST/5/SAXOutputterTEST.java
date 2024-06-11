package org.jdom2.output;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SAXOutputterTest {

    private DefaultHandler handler;
    private StringWriter writer;
    private SAXOutputter outputter;

    @Before
    public void setUp() {
        handler = new DefaultHandler() {
            private List<String> elements = new java.util.ArrayList<>();

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                elements.add(qName);
            }

            public List<String> getElements() {
                return elements;
            }
        };

        writer = new StringWriter();
        outputter = new SAXOutputter(handler, writer);
    }

    @After
    public void tearDown() {
        handler = null;
        writer = null;
        outputter = null;
    }

    @Test
    public void testEmptyOutput() throws SAXException, JDOMException {
        outputter.output(ElementTest.getEmptyDocument());
        String xml = writer.toString();
        assertNull(xml);
    }

    @Test
    public void testOutputDocument() throws SAXException, JDOMException {
        Document document = ElementTest.getSampleDocument();
        outputter.output(document);
        String xml = writer.toString();
        assertEquals("<test><name>John</name><age>30</age></test>", xml);
    }

    @Test
    public void testOutputEmptyElement() throws SAXException, JDOMException {
        Element element = new Element("test");
        outputter.outputFragment(element);
        String xml = writer.toString();
        assertEquals("<test/>", xml);
    }

    @Test
    public void testOutputText() throws SAXException, JDOMException {
        Element element = new Element("test").addContent(new Text("Hello World"));
        outputter.outputFragment(element);
        String xml = writer.toString();
        assertEquals("<test>Hello World</test>", xml);
    }

    @Test
    public void testOutputCDATA() throws SAXException, JDOMException {
        Element element = new Element("test").addContent(new CDATA("<![CDATA[Hello World]]>"));
        outputter.outputFragment(element);
        String xml = writer.toString();
        assertEquals("<test><![CDATA[Hello World]]></test>", xml);
    }

    @Test
    public void testOutputComment() throws SAXException, JDOMException {
        Element element = new Element("test").addContent(new Comment("Hello World"));
        outputter.outputFragment(element);
        String xml = writer.toString();
        assertEquals("<test><!--Hello World--></test>", xml);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNull() throws SAXException, JDOMException {
        outputter.outputFragment(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputUnknownType() throws SAXException, JDOMException {
        Element element = new Element();
        outputter.outputFragment(element);
    }
}