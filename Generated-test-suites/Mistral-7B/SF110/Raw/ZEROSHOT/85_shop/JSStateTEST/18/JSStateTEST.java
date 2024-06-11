package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTState;

public class JSStateTest {

    @Test
    public void testJSStateConstructor() {
        JSState jsState = new JSState();
        assertNotNull(jsState);
    }

    @Test
    public void testJSStateConstructorWithTokenizer() {
        JSState jsState = new JSState(new StreamTokenizer());
        assertNotNull(jsState);
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p(a)", null, null));
        s.add(new JSPredicateForm("q(b)", null, null));
        JSState jsState = new JSState();
        jsState.addElementsToState(s);

        List<JSPredicateForm> expected = new ArrayList<>(Arrays.asList(
                new JSPredicateForm("p(a)", null, null),
                new JSPredicateForm("q(b)", null, null)));
        List<JSPredicateForm> actual = new ArrayList<>(jsState.elementList());

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddElementsToStateWithDuplicateElement() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p(a)", null, null));
        JSState jsState = new JSState();
        jsState.addElementsToState(s);

        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        s2.add(new JSPredicateForm("p(a)", null, null));
        jsState.addElementsToState(s2);
    }

    @Test
    public void testApplyOpWithNullOperators() {
        JSState jsState = new JSState();
        assertNull(jsState.applyOp(null, null, null, null));
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator("op", new JSPredicateForm("p(x)", null, null), null);
        JSSubstitution alpha = new JSSubstitution();
        JSState jsState = new JSState();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.add(new JSPredicateForm("q(b)", null, null));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.add(new JSPredicateForm("r(c)", null, null));

        JSState expected = new JSState();
        expected.addElement(new JSPredicateForm("p(a)", null, null));
        expected.addElement(new JSPredicateForm("q(b)", null, null));

        JSState result = jsState.applyOp(op, alpha, addL, delL);

        List<JSPredicateForm> expectedElements = new ArrayList<>(Arrays.asList(
                new JSPredicateForm("p(a)", null, null),
                new JSPredicateForm("q(b)", null, null)));
        List<JSPredicateForm> resultElements = new ArrayList<>(result.elementList());

        assertEquals(expectedElements, resultElements);
    }

    @Test
    public void testSatisfiesWithEmptyConditionsAndEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSState jsState = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesWithEmptyConditionsAndNonEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p(a)", null, null));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesWithNonEmptyConditionsAndEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(a)", null, null));
        JSState jsState = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesWithNonEmptyConditionsAndNonEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(a)", null, null));
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("q(b)", null, null));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesAllWithEmptyConditions() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSState jsState = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesAllWithNonEmptyConditionsAndEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(a)", null, null));
        JSState jsState = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesAllWithNonEmptyConditionsAndNonEmptyState() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(a)", null, null));
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("q(b)", null, null));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmWithEmptyConditions() {
        JSPredicateForm t = new JSPredicateForm("p(x)", null, null);
        JSState jsState = new JSState();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmWithEmptyStateAndEmptyGoal() {
        JSPredicateForm t = new JSPredicateForm("p(x)", null, null);
        JSState jsState = new JSState();
        JSListSubstitution goal = new JSListSubstitution();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmWithEmptyStateAndNonEmptyGoal() {
        JSPredicateForm t = new JSPredicateForm("p(x)", null, null);
        JSState jsState = new JSState();
        JSListSubstitution goal = new JSListSubstitution();
        goal.add(new JSSubstitution());
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmWithNonEmptyStateAndEmptyGoal() {
        JSPredicateForm t = new JSPredicateForm("p(x)", null, null);
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p(a)", null, null));
        JSListSubstitution goal = new JSListSubstitution();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmWithNonEmptyStateAndNonEmptyGoal() {
        JSPredicateForm t = new JSPredicateForm("p(x)", null, null);
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p(a)", null, null));
        JSListSubstitution goal = new JSListSubstitution();
        goal.add(new JSSubstitution());
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertFalse(result.isEmpty());
    }
}