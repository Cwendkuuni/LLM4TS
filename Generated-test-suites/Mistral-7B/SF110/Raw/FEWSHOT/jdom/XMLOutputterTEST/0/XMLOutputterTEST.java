package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputterInstance() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter);

        XMLOutputter cloneOutputter = outputter.clone();
        assertEquals(outputter, cloneOutputter);
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        doc.setRootElementName("root");
        Element root = doc.getRootElement();
        root.addContent(new Element("elem1").setText("value1"));
        root.addContent(new Element("elem2").setText("value2"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(doc, out);

        String expected = "<root><elem1>value1</elem1><elem2>value2</elem2></root>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        doc.setRootElementName("root");
        Element root = doc.getRootElement();

        DocType docType = doc.getRootElement().getDocument().getDocType();
        docType.setName("dtd");
        docType.setPublic("-//company//DTD My document//EN");

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(docType, out);

        String expected = "<!DOCTYPE dtd PUBLIC \"-//company//DTD My document//EN\">";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("elem").setText("value"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getChild(0), out);

        String expected = "<elem>value</elem>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputString() {
        XMLOutputter outputter = new XMLOutputter();
        String input = "<root><elem>value</elem></root>";

        String expected = outputter.outputString(new Document(new JDOMFactory().parse(new StringReader(input))));
        assertEquals(expected, outputter.outputString(input));
    }

    @Test
    public void testOutputElementContentString() {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("elem").setText("value"));

        XMLOutputter outputter = new XMLOutputter();
        assertEquals(outputter.outputElementContentString(root.getChild(0)), "value");
    }

    @Test
    public void testOutputListContent() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("elem1").setText("value1"));
        root.addContent(new Element("elem2").setText("value2"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getContent(), out);

        String expected = "<elem1>value1</elem1><elem2>value2</elem2>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new CDATA("<![CDATA[value]]>"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getChild(0), out);

        String expected = "<![CDATA[value]]>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Text("value"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getChild(0), out);

        String expected = "value";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Comment("comment"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getChild(0), out);

        String expected = "<!--comment-->";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();
        root.addContent(new ProcessingInstruction("target", "data"));

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(root.getChild(0), out);

        String expected = "<?target data?>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        Element root = doc.getRootElement();

        EntityRef entityRef = new EntityRef(" entities.xml ");
        root.addContent(entityRef);

        XMLOutputter outputter = new XMLOutputter();
        Writer out = new StringWriter();
        outputter.output(entityRef, out);

        String expected = "&entities.xml;";
        assertEquals(expected, out.toString());
    }
}