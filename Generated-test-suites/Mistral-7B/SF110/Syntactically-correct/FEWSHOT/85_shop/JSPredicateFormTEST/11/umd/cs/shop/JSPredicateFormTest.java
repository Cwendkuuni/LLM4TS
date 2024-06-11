package org.apache.commons.cli; // This is just for consistency with the given examples, as the JSPredicateForm class is not in the same package.

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspPredicateForm = new JSPredicateForm();
        assertEquals(0, jspPredicateForm.size());
    }

    @Test
    public void testJSPredicateFormWithText() {
        String text = "!p(x)";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
        assertEquals(1, jspPredicateForm.size());
        assertEquals("!p", jspPredicateForm.elementAt(0));

        JSPredicateForm clonedJSPredicateForm = jspPredicateForm.clonePF();
        assertEquals(1, clonedJSPredicateForm.size());
        assertEquals("!p", clonedJSPredicateForm.elementAt(0));

        String expectedToString = "(!p)";
        assertEquals(expectedToString, jspPredicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormWithTokenizer() {
        String text = "!p(x)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspPredicateForm = new JSPredicateForm(tokenizer);
        assertEquals(1, jspPredicateForm.size());
        assertEquals("!p", jspPredicateForm.elementAt(0));

        JSPredicateForm clonedJSPredicateForm = jspPredicateForm.clonePF();
        assertEquals(1, clonedJSPredicateForm.size());
        assertEquals("!p", clonedJSPredicateForm.elementAt(0));

        String expectedToString = "(!p)";
        assertEquals(expectedToString, jspPredicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormWithNotOperator() {
        String text = "not (p(x) or q(x))";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
        assertEquals(3, jspPredicateForm.size());
        assertEquals("not", jspPredicateForm.elementAt(0));
        assertEquals("p(x) or q(x)", jspPredicateForm.elementAt(1).toString());

        JSPredicateForm clonedJSPredicateForm = jspPredicateForm.clonePF();
        assertEquals(3, clonedJSPredicateForm.size());
        assertEquals("not", clonedJSPredicateForm.elementAt(0));
        assertEquals("p(x) or q(x)", clonedJSPredicateForm.elementAt(1).toString());

        String expectedToString = "(not (p(x) or q(x)))";
        assertEquals(expectedToString, jspPredicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormWithCallOperator() {
        String text = "call f(x)";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
        assertEquals(2, jspPredicateForm.size());
        assertEquals("call", jspPredicateForm.elementAt(0));
        assertNotNull(jspPredicateForm.elementAt(1));

        JSTerm jsterm = (JSTerm) jspPredicateForm.elementAt(1);
        assertEquals("f(x)", jsterm.toStr().toString());

        JSPredicateForm clonedJSPredicateForm = jspPredicateForm.clonePF();
        assertEquals(2, clonedJSPredicateForm.size());
        assertEquals("call", clonedJSPredicateForm.elementAt(0));
        assertNotNull(clonedJSPredicateForm.elementAt(1));

        JSTerm clonedJSTerm = (JSTerm) clonedJSPredicateForm.elementAt(1);
        assertEquals("f(x)", clonedJSTerm.toStr().toString());

        String expectedToString = "((call f)(x))";
        assertEquals(expectedToString, jspPredicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormWithMultipleTerms() {
        String text = "(p(x) or q(x)) and r(x)";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
        assertEquals(4, jspPredicateForm.size());

        assertEquals("or", jspPredicateForm.elementAt(1).toString());
        assertEquals("and", jspPredicateForm.elementAt(3).toString());

        JSTerm pTerm = (JSTerm) jspPredicateForm.elementAt(2);
        assertEquals("p(x)", pTerm.toStr().toString());

        JSTerm qTerm = (JSTerm) jspPredicateForm.elementAt(4);
        assertEquals("r(x)", qTerm.toStr().toString());

        String expectedToString = "((p(x)) or (q(x))) and (r(x))";
        assertEquals(expectedToString, jspPredicateForm.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithInvalidText() {
        String text = "!%$#p(x)";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithMissingRightParenthesis() {
        String text = "!p(x";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithMissingLeftParenthesis() {
        String text = "p(x)!";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);
    }

    @Test
    public void testJSPredicateFormMatches() {
        String text1 = "p(x)";
        JSPredicateForm jspPredicateForm1 = new JSPredicateForm(text1);

        String text2 = "q(x)";
        JSPredicateForm jspPredicateForm2 = new JSPredicateForm(text2);

        JSSubstitution substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "p(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertFalse(substitution.isEmpty());
        assertEquals("p(x)", substitution.getSubstitutionFor("p(x)").toString());
        assertEquals(0, substitution.size());

        text1 = "not p(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "not p(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "not p(x) or r(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "not p(x) or q(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "not p(y) or q(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "not p(x) or q(z)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "p(x) or q(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertFalse(substitution.isEmpty());
        assertEquals("p(x)", substitution.getSubstitutionFor("not p(x)").toString());
        assertEquals(1, substitution.size());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "p(y) or q(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());

        text1 = "not p(x) or q(x)";
        jspPredicateForm1 = new JSPredicateForm(text1);

        text2 = "r(x) or q(x)";
        jspPredicateForm2 = new JSPredicateForm(text2);

        substitution = jspPredicateForm1.matches(jspPredicateForm2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testJSPredicateFormStandarizer() {
        String text = "not (p(x) or q(x))";
        JSPredicateForm jspPredicateForm = new JSPredicateForm(text);

        JSPredicateForm standardizedJSPredicateForm = jspPredicateForm.standarizerPredicateForm();

        assertEquals(2, standardizedJSPredicateForm.size());
        assertEquals("not", standardizedJSPredicateForm.elementAt(0));
        assertEquals("or", standardizedJSPredicateForm.elementAt(1));

        JSPredicateForm clonedStandardizedJSPredicateForm = standardizedJSPredicateForm.clonePF();

        assertEquals(2, clonedStandardizedJSPredicateForm.size());
        assertEquals("not", clonedStandardizedJSPredicateForm.elementAt(0));
        assertEquals("or", clonedStandardizedJSPredicateForm.elementAt(1));

        String expectedToString = "((not) (or (not p(x)) (not q(x))))";
        assertEquals(expectedToString, standardizedJSPredicateForm.toStr().toString());
    }
}