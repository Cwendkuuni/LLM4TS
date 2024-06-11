import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
        Assert.assertNull(doc);
    }

    @Test
    public void testParseValidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<root><child/></root>"), "baseUri");
        Element root = doc.rootElement();
        Element child = root.child(0);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("baseUri", root.baseUri().toString());
        Assert.assertTrue(child.hasText());
        Assert.assertEquals("", child.text());
    }

    @Test(expected = ParseError.class)
    public void testParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<?xml version='1.0'?><root><child><"), "baseUri");
        // The parse should fail since the XML is not well-formed
    }

    @Test
    public void testParseXmlWithCDataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<root><![CDATA[Hello World]]]><child/></root>"), "baseUri");
        Element root = doc.rootElement();
        Element child = root.child(1);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("baseUri", root.baseUri().toString());
        Assert.assertNotNull(root.child(0));
        Assert.assertNull(root.child(0).child(0));
        Assert.assertEquals("Hello World", root.child(0).text());
    }

    @Test
    public void testParseXmlWithComments() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<root><!-- Comment 1 --><child><!-- Comment 2 --></child><!-- Comment 3 --></root>"), "baseUri");
        Element root = doc.rootElement();
        Element child = root.child(0);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("baseUri", root.baseUri().toString());
        Assert.assertNotNull(child);
        Assert.assertNull(child.child(0));
        Assert.assertEquals("", child.text());

        List<Node> comments = root.childNodes();
        Assert.assertEquals(3, comments.size());
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(comments.get(i) instanceof Comment);
        }
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED> ]><root><child attr='value'/></root>"), "baseUri");
        Element root = doc.rootElement();
        Element child = root.child(0);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("baseUri", root.baseUri().toString());
        Assert.assertEquals("value", child.attributes().getAttributeValue("attr"));
    }

    @Test
    public void testParseXmlFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child/></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Assert.assertEquals("root", root.tagName());
        Assert.assertEquals("child", child.tagName());
        Assert.assertEquals("baseUri", root.baseUri().toString());
        Assert.assertTrue(child.hasText());
        Assert.assertEquals("", child.text());
    }
}