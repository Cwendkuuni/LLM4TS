package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState state = new JSState();
        assertEquals(0, state.size());
        assertNull(state.getLabel());
    }

    @Test
    public void testJSStateWithLabel() {
        String label = "state1";
        JSState state = new JSState(label);
        assertEquals(label, state.getLabel());
        assertEquals(0, state.size());
    }

    @Test
    public void testJSStateWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSState state = new JSState(tokenizer);
        assertNull(state.getLabel());
        assertEquals(0, state.size());
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSPredicateForm predicate1 = new JSPredicateForm("p1", new ArrayList<>());
        JSPredicateForm predicate2 = new JSPredicateForm("p2", new ArrayList<>());
        state.addElementsToState(new JSListLogicalAtoms(predicate1, predicate2));
        assertEquals(2, state.size());
        assertContains(state, predicate1);
        assertContains(state, predicate2);
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState expected = new JSState();
        JSState result = state.apply(plan);
        assertEquals(expected, result);
        JSUtil.flag20("JSState.apply(JSPlan) not implemented yet");
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator("op", null, null, null);
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        JSState expectedResult = new JSState();
        JSState result = state.applyOp(op, new JSSubstitution(), addList, delList);
        assertEquals(expectedResult, result);
        JSUtil.flag20("JSState.applyOp(JSOperator, JSSubstitution, JSListLogicalAtoms, JSListLogicalAtoms) not implemented yet");
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution result = state.satisfies(conditions, alpha, axioms);
        assertEquals(alpha, result);
        JSUtil.flag20("JSState.satisfies(JSListLogicalAtoms, JSSubstitution, JSListAxioms) not implemented yet");
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = state.satisfiesAll(conditions, alpha, axioms);
        assertTrue(result.isEmpty());
        JSUtil.flag20("JSState.satisfiesAll(JSListLogicalAtoms, JSSubstitution, JSListAxioms) not implemented yet");
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm goal = new JSPredicateForm("goal", new ArrayList<>());
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = state.satisfiesTAm(goal, alpha);
        assertTrue(result.isEmpty());
        JSUtil.flag20("JSState.satisfiesTAm(JSPredicateForm, JSSubstitution) not implemented yet");
    }

    @Ignore("Not implemented and likely not testable in isolation")
    public void testContains() {
        JSState state = new JSState();
        JSPredicateForm predicate = new JSPredicateForm("p", new ArrayList<>());
        state.addElement(predicate);
        assertTrue(state.contains(predicate));
    }

    private boolean assertContains(JSState state, JSPredicateForm predicate) {
        for (JSPredicateForm el : state) {
            if (el.equals(predicate)) {
                return true;
            }
        }
        fail("State does not contain the expected predicate.");
        return false;
    }
}