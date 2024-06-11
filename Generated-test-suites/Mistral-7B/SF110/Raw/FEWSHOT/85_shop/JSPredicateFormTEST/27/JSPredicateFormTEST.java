package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInitialization() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());

        pf = new JSPredicateForm(new StringReader("(p)"));
        assertEquals(1, pf.size());
        assertEquals("p", pf.elementAt(0));

        pf = new JSPredicateForm(new StringReader("(not (p))"));
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals("p", pf.elementAt(1));
    }

    @Test
    public void testJSPredicateFormInitWithStreamTokenizer() {
        String text = "(not (p))";
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0));
        assertEquals("p", pf.elementAt(1));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitFailingCase() {
        String text = "(not (p) not)";
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
    }

    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm(new StringReader("(p)"));
        pf.print();
        assertEquals("(p) ", pf.toStr().toString());

        pf = new JSPredicateForm(new StringReader("(not (p))"));
        pf.print();
        assertEquals("(not (p)) ", pf.toStr().toString());

        pf = new JSPredicateForm(new StringReader("(not (not p))"));
        pf.print();
        assertEquals("(not (not p)) ", pf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm original = new JSPredicateForm(new StringReader("(not (p))"));
        JSPredicateForm cloned = original.clonePF();

        assertEquals(original.size(), cloned.size());
        for (int i = 0; i < original.size(); i++) {
            JSTerm originalElement = (JSTerm) original.elementAt(i);
            JSTerm clonedElement = (JSTerm) cloned.elementAt(i);
            assertEquals(originalElement.toString(), clonedElement.toString());
        }
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm pf = new JSPredicateForm(new StringReader("(p)"));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf.applySubstitutionPF(alpha);

        assertEquals(pf.size(), beta.size());
        for (int i = 0; i < pf.size(); i++) {
            JSTerm element = (JSTerm) pf.elementAt(i);
            JSTerm substitutedElement = (JSTerm) beta.elementAt(i);
            assertEquals(element.toString(), substitutedElement.toString());
        }

        pf = new JSPredicateForm(new StringReader("(not (p))"));
        JSPredicateForm pf2 = new JSPredicateForm(new StringReader("(q)"));
        alpha.addElement(new TermSubstitution("p", "q"));
        beta = pf.applySubstitutionPF(alpha);

        assertEquals(pf.size(), beta.size());
        assertEquals("q", beta.elementAt(0));
        for (int i = 1; i < pf.size(); i++) {
            JSTerm element = (JSTerm) pf.elementAt(i);
            JSTerm substitutedElement = (JSTerm) beta.elementAt(i);
            assertEquals(element.toString(), substitutedElement.toString());
        }
    }

    @Test
    public void testMatches() {
        JSPredicateForm pf1 = new JSPredicateForm(new StringReader("(p)"));
        JSPredicateForm pf2 = new JSPredicateForm(new StringReader("(q)"));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2, alpha);
        assertNull(beta);

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = new JSPredicateForm(new StringReader("(p)"));
        beta = pf1.matches(pf2, alpha);
        assertNull(beta);

        pf1 = new JSPredicateForm(new StringReader("(not (p))"));
        pf2 = new JSPredicateForm(new StringReader("(not (p))"));
        beta = pf1.matches(pf2, alpha);
        assertNull(beta);

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = new JSPredicateForm(new StringReader("(not (p))"));
        beta = pf1.matches(pf2, alpha);
        assertFalse(beta.isFailure());
        assertTrue(beta.isEmpty());

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = new JSPredicateForm(new StringReader("(p q)"));
        beta = pf1.matches(pf2, alpha);
        assertFalse(beta.isFailure());
        assertNull(beta.getFirstElement());
        assertTrue(beta.isEmpty());

        pf1 = new JSPredicateForm(new StringReader("(not (p))"));
        pf2 = new JSPredicateForm(new StringReader("(p)"));
        beta = pf1.matches(pf2, alpha);
        assertFalse(beta.isFailure());
        assertTrue(beta.isEmpty());

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = new JSPredicateForm(new StringReader("(p q)"));
        alpha.addElement(new TermSubstitution("p", "q"));
        beta = pf1.matches(pf2, alpha);
        assertEquals(1, beta.size());
        assertEquals("q", beta.getFirstElement());

        pf1 = new JSPredicateForm(new StringReader("(not (p))"));
        pf2 = new JSPredicateForm(new StringReader("(p)"));
        alpha.addElement(new TermSubstitution("p", "not (p)"));
        beta = pf1.matches(pf2, alpha);
        assertFalse(beta.isFailure());
        assertEquals(1, beta.size());
        assertEquals("not (p)", beta.getFirstElement());
    }

    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm(new StringReader("(p)"));
        JSPredicateForm pf2 = new JSPredicateForm(new StringReader("(p)"));
        assertTrue(pf1.equals(pf2));

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = new JSPredicateForm(new StringReader("(not (p))"));
        assertFalse(pf1.equals(pf2));

        pf1 = new JSPredicateForm(new StringReader("(p)"));
        pf2 = null;
        assertFalse(pf1.equals(pf2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm(new StringReader("(not (p))"));
        JSPredicateForm standardized = pf.standarizerPredicateForm();
        assertEquals(2, standardized.size());
        assertEquals("not", standardized.elementAt(0));
        assertEquals("p", standardized.elementAt(1));
    }
}