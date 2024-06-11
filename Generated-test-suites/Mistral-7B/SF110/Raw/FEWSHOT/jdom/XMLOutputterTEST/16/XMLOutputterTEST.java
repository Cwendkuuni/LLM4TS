package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.OutputFormat;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.jdom2.output.support.FormatStack;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputterConstruction() {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);
        assertSame(format, outputter.getFormat());
        assertSame(processor, outputter.getXMLOutputProcessor());

        outputter = new XMLOutputter(format);
        assertSame(format, outputter.getFormat());
        assertNull(outputter.getXMLOutputProcessor());

        outputter = new XMLOutputter(processor);
        assertNull(outputter.getFormat());
        assertSame(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        OutputFormat format = new OutputFormat();
        outputter.setFormat(format);
        assertSame(format, outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        outputter.setXMLOutputProcessor(processor);
        assertSame(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        Document doc = new Document();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(doc, writer);
        writer.flush();
        String expectedXml = "<document/>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocType() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        DocType docType = new DocType();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(docType, writer);
        writer.flush();
        String expectedXml = "<!DOCTYPE [>\n<!DOCTYPE [>\n";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputElement() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        Element element = new Element("element");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(element, writer);
        writer.flush();
        String expectedXml = "<element/>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputText() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        Text text = new Text("text");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(text, writer);
        writer.flush();
        String expectedXml = "<text>text</text>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputComment() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        Comment comment = new Comment("comment");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(comment, writer);
        writer.flush();
        String expectedXml = "<!---comment--->";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(pi, writer);
        writer.flush();
        String expectedXml = "<?!target data>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        EntityRef entity = new EntityRef("entity");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(entity, writer);
        writer.flush();
        String expectedXml = "<!ENTITY entity SYSTEM " + EntityRef.SYSTEM_ID + ">";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        Element element = new Element("element");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        List<Content> content = new ArrayList<>();
        content.add(new Text("text1"));
        content.add(new Text("text2"));
        element.setContent(content);

        outputter.outputElementContent(element, writer);
        writer.flush();
        String expectedXml = "<element>\n<text>text1</text>\n<text>text2</text>\n</element>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputListOfContent() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        List<Content> content = new ArrayList<>();
        content.add(new Text("text1"));
        content.add(new Text("text2"));

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(content, writer);
        writer.flush();
        String expectedXml = "<text>text1</text>\n<text>text2</text>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputCDATA() throws IOException {
        OutputFormat format = new OutputFormat();
        XMLOutputProcessor processor = mock(XMLOutputProcessor.class);
        XMLOutputter outputter = new XMLOutputter(format, processor);

        CDATA cdata = new CDATA("text");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outStream);

        outputter.output(cdata, writer);
        writer.flush();
        String expectedXml = "<![CDATA[text]]>";
        String actualXml = outStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputString() {
        OutputFormat format = new OutputFormat();
        XMLOutputter outputter = new XMLOutputter(format);

        String input = "<root><element>text</element></root>";
        OutputStream outputStream = mock(OutputStream.class);
        Writer writer = new StringWriter();

        try {
            outputter.outputString(new Document(new InputSource(new StringReader(input))), writer);
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }

        String expectedXml = "<root><element>text</element></root>";
        String actualXml = writer.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testEscapeAttributeEntities() {
        OutputFormat format = new OutputFormat();
        DefaultXMLProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(format);

        String input = "&lt;&gt;&amp;&apos;&quot;&#34;&#38;";
        String expectedOutput = "&amp;lt;&amp;gt;&amp;amp;&amp;apos;&amp;quot;&amp;#x22;;&amp;#x26;";
        String actualOutput = outputter.escapeAttributeEntities(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEscapeElementEntities() {
        OutputFormat format = new OutputFormat();
        DefaultXMLProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(format);

        String input = "&lt;foo &gt;bar &amp; baz&apos; &quot;quux&quot; &#34;quux&quot; &#38;quux";
        String expectedOutput = "&lt;foo &gt;bar &amp;amp; baz&amp;apos; &quot;quux&quot; &quot;quux&quot; &amp;#x26;quux";
        String actualOutput = outputter.escapeElementEntities(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToString() {
        OutputFormat format = new OutputFormat();
        XMLOutputter outputter = new XMLOutputter(format);

        StringBuilder buffer = new StringBuilder();
        buffer.append("XMLOutputter[omitDeclaration = false, ");
        buffer.append("encoding = null, ");
        buffer.append("omitEncoding = false, ");
        buffer.append("indent = '', ");
        buffer.append("expandEmptyElements = true, ");
        buffer.append("lineSeparator = [\r\n], ");
        buffer.append("mode = XML]");

        String expectedString = buffer.toString();
        String actualString = outputter.toString();
        assertEquals(expectedString, actualString);
    }
}