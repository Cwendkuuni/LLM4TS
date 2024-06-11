package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        List<String> expected = Arrays.asList("and", "=", "x", "3", "=", "y", "2");
        assertEquals(expected.size(), pf.size());
        for (int i = 0; i < pf.size(); ++i) {
            assertEquals(expected.get(i), pf.elementAt(i).toString());
        }
    }

    @Test
    public void testJSPredicateFormInit_Empty() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertTrue(pf.isEmpty());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_InvalidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a b)"));
        new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_MissingLeftParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("= x 3"));
        new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_MissingRightParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(= x 3"));
        new JSPredicateForm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_UnexpectedOperator() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(= ! x 3)"));
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testJSPredicateFormInit_Not() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (= x 3))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals("not", pf.elementAt(0).toString());
        assertEquals(1, pf.size());
        JSPredicateForm notEl = (JSPredicateForm) pf.elementAt(0);
        assertEquals(1, notEl.size());
        assertEquals("=", notEl.elementAt(0).toString());
        assertEquals("x", notEl.elementAt(1).toString());
        assertEquals(3, notEl.elementAt(2).toString().intValue());
    }

    @Test
    public void testJSPredicateFormInit_Call() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call f (= x 3))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals("call", pf.elementAt(0).toString());
        assertEquals(1, pf.size());
        JSTerm callEl = (JSTerm) pf.elementAt(0);
        assertEquals("f", callEl.toString());
        assertEquals(1, callEl.size());
        assertEquals("=", callEl.elementAt(0).toString());
        assertEquals("x", callEl.elementAt(1).toString());
        assertEquals(3, callEl.elementAt(2).toString().intValue());
    }

    @Test
    public void testPrint() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        pf.print();
        String expected = "(and (= x 3) (= y 2))";
        assertEquals(expected, JSUtil.getPrintBuffer().toString());
    }

    @Test
    public void testToStr() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        StringBuffer sb = pf.toStr();
        String expected = "(and (= x 3) (= y 2))";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testClonePF() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))"));
        JSPredicateForm original = new JSPredicateForm(tokenizer);
        JSPredicateForm clone = original.clonePF();

        assertEquals(original.size(), clone.size());
        for (int i = 0; i < original.size(); ++i) {
            if (original.elementAt(i) instanceof JSPredicateForm) {
                JSPredicateForm originalForm = (JSPredicateForm) original.elementAt(i);
                JSPredicateForm cloneForm = (JSPredicateForm) clone.elementAt(i);
                assertEquals(originalForm.toString(), cloneForm.toString());
            } else {
                assertEquals(original.elementAt(i).toString(), clone.elementAt(i).toString());
            }
        }
    }

    @Test
    public void testApplySubstitutionPF() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))"));
        JSPredicateForm original = new JSPredicateForm(tokenizer);
        JSSubstitution alpha = new JSSubstitution();
        alpha.put("x", new JSTerm(3));
        alpha.put("y", new JSTerm(4));
        JSPredicateForm substituted = original.applySubstitutionPF(alpha);
        String expected = "(and (= 3 3) (= 2 4))";
        assertEquals(expected, substituted.toStr().toString());
    }

    @Test
    public void testMatches_Empty() {
        JSPredicateForm p1 = new JSPredicateForm();
        JSPredicateForm p2 = new JSPredicateForm();
        JSSubstitution result = p1.matches(p2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMatches_Equal() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(= x 3)")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(= x 3)")));
        JSSubstitution result = p1.matches(p2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMatches_NotEqual() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(= x 3)")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(= x 4)")));
        JSSubstitution result = p1.matches(p2);
        assertTrue(result.fail());
    }

    @Test
    public void testMatches_Nested() {
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader("(and (= x 3) (not (= y 2)))"));
        JSPredicateForm p1 = new JSPredicateForm(tokenizer1);

        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(and (= x 3) (= y 3))"));
        JSPredicateForm p2 = new JSPredicateForm(tokenizer2);

        JSSubstitution result = p1.matches(p2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMatches_MissingSubterm() {
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader("(and (= x 3) (not (= y 2)))"));
        JSPredicateForm p1 = new JSPredicateForm(tokenizer1);

        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(and (= x 3) (not (= z 2)))"));
        JSPredicateForm p2 = new JSPredicateForm(tokenizer2);

        JSSubstitution result = p1.matches(p2);
        assertTrue(result.fail());
    }

    @Test
    public void testStandarizerPredicateForm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (and (= x 3) (= y 2)))"));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        JSPredicateForm normalized = pf.standarizerPredicateForm();
        String expected = "(not (and (= y 2) (= x 3)))";
        assertEquals(expected, normalized.toStr().toString());
    }

    @Test
    public void testEquals_Equal() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))")));
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEquals_NotEqual() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 1))")));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEquals_Nested() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (not (= y 2)))")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (and (= y 2) (= x 3)))")));
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEquals_DifferentSize() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))")));
        JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2) (= z 2))")));
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEquals_DifferentType() {
        JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 3) (= y 2))")));
        String str = "another string";
        assertFalse(p1.equals(str));
    }
}