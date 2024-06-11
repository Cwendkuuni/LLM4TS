package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("test.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(doc, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output document to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputDocType() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        DocType doctype = doc.getRootElement().getDocument().getDoctype();

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("doctype.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(doctype, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output DocType to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputElement() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Element element = doc.getRootElement().getChild("child");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("element.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(element, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output Element to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputList() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Element root = doc.getRootElement();
        List<Content> list = root.getChildren();

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("list.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(list, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output List to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputCDATA() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Element element = doc.getRootElement().getChild("cdata");
        CDATA cdata = (CDATA) element;

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("cdata.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(cdata, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output CDATA to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputText() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Text text = doc.getRootElement().getChildText("text");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("text.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(text, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output Text to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputComment() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Comment comment = doc.getRootElement().getChild("comment");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("comment.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(comment, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output Comment to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputProcessingInstruction() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        ProcessingInstruction pi = doc.getRootElement().getChild("pi");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("pi.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(pi, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output ProcessingInstruction to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputEntityRef() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        EntityRef entity = doc.getRootElement().getChild("entityref");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("entityref.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.output(entity, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output EntityRef to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOutputElementContent() {
        Document doc = new JDOMParser().parse(getClass().getResource("test.xml"));
        Element element = doc.getRootElement().getChild("child");

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = getClass().getResource("element_content.xml").getFile();
        String actualOutput = "";

        try (StringWriter sw = new StringWriter();) {
            outputter.outputElementContent(element, sw);
            actualOutput = sw.toString();
        } catch (IOException e) {
            fail("Failed to output Element content to StringWriter");
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String input = "<root attr=\"<tag>&<symbol>&quot;&amp;&gt;\" />";
        String expectedOutput = "<root attr=\"&lt;tag&gt;&amp;lt;symbol&gt;&amp;lt;amp&gt;&amp;gt;&quot;\" />";
        String actualOutput = outputter.escapeAttributeEntities(input, null);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String input = "<root>&lt;tag&gt;&amp;<symbol>&gt;</root>";
        String expectedOutput = "<root>&lt;tag&gt;&amp;lt;symbol&gt;&gt;</root>";
        String actualOutput = outputter.escapeElementEntities(input, null);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClone() {
        XMLOutputter original = new XMLOutputter();
        XMLOutputter clone = original.clone();

        assertEquals(original.getFormat(), clone.getFormat());
        assertEquals(original.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }
}