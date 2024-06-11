package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormCreationWithText() {
        String text = "not ( ( a = b ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("(", jspf.elementAt(1));
        assertEquals("( a = b ) ", jspf.elementAt(2));
    }

    @Test
    public void testJSPredicateFormCreationWithTokenizer() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("(", jspf.elementAt(1));
        assertEquals("( a = b ) ", jspf.elementAt(2));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        StreamTokenizer tokenizer = null;
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testJSPredicateFormInit() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.JSPredicateFormInit(tokenizer);
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("(", jspf.elementAt(1));
        assertEquals("( a = b ) ", jspf.elementAt(2));
    }

    @Test
    public void testJSPredicateFormPrint() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        jspf.print();
        assertTrue(JSUtil.getLastPrintedString().contains("not ( ( a = b ) ) "));
    }

    @Test
    public void testJSPredicateFormToString() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        String expected = "not( ( a = b ) )";
        String actual = jspf.toStr().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testJSPredicateFormClonePF() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf.size(), cloned.size());
        for (int i = 0; i < jspf.size(); i++) {
            if (jspf.elementAt(i) instanceof JSTerm) {
                JSTerm jsterm1 = (JSTerm) jspf.elementAt(i);
                JSTerm jsterm2 = (JSTerm) cloned.elementAt(i);
                assertEquals(jsterm1.toStr().toString(), jsterm2.toStr().toString());
            } else {
                assertEquals(jspf.elementAt(i), cloned.elementAt(i));
            }
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        String text = "not ( ( a = b ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = jspf.applySubstitutionPF(alpha);
        assertEquals(jspf.size(), beta.size());
        for (int i = 0; i < jspf.size(); i++) {
            if (jspf.elementAt(i) instanceof JSTerm) {
                JSTerm jsterm1 = (JSTerm) jspf.elementAt(i);
                JSTerm jsterm2 = (JSTerm) beta.elementAt(i);
                assertEquals(jsterm1.toStr().toString(), jsterm2.toStr().toString());
            }
        }
    }

    @Test
    public void testJSPredicateFormMatches() {
        String text1 = "( a = b )";
        String text2 = "( c = d )";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm p1 = new JSPredicateForm(tokenizer1);
        JSPredicateForm p2 = new JSPredicateForm(tokenizer2);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = p1.matches(p2, alpha);
        assertNull(beta);

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( c = d )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        beta = p1.matches(p2, alpha);
        assertFalse(beta.fail());
        assertEquals(0, beta.size());

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( a = c )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        beta = p1.matches(p2, alpha);
        assertTrue(beta.fail());

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( a = a )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        beta = p1.matches(p2, alpha);
        assertFalse(beta.fail());
        assertEquals(0, beta.size());

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( ( a = b ) )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        beta = p1.matches(p2, alpha);
        assertFalse(beta.fail());
        assertEquals(1, beta.size());
        assertEquals("not", beta.elementAt(0).toString());
        assertEquals("( a = b ) ", beta.elementAt(1).toString());

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "not ( c = d )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        beta = p1.matches(p2, alpha);
        assertTrue(beta.fail());
    }

    @Test
    public void testJSPredicateFormEquals() {
        String text1 = "( a = b )";
        String text2 = "( a = b )";
        StreamTokenizer tokenizer1 = new StreamTokenizer(new StringReader(text1));
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader(text2));
        JSPredicateForm p1 = new JSPredicateForm(tokenizer1);
        JSPredicateForm p2 = new JSPredicateForm(tokenizer2);
        assertTrue(p1.equals(p2));

        text1 = "( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( c = d )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertFalse(p1.equals(p2));

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "not ( a = b )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertTrue(p1.equals(p2));

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "( a = b )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertFalse(p1.equals(p2));

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "not ( a = c )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertFalse(p1.equals(p2));

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "not ( a = b ) not ( c = d )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertFalse(p1.equals(p2));

        text1 = "not ( a = b )";
        tokenizer1 = new StreamTokenizer(new StringReader(text1));
        p1 = new JSPredicateForm(tokenizer1);
        text2 = "not ( a = b ) not ( a = c )";
        tokenizer2 = new StreamTokenizer(new StringReader(text2));
        p2 = new JSPredicateForm(tokenizer2);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        String text = "not ( ( a = b ) not ( c = d ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        JSPredicateForm standardized = jspf.standarizerPredicateForm();
        String expected = "not( not( a = b ) not( c = d ) )";
        String actual = standardized.toStr().toString();
        assertEquals(expected, actual);
    }
}