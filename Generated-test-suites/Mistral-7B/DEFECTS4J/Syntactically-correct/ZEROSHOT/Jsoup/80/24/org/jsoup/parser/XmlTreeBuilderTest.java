package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\"><subElement/></element></root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");

        Element root = xmlDocument.rootElement();
        Assert.assertEquals("root", root.tagName());
        List<Node> rootChildren = root.childNodes();
        Element element = (Element) rootChildren.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("value", element.attribute("attr"));
        List<Node> elementChildren = element.childNodes();
        Element subElement = (Element) elementChildren.get(0);
        Assert.assertNull(subElement.attribute("attr"));

        // Test for empty element
        xmlString = "<element/>";
        xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        element = xmlDocument.rootElement();
        Assert.assertEquals("element", element.tagName());
        Assert.assertTrue(element.childNodes().isEmpty());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\"><subElement/></element>";
        List<Node> xmlFragmentNodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();

        Element element = (Element) xmlFragmentNodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("value", element.attribute("attr"));
        List<Node> elementChildren = element.childNodes();
        Element subElement = (Element) elementChildren.get(0);
        Assert.assertNull(subElement.attribute("attr"));
    }

    @Test(expected = AssertionError.class)
    public void testUnknownTag() {
        String xmlString = "<unknownTag/>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        // This test should fail as 'unknownTag' is not a known tag.
        xmlDocument.rootElement();
    }

    @Test
    public void testXmlDeclaration() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value\"><subElement/></element></root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDocument.childNodes().get(0);
        Assert.assertEquals("xml", xmlDeclaration.tagName());
        Assert.assertEquals("1.0", xmlDeclaration.version());
        Assert.assertEquals("UTF-8", xmlDeclaration.encoding());
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<!-- This is a comment -->\n<root><element attr=\"value\"><subElement/></element></root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        List<Node> xmlDocumentNodes = xmlDocument.childNodes();
        Comment comment = (Comment) xmlDocumentNodes.get(1);
        Assert.assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testCDataSection() {
        String xmlString = "<![CDATA[ This is a CData section ]]>\n<root><element attr=\"value\"><subElement><![CDATA[ This is another CData section ]]></subElement></element></root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        List<Node> xmlDocumentNodes = xmlDocument.childNodes();
        Element subElement = (Element) xmlDocumentNodes.get(2).childNodes().get(0);
        CDataNode cDataNode = (CDataNode) subElement.childNodes().get(0);
        Assert.assertEquals("This is another CData section", cDataNode.getData().getData());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">\n<root><element attr=\"value\"><subElement/></element></root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        DocumentType doctypeNode = (DocumentType) xmlDocument.childNodes().get(0);
        Assert.assertEquals("root", doctypeNode.tagName());
        Assert.assertEquals("http://example.com/root.dtd", doctypeNode.systemId());
    }
}