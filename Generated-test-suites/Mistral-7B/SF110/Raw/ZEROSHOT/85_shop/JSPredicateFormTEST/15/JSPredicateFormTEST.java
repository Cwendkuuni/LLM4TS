package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormEmptyConstruction() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormStringConstruction() {
        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(4, jspf.size());
        assertEquals("a = b", jspf.elementAt(0).toString());
        assertEquals("and", jspf.elementAt(1).toString());
        assertEquals("c = d", jspf.elementAt(2).toString());
    }

    @Test
    public void testJSPredicateFormStreamTokenizerConstruction() {
        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        assertEquals(4, jspf.size());
        assertEquals("a = b", jspf.elementAt(0).toString());
        assertEquals("and", jspf.elementAt(1).toString());
        assertEquals("c = d", jspf.elementAt(2).toString());
    }

    @Test
    public void testJSPredicateFormInitWithInvalidInput() {
        String input = "((a = b) and %%%)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSPredicateForm jspf = new JSPredicateForm();
        try {
            jspf.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // Expected
        }
    }

    @Test
    public void testJSPredicateFormNotOperator() {
        String input = "not ((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(5, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertEquals("((a = b) and (c = d))", jspf.elementAt(1).toString());
        assertFalse(jspf.elementAt(2) instanceof JSPredicateForm);
        assertEquals(4, jspf.elementAt(2).size());
        assertEquals("a = b", jspf.elementAt(2).elementAt(0).toString());
        assertEquals("and", jspf.elementAt(2).elementAt(1).toString());
        assertEquals("c = d", jspf.elementAt(2).elementAt(2).toString());
    }

    @Test
    public void testJSPredicateFormCallOperator() {
        String input = "((call a) (b = c))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(3, jspf.size());
        assertEquals("call", jspf.elementAt(0).toString());
        assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
        assertEquals(2, jspf.elementAt(1).size());
        assertEquals("a", jspf.elementAt(1).elementAt(0).toString());
        assertEquals("b = c", jspf.elementAt(1).elementAt(1).toString());
    }

    @Test
    public void testPrint() {
        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals("(a = b and c = d)", jspf.print());
    }

    @Test
    public void testToString() {
        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals("(a = b and c = d)", jspf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals("(a = b and c = d)", cloned.print());
        assertNotSame(jspf, cloned);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addEntry("a", new JSTerm("x"));
        alpha.addEntry("b", new JSTerm("y"));

        String input = "((a = b) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        JSPredicateForm result = jspf.applySubstitutionPF(alpha);
        assertEquals("(x = y and c = d)", result.print());
    }

    @Test
    public void testMatches() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addEntry("a", new JSTerm("x"));
        alpha.addEntry("b", new JSTerm("y"));

        String input1 = "((a = b) and (c = d))";
        JSPredicateForm jspf1 = new JSPredicateForm(input1);

        String input2 = "((x = y) and (e = d))";
        JSPredicateForm jspf2 = new JSPredicateForm(input2);

        JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
        assertTrue(beta.isEmpty());

        beta = jspf1.matches(jspf2, alpha);
        assertFalse(beta.isEmpty());
        assertEquals(2, beta.size());
        assertEquals("x", beta.elementAt(0).getName());
        assertEquals("y", beta.elementAt(1).getName());
    }

    @Test
    public void testEquals() {
        String input1 = "((a = b) and (c = d))";
        JSPredicateForm jspf1 = new JSPredicateForm(input1);

        String input2 = "((a = b) and (c = d))";
        JSPredicateForm jspf2 = new JSPredicateForm(input2);

        String input3 = "((a = b) and (e = d))";
        JSPredicateForm jspf3 = new JSPredicateForm(input3);

        assertTrue(jspf1.equals(jspf2));
        assertFalse(jspf1.equals(jspf3));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String input = "((not (a = b)) and (c = d))";
        JSPredicateForm jspf = new JSPredicateForm(input);
        JSPredicateForm standardized = jspf.standarizerPredicateForm();
        assertEquals("(not (a = b)) and (c = d)", jspf.print());
        assertEquals("(not (a = b) and c = d)", standardized.print());
    }
}