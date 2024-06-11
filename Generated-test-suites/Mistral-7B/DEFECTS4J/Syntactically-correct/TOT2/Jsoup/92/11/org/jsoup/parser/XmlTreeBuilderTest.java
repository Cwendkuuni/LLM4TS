package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import java.io.StringReader;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testParseBasicXml() {
        ParseSettings settings = builder.defaultSettings();
        Document xmlDoc = builder.parse(new StringReader("<root><child>Hello</child></root>"), null);
        Element root = xmlDoc.rootElement();
        Element child = root.child(0);
        assert(child.text().equals("Hello"));
    }

    @Test
    public void testParseFragmentBasicXml() {
        ParseSettings settings = builder.defaultSettings();
        List<Node> nodes = builder.parseFragment("<root><child>Hello</child></root>", null, new Parser(builder));
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assert(child.text().equals("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        ParseSettings settings = builder.defaultSettings();
        builder.parse(new StringReader("<root>"), null); // missing end tag
    }

    @Test
    public void testParseDoctypeXml() {
        ParseSettings settings = builder.defaultSettings();
        Document xmlDoc = builder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST root id ID #IMPLIED> ]><root></root>"), null);
        Element root = xmlDoc.rootElement();
        assert(root.attributes().get("id") == null);
        DocumentType doctype = (DocumentType) root.child(0);
        assert(doctype.name().equals("root"));
        assert(doctype.getPublicIdentifier() == null);
        assert(doctype.getSystemIdentifier() == null);
    }

    @Test
    public void testParseCommentXml() {
        ParseSettings settings = builder.defaultSettings();
        Document xmlDoc = builder.parse(new StringReader("<root><!-- This is a comment --></root>"), null);
        List<Node> comments = xmlDoc.body().childNodes();
        Comment comment = (Comment) comments.get(0);
        assert(comment.data().equals(" This is a comment "));
    }
}