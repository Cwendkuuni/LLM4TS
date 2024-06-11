import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Document doc = new JDOMFactory().newDocument("Root");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(doc, new OutputStreamWriter(outStream));
        String expected = "<Root/>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputDocType() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        DocType doctype = new JDOMFactory().getDocumentBuilder().getDocumentType();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(doctype, new OutputStreamWriter(outStream));
        String expected = "<!DOCTYPE Root []\n<Root>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputElement() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Element root = new JDOMFactory().newElement("Root");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(root, new OutputStreamWriter(outStream));
        String expected = "<Root/>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputElementContent() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Element root = new JDOMFactory().newElement("Root");
        root.addContent(new JDOMFactory().getText("ChildText"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.outputElementContent(root, new OutputStreamWriter(outStream));
        String expected = "ChildText";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputList() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Element root = new JDOMFactory().newElement("Root");
        List<Object> list = new ArrayList<>();
        list.add(new JDOMFactory().getText("Child1"));
        list.add(new JDOMFactory().getText("Child2"));
        root.addContent(new JDOMFactory().getDocumentBuilder().createCDATA("ListContent"));
        root.addContent(new JDOMFactory().getDocumentBuilder().createComment("List Comment"));
        root.addContent(list);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(root, new OutputStreamWriter(outStream));
        String expected = "<Root>\n<![CDATA[ListContent]]>\n<!--List Comment-->\n<Child1/>\n<Child2/>\n";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputCDATA() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        CDATA cdata = new JDOMFactory().newCDATA("CDATAText");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(cdata, new OutputStreamWriter(outStream));
        String expected = "<![CDATA[CDATAText]]>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputText() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Text text = new JDOMFactory().getText("TextContent");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(text, new OutputStreamWriter(outStream));
        String expected = "<TextContent/>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputComment() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        Comment comment = new JDOMFactory().newComment("CommentText");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(comment, new OutputStreamWriter(outStream));
        String expected = "<!--CommentText-->";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("target", "data");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(pi, new OutputStreamWriter(outStream));
        String expected = "<?" + "target" + " " + "data" + "?>";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputEntityRef() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        EntityRef entity = new JDOMFactory().newEntityRef("entityName");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlo.output(entity, new OutputStreamWriter(outStream));
        String expected = "&entityName;";
        assertEquals(expected, outStream.toString());
    }

    @Test
    public void testOutputString() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<Root><Child>Text</Child></Root>";
        String expected = "<Root><Child>Text</Child></Root>";
        assertEquals(expected, xmlo.outputString(JDOMFactory.stringToDocument(input)));
    }

    @Test
    public void testEscapeAttributeEntities() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<Root xmlns:x=\"http://www.w3.org/2000/xmlns/\" foo=\"<bar>Baz</bar>\" />";
        String expected = "<Root xmlns:x=\"http://www.w3.org/2000/xmlns/\" foo=\"&lt;bar&gt;Baz&lt;/bar&gt;\" />";
        String result = xmlo.escapeAttributeEntities(input, XMLOutputter.DEFAULTPROCESSOR.getFormat());
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeElementEntities() throws Exception {
        XMLOutputter xmlo = new XMLOutputter();
        String input = "<Root>&lt;Child&gt;</Root>";
        String expected = "<Root>&lt;Child&gt;</Root>";
        String result = xmlo.escapeElementEntities(input, XMLOutputter.DEFAULTPROCESSOR.getFormat());
        assertEquals(expected, result);
    }
}