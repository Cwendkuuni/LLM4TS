package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        doc.getRootElement().addContent(new Element("root").setText("Test"));

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "<root>Test</root>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputDocType() throws Exception {
        DocType doctype = new JDOMFactory().newDocumentType("test", "", "");

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doctype);
        String expected = "<!DOCTYPE test [ ]>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElement() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);
        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(root);
        String expected = "<root><child/></root>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputElementContent() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Text text = new Text("Test");
        root.addContent(text);
        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputElementContentString(root);
        String expected = "Test";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputListOfContent() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        Text text1 = new Text("Text 1");
        Text text2 = new Text("Text 2");
        root.addContent(text1, text2);
        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(root);
        String expected = "<root><Text>Text 1</Text><Text>Text 2</Text></root>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputCDATA() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        CDATA cdata = new CDATA("<![CDATA[Test]]>");
        doc.setRootElement(cdata);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "<![CDATA[Test]]>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputText() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Text text = new Text("Test");
        doc.setRootElement(text);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "Test";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputComment() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        Comment comment = new Comment("Test");
        doc.setRootElement(comment);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "<!--Test-->";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputProcessingInstruction() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\" type=\"text/css\"");
        doc.setRootElement(pi);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "<xml-stylesheet type=\"text/css\" href=\"style.css\"/>";
        assertEquals(expected, actual);
    }

    @Test
    public void testOutputEntityRef() throws Exception {
        Document doc = new JDOMFactory().newDocument();
        EntityRef entityRef = new EntityRef("ampersand");
        doc.setRootElement(entityRef);

        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.outputString(doc);
        String expected = "&ampersand;";
        assertEquals(expected, actual);
    }

    @Test
    public void testEscapeAttributeEntities() throws Exception {
        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.escapeAttributeEntities("Test <div> & & &amp; ' &quot; &apos; \" &lt; &gt;");
        String expected = "Test &lt;div&gt; &amp; &amp; &amp;amp; &apos; &amp;quot; &amp;apos; &lt; &amp;gt;";
        assertEquals(expected, actual);
    }

    @Test
    public void testEscapeElementEntities() throws Exception {
        XMLOutputter outputter = new XMLOutputter();
        String actual = outputter.escapeElementEntities("Test <div> & & &amp; ' &quot; &apos; \" &lt; &gt;");
        String expected = "Test &lt;div&gt; &amp; &amp; &amp;amp; &apos; &amp;quot; &amp;apos; &lt; &amp;gt;";
        assertEquals(expected, actual);
    }
}