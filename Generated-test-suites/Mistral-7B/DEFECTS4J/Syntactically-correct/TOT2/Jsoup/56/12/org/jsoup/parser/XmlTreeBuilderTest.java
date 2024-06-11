package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        // Test defaultSettings method
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testParseMethodWithValidInput() {
        // Test parse method with valid input
        String xmlInput = "<root><child>Hello World</child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");
        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        Assert.assertEquals("Hello World", child.text());
    }

    @Test
    public void testProcessStartTagMethod() {
        // Test processStartTag method
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("root");
        Element el = xmlTreeBuilder.insert(startTagToken);
        Assert.assertEquals("root", el.tagName());
    }

    @Test(expected = AssertionError.class)
    public void testProcessEndTagMethodWithUnknownTagName() {
        // Test popStackToClose method with unknown tag name
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTagToken = new Token.EndTag("unknown");
        xmlTreeBuilder.popStackToClose(endTagToken);
    }

    @Test
    public void testParseFragmentMethodWithValidInput() {
        // Test parseFragment method with valid input
        String xmlFragment = "<root><child>Hello World</child></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Assert.assertEquals("Hello World", child.text());
    }

    // Add other test cases for other methods and edge cases
}