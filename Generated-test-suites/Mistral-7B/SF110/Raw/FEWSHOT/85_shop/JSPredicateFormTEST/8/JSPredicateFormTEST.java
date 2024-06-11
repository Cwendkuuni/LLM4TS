package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSJshopVars;
import umd.cs.shop.StreamTokenizer;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());
    }

    @Test
    public void testJSPredicateFormInitializationWithNullText() {
        JSPredicateForm pf = new JSPredicateForm();
        assertNull(pf.elementAt(0));
        assertEquals(0, pf.size());
    }

    @Test
    public void testJSPredicateFormInitializationWithText() {
        String text = "not ( ( a = b ) and ( c = d ) )";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(3, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals(1, pf.elementAt(1).size());
        JSTerm t1 = pf.elementAt(1).elementAt(0);
        assertEquals("(", t1.elementAt(0));
        assertEquals("a", t1.elementAt(1).elementAt(0));
        assertEquals("=", t1.elementAt(1).elementAt(1).elementAt(0));
        assertEquals("b", t1.elementAt(1).elementAt(1).elementAt(1));
        assertEquals(")", t1.elementAt(1).size());
        JSTerm t2 = pf.elementAt(2).elementAt(0);
        assertEquals("and", t2.elementAt(0));
        assertEquals(2, t2.size());
        JSTerm t3 = t2.elementAt(1);
        assertEquals("(", t3.elementAt(0));
        assertEquals("c", t3.elementAt(1).elementAt(0));
        assertEquals("=", t3.elementAt(1).elementAt(1).elementAt(0));
        assertEquals("d", t3.elementAt(1).elementAt(1).elementAt(1));
        assertEquals(")", t3.size());
    }

    @Test
    public void testJSPredicateFormInitializationWithStreamTokenizer() {
        String text = "not ( ( a = b ) and ( c = d ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals(3, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals(1, pf.elementAt(1).size());
        JSTerm t1 = pf.elementAt(1).elementAt(0);
        assertEquals("(", t1.elementAt(0));
        assertEquals("a", t1.elementAt(1).elementAt(0));
        assertEquals("=", t1.elementAt(1).elementAt(1).elementAt(0));
        assertEquals("b", t1.elementAt(1).elementAt(1).elementAt(1));
        assertEquals(")", t1.size());
        JSTerm t2 = pf.elementAt(2).elementAt(0);
        assertEquals("and", t2.elementAt(0));
        assertEquals(2, t2.size());
        JSTerm t3 = t2.elementAt(1);
        assertEquals("(", t3.elementAt(0));
        assertEquals("c", t3.elementAt(1).elementAt(0));
        assertEquals("=", t3.elementAt(1).elementAt(1).elementAt(0));
        assertEquals("d", t3.elementAt(1).elementAt(1).elementAt(1));
        assertEquals(")", t3.size());
    }

    @Test
    public void testJSPredicateFormPrint() {
        String text = "not ( ( a = b ) and ( c = d ) )";
        JSPredicateForm pf = new JSPredicateForm(text);
        String expected = "not ( a = b and c = d )";
        assertEquals(expected, pf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClone() {
        String text = "not ( ( a = b ) and ( c = d ) )";
        JSPredicateForm pf1 = new JSPredicateForm(text);
        JSPredicateForm pf2 = pf1.clonePF();
        assertEquals(pf1.size(), pf2.size());
        for (int i = 0; i < pf1.size(); i++) {
            JSTerm t1 = pf1.elementAt(i);
            JSTerm t2 = pf2.elementAt(i);
            assertEquals(t1.size(), t2.size());
            for (int j = 0; j < t1.size(); j++) {
                assertEquals(t1.elementAt(j), t2.elementAt(j));
            }
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitution() {
        String text1 = "not ( a = b )";
        String text2 = "not ( c = d )";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSTerm t1 = new JSTerm("a", "=", new JSTerm("b"));
        JSPredicateForm pf2 = new JSPredicateForm();
        pf2.addElement(pf1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElementPair("a", "c");
        alpha.addElementPair("b", "d");
        JSPredicateForm pf3 = pf1.applySubstitutionPF(alpha);
        assertEquals(pf2.size(), pf3.size());
        for (int i = 0; i < pf2.size(); i++) {
            JSTerm t1 = pf2.elementAt(i);
            JSTerm t2 = pf3.elementAt(i);
            assertEquals(t1.size(), t2.size());
            for (int j = 0; j < t1.size(); j++) {
                JSTerm ti = t1.elementAt(j);
                JSTerm oti = t2.elementAt(j);
                assertEquals(ti.size(), oti.size());
                for (int k = 0; k < ti.size(); k++) {
                    assertEquals(ti.elementAt(k), oti.elementAt(k));
                }
            }
        }
    }

    @Test
    public void testJSSubstitutionMatches() {
        String text1 = "a = b";
        String text2 = "c = d";
        JSTerm t1 = new JSTerm("a", "=", new JSTerm("b"));
        JSTerm t2 = new JSTerm("c", "=", new JSTerm("d"));
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElementPair("a", "c");
        alpha.addElementPair("b", "d");
        JSSubstitution beta = t1.matches(t2, alpha);
        assertTrue(beta.size() > 0);
        assertEquals("a", beta.elementAt(0).getKey());
        assertEquals("c", beta.elementAt(0).getValue());
        assertEquals("b", beta.elementAt(1).getKey());
        assertEquals("d", beta.elementAt(1).getValue());
    }

    @Test
    public void testJSPredicateFormMatches() {
        String text1 = "not ( a = b )";
        String text2 = "not ( c = d )";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2, alpha);
        assertNull(beta);

        alpha.addElementPair("a", "c");
        alpha.addElementPair("b", "d");

        JSTerm t1 = new JSTerm("a", "=", new JSTerm("b"));
        JSTerm t2 = new JSTerm("c", "=", new JSTerm("d"));
        JSPredicateForm pf3 = new JSPredicateForm();
        pf3.addElement(t1);
        pf3.addElement(new JSTerm("and"));
        pf3.addElement(t2);
        JSPredicateForm pf4 = pf1.applySubstitutionPF(alpha);
        JSPredicateForm pf5 = pf4.applySubstitutionPF(new JSSubstitution()); // identity substitution
        JSPredicateForm pf6 = pf2.applySubstitutionPF(alpha);
        beta = pf5.matches(pf6, new JSSubstitution());
        assertTrue(beta.size() > 0);
        assertEquals("a", beta.elementAt(0).getKey());
        assertEquals("c", beta.elementAt(0).getValue());
        assertEquals("b", beta.elementAt(1).getKey());
        assertEquals("d", beta.elementAt(1).getValue());
    }

    @Test
    public void testJSPredicateFormEquals() {
        String text1 = "not ( a = b )";
        String text2 = "not ( a = b )";
        JSPredicateForm pf1 = new JSPredicateForm(text1);
        JSPredicateForm pf2 = new JSPredicateForm(text1);
        assertTrue(pf1.equals(pf2));

        text2 = "not ( a = b ) and c > 0";
        JSPredicateForm pf3 = new JSPredicateForm(text1);
        JSPredicateForm pf4 = new JSPredicateForm(text2);
        assertFalse(pf1.equals(pf4));

        text1 = "not ( a = b )";
        text2 = "not ( b = a )";
        JSPredicateForm pf5 = new JSPredicateForm(text1);
        JSPredicateForm pf6 = new JSPredicateForm(text2);
        assertFalse(pf5.equals(pf6));

        text1 = "not ( a = b )";
        JSTerm t1 = new JSTerm("a", "=", new JSTerm("b"));
        JSTerm t2 = new JSTerm("b", "=", new JSTerm("a"));
        JSPredicateForm pf7 = new JSPredicateForm();
        pf7.addElement(t1);
        JSPredicateForm pf8 = new JSPredicateForm();
        pf8.addElement(t2);
        JSPredicateForm pf9 = new JSPredicateForm();
        pf9.addElement(new JSTerm("not"));
        pf9.addElement(pf7);
        pf9.addElement(pf8);
        assertTrue(pf5.equals(pf9));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        String text = "not ( ( a = b ) and ( c = d ) )";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm np = pf.standarizerPredicateForm();
        String expected = "not ( a != b and c = d )";
        assertEquals(expected, np.toStr().toString());
    }
}