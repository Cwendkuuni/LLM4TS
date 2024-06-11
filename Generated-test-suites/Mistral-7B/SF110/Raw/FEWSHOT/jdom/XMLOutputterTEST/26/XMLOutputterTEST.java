package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Format;
import org.jdom2.JDOMException;
import org.jdom2.Name;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.jdom2.output.support.FormatStack;

public class XMLOutputterTest {

    @Test
    public void testNewInstance() {
        XMLOutputter xmlo = new XMLOutputter();
        assertNull(xmlo.getFormat());
        assertNull(xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithFormat() {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);
        assertEquals(format, xmlo.getFormat());
        assertNull(xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithFormatAndProcessor() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmlo = new XMLOutputter(format, processor);
        assertEquals(format, xmlo.getFormat());
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmlo = new XMLOutputter(processor);
        assertNull(xmlo.getFormat());
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithThat() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmloSource = new XMLOutputter(format, processor);
        XMLOutputter xmlo = new XMLOutputter(xmloSource);
        assertEquals(format, xmlo.getFormat());
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithThatFormat() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmloSource = new XMLOutputter(format, processor);
        XMLOutputter xmlo = new XMLOutputter(xmloSource.getFormat());
        assertEquals(format, xmlo.getFormat());
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testNewInstanceWithThatProcessor() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmloSource = new XMLOutputter(format, processor);
        XMLOutputter xmlo = new XMLOutputter(xmloSource.getXMLOutputProcessor());
        assertEquals(format, xmlo.getFormat());
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        XMLOutputter xmlo = new XMLOutputter();
        Format format = new Format();
        xmlo.setFormat(format);
        assertEquals(format, xmlo.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter xmlo = new XMLOutputter();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        xmlo.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlo.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<root/>", xml);
    }

    @Test
    public void testOutputDocType() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        DocType doctype = new DocType("doc", Arrays.asList(new Element("html"), new Element("head"), new Element("body")));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(doctype, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<!DOCTYPE doc [ <!ELEMENT html (head, body) > <!ELEMENT head (#PCDATA)|*(*)> <!ELEMENT body (#PCDATA)|*(*)> ]>", xml);
    }

    @Test
    public void testOutputElement() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Element element = new Element("element");
        element.setText("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(element, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<element>text</element>", xml);
    }

    @Test
    public void testOutputCDATA() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        CDATA cdata = new CDATA("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(cdata, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<![CDATA[text]]>", xml);
    }

    @Test
    public void testOutputText() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Text text = new Text("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(text, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<text>text</text>", xml);
    }

    @Test
    public void testOutputComment() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Comment comment = new Comment("comment");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(comment, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<!--comment-->", xml);
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        ProcessingInstruction pi = new ProcessingInstruction("target", "data");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(pi, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<?target data?>", xml);
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        EntityRef entityRef = new EntityRef("entity");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(entityRef, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("&entity;", xml);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Element element = new Element("element");
        element.setText("text");
        element.addContent(new Element("child").setText("child text"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.outputElementContent(element, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<element><text>text</text><child>child text</child></element>", xml);
    }

    @Test
    public void testOutputList() throws IOException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        List<Content> list = new ArrayList<>();
        list.add(new Text("text1"));
        list.add(new Element("element1").setText("element text1"));
        list.add(new Comment("comment1"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlo.output(list, out);
        out.flush();

        String xml = out.toString("UTF-8");
        assertEquals("<text>text1</text><element1><text>element text1</text></element1><!--comment1-->", xml);
    }

    @Test
    public void testOutputWriter() throws IOException, JDOMException {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        root.addContent(new Element("child1").setText("text1"));
        root.addContent(new Element("child2")
                .addContent(new Element("grandchild1").setText("text2"))
                .addContent(new Comment("comment1"))
                .addContent(new Text("text3")));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter();
        xmlo.output(doc, writer);

        xmlo.output(doc, out, writer.toString());

        String xml = writer.toString();
        assertEquals("<root><child1>text1</child1><child2><grandchild1>text2</grandchild1><!--comment1--></child2><text3/></root>", xml);

        xml = out.toString("UTF-8");
        assertEquals(xml, writer.toString());
    }

    @Test
    public void testOutputWriterWithLineSeparator() throws IOException, JDOMException {
        Format format = new Format();
        format.setLineSeparator("\n");
        XMLOutputter xmlo = new XMLOutputter(format);

        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        root.addContent(new Element("child1").setText("text1"));
        root.addContent(new Element("child2")
                .addContent(new Element("grandchild1").setText("text2"))
                .addContent(new Comment("comment1"))
                .addContent(new Text("text3")));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter();
        xmlo.output(doc, writer);

        xmlo.output(doc, out, writer.toString());

        String xml = writer.toString();
        assertEquals("<root><child1>text1</child1>\n<child2>\n<grandchild1>text2</grandchild1>\n<!--comment1-->\n<text3/>\n</child2>\n</root>", xml);

        xml = out.toString("UTF-8");
        assertEquals(xml, writer.toString());
    }

    @Test
    public void testToString() {
        Format format = new Format();
        XMLOutputter xmlo = new XMLOutputter(format);

        StringBuilder buffer = new StringBuilder();
        buffer.append("XMLOutputter[omitDeclaration = false, encoding = null, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = [\r,\n,\t], mode = XML]");

        assertEquals(buffer.toString(), xmlo.toString());
    }

    @Test
    public void testClone() {
        Format format = new Format();
        XMLOutputter xmloSource = new XMLOutputter(format);

        XMLOutputter xmloClone = xmloSource.clone();

        assertEquals(format, xmloClone.getFormat());
        assertEquals(xmloSource.getXMLOutputProcessor(), xmloClone.getXMLOutputProcessor());
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlo = new XMLOutputter();

        String attribute = "&amp;&lt;&gt;&quot;'&apos();<>&>";
        String escaped = xmlo.escapeAttributeEntities(attribute);
        assertEquals("&amp;amp;&amp;lt;&amp;gt;&amp;quot;&amp;apos();&amp;amp;&amp;lt;&amp;gt;&amp;quot;&amp;apos();<>&amp;amp;&gt;", escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlo = new XMLOutputter();

        String element = "&amp;&lt;&gt;&quot;'&apos();<>&>\"&'/";
        String escaped = xmlo.escapeElementEntities(element, null);
        assertEquals("&amp;amp;amp;&lt;>&gt;&quot;&apos;()<>&gt;\"&amp;amp;quot;&amp;apos;", escaped);
    }
}