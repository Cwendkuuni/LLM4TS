package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {
    @Test
    public void testEmptyConstructor() {
        JSTerm jsterm = new JSTerm();
        assertFalse(jsterm.isEval());
        assertTrue(jsterm.isEmpty());
    }

    @Test
    public void testVariableConstructor() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertTrue(jsterm.isVariable());
        assertEquals("x", jsterm.elementAt(0));
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isEval());
        assertFalse(jsterm.isEmpty());
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testConstantConstructor() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("5")));
        assertTrue(jsterm.isConstant());
        assertEquals("5", jsterm.elementAt(0));
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isEval());
        assertFalse(jsterm.isEmpty());
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testFunctionConstructor() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertTrue(jsterm.isFunction());
        assertEquals("+", jsterm.elementAt(0));
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isEval());
        assertFalse(jsterm.isEmpty());
        assertEquals(3, jsterm.size());
        assertEquals(1, jsterm.elementAt(1).size());
        assertEquals(2, jsterm.elementAt(2).size());
    }

    @Test(expected = JSParserError.class)
    public void testParseError() {
        new JSTerm(new StreamTokenizer(new StringReader("nope")));
    }

    @Test
    public void testParseList() {
        JSTerm jsterm = new JSTerm();
        jsterm.parseList(new StreamTokenizer(new StringReader("(a b c d)")));
        assertTrue(jsterm.isFunction());
        assertEquals(".", jsterm.elementAt(0));
        assertEquals("list", jsterm.elementAt(1).elementAt(0));
        JSTerm list = (JSTerm) jsterm.elementAt(1).elementAt(1);
        assertEquals("a", list.elementAt(0).elementAt(0));
        assertEquals("b", list.elementAt(1).elementAt(0));
        assertEquals("c", list.elementAt(2).elementAt(0));
        assertEquals("d", list.elementAt(3).elementAt(0));
        assertEquals(4, list.size());
    }

    @Test
    public void testPrintList() {
        JSTerm jsterm = new JSTerm();
        jsterm.parseList(new StreamTokenizer(new StringReader("(a b c d)")));
        assertEquals("a b c d", jsterm.toStr().toString());
    }

    @Test
    public void testClone() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm jsterm2 = jsterm1.cloneT();
        assertEquals(jsterm1, jsterm2);
        assertTrue(jsterm1.isFunction());
        assertFalse(jsterm1.isEval());

        jsterm1.makeEval(true);
        jsterm2 = jsterm1.cloneT();
        assertFalse(jsterm1.isFunction());
        assertTrue(jsterm2.isFunction());
        assertFalse(jsterm1.equals(jsterm2));
    }

    @Test
    public void testApplySubstitution() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(x y)")));
        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(a b)")));
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairVarTerm("x", "a"));
        alpha.addElement(new JSPairVarTerm("y", "b"));

        JSTerm result = jsterm1.applySubstitutionT(alpha);
        assertEquals("(a b)", result.toStr().toString());
    }

    @Test
    public void testMatches() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(x y)")));
        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(z w)")));
        JSSubstitution beta = new JSTerm(new StreamTokenizer(new StringReader("(x y)"))).matches(jsterm1);
        assertFalse(beta.isEmpty());
        assertEquals(2, beta.size());
        assertEquals("x", beta.elementAt(0).first.toString());
        assertEquals("z", beta.elementAt(0).second.toString());
        assertEquals("y", beta.elementAt(1).first.toString());
        assertEquals("w", beta.elementAt(1).second.toString());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(a . b)")));
        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("(a c)")));
        assertEquals(false, jsterm3.matches(jsterm4).isEmpty());
    }

    @Test
    public void testEquals() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertTrue(jsterm1.equals(jsterm2));
        assertTrue(jsterm1.equals(jsterm2));
        assertFalse(jsterm1.equals("hello"));

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(x y)")));
        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("(y x)")));
        assertFalse(jsterm3.equals(jsterm4));
    }

    @Test
    public void testIsVariable() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertTrue(jsterm.isVariable());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("5")));
        assertFalse(jsterm2.isVariable());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertFalse(jsterm3.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("5")));
        assertTrue(jsterm.isConstant());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm2.isConstant());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertFalse(jsterm3.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertFalse(jsterm.isFunction());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("+(1 2)")));
        assertTrue(jsterm2.isFunction());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm3.isFunction());

        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm4.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertFalse(jsterm.isEval());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(eval (+ 1 2))")));
        assertTrue(jsterm2.isEval());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm3.isEval());

        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm4.isEval());
    }

    @Test
    public void testIsGround() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("5")));
        assertTrue(jsterm1.isGround());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertFalse(jsterm2.isGround());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        assertFalse(jsterm3.isGround());

        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        jsterm4.makeConstant();
        assertTrue(jsterm4.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm standardized = jsterm.standardizerTerm();
        assertEquals("(+ x1 2)", standardized.toStr().toString());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 5 2)")));
        JSTerm standardized2 = jsterm2.standardizerTerm();
        assertEquals("(+ 5 2)", standardized2.toStr().toString());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2 x)")));
        JSTerm standardized3 = jsterm3.standardizerTerm();
        assertEquals("(+ x1 x2)", standardized3.toStr().toString());
    }

    @Test
    public void testCall() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm result = jsterm.call();
        assertEquals("3", result.toStr().toString());

        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(* 2 (+ 1 2))")));
        JSTerm result2 = jsterm2.call();
        assertEquals("7", result2.toStr().toString());

        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(not (= x 5))")));
        JSTerm result3 = jsterm3.call();
        assertEquals("(not (= x 5))", result3.toStr().toString());

        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("(not (= x 5))")));
        jsterm4.makeConstant();
        JSTerm result4 = jsterm4.call();
        assertEquals("false", result4.toStr().toString());
    }
}