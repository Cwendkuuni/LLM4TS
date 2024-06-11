package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testEmptyTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(0, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertTrue(term.isGround());
        assertEquals("", term.toStr().toString());
        assertTrue(term.equals(new JSTerm()));
    }

    @Test
    public void testVariableTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(1, term.size());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertFalse(term.isGround());
        assertEquals("x", term.elementAt(0).toString());
        assertEquals("x", term.toStr().toString());
        assertTrue(term.equals(new JSTerm("x")));
    }

    @Test
    public void testConstantTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("3"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(1, term.size());
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertTrue(term.isGround());
        assertEquals("3", term.elementAt(0).toString());
        assertEquals("3", term.toStr().toString());
        assertTrue(term.equals(new JSTerm("3")));
    }

    @Test
    public void testFunctionTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call x 3)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(3, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertTrue(term.isEval());
        assertEquals("call", term.elementAt(0).toString());
        assertFalse(term.elementAt(1).isVariable());
        assertTrue(term.elementAt(1).isConstant());
        assertFalse(term.elementAt(1).isFunction());
        assertFalse(term.elementAt(1).isEval());
        assertEquals("x", term.elementAt(2).toString());
        assertFalse(term.elementAt(2).isVariable());
        assertFalse(term.elementAt(2).isConstant());
        assertFalse(term.elementAt(2).isFunction());
        assertFalse(term.elementAt(2).isEval());
        assertEquals("3", term.elementAt(3).toString());
        assertFalse(term.elementAt(3).isVariable());
        assertTrue(term.elementAt(3).isConstant());
        assertFalse(term.elementAt(3).isFunction());
        assertFalse(term.elementAt(3).isEval());
        assertTrue(term.isGround());
        assertEquals("(call x 3)", term.toStr().toString());
        assertFalse(term.equals(new JSTerm("(call y 3)")));
        assertFalse(term.equals(new JSTerm("(call x 4)")));
    }

    @Test
    public void testListTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list 1 2 3)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(3, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());
        assertEquals("list", term.elementAt(0).toString());
        assertFalse(term.elementAt(1).isVariable());
        assertTrue(term.elementAt(1).isConstant());
        assertFalse(term.elementAt(1).isFunction());
        assertFalse(term.elementAt(1).isEval());
        assertEquals("1", term.elementAt(2).toString());
        assertFalse(term.elementAt(2).isVariable());
        assertTrue(term.elementAt(2).isConstant());
        assertFalse(term.elementAt(2).isFunction());
        assertFalse(term.elementAt(2).isEval());
        JSTerm list = new JSTerm();
        list.addElement(term.elementAt(1).cloneT());
        list.addElement(term.elementAt(2).cloneT());
        list.makeFunction();
        term = new JSTerm(tokenizer);
        assertEquals(list, term);
        term = new JSTerm("(list 1 2 3)");
        assertEquals(list, term);
        term = new JSTerm("(list 1 1 3)");
        assertNotEquals(term, list);
    }

    @Test
    public void testApplySubstitution() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("y");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term2));
        term1.applySubstitution(substitution);
        assertEquals(term2, term1);
        term1 = new JSTerm("(call x 3)");
        term2 = new JSTerm("(call y 3)");
        substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term2));
        term1.applySubstitution(substitution);
        assertEquals(term2, term1);
        term1 = new JSTerm("(call (list x 1) 3)");
        term2 = new JSTerm("(call (list y 1) 3)");
        substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term2.elementAt(1).cloneT()));
        term1.applySubstitution(substitution);
        assertEquals(term2, term1);
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("x");
        JSTerm term3 = new JSTerm("y");
        JSTerm term4 = new JSTerm("3");
        JSTerm term5 = new JSTerm("(call x 3)");
        JSTerm term6 = new JSTerm("(call y 3)");
        JSSubstitution substitution = new JSSubstitution();
        JSSubstitution result = term1.matches(term2);
        assertEquals(substitution, result);
        result = term1.matches(term3);
        assertNull(result);
        result = term1.matches(term4);
        assertNull(result);
        result = term1.matches(term5);
        assertNull(result);
        substitution.addElement(new JSPairTerm("x", term5));
        result = term1.matches(term5, substitution);
        assertEquals(substitution, result);
        result = term5.matches(term1);
        assertNull(result);
        result = term5.matches(term6);
        assertNull(result);
        substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term2));
        result = term5.matches(term6, substitution);
        assertEquals(substitution, result);
    }

    @Test(expected = JSParserError.class)
    public void testParseError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x)"));
        new JSTerm(tokenizer);
    }
}