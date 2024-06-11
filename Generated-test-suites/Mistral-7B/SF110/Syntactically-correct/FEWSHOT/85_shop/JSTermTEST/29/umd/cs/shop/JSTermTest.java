package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermEmpty() {
        JSTerm jsterm = new JSTerm();
        assertEquals(0, jsterm.size());
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isEval());
        assertNull(jsterm.elementAt(0));
    }

    @Test
    public void testJSTermVariable() {
        JSTerm jsterm = new JSTerm("x");
        assertEquals(1, jsterm.size());
        assertTrue(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("x", jsterm.elementAt(0));
    }

    @Test
    public void testJSTermConstant() {
        JSTerm jsterm = new JSTerm("3");
        assertEquals(1, jsterm.size());
        assertFalse(jsterm.isVariable());
        assertTrue(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("3", jsterm.elementAt(0));
    }

    @Test
    public void testJSTermFunction() {
        JSTerm jsterm = new JSTerm("+");
        assertEquals(1, jsterm.size());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("+", jsterm.elementAt(0));
    }

    @Test
    public void testJSTermEvaluableFunction() {
        JSTerm jsterm = new JSTerm("call");
        assertEquals(1, jsterm.size());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertTrue(jsterm.isFunction());
        assertTrue(jsterm.isEval());
        assertEquals("call", jsterm.elementAt(0));
    }

    @Test
    public void testJSTermList() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement(new JSTerm("x"));
        jsterm.addElement(new JSTerm("3"));
        assertEquals(2, jsterm.size());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("x", jsterm.elementAt(0));
        assertEquals("3", jsterm.elementAt(1));
    }

    @Test(expected = JSParserError.class)
    public void testJSTermInvalidTerm() throws JSParserError {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        new JSTerm(tokenizer);
    }

    @Test
    public void testJSTermParseList() {
        JSTerm jsterm = new JSTerm();
        jsterm.parseList(new StreamTokenizer(new StringReader("(1 2 3)")));
        assertEquals(3, jsterm.size());
        assertEquals("nil", jsterm.elementAt(2).elementAt(0));
        assertEquals(3, jsterm.elementAt(1).size());
        assertEquals("1", jsterm.elementAt(1).elementAt(0));
        assertEquals("2", jsterm.elementAt(1).elementAt(1));
        assertEquals("3", jsterm.elementAt(1).elementAt(2));
    }

    @Test
    public void testJSTermPrint() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement(new JSTerm("+"));
        jsterm.addElement(new JSTerm("x"));
        jsterm.addElement(new JSTerm("3"));
        jsterm.print();
        assertEquals("(+ x 3)", JSTerm.getOutput());
        JSTerm.clearOutput();
    }

    @Test
    public void testJSTermCloneT() {
        JSTerm jsterm1 = new JSTerm("(+ x 3)");
        JSTerm jsterm2 = jsterm1.cloneT();
        assertEquals(jsterm1, jsterm2);
    }

    @Test
    public void testJSTermApplySubstitutionT() {
        JSTerm jsterm1 = new JSTerm("(x y z)");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("3")));
        JSTerm jsterm2 = jsterm1.applySubstitutionT(substitution);
        assertEquals("(3 y z)", jsterm2.toStr());
    }

    @Test
    public void testJSTermMatches() {
        JSTerm jsterm1 = new JSTerm("(x y z)");
        JSTerm jsterm2 = new JSTerm("(u v w)");
        JSSubstitution substitution = new JSSubstitution();
        substitution = jsterm1.matches(jsterm2);
        assertTrue(substitution.isEmpty());

        substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("u")));
        substitution.addElement(new JSPairTerm("y", new JSTerm("v")));
        substitution.addElement(new JSPairTerm("z", new JSTerm("w")));
        JSTerm jsterm3 = jsterm1.applySubstitutionT(substitution);
        assertEquals("(u v w)", jsterm3.toStr());

        JSTerm jsterm4 = new JSTerm("(+(x y) z)");
        JSTerm jsterm5 = new JSTerm("(+(u v) w)");
        substitution = new JSSubstitution();
        substitution = jsterm4.matches(jsterm5);
        assertTrue(substitution.isEmpty());

        substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("u")));
        substitution.addElement(new JSPairTerm("y", new JSTerm("v")));
        substitution.addElement(new JSPairTerm("z", new JSTerm("w")));
        substitution.addElement(new JSPairTerm("+", new JSTerm("+")));
        JSTerm jsterm6 = jsterm4.applySubstitutionT(substitution);
        assertEquals("((+ u v) w)", jsterm6.toStr());
    }

    @Test
    public void testJSTermEquals() {
        JSTerm jsterm1 = new JSTerm("(x y z)");
        JSTerm jsterm2 = new JSTerm("(u v w)");
        assertFalse(jsterm1.equals(jsterm2));

        JSTerm jsterm3 = new JSTerm("(x y z)");
        JSTerm jsterm4 = new JSTerm("(x y z)");
        assertTrue(jsterm3.equals(jsterm4));

        JSTerm jsterm5 = new JSTerm("(+(x y) z)");
        JSTerm jsterm6 = new JSTerm("(+(u v) w)");
        assertFalse(jsterm5.equals(jsterm6));

        JSTerm jsterm7 = new JSTerm("(+(x y) z)");
        JSTerm jsterm8 = new JSTerm("(+(x y) z)");
        assertTrue(jsterm7.equals(jsterm8));
    }

    @Test
    public void testJSTermIsVariable() {
        JSTerm jsterm = new JSTerm("x");
        assertTrue(jsterm.isVariable());

        JSTerm jsterm2 = new JSTerm("3");
        assertFalse(jsterm2.isVariable());
    }

    @Test
    public void testJSTermIsConstant() {
        JSTerm jsterm = new JSTerm("3");
        assertTrue(jsterm.isConstant());

        JSTerm jsterm2 = new JSTerm("x");
        assertFalse(jsterm2.isConstant());
    }

    @Test
    public void testJSTermIsFunction() {
        JSTerm jsterm = new JSTerm("+");
        assertTrue(jsterm.isFunction());

        JSTerm jsterm2 = new JSTerm("x");
        assertFalse(jsterm2.isFunction());
    }

    @Test
    public void testJSTermIsEval() {
        JSTerm jsterm = new JSTerm("call");
        assertTrue(jsterm.isEval());

        JSTerm jsterm2 = new JSTerm("x");
        assertFalse(jsterm2.isEval());
    }

    @Test
    public void testJSTermIsGround() {
        JSTerm jsterm = new JSTerm("3");
        assertTrue(jsterm.isGround());

        JSTerm jsterm2 = new JSTerm("x");
        assertFalse(jsterm2.isGround());

        JSTerm jsterm3 = new JSTerm("(+(x y) z)");
        assertFalse(jsterm3.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm jsterm = new JSTerm("(+(x y) z)");
        JSTerm jsterm2 = jsterm.standardizerTerm();
        assertEquals("(+(x x) (+(x y) z))", jsterm2.toStr());
    }

    @Test
    public void testJSTermCall() {
        JSTerm jsterm = new JSTerm("(+(x y) z)");
        JSTerm jsterm2 = jsterm.call();
        assertEquals("+(x y) z", jsterm2.toStr());
    }

}