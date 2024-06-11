package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormEmpty() {
        JSPredicateForm pf = new JSPredicateForm();
        assertTrue(pf.isEmpty());
        assertEquals("", pf.toString());
    }

    @Test
    public void testJSPredicateFormInit() {
        String text = "not ( and ( x1 '=' 3 ) ( x2 '=' 4 ) )";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(4, pf.size());
        assertEquals("not", pf.get(0).toString());
        assertEquals("and", pf.get(1).toString());
        assertEquals("x1", pf.get(2).toString());
        assertEquals("=", pf.get(3).toString());
        assertEquals(3, pf.get(4).toString());

        String text2 = " x1 '=' 3 ";
        JSPredicateForm pf2 = new JSPredicateForm(text2);
        assertEquals(2, pf2.size());
        assertEquals("x1", pf2.get(0).toString());
        assertEquals("=", pf2.get(1).toString());
        assertEquals(3, pf2.get(2).toString());
    }

    @Test
    public void testJSPredicateFormInitError() {
        String text = "not ( x1 '=' 3 ) )";
        try {
            JSPredicateForm pf = new JSPredicateForm(text);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        JSPredicateForm predicateForm = new JSPredicateForm("x1 = 3");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("x1", new JSTerm("y1"));

        JSPredicateForm result = predicateForm.applySubstitutionPF(alpha);
        assertEquals("y1 = 3", result.toString());
    }

    @Test
    public void testJSPredicateFormMatches() {
        JSPredicateForm predicateForm = new JSPredicateForm("x1 = x2");
        JSPredicateForm term = new JSPredicateForm("x1 = 3");

        JSSubstitution substitution = predicateForm.matches(term);
        assertFalse(substitution.isFailure());
        assertEquals(2, substitution.size());
        assertEquals("x1", substitution.getKeyAt(0).toString());
        assertEquals("3", substitution.getValueAt(0).toString());
        assertEquals("x2", substitution.getKeyAt(1).toString());
    }

    @Test
    public void testJSPredicateFormMatchesFailure() {
        JSPredicateForm predicateForm = new JSPredicateForm("x1 = 3");
        JSPredicateForm term = new JSPredicateForm("x1 = 4");

        JSSubstitution substitution = predicateForm.matches(term);
        assertTrue(substitution.isFailure());
    }

    @Test
    public void testJSPredicateFormEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("x1 = 3");
        JSPredicateForm pf2 = new JSPredicateForm("x1 = 3");
        JSPredicateForm pf3 = new JSPredicateForm("x1 = 4");

        assertEquals(pf1, pf1);
        assertEquals(pf1, pf2);
        assertFalse(pf1.equals(pf3));
    }

    @Test
    public void testJSPredicateFormToString() {
        JSPredicateForm predicateForm = new JSPredicateForm("not ( and ( x1 '=' 3 ) ( x2 '=' 4 ) )");
        assertEquals("not ( and ( x1 = 3 ) ( x2 = 4 ) )", predicateForm.toString());
    }

    @Test
    public void testJSPredicateFormClonePF() {
        JSPredicateForm predicateForm = new JSPredicateForm("not ( and ( x1 '=' 3 ) ( x2 '=' 4 ) )");
        JSPredicateForm cloned = predicateForm.clonePF();

        assertEquals(predicateForm, cloned);
    }

    @Test
    public void testJSPredicateFormPrint() {
        JSPredicateForm predicateForm = new JSPredicateForm("not ( and ( x1 '=' 3 ) ( x2 '=' 4 ) )");
        predicateForm.print();

        assertEquals("(not (and (x1 = 3) (x2 = 4)))", JSUtil.readLine());
    }

}