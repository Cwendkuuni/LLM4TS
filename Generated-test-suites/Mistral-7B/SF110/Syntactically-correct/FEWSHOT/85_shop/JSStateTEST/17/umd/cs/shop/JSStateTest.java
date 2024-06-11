package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSListSubstitution;

public class JSStateTest {

    @Test
    public void testJSState_Constructor() {
        JSState jsState = new JSState();
        assertNull(jsState.getLabel());
        assertTrue(jsState.isEmpty());
    }

    @Test
    public void testJSState_ConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSState jsState = new JSState(tokenizer);
        assertNull(jsState.getLabel());
        assertTrue(jsState.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSState_NullTokenizer() {
        JSState jsState = new JSState(null);
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        list.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
        state.addElementsToState(list);
        assertTrue(state.size() == 2);
        assertTrue(state.contains(new JSPredicateForm("p(x)", new String[]{"x"})));
        assertTrue(state.contains(new JSPredicateForm("q(x)", new String[]{"x"})));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState expected = new JSState();
        JSUtil.flag20("JSState.apply(JSPlan) not implemented yet");
        JSState actual = state.apply(plan);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator("+", new String[]{"p", "q"}, new String[]{"r"}, null, null);
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState expectedState = new JSState();
        JSState expectedAddL = new JSState();
        JSState expectedNAddL = new JSState();
        JSState expectedNDelL = new JSState();

        expectedState.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        expectedState.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
        expectedAddL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        expectedAddL.addElement(new JSPredicateForm("r(x)", new String[]{"x"}));
        expectedNAddL.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
        expectedNDelL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));

        expectedState.addElementsToState(expectedAddL);
        expectedState.addElementsToState(expectedNDelL);

        state.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
        state.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));

        JSState actualState = state.applyOp(op, alpha, addL, delL);

        assertEquals(expectedState.size(), actualState.size());
        for (int i = 0; i < expectedState.size(); ++i) {
            JSPredicateForm expectedElement = expectedState.elementAt(i);
            JSPredicateForm actualElement = actualState.elementAt(i);
            assertEquals(expectedElement.toString(), actualElement.toString());
        }
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();

        JSPredicateForm cond1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm cond2 = new JSPredicateForm("q(x)", new String[]{"x"});
        conds.addElement(cond1);
        conds.addElement(cond2);

        JSPredicateForm axiom1 = new JSPredicateForm("p(a)", new String[]{"a"});
        JSPredicateForm axiom2 = new JSPredicateForm("q(b)", new String[]{"b"});
        axioms.addElement(axiom1);
        axioms.addElement(axiom2);

        JSSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);
        JSSubstitution actual = state.satisfies(conds, alpha, axioms);

        if (satisfiers.fail()) {
            assertEquals(expected.isEmpty(), actual.isEmpty());
        } else {
            assertEquals(satisfiers.size(), actual.size());
            for (int i = 0; i < satisfiers.size(); ++i) {
                JSPredicateForm satisfier = satisfiers.elementAt(i).head();
                JSPredicateForm actualElement = actual.elementAt(i).head();
                assertEquals(satisfier.toString(), actualElement.toString());
            }
        }
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expected = new JSListSubstitution();
        JSListSubstitution satisfiers = new JSListSubstitution();

        JSPredicateForm cond1 = new JSPredicateForm("p(x)", new String[]{"x"});
        JSPredicateForm cond2 = new JSPredicateForm("q(x)", new String[]{"x"});
        conds.addElement(cond1);
        conds.addElement(cond2);

        JSPredicateForm axiom1 = new JSPredicateForm("p(a)", new String[]{"a"});
        JSPredicateForm axiom2 = new JSPredicateForm("q(b)", new String[]{"b"});
        JSPredicateForm axiom3 = new JSPredicateForm("p(c)", new String[]{"c"});
        axioms.addElement(axiom1);
        axioms.addElement(axiom2);
        axioms.addElement(axiom3);

        satisfiers = axioms.TheoremProver(conds, state, alpha, true);

        JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);

        assertEquals(satisfiers.size(), actual.size());
        for (int i = 0; i < satisfiers.size(); ++i) {
            JSSubstitution satisfier = satisfiers.elementAt(i);
            JSSubstitution actualElement = actual.elementAt(i);
            assertEquals(satisfier.size(), actualElement.size());
            for (int j = 0; j < satisfier.size(); ++j) {
                JSPredicateForm satisfierElement = satisfier.elementAt(j).head();
                JSPredicateForm actualElement = actualElement.head();
                assertEquals(satisfierElement.toString(), actualElement.toString());
            }
        }
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm goal = new JSPredicateForm("p(x)", new String[]{"x"});
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution expected = new JSListSubstitution();

        JSPredicateForm atom1 = new JSPredicateForm("p(a)", new String[]{"a"});
        JSPredicateForm atom2 = new JSPredicateForm("p(b)", new String[]{"b"});
        expected.addElement(new JSSubstitution(new JSPredicateForm("x", new String[]{"a"})));
        expected.addElement(new JSSubstitution(new JSPredicateForm("x", new String[]{"b"})));

        JSListSubstitution actual = state.satisfiesTAm(goal, alpha);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            JSSubstitution expectedElement = expected.elementAt(i);
            JSSubstitution actualElement = actual.elementAt(i);
            assertEquals(expectedElement.size(), actualElement.size());
            for (int j = 0; j < expectedElement.size(); ++j) {
                JSPredicateForm expectedElementPredicate = expectedElement.elementAt(j).head();
                JSPredicateForm actualElementPredicate = actualElement.elementAt(j).head();
                assertEquals(expectedElementPredicate.toString(), actualElementPredicate.toString());
            }
        }
    }
}