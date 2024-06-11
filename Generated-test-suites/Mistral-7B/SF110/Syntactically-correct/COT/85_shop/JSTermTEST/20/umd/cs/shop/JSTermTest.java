package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testConstructor() {
        JSTerm term = new JSTerm();
        assertFalse(term.isEval());

        JSTerm term1 = new JSTerm(new java.io.StringReader("(term1)"));
        assertTrue(term1.isFunction());

        JSTerm term2 = new JSTerm(new java.io.StringReader("(call term1)"));
        assertTrue(term2.isEval());
    }

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        assertTrue(term.isVariable());

        term.makeFunction();
        assertFalse(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm();
        term.addElement("3.14");
        term.makeConstant();
        assertTrue(term.isConstant());

        term.makeVariable();
        assertFalse(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm();
        term.addElement("+");
        term.makeFunction();
        assertTrue(term.isFunction());

        term.makeConstant();
        assertFalse(term.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = new JSTerm();
        assertFalse(term.isEval());

        term.makeEval(true);
        assertTrue(term.isEval());

        term.makeEval(false);
        assertFalse(term.isEval());
    }

    @Test(expected = JSParserError.class)
    public void testParseInvalidTerm() throws IOException {
        JSTerm term = new JSTerm(new java.io.StringReader("(term x 3)"));
    }

    @Test
    public void testParseTermWithVariable() throws IOException {
        JSTerm term = new JSTerm(new java.io.StringReader("(term x)"));
        JSTerm expected = new JSTerm();
        expected.addElement("x");
        expected.makeVariable();
        assertEquals(expected, term);
    }

    @Test
    public void testParseTermWithConstant() throws IOException {
        JSTerm term = new JSTerm(new java.io.StringReader("(term 3.14)"));
        JSTerm expected = new JSTerm();
        expected.addElement("3.14");
        expected.makeConstant();
        assertEquals(expected, term);
    }

    @Test
    public void testParseTermWithList() throws IOException {
        JSTerm term = new JSTerm(new java.io.StringReader("(term (+ x 3) 4)"));
        JSTerm expected = new JSTerm();
        expected.addElement("+");
        expected.addElement("x");
        expected.addElement("3");
        expected.addElement("4");
        assertEquals(expected, term);
    }

    @Test(expected = JSParserError.class)
    public void testParseInvalidOperator() throws IOException {
        JSTerm term = new JSTerm(new java.io.StringReader("(term * x 3)"));
    }

    @Test
    public void testClone() {
        JSTerm term = new JSTerm();
        term.addElement("+");
        term.addElement("x");
        term.addElement("3");
        JSTerm clonedTerm = term.cloneT();
        JSTerm expected = new JSTerm();
        expected.addElement("+");
        expected.addElement("x");
        expected.addElement("3");
        assertEquals(expected, clonedTerm);
    }

    @Test
    public void testApplySubstitution() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("x", "y"));
        JSTerm result = term.applySubstitutionT(substitution);
        JSTerm expected = new JSTerm();
        expected.addElement("y");
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSSubstitution substitution = term1.matches(term2);
        assertFalse(substitution.isEmpty());

        JSTerm term3 = new JSTerm();
        term3.addElement("1");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("x");
        term4.makeVariable();

        substitution = term1.matches(term4);
        assertTrue(substitution.isEmpty());

        JSTerm term5 = new JSTerm();
        term5.addElement("x");
        term5.makeVariable();

        substitution = term5.matches(term1);
        assertFalse(substitution.isEmpty());
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("y");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("3.14");
        term4.makeConstant();

        assertTrue(term1.equals(term1));
        assertTrue(term1.equals(term2));
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term4));
    }
}