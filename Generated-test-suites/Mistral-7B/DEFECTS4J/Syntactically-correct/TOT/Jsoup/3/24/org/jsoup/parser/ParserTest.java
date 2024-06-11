package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ParserTest {

    private Parser parser;
    private Document parsedDocument;

    @Before
    public void setup() {
        String html = "<!DOCTYPE html>\n" +
                "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word' xmlns:m='urn:schemas-microsoft-com:office:mac'\n" +
                "xmlns:v='urn:schemas-microsoft-com:vml'\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "<p>This is a <strong>test</strong>.</p>\n" +
                "</body>\n" +
                "</html>";

        String baseUri = "http://example.com";
        this.parser = new Parser(html, baseUri, false);
        this.parsedDocument = parser.parse();
    }

    @Test
    public void testParseHtml() {
        // Check if parsed document is not null
        assertThat(parsedDocument, is(notNullValue()));

        // Check if parsed document has expected number of elements
        List<Node> elements = new ArrayList<>(parsedDocument.bodyChildren());
        assertThat(elements.size(), is(3));

        // Check if parsed h1 and p elements have expected text
        Element h1 = (Element) elements.get(0);
        assertThat(h1.text(), is("Hello World"));

        Element p = (Element) elements.get(1);
        assertThat(p.text(), is("This is a <strong>test</strong>."));

        // Check if parsed strong element has expected text
        Element strong = (Element) p.child(0);
        assertThat(strong.text(), is("test"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a <strong>test</strong></p>";
        Document bodyParsedDocument = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        // Check if parsed document is not null
        assertThat(bodyParsedDocument, is(notNullValue()));

        // Check if parsed document has empty head
        assertThat(bodyParsedDocument.head(), is(nullValue()));

        // Check if parsed document has expected number of elements
        List<Node> elements = new ArrayList<>(bodyParsedDocument.bodyChildren());
        assertThat(elements.size(), is(2));

        // Check if parsed p and strong elements have expected text
        Element p2 = (Element) elements.get(0);
        assertThat(p2.text(), is("This is a <strong>test</strong>"));

        Element strong2 = (Element) p2.child(0);
        assertThat(strong2.text(), is("test"));
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html>";
        Document xmlDeclParsedDocument = parser.parse(xmlDeclHtml, "http://example.com");

        // Check if parsed document has expected XmlDeclaration element
        XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclParsedDocument
                .getElementById("xml-declaration");
        assertThat(xmlDeclaration, is(notNullValue()));
        assertThat(xmlDeclaration.version(), is("1.0"));
        assertThat(xmlDeclaration.encoding(), is("UTF-8"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String invalidHtml = "</html>";
        parser = new Parser(invalidHtml, "http://example.com", false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        String emptyHtml = "";
        parser = new Parser(emptyHtml, "http://example.com", false);
        parser.parse();
    }
}