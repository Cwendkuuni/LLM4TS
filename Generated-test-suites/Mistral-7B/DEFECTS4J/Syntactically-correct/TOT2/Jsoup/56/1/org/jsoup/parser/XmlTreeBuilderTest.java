package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parse.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        String xmlInput = "<root><child1/><child2><grandChild/></child2></root>";
        Document document = xmlTreeBuilder.parse(xmlInput);
        Element root = document.getRootElement();
        assertEquals("root", root.nodeName());
        Element child1 = root.child(0);
        assertNull(child1.child(0));
        Element child2 = root.child(1);
        Element grandChild = child2.child(0);
        assertEquals("grandChild", grandChild.nodeName());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<child><grandChild/></child>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, XmlTreeBuilder.ParseSettings.preserveCase);
        Element child = (Element) nodes.get(0);
        Element grandChild = (Element) child.child(0);
        assertEquals("grandChild", grandChild.nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", null, null, XmlTreeBuilder.ParseSettings.preserveCase);
    }

    @Test
    public void testComment() {
        String xmlInput = "<!-- This is a comment --><root><child/></root>";
        Document document = xmlTreeBuilder.parse(xmlInput);
        List<Node> commentNodes = document.getElementsByTag("comment");
        assertEquals(1, commentNodes.size());
        Comment comment = (Comment) commentNodes.get(0);
        assertEquals("This is a comment", comment.getData());
    }
}