import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLOutputterTest {

    @Test
    public void testEmptyDocument() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();
        StringWriter writer = new StringWriter();

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXml = "<document/>";
        String actualXml = writer.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testDocumentWithElementsAndText() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );
        StringWriter writer = new StringWriter();

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXml = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXml = writer.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testDocumentWithMixedContent() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World")),
                new Element("child3", null, new Comment("This is a comment")),
                new Element("child4", null, new CDATA("This is some CDATA content")),
                new Element("child5", null, new ProcessingInstruction("xml-declaration", "version='1.0'"))
            )
        );
        StringWriter writer = new StringWriter();

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXml = "<root><!--This is a comment--><child1>Hello</child1><child2>World</child2><![CDATA[This is some CDATA content]]><child3 version=\"1.0\"/></root>";
        String actualXml = writer.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testDocumentWithEntities() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("&lt;Hello&gt;")),
                new Element("child2", null, new Text("&amp;World"))
            )
        );
        StringWriter writer = new StringWriter();

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXml = "<root><child1><&lt;Hello&gt;</child1><child2>&amp;World</child2></root>";
        String actualXml = writer.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputString() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );

        String expectedXml = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXml = outputter.outputString(doc);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IOException.class)
    public void testNullDocument() {
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();

        outputter.output(null, writer);
    }

    @Test(expected = IOException.class)
    public void testNullOutputStream() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();

        outputter.output(doc, null);
    }

    @Test
    public void testFormatChange() {
        XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
        XMLOutputter outputter2 = new XMLOutputter(Format.getRawFormat());

        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );

        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();

        try {
            outputter1.output(doc, writer1);
            outputter2.output(doc, writer2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXmlPretty = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXmlPretty = writer1.toString();
        Assert.assertEquals(expectedXmlPretty, actualXmlPretty);

        String expectedXmlRaw = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXmlRaw = writer2.toString();
        Assert.assertEquals(expectedXmlRaw, actualXmlRaw);

        Assert.assertNotEquals(actualXmlPretty, actualXmlRaw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(new Object());
    }

    @Test
    public void testProcessorChange() {
        XMLOutputter outputter1 = new XMLOutputter(null, new DefaultXMLProcessor());
        XMLOutputter outputter2 = new XMLOutputter(null, new MyCustomXMLProcessor());

        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );

        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();

        try {
            outputter1.output(doc, writer1);
            outputter2.output(doc, writer2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXmlDefault = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXmlDefault = writer1.toString();
        Assert.assertEquals(expectedXmlDefault, actualXmlDefault);

        String expectedXmlCustom = "<root><child1>HelloCustom</child1><child2>WorldCustom</child2></root>";
        String actualXmlCustom = writer2.toString();
        Assert.assertNotEquals(expectedXmlDefault, actualXmlCustom);
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();

        Format format = new Format();
        format.setOmitDeclaration(true);
        format.setEncoding("UTF-8");
        format.setLineSeparator("\r\n");
        format.setExpandEmptyElements(true);
        format.setIndent("  ");

        outputter.setFormat(format);

        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );

        StringWriter writer = new StringWriter();

        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\r\n<root>\r\n  <child1>Hello</child1>\r\n  <child2>World</child2>\r\n</root>";
        String actualXml = writer.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testClone() {
        XMLOutputter original = new XMLOutputter(Format.getPrettyFormat());
        XMLOutputter cloned = original.clone();

        Document doc = new Document(
            new Element("root", null,
                new Element("child1", null, new Text("Hello")),
                new Element("child2", null, new Text("World"))
            )
        );

        StringWriter writerOriginal = new StringWriter();
        StringWriter writerCloned = new StringWriter();

        try {
            original.output(doc, writerOriginal);
            cloned.output(doc, writerCloned);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedXmlOriginal = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXmlOriginal = writerOriginal.toString();
        Assert.assertEquals(expectedXmlOriginal, actualXmlOriginal);

        String expectedXmlCloned = "<root><child1>Hello</child1><child2>World</child2></root>";
        String actualXmlCloned = writerCloned.toString();
        Assert.assertEquals(expectedXmlOriginal, actualXmlCloned);

        Assert.assertNotSame(original, cloned);
    }
}