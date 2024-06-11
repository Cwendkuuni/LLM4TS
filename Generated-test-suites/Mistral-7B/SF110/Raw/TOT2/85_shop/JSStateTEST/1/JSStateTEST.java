package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testEmptyConstructor() {
        JSState jsState = new JSState();
        assertEquals(0, jsState.size());
    }

    @Test
    public void testConstructorWithTokenizer() {
        JSState jsState = new JSState(new StreamTokenizer());
        assertEquals(0, jsState.size());
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        jsState.addElementsToState(s);
        assertEquals(s.size(), jsState.size());

        JSState jsState1 = new JSState();
        JSListLogicalAtoms s1 = new JSListLogicalAtoms();
        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        jsState1.addElementsToState(s1);
        jsState1.addElementsToState(s2);
        assertEquals(s1.size() + s2.size(), jsState1.size());

        JSState jsState3 = new JSState();
        JSListLogicalAtoms s3 = new JSListLogicalAtoms();
        jsState3.addElementsToState(s3);
        jsState3.addElementsToState(s3);
        assertEquals(2 * s3.size(), jsState3.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyOpWithNullOperator() {
        JSState jsState = new JSState();
        JSOperator op = null;
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        jsState.applyOp(op, alpha, addL, delL);
    }

    @Test
    public void testSatisfies() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListLogicalAtoms conds1 = new JSListLogicalAtoms();
        JSPredicateForm predicate = new JSPredicateForm("p", new JSSymbol("x"), new JSSymbol("y"), new JSSymbol("z"));
        conds1.addElement(predicate);
        JSListSubstitution satisfiers = axioms.TheoremProver(conds1, jsState, alpha, false);
        assertFalse(satisfiers.fail());
        assertTrue(satisfiers.size() > 0);

        JSListLogicalAtoms conds2 = new JSListLogicalAtoms();
        JSListLogicalAtoms unsatisfiableConds = new JSListLogicalAtoms();
        JSListSubstitution satisfiers2 = axioms.TheoremProver(conds2, jsState, alpha, false);
        assertTrue(satisfiers2.fail());
        assertTrue(satisfiers2.isEmpty());
    }

    @Test
    public void testSatisfiesAll() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListLogicalAtoms conds3 = new JSListLogicalAtoms();
        JSListSubstitution satisfiers3 = axioms.TheoremProver(conds3, jsState, alpha, true);
        assertTrue(satisfiers3.isEmpty());

        JSListLogicalAtoms conds4 = new JSListLogicalAtoms();
        JSListLogicalAtoms conds4_1 = new JSListLogicalAtoms();
        JSListLogicalAtoms conds4_2 = new JSListLogicalAtoms();
        JSListSubstitution satisfiers4 = axioms.TheoremProver(conds4, jsState, alpha, true);
        assertFalse(satisfiers4.fail());
        assertEquals(2, satisfiers4.size());
    }

    @Test
    public void testSatisfiesTAm() {
        JSState jsState = new JSState();
        JSPredicateForm t = new JSPredicateForm("p", new JSSymbol("x"), new JSSymbol("y"), new JSSymbol("z"));
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertTrue(result.isEmpty());

        JSState jsState1 = new JSState();
        JSListLogicalAtoms conds1 = new JSListLogicalAtoms();
        JSPredicateForm predicate1 = new JSPredicateForm("p", new JSSymbol("x"), new JSSymbol("y"), new JSSymbol("z"));
        conds1.addElement(predicate1);
        JSListSubstitution satisfiers1 = axioms.TheoremProver(conds1, jsState1, alpha, false);
        JSListSubstitution answers1 = new JSListSubstitution();
        if (satisfiers1.fail()) {
            answers1.assignFailure();
        } else {
            answers1.addElement(satisfiers1.elementAt(0));
        }
        assertEquals(answers1, jsState1.satisfiesTAm(t, alpha));
    }
}