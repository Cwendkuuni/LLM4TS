package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    @Test
    public void testEmptyConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testStringConstructorValidInput() {
        String input = "!p(x)";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(2, jspf.size());
        assertEquals("!", jspf.elementAt(0).toString());
        assertEquals("p(x)", jspf.elementAt(1).toString());
    }

    @Test
    public void testNullTokenizer() {
        StringReader sr = null;
        StreamTokenizer st = new StreamTokenizer(sr);
        try {
            JSPredicateForm jspf = new JSPredicateForm(st);
            fail("Expected JSParserError or IOException");
        } catch (IOException e) {
            // This exception should not be thrown
        } catch (JSParserError e) {
            // Expected exception
            assertTrue(e.getMessage().contains("expected '('"));
        }
    }

    @Test
    public void testEmptyInputStream() {
        StringReader sr = new StringReader("");
        StreamTokenizer st = new StreamTokenizer(sr);
        try {
            JSPredicateForm jspf = new JSPredicateForm(st);
            fail("Expected JSParserError or IOException");
        } catch (IOException e) {
            // This exception should not be thrown
        } catch (JSParserError e) {
            // Expected exception
            assertTrue(e.getMessage().contains("Unexpected EOF found"));
        }
    }

    // Testing for invalid input format
    // Testing for incorrectly nested conditions
    // Testing for exceptions thrown when reading from the tokenizer
}