package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    // test methods for Parser class
    // ...

    // test methods for static methods
    // ...

    // test methods for getters and setters
    // ...

    // test method for getErrors
    // ...
}