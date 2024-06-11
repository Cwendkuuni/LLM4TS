package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorFromText() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        assertEquals("not", jspf.elementAt(0));
        JSPredicateForm not = jspf.elementAt(1);
        assertEquals("or", not.elementAt(0));
        JSPredicateForm or = not.elementAt(1);
        assertEquals("=", or.elementAt(0).toString());
        assertEquals(2, or.size());
        JSTerm term1 = or.elementAt(1);
        assertEquals(1, term1.size());
        assertEquals("x", term1.elementAt(0).toString());
        assertEquals(1, term1.elementAt(1).intValue());

        term1 = or.elementAt(2);
        assertEquals(1, term1.size());
        assertEquals("x", term1.elementAt(0).toString());
        assertEquals(2, term1.elementAt(1).intValue());
    }

    @Test
    public void testJSPredicateFormConstructorFromStreamTokenizer() {
        String text = "(not (or (= x 1) (= x 2)))";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
        assertEquals("not", jspf.elementAt(0));
        JSPredicateForm not = jspf.elementAt(1);
        assertEquals("or", not.elementAt(0));
        JSPredicateForm or = not.elementAt(1);
        assertEquals("=", or.elementAt(0).toString());
        assertEquals(2, or.size());
        JSTerm term1 = or.elementAt(1);
        assertEquals(1, term1.size());
        assertEquals("x", term1.elementAt(0).toString());
        assertEquals(1, term1.elementAt(1).intValue());

        term1 = or.elementAt(2);
        assertEquals(1, term1.size());
        assertEquals("x", term1.elementAt(0).toString());
        assertEquals(2, term1.elementAt(1).intValue());
    }

    @Test
    public void testJSPredicateFormSize() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        assertEquals(3, jspf.size());
    }

    @Test
    public void testJSPredicateFormPrint() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        jspf.print();
        assertEquals("(not (or (= x 1) (= x 2)))", JSPredicateForm.input);
    }

    @Test
    public void testJSPredicateFormToString() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        assertEquals("(not (or (= x 1) (= x 2)))", jspf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClonePF() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        JSPredicateForm cloned = jspf.clonePF();
        assertNotSame(jspf, cloned);
        assertEquals(jspf.size(), cloned.size());
        for (int i = 0; i < jspf.size(); i++) {
            JSTerm jsterm1 = jspf.elementAt(i);
            JSTerm jsterm2 = cloned.elementAt(i);
            assertEquals(jsterm1.size(), jsterm2.size());
            for (int j = 0; j < jsterm1.size(); j++) {
                assertEquals(jsterm1.elementAt(j), jsterm2.elementAt(j));
            }
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("x", 3);
        JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
        assertEquals("(not (or (= 3 1) (= 3 2)))", jspf2.toStr().toString());
    }

    @Test
    public void testJSPredicateFormMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= y 1) (= y 2)))");
        JSSubstitution beta = jspf1.matches(jspf2);
        assertNotNull(beta);
        assertTrue(beta.isEmpty());

        alpha.addBinding("x", "x");
        JSSubstitution gamma = jspf1.matches(jspf2, alpha);
        assertNotNull(gamma);
        assertTrue(gamma.isEmpty());

        alpha.addBinding("x", "y");
        JSSubstitution delta = jspf1.matches(jspf2, alpha);
        assertEquals(2, delta.size());
        assertEquals("y", delta.bindingAt(0).getName());
        assertEquals(1, delta.bindingAt(0).getValue().intValue());
        assertEquals("x", delta.bindingAt(1).getName());
        assertEquals(2, delta.bindingAt(1).getValue().intValue());
    }

    @Test
    public void testJSPredicateFormEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        JSPredicateForm jspf2 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        assertTrue(jspf1.equals(jspf2));

        JSTerm term1 = new JSTerm(new Term("=", new Atom("x"), new IntTerm(1)));
        JSTerm term2 = new JSTerm(new Term("=", new Atom("x"), new IntTerm(2)));
        JSPredicateForm jspf3 = new JSPredicateForm("(not (or " + term1.toStr() + " " + term2.toStr() + "))");
        assertFalse(jspf1.equals(jspf3));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
        JSPredicateForm standardized = jspf.standarizerPredicateForm();
        assertEquals("(not (or (= x 1) (= x 2)))", standardized.toStr().toString());
    }
}