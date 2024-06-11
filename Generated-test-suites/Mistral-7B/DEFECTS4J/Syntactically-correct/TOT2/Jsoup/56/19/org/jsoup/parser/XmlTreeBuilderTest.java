import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        String xmlInput = "<root><child element='value' /></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.tagName());
        Element child = root.getChild(0);
        assertEquals("child", child.tagName());
        assertEquals("value", child.attr("element"));
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><child element='value' /><child element='value2' /></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        Element root = xmlDoc.getRootElement();
        Element child1 = root.getChild(0);
        assertEquals("child", child1.tagName());
        Element child2 = root.getChild(1);
        assertEquals("child", child2.tagName());
        assertNotSame(child1, child2);
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><child><grandchild /></child></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        List<Node> nodes = xmlDoc.childNodes();
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Element grandChild = (Element) nodes.get(2);
        assertEquals("grandchild", grandChild.tagName());
        assertEquals(root, grandChild.parent());
        assertEquals(child, grandChild.parent().getChild(0));
    }

    @Test
    public void testProcessCommentTag() {
        String xmlInput = "<root><!--comment--> <child element='value' /></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        List<Node> nodes = xmlDoc.childNodes();
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Comment comment = (Comment) nodes.get(2);
        assertEquals("comment", comment.text());
        assertEquals(root, comment.parent());
    }

    @Test(expected = AssertionError.class)
    public void testProcessInvalidToken() {
        String xmlInput = "<root><child>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        new XmlTreeBuilder().parse(xmlInput, "baseUri");
        Validate.fail("Unexpected exception not thrown");
    }

    @Test(expected = AssertionError.class)
    public void testProcessInvalidDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ENTITY % entity SYSTEM 'http://example.com' > ]><root></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        new XmlTreeBuilder().parse(xmlInput, "baseUri");
        Validate.fail("Unexpected exception not thrown");
    }

    @Test(expected = AssertionError.class)
    public void testProcessCharacterTokenWithEmptyInput() {
        String xmlInput = "";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        new XmlTreeBuilder().parse(xmlInput, "baseUri");
        Validate.fail("Unexpected exception not thrown");
    }

    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><child><grandchild /></child></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        List<Node> nodes = xmlDoc.childNodes();
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Element grandChild = (Element) nodes.get(2);
        assertEquals("grandchild", grandChild.tagName());
        assertEquals(root, grandChild.parent());
        assertEquals(child, grandChild.parent().getChild(0));
    }

    @Test
    public void testParseFragment() {
        String xmlInput = "<root><child element='value' /></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlInput, "baseUri", new ParseErrorList(), settings).childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertEquals("value", child.attr("element"));
    }

    @Test
    public void testProcessUnknownTag() {
        String xmlInput = "<root><unknown tag='value' /></root>";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "baseUri");
        List<Node> nodes = xmlDoc.childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element unknown = (Element) nodes.get(1);
        assertEquals("unknown", unknown.tagName());
        assertEquals("tag", unknown.attr("tag"));
        assertEquals("value", unknown.attr("value"));
    }

    @Test
    public void testPopStackToCloseWithEmptyStack() {
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        new XmlTreeBuilder().popStackToClose(new Token.EndTag("root"));
        Validate.fail("Unexpected exception not thrown");
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        String xmlInput = "";
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlInput, "baseUri", new ParseErrorList(), settings).childNodes();
        assertEquals(0, nodes.size());
    }
}