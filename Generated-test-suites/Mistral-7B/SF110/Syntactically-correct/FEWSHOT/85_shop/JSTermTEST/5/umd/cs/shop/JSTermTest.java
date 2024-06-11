package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermEmpty() {
        JSTerm term = new JSTerm();
        assertTrue(term.isFunction());
        assertEquals(0, term.size());
        assertNull(term.elementAt(0));
    }

    @Test
    public void testJSTermVariable() {
        JSTerm term = new JSTerm("x");
        assertTrue(term.isVariable());
        assertEquals("x", term.elementAt(0));
        assertEquals(1, term.size());
    }

    @Test
    public void testJSTermConstant() {
        JSTerm term = new JSTerm("5");
        assertTrue(term.isConstant());
        assertEquals("5", term.elementAt(0));
        assertEquals(1, term.size());
    }

    @Test
    public void testJSTermList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals("+", term.elementAt(0));
        assertEquals(2, term.size());
        assertEquals(1, term.elementAt(1).size());
        assertEquals("1", term.elementAt(1).elementAt(0));
        assertEquals(1, term.elementAt(2).size());
        assertEquals("2", term.elementAt(2).elementAt(0));
    }

    @Test
    public void testJSTermCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertTrue(result.isConstant());
        assertEquals("3", result.elementAt(0));
    }

    @Test
    public void testJSTermListCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(* (+ 1 2) 3)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertTrue(result.isConstant());
        assertEquals("7", result.elementAt(0));
    }

    @Test
    public void testJSTermSubstitution() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("y");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("x", term2));
        JSTerm result = term1.applySubstitution(substitution);
        assertEquals(term2, result);
    }

    @Test
    public void testJSTermMatches() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("y");
        JSTerm term3 = new JSTerm("x");
        JSTerm term4 = new JSTerm("z");
        JSSubstitution substitution = new JSSubstitution();
        JSTerm result1 = term1.matches(term3);
        JSTerm result2 = term1.matches(term2);
        JSTerm result3 = term1.matches(term4);
        assertNull(result1);
        assertTrue(result2.fail());
        assertFalse(result3.fail());
    }

    @Test
    public void testJSTermGround() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("5");
        JSTerm term3 = new JSTerm("(+ x 5)");
        assertTrue(term2.isGround());
        assertFalse(term1.isGround());
        assertTrue(term3.isGround());
    }

    @Test
    public void testJSTermToString() {
        JSTerm term = new JSTerm("(* (+ 1 2) 3)");
        assertEquals("(* (+ 1 2) 3)", term.toStr().toString());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term = new JSTerm("(+ x y)");
        JSTerm result = term.standardizerTerm();
        assertEquals("(+ ?x ?y)", result.toStr().toString());
    }
}