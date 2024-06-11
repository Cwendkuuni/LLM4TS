package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormEmpty() {
        JSPredicateForm predicateForm = new JSPredicateForm();
        predicateForm.print();
        assertEquals("Predicateform.print(): 0 elements", predicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormInitWithText() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        predicateForm.print();
        assertEquals("(! p1) (or (= x1 y1) (= x1 z1))", predicateForm.toStr().toString());
    }

    @Test
    public void testJSPredicateFormInitWithStreamTokenizer() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        StringReader stringReader = new StringReader(input);
        StreamTokenizer tokenizer = new StreamTokenizer(stringReader);
        JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
        predicateForm.print();
        assertEquals("(! p1) (or (= x1 y1) (= x1 z1))", predicateForm.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidText() {
        String input = "(! p1) (or (= x1 y1) %%%)";
        StringReader stringReader = new StringReader(input);
        StreamTokenizer tokenizer = new StreamTokenizer(stringReader);
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testPrint() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        predicateForm.print();
        System.out.println("Expected: (! p1) (or (= x1 y1) (= x1 z1))");
        System.out.println("Actual:  " + predicateForm.toStr().toString());
        assertEquals("Expected: (! p1) (or (= x1 y1) (= x1 z1))", "Actual:  " + predicateForm.toStr().toString());
    }

    @Test
    public void testToStr() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        String expected = "(! p1) (or (= x1 y1) (= x1 z1))";
        String actual = predicateForm.toStr().toString();
        assertEquals("Expected: " + expected, "Actual:  " + actual);
    }

    @Test
    public void testClonePF() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        JSPredicateForm clonedPredicateForm = predicateForm.clonePF();
        predicateForm.print();
        clonedPredicateForm.print();
        assertEquals("Expected: (! p1) (or (= x1 y1) (= x1 z1))", "Expected:  " + predicateForm.toStr().toString());
        assertEquals("Actual:  " + clonedPredicateForm.toStr().toString(), "Actual:  " + predicateForm.toStr().toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm expected = new JSPredicateForm("(! p1) (or (= x1 z1) (= x1 z1))");
        JSPredicateForm actual = predicateForm.applySubstitutionPF(alpha);
        actual.print();
        assertEquals("Expected: " + expected.toStr().toString(), "Actual:  " + actual.toStr().toString());
    }

    @Test
    public void testMatches() {
        String input1 = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm1 = new JSPredicateForm(input1);

        String input2 = "(! p1) (or (= x1 z1) (= x1 z1))";
        JSPredicateForm predicateForm2 = new JSPredicateForm(input2);

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = predicateForm1.matches(predicateForm2);
        assertTrue(beta.isEmpty());

        String input3 = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm3 = new JSPredicateForm(input3);

        String input4 = "(! p1) (or (= x1 z1) (= x1 y1))";
        JSPredicateForm predicateForm4 = new JSPredicateForm(input4);

        alpha = new JSSubstitution();
        beta = predicateForm3.matches(predicateForm4, alpha);
        assertTrue(beta.isEmpty());

        alpha.addSubstitution("x1", "z1");
        beta = predicateForm3.matches(predicateForm4, alpha);
        assertTrue(beta.isEmpty());

        alpha.addSubstitution("x1", "y1");
        beta = predicateForm3.matches(predicateForm4, alpha);
        assertEquals("Expected: (x1->y1)", "Actual:  " + beta.toString());
    }

    @Test
    public void testEquals() {
        String input1 = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm1 = new JSPredicateForm(input1);

        String input2 = "(! p1) (or (= x1 z1) (= x1 z1))";
        JSPredicateForm predicateForm2 = new JSPredicateForm(input2);

        String input3 = "(! p1) (or (= x1 y1) (= x1 y1))";
        JSPredicateForm predicateForm3 = new JSPredicateForm(input3);

        assertTrue(predicateForm1.equals(predicateForm1));
        assertTrue(predicateForm1.equals(predicateForm3));
        assertFalse(predicateForm1.equals(predicateForm2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String input = "(! p1) (or (= x1 y1) (= x1 z1))";
        JSPredicateForm predicateForm = new JSPredicateForm(input);
        JSPredicateForm standardPredicateForm = predicateForm.standarizerPredicateForm();
        predicateForm.print();
        standardPredicateForm.print();
        assertEquals("Expected: " + predicateForm.toStr().toString(), "Expected:  " + predicateForm.toStr().toString());
        assertEquals("Actual:  " + standardPredicateForm.toStr().toString(), "Actual:  " + predicateForm.toStr().toString());
    }
}