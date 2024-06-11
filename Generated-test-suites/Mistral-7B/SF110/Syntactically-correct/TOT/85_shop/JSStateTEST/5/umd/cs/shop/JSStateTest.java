
package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1", new JSSymbolicConstant("x"), new JSSymbolicConstant("1")));
        s.addElement(new JSPredicateForm("p2", new JSSymbolicConstant("x"), new JSSymbolicConstant("2")));

        JSState state = new JSState();
        state.addElementsToState(s);

        assertTrue(state.contains(new JSPredicateForm("p1", new JSSymbolicConstant("x"), new JSSymbolicConstant("1"))));
        assertTrue(state.contains(new JSPredicateForm("p2", new JSSymbolicConstant("x"), new JSSymbolicConstant("2"))));
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1", new JSSymbolicConstant("x"), new JSSymbolicConstant("1")));
        state.addElement(new JSPredicateForm("p2", new JSSymbolicConstant("x"), new JSSymbolicConstant("2")));

        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p3", new JSSymbolicConstant("x"), new JSSymbolicConstant("3")));
        addL.addElement(new JSPredicateForm("p4", new JSSymbolicConstant("x"), new JSSymbolicConstant("4")));

        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("p1", new JSSymbolicConstant("x"), new JSSymbolicConstant("1")));

        JSOperator op = new JSOperator(new JSFunctionSymbol("add"), new JSFunctionSymbol("subtract"), new JSFunctionSymbol("constant"), new JSListSymbol("list"));
        JSListLogicalAtoms addList = op.applySubstitutionListLogicalAtoms(new JSSubstitution());
        JSListLogicalAtoms delList = op.deleteList();

        JSTState result = state.applyOp(op, new JSSubstitution(), addL, delL);

        assertEquals(3, result.getState().size());
        assertEquals(4, result.getAddList().size());
        assertEquals(1, result.getDelList().size());
        assertEquals(new JSPredicateForm("p3", new JSSymbolicConstant("x"), new JSSymbolicConstant("3")), result.getAddList().elementAt(0));
        assertEquals(new JSPredicateForm("p4", new JSSymbolicConstant("x"), new JSSymbolicConstant("4")), result.getAddList().elementAt(1));
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1", new JSSymbolicConstant("x"), new JSSymbolicConstant("1")));
        state.addElement(new JSPredicateForm("p2", new JSSymbolicConstant("x"), new JSSymbolicConstant("2")));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1(x, 1)", new JSSymbolicConstant("x"), new JSSymbolicConstant("1")));
        conds.addElement(new JSPredicateForm("p2(x, 2)", new JSSymbolicConstant("x"), new JSSymbolicConstant("2")));

        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSSubstitutionAssignment(new JSSymbolicConstant("x"), new JSSymbolicConstant("3")));

        JSSubstitution result = new JSState(state, new JSListLogicalAtoms(), new JSListLogicalAtoms()).satisfies(conds, expected, new JSListAxioms());

        assertEquals(expected, result);
    }
}
