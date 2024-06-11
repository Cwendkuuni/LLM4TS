package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JSPredicateFormTest {

    private StreamTokenizer tokenizer;
    private JSPredicateForm predicateForm;
    private JSTerm term;

    @Before
    public void setup() {
        tokenizer = new StreamTokenizer(new StringReader("(not (or (= x 1) (= x 2)))"));
        predicateForm = new JSPredicateForm(tokenizer);
        term = new JSTerm(tokenizer);
        term.makeEval(true);
    }

    @Test
    public void testJSPredicateFormInit() {
        assertEquals(6, predicateForm.size());
        assertEquals("not", predicateForm.get(0).toString());
        assertEquals("or", predicateForm.get(1).toString());
        assertEquals("=", predicateForm.get(2).toString());
        assertEquals("x", predicateForm.get(3).toString());
        assertEquals("1", predicateForm.get(4).toString());
        assertEquals("2", predicateForm.get(5).toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm clonedPredicateForm = predicateForm.clonePF();
        assertEquals(predicateForm, clonedPredicateForm);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution substitution = new JSSubstitution();
        substitution.put("x", "y");

        JSPredicateForm appliedSubstitutionPredicateForm = predicateForm.applySubstitutionPF(substitution);
        assertEquals(6, appliedSubstitutionPredicateForm.size());
        assertEquals("not", appliedSubstitutionPredicateForm.get(0).toString());
        assertEquals("or", appliedSubstitutionPredicateForm.get(1).toString());
        assertEquals("=", appliedSubstitutionPredicateForm.get(2).toString());
        assertEquals("y", appliedSubstitutionPredicateForm.get(3).toString());
        assertEquals("1", appliedSubstitutionPredicateForm.get(4).toString());
        assertEquals("2", appliedSubstitutionPredicateForm.get(5).toString());
    }

    @Test
    public void testMatches() {
        JSPredicateForm anotherPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 1) (= x 2))")));
        JSSubstitution matches = predicateForm.matches(anotherPredicateForm);
        assertFalse(matches.fail());
        assertEquals(0, matches.size());

        JSPredicateForm differentPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(or (= x 1) (= x 3))")));
        matches = predicateForm.matches(differentPredicateForm);
        assertTrue(matches.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm anotherPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (or (= x 1) (= x 2)))")));
        assertTrue(predicateForm.equals(anotherPredicateForm));

        JSPredicateForm differentPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x 1) (= x 2))")));
        assertFalse(predicateForm.equals(differentPredicateForm));

        assertNull(predicateForm.equals(null));
    }

    @Test
    public void testPrint() {
        predicateForm.print();
    }

    @Test
    public void testToString() {
        assertEquals("(not (or (= x 1) (= x 2)))", predicateForm.toStr().toString());
    }
}