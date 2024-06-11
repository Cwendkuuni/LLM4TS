package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JJSTokenizer;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        // Test empty constructor
        JSPredicateForm predicateForm = new JSPredicateForm();
        assertEquals(0, predicateForm.size());

        // Test constructor with null text
        JSPredicateForm predicateFormNullText = new JSPredicateForm(null);
        assertNull(predicateFormNullText);

        // Test constructor with valid text
        String text = "(!p1)";
        JSPredicateForm predicateFormText = new JSPredicateForm(text);
        assertEquals(1, predicateFormText.size());
        assertEquals("!p1", predicateFormText.elementAt(0).toString());

        // Test constructor with StreamTokenizer
        StringReader stringReader = new StringReader(text);
        JJSTokenizer tokenizer = new JJSTokenizer(stringReader);
        JSPredicateForm predicateFormTokenizer = new JSPredicateForm(tokenizer);
        assertEquals(predicateFormText, predicateFormTokenizer);

        // Test exception thrown when StreamTokenizer is null
        assertThrows(IllegalArgumentException.class, () -> new JSPredicateForm(null));
    }

    @Test
    public void testPrint() {
        String text = "(!p1)";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        assertEquals("( !p1 )", predicateForm.print());
    }

    @Test
    public void testToStr() {
        String text = "(!p1)";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        assertEquals("( !p1 )", predicateForm.toStr().toString());
    }

    @Test
    public void testClonePF() {
        String text = "(!p1)";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        JSPredicateForm clonedPF = predicateForm.clonePF();
        assertEquals(predicateForm, clonedPF);
    }

    @Test
    public void testApplySubstitutionPF() {
        String text1 = "(!p1)";
        String text2 = "(!p2)";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        substitution.addBinding("p1", "p2");
        JSPredicateForm result = predicateForm1.applySubstitutionPF(substitution);
        assertEquals(predicateForm2, result);
    }

    @Test
    public void testMatches() {
        String text1 = "(!p1)";
        String text2 = "(!p1)";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        JSPredicateForm result = predicateForm1.matches(predicateForm2, substitution);
        assertEquals(predicateForm1, result);
    }

    @Test
    public void testMatchesWithSubstitution() {
        String text1 = "(!p1)";
        String text2 = "(!p2)";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        substitution.addBinding("p1", "p2");
        JSPredicateForm result = predicateForm1.matches(predicateForm2, substitution);
        assertEquals(predicateForm1, result);
    }

    @Test
    public void testMatchesFailure() {
        String text1 = "(!p1)";
        String text2 = "(!p2)";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        JSSubstitution substitution = new JSSubstitution();
        substitution.addBinding("p1", "q1");
        JSPredicateForm result = predicateForm1.matches(predicateForm2, substitution);
        assertFalse(result.fail());
    }

    @Test
    public void testEquals() {
        String text1 = "(!p1)";
        String text2 = "(!p2)";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        assertFalse(predicateForm1.equals(predicateForm2));

        text2 = "(!p1)";
        JSPredicateForm predicateForm3 = new JSPredicateForm(text2);
        assertTrue(predicateForm1.equals(predicateForm3));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String text1 = "(!((p1 = p2) and (p2 = p3)))";
        JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        JSPredicateForm result = predicateForm1.standarizerPredicateForm();
        List<String> expected = Arrays.asList("!", "not", "and", "=", "p1", "p2", "p3");
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.elementAt(i).toString());
        }
    }
}