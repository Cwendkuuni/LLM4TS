package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSState() {
        JSState state = new JSState();
        assertNull(state.getLabel());
        assertNotNull(state);
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));
        state.addElementsToState(list);

        List<JSPredicateForm> expected = new ArrayList<>(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        assertEquals(expected, state.asList());
    }

    @Test
    public void testApplyOp_whenOpAddLIsEmpty_returnsEmptyState() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        JSOperator op = new JSAdd();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTState(new JSState(), addL, delL);

        JSTState actual = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp_whenStateIsEmpty_returnsEmptyState() {
        JSState state = new JSState();
        JSOperator op = new JSAdd();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTate(new JSState(), addL, delL);

        JSTState actual = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp_whenStateContainsElementInAddL_returnsNewStateWithAddedElement() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2")));
        JSOperator op = new JSAdd();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p3"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTate(new JSState(new StreamTokenizer(new StringReader("p1 p2 p3"))), addL, delL);

        JSTState actual = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp_whenStateContainsElementInDelL_returnsNewStateWithoutRemovedElement() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2 p3")));
        JSOperator op = new JSAdd();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p3"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("p3"));
        JSTState expected = new JSTate(new JSState(new StreamTokenizer(new StringReader("p1 p2"))), addL, delL);

        JSTate actual = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp_whenStateDoesNotContainElementInAddL_returnsNewStateWithSameElements() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2 p3")));
        JSOperator op = new JSAdd();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p4"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTate(new JSState(new StreamTokenizer(new StringReader("p1 p2 p3"))), addL, delL);

        JSTate actual = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expected, actual);
    }

    @Test
    public void testSatisfies_whenConditionsAreSatisfied_returnsSubstitutionWithAddedElements() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2")));
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSPredicateForm("p3", "x1"));

        JSSubstitution actual = state.satisfies(conditions, alpha, axioms);

        assertEquals(expected, actual);
    }

    @Test
    public void testSatisfiesAll_whenConditionsAreSatisfiedByMultipleSubstitutions_returnsAllSubstitutions() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2")));
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution sub1 = new JSSubstitution();
        sub1.addElement(new JSPredicateForm("p3", "x1"));
        JSSubstitution sub2 = new JSSubstitution();
        sub2.addElement(new JSPredicateForm("p4", "x2"));
        List<JSSubstitution> satisfiers = Arrays.asList(sub1, sub2);

        JSListSubstitution expected = new JSListSubstitution();
        expected.addElement(sub1);
        expected.addElement(sub2);

        JSListSubstitution actual = state.satisfiesAll(conditions, alpha, axioms);

        assertEquals(expected, actual);
    }

    @Test
    public void testSatisfiesTAm_whenAtomsMatch_returnsListWithSubstitutions() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2 p3")));
        JSPredicateForm goal = new JSPredicateForm("p1(x1)", "x1");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSPredicateForm("p1(x1)", "x1"));

        JSListSubstitution actual = state.satisfiesTAm(goal, alpha);

        assertEquals(expected, actual);
    }

    @Test
    public void testSatisfiesTAm_whenNoAtomsMatch_returnsEmptyList() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("p1 p2 p3")));
        JSPredicateForm goal = new JSPredicateForm("p4(x1)", "x1");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution actual = state.satisfiesTAm(goal, alpha);

        assertTrue(actual.isEmpty());
    }
}