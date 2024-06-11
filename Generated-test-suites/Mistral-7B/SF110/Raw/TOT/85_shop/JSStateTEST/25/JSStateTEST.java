package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms otherList = new JSListLogicalAtoms();
        otherList.addElement(new JSPredicateForm("p1"));
        state.addElementsToState(otherList);
        assertTrue(state.contains(new JSPredicateForm("p1")));
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("p1"));
        delList.addElement(new JSPredicateForm("p2"));
        JSOperator op = new JSAddOperator("+");
        JSState result = state.applyOp(op, new JSSubstitution(), addList, delList);
        assertTrue(result.contains(new JSPredicateForm("p1")));
        assertFalse(result.contains(new JSPredicateForm("p2")));
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = state.satisfies(conditions, alpha, new JSListAxioms());
        assertTrue(result.size() > 0);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1"));
        conds.addElement(new JSPredicateForm("p2"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        assertTrue(answers.size() > 0);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSState expected = new JSState();
        expected.addElement(new JSPredicateForm("p1"));
        JSPredicateForm condition = new JSPredicateForm("p1");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = state.satisfiesTAm(condition, alpha);
        assertEquals(expected, state);
    }

    @Test
    public void testEmptyStateApplyOp() {
        JSState state = new JSState();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        JSOperator op = new JSAddOperator("+");
        JSState result = state.applyOp(op, new JSSubstitution(), addList, delList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyStateApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState result = state.apply(plan);
        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyOpNullAddList() {
        JSState state = new JSState();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        JSOperator op = new JSAddOperator("+");
        JSState result = state.applyOp(op, new JSSubstitution(), null, delList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyOpNullDelList() {
        JSState state = new JSState();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSOperator op = new JSAddOperator("+");
        JSState result = state.applyOp(op, new JSSubstitution(), addList, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyOpNullPlan() {
        JSState state = new JSState();
        JSOperator op = new JSAddOperator("+");
        JSState result = state.applyOp(op, null, new JSListLogicalAtoms(), new JSListLogicalAtoms());
    }

    @Test
    public void testApplyOpWithSameElements() {
        JSState state1 = new JSState();
        JSState state2 = new JSState();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("p1"));
        JSOperator op = new JSAddOperator("+");
        JSState result = state1.applyOp(op, new JSSubstitution(), addList, new JSListLogicalAtoms());
        state2.addElement(new JSPredicateForm("p1"));
        assertTrue(result.equals(state2));
    }
}