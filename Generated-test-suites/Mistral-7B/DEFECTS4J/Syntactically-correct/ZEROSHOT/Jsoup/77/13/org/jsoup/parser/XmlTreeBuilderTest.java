package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

/**
 * Test class for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root element=\"test\"><child/><anotherChild attribute=\"value\"/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("test", root.attribute("element").get());

        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertTrue(child.children().isEmpty());

        Element anotherChild = root.child(1);
        assertEquals("anotherChild", anotherChild.tagName());
        assertEquals("value", anotherChild.attribute("attribute").get());

        List<Node> children = root.children();
        assertEquals(2, children.size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root element=\"test\"><child/><anotherChild attribute=\"value\"/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Document fragmentDoc = Jsoup.parse(new StringReader(XmlTreeBuilder.tagName(fragmentNodes.get(0))), "http://test.com", Parser.xmlParser());
        Element root = fragmentDoc.rootElement();

        assertEquals("root", root.tagName());
        assertEquals("test", root.attribute("element").get());

        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertTrue(child.children().isEmpty());

        Element anotherChild = root.child(1);
        assertEquals("anotherChild", anotherChild.tagName());
        assertEquals("value", anotherChild.attribute("attribute").get());

        List<Node> children = root.children();
        assertEquals(2, children.size());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root element=\"test\"><child/><anotherChild attribute=\"value\"/></root>";
        StringReader reader = new StringReader(input);

        Token token = xmlTreeBuilder.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).next();

        // start tag
        token = xmlTreeBuilder.next();
        assertEquals(Token.StartTag.class, token.getClass());
        Element root = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("root", root.tagName());
        assertEquals("test", root.attribute("element").get());

        // self closing tag
        token = xmlTreeBuilder.next();
        assertEquals(Token.StartTag.class, token.getClass());
        Element child = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("child", child.tagName());
        assertTrue(child.children().isEmpty());
        assertTrue(child.isSelfClosing());

        token = xmlTreeBuilder.next();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.popStackToClose(token.asEndTag());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root element=\"test\"><child><anotherChild attribute=\"value\"/></child></root>";
        StringReader reader = new StringReader(input);

        Token token = xmlTreeBuilder.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).next();

        // start tags
        token = xmlTreeBuilder.next();
        assertEquals(Token.StartTag.class, token.getClass());
        Element root = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("root", root.tagName());
        assertEquals("test", root.attribute("element").get());

        token = xmlTreeBuilder.next();
        assertEquals(Token.StartTag.class, token.getClass());
        Element child = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("child", child.tagName());
        assertTrue(child.children().isEmpty());
        stack.push(child);

        token = xmlTreeBuilder.next();
        assertEquals(Token.StartTag.class, token.getClass());
        Element anotherChild = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("anotherChild", anotherChild.tagName());
        assertEquals("value", anotherChild.attribute("attribute").get());
        stack.push(anotherChild);

        // end tag
        token = xmlTreeBuilder.next();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertEquals(child, stack.peek());

        // another end tag
        token = xmlTreeBuilder.next();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertEquals(root, stack.peek());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root element=\"test\"><child><anotherChild attribute=\"value\"/><invalidToken/></child></root>";
        StringReader reader = new StringReader(input);

        Token token = xmlTreeBuilder.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).next();

        token = xmlTreeBuilder.next(); // start tag
        token = xmlTreeBuilder.next(); // start tag
        token = xmlTreeBuilder.next(); // start tag
        token = xmlTreeBuilder.next(); // end tag
        token = xmlTreeBuilder.next(); // end tag
        token = xmlTreeBuilder.next(); // valid character

        token = xmlTreeBuilder.next(); // invalid token
        Validate.fail("Unexpected token type: " + token.type);
    }
}