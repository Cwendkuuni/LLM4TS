package org.apache.commons.cli; // Replace the package name with the actual package name of the JSPredicateForm class

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;
import java.util.Vector;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInitialization() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());

        String text = "not (x < 5)";
        pf = new JSPredicateForm(text);
        assertEquals(3, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals(new JSPredicateForm(new String[]{"x", "<", "5"}), pf.elementAt(1));
    }

    @Test
    public void testJSPredicateFormInitWithTokenizer() {
        String text = "not (x < 5)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals(3, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals(new JSPredicateForm(new String[]{"x", "<", "5"}), pf.elementAt(1));
    }

    @Test
    public void testJSPredicateFormPrint() {
        String text = "not (x < 5)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        JSUtil printUtil = new JSUtil();
        printUtil.setOutStream(out);
        pf.print();
        String expectedOutput = "(- not (x < 5)) ";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testJSPredicateFormToString() {
        String text = "not (x < 5)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);

        assertEquals("(- not (x < 5))", pf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClone() {
        String text = "not (x < 5)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);

        JSPredicateForm clonedPF = pf.clonePF();
        assertEquals(pf, clonedPF);
    }

    @Test
    public void testJSPredicateFormApplySubstitution() {
        String text1 = "x = y";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        JSTerm term1 = new JSTerm(tokenizer1);

        String text2 = "not (x < 5)";
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm pf = new JSPredicateForm(tokenizer2);

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElements(new String[]{"x", "y"});

        JSPredicateForm result = pf.applySubstitutionPF(alpha);

        assertEquals(3, result.size());
        assertEquals("not", result.elementAt(0));
        assertEquals(new JSPredicateForm(new String[]{"y", "<", "5"}), result.elementAt(1));
    }

    @Test
    public void testJSPredicateFormMatches() {
        String text1 = "x = y";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        JSTerm term1 = new JSTerm(tokenizer1);

        String text2 = "not (x < 5)";
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm pf = new JSPredicateForm(tokenizer2);

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElements(new String[]{"x", "y"});

        JSSubstitution beta = pf.matches(new JSPredicateForm(text1), alpha);

        assertNotNull(beta);
        assertEquals(0, beta.size());
    }

    @Test
    public void testJSPredicateFormMatchesFailure() {
        String text1 = "x = y";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        JSTerm term1 = new JSTerm(tokenizer1);

        String text2 = "not (x < 5)";
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm pf = new JSPredicateForm(tokenizer2);

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElements(new String[]{"x", "z"});

        JSSubstitution beta = pf.matches(new JSPredicateForm(text1), alpha);

        assertEquals(new JSSubstitution(), beta);
    }

    @Test
    public void testJSPredicateFormEquals() {
        String text1 = "x = y";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        JSTerm term1 = new JSTerm(tokenizer1);

        String text2 = "not (x < 5)";
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm pf1 = new JSPredicateForm(tokenizer1);
        JSPredicateForm pf2 = new JSPredicateForm(tokenizer2);

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElements(new String[]{"x", "y"});

        JSPredicateForm pf3 = pf1.applySubstitutionPF(alpha);
        JSPredicateForm pf4 = pf2.applySubstitutionPF(alpha);

        assertTrue(pf3.equals(pf4));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        String text = "not (x = y)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);

        JSPredicateForm standardizedPF = pf.standarizerPredicateForm();

        assertEquals(3, standardizedPF.size());
        assertEquals("not", standardizedPF.elementAt(0));
        assertEquals(new JSTerm("not", new JSTerm("=", new JSTerm("x"), new JSTerm("y"))), standardizedPF.elementAt(1));
    }
}