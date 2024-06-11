import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlDeclaration;
import org.jsoup.parser.CDataNode;
import org.jsoup.parser.TextNode;
import org.jsoup.parser.DocumentType;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Tag;

import java.io.StringReader;
import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseValidXml() {
        String xml = "<root><element1 attr1='value1'>contents</element1><element2/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        Element root = doc.getRootElement();
        Element element1 = root.getElementsByTagName("element1").get(0);
        Element element2 = root.getElementsByTagName("element2").get(0);

        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals("element1", element1.tagName());
        Assertions.assertEquals("value1", element1.attr("attr1"));
        Assertions.assertNull(element2.attr("attr1"));
        Assertions.assertNotNull(element1.child(0));
        Assertions.assertEquals(0, element2.childNodes().size());
    }

    @Test
    public void testParseSelfClosingTag() {
        String xml = "<root><img src='image.jpg'/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        Element root = doc.getRootElement();
        Element img = root.getElementsByTagName("img").get(0);

        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals("img", img.tagName());
        Assertions.assertNotNull(img.attr("src"));
        Assertions.assertTrue(img.isSelfClosing());
        Assertions.assertNull(img.childNodes().get(0));
    }

    @Test
    public void testParseComment() {
        String xml = "<root><!-- this is a comment --></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        List<Node> nodes = doc.childNodes();

        Assertions.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assertions.assertTrue(node instanceof Comment);
        Assertions.assertEquals("this is a comment", node.text());
    }

    @Test
    public void testParseCDataSection() {
        String xml = "<root><![CDATA[ this is a CDATA section ]]></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        List<Node> nodes = doc.childNodes();

        Assertions.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assertions.assertTrue(node instanceof CDataNode);
        Assertions.assertEquals("this is a CDATA section", node.text());
    }

    @Test
    public void testParseDocumentType() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr='value'></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml), "http://example.com");
        DocumentType doctype = doc.doctype();

        Assertions.assertEquals("root", doc.tagName());
        Assertions.assertNotNull(doctype);
        Assertions.assertEquals("root", doctype.name());
        Assertions.assertEquals("http://example.com/", doctype.publicId());
        Assertions.assertNull(doctype.systemId());
        Assertions.assertEquals("element", doctype.elementName());
        Assertions.assertEquals("attr", doctype.attributeName("attr"));
        Assertions.assertEquals("CDATA", doctype.attributeType("attr").toString());
        Assertions.assertTrue(doctype.isInternalSubset());
    }

    @Test
    public void testParseErrorHandling() {
        String xml = "<root><!-- <script src='script.js'></script> --></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = null;

        try {
            doc = builder.parse(new StringReader(xml), "http://example.com", errors, XmlTreeBuilder.ParseSettings.preserveCase);
        } catch (IOException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }

        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(ParseError.SCRIPT_IN_XML, errors.get(0).getType());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr='value'/><![CDATA[ this is a CDATA section ]]><element2/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

        Assertions.assertEquals(4, nodes.size());
        Element root = (Element) nodes.get(0);
        Assertions.assertEquals("root", root.tagName());
        Element element1 = (Element) nodes.get(1);
        Assertions.assertEquals("element", element1.tagName());
        Assertions.assertEquals("value", element1.attr("attr"));
        Assertions.assertEquals(0, element1.childNodes().size());
        Element element2 = (Element) nodes.get(3);
        Assertions.assertEquals("element2", element2.tagName());
        Assertions.assertEquals(0, element2.childNodes().size());
    }
}