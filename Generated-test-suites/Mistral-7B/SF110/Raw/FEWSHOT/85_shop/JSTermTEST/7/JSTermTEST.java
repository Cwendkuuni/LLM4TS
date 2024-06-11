package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testParseTerm() {
        // Test parsing a constant term
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("1"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(1, term.size());
        assertEquals("1", term.elementAt(0).toString());
        assertTrue(term.isConstant());

        // Test parsing a variable term
        tokenizer = new StreamTokenizer(new StringReader("x"));
        term = new JSTerm(tokenizer);
        assertEquals(1, term.size());
        assertEquals("x", term.elementAt(0).toString());
        assertTrue(term.isVariable());

        // Test parsing a function term
        tokenizer = new StreamTokenizer(new StringReader("(+ x 2)"));
        term = new JSTerm(tokenizer);
        assertEquals(3, term.size());
        assertEquals("+", term.elementAt(0).toString());
        assertFalse(term.isConstant());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
        assertEquals("x", term.elementAt(1).toString());
        assertEquals("2", term.elementAt(2).toString());

        // Test parsing a list term
        tokenizer = new StreamTokenizer(new StringReader("(list x y z)"));
        term = new JSTerm(tokenizer);
        assertEquals(4, term.size());
        assertEquals("list", term.elementAt(0).toString());
        assertEquals("x", term.elementAt(1).toString());
        assertEquals("y", term.elementAt(2).toString());
        assertEquals("z", term.elementAt(3).toString());
        assertFalse(term.isConstant());
        assertFalse(term.isVariable());
        assertTrue(term.isFunction());

        // Test parsing a term with control parameters
        tokenizer = new StreamTokenizer(new StringReader("(+ x 2 3 4)"));
        term = new JSTerm(tokenizer);
        assertEquals(5, term.size());
        assertEquals("+", term.elementAt(0).toString());
        assertFalse(term.isConstant());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
        assertEquals("x", term.elementAt(1).toString());
        assertEquals("3", term.elementAt(2).toString());
        assertEquals("4", term.elementAt(3).toString());
        assertTrue(term.isEval());
    }

    @Test
    public void testApplySubstitution() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y)"));
        JSTerm term = new JSTerm(tokenizer);
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("z")));

        JSTerm newTerm = term.applySubstitution(substitution);

        assertEquals("(+ z y)", newTerm.toStr().toString());
        assertTrue(newTerm.isFunction());
    }

    @Test
    public void testMatches() {
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader("(+ x y)"));
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(+ z w)"));
        JSTerm term1 = new JSTerm(tokenizer1);
        JSTerm term2 = new JSTerm(tokenizer2);

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term2));

        JSSubstitution result = term1.matches(term2, substitution);

        assertTrue(result.size() > 0);
    }

    @Test
    public void testToString() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x (* y z))"));
        JSTerm term = new JSTerm(tokenizer);

        assertEquals("(+ x (* y z))", term.toStr().toString());
        assertTrue(term.isFunction());
    }

    @Test
    public void testStandardizerTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x (* y z))"));
        JSTerm term = new JSTerm(tokenizer);

        JSTerm newTerm = term.standardizerTerm();

        assertEquals("(+ x1 (* y1 z1))", newTerm.toStr().toString());
        assertTrue(newTerm.isFunction());
    }

    @Test
    public void testCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x (* y (sin x)))"));
        JSTerm term = new JSTerm(tokenizer);

        JSTerm newTerm = term.call();

        assertEquals("(+ x (sin x))", newTerm.toStr().toString());
        assertTrue(newTerm.isEval());
    }
}