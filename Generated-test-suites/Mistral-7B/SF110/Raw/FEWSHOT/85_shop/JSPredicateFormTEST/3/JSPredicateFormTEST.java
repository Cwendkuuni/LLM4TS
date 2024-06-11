package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        String text = "(!p)";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(Arrays.asList("!", "p"), pf.toList());

        text = "((not a) and (b = c))";
        pf = new JSPredicateForm(text);
        assertEquals(Arrays.asList("not", "a", "and", "b", "=", "c"), pf.toList());

        text = "((call f (x)) and ((not y) or z))";
        pf = new JSPredicateForm(text);
        assertEquals(Arrays.asList("call", "f", "(", "x", ")", "and", "not", "y", "or", "z"), pf.toList());
    }

    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.addElement("not");
        pf.addElement(new JSTerm("x"));
        pf.addElement("and");
        pf.addElement(new JSTerm("y"));
        pf.addElement("=");
        pf.addElement(new JSTerm("z"));
        pf.print();
        assertEquals("(not x and y = z)", pf.toStr().toString());
    }

    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("(not a) and (b = c)");
        JSPredicateForm pf2 = new JSPredicateForm("(not a) and (b = c)");
        JSPredicateForm pf3 = new JSPredicateForm("(not a) and (b = d)");
        JSPredicateForm pf4 = new JSPredicateForm("((not a) and (b = c))");

        assertTrue(pf1.equals(pf2));
        assertFalse(pf1.equals(pf3));
        assertFalse(pf1.equals(pf4));
    }

    @Test
    public void testMatches() {
        JSTerm t1 = new JSTerm("x > 3");
        JSTerm t2 = new JSTerm("y = z");
        JSTerm t3 = new JSTerm("x > 3 and y = z");
        JSPredicateForm pf1 = new JSPredicateForm("(not (x > 3))");
        JSPredicateForm pf2 = new JSPredicateForm("((not (x > 3)) and (x < 4))");

        JSSubstitution subs = pf1.matches(new JSPredicateForm(t1), new JSSubstitution());
        assertNull(subs);

        subs = pf1.matches(new JSPredicateForm(t2), new JSSubstitution());
        assertNull(subs);

        subs = pf1.matches(new JSPredicateForm(t3), new JSSubstitution());
        List<String> expected = Arrays.asList("x", "3", "y", "z");
        assertEquals(expected, subs.getKeys());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm("(not a) and (b = c)");
        JSPredicateForm clone = pf.clonePF();

        assertEquals(pf.toStr(), clone.toStr());
    }

    @Test
    public void testApplySubstitutionPF() {
        JSTerm t1 = new JSTerm("x > 3");
        JSTerm t2 = new JSTerm("y = z");
        JSTerm t3 = new JSTerm("x > 3 and y = z");
        JSTerm t4 = new JSTerm("a > b");
        JSPredicateForm pf1 = new JSPredicateForm("(not (x > 3))");
        JSPredicateForm pf2 = new JSPredicateForm("((not (x > 3)) and (x < 4))");
        JSPredicateForm pf3 = new JSPredicateForm("((not a) and (b = c))");

        JSSubstitution subs = new JSSubstitution();
        subs.put("x", "a");

        JSPredicateForm pf4 = pf1.applySubstitutionPF(subs);
        assertEquals("(not (a > 3))", pf4.toStr().toString());

        pf4 = pf2.applySubstitutionPF(subs);
        assertEquals("((not (a > 3)) and (a < 4))", pf4.toStr().toString());

        JSSubstitution subs2 = new JSSubstitution();
        subs2.put("b", "d");
        subs2.put("c", "e");

        JSPredicateForm pf5 = pf3.applySubstitutionPF(subs2);
        assertEquals("((not a) and (d = e))", pf5.toStr().toString());
    }

    @Test
    public void testStandardizerPredicateForm() {
        JSTerm t1 = new JSTerm("x > 3");
        JSTerm t2 = new JSTerm("y = z");
        JSTerm t3 = new JSTerm("x > 3 and y = z");
        JSPredicateForm pf1 = new JSPredicateForm("(not (x > 3))");
        JSPredicateForm pf2 = new JSPredicateForm("((not (x > 3)) and (x < 4))");
        JSPredicateForm pf3 = new JSPredicateForm("((not a) and (b = c))");

        JSPredicateForm npf1 = pf1.standarizerPredicateForm();
        assertEquals("(not (x > 3))", npf1.toStr().toString());

        JSPredicateForm npf2 = pf2.standarizerPredicateForm();
        assertEquals("(and (not (x > 3)) (< x 4))", npf2.toStr().toString());

        JSPredicateForm npf3 = pf3.standarizerPredicateForm();
        assertEquals("(and (not a) (= b c))", npf3.toStr().toString());
    }

    @Test
    public void testJSPredicateFormInit_Empty() {
        JSPredicateForm pf = new JSPredicateForm();
        assertTrue(pf.isEmpty());
    }

    @Test
    public void testJSPredicateFormInit_NullText() {
        try {
            new JSPredicateForm(null);
            fail("should have thrown an exception");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testJSPredicateFormInit_InvalidText() {
        String text = "Invalid text";
        try {
            new JSPredicateForm(text);
            fail("should have thrown an exception");
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testJSPredicateFormInit_EmptyStream() {
        StringReader reader = new StringReader("");
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        try {
            new JSPredicateForm(tokenizer);
            fail("should have thrown an exception");
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testJSPredicateFormInit_InvalidToken() {
        String text = "(!p)a";
        StringReader reader = new StringReader(text);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        try {
            new JSPredicateForm(tokenizer);
            fail("should have thrown an exception");
        } catch (JSParserError e) {
            // expected
        }
    }
}