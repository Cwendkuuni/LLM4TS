package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertEquals(0, jsState.size());
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p1", new JSListLogicalAtoms()));
        state1.addElement(new JSPredicateForm("p2", new JSListLogicalAtoms()));

        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("p3", new JSListLogicalAtoms()));
        state2.addElement(new JSPredicateForm("p1", new JSListLogicalAtoms()));

        JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());

        JSState expectedResult = new JSState();
        expectedResult.addElement(new JSPredicateForm("p1", new JSListLogicalAtoms()));
        expectedResult.addElement(new JSPredicateForm("p2", new JSListLogicalAtoms()));
        expectedResult.addElement(new JSPredicateForm("p3", new JSListLogicalAtoms()));

        JSState result = state1.applyOp(op, new JSSubstitution(), new JSListLogicalAtoms(), new JSListLogicalAtoms());

        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); ++i) {
            assertEquals(expectedResult.elementAt(i).toString(), result.elementAt(i).toString());
        }
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1", new JSListLogicalAtoms()));
        state.addElement(new JSPredicateForm("p2", new JSListLogicalAtoms()));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1", new JSListLogicalAtoms()));

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSVariable("x", new JSListLogicalAtoms()));
        alpha.addElement(new JSSubstitutionLiteral(new JSSymbol("true"), new JSListLogicalAtoms()));

        JSListAxioms axioms = new JSListAxioms();
        axioms.addElement(new JSAtomAxiom("p1(x)", new JSListLogicalAtoms()));
        axioms.addElement(new JSAtomAxiom("p1(x) -> p2(x)", new JSListLogicalAtoms()));

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertEquals(2, result.size());
        assertEquals(alpha.elementAt(0), result.elementAt(0));
        assertEquals(alpha.elementAt(1), result.elementAt(1));
    }
}