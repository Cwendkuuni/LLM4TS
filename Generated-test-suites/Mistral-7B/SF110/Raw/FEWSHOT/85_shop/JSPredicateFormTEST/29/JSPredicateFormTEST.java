package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit_Empty() {
        JSPredicateForm form = new JSPredicateForm();
        assertFalse(form.isEmpty());
        assertEquals(0, form.size());
    }

    @Test
    public void testJSPredicateFormInit_NullText() {
        JSPredicateForm form = new JSPredicateForm();
        assertFalse(form.isEmpty());
        assertEquals(0, form.size());
    }

    @Test
    public void testJSPredicateFormInit_EmptyText() {
        JSPredicateForm form = new JSPredicateForm(new StringReader(""));
        assertFalse(form.isEmpty());
        assertEquals(0, form.size());
    }

    @Test
    public void testJSPredicateFormInit_SimplePredicate() {
        String text = "f";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
        assertFalse(form.isEmpty());
        assertEquals(1, form.size());
        assertEquals("f", form.elementAt(0).toString());
    }

    @Test
    public void testJSPredicateFormInit_PredicateWithNot() {
        String text = "not(f)";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
        assertFalse(form.isEmpty());
        assertEquals(1, form.size());
        assertEquals("not", form.elementAt(0).toString().toLowerCase());
        assertTrue(form.elementAt(1) instanceof JSPredicateForm);
    }

    @Test
    public void testJSPredicateFormInit_PredicateWithCall() {
        String text = "call(g(x))";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
        assertFalse(form.isEmpty());
        assertEquals(2, form.size());
        assertEquals("call", form.elementAt(0).toString().toLowerCase());
        assertTrue(form.elementAt(1) instanceof JSTerm);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_PredicateWithInvalidToken() {
        String text = "not%%%";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_PredicateWithMissingRightParenthesis() {
        String text = "not(f";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
    }

    @Test
    public void testJSPredicateFormInit_ComplexPredicate() {
        String text = "not(f(x)) or g(y)";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
        assertFalse(form.isEmpty());
        assertEquals(3, form.size());
        assertEquals("not", form.elementAt(0).toString().toLowerCase());
        assertTrue(form.elementAt(1) instanceof JSTerm);
        assertEquals("or", form.elementAt(2).toString().toLowerCase());
        assertTrue(form.elementAt(3) instanceof JSPredicateForm);
    }

    @Test
    public void testClonePF() {
        String text = "not(f(x)) or g(y)";
        JSPredicateForm originalForm = new JSPredicateForm(new StringReader(text));
        assertFalse(originalForm.isEmpty());
        JSPredicateForm clonedForm = originalForm.clonePF();
        assertEquals(originalForm.size(), clonedForm.size());
        for (int i = 0; i < originalForm.size(); ++i) {
            assertEquals(originalForm.elementAt(i).toString(), clonedForm.elementAt(i).toString());
        }
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution substitution = new JSSubstitution();
        String text = "not(f(x)) or g(y)";
        JSPredicateForm originalForm = new JSPredicateForm(new StringReader(text));
        JSPredicateForm substitutedForm = originalForm.applySubstitutionPF(substitution);
        assertFalse(originalForm.isEmpty());
        assertFalse(substitutedForm.isEmpty());
        assertEquals(originalForm.size(), substitutedForm.size());
        for (int i = 0; i < originalForm.size(); ++i) {
            JSTerm originalTerm = originalForm.elementAt(i);
            JSTerm substitutedTerm = substitutedForm.elementAt(i);
            if (originalTerm instanceof JSTerm) {
                assertEquals(originalTerm.toString(), substitutedTerm.toString());
            } else if (originalTerm instanceof JSPredicateForm) {
                assertEquals(originalTerm.toString(), substitutedTerm.toString());
                JSPredicateForm subTermOriginal = (JSPredicateForm) originalTerm;
                JSPredicateForm subTermSubstituted = (JSPredicateForm) substitutedTerm;
                assertEquals(subTermOriginal.size(), subTermSubstituted.size());
                for (int j = 0; j < subTermOriginal.size(); ++j) {
                    JSTerm subTermOriginalTerm = subTermOriginal.elementAt(j);
                    JSTerm subTermSubstitutedTerm = subTermSubstituted.elementAt(j);
                    assertEquals(subTermOriginalTerm.toString(), subTermSubstitutedTerm.toString());
                }
            }
        }
    }

    @Test
    public void testMatches() {
        JSSubstitution substitution = new JSSubstitution();
        String text = "not(f(x)) or g(y)";
        JSPredicateForm originalForm = new JSPredicateForm(new StringReader(text));
        String text2 = "not(h(z)) or h(w)";
        JSPredicateForm otherForm = new JSPredicateForm(new StringReader(text2));
        JSSubstitution result = originalForm.matches(otherForm);
        assertTrue(result.isEmpty());

        substitution.addSubstitution("f", "h");
        substitution.addSubstitution("x", "z");
        result = originalForm.matches(otherForm, substitution);
        assertEquals(Collections.singletonList("f(z)"), Arrays.asList(result.getSubstitutionList()));

        substitution.clear();
        substitution.addSubstitution("g", "h");
        substitution.addSubstitution("y", "w");
        result = originalForm.matches(otherForm, substitution);
        assertEquals(Arrays.asList("not(f(z))", "h(w)"), Arrays.asList(result.getSubstitutionList()));
    }

    @Test
    public void testEquals() {
        String text = "not(f(x)) or g(y)";
        JSPredicateForm form1 = new JSPredicateForm(new StringReader(text));
        JSPredicateForm form2 = new JSPredicateForm(new StringReader(text));
        assertTrue(form1.equals(form2));

        form1.addElement("not");
        assertFalse(form1.equals(form2));

        form2.addElement("not");
        assertTrue(form1.equals(form2));

        JSPredicateForm form3 = new JSPredicateForm(new StringReader("not(f(x)) or g(z)"));
        assertFalse(form1.equals(form3));

        form3 = new JSPredicateForm(new StringReader("not(f(x)) or g(y)"));
        assertTrue(form1.equals(form3));
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm(new StringReader("not(f(x))"));
        String expected = "Predicateform.print(): not(f(x))";
        form.print();
        assertEquals(expected, JSUtil.getLastPrintedLine());

        form.print();
        assertEquals("", JSUtil.getLastPrintedLine());
    }

    @Test
    public void testToString() {
        JSPredicateForm form = new JSPredicateForm(new StringReader("not(f(x))"));
        String expected = "(\\(not\\) f(x))";
        assertEquals(expected, form.toStr().toString());

        form = new JSPredicateForm(new StringReader("not(f(x)) or g(y)"));
        expected = "(\\(not\\) f(x)) or g(y)";
        assertEquals(expected, form.toStr().toString());
    }

    @Test
    public void testStandardizerPredicateForm() {
        String text = "not(not(f(x)))";
        JSPredicateForm form = new JSPredicateForm(new StringReader(text));
        JSPredicateForm standardizedForm = form.standarizerPredicateForm();
        assertEquals(Arrays.asList("not", "not", "f(x)"), Arrays.asList(standardizedForm.elementAt(0), standardizedForm.elementAt(1), standardizedForm.elementAt(standardizedForm.size() - 1)));
    }
}