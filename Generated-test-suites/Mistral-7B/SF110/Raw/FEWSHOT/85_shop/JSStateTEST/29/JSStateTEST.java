package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstruction() {
        JSState state = new JSState();
        assertEquals(0, state.size());
        assertNull(state.getLabel());
    }

    @Test
    public void testJSStateConstructionWithTokenizer() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        assertEquals(0, state.size());
        assertNull(state.getLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateAddElementsToStateNullList() {
        JSState state = new JSState();
        state.addElementsToState(null);
    }

    @Test
    public void testJSStateAddElementsToState() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"}, new String[]{"q"}, new int[0]);

        state.addElementsToState(new JSListLogicalAtoms(atom1, atom2));

        List<JSPredicateForm> expected = new ArrayList<>();
        expected.add(atom1);
        expected.add(atom2);

        assertEquals(expected.size(), state.size());
        for (int i = 0; i < state.size(); ++i) {
            assertEquals(state.elementAt(i), expected.get(i));
        }
    }

    @Test
    public void testJSStateApplyOpNullLists() {
        JSState state = new JSState();
        JSOperator op = new JSOperator("op", new String[0], new int[0], null);
        JSListLogicalAtoms addL = null;
        JSListLogicalAtoms delL = null;

        try {
            state.applyOp(op, new JSSubstitution(), addL, delL);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        addL = null;
        delL = new JSListLogicalAtoms();

        try {
            state.applyOp(op, new JSSubstitution(), addL, delL);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        addL = new JSListLogicalAtoms();
        delL = null;

        JSState expectedState = new JSState();
        JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();

        try {
            state.applyOp(op, new JSSubstitution(), addL, delL);
            assertEquals(expectedState, state);
            assertEquals(expectedAddL, addL);
            assertEquals(expectedDelL, delL);
        } finally {
            // Clean up
            addL = null;
            delL = null;
            state = null;
        }
    }

    @Test
    public void testJSStateApplyOp() {
        JSState state = new JSState();
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSPredicateForm atom2 = new JSPredicateForm("q(y)", new String[]{"y"}, new String[]{"q"}, new int[0]);
        JSPredicateForm atom3 = new JSPredicateForm("r(z)", new String[]{"z"}, new String[]{"r"}, new int[0]);

        state.addElementsToState(new JSListLogicalAtoms(atom1, atom2));

        JSOperator op = new JSOperator("op", new String[0], new int[0], null);
        JSListLogicalAtoms addL = new JSListLogicalAtoms(atom3);
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSState expectedState = new JSState();
        JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms(atom3);
        JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();

        JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(expectedState, result);
        assertEquals(expectedAddL, addL);
        assertEquals(expectedDelL, delL);
    }

    @Test
    public void testJSStateSatisfiesNullLists() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = null;
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertNull(result);

        conds = new JSListLogicalAtoms();

        result = state.satisfies(conds, alpha, axioms);
        assertTrue(result.isEmpty());

        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSPredicateForm atom2 = new JSPredicateForm("p(y)", new String[]{"y"}, new String[]{"p"}, new int[0]);

        state.addElementsToState(new JSListLogicalAtoms(atom1, atom2));
        conds.addElement(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]));

        result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(atom1, result.elementAt(0));
    }

    @Test
    public void testJSStateSatisfiesAllNullLists() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = null;
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(0, result.size());

        conds = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]));

        result = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(0, result.size());

        state.addElementsToState(new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0])));

        result = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(1, result.size());
        assertEquals(alpha, result.elementAt(0));
    }

    @Test
    public void testJSStateSatisfiesTAmNullLists() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());

        state.addElementsToState(new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0])));

        result = state.satisfiesTAm(t, alpha);
        assertEquals(1, result.size());
        assertEquals(alpha, result.elementAt(0));
    }

    @Test
    public void testJSStateSatisfiesTAmAtomDoesNotMatch() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSPredicateForm atom1 = new JSPredicateForm("q(x)", new String[]{"x"}, new String[]{"q"}, new int[0]);
        JSSubstitution alpha = new JSSubstitution();

        state.addElementsToState(new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0])));

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSStateSatisfiesTAmAtomMatches() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSPredicateForm atom1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0]);
        JSSubstitution alpha = new JSSubstitution();

        state.addElementsToState(new JSListLogicalAtoms(new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"p"}, new int[0])));

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertEquals(1, result.size());
        assertEquals(alpha, result.elementAt(0));
    }
}