import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, new OutputStreamWriter(outputStream));

        String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType docType = new DocType("html", null, null, null, null, null, null, null, null, null, null);

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(docType, new OutputStreamWriter(outputStream));

        String expectedXml = "<!DOCTYPE html [ ]>";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("element");
        element.setText("Element Text");

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(element, new OutputStreamWriter(outputStream));

        String expectedXml = "<element>Element Text</element>";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputEmptyElementContent() throws IOException {
        Element element = new Element("element");

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.outputElementContent(element, new OutputStreamWriter(outputStream));

        String expectedXml = "";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(null, new OutputStreamWriter(System.out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullOutputStream() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(new Document(), null);
    }
}