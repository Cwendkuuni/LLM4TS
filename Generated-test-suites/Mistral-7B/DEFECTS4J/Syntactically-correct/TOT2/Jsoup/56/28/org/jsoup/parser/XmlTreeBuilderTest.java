package org.jsoup.parser; // or the package where XmlTreeBuilder is located

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testParseMethod() {
        String xmlInput = "<root><child>Hello</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        assertEquals("Hello", child.text());
    }

    @Test
    public void testProcessStartTagMethod() {
        String xmlInput = "<root><child/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        assertEquals(0, root.childNodes().size());
        assertEquals("child", child.tagName());
        assertTrue(child.isSelfClosing());
    }

    @Test
    public void testProcessEndTagMethod() {
        String xmlInput = "<root><child /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.getRootElement();
        List<Node> children = root.childNodes();
        Element child = (Element) children.get(0);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        List<Node> updatedChildren = root.childNodes();

        assertEquals(0, updatedChildren.size());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlInput = "<root>"; // missing end tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Validate.expect(AssertionError.class);
        xmlTreeBuilder.process(new Token(Document.TokenType.EOF, ""));
    }

    @Test
    public void testParseFragmentMethod() {
        String xmlInput = "<root><child><grandChild>Hello</grandChild></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, "", new ParseErrorList(), ParseSettings.preserveCase);

        Document xmlDoc = Jsoup.parseBodyFragment(nodes.toString());
        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        Element grandChild = child.getChild(0);
        assertEquals("Hello", grandChild.text());
    }
}