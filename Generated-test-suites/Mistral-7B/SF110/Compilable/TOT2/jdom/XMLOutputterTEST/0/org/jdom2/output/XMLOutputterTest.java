package org.jdom2.output;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        Element child = new Element("child");
        root.addContent(child);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(doc, outputStream);

        String expectedXml = "<root><child/></root>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }
}