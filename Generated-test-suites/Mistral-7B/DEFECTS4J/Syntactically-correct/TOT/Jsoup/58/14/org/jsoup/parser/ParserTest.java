package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

    @Mock
    private TreeBuilder treeBuilder;

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInputWithValidHtml() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document expectedDocument = new Document() {
            @Override
            public Element body() {
                return new Element("body") {
                    @Override
                    public Element head() {
                        return new Element("head") {
                            @Override
                            public List<Node> children() {
                                return new ArrayList<>();
                            }
                        };
                    }

                    @Override
                    public String text() {
                        return "";
                    }

                    @Override
                    public Element child(int index) {
                        if (index == 0) {
                            return new Element("h1") {
                                @Override
                                public String text() {
                                    return "Test";
                                }
                            };
                        }
                        return null;
                    }
                };
            }
        };

        Document actualDocument = parser.parseInput(html, "http://test.com");

        Mockito.verify(treeBuilder, Mockito.times(1)).parse(html, "http://test.com", Mockito.isInstanceOf(ParseErrorList.class), Mockito.isInstanceOf(ParseSettings.class));
        Mockito.verify(treeBuilder, Mockito.times(1)).parse(html, "http://test.com", Mockito.isA(ParseErrorList.class), Mockito.isInstanceOf(ParseSettings.class)).nodes();

        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputWithInvalidHtml() {
        String html = "<html><head></head><body><h1>Test<script>alert('Hello World');</script></body></html>";
        List<ParseError> expectedErrors = new ArrayList<>();
        expectedErrors.add(new ParseError("Error message", "lineNumber", "columnNumber"));

        parser.setTrackErrors(1);
        Document actualDocument = parser.parseInput(html, "http://test.com");
        List<ParseError> actualErrors = parser.getErrors();

        Assert.assertEquals(expectedErrors.size(), actualErrors.size());
        for (int i = 0; i < expectedErrors.size(); i++) {
            Assert.assertEquals(expectedErrors.get(i).getMessage(), actualErrors.get(i).getMessage());
            Assert.assertEquals(expectedErrors.get(i).getLine(), actualErrors.get(i).getLine());
            Assert.assertEquals(expectedErrors.get(i).getColumn(), actualErrors.get(i).getColumn());
        }

        Assert.assertNull(actualDocument);
    }

    @Test
    public void testParseInputWithDisabledErrorTracking() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        parser.setTrackErrors(0);

        Document actualDocument = parser.parseInput(html, "http://test.com");

        Assert.assertNull(parser.getErrors());
        Assert.assertNotNull(actualDocument);
    }

    @Test
    public void testParseStaticFunctionWithValidHtml() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document expectedDocument = parse(html, "http://test.com");

        Assert.assertEquals(expectedDocument, Parser.parse(html, "http://test.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStaticFunctionWithInvalidHtml() {
        String html = "<html><head></head><body><h1>Test<script>alert('Hello World');</script></body></html>";
        parser.setTrackErrors(1);

        Parser.parse(html, "http://test.com");

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getMessage());
        Assert.assertNotNull(errors.get(0).getLine());
        Assert.assertNotNull(errors.get(0).getColumn());

        throw errors.get(0);
    }
}