package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setup() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        parser = new Parser(html, null, false);
        doc = parser.parse();
    }

    @Test
    public void testParse() {
        assertThat(doc.tagName(), is("html"));
        Element html = doc.body();
        assertThat(html.tagName(), is("body"));
        Element title = doc.head().title();
        assertThat(title.text(), is("Test Page"));
        Element heading = html.children().get(0);
        assertThat(heading.tagName(), is("h1"));
        Element paragraph = html.children().get(1);
        assertThat(paragraph.tagName(), is("p"));
        assertThat(paragraph.text(), is("Test Paragraph"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph</p>";
        Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, null);
        Element body = fragmentDoc.body();
        Element paragraph = body.children().get(0);
        assertThat(paragraph.tagName(), is("p"));
        assertThat(paragraph.text(), is("Test Paragraph"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        new Parser(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new Parser("", null, false);
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = new Parser(html, null, false).parse();
        Element root = doc;
        assertThat(root.tagName(), is("html"));
        assertThat(root.childElements().size(), is(0));
    }

    @Test
    public void testParseComment() {
        String html = "<html>\n" +
                "<!-- Test Comment -->\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element comment = doc.body().childElements().get(0);
        assertThat(comment.tagName(), is(Comment.class.getSimpleName()));
        assertThat(comment.text(), is("Test Comment"));
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
        Document doc = new Parser(html, null, false).parse();
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.head().childElements().get(0);
        assertThat(xmlDecl.xmlVersion(), is("1.0"));
        assertThat(xmlDecl.encoding(), is("UTF-8"));
        assertThat(xmlDecl.isStandalone(), is(false));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element head = doc.head();
        Element body = doc.body();
        Element htmlElement = doc;
        assertThat(head.tagName(), is("head"));
        assertThat(head.childElements().size(), is(0));
        assertThat(body.tagName(), is("body"));
        assertThat(body.childElements().size(), is(0));
        assertThat(htmlElement.childElements().size(), is(2));
    }

    @Test
    public void testParseStartTag() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element htmlElement = doc;
        Element head = doc.head();
        Element title = head.title();
        Element body = doc.body();
        Element heading = body.children().get(0);
        Element paragraph = body.children().get(1);
        // Test implicit parent
        String implicitHtml = "<html>\n" +
                "<body>\n" +
                "<textarea></textarea>\n" +
                "</body>\n" +
                "</html>";
        Document implicitDoc = new Parser(implicitHtml, null, false).parse();
        Element implicitTextarea = implicitDoc.body().get(0).get(0);
        Element implicitBody = implicitTextarea.parent();
        assertThat(implicitBody.tagName(), is("body"));
        // Test invalid parent
        String invalidHtml = "<html>\n" +
                "<body>\n" +
                "<h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";
        Document invalidDoc = new Parser(invalidHtml, null, false).parse();
        Element invalidH1 = invalidDoc.body().get(0);
        Element invalidP = invalidH1.get(1);
        assertThat(invalidP, nullValue());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.jpg\" alt=\"Test Image\" title=\"Test Title\">";
        Document doc = new Parser(html, null, false).parse();
        Element img = doc.body().get(0);
        HashMap<String, String> attributes = img.attributes();
        assertThat(attributes.get("src"), is("image.jpg"));
        assertThat(attributes.get("alt"), is("Test Image"));
        assertThat(attributes.get("title"), is("Test Title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html>\n" +
                "<body>\n" +
                "<p>Test Paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element p = doc.body().get(0).get(0);
        String text = p.text();
        assertThat(text, is("Test Paragraph"));
    }

    @Test
    public void testParseCdata() {
        String html = "<html>\n" +
                "<body>\n" +
                "<script>\n" +
                "<![CDATA[\n" +
                "function testFunction() {\n" +
                "alert('Test');\n" +
                "}\n" +
                "]]>\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element script = doc.body().get(0).get(0);
        String rawText = script.childNodes().get(0).text();
        assertThat(rawText, is("function testFunction() {\nalert('Test');\n}"));
    }

    @Test
    public void testAddChildToParent() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element h1 = doc.body().get(0);
        Element p = new Element("p", null);
        Element parent = h1;
        Element child = Parser.addChildToParent(p, false);
        h1.appendChild(child);
        parent = h1;
        child = Parser.addChildToParent(new Element("ul", null), false);
        h1.appendChild(child);
        parent = child;
        child = Parser.addChildToParent(new Element("li", null), true);
        parent.appendChild(child);
        List<Element> children = h1.children();
        assertThat(children.size(), is(3));
        Element firstChild = children.get(0);
        assertThat(firstChild.tagName(), is("p"));
        Element secondChild = children.get(1);
        assertThat(secondChild.tagName(), is("ul"));
        List<Element> ulChildren = secondChild.children();
        assertThat(ulChildren.size(), is(1));
        Element liChild = ulChildren.get(0);
        assertThat(liChild.tagName(), is("li"));
    }

    @Test
    public void testStackHasValidParent() {
        String html = "<html>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element h1 = doc.body().get(0);
        Tag h1Tag = h1.tag();
        Tag bodyTag = h1.parent().tag();
        Tag htmlTag = doc.tag();
        assertThat(Parser.stackHasValidParent(h1Tag), is(true));
        assertThat(Parser.stackHasValidParent(bodyTag), is(true));
        assertThat(Parser.stackHasValidParent(htmlTag), is(true));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<html>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element h1 = doc.body().get(0);
        Element p = new Element("p", null);
        Element parent = h1;
        Element child = Parser.popStackToSuitableContainer(h1Tag);
        h1.appendChild(child);
        parent = h1;
        child = new Element("ul", null);
        parent = Parser.popStackToSuitableContainer(h1Tag);
        h1.appendChild(child);
        parent = child;
        child = new Element("li", null);
        assertThat(Parser.popStackToSuitableContainer(h1Tag), nullValue());
    }

    @Test
    public void testPopStackToClose() {
        String html = "<html>\n" +
                "<body>\n" +
                "<h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element h1 = doc.body().get(0);
        Element p = h1.get(1);
        Element parent = Parser.popStackToClose(h1Tag);
        assertThat(parent, h1);
    }

    @Test
    public void testLast() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test Heading</h1>\n" +
                "<p>Test Paragraph</p>\n" +
                "</body>\n" +
                "</html>";
        Document doc = new Parser(html, null, false).parse();
        Element last = Parser.last();
        assertThat(last, doc);
    }

    @After
    public void teardown() {
        parser = null;
        doc = null;
    }
}