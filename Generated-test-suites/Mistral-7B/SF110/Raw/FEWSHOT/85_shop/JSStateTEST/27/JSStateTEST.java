package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState state = new JSState();
        assertTrue(state.isEmpty());
        assertEquals(0, state.size());
    }

    @Test
    public void testJSStateWithTokenizerConstructor() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        assertTrue(state.isEmpty());
        assertEquals(0, state.size());
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        List<JSPredicateForm> list = Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
        state.addElementsToState(new JSListLogicalAtoms(list));
        List<JSPredicateForm> expected = Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
        assertEquals(expected, state.toList());
    }

    @Test
    public void testContains() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        assertTrue(state.contains(new JSPredicateForm("p1")));
        assertFalse(state.contains(new JSPredicateForm("p2")));
    }

    @Test
    public void testInsertElementAt() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.insertElementAt(new JSPredicateForm("p2"), 0);
        List<JSPredicateForm> expected = Arrays.asList(new JSPredicateForm("p2"), new JSPredicateForm("p1"));
        assertEquals(expected, state.toList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFailAddingElementToFullState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));
        state.addElement(new JSPredicateForm("p3"));
        state.addElement(new JSPredicateForm("p4")); // Should throw an UnsupportedOperationException
    }

    @Test
    public void testSize() {
        JSState state = new JSState();
        assertEquals(0, state.size());
        state.addElement(new JSPredicateForm("p1"));
        assertEquals(1, state.size());
        state.addElementsToState(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p2"), new JSPredicateForm("p3"))));
        assertEquals(4, state.size());
    }

    @Test
    public void testIsEmpty() {
        JSState state = new JSState();
        assertTrue(state.isEmpty());
        state.addElement(new JSPredicateForm("p1"));
        assertFalse(state.isEmpty());
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        assertNull(state.apply(plan));
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p1"));
        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("p2"));
        JSOperator op = new JSOperator("op");
        JSListLogicalAtoms addL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p3"), new JSPredicateForm("p4")));
        JSListLogicalAtoms delL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
        JSState result = state1.applyOp(op, new JSSubstitution(), addL, delL);
        List<JSPredicateForm> expected = Arrays.asList(new JSPredicateForm("p2"), new JSPredicateForm("p4"));
        assertEquals(expected, result.toList());
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertTrue(answer.isEmpty());

        state.addElement(new JSPredicateForm("p2"));
        answer = state.satisfies(conds, alpha, new JSListAxioms());
        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSSubstitutionElement("p1", "p2"));
        assertEquals(expected, answer);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        assertTrue(answers.isEmpty());

        state.addElement(new JSPredicateForm("p2"));
        answers = state.satisfiesAll(conds, alpha, axioms);
        List<JSSubstitution> expected = Arrays.asList(new JSSubstitution(), new JSSubstitution());
        expected.get(0).addElement(new JSSubstitutionElement("p1", "p2"));
        expected.get(1).addElement(new JSSubstitutionElement("p2", "p2"));
        assertEquals(expected, answers.toList());
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));
        JSPredicateForm t = new JSPredicateForm("p(x) :- p1(x), p2(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        assertTrue(answers.isEmpty());

        state.addElement(new JSPredicateForm("p3"));
        answers = state.satisfiesTAm(t, alpha);
        List<JSSubstitution> expected = Arrays.asList(new JSSubstitution(), new JSSubstitution());
        expected.get(0).addElement(new JSSubstitutionElement("p1", "p3"));
        expected.get(0).addElement(new JSSubstitutionElement("p2", "p3"));
        expected.get(1).addElement(new JSSubstitutionElement("p3", "p3"));
        assertEquals(expected, answers.toList());
    }
}