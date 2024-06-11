package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        String input = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        assertEquals(3, predicateForm.size());
        assertEquals("not", predicateForm.elementAt(0).toString());
        assertEquals("or", predicateForm.elementAt(1).toString());
        JSTerm term = predicateForm.elementAt(2);
        assertEquals("=", term.toString());
        assertEquals("x", term.elementAt(0).toString());
        assertEquals(1, term.size());
        assertEquals(1, term.elementAt(0).size());
        assertEquals("1", term.elementAt(0).elementAt(0).toString());

        input = "(not (call foo x))";
        predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        assertEquals(2, predicateForm.size());
        assertEquals("not", predicateForm.elementAt(0).toString());
        assertEquals("call", predicateForm.elementAt(1).toString());
        term = predicateForm.elementAt(2);
        assertEquals("foo", term.toString());
        assertEquals(1, term.size());
    }

    @Test
    public void testJSPredicateFormClonePF() {
        String input = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        JSPredicateForm cloned = predicateForm.clonePF();
        assertEquals(predicateForm, cloned);
        assertEquals(predicateForm.size(), cloned.size());
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        String input = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        JSSubstitution substitution = new JSSubstitution();
        substitution.put("x", new JSTerm("3"));
        JSPredicateForm applied = predicateForm.applySubstitutionPF(substitution);
        JSTerm term = applied.elementAt(1);
        assertEquals("3", term.elementAt(0).toString());
    }

    @Test
    public void testJSPredicateFormMatches() {
        String input1 = "(not (or (= x 1) (= x 2)))";
        String input2 = "(not (or (= x 3) (= x 4)))";
        JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader(input2)));
        JSSubstitution substitution = new JSSubstitution();
        assertEquals(false, predicateForm1.matches(predicateForm2, substitution).fail());

        input1 = "(not (or (= x 1) (= x 2)))";
        input2 = "(not (or (= x 1) (= x 1)))";
        predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader(input2)));
        substitution = new JSSubstitution();
        assertEquals(true, predicateForm1.matches(predicateForm2, substitution).isEmpty());
    }

    @Test
    public void testJSPredicateFormPrint() {
        String input = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        predicateForm.print();
        assertTrue(JSUtil.isPrinted("(not (or (= x 1) (= x 2)))"));
    }

    @Test
    public void testJSPredicateFormToStr() {
        String input = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        String result = predicateForm.toStr().toString();
        assertEquals(input, result);
    }
}