package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testParseConstantTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("-123.45");
        assertEquals("-123.45", term.toStr().toString());
        term.parseTerm("3.14159265359");
        assertEquals("3.14159265359", term.toStr().toString());
    }

    @Test
    public void testParseVariableTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("x");
        assertEquals("x", term.toStr().toString());
        term.parseTerm("y");
        assertEquals("y", term.toStr().toString());
        term.parseTerm("z");
        assertEquals("z", term.toStr().toString());
    }

    @Test
    public void testParseFunctionTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("+");
        assertEquals("+", term.toStr().toString());
        term.parseTerm("-");
        assertEquals("-", term.toStr().toString());
        term.parseTerm("*");
        assertEquals("*", term.toStr().toString());
        term.parseTerm("/");
        assertEquals("/", term.toStr().toString());
        term.parseTerm("=");
        assertEquals("=", term.toStr().toString());
        term.parseTerm("<");
        assertEquals("<", term.toStr().toString());
        term.parseTerm(">");
        assertEquals(">", term.toStr().toString());
        term.parseTerm("<=");
        assertEquals("<=", term.toStr().toString());
        term.parseTerm(">=");
        assertEquals(">=", term.toStr().toString());
        term.parseTerm("call");
        assertEquals("call", term.toStr().toString());
        term.parseTerm("list");
        assertEquals("list", term.toStr().toString());
    }

    @Test
    public void testParseListTerm() {
        JSTerm term = new JSTerm();
        term.parseListTerm();
        assertEquals("nil", term.toStr().toString());

        term.parseListTerm();
        term.parseTerm("1");
        term.parseTerm(".");
        term.parseTerm("2");
        term.parseListTerm();

        assertEquals("(1 . 2)", term.toStr().toString());
    }

    @Test
    public void testParseFunctionCallTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("call");
        term.parseTerm("sin");
        term.parseTerm("(");
        term.parseTerm("3.14159265359");
        term.parseTerm(")");

        assertEquals("(call sin 3.14159265359)", term.toStr().toString());
    }

    @Test
    public void testParseFunctionApplicationTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("*");
        term.parseTerm("2");
        term.parseTerm(".");
        term.parseTerm("3");
        term.parseTerm(".");
        term.parseTerm("4");

        assertEquals("(* 2 3 4)", term.toStr().toString());
    }

    @Test
    public void testParseTermWithSpaces() {
        JSTerm term = new JSTerm();
        term.parseTerm(" * ");
        term.parseTerm("2");
        term.parseTerm(" ");
        term.parseTerm("+");
        term.parseTerm("3");
        term.parseTerm(" ");
        term.parseTerm("4");

        assertEquals("(* 2 + 3 4)", term.toStr().toString());
    }

    @Test
    public void testParseTermWithComments() {
        JSTerm term = new JSTerm();
        term.parseTerm("(* This is a comment *");
        term.parseTerm("+");
        term.parseTerm("1");
        term.parseTerm(" ; This is another comment *");
        term.parseTerm("2");

        assertEquals("(+ 1 2)", term.toStr().toString());
    }

    @Test
    public void testParseTermWithErrorHandling() {
        JSTerm term = new JSTerm();
        try {
            term.parseTerm("---");
            fail();
        } catch (JSParserError e) {
            assertEquals("Error reading control parameters: ---", e.getMessage());
        }

        try {
            term.parseTerm("Expecting Term");
            fail();
        } catch (JSParserError e) {
            assertEquals("Expecting Term", e.getMessage());
        }

        try {
            term.parseTerm("%%%");
            fail();
        } catch (JSParserError e) {
            assertEquals("Term expected", e.getMessage());
        }

        try {
            term.parseTerm("5 *");
            fail();
        } catch (JSParserError e) {
            assertEquals("Term expected", e.getMessage());
        }
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("+ x y z");
        assertEquals("(+ x y z)", term.toStr().toString());

        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("(+ x1 y1 z1)", standardizedTerm.toStr().toString());
    }

    @Test
    public void testCallTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm("(+ 1 2 3)");
        assertEquals("(+ 1 2 3)", term.toStr().toString());

        JSTerm calledTerm = term.call();
        assertEquals("(+ 1 2 3)", calledTerm.toStr().toString());

        term.parseTerm("(+ x y z)");
        calledTerm = term.call();
        assertEquals("(+ x y z)", calledTerm.toStr().toString());

        term.parseTerm("(+ (sin x) (* y z) (- 2 x))");
        calledTerm = term.call();
        assertEquals("(+ (sin x) (* y z) (- 2 x))", calledTerm.toStr().toString());
    }
}