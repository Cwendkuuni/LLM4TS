package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmpty() {
        JSState state = new JSState();
        assertTrue(state.isEmpty());
        assertEquals(0, state.size());
    }

    @Test
    public void testJSStateWithLabel() {
        JSState state = new JSState("label");
        assertEquals("label", state.getLabel());
    }

    @Test
    public void testJSStateAddElementsToState() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"});
        state.addElementsToState(new JSListLogicalAtoms(atom1, atom2));
        assertTrue(state.contains(atom1));
        assertTrue(state.contains(atom2));
        assertEquals(2, state.size());
    }

    @Test
    public void testJSStateContains() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"});
        state.addElement(atom1);
        assertTrue(state.contains(atom1));
        assertFalse(state.contains(atom2));
    }

    @Test
    public void testJSStateApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("q(y)", new String[]{"y"}));
        JSOperator op = new JSOperator("->", new String[]{"p(x)", "q(x)"});
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("r(x)", new String[]{"x"}));
        delL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));

        JSState expected = new JSState();
        expected.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        expected.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
        expected.addElement(new JSPredicateForm("r(x)", new String[]{"x"}));

        JSState result = state1.applyOp(op, new JSSubstitution(), addL, delL);
        assertTrue(expected.equals(result));
    }

    @Test
    public void testJSStateSatisfies() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(x)", new String[]{"x"});
        state.addElement(atom1);
        state.addElement(atom2);

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = new JSSubstitution();
        beta.addElement(new JSPredicateForm("x = 1", new String[0]));

        JSListLogicalAtoms conds = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}), new JSPredicateForm("q(x)", new String[]{"x"}));
        JSSubstitution result = state.satisfies(conds, alpha, new JSListAxioms());
        assertTrue(result.isEmpty());

        state.addElement(new JSPredicateForm("r(1)", new String[0]));
        result = state.satisfies(conds, beta, new JSListAxioms());
        assertTrue(result.size() > 0);
    }

    @Test
    public void testJSStateSatisfiesAll() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(x)", new String[]{"x"});
        state.addElement(atom1);
        state.addElement(atom2);

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = new JSSubstitution();
        beta.addElement(new JSPredicateForm("x = 1", new String[0]));

        JSListLogicalAtoms conds = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}), new JSPredicateForm("q(x)", new String[]{"x"}));
        JSListSubstitution result = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertTrue(result.isEmpty());

        state.addElement(new JSPredicateForm("r(1)", new String[0]));
        result = state.satisfiesAll(conds, beta, new JSListAxioms());
        assertTrue(result.size() > 0);
    }

    @Test
    public void testJSStateSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"});
        state.addElement(atom1);
        state.addElement(atom2);

        JSPredicateForm goal = new JSPredicateForm("r(x)", new String[]{"x"});
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(goal, alpha);
        assertTrue(result.isEmpty());

        state.addElement(new JSPredicateForm("r(1)", new String[0]));
        result = state.satisfiesTAm(goal, alpha);
        assertTrue(result.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateNullAddList() {
        JSState state = new JSState();
        JSOperator op = new JSOperator("->", new String[0]);
        JSListLogicalAtoms addL = null;
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        state.applyOp(op, new JSSubstitution(), addL, delL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateNullDeleteList() {
        JSState state = new JSState();
        JSOperator op = new JSOperator("->", new String[0]);
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = null;

        state.applyOp(op, new JSSubstitution(), addL, delL);
    }
}