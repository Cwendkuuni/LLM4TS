package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        String text = "!(not (and (A x) (B x)))";
        JSPredicateForm jspf = new JSPredicateForm(text);

        assertEquals("!(not (and (A x) (B x)))", jspf.toStr().toString());

        // Testing the initialization with StreamTokenizer
        StringReader reader = new StringReader(text);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSPredicateForm jspfFromTokenizer = new JSPredicateForm(tokenizer);

        assertEquals("!(not (and (A x) (B x)))", jspfFromTokenizer.toStr().toString());
        assertArrayEquals(jspf.toArray(), jspfFromTokenizer.toArray());
    }

    @Test
    public void testPrint() {
        JSPredicateForm jspf = new JSPredicateForm("!(not (and (A x) (B x)))");
        jspf.print();
    }

    @Test
    public void testToStr() {
        JSPredicateForm jspf = new JSPredicateForm("!(not (and (A x) (B x)))");
        assertEquals("!(not (and (A x) (B x)))", jspf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm("!(not (and (A x) (B x)))");
        JSPredicateForm clonedPF = jspf.clonePF();

        assertEquals(jspf.toStr().toString(), clonedPF.toStr().toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm("!(not (and (A x) (B x)))");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "y");
        JSPredicateForm beta = jspf.applySubstitutionPF(alpha);

        assertEquals("!(not (and (A y) (B y)))", beta.toStr().toString());
    }

    @Test
    public void testMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("!(not (and (A x) (B x)))");
        JSPredicateForm jspf2 = new JSPredicateForm("!(not (and (A y) (C y)))");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = jspf1.matches(jspf2, alpha);

        assertTrue(beta.isEmpty());

        alpha.addSubstitution("A", "P");
        beta = jspf1.matches(jspf2, alpha);

        assertFalse(beta.isEmpty());
        assertEquals(1, beta.size());
        assertEquals("y", beta.getSymbol("x").toString());
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("!(not (and (A x) (B x)))");
        JSPredicateForm jspf2 = new JSPredicateForm("!(not (and (A y) (B y)))");

        assertFalse(jspf1.equals(jspf2));

        JSPredicateForm jspf3 = new JSPredicateForm("!(not (and (A x) (B x)))");
        assertTrue(jspf1.equals(jspf3));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm("!(not (and (A x) (B x)))");
        JSPredicateForm standardizedPF = jspf.standarizerPredicateForm();

        assertEquals("!(not (and (A x) (B x)))", standardizedPF.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitInvalid() {
        String text = "!";
        new JSPredicateForm(text);
    }
}