package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSParserError;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

    @Test
    public void testEmptyConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testStringConstructor() {
        String text = "not (p(x) or q(y))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(3, jspf.size());
        assertEquals("-not-", jspf.elementAt(0).toString());
        assertEquals("p", jspf.elementAt(1).toString());
        assertEquals("x", ((JSTerm) jspf.elementAt(2)).getVarName());
        assertEquals(false, jspf.elementAt(2).isConstant());
    }

    @Test
    public void testStreamTokenizerConstructor() {
        String text = "not (p(x) or q(y))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(3, jspf.size());
        assertEquals("-not-", jspf.elementAt(0).toString());
        assertEquals("p", jspf.elementAt(1).toString());
        assertEquals("x", ((JSTerm) jspf.elementAt(2)).getVarName());
        assertEquals(false, jspf.elementAt(2).isConstant());
    }

    @Test(expected = JSParserError.class)
    public void testInvalidConstructor() {
        String text = "not (p(x) or q(y) %%%)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testPrintMethod() {
        String text = "not (p(x) or q(y))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        jspf.print();
        assertTrue(JSUtil.readLine().contains("not (p(x) or q(y))"));
    }

    @Test
    public void testToStringMethod() {
        String text = "not (p(x) or q(y))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String expected = "((-not-) (p x) (or) (q y))";
        assertEquals(expected, jspf.toStr().toString());
    }

    @Test
    public void testClonePFMethod() {
        String text = "not (p(x) or q(y))";
        JSPredicateForm original = new JSPredicateForm(text);
        JSPredicateForm cloned = original.clonePF();
        assertEquals(original.size(), cloned.size());
        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.elementAt(i), cloned.elementAt(i));
        }
    }

    @Test
    public void testApplySubstitutionPFMethod() {
        String text = "p(x)";
        JSPredicateForm predicateForm = new JSPredicateForm(text);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("x", new JSTerm("y"));
        JSPredicateForm result = predicateForm.applySubstitutionPF(alpha);
        assertEquals(1, result.size());
        assertEquals("p(y)", result.elementAt(0).toString());
    }

    @Test
    public void testMatchesMethod() {
        String text1 = "p(x)";
        String text2 = "q(x)";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2);
        assertFalse(beta.fail());
        assertNull(beta.getFailureReason());
    }

    @Test
    public void testMatchesMethodWithFailure() {
        String text1 = "p(x)";
        String text2 = "q(y)";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2);
        assertTrue(beta.fail());
        assertEquals("Domain error: wrong number of terms in formula",
                beta.getFailureReason());
    }

    @Test
    public void testEqualsMethod() {
        String text1 = "p(x)";
        String text2 = "p(x)";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        assertTrue(pf1.equals(pf2));
    }

    @Test
    public void testEqualsMethodFalse() {
        String text1 = "p(x)";
        String text2 = "q(x)";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        assertFalse(pf1.equals(pf2));
    }

    @Test
    public void testStandarizerPredicateFormMethod() {
        String text = "not (p(x) or (q(y) and r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm standardized = jspf.standarizerPredicateForm();
        String expected = "(and (not (p x)) (or (not q y) r z))";
        assertEquals(expected, standardized.toStr().toString());
    }
}