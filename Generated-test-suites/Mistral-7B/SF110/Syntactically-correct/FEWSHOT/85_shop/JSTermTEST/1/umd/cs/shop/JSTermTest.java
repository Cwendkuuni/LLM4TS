package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTerm_empty() {
        JSTerm jsterm = new JSTerm();
        assertNull(jsterm.elementAt(0));
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testJSTerm_constant() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("constant");
        jsterm.makeConstant();
        assertEquals("constant", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());
        assertTrue(jsterm.isConstant());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
    }

    @Test
    public void testJSTerm_variable() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeVariable();
        assertEquals("x", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());
        assertTrue(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
    }

    @Test
    public void testJSTerm_function() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeFunction();
        jsterm.addElement("f");
        assertEquals("f", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isEval());
    }

    @Test
    public void testJSTerm_eval() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeEval(true);
        assertEquals(true, jsterm.isEval());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
    }

    @Test
    public void testJSTerm_parseTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
        JSTerm jsterm = new JSTerm();
        jsterm = jsterm.parseTerm(tokenizer);
        assertEquals("+", jsterm.elementAt(0));
        assertEquals(2, jsterm.size());
        assertNotNull(jsterm.elementAt(1));
        assertEquals("x", jsterm.elementAt(1).elementAt(0));
        assertEquals(1, jsterm.elementAt(1).size());
        assertEquals(3, jsterm.elementAt(2).toInt());
    }

    @Test
    public void testJSTerm_parseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(append x '(a b c))"));
        JSTerm jsterm = new JSTerm();
        jsterm = jsterm.parseTerm(tokenizer);
        assertEquals("append", jsterm.elementAt(0));
        assertEquals(3, jsterm.size());
        assertNotNull(jsterm.elementAt(1));
        assertEquals("x", jsterm.elementAt(1).elementAt(0));
        assertEquals(1, jsterm.elementAt(1).size());
        assertEquals("(a b c)", jsterm.elementAt(2).toString());
    }

    @Test
    public void testJSTerm_standardizerTerm() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("f");
        jsterm.addElement("x");
        JSTerm standardized = jsterm.standardizerTerm();
        assertEquals("f_1", standardized.elementAt(0));
        assertEquals("x_0", standardized.elementAt(1).elementAt(0));
        assertEquals(2, standardized.size());
    }

    @Test
    public void testJSTerm_call() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("+");
        jsterm.addElement(new JSTerm());
        jsterm.addElement("3");
        JSTerm result = jsterm.call();
        assertEquals(6, result.toInt());
    }

    @Test(expected = JSParserError.class)
    public void testJSTerm_parseError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x"));
        JSTerm jsterm = new JSTerm();
        jsterm.parseTerm(tokenizer);
    }
}