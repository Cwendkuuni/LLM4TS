package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmpty() {
        JSState state = new JSState();
        assertTrue("An empty JSState should have no elements", state.size() == 0);
    }

    @Test
    public void testJSStateAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));
        state.addElementsToState(s);
        assertEquals("An empty state should not contain the added elements initially", state.size(), 0);
        assertEquals("The size of the state after adding elements should be equal to the size of the input list", state.size(), s.size());
        for (short i = 0; i < s.size(); ++i) {
            JSPredicateForm el = state.elementAt(i);
            assertSame("The elements in the state should be the same as the elements in the input list", el, s.elementAt(i));
        }
    }

    @Test
    public void testJSStateApplyOp() {
        JSState state1 = new JSState();
        JSOperator op = new JSOperator("op", Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")), Arrays.asList(new JSPredicateForm("q1"), new JSPredicateForm("q2")));
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.add(new JSPredicateForm("r1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.add(new JSPredicateForm("p1"));

        JSState state2 = state1.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals("The resulting state after applying an empty operator should be the same as the original state", state1, state2);

        state1.addElementsToState(Arrays.asList(new JSPredicateForm("p3"), new JSPredicateForm("p4")));

        JSState state3 = state1.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals("The size of the state after applying an operator should be equal to the size of the original state minus the size of the deleted elements plus the size of the added elements", state3.size(), state1.size() - delL.size() + addL.size());
        for (short i = 0; i < state3.size(); ++i) {
            JSPredicateForm el = state3.elementAt(i);
            if (delL.contains(el)) {
                assertFalse("The deleted elements should not be present in the resulting state", true);
            }
            if (addL.contains(el)) {
                assertTrue("The added elements should be present in the resulting state", true);
            }
            if (!delL.contains(el) && !addL.contains(el)) {
                assertSame("The elements present in both the original and the resulting state should be the same", el, state1.elementAt(i));
            }
        }
    }

    @Test
    public void testJSStateSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        conds.add(new JSPredicateForm("p1", Arrays.asList("x", "y"), Arrays.asList("z")));
        state.addElementsToState(Arrays.asList(new JSPredicateForm("p1", Arrays.asList("a", "b"), Arrays.asList("c")), new JSPredicateForm("p2", Arrays.asList("x", "y"), Arrays.asList("z"))));

        JSSubstitution answer = state.satisfies(conds, alpha, axioms);

        assertEquals("The answer should be the input substitution when the state satisfies the conditions", answer, alpha);

        conds.add(new JSPredicateForm("p3", Arrays.asList("x", "y"), Arrays.asList("z")));

        answer = state.satisfies(conds, alpha, axioms);

        assertNull("The answer should be null when the state does not satisfy the conditions", answer);

    }

    @Test
    public void testJSStateSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        conds.add(new JSPredicateForm("p1", Arrays.asList("x", "y"), Arrays.asList("z")));
        state.addElementsToState(Arrays.asList(new JSPredicateForm("p1", Arrays.asList("a", "b"), Arrays.asList("c")), new JSPredicateForm("p2", Arrays.asList("x", "y"), Arrays.asList("z"))));

        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);

        assertEquals("The number of answers should be equal to the number of satisfiers", answers.size(), axioms.TheoremProver(conds, state, alpha, false).size());

        for (int i = 0; i < answers.size(); ++i) {
            JSSubstitution beta = answers.elementAt(i);
            assertEquals("The elements in the answer substitutions should be the same as the elements in the input substitution plus the satisfiers", beta, new JSSubstitution(alpha).addElements(axioms.TheoremProver(conds, state, alpha, false).elementAt(i)));
        }

        conds.add(new JSPredicateForm("p3", Arrays.asList("x", "y"), Arrays.asList("z")));

        answers = state.satisfiesAll(conds, alpha, axioms);

        assertTrue("The answer should not be empty when there are satisfiers", !answers.isEmpty());

    }

    @Test
    public void testJSStateSatisfiesTAm() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();

        JSPredicateForm t = new JSPredicateForm("p(x, y, z) :- q(x, y)", Arrays.asList("x", "y", "z"), Arrays.asList());

        state.addElementsToState(Arrays.asList(new JSPredicateForm("q(a, b)", Arrays.asList("x", "y"), Arrays.asList()), new JSPredicateForm("p(a, b, c)", Arrays.asList("x", "y", "z"), Arrays.asList("d"))));

        JSListSubstitution answers = state.satisfiesTAm(t, alpha);

        assertFalse("There should be no answer when the goal does not match any atom in the state", answers.isEmpty());

        assertEquals("The first answer should match the goal", t.head().applySubstitutionTA(answers.elementAt(0)), t);

        JSState state2 = new JSState();
        state2.addElementsToState(Arrays.asList(new JSPredicateForm("q(a, b)", Arrays.asList("x", "y"), Arrays.asList()), new JSPredicateForm("p(a, b, c)", Arrays.asList("x", "y", "z"), Arrays.asList("d")), new JSPredicateForm("p(a, b, e)", Arrays.asList("x", "y", "z"), Arrays.asList("f"))));

        answers = state2.satisfiesTAm(t, alpha);

        assertTrue("There should be more than one answer when there are multiple matches", answers.size() > 1);

        for (int i = 0; i < answers.size(); ++i) {
            assertEquals("The answers should all match the goal", t.head().applySubstitutionTA(answers.elementAt(i)), t);
            assertNotEquals("The answers should be distinct", answers.elementAt(i), answers.elementAt(0));
        }

    }
}