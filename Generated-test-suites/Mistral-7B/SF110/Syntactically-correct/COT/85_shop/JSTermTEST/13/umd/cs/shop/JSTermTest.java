package umd.cs.shop; // replace with the actual package name if necessary

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class JSTermTest {

    @Test
    public void testConstructor() {
        JSTerm term = new JSTerm();
        assertNull(term.elementAt(0));
        assertEquals(0, term.size());
        assertFalse(term.isVariable);
        assertFalse(term.isConstant);
        assertFalse(term.isFunction);
        assertFalse(term.isEval);

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
        JSTerm termFromStream = new JSTerm(tokenizer);
        assertEquals("+", termFromStream.elementAt(0));
        assertEquals(2, termFromStream.size());
        assertTrue(termFromStream.isFunction);
        assertFalse(termFromStream.isVariable);
        assertFalse(termFromStream.isConstant);
        assertFalse(termFromStream.isEval);
        assertEquals("x", termFromStream.elementAt(1).elementAt(0));
        assertEquals("3", termFromStream.elementAt(2).elementAt(0));
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ (list x y) z)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("list", term.elementAt(1).elementAt(0));
        assertEquals(2, term.elementAt(1).size());
        assertEquals("x", term.elementAt(2).elementAt(0));
        assertEquals("y", term.elementAt(3).elementAt(0));
        assertEquals("z", term.elementAt(4).elementAt(0));
    }

    @Test
    public void testCloneT() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm clonedTerm = term.cloneT();
        assertEquals(term.toString(), clonedTerm.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplySubstitutionTWithNullAlpha() {
        JSTerm term = new JSTerm();
        term.applySubstitutionT(null);
    }

    @Test
    public void testApplySubstitutionT() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
        JSTerm term = new JSTerm(tokenizer);
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign(new JSPairVarTerm("x", new JSTerm("y")));
        JSTerm result = term.applySubstitutionT(alpha);
        assertEquals("y + 3", result.toString());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("y");
        JSTerm term3 = new JSTerm("x");
        JSTerm term4 = new JSTerm("z");
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign(new JSPairVarTerm("x", term3));

        JSSubstitution result1 = term1.matches(term2);
        assertTrue(result1.isEmpty());

        JSSubstitution result2 = term1.matches(term3);
        assertEquals(1, result2.size());
        JSPairVarTerm pair = (JSPairVarTerm) result2.elementAt(0);
        assertEquals("x", pair.getFirst());
        assertEquals(term3, pair.getSecond());

        JSSubstitution result3 = term1.matches(term4);
        assertTrue(result3.isEmpty());

        JSSubstitution result4 = term3.matches(term1);
        assertEquals(1, result4.size());
        JSPairVarTerm pair2 = (JSPairVarTerm) result4.elementAt(0);
        assertEquals("x", pair2.getFirst());
        assertEquals(term1, pair2.getSecond());

        JSSubstitution result5 = term1.matches(term1, alpha);
        assertEquals(1, result5.size());
        JSPairVarTerm pair3 = (JSPairVarTerm) result5.elementAt(0);
        assertEquals("x", pair3.getFirst());
        assertEquals(term3, pair3.getSecond());
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("y");
        JSTerm term3 = new JSTerm("x");

        assertTrue(term1.equals(term1));
        assertFalse(term1.equals(term2));
        assertTrue(term1.equals(term3));
    }

    @Test
    public void testIsVariable() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("3");
        JSTerm term3 = new JSTerm("(+ x 3)");

        assertTrue(term1.isVariable());
        assertFalse(term2.isVariable());
        assertTrue(term3.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("3");
        JSTerm term3 = new JSTerm("(+ x 3)");

        assertFalse(term1.isConstant());
        assertTrue(term2.isConstant());
        assertFalse(term3.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("(+ x 3)");

        assertFalse(term1.isFunction());
        assertTrue(term2.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term1 = new JSTerm("x");
        JSTerm term2 = new JSTerm("(+ x 3)");
        JSTerm term3 = new JSTerm("(call (+ x 3))");

        assertFalse(term1.isEval());
        assertFalse(term2.isEval());
        assertTrue(term3.isEval());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm("x");
        assertEquals("x", term.toString());

        term = new JSTerm("(+ x 3)");
        assertEquals("+(x 3)", term.toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term1 = new JSTerm("3");
        JSTerm term2 = new JSTerm("x");
        JSTerm term3 = new JSTerm("(+ x 3)");

        assertTrue(term1.isGround());
        assertFalse(term2.isGround());
        assertFalse(term3.isGround());
    }

    @Test(expected = NullPointerException.class)
    public void testStandardizerTermWithNullTerm() {
        JSTerm term = null;
        term.standardizerTerm();
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm("x");
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.toString());

        term = new JSTerm("(+ x 3)");
        standardizedTerm = term.standardizerTerm();
        assertEquals("+(x" + String.valueOf(JSJshopVars.VarCounter) + " 3)", standardizedTerm.toString());
    }

    @Test
    public void testCall() {
        JSTerm term = new JSTerm("(+ x 3)");
        JSTerm result = term.call();
        assertEquals("+(+ x 3)", result.toString());

        term = new JSTerm("(- x 3)");
        result = term.call();
        assertEquals("+(*) (- x 1) 3", result.toString());

        term = new JSTerm("(not x)");
        result = term.call();
        assertEquals("(not x)", result.toString());
    }
}