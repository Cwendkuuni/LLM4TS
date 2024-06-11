
package umd.cs.shop;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import static junit.framework.Assert.*;

public class JSPredicateFormTest {

    @BeforeClass
    public static void setUp() {
        // Initialize JSUtil and JSSubstitution static classes
        JSUtil.initialize();
        JSSubstitution.initialize();
    }

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormWithText() {
        String text = "not (x = y)";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals("!(x = y)", jspf.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithNullText() {
        String text = null;
        JSPredicateForm jspf = new JSPredicateForm(text);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithInvalidTextFormat() {
        String text = "not x = y)";
        JSPredicateForm jspf = null;
        try {
            jspf = new JSPredicateForm(text);
        } catch (JSParserError e) {
            // Expected exception
        }
        assertNotNull(jspf);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithNullStreamTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
    }

    @Test
    public void testJSPredicateFormWithStreamTokenizer() throws IOException {
        String text = "not (x = y)";
        StringReader reader = new StringReader(text);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        StringWriter sw = new StringWriter();
        jspf.print(sw);
        assertEquals("!(x = y)", sw.toString());
    }

    @Test
    public void testClonePF() {
        String text = "not (x = y)";
        JSPredicateForm original = new JSPredicateForm(text);
        JSPredicateForm cloned = original.clonePF();
        assertEquals(original.toStr().toString(), cloned.toStr().toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        String text1 = "not (x = y)";
        String text2 = "not (z = w)";
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("x", "z");
        alpha.assign("y", "w");
        JSPredicateForm original = new JSPredicateForm(text1);
        JSPredicateForm expected = new JSPredicateForm("not (z = w)");
        JSPredicateForm cloned = original.applySubstitutionPF(alpha);
        assertEquals(expected.toStr().toString(), cloned.toStr().toString());
    }

    @Test
    public void testMatches() {
        String text1 = "not (x = y)";
        String text2 = "not (y = x)";
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        JSSubstitution beta = pf1.matches(pf2);
        assertTrue(beta.isEmpty());

        alpha.assign("x", "a");
        alpha.assign("y", "b");
        beta = pf1.matches(pf2, alpha);
        assertEquals(2, beta.size());
        assertEquals("a", beta.getValue("a"));
        assertEquals("b", beta.getValue("b"));

        alpha.clear();
        alpha.assign("x", "x");
        alpha.assign("y", "y");
        beta = pf1.matches(pf1, alpha);
        assertTrue(beta.isEmpty());
    }

    @Test
    public void testEquals() {
        String text1 = "not (x = y)";
        String text2 = "not (x = y)";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        assertTrue(pf1.equals(pf2));

        text2 = "not (x = z)";
        assertFalse(pf1.equals(pf2));

        String text3 = "not (y = x)";
        assertFalse(pf1.equals(new JSPredicateForm(text3)));

        text3 = "not (x = y)";
        JSTerm t1 = new JSTerm("x");
        JSTerm t2 = new JSTerm("y");
        JSPredicateForm pf3 = new JSPredicateForm();
        pf3.addElement(t1);
        pf3.addElement(t2);
        assertFalse(pf1.equals(pf3));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String text = "not (x = y)";
        JSPredicateForm original = new JSPredicateForm(text);
        JSPredicateForm standardized = original.standarizerPredicateForm();
        String expectedText = "!(= x y)";
        assertEquals(expectedText, standardized.toStr().toString());
    }
}