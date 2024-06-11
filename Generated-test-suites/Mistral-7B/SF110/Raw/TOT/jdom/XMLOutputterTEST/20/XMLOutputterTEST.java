import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMFactory().createDocument();
        doc.setRootElement(new Element("root"));
        doc.getRootElement().addContent(new Element("child1").setText("child1"));
        doc.getRootElement().addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outStream);

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<child1>child1</child1>\n<child2>child2</child2>\n</root>";
        String actualXml = outStream.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithFormat() throws IOException {
        Document doc = new JDOMFactory().createDocument();
        doc.setRootElement(new Element("root"));
        doc.getRootElement().addContent(new Element("child1").setText("child1"));
        doc.getRootElement().addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter(new Format("UTF-8"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outStream);

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<child1>child1</child1>\n<child2>child2</child2>\n</root>";
        String actualXml = outStream.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithProcessor() throws IOException {
        Document doc = new JDOMFactory().createDocument();
        doc.setRootElement(new Element("root"));
        doc.getRootElement().addContent(new Element("child1").setText("child1"));
        doc.getRootElement().addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter(null, new MyXMLProcessor());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outStream);

        String expectedXml = "<root><child1>child1</child1><child2>child2</child2></root>";
        String actualXml = outStream.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithNullDoc() {
        XMLOutputter xmlOutputter = new XMLOutputter();

        Assert.assertThrows(NullPointerException.class, () -> xmlOutputter.output(null, new StringWriter()));
    }

    @Test
    public void testOutputDocumentWithNullWriter() {
        Document doc = new JDOMFactory().createDocument();
        XMLOutputter xmlOutputter = new XMLOutputter();

        Assert.assertThrows(NullPointerException.class, () -> xmlOutputter.output(doc, null));
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMFactory().createDocument();
        Element root = doc.getRootElement();
        root.setName("root");
        root.addContent(new Element("child").setText("child"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(root, outStream);

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root><child>child</child></root>";
        String actualXml = outStream.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputEmptyDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(new Document(), outStream);

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<document/>";
        String actualXml = outStream.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputNullElement() {
        XMLOutputter xmlOutputter = new XMLOutputter();

        Assert.assertThrows(NullPointerException.class, () -> xmlOutputter.output(null, new StringWriter()));
    }

    @Test
    public void testOutputWriter() throws IOException {
        Document doc = new JDOMFactory().createDocument();
        doc.setRootElement(new Element("root"));
        doc.getRootElement().addContent(new Element("child1").setText("child1"));
        doc.getRootElement().addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        xmlOutputter.output(doc, writer);

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<child1>child1</child1>\n<child2>child2</child2>\n</root>";
        String actualXml = writer.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputNullWriter() {
        Document doc = new JDOMFactory().createDocument();
        XMLOutputter xmlOutputter = new XMLOutputter();

        Assert.assertThrows(NullPointerException.class, () -> xmlOutputter.output(doc, null));
    }

    private static class MyXMLProcessor extends XMLOutputProcessor {
        @Override
        public void process(Writer out, Format format, Content content) throws IOException {
            if (content instanceof Element) {
                Element element = (Element)content;
                out.write("<" + element.getName());
                processAttributes(out, format, element);
                out.write(">");
                processContent(out, format, element);
                out.write("</" + element.getName() + ">");
            } else {
                super.process(out, format, content);
            }
        }

        private void processContent(Writer out, Format format, Element element) throws IOException {
            if (element.getContentSize() == 0) {
                out.write("/>");
            } else {
                super.process(out, format, element.getContent());
            }
        }
    }
}