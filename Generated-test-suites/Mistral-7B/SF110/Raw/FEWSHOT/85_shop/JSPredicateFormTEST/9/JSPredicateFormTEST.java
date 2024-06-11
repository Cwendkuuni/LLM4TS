package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormWithText() {
        String text = "not (x < 5)";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
        JSPredicateForm nestedJSPredicateForm = (JSPredicateForm) jspf.elementAt(1);
        assertEquals(1, nestedJSPredicateForm.size());
        assertEquals("x < 5", nestedJSPredicateForm.elementAt(0).toString());
    }

    @Test
    public void testJSPredicateFormWithTokenizer() {
        String text = "not (x < 5)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
        JSPredicateForm nestedJSPredicateForm = (JSPredicateForm) jspf.elementAt(1);
        assertEquals(1, nestedJSPredicateForm.size());
        assertEquals("x < 5", nestedJSPredicateForm.elementAt(0).toString());
    }

    @Test
    public void testJSPredicateFormWithNotOperator() {
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.addElement("not");
        jspf.addElement(new JSTerm("x"));
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertEquals("x", jspf.elementAt(1).toString());
    }

    @Test
    public void testJSPredicateFormWithCallOperator() {
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.addElement("call");
        jspf.addElement(new String("foo"));
        assertEquals(2, jspf.size());
        assertEquals("call", jspf.elementAt(0).toString());
        assertEquals("foo", jspf.elementAt(1).toString());
    }

    @Test
    public void testJSPredicateFormPrinting() {
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.addElement("not");
        jspf.addElement(new JSPredicateForm());
        jspf.getLastElement().addElement(new JSTerm("x"));
        jspf.getLastElement().addElement(new JSTerm("=", 5));
        jspf.print();
        assertEquals("(not (x = 5))", jspf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormCloning() {
        String text = "not (x < 5)";
        JSPredicateForm originalJSPredicateForm = new JSPredicateForm(text);
        JSPredicateForm clonedJSPredicateForm = originalJSPredicateForm.clonePF();
        assertEquals(originalJSPredicateForm, clonedJSPredicateForm);
    }

    @Test
    public void testJSPredicateFormApplyingSubstitution() {
        String text = "x = y and y = z";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSSubstitution substitution = new JSSubstitution();
        substitution.addSubstitution("x", "a");
        substitution.addSubstitution("y", "b");
        substitution.addSubstitution("z", "c");
        JSPredicateForm transformedJSPredicateForm = jspf.applySubstitutionPF(substitution);
        assertEquals("a = b and b = c", transformedJSPredicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormMatching() {
        String text1 = "not (x < 5)";
        String text2 = "x > 5";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        JSSubstitution result = jspf1.matches(jspf2, substitution);
        assertTrue(result.fail());
    }

    @Test
    public void testJSPredicateFormMatchingWithSuccess() {
        String text1 = "not (x < 5)";
        String text2 = "x >= 5";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        JSSubstitution result = jspf1.matches(jspf2, substitution);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSPredicateFormEquality() {
        String text1 = "not (x < 5)";
        String text2 = "not (x < 5)";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertTrue(jspf1.equals(jspf2));
    }

    @Test
    public void testJSPredicateFormStandardization() {
        String text = "not (x < 5 and y > 3 and z = a)";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm standardizedJSPredicateForm = jspf.standarizerPredicateForm();
        assertEquals("not (and (< x 5) (> y 3) (= z a))", standardizedJSPredicateForm.toStr().toString());
    }
}