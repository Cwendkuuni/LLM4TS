package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.select("h1").text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    // Add test cases for parseComment, parseCdata, parseXmlDecl, parseEndTag, parseStartTag, parseTextNode, addChildToParent, stackHasValidParent, popStackToSuitableContainer, and popStackToClose methods here
}