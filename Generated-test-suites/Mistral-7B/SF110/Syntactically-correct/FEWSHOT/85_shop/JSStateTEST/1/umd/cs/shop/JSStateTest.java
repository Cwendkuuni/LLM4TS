package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSState() {
        JSState state = new JSState();
        assertNull(state.getLabel());
        assertTrue(state.size() == 0);
    }

    @Test
    public void testJSStateWithLabel() {
        JSState state = new JSState("testLabel");
        assertEquals("testLabel", state.getLabel());
        assertTrue(state.size() == 0);
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"});
        state.addElementsToState(new JSListLogicalAtoms(atom1, atom2));
        List<JSPredicateForm> expected = new ArrayList<>();
        expected.add(atom1);
        expected.add(atom2);
        assertTrue(state.size() == expected.size());
        for (int i = 0; i < state.size(); ++i) {
            assertTrue(state.elementAt(i).equals(expected.get(i)));
        }
    }

    @Test
    public void testContains() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        state.addElement(atom1);
        assertTrue(state.contains(atom1));
        assertFalse(state.contains(new JSPredicateForm("q(y)", new String[]{"y"})));
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        state1.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));

        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("r(y)", new String[]{"y"}));

        JSOperator op = new JSOperator("->", new String[]{"p(x)", "q(x)"}, new String[]{"r(y)"});
        JSListLogicalAtoms addL = new JSListLogicalAtoms(new JSPredicateForm("s(z)", new String[]{"z"}));
        JSListLogicalAtoms delL = new JSListLogicalAtoms(new JSPredicateForm("s(x)", new String[]{"x"}));

        JSState expected = new JSState();
        expected.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        expected.addElement(new JSPredicateForm("s(z)", new String[]{"z"}));
        expected.addElementsToState(new JSListLogicalAtoms(new JSPredicateForm("q(x)", new String[]{"x"})));

        JSState result = state1.applyOp(op, null, addL, delL);
        assertTrue(result.size() == expected.size());
        for (int i = 0; i < result.size(); ++i) {
            assertTrue(result.elementAt(i).equals(expected.elementAt(i)));
        }
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        state.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution answer = state.satisfies(conditions, alpha, new JSListAxioms());
        assertTrue(answer.size() > 0);
        assertTrue(answer.elementAt(0).size() > 0);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        state.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}),
                new JSPredicateForm("q(x)", new String[]{"x"}));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution answers = state.satisfiesAll(conditions, alpha, axioms);
        assertTrue(answers.size() > 0);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        state.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));

        JSPredicateForm t = new JSPredicateForm("p(x) -> q(x)", new String[]{"x"});
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        assertTrue(answers.size() > 0);
    }
}