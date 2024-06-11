import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        doc.setRootElement(root);

        XMLOutputter xmlo = new XMLOutputter();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(doc, outStream);

        String expectedXml = "<root/>";
        String actualXml = new String(outStream.toByteArray());

        Assert.assertEquals(expectedXml, actualXml);

        // Test with custom format
        XMLOutputter xmloCustom = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream outStreamCustom = new ByteArrayOutputStream();
        xmloCustom.output(doc, outStreamCustom);

        String expectedXmlPretty = "<root>\n</root>";
        String actualXmlPretty = new String(outStreamCustom.toByteArray());

        Assert.assertEquals(expectedXmlPretty, actualXmlPretty);

        // Test with empty document
        Document emptyDoc = new JDOMFactory().newDocument();
        ByteArrayOutputStream outStreamEmpty = new ByteArrayOutputStream();
        xmlo.output(emptyDoc, outStreamEmpty);

        String emptyExpectedXml = "<document/>";
        String emptyActualXml = new String(outStreamEmpty.toByteArray());

        Assert.assertEquals(emptyExpectedXml, emptyActualXml);

        // Test with error - null document
        try {
            xmlo.output(null, new StringWriter());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with error - null output stream
        try {
            xmlo.output(doc, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOutputElement() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);
        doc.setRootElement(root);

        XMLOutputter xmlo = new XMLOutputter();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(element, outStream);

        String expectedXml = "<root><child/></root>";
        String actualXml = new String(outStream.toByteArray());

        Assert.assertEquals(expectedXml, actualXml);

        // Test with custom format
        XMLOutputter xmloCustom = new XMLOutputter(Format.getPrettyFormat());
        ByteArrayOutputStream outStreamCustom = new ByteArrayOutputStream();
        xmloCustom.output(child, outStreamCustom);

        String expectedXmlPretty = "<child/>\n";
        String actualXmlPretty = new String(outStreamCustom.toByteArray());

        Assert.assertEquals(expectedXmlPretty, actualXmlPretty);

        // Test with error - null element
        try {
            xmlo.output(null, new StringWriter());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with error - null output stream
        try {
            xmlo.output(child, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other methods, such as output(List<Content> list, OutputStream out), output(Text text, OutputStream out), etc.

    // Add test cases for edge cases and error scenarios, such as invalid formats, empty strings, null values, etc.

    // Add test cases for different configurations and customizations of the XMLOutputter, such as different processors, different formats, etc.
}