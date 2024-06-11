package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSJshopVars;
import umd.cs.shop.StreamTokenizer;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormEmpty() {
        final JSPredicateForm jspf = new JSPredicateForm();
        assertTrue(jspf.size() == 0);
    }

    @Test
    public void testJSPredicateFormInitWithText() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(7, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("and", jspf.elementAt(1));
        assertEquals("or", jspf.elementAt(2));
        assertEquals("not", jspf.elementAt(3));
        assertEquals("a", jspf.elementAt(4));
        assertEquals("b", jspf.elementAt(5));
        assertEquals("c", jspf.elementAt(6));
    }

    @Test
    public void testJSPredicateFormInitWithTokenizer() {
        final String text = "not (and (or (not a) b) c)";
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        final JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(7, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("and", jspf.elementAt(1));
        assertEquals("or", jspf.elementAt(2));
        assertEquals("not", jspf.elementAt(3));
        assertEquals("a", jspf.elementAt(4));
        assertEquals("b", jspf.elementAt(5));
        assertEquals("c", jspf.elementAt(6));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidText() {
        final String text = "not (and (or (not a b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
    }

    @Test
    public void testJSPredicateFormPrint() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        jspf.print();
        assertEquals("(not (and (or (not a) b) c))", JSUtil.readLine());
    }

    @Test
    public void testJSPredicateFormToString() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        final String expected = "(not (and (or (not a) b) c))";
        assertEquals(expected, jspf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClonePF() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        final JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf.size(), cloned.size());
        for (int i = 0; i < jspf.size(); i++) {
            final Object e1 = jspf.elementAt(i);
            final Object e2 = cloned.elementAt(i);
            if (e1 instanceof String) {
                assertEquals(e1, e2);
            } else {
                assertEquals(e1.getClass(), e2.getClass());
                assertEquals(e1, ((JSTerm) e2).cloneT());
            }
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        final String text1 = "not (and (or (not a) b) c)";
        final String text2 = "not (and (or (not b) a) c)";
        final JSSubstitution alpha = new JSSubstitution();
        alpha.addMapping("a", "b");
        final JSPredicateForm jspf1 = new JSPredicateForm(text1);
        final JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
        assertEquals(text2, jspf2.toStr().toString());
    }

    @Test
    public void testJSPredicateFormMatches() {
        final String text1 = "not (and (or (not a) b) c)";
        final String text2 = "not (and (or (not b) a) c)";
        final String text3 = "not (and (or (not a) d) c)";
        final JSSubstitution alpha = new JSSubstitution();
        alpha.addMapping("a", "b");
        final JSPredicateForm jspf1 = new JSPredicateForm(text1);
        final JSPredicateForm jspf2 = new JSPredicateForm(text2);
        final JSSubstitution beta = jspf1.matches(jspf2, alpha);
        assertTrue(beta.isEmpty());

        final JSPredicateForm jspf3 = new JSPredicateForm(text1);
        final JSPredicateForm jspf4 = new JSPredicateForm(text3);
        final JSSubstitution gamma = new JSSubstitution();
        gamma.addMapping("a", "d");
        final JSSubstitution beta2 = jspf1.matches(jspf4, gamma);
        assertNull(beta2);
    }

    @Test
    public void testJSPredicateFormEquals() {
        final String text1 = "not (and (or (not a) b) c)";
        final String text2 = "not (and (or (not b) a) c)";
        final JSPredicateForm jspf1 = new JSPredicateForm(text1);
        final JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertFalse(jspf1.equals(jspf2));

        final String text3 = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf3 = new JSPredicateForm(text3);
        assertTrue(jspf1.equals(jspf3));

        final String text4 = "not (and (or (not a) b) d)";
        final JSPredicateForm jspf4 = new JSPredicateForm(text4);
        assertFalse(jspf1.equals(jspf4));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        final JSPredicateForm standard = jspf.standarizerPredicateForm();
        assertEquals("(not (and (or (not a) b) c))", standard.toStr().toString());
    }

    @Test
    public void testJSPredicateFormListMethods() {
        final String text = "not (and (or (not a) b) c)";
        final JSPredicateForm jspf = new JSPredicateForm(text);
        final List<Object> list = new ArrayList<>();
        for (Object o : jspf) {
            list.add(o);
        }
        assertEquals(7, list.size());
        assertEquals("not", list.get(0));
        assertEquals("and", list.get(1));
        assertEquals("or", list.get(2));
        assertEquals("not", list.get(3));
        assertEquals("a", list.get(4));
        assertEquals("b", list.get(5));
        assertEquals("c", list.get(6));
    }
}