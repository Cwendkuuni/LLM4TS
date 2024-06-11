
package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTate;
import umd.cs.shop.JSStateTest;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));

        JSState state = new JSState();

        state.addElementsToState(s);

        for (JSPredicateForm el : s.toArray(new JSPredicateForm[0])) {
            assertTrue(state.contains(el));
        }

        assertEquals(s.size() + state.size(), state.size());
    }

    @Test
    public void testApplyOp() {
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("p1"));

        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p2"));

        JSTate expected = new JSTate(new JSState(), addL, delL);

        JSTate result = state.applyOp(new JSOperator(), new JSSubstitution(), addL, delL);

        assertEquals(expected.state, result.state);
        assertEquals(expected.addL, result.addL);
        assertEquals(expected.delL, result.delL);
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1(a)"));
        conds.addElement(new JSPredicateForm("p2(b)"));

        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1(a)"));
        state.addElement(new JSPredicateForm("p2(b)"));

        JSSubstitution emptySubstitution = new JSSubstitution();
        JSSubstitution expectedSubstitution = new JSSubstitution();
        expectedSubstitution.addElement(new JSSubstitutionAssign("a", "1"));
        expectedSubstitution.addElement(new JSSubstitutionAssign("b", "2"));

        JSSubstitution result = state.satisfies(conds, emptySubstitution, new JSListAxioms());

        assertEquals(expectedSubstitution, result);
    }
}