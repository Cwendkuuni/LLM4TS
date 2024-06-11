package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermEmpty() {
        JSTerm term = new JSTerm();
        assertTrue(term.isEmpty());
        assertEquals("", term.toStr().toString());
    }

    @Test
    public void testJSTermVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        assertFalse(term.isEmpty());
        assertEquals("x", term.toStr().toString());
    }

    @Test
    public void testJSTermConstant() {
        JSTerm term = new JSTerm();
        term.addElement("5");
        term.makeConstant();
        assertFalse(term.isEmpty());
        assertEquals("5", term.toStr().toString());
    }

    @Test
    public void testJSTermFunction() {
        JSTerm term = new JSTerm();
        term.addElement("+");
        term.makeFunction();
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        assertFalse(term.isEmpty());
        assertEquals("+(, ,)", term.toStr().toString());
    }

    @Test
    public void testJSTermEvaluableFunction() {
        JSTerm term = new JSTerm();
        term.makeEval(true);
        term.addElement("+");
        term.makeFunction();
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        assertFalse(term.isEmpty());
        assertEquals("call +", term.toStr().toString());
    }

    @Test
    public void testJSTermList() {
        JSTerm term = new JSTerm();
        term.addElement("list");
        term.makeFunction();
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        assertFalse(term.isEmpty());
        assertEquals("(list , ,)", term.toStr().toString());
    }

    @Test
    public void testJSTermApplySubstitution() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", "y"));

        JSTerm term3 = term1.applySubstitutionT(alpha);
        assertEquals("y", term3.elementAt(0).toString());
        assertEquals(term2.toStr().toString(), term3.toStr().toString());

        JSTerm term4 = new JSTerm();
        term4.addElement("(+ x y)");
        term4.makeFunction();

        JSTerm term5 = term4.applySubstitutionT(alpha);
        assertEquals("(+ y y)", term5.toStr().toString());
    }

    @Test
    public void testJSTermMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("(+ x y)");
        term3.makeFunction();

        JSTerm term4 = new JSTerm();
        term4.addElement("(+ y z)");
        term4.makeFunction();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", "y"));

        JSSubstitution matches1 = term1.matches(term2);
        assertEquals(0, matches1.size());

        JSSubstitution matches2 = term1.matches(term3);
        assertEquals(1, matches2.size());
        assertEquals(new JSPair("x", "y"), matches2.elementAt(0));

        JSSubstitution matches3 = term3.matches(term4);
        assertEquals(0, matches3.size());
    }

    @Test
    public void testJSTermEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("x");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("y");
        term4.makeVariable();

        JSTerm term5 = new JSTerm();
        term5.addElement("(+ x y)");
        term5.makeFunction();

        JSTerm term6 = new JSTerm();
        term6.addElement("(+ y z)");
        term6.makeFunction();

        assertTrue(term1.equals(term3));
        assertFalse(term1.equals(term2));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
    }

    @Test
    public void testJSTermSize() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("y");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement(term);
        term.addElement(term);
        assertEquals(6, term.size());
    }

    @Test
    public void testJSTermToString() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement("y");
        term.makeVariable();
        term.addElement("3");
        term.makeConstant();

        assertEquals("(+(x y) 3)", term.toStr().toString());
    }

    @Test
    public void testJSTermIsGround() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement("y");
        term.makeVariable();
        term.addElement("3");
        term.makeConstant();

        assertFalse(term.isGround());

        term.addElement("5");
        term.makeConstant();

        assertTrue(term.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement("y");
        term.makeVariable();
        term.addElement("3");
        term.makeConstant();

        JSTerm standardTerm = term.standardizerTerm();

        JSTerm expectedTerm = new JSTerm();
        expectedTerm.makeEval(true);
        expectedTerm.addElement("x");
        expectedTerm.makeVariable();
        expectedTerm.addElement("y");
        expectedTerm.makeVariable();
        expectedTerm.addElement("z");
        expectedTerm.makeVariable();
        expectedTerm.addElement("+");
        expectedTerm.makeFunction();
        expectedTerm.addElement("z");
        expectedTerm.addElement("3");
        expectedTerm.makeConstant();

        assertEquals(expectedTerm.toStr().toString(), standardTerm.toStr().toString());
    }

    @Test
    public void testJSTermCall() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement("y");
        term.makeVariable();
        term.addElement("3");
        term.makeConstant();

        JSTerm result = term.call();

        JSTerm expectedResult = new JSTerm();
        expectedResult.addElement("(+(x y) 3)");
        expectedResult.makeFunction();

        assertEquals(expectedResult.toStr().toString(), result.toStr().toString());
    }
}