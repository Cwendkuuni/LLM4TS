import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

    @org.junit.Test
    public void testDefaultSAXOutputter() throws JDOMException, SAXException {
        final Document document = new JDOMFactory().newDocument();
        document.setRootElement(new Element("root"));
        document.getRootElement().addContent(new Text("Hello World"));

        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        saxOutputter.setFormat(new PrettyFormat().setEncoding("UTF-8").setIndent("  "));
        saxOutputter.setContentHandler(new DefaultHandler() {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                stringWriter.write(new String(ch, start, length));
            }
        });
        saxOutputter.output(document);

        final String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<text xmlns=\"\">Hello World</text>\n</root>";
        assertEquals(expectedXml, stringWriter.toString());
    }

    @org.junit.Test
    public void testSAXOutputterWithHandlers() throws JDOMException, SAXException {
        final Document document = new JDOMFactory().newDocument();
        document.setRootElement(new Element("root"));
        document.getRootElement().addContent(new Text("Hello World"));

        final SAXOutputter saxOutputter = new SAXOutputter(new DefaultHandler() {
            private StringBuilder xml = new StringBuilder();

            @Override
            public void startDocument() throws SAXException {
                xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                xml.append("<").append(qName).append(">");
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                xml.append(new String(ch, start, length));
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                xml.append("</").append(qName).append(">");
            }

            @Override
            public void endDocument() throws SAXException {
                xml.append("</root>");
            }

            public String getXml() {
                return xml.toString();
            }
        }, new DefaultHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                fail("Error occurred: " + exception.getMessage());
            }
        });
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setFormat(Format.getRawFormat());
        saxOutputter.setContentHandler(new DefaultHandler() {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                stringWriter.write(new String(ch, start, length));
            }
        });
        saxOutputter.output(document);

        final String expectedXml = document.getDocumentText();
        assertEquals(expectedXml, saxOutputter.getHandler().getXml());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSAXOutputterWithNullDocument() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(null);
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullContentHandler() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullErrorHandler() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setErrorHandler(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullDTDHandler() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setDTDHandler(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullEntityResolver() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setEntityResolver(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullLexicalHandler() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setLexicalHandler(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test(expected = SAXException.class)
    public void testSAXOutputterWithNullDeclHandler() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setDeclHandler(null);
        saxOutputter.output(new JDOMFactory().newDocument());
    }

    @org.junit.Test
    public void testSAXOutputterWithNullFormat() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setFormat(null);
        saxOutputter.setContentHandler(new DefaultHandler() {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                stringWriter.write(new String(ch, start, length));
            }
        });
        saxOutputter.output(new JDOMFactory().newDocument());

        final String expectedXml = new String(new JDOMFactory().newDocument().getDocumentText());
        assertEquals(expectedXml, stringWriter.toString());
    }
}