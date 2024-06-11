import static org.junit.Assert.*;
import static org.w3c.dom.Nodeube.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilderFactory.NewDocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {
    @org.junit.Test
    public void testChildren() {
        String xmlString = "<root><a><b/><c/></a></root>";
        DocumentBuilderFactory factory = new DocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        document.getDocumentElement().getChildNodes().setNamedItem(" description", createTextNode(" root description"));

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> expected = helper.children(document.getDocumentElement());
        List<Node> actual = helper.children(document.getDocumentElement());

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNodeName(), actual.get(i).getNodeName());
            assertEquals(expected.get(i).getNodeType(), actual.get(i).getNodeType());
        }
    }

    @org.junit.Test
    public void testFind() {
        String xmlString = "<root><a><b/><c/></a></root>";
        DocumentBuilderFactory factory = new DocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        document.getDocumentElement().getChildNodes().setNamedItem(" description", createTextNode(" root description"));

        OperationsHelper helper = new OperationsHelperImpl();
        Node expected = helper.find(document, "/root/a/c");
        Node actual = helper.find(document, "/root/a/c");

        assertEquals(expected, actual);
        if (expected != null) {
            assertEquals(expected.getNodeName(), "c");
        }
    }

    @org.junit.Test
    public void testFindError() {
        String xmlString = "<root><a><b/><c/></a></root>";
        DocumentBuilderFactory factory = new DocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        document.getDocumentElement().getChildNodes().setNamedItem(" description", createTextNode(" root description"));

        OperationsHelper helper = new OperationsHelperImpl();
        Node actual = helper.find(document, "/root/x/x");

        assertNull(actual);
    }

    @org.junit.Test
    public void testNodeWithName() {
        String xmlString = "<root><a><b/><c name=\"test\"/><d name=\"test\"/></a></root>";
        DocumentBuilderFactory factory = new DocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        document.getDocumentElement().getChildNodes().setNamedItem(" description", createTextNode(" root description"));

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> nodes = helper.children(document.getDocumentElement());
        Node expected = helper.nodeWithName(nodes, "c");
        Node actual = helper.nodeWithName(nodes, "c");

        assertEquals(expected, actual);
        if (expected != null) {
            assertEquals(expected.getNodeName(), "c");
        }
    }

    @org.junit.Test
    public void testSublistWithName() {
        String xmlString = "<root><a><b/><c name=\"test\"/><d name=\"test\"/></a></root>";
        DocumentBuilderFactory factory = new DocumentBuilderFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        document.getDocumentElement().getChildNodes().setNamedItem(" description", createTextNode(" root description"));

        OperationsHelper helper = new OperationsHelperImpl();
        List<Node> nodes = helper.children(document.getDocumentElement());
        List<Node> expected = helper.sublistWithName(nodes, "c");
        List<Node> actual = helper.sublistWithName(nodes, "c");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getNodeName(), actual.get(i).getNodeName());
            assertEquals(expected.get(i).getNodeType(), actual.get(i).getNodeType());
        }
    }
}