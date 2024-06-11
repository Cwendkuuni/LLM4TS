import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.Attribute;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p>This is a <b>test</b> page.</p>" +
                "</body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        // Check document structure
        Assert.assertEquals("html", doc.tagName());
        Element htmlNode = doc.head();
        Assert.assertEquals("head", htmlNode.tagName());
        Element titleNode = htmlNode.child(0);
        Assert.assertEquals("title", titleNode.tagName());
        String titleText = titleNode.text();
        Assert.assertEquals("Test Page", titleText);
        Element bodyNode = doc.body();
        Assert.assertEquals("body", bodyNode.tagName());
        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());
        String h1Text = h1Node.text();
        Assert.assertEquals("Hello World", h1Text);
        Element pNode = bodyNode.child(1);
        Assert.assertEquals("p", pNode.tagName());
        List<Node> pChildren = pNode.children();
        Assert.assertEquals(2, pChildren.size());
        Element bNode = (Element) pChildren.get(0);
        Assert.assertEquals("b", bNode.tagName());
        String bText = bNode.text();
        Assert.assertEquals("test", bText);
        TextNode textNode = (TextNode) pChildren.get(1);
        String text = textNode.text();
        Assert.assertEquals(" this is a ", text.substring(0, text.length()-2));
        Assert.assertEquals("page.", text.substring(text.length()-2));

        // Check attributes
        Element imgNode = new Element(Tag.valueOf("img"), "http://example.com");
        imgNode.setAttribute("src", "image.jpg");
        imgNode.setAttribute("alt", "An image");
        Element imgInDocument = doc.parseBodyFragment(imgNode.body().toString(), "http://example.com");
        Element imgElement = imgInDocument.body().child(0);
        List<Attribute> imgAttributes = imgElement.attributes();
        Assert.assertEquals(2, imgAttributes.size());
        Attribute srcAttribute = imgAttributes.get(0);
        Assert.assertEquals("src", srcAttribute.getKey());
        Assert.assertEquals("image.jpg", srcAttribute.getValue());
        Attribute altAttribute = imgAttributes.get(1);
        Assert.assertEquals("alt", altAttribute.getKey());
        Assert.assertEquals("An image", altAttribute.getValue());

        // Check comments and CDATA
        String htmlWithComment = "<html><!-- This is a comment --><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p>This is a <b>test</b> page.</p>" +
                "<!-- Another comment -->" +
                "<script><![CDATA[function test() { alert('Hello World'); }]]></script>" +
                "</body></html>";
        Document htmlWithCommentDoc = Parser.parse(htmlWithComment, "http://example.com");
        Comment firstComment = (Comment) htmlWithCommentDoc.body().child(0);
        String commentText = firstComment.text();
        Assert.assertEquals("This is a comment", commentText);
        Element scriptNode = htmlWithCommentDoc.body().child(3);
        DataNode cdataNode = (DataNode) scriptNode.child(0);
        String cdataText = cdataNode.text();
        Assert.assertEquals("function test() { alert('Hello World'); }", cdataText);

        // Check base tag
        String htmlWithBase = "<html><base href=\"/test/\">" +
                "<body><a href=\"link.html\">Link</a></body></html>";
        Document baseDoc = Parser.parse(htmlWithBase, "http://example.com");
        String baseUrl = baseDoc.baseUri();
        Assert.assertEquals("/test/link.html", baseUrl);

        // Check error cases
        try {
            Parser.parse("<html><body></body></html>", "http://example.com");
            Assert.fail("Expected ParserException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Parser.parseBodyFragment("</html>", "http://example.com");
            Assert.fail("Expected ParserException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Parser.parse("<html><head><script>", "http://example.com");
            Assert.fail("Expected ParserException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBodyFragment() {
        String htmlFragment = "<p>This is a <b>test</b> fragment.</p>";
        Document doc = Parser.parseBodyFragment(htmlFragment, "http://example.com");
        Element bodyNode = doc.body();
        Element pNode = bodyNode.child(0);
        List<Node> pChildren = pNode.children();
        Element bNode = (Element) pChildren.get(0);
        Assert.assertEquals("p", pNode.tagName());
        Assert.assertEquals("b", bNode.tagName());
        String bText = bNode.text();
        Assert.assertEquals("test", bText);
        TextNode textNode = (TextNode) pChildren.get(1);
        String text = textNode.text();
        Assert.assertEquals(" This is a ", text.substring(0, text.length()-2));
        Assert.assertEquals("fragment.", text.substring(text.length()-2));
    }

    @Test
    public void testParseXMLDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse("<html>" + xmlDecl + "</html>", "http://example.com");
        XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.body().child(0);
        String version = xmlDeclaration.version();
        String encoding = xmlDeclaration.encoding();
        Assert.assertEquals("1.0", version);
        Assert.assertEquals("UTF-8", encoding);
    }
}