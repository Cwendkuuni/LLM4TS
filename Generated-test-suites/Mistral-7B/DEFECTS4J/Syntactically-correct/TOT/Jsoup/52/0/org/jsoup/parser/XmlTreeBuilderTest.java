import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String xml = "";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Assert.assertEquals(0, nodes.size());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "<root><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        Element root = document.getRootElement();
        Element child = root.getChild(0);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("root", root.nodeName());
        Assert.assertEquals("child", child.nodeName());
        Assert.assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xml = "<root attr1=\"value1\" attr2='value2'><child attr3=\"value3\">Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        Element root = document.getRootElement();
        Element child = root.getChild(0);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals("root", root.nodeName());
        Assert.assertEquals("child", child.nodeName());
        Assert.assertEquals("value1", root.attr("attr1"));
        Assert.assertEquals("value2", root.attr("attr2"));
        Assert.assertEquals("value3", child.attr("attr3"));
        Assert.assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<root><img src=\"image.png\" /> </root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        Element root = document.getRootElement();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals("root", root.nodeName());
        Element img = null;
        for (Node node : nodes) {
            if (node instanceof Element && node.nodeName().equals("img")) {
                img = (Element) node;
                break;
            }
        }
        Assert.assertNotNull(img);
        Assert.assertNull(img.getChildNodes().get(0));
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<root><!-- This is a comment --><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        Element root = document.getRootElement();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals("root", root.nodeName());
        Element child = root.getChild(0);
        Assert.assertEquals("child", child.nodeName());
        Assert.assertEquals(1, root.childNodes().size());
        Assert.assertEquals(1, child.childNodes().size());
        Node comment = root.childNodes().get(0);
        Assert.assertTrue(comment instanceof Comment);
        Assert.assertEquals("This is a comment", comment.text().toString());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html><body><p>Hello World</p></body></html>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xml, "", xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        DocumentType doctype = document.doctype();
        Assert.assertEquals("html", doctype.name());
        Assert.assertNotNull(doctype.publicIdentifier());
        Assert.assertNotNull(doctype.systemIdentifier());
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("html", nodes.get(0).nodeName());
    }

    @Test
    public void testParseXmlWithError() {
        String xml = "<root <child>Hello World </root>"; // missing end tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document document = null;
        try {
            document = Jsoup.parse(xml, "", xmlTreeBuilder);
        } catch (Exception e) {
            // expected
        }
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", errors);
        Assert.assertEquals(0, nodes.size());
        Assert.assertTrue(errors.getErrors().size() > 0);
        Assert.assertEquals("Unexpected end tag : child", errors.getErrors().get(0).toString());
    }
}