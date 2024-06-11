package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertTrue(jspf.isEmpty());
    }

    @Test
    public void testCreateJSPredicateFormWithText() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(6, jspf.size());
        assertEquals("not", jspf.get(0).toString());
        assertEquals("a", jspf.get(1).toString());
        assertEquals("b", jspf.get(2).toString());
        assertEquals("=", jspf.get(3).toString());
        assertEquals("c", jspf.get(4).toString());
        assertEquals("<", jspf.get(5).toString());
        assertEquals("d", jspf.get(6).toString());
    }

    @Test
    public void testCreateJSPredicateFormWithStreamTokenizer() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(6, jspf.size());
        // ... add assertions similar to testCreateJSPredicateFormWithText() here
    }

    @Test
    public void testJSPredicateFormToString() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals("not((a=b) and (c<d))", jspf.toString());
    }

    @Test
    public void testJSPredicateFormClone() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf, cloned);
    }

    @Test
    public void testJSPredicateFormApplySubstitution() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSSubstitution alpha = new JSSubstitution();
        alpha.put("a", new JSTerm("c"));
        alpha.put("b", new JSTerm("e"));
        JSPredicateForm result = jspf.applySubstitutionPF(alpha);
        assertEquals("not((c=e) and (c<d))", result.toString());
    }

    @Test
    public void testJSPredicateFormMatchesEmpty() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(jspf2);
        assertNull(beta);
    }

    @Test
    public void testJSPredicateFormMatchesSame() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = b )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(jspf2);
        assertEquals(new JSSubstitution(), beta);
    }

    @Test
    public void testJSPredicateFormMatchesDifferentSizes() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = b ) and c = d";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(jspf2);
        assertFalse(beta.isEmpty());
    }

    @Test
    public void testJSPredicateFormMatchesDifferentTerms() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = c )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(jspf2);
        assertFalse(beta.isEmpty());
    }

    @Test
    public void testJSPredicateFormMatchesSubstitution() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( c = d )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution alpha = new JSSubstitution();
        alpha.put("a", new JSTerm("c"));
        alpha.put("b", new JSTerm("d"));
        JSSubstitution beta = jspf1.matches(jspf2, alpha);
        assertEquals(new JSSubstitution(), beta);
    }

    @Test
    public void testJSPredicateFormMatchesFailure() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = c )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(jspf2);
        assertFalse(beta.isEmpty());
    }

    @Test
    public void testJSPredicateFormEquals() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = b )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertTrue(jspf1.equals(jspf2));
    }

    @Test
    public void testJSPredicateFormNotEquals() {
        String text1 = "not ( a = b )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        String text2 = "not ( a = c )";
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertFalse(jspf1.equals(jspf2));
    }

    @Test
    public void testJSPredicateFormEqualsNull() {
        String text = "not ( a = b )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertFalse(jspf.equals(null));
    }

    @Test
    public void testJSPredicateFormStandarizer() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm result = jspf.standarizerPredicateForm();
        assertEquals("not((a=b) and (c<d))", result.toString());
    }

    @Test
    public void testJSPredicateFormSize() {
        String text = "not ( ( a = b ) and ( c < d ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(6, jspf.size());
    }

    @Test
    public void testJSPredicateFormIsEmpty() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertTrue(jspf.isEmpty());
    }
}