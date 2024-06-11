package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstruction() {
        JSState jsState = new JSState();
        assertTrue(jsState.isEmpty());
        assertTrue(jsState.size() == 0);
    }

    @Test
    public void testJSStateWithTokenizerConstruction() {
        JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
        assertTrue(jsState.isEmpty());
        assertTrue(jsState.size() == 0);
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms expected = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));

        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));

        jsState.addElementsToState(s);

        JSListLogicalAtoms actual = jsState.asList();

        assertTrue(jsState.size() == 2);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testApplyOp_EmptyState() {
        JSState jsState = new JSState();
        JSOperator op = new JSOperator();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTate(new JSState(), addL, delL);

        JSTate actual = jsState.applyOp(op, new JSSubstitution(), addL, delL);

        assertTrue(actual.getState().isEmpty());
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testApplyOp_NonEmptyState() {
        JSState jsState = new JSState();
        JSOperator op = new JSOperator();
        JSListLogicalAtoms addL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p3"), new JSPredicateForm("p4")));
        JSListLogicalAtoms delL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        JSState expectedState = new JSState();
        expectedState.add(new JSPredicateForm("p1"));
        expectedState.add(new JSPredicateForm("p2"));
        JSTate expected = new JSTate(expectedState, addL, delL);

        JSListLogicalAtoms stateToModify = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));
        jsState.addElementsToState(stateToModify);

        JSTate actual = jsState.applyOp(op, new JSSubstitution(), addL, delL);

        JSListLogicalAtoms actualState = actual.getState();
        JSListLogicalAtoms expectedStateAsList = expected.getState().asList();

        assertTrue(actual.getState().size() == expectedState.getState().size());
        assertTrue(expectedStateAsList.equals(actualState));
    }

    @Test
    public void testSatisfies() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();
        JSSubstitution actual = jsState.satisfies(conds, alpha, axioms);

        assertNull(actual.getSubstitution());
        assertFalse(actual.isSuccess());
    }

    @Test
    public void testSatisfies_Satisfiable() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();
        expected.add(new JSSubstitutionEntry("x", "y"));

        JSSubstitution actual = jsState.satisfies(conds, alpha, axioms);

        assertTrue(actual.getSubstitution().equals(expected.getSubstitution()));
        assertTrue(actual.isSuccess());
    }

    @Test
    public void testSatisfiesAll() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expected = new JSListSubstitution(Arrays.asList(new JSSubstitution(), new JSSubstitution()));

        JSListSubstitution actual = jsState.satisfiesAll(conds, alpha, axioms);

        assertTrue(actual.size() == 2);
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(expected.get(i).getSubstitution().equals(actual.get(i).getSubstitution()));
        }
    }

    @Test
    public void testSatisfiesTAm() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));
        JSPredicateForm t = new JSPredicateForm("(p1 -> p2)");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution expected = new JSListSubstitution();
        expected.add(new JSSubstitutionEntry("x", "y"));

        JSListSubstitution actual = jsState.satisfiesTAm(t, alpha);

        assertTrue(actual.get(0).getSubstitution().equals(expected.get(0).getSubstitution()));
        assertTrue(actual.size() == 1);
    }

    @Test
    public void testSatisfiesTAm_NoMatch() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));
        JSPredicateForm t = new JSPredicateForm("(p1 <- p2)");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution actual = jsState.satisfiesTAm(t, alpha);

        assertTrue(actual.isEmpty());
    }
}