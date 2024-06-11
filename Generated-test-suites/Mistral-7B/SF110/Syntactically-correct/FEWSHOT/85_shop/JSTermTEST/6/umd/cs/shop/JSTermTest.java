package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testNewInstance() {
        JSTerm t = new JSTerm();
        assertNull(t.elementAt(0));
        assertEquals(0, t.size());
        assertFalse(t.isVariable());
        assertFalse(t.isConstant());
        assertFalse(t.isFunction());
        assertFalse(t.isEval());
    }

    @Test
    public void testNewInstanceWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm t = new JSTerm(tokenizer);
        assertEquals(4, t.size());
        assertEquals("+", t.elementAt(0));
        assertEquals("3", t.elementAt(1).elementAt(0));
        assertEquals("4", t.elementAt(2).elementAt(0));
        assertEquals("nil", t.elementAt(3).elementAt(0));
        assertTrue(t.isFunction());
        assertFalse(t.isVariable());
        assertFalse(t.isConstant());
        assertFalse(t.isEval());
    }

    @Test(expected = JSParserError.class)
    public void testInvalidInstanceWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)XXX"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testClone() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm t = new JSTerm(tokenizer);
        JSTerm cloned = t.cloneT();
        assertEquals(cloned, t);
    }

    @Test
    public void testCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm t = new JSTerm(tokenizer);
        JSTerm result = t.call();
        assertEquals("7", result.elementAt(0).elementAt(0));
        assertEquals("nil", result.elementAt(1).elementAt(0));
        assertEquals(0, result.size() - 1);
        assertTrue(result.isConstant());
    }

    @Test
    public void testApplySubstitution() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("y");
        t2.makeVariable();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm(t1, t2));

        JSTerm term = new JSTerm();
        term.addElement("(+ x y)");
        term.makeFunction();

        JSTerm result = term.applySubstitutionT(substitution);
        assertEquals("(+ y y)", result.toStr().toString());
    }

    @Test
    public void testMatches() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("x");
        t2.makeVariable();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm(t1, t2));

        JSTerm term = new JSTerm();
        term.addElement("(+ x x)");
        term.makeFunction();

        JSTerm result = term.matches(new JSTerm(), substitution).getFirst();
        assertSame(term, result);

        result = term.matches(new JSTerm(), substitution).getSecond();
        assertEquals(substitution, result);

        term = new JSTerm();
        term.addElement("(+ y y)");
        term.makeFunction();

        result = term.matches(new JSTerm(), substitution);
        assertNull(result);
    }

    @Test
    public void testEquals() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("x");
        t2.makeVariable();

        JSTerm t3 = new JSTerm();
        t3.addElement("y");
        t3.makeVariable();

        JSTerm t4 = new JSTerm();
        t4.addElement("(+ x x)");
        t4.makeFunction();

        JSTerm t5 = new JSTerm();
        t5.addElement("(+ y y)");
        t5.makeFunction();

        assertEquals(t1, t1);
        assertEquals(t1, t1);
        assertEquals(t1, t1);

        assertEquals(t1, t2);
        assertEquals(t2, t1);

        assertFalse(t1.equals(t3));
        assertFalse(t3.equals(t1));

        assertFalse(t1.equals(t4));
        assertFalse(t4.equals(t1));

        assertFalse(t1.equals(t5));
        assertFalse(t5.equals(t1));
    }

    @Test
    public void testIsVariable() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("3");
        t2.makeConstant();

        assertFalse(t2.isVariable());
        assertTrue(t1.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("3");
        t2.makeConstant();

        assertFalse(t1.isConstant());
        assertTrue(t2.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("(+ x x)");
        t2.makeFunction();

        assertFalse(t1.isFunction());
        assertTrue(t2.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm t1 = new JSTerm();
        t1.addElement("x");
        t1.makeVariable();

        JSTerm t2 = new JSTerm();
        t2.addElement("(+ x x)");
        t2.makeFunction();

        JSTerm t3 = new JSTerm();
        t3.addElement("(call (+ x x))");
        t3.makeEval(true);

        assertFalse(t1.isEval());
        assertFalse(t2.isEval());
        assertTrue(t3.isEval());
    }

    @Test
    public void testPrint() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm term = new JSTerm(tokenizer);
        term.print();
        assertEquals("(+ 3 4)", term.toStr().toString());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        assertEquals("x", term.toStr().toString());

        term.addElement("(+ x x)");
        term.makeFunction();

        assertEquals("(+ x x)", term.toStr().toString());
    }

    @Test
    public void testStandardizerTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 (x))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm standardized = term.standardizerTerm();
        assertEquals("(+ 3 x)", standardized.toStr().toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        assertFalse(term.isGround());

        term.addElement("3");
        term.makeConstant();

        assertTrue(term.isGround());
    }
}