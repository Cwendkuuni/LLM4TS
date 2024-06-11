package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm();
        assertFalse(term.isVariable());
        
        term = new JSTerm("x");
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm();
        assertFalse(term.isConstant());

        term = new JSTerm("3");
        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm();
        assertFalse(term.isFunction());

        term = new JSTerm("sin");
        assertTrue(term.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = new JSTerm();
        assertFalse(term.isEval());

        term = new JSTerm("(call sin 2.0)");
        assertTrue(term.isEval());
    }

    @Test
    public void testParseTermWithConstant() {
        JSTerm term = new JSTerm(new StringReader("3"));
        term.parseTerm();
        assertEquals("3", term.toStr().toString());
    }

    @Test
    public void testParseTermWithVariable() {
        JSTerm term = new JSTerm(new StringReader("x"));
        term.parseTerm();
        assertEquals("x", term.toStr().toString());
    }

    @Test
    public void testParseTermWithFunctionCall() {
        JSTerm term = new JSTerm(new StringReader("(call sin 2.0)"));
        term.parseTerm();
        assertEquals("(sin 2.0)", term.toStr().toString());
    }

    @Test
    public void testParseTermWithList() {
        JSTerm term = new JSTerm(new StringReader("(+ 1 2)"));
        term.parseTerm();
        assertEquals("(+ 1 2)", term.toStr().toString());
    }

    @Test
    public void testParseTermWithNestedList() {
        JSTerm term = new JSTerm(new StringReader("(* (+ 1 2) 3)"));
        term.parseTerm();
        assertEquals("(* (+ 1 2) 3)", term.toStr().toString());
    }

    @Test
    public void testCloneTerm() {
        JSTerm original = new JSTerm(new StringReader("(+ 1 2)"));
        original.parseTerm();
        JSTerm cloned = original.cloneT();
        assertEquals(original.toStr().toString(), cloned.toStr().toString());
    }

    @Test
    public void testApplySubstitutionTerm() {
        JSTerm term1 = new JSTerm(new StringReader("x"));
        term1.parseTerm();
        term1.makeVariable();

        JSTerm term2 = new JSTerm(new StringReader("y"));
        term2.parseTerm();
        term2.makeVariable();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addMapping(new JSPairVarTerm("x", "y"));

        JSTerm result = term1.applySubstitutionT(substitution);
        assertEquals("y", result.toStr().toString());
    }

    @Test
    public void testMatchesTerm() {
        JSTerm term1 = new JSTerm(new StringReader("x"));
        term1.parseTerm();
        term1.makeVariable();

        JSTerm term2 = new JSTerm(new StringReader("y"));
        term2.parseTerm();
        term2.makeVariable();

        JSTerm term3 = new JSTerm(new StringReader("z"));
        term3.parseTerm();
        term3.makeVariable();

        JSTerm term4 = new JSTerm(new StringReader("(+ x 1)"));
        term4.parseTerm();

        JSTerm term5 = new JSTerm(new StringReader("(+ y 1)"));
        term5.parseTerm();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addMapping(new JSPairVarTerm("x", "y"));

        JSSubstitution result = term1.matches(term5, substitution);
        assertTrue(result.size() > 0);
        assertEquals("y", result.get(0).getKey().toStr().toString());
        assertEquals("x", result.get(0).getValue().toStr().toString());

        result = term1.matches(term3, substitution);
        assertNull(result);
    }

    @Test
    public void testEqualsTerms() {
        JSTerm term1 = new JSTerm(new StringReader("x"));
        term1.parseTerm();
        term1.makeVariable();

        JSTerm term2 = new JSTerm(new StringReader("y"));
        term2.parseTerm();
        term2.makeVariable();

        JSTerm term3 = new JSTerm(new StringReader("x"));
        term3.parseTerm();
        term3.makeVariable();

        JSTerm term4 = new JSTerm(new StringReader("y"));
        term4.parseTerm();
        term4.makeVariable();

        JSTerm term5 = new JSTerm(new StringReader("(+ x 1)"));
        term5.parseTerm();

        JSTerm term6 = new JSTerm(new StringReader("(+ y 1)"));
        term6.parseTerm();

        JSTerm term7 = new JSTerm(new StringReader("(+ x 1)"));
        term7.parseTerm();

        assertTrue(term1.equals(term1));
        assertTrue(term1.equals(term3));
        assertFalse(term1.equals(term2));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
        assertFalse(term1.equals(term7));
        assertFalse(term1.equals(null));

        assertTrue(term2.equals(term2));
        assertFalse(term2.equals(term1));
        assertFalse(term2.equals(term3));
        assertFalse(term2.equals(term4));
        assertFalse(term2.equals(term5));
        assertFalse(term2.equals(term6));
        assertFalse(term2.equals(term7));
        assertFalse(term2.equals(null));

        assertTrue(term3.equals(term3));
        assertTrue(term3.equals(term1));
        assertFalse(term3.equals(term2));
        assertFalse(term3.equals(term4));
        assertFalse(term3.equals(term5));
        assertFalse(term3.equals(term6));
        assertFalse(term3.equals(term7));
        assertFalse(term3.equals(null));

        assertTrue(term4.equals(term4));
        assertFalse(term4.equals(term1));
        assertFalse(term4.equals(term2));
        assertFalse(term4.equals(term3));
        assertFalse(term4.equals(term5));
        assertFalse(term4.equals(term6));
        assertFalse(term4.equals(term7));
        assertFalse(term4.equals(null));

        assertTrue(term5.equals(term5));
        assertFalse(term5.equals(term1));
        assertFalse(term5.equals(term2));
        assertFalse(term5.equals(term3));
        assertFalse(term5.equals(term4));
        assertFalse(term5.equals(term6));
        assertFalse(term5.equals(term7));
        assertFalse(term5.equals(null));

        assertTrue(term6.equals(term6));
        assertFalse(term6.equals(term1));
        assertFalse(term6.equals(term2));
        assertFalse(term6.equals(term3));
        assertFalse(term6.equals(term4));
        assertFalse(term6.equals(term5));
        assertFalse(term6.equals(term7));
        assertFalse(term6.equals(null));

        assertTrue(term7.equals(term7));
        assertFalse(term7.equals(term1));
        assertFalse(term7.equals(term2));
        assertFalse(term7.equals(term3));
        assertFalse(term7.equals(term4));
        assertFalse(term7.equals(term5));
        assertFalse(term7.equals(term6));
        assertFalse(term7.equals(null));
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        assertEquals("", term.toStr().toString());

        term = new JSTerm("3");
        assertEquals("3", term.toStr().toString());

        term = new JSTerm("x");
        assertEquals("x", term.toStr().toString());

        term = new JSTerm("(+ 1 2)");
        assertEquals("(+ 1 2)", term.toStr().toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm();
        assertFalse(term.isGround());

        term = new JSTerm("3");
        assertTrue(term.isGround());

        term = new JSTerm("x");
        assertFalse(term.isGround());

        term = new JSTerm("(+ x 1)");
        assertFalse(term.isGround());

        term = new JSTerm("(+ 1 2)");
        assertTrue(term.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        assertEquals("", term.standardizerTerm().toStr().toString());

        term = new JSTerm("3");
        assertEquals("3", term.standardizerTerm().toStr().toString());

        term = new JSTerm("x");
        assertEquals("X" + JSJshopVars.VarCounter, term.standardizerTerm().toStr().toString());

        term = new JSTerm("(+ 1 2)");
        assertEquals("(+ 1 2)", term.standardizerTerm().toStr().toString());

        term = new JSTerm("(sin x)");
        assertEquals("(sin X" + JSJshopVars.VarCounter + ")", term.standardizerTerm().toStr().toString());

        term = new JSTerm("(+ x 1)");
        assertEquals("(+ X" + JSJshopVars.VarCounter + " 1)", term.standardizerTerm().toStr().toString());
    }

    @Test
    public void testCallTerm() {
        JSTerm term = new JSTerm();
        assertEquals("", term.call().toStr().toString());

        term = new JSTerm("3");
        assertEquals("3", term.call().toStr().toString());

        term = new JSTerm("x");
        assertEquals("x", term.call().toStr().toString());

        term = new JSTerm("(+ 1 2)");
        assertEquals("(+ 1 2)", term.call().toStr().toString());

        term = new JSTerm("(sin x)");
        assertEquals("sin", term.call().toStr().toString());
        term = new JSTerm("(sin (sin x))");
        assertEquals("sin", term.call().toStr().toString());

        term = new JSTerm("(+ x 1)");
        assertEquals("+", term.call().toStr().toString());
        term = new JSTerm("(+(+ x 1) 3)");
        assertEquals("+", term.call().toStr().toString());
    }
}