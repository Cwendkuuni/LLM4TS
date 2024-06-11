
package org.jdom2.output;

import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        Text text = new Text("Hello World");
        root.addContent(text);

        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
        String expected = "<root><text>Hello World</text></root>";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testEmptyOutputDocument() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        Document doc = new Document();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
        String expected = "";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputDocumentWithNullOutputStream() throws IOException {
        Document doc = new Document();
        Text text = new Text("Hello World");
        doc.setRootElement(new Element("root").addContent(text));

        XMLOutputter xmlo = new XMLOutputter();
        try {
            xmlo.output(doc, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOutputDocumentWithNullFormat() throws IOException {
        Document doc = new Document();
        Text text = new Text("Hello World");
        doc.setRootElement(new Element("root").addContent(text));

        XMLOutputter xmlo = new XMLOutputter();
        try {
            xmlo.output(doc, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOutputLargeDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        for (int i = 0; i <= 10000; i++) {
            root.addContent(new Text("Element " + i));
        }

        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
    }

    @Test
    public void testOutputElementContentString() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        Text text = new Text("Hello World");
        root.addContent(text);

        XMLOutputter xmlo = new XMLOutputter();
        String expected = "<text>Hello World</text>";
        assertEquals(expected, xmlo.outputElementContentString(root));
    }

    @Test
    public void testOutputElementContentStringWithNullElement() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        String expected = "";
        assertEquals(expected, xmlo.outputElementContentString(null));
    }

    @Test
    public void testOutputElementContentStringWithEmptyElement() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        XMLOutputter xmlo = new XMLOutputter();
        String expected = "<element/>";
        assertEquals(expected, xmlo.outputElementContentString(root));
    }

    @Test
    public void testClone() {
        XMLOutputter original = new XMLOutputter();
        XMLOutputter clone = original.clone();
        assertNotSame(original, clone);
    }

    @Test
    public void testCloneWithClonedObjectExists() {
        XMLOutputter original = new XMLOutputter();
        XMLOutputter clone = original.clone();
        XMLOutputter clone2 = clone.clone();
        assertNotSame(original, clone2);
    }
}