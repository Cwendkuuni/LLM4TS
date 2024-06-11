import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermConstruction() {
        JSTerm jsterm = new JSTerm();
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEmpty(jsterm.getElements());

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Term"));
        JSTerm term = new JSTerm(tokenizer);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertEquals("Term", term.getElements().get(0));
        assertEquals(1, term.size());

        tokenizer = new StreamTokenizer(new StringReader("(+ 2 3)"));
        term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals("+", term.getElements().get(0));
        assertEquals(3, term.size());
        assertEquals("2", term.getElements().get(1).toString());
        assertEquals("3", term.getElements().get(2).toString());

        tokenizer = new StreamTokenizer(new StringReader("(term1 (+ 2 3))"));
        term = new JSTerm(tokenizer);
        assertTrue(term.isEval());
        assertEquals("call", term.getElements().get(0).toString());
        assertEquals("term1", term.getElements().get(1).toString());
        assertEquals("+", term.getElements().get(2).toString());
        assertEquals("2", term.getElements().get(3).toString());
        assertEquals("3", term.getElements().get(4).toString());
        assertEquals(5, term.size());
    }

    @Test
    public void testJSTermList() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("term1");
        jsterm.addElement(new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))));
        jsterm.addElement("term2");
        jsterm.addElement(new JSTerm(new StreamTokenizer(new StringReader("(* 3 4)"))));
        List<JSTerm> expectedList = new ArrayList<>();
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("term1"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("+"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("1"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("2"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("term2"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("*"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("3"))));
        expectedList.add(new JSTerm(new StreamTokenizer(new StringReader("4"))));

        List<JSTerm> actualList = new ArrayList<>();
        for (Object obj : jsterm.getElements()) {
            actualList.add((JSTerm) obj);
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testJSTermEquals() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        assertTrue(jsterm1.equals(jsterm2));

        String var1 = "x";
        String var2 = "y";
        JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(+ " + var1 + " 2)")));
        JSTerm jsterm4 = new JSTerm(new StreamTokenizer(new StringReader("(+ " + var2 + " 2)")));
        assertFalse(jsterm3.equals(jsterm4));

        JSTerm jsterm5 = new JSTerm(new StreamTokenizer(new StringReader("nil")));
        JSTerm jsterm6 = new JSTerm(new StreamTokenizer(new StringReader("nil")));
        assertTrue(jsterm5.equals(jsterm6));

        JSTerm jsterm7 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm jsterm8 = new JSTerm(new StreamTokenizer(new StringReader("(+ 2 1)")));
        assertFalse(jsterm7.equals(jsterm8));

        JSTerm jsterm9 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm jsterm10 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2.0)")));
        assertFalse(jsterm9.equals(jsterm10));
    }

    @Test
    public void testJSTermToString() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("term1");
        jsterm.addElement(new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))));
        jsterm.addElement("term2");
        jsterm.addElement(new JSTerm(new StreamTokenizer(new StringReader("(* 3 4)"))));

        String expected = "term1 (+ 1 2) term2 (* 3 4)";
        String actual = jsterm.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testJSTermClone() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm clonedJsterm = jsterm.cloneT();
        assertEquals(jsterm, clonedJsterm);
    }

    @Test
    public void testJSTermApplySubstitution() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("3")));
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", term3));

        JSTerm result = term2.applySubstitutionT(substitution);
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader("(+ 3 2)")));
        assertEquals(expected, result);
    }

    @Test
    public void testJSTermMatches() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("(+ y 2)")));
        JSSubstitution substitution = new JSSubstitution();

        JSTerm result = term3.matches(term4);
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader("(+ y 2)")));
        assertEquals(expected, result);

        substitution.addElement(new JSPairTerm("x", term4));
        result = term1.matches(term3, substitution);
        assertEquals(term3, result);
    }

    @Test
    public void testJSTermGround() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2.0)")));
        JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("3")));

        assertTrue(term4.isGround());
        assertFalse(term1.isGround());
        assertFalse(term2.isGround());
        assertFalse(term3.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2.0)")));
        JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("3")));
        JSTerm term5 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));

        JSTerm result1 = term1.standardizerTerm();
        JSTerm expected1 = new JSTerm(new StreamTokenizer(new StringReader("x" + String.valueOf(VarCounter))));
        assertEquals(expected1, result1);

        JSTerm result2 = term2.standardizerTerm();
        JSTerm expected2 = new JSTerm(new StreamTokenizer(new StringReader("(+ " + String.valueOf(VarCounter) + " 2)")));
        assertEquals(expected2, result2);

        JSTerm result3 = term3.standardizerTerm();
        JSTerm expected3 = new JSTerm(new StreamTokenizer(new StringReader("(+ " + String.valueOf(VarCounter) + " 2.0)")));
        assertEquals(expected3, result3);

        JSTerm result4 = term4.standardizerTerm();
        JSTerm expected4 = term4; // Constants don't need to be standardized
        assertEquals(expected4, result4);

        JSTerm result5 = term5.standardizerTerm();
        JSTerm expected5 = new JSTerm(new StreamTokenizer(new StringReader("(+ " + String.valueOf(VarCounter) + " 2)")));
        assertEquals(expected5, result5);
    }

    @Test
    public void testJSTermCall() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)")));
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("3")));
        JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("(+)")));
        JSTerm term5 = new JSTerm(new StreamTokenizer(new StringReader("not")));

        JSTerm result1 = term2.call();
        JSTerm expected1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 3 2)")));
        assertEquals(expected1, result1);

        result1 = term1.call();
        JSTerm expected2 = term1;
        assertEquals(expected2, result1);

        result1 = term3.call();
        JSTerm expected3 = term3;
        assertEquals(expected3, result1);

        result1 = term4.call();
        JSTerm expected4 = new JSTerm();
        assertEquals(expected4, result1);

        result1 = term5.call();
        JSTerm expected5 = new JSTerm(new StreamTokenizer(new StringReader("not")));
        assertEquals(expected5, result1);
    }

}