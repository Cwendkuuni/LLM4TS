package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermEmpty() {
        JSTerm term = new JSTerm();
        assertEquals(0, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertTrue(term.equals(term));
        assertTrue(term.isGround());
        assertEquals("", term.toStr().toString());
    }

    @Test
    public void testJSTermVariable() {
        JSTerm term = new JSTerm("x");
        assertEquals(1, term.size());
        assertFalse(term.isFunction());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("y")));
        assertFalse(term.isGround());
        assertEquals("x", term.toStr().toString());
    }

    @Test
    public void testJSTermConstant() {
        JSTerm term = new JSTerm("2");
        assertEquals(1, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isEval());
        assertTrue(term.equals(term));
        assertTrue(term.isGround());
        assertEquals("2", term.toStr().toString());
    }

    @Test
    public void testJSTermFunction() {
        JSTerm term = new JSTerm("f", new JSTerm("x"));
        assertEquals(2, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"))));
        assertFalse(term.isGround());
        assertEquals("f (x)", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionEval() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), true);
        assertEquals(2, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), true)));
        assertFalse(term.isGround());
        assertEquals("f (x)", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCall() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("y"));
        assertEquals(3, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("z"))));
        assertFalse(term.isGround());
        assertEquals("f (x) (y)", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCallEval() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("y"), true);
        assertEquals(3, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("z"), true)));
        assertFalse(term.isGround());
        assertEquals("f (x) (y)", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCallConstant() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("2"));
        assertEquals(3, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("3"))));
        assertFalse(term.isGround());
        assertEquals("f (x) 2", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCallConstantEval() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("2"), true);
        assertEquals(3, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("3"), true)));
        assertFalse(term.isGround());
        assertEquals("f (x) 2", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCallVariable() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("y"),
                new JSTerm("z"));
        assertEquals(4, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("w"),
                new JSTerm("z"))));
        assertFalse(term.isGround());
        assertEquals("f (x) (y) (z)", term.toStr().toString());
    }

    @Test
    public void testJSTermFunctionCallVariableEval() {
        JSTerm term = new JSTerm("f", new JSTerm("x"), new JSTerm("y"), new JSTerm("z"), true);
        assertEquals(4, term.size());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("g", new JSTerm("x"), new JSTerm("w"),
                new JSTerm("z"), true)));
        assertFalse(term.isGround());
        assertEquals("f (x) (y) (z)", term.toStr().toString());
    }

    @Test
    public void testJSTermList() {
        JSTerm term = new JSTerm("(list x y z)", new JSTerm("x"), new JSTerm("y"),
                new JSTerm("z"));
        assertEquals(4, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("(list w x y z)", new JSTerm("w"), new JSTerm("x"), new JSTerm("y"))));
        assertFalse(term.isGround());
        assertEquals("(list x y z)", term.toStr().toString());
    }

    @Test
    public void testJSTermListEval() {
        JSTerm term = new JSTerm("(list x y z)", new JSTerm("x"), new JSTerm("y"), new JSTerm("z"), true);
        assertEquals(4, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("(list w x y z)", new JSTerm("w"), new JSTerm("x"), new JSTerm("y"), true)));
        assertFalse(term.isGround());
        assertEquals("(list x y z)", term.toStr().toString());
    }

    @Test
    public void testJSTermListEmpty() {
        JSTerm term = new JSTerm("(list)", new JSTerm());
        assertEquals(2, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertTrue(term.isGround());
        assertEquals("(list)", term.toStr().toString());
    }

    @Test
    public void testJSTermListEmptyEval() {
        JSTerm term = new JSTerm("(list)", new JSTerm(), true);
        assertEquals(2, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertTrue(term.isEval());
        assertTrue(term.isGround());
        assertEquals("(list)", term.toStr().toString());
    }

    @Test
    public void testJSTermListCall() {
        JSTerm term = new JSTerm("(list f x y)", new JSTerm("f"), new JSTerm("x"), new JSTerm("y"));
        assertEquals(4, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
        assertFalse(term.equals(new JSTerm("(list g w v)", new JSTerm("g"), new JSTerm("w"), new JSTerm("v"))));
        assertFalse(term.isGround());
        assertEquals("(list f x y)", term.toStr().toString());
    }

    @Test
    public void testJSTermListCallEval() {
        JSTerm term = new JSTerm("(list f x y)", new JSTerm("f"), new JSTerm("x"), new JSTerm("y"), true);
        assertEquals(4, term.size());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertTrue(term.isEval());
        assertFalse(term.equals(new JSTerm("(list g w v)", new JSTerm("g"), new JSTerm("w"), new JSTerm("v"), true)));
        assertFalse(term.isGround());
        assertEquals("(list f x y)", term.toStr().toString());
    }

    @Test
    public void testJSTermGround() {
        JSTerm term = new JSTerm("(list f x y)", new JSTerm("f"), new JSTerm("x"), new JSTerm("y"));
        assertFalse(term.isGround());
        term = new JSTerm("2");
        assertTrue(term.isGround());
        term = new JSTerm("x");
        assertFalse(term.isGround());
        term = new JSTerm("f(x)");
        assertFalse(term.isGround());
        term = new JSTerm("f(x) 2");
        assertFalse(term.isGround());
        term = new JSTerm("(list f x y)", new JSTerm("f"), new JSTerm("x"), new JSTerm("y"), true);
        assertTrue(term.isGround());
        term = new JSTerm("(list f x y)", new JSTerm("f"), new JSTerm("x"), new JSTerm("y"), false);
        assertFalse(term.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term = new JSTerm("f(x)");
        JSTerm standardTerm = term.standardizerTerm();
        assertEquals(1, standardTerm.size());
        assertEquals("f1", standardTerm.elementAt(0).toString());
        term = new JSTerm("f(x1)");
        standardTerm = term.standardizerTerm();
        assertEquals(1, standardTerm.size());
        assertEquals("f1", standardTerm.elementAt(0).toString());
        term = new JSTerm("f(x1)");
        standardTerm = term.standardizerTerm();
        assertEquals(1, standardTerm.size());
        assertEquals("f1", standardTerm.elementAt(0).toString());
        term = new JSTerm("(list f(x1) x2 y2)");
        standardTerm = term.standardizerTerm();
        assertEquals(4, standardTerm.size());
        assertEquals("(list f1 x1 x2 y2)", standardTerm.toStr().toString());
    }
}