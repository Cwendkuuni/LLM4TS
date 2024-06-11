package umd.cs.shop.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

    @Test
    public void testEmpty() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm clone = pf.clonePF();
        assertEquals(pf, clone);
    }

    @Test
    public void testParseSimple() {
        String text = "x > 5";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm clone = pf.clonePF();

        assertEquals(pf, clone);
        assertEquals(text, pf.toStr().toString());

        assertEquals("x > 5", pf.elementAt(0).toString());
        assertEquals(1, pf.size());
        assertNull(pf.elementAt(1));
    }

    @Test
    public void testParseComplex() {
        String text = "(not (x > 5) and (y = 7))";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm clone = pf.clonePF();

        assertEquals(pf, clone);
        assertEquals(text, pf.toStr().toString());

        assertEquals("not", pf.elementAt(0).toString());
        assertEquals(2, pf.size());

        assertEquals("x > 5", pf.elementAt(1).toStr().toString());
        assertEquals("y = 7", pf.elementAt(2).toStr().toString());
    }

    @Test
    public void testParseComplexWithCall() {
        String text = "(call myFunction (x > 5) and (y = 7))";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm clone = pf.clonePF();

        assertEquals(pf, clone);
        assertEquals(text, pf.toStr().toString());

        assertEquals("call", pf.elementAt(0).toString());
        assertEquals(3, pf.size());

        assertEquals("x > 5", pf.elementAt(1).toStr().toString());
        assertEquals("y = 7", pf.elementAt(2).toStr().toString());
    }

    @Test
    public void testParseComplexWithNot() {
        String text = "(not ((x > 5) and (y = 7)))";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm clone = pf.clonePF();

        assertEquals(pf, clone);
        assertEquals(text, pf.toStr().toString());

        assertEquals("not", pf.elementAt(0).toString());
        assertEquals(3, pf.size());

        assertEquals("(x > 5) and (y = 7)", pf.elementAt(1).toStr().toString());
        assertNull(pf.elementAt(2));
    }

    @Test
    public void testApplySubstitution() {
        String text = "(not (x > 5) and (y = 7))";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "a");
        alpha.addSubstitution("y", "b");

        JSPredicateForm result = pf.applySubstitutionPF(alpha);

        assertEquals(text, pf.toStr().toString());
        assertEquals("(not ((a > 5) and (b = 7)))", result.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testParseError() {
        String text = "x > 5)";
        JSPredicateForm pf = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testParseError2() {
        String text = "not (x > 5";
        JSPredicateForm pf = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testParseError3() {
        String text = "not ((x > 5)";
        JSPredicateForm pf = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testParseError4() {
        String text = "not ((x > 5) and)";
        JSPredicateForm pf = new JSPredicateForm(text);
    }

    @Test
    public void testEquality() {
        String text1 = "x > 5";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "x > 5";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        assertTrue(pf1.equals(pf2));
    }

    @Test
    public void testInequality() {
        String text1 = "x > 5";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "x < 5";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        assertFalse(pf1.equals(pf2));
    }

    @Test
    public void testEqualityComplex() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        assertTrue(pf1.equals(pf2));
    }

    @Test
    public void testInequalityComplex() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x <= 5) or (y != 7))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        assertFalse(pf1.equals(pf2));
    }

    @Test
    public void testMatches() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x > 5) and (y = 8))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        JSSubstitution result = pf1.matches(pf2);
        assertEquals(2, result.size());
        assertTrue(result.containsSubstitution("y", "8"));
        assertTrue(result.containsSubstitution("x", "x"));
    }

    @Test
    public void testMatchesIdentity() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        JSSubstitution result = pf1.matches(pf2);
        assertEquals(0, result.size());
    }

    @Test
    public void testMatchesFail() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x > 5) and (y = 6))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        JSSubstitution result = pf1.matches(pf2);
        assertEquals(0, result.size());
        assertTrue(result.isFailure());
    }

    @Test
    public void testMatchesComplex() {
        String text1 = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        String text2 = "not ((x <= 5) or (y != 7))";
        JSPredicateForm pf2 = new JSPredicateForm(text2);

        JSSubstitution result = pf1.matches(pf2);
        assertEquals(0, result.size());
        assertTrue(result.isFailure());

        text1 = "not ((x > 5) and (y = 7))";
        text2 = "not ((x > 5) and (z = 7))";
        pf1 = new JSPredicateForm(text1);
        pf2 = new JSPredicateForm(text2);

        result = pf1.matches(pf2);
        assertEquals(0, result.size());
        assertTrue(result.isFailure());

        text1 = "not ((x > 5) and (y = 7))";
        text2 = "not ((x > 5) and (y = 7) and (z = 7))";
        pf1 = new JSPredicateForm(text1);
        pf2 = new JSPredicateForm(text2);

        result = pf1.matches(pf2);
        assertEquals(0, result.size());
        assertFalse(result.isFailure());

        result = pf1.matches(pf2, new JSSubstitution());
        assertEquals(1, result.size());
        assertTrue(result.containsSubstitution("z", "z"));
    }

    @Test
    public void testStandardizer() {
        String text = "not ((x > 5) and (y = 7))";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm standardized = pf.standarizerPredicateForm();

        assertEquals(text, pf.toStr().toString());
        assertEquals("not (or (not (x <= 5)) (not (y != 7)))", standardized.toStr().toString());
    }
}